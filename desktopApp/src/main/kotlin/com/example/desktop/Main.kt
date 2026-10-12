package com.example.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val Cream = Color(0xFFFFF8F0)
private val Orange = Color(0xFFF6B26B)
private val Brown = Color(0xFFB88962)
private val Dark = Color(0xFF4A3426)
private val Surface = Color(0xFFFFF1E2)

private data class PcpResult(val sequence: List<Int>, val top: String, val bottom: String)

@Composable
fun MainScreen() {
    var a by remember { mutableStateOf(listOf("a", "ba")) }
    var b by remember { mutableStateOf(listOf("ab", "a")) }
    var depth by remember { mutableIntStateOf(5) }
    var tab by remember { mutableIntStateOf(0) }
    var result by remember { mutableStateOf<PcpResult?>(null) }
    var status by remember { mutableStateOf("Ready to search") }
    var running by remember { mutableStateOf(false) }
    var checked by remember { mutableLongStateOf(0L) }
    val scope = rememberCoroutineScope()

    MaterialTheme(colorScheme = lightColorScheme(primary = Brown, secondary = Orange, background = Cream, surface = Surface, onSurface = Dark)) {
        Column(Modifier.fillMaxSize().background(Cream).verticalScroll(rememberScrollState()).padding(28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Card(colors = CardDefaults.cardColors(containerColor = Surface), shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.fillMaxWidth().padding(22.dp)) {
                    Text("PCP SOLVER", color = Brown, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("Post Correspondence Problem", color = Dark, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    Text("Interactive PCP Visual Solver & Explorer", color = Dark.copy(alpha = .72f), fontSize = 15.sp)
                    Spacer(Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Solver", "How It Works", "Complexity").forEachIndexed { i, name ->
                            FilterChip(selected = tab == i, onClick = { tab = i }, label = { Text(name) })
                        }
                    }
                }
            }
            when (tab) {
                0 -> {
                    Card(colors = CardDefaults.cardColors(containerColor = Surface), shape = RoundedCornerShape(18.dp)) {
                        Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("Tile pairs", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = Dark)
                            Text("Edit matching positions in set A and set B.", color = Dark.copy(alpha = .7f))
                            a.indices.forEach { i ->
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("${i + 1}.", color = Brown, fontWeight = FontWeight.Bold)
                                    OutlinedTextField(value = a[i], onValueChange = { v -> a = a.toMutableList().also { it[i] = v }; result = null }, label = { Text("A") }, modifier = Modifier.weight(1f), singleLine = true, enabled = !running)
                                    OutlinedTextField(value = b[i], onValueChange = { v -> b = b.toMutableList().also { it[i] = v }; result = null }, label = { Text("B") }, modifier = Modifier.weight(1f), singleLine = true, enabled = !running)
                                    TextButton(onClick = { if (a.size > 1 && !running) { a = a.filterIndexed { idx, _ -> idx != i }; b = b.filterIndexed { idx, _ -> idx != i } } }, enabled = !running && a.size > 1) { Text("Remove") }
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(onClick = { if (a.size < 6 && !running) { a = a + ""; b = b + "" } }, enabled = !running && a.size < 6) { Text("+ Add tile pair") }
                                OutlinedButton(onClick = { a = listOf("a", "ba"); b = listOf("ab", "a"); depth = 5; result = null; status = "Preset loaded" }, enabled = !running) { Text("Load preset") }
                            }
                            Text("Maximum search depth: $depth", fontWeight = FontWeight.SemiBold)
                            Slider(value = depth.toFloat(), onValueChange = { depth = it.toInt().coerceIn(1, 8) }, valueRange = 1f..8f, steps = 6, enabled = !running)
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(onClick = {
                                    running = true; result = null; checked = 0; status = "Searching…"
                                    scope.launch {
                                        val cleanA = a.map { it.trim() }; val cleanB = b.map { it.trim() }
                                        if (cleanA.any { it.isEmpty() } || cleanB.any { it.isEmpty() }) {
                                            status = "Enter non-empty strings for every tile."; running = false
                                        } else {
                                            var found: PcpResult? = null
                                            var count = 0L
                                            suspend fun search(prefix: List<Int>) {
                                                if (found != null || prefix.size >= depth) return
                                                for (i in cleanA.indices) {
                                                    if (found != null) break
                                                    val seq = prefix + (i + 1)
                                                    val top = seq.joinToString("") { cleanA[it - 1] }
                                                    val bottom = seq.joinToString("") { cleanB[it - 1] }
                                                    count++
                                                    checked = count
                                                    if (top == bottom) { found = PcpResult(seq, top, bottom); break }
                                                    if (top.startsWith(bottom) || bottom.startsWith(top)) search(seq)
                                                    if (count % 100L == 0L) delay(1)
                                                }
                                            }
                                            search(emptyList())
                                            result = found
                                            status = if (found != null) "Matching sequence found" else "No match found within depth $depth"
                                            running = false
                                        }
                                    }
                                }, enabled = !running) { Text("Start search") }
                                OutlinedButton(onClick = { result = null; checked = 0; status = "Ready to search" }, enabled = !running) { Text("Reset") }
                            }
                        }
                    }
                    Card(colors = CardDefaults.cardColors(containerColor = Surface), shape = RoundedCornerShape(18.dp)) {
                        Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Search visualization", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = Dark)
                            Text(status, color = Brown, fontWeight = FontWeight.SemiBold)
                            Text("Sequences checked: $checked")
                            result?.let {
                                HorizontalDivider()
                                Text("Solution: [${it.sequence.joinToString(", ")}]", fontWeight = FontWeight.Bold, fontSize = 19.sp)
                                Text("A concatenation: ${it.top}")
                                Text("B concatenation: ${it.bottom}")
                                Text("Both concatenations are equal.", color = Color(0xFF38734A), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                1 -> InfoCard("How it works", "Each tile has a top string from A and a bottom string from B. The solver explores index sequences up to the chosen depth. A solution is found when both concatenated strings are exactly equal. Prefix pruning skips sequences that can no longer match.")
                else -> InfoCard("Search complexity", "For n tile pairs and maximum depth d, the brute-force search space is n + n² + … + nᵈ. The number of candidate sequences grows exponentially, so use a small tile set and depth for practical exploration.")
            }
            Text("PCP Solver • Darshak K. Bisane • CM25D004", color = Brown, fontSize = 12.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Composable
private fun InfoCard(title: String, body: String) {
    Card(colors = CardDefaults.cardColors(containerColor = Surface), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.fillMaxWidth().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Dark)
            Text(body, color = Dark, fontSize = 16.sp, lineHeight = 24.sp)
        }
    }
}

fun main() = androidx.compose.ui.window.application {
    Window(onCloseRequest = ::exitApplication, title = "PCP Solver") { MainScreen() }
}
