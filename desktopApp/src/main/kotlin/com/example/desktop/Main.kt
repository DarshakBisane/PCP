package com.example.desktop

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

private val Cream = Color(0xFFFFF8F0)
private val Orange = Color(0xFFF6B26B)
private val SoftOrange = Color(0xFFFFE4C5)
private val Brown = Color(0xFFB88962)
private val Dark = Color(0xFF4A3426)
private val Muted = Color(0xFF8B7565)
private val Surface = Color(0xFFFFFDF9)
private val Border = Color(0xFFE8D7C5)
private val Green = Color(0xFF34734B)
private val GreenSoft = Color(0xFFE7F4E9)
private val Red = Color(0xFF9B3D31)

private data class PcpResult(val sequence: List<Int>, val top: String, val bottom: String)
private data class Preset(val name: String, val top: List<String>, val bottom: List<String>, val depth: Int)

private val presets = listOf(
    Preset("Starter example", listOf("a", "ba"), listOf("ab", "a"), 5),
    Preset("Single tile match", listOf("ab", "a"), listOf("ab", "b"), 4),
    Preset("No quick match", listOf("a", "bb"), listOf("b", "aa"), 5)
)

@Composable
fun MainScreen() {
    var a by remember { mutableStateOf(listOf("a", "ba")) }
    var b by remember { mutableStateOf(listOf("ab", "a")) }
    var depth by remember { mutableIntStateOf(5) }
    var speed by remember { mutableIntStateOf(120) }
    var tab by remember { mutableIntStateOf(0) }
    var result by remember { mutableStateOf<PcpResult?>(null) }
    var status by remember { mutableStateOf("Ready to search") }
    var running by remember { mutableStateOf(false) }
    var paused by remember { mutableStateOf(false) }
    var checked by remember { mutableLongStateOf(0L) }
    var currentSequence by remember { mutableStateOf<List<Int>>(emptyList()) }
    var currentTop by remember { mutableStateOf("") }
    var currentBottom by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }
    var searchJob by remember { mutableStateOf<Job?>(null) }
    val scope = rememberCoroutineScope()
    val numberFormat = remember { NumberFormat.getNumberInstance(Locale.US) }

    fun resetSearch() {
        searchJob?.cancel()
        searchJob = null
        running = false
        paused = false
        result = null
        checked = 0
        currentSequence = emptyList()
        currentTop = ""
        currentBottom = ""
        status = "Ready to search"
        validationError = null
    }

    fun loadPreset(preset: Preset) {
        resetSearch()
        a = preset.top
        b = preset.bottom
        depth = preset.depth
        status = "${preset.name} loaded"
    }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Dark,
            onPrimary = Color.White,
            secondary = Orange,
            background = Cream,
            surface = Surface,
            onSurface = Dark,
            outline = Border
        )
    ) {
        Box(Modifier.fillMaxSize().background(Cream)) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 30.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(Modifier.widthIn(max = 1120.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    HeaderCard(tab = tab, onTabChange = { tab = it })

                    AnimatedContent(
                        targetState = tab,
                        label = "main-page-transition",
                        transitionSpec = {
                            (fadeIn(tween(240, easing = FastOutSlowInEasing)) +
                                slideInVertically(tween(240, easing = FastOutSlowInEasing)) { it / 14 }) togetherWith
                                (fadeOut(tween(150)) + slideOutVertically(tween(150)) { -it / 24 })
                        }
                    ) { activeTab ->
                    when (activeTab) {
                        0 -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(18.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(Modifier.weight(1.15f), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                                    TileEditor(
                                        a = a,
                                        b = b,
                                        enabled = !running,
                                        validationError = validationError,
                                        onChangeA = { i, value -> a = a.toMutableList().also { it[i] = value }; result = null; validationError = null },
                                        onChangeB = { i, value -> b = b.toMutableList().also { it[i] = value }; result = null; validationError = null },
                                        onAdd = { if (a.size < 6) { a = a + ""; b = b + "" } },
                                        onRemove = { i -> if (a.size > 1) { a = a.filterIndexed { idx, _ -> idx != i }; b = b.filterIndexed { idx, _ -> idx != i } } },
                                        onPreset = ::loadPreset
                                    )
                                    SearchSettings(
                                        depth = depth,
                                        speed = speed,
                                        enabled = !running,
                                        onDepth = { depth = it; result = null },
                                        onSpeed = { speed = it }
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Button(
                                            onClick = {
                                                val cleanA = a.map { it.trim() }
                                                val cleanB = b.map { it.trim() }
                                                val error = when {
                                                    cleanA.size != cleanB.size -> "Tile sets A and B must have the same number of entries."
                                                    cleanA.any { it.isEmpty() } || cleanB.any { it.isEmpty() } -> "Every tile must contain at least one character."
                                                    cleanA.size !in 1..6 -> "Use between 1 and 6 tile pairs."
                                                    else -> null
                                                }
                                                if (error != null) {
                                                    validationError = error
                                                    status = "Check the tile inputs"
                                                } else {
                                                    resetSearch()
                                                    running = true
                                                    status = "Searching candidate sequences…"
                                                    searchJob = scope.launch {
                                                        var found: PcpResult? = null
                                                        var count = 0L
                                                        try {
                                                            suspend fun search(prefix: List<Int>) {
                                                                if (found != null || prefix.size >= depth) return
                                                                for (index in cleanA.indices) {
                                                                    while (paused) delay(60)
                                                                    if (!running || found != null) return
                                                                    val sequence = prefix + (index + 1)
                                                                    val top = sequence.joinToString("") { cleanA[it - 1] }
                                                                    val bottom = sequence.joinToString("") { cleanB[it - 1] }
                                                                    count++
                                                                    checked = count
                                                                    currentSequence = sequence
                                                                    currentTop = top
                                                                    currentBottom = bottom
                                                                    if (top == bottom) {
                                                                        found = PcpResult(sequence, top, bottom)
                                                                        return
                                                                    }
                                                                    if (top.startsWith(bottom) || bottom.startsWith(top)) search(sequence)
                                                                    if (count % 8L == 0L) delay(speed.toLong().coerceAtLeast(1L))
                                                                    if (found != null) return
                                                                }
                                                            }
                                                            search(emptyList())
                                                            result = found
                                                            status = if (found != null) "Solution found" else "No solution found within depth $depth"
                                                        } catch (_: CancellationException) {
                                                            status = "Search reset"
                                                        } finally {
                                                            running = false
                                                            paused = false
                                                        }
                                                    }
                                                }
                                            },
                                            enabled = !running,
                                            shape = RoundedCornerShape(10.dp),
                                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                                        ) { Text("▶  Run brute-force search", fontWeight = FontWeight.SemiBold) }
                                        OutlinedButton(
                                            onClick = { resetSearch() },
                                            shape = RoundedCornerShape(10.dp),
                                            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp)
                                        ) { Text("Reset") }
                                    }
                                }
                                Column(Modifier.weight(0.85f), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                                    SearchVisualization(
                                        status = status,
                                        running = running,
                                        paused = paused,
                                        checked = checked,
                                        depth = depth,
                                        currentSequence = currentSequence,
                                        currentTop = currentTop,
                                        currentBottom = currentBottom,
                                        result = result,
                                        onPause = { val nextPaused = !paused; paused = nextPaused; status = if (nextPaused) "Search paused" else "Searching candidate sequences…" }
                                    )
                                    TipCard()
                                }
                            }
                        }
                        1 -> HowItWorksPage()
                        else -> ComplexityPage()
                    }
                    }
                    Text(
                        "PCP Solver  •  Interactive PCP Visual Solver & Explorer  •  Darshak K. Bisane / CM25D004",
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        color = Muted,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderCard(tab: Int, onTabChange: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Border)
    ) {
        Column(Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 22.dp, bottom = 8.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(Modifier.background(Orange, RoundedCornerShape(7.dp)).padding(horizontal = 10.dp, vertical = 5.dp)) {
                            Text("PCP", color = Dark, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                        }
                        Text("PCP Solver", color = Dark, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    }
                    Text("Post's Correspondence Problem", color = Muted, fontSize = 14.sp)
                    Text("Explore tile sequences and watch the matching process step by step.", color = Dark.copy(alpha = .82f), fontSize = 14.sp)
                }
                Box(Modifier.background(Cream, RoundedCornerShape(9.dp)).border(1.dp, Border, RoundedCornerShape(9.dp)).padding(horizontal = 12.dp, vertical = 8.dp)) {
                    Text("TAE-1  ·  PBL", color = Dark, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(Modifier.height(18.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Solver", "How It Works", "Complexity").forEachIndexed { i, label ->
                    if (tab == i) {
                        Button(onClick = { onTabChange(i) }, shape = RoundedCornerShape(topStart = 9.dp, topEnd = 9.dp, bottomStart = 4.dp, bottomEnd = 4.dp), contentPadding = PaddingValues(horizontal = 17.dp, vertical = 10.dp)) {
                            Text(label, fontWeight = FontWeight.SemiBold)
                        }
                    } else {
                        TextButton(onClick = { onTabChange(i) }, contentPadding = PaddingValues(horizontal = 17.dp, vertical = 10.dp)) {
                            Text(label, color = Muted)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionCard(title: String, subtitle: String? = null, trailing: (@Composable () -> Unit)? = null, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().animateContentSize(tween(220, easing = FastOutSlowInEasing)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Border)
    ) {
        Column(Modifier.fillMaxWidth().animateContentSize(tween(220, easing = FastOutSlowInEasing)).padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(title, color = Dark, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    if (subtitle != null) Text(subtitle, color = Muted, fontSize = 12.sp, lineHeight = 17.sp)
                }
                if (trailing != null) trailing()
            }
            content()
        }
    }
}

@Composable
private fun TileEditor(
    a: List<String>,
    b: List<String>,
    enabled: Boolean,
    validationError: String?,
    onChangeA: (Int, String) -> Unit,
    onChangeB: (Int, String) -> Unit,
    onAdd: () -> Unit,
    onRemove: (Int) -> Unit,
    onPreset: (Preset) -> Unit
) {
    SectionCard("PCP tile sets", "Each row is one tile pair. Both strings are concatenated in the selected index order.", trailing = {
        Text("${a.size} / 6 pairs", color = Muted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }) {
        Text("QUICK PRESETS", color = Brown, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(7.dp), modifier = Modifier.fillMaxWidth()) {
            presets.forEach { preset ->
                OutlinedButton(
                    onClick = { onPreset(preset) },
                    enabled = enabled,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 9.dp),
                    shape = RoundedCornerShape(9.dp)
                ) { Text(preset.name, fontSize = 11.sp, maxLines = 1) }
            }
        }
        Row(Modifier.fillMaxWidth().background(Cream, RoundedCornerShape(8.dp)).padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("#", color = Muted, fontSize = 11.sp, modifier = Modifier.width(30.dp))
            Text("TOP STRING · A", color = Brown, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(10.dp))
            Text("BOTTOM STRING · B", color = Brown, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(42.dp))
        }
        a.indices.forEach { i ->
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("${i + 1}", color = Brown, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(30.dp), textAlign = TextAlign.Center)
                OutlinedTextField(
                    value = a[i], onValueChange = { onChangeA(i, it) }, enabled = enabled,
                    modifier = Modifier.weight(1f), singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 15.sp),
                    placeholder = { Text("e.g. a", color = Muted) },
                    shape = RoundedCornerShape(9.dp)
                )
                OutlinedTextField(
                    value = b[i], onValueChange = { onChangeB(i, it) }, enabled = enabled,
                    modifier = Modifier.weight(1f), singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 15.sp),
                    placeholder = { Text("e.g. ba", color = Muted) },
                    shape = RoundedCornerShape(9.dp)
                )
                TextButton(onClick = { onRemove(i) }, enabled = enabled && a.size > 1, contentPadding = PaddingValues(horizontal = 4.dp)) {
                    Text("Remove", color = if (enabled && a.size > 1) Red else Muted, fontSize = 11.sp)
                }
            }
        }
        if (validationError != null) {
            Text(validationError, color = Red, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().background(Color(0xFFFFEFEB), RoundedCornerShape(8.dp)).padding(10.dp))
        }
        OutlinedButton(onClick = onAdd, enabled = enabled && a.size < 6, shape = RoundedCornerShape(9.dp), contentPadding = PaddingValues(horizontal = 13.dp, vertical = 8.dp)) {
            Text("+  Add tile pair")
        }
    }
}

@Composable
private fun SearchSettings(depth: Int, speed: Int, enabled: Boolean, onDepth: (Int) -> Unit, onSpeed: (Int) -> Unit) {
    SectionCard("Search controls", "Bounded brute-force search with prefix pruning.") {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Maximum search depth", color = Dark, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Box(Modifier.background(SoftOrange, RoundedCornerShape(7.dp)).padding(horizontal = 10.dp, vertical = 5.dp)) {
                Text("$depth steps", color = Dark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
        Slider(value = depth.toFloat(), onValueChange = { onDepth(it.toInt().coerceIn(1, 8)) }, valueRange = 1f..8f, steps = 6, enabled = enabled, colors = SliderDefaults.colors(thumbColor = Dark, activeTrackColor = Orange, inactiveTrackColor = SoftOrange))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("1 · Faster", color = Muted, fontSize = 11.sp)
            Text("8 · More candidates", color = Muted, fontSize = 11.sp)
        }
        HorizontalDivider(color = Border)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Animation speed", color = Dark, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(if (speed <= 30) "Fast" else if (speed <= 140) "Normal" else "Slow", color = Brown, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
        Slider(value = speed.toFloat(), onValueChange = { onSpeed(it.toInt()) }, valueRange = 10f..300f, enabled = true, colors = SliderDefaults.colors(thumbColor = Dark, activeTrackColor = Orange, inactiveTrackColor = SoftOrange))
        Text("Slower animation makes individual search steps easier to observe.", color = Muted, fontSize = 12.sp, lineHeight = 17.sp)
    }
}

@Composable
private fun SearchVisualization(
    status: String,
    running: Boolean,
    paused: Boolean,
    checked: Long,
    depth: Int,
    currentSequence: List<Int>,
    currentTop: String,
    currentBottom: String,
    result: PcpResult?,
    onPause: () -> Unit
) {
    SectionCard("Search visualization", "Live view of the candidate sequence being evaluated.", trailing = {
        Box(Modifier.background(if (result != null) GreenSoft else SoftOrange, RoundedCornerShape(7.dp)).padding(horizontal = 9.dp, vertical = 5.dp)) {
            Text(if (result != null) "SOLVED" else if (running && paused) "PAUSED" else if (running) "RUNNING" else "READY", color = if (result != null) Green else Brown, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
        }
    }) {
        val statusColor by animateColorAsState(
            targetValue = if (result != null) Green else if (running && paused) Brown else Dark,
            animationSpec = tween(260, easing = FastOutSlowInEasing),
            label = "status-color"
        )
        Text(status, color = statusColor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            MetricBox("Sequences checked", NumberFormat.getNumberInstance(Locale.US).format(checked), Modifier.weight(1f))
            MetricBox("Depth limit", depth.toString(), Modifier.weight(1f))
        }
        AnimatedVisibility(
            visible = running,
            enter = fadeIn(tween(180)) + slideInVertically(tween(180)) { -it / 4 },
            exit = fadeOut(tween(140))
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val pulse by animateFloatAsState(
                targetValue = if (paused) 0.28f else 1f,
                animationSpec = tween(350, easing = FastOutSlowInEasing),
                label = "search-progress-emphasis"
            )
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = Orange.copy(alpha = pulse),
                trackColor = SoftOrange
            )
            OutlinedButton(onClick = onPause, shape = RoundedCornerShape(8.dp), contentPadding = PaddingValues(horizontal = 12.dp, vertical = 7.dp)) {
                Text(if (paused) "▶  Resume search" else "Ⅱ  Pause search")
            }
            }
        }
        Text("CURRENT CANDIDATE", color = Brown, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        AnimatedContent(
            targetState = currentSequence,
            label = "candidate-sequence-transition",
            transitionSpec = {
                (fadeIn(tween(130)) + slideInVertically(tween(130)) { it / 5 }) togetherWith fadeOut(tween(90))
            }
        ) { sequence ->
        Box(Modifier.fillMaxWidth().background(Cream, RoundedCornerShape(10.dp)).border(1.dp, Border, RoundedCornerShape(10.dp)).padding(14.dp)) {
            Text(
                if (sequence.isEmpty()) "Waiting for the first sequence…" else sequence.joinToString("  →  ") { "T$it" },
                color = Dark, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 15.sp
            )
        }
        }
        Column(Modifier.fillMaxWidth().animateContentSize(tween(180)).background(Cream, RoundedCornerShape(10.dp)).padding(13.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Text("A", color = Brown, fontWeight = FontWeight.ExtraBold, modifier = Modifier.width(24.dp))
                Text(currentTop.ifEmpty { "Top concatenation appears here" }, color = Dark, fontFamily = FontFamily.Monospace, fontSize = 13.sp)
            }
            HorizontalDivider(color = Border)
            Row(verticalAlignment = Alignment.Top) {
                Text("B", color = Brown, fontWeight = FontWeight.ExtraBold, modifier = Modifier.width(24.dp))
                Text(currentBottom.ifEmpty { "Bottom concatenation appears here" }, color = Dark, fontFamily = FontFamily.Monospace, fontSize = 13.sp)
            }
        }
        AnimatedVisibility(
            visible = result != null,
            enter = fadeIn(tween(260)) + slideInVertically(tween(260)) { it / 5 },
            exit = fadeOut(tween(150))
        ) {
        if (result != null) {
            Column(Modifier.fillMaxWidth().animateContentSize(tween(220)).background(GreenSoft, RoundedCornerShape(10.dp)).padding(14.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Text("✓  Matching sequence found", color = Green, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text("Index sequence: [${result.sequence.joinToString(", ")}]", color = Dark, fontWeight = FontWeight.SemiBold)
                Text("A = ${result.top}", color = Dark, fontFamily = FontFamily.Monospace)
                Text("B = ${result.bottom}", color = Dark, fontFamily = FontFamily.Monospace)
                Text("Both concatenations are identical.", color = Green, fontSize = 12.sp)
            }
        } else if (!running && checked > 0) {
            Text("No matching sequence was found within the selected depth.", color = Muted, fontSize = 12.sp, lineHeight = 17.sp)
        }
        }
    }
}

@Composable
private fun MetricBox(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier.background(Cream, RoundedCornerShape(9.dp)).padding(11.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(label, color = Muted, fontSize = 11.sp)
        Text(value, color = Dark, fontSize = 19.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun TipCard() {
    Card(colors = CardDefaults.cardColors(containerColor = SoftOrange), shape = RoundedCornerShape(14.dp)) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text("QUICK NOTE", color = Brown, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            Text("What counts as a solution?", color = Dark, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("The same sequence of tile indices must make the top and bottom strings exactly equal. A prefix mismatch is pruned when neither string starts with the other.", color = Dark.copy(alpha = .85f), fontSize = 12.sp, lineHeight = 18.sp)
            Text("PCP is undecidable in general. This tool searches only up to your chosen depth.", color = Dark, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, lineHeight = 18.sp)
        }
    }
}

@Composable
private fun HowItWorksPage() {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        SectionCard("How the PCP solver works", "A clear walkthrough of the search process.") {
            StepRow("01", "Create tile pairs", "Each tile i contains a top string Aᵢ and a bottom string Bᵢ.")
            StepRow("02", "Build an index sequence", "The solver tries sequences such as [1], [2, 1], or [1, 2, 1]. Tiles may be reused.")
            StepRow("03", "Concatenate both sides", "For the same sequence, concatenate all selected top strings and all selected bottom strings.")
            StepRow("04", "Compare and prune", "Equal strings mean a solution. If neither side is a prefix of the other, extending that sequence cannot fix the mismatch.")
        }
        SectionCard("Formal definition") {
            Text("Given a finite list of pairs (A₁, B₁), (A₂, B₂), …, (Aₙ, Bₙ), find a non-empty index sequence i₁, i₂, …, iₖ such that:", color = Dark, fontSize = 14.sp, lineHeight = 22.sp)
            Box(Modifier.fillMaxWidth().background(Cream, RoundedCornerShape(10.dp)).padding(18.dp)) {
                Text("Aᵢ₁ Aᵢ₂ … Aᵢₖ  =  Bᵢ₁ Bᵢ₂ … Bᵢₖ", color = Dark, fontFamily = FontFamily.Monospace, fontSize = 16.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
            Text("The order of indices must be identical on both sides. Repeating an index is allowed.", color = Muted, fontSize = 13.sp)
        }
    }
}

@Composable
private fun ComplexityPage() {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        SectionCard("Complexity & limitations", "Why the search depth matters.") {
            Text("With n tile pairs and maximum sequence length d, the total number of candidate sequences before pruning is:", color = Dark, fontSize = 14.sp, lineHeight = 22.sp)
            Box(Modifier.fillMaxWidth().background(Cream, RoundedCornerShape(10.dp)).padding(18.dp)) {
                Text("n + n² + n³ + … + nᵈ", color = Dark, fontSize = 20.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }
            Text("This is exponential in the depth. Prefix pruning can reduce practical work, but worst-case search remains exponential for this bounded brute-force approach.", color = Dark, fontSize = 14.sp, lineHeight = 22.sp)
        }
        SectionCard("Important theoretical fact") {
            Text("The Post Correspondence Problem is undecidable: there is no algorithm that correctly decides every possible PCP instance. This app is an educational explorer, not a general decision procedure.", color = Dark, fontSize = 14.sp, lineHeight = 22.sp)
        }
        SectionCard("How to use the depth setting") {
            StepRow("LOW", "Depth 1–3", "Quickly checks short sequences and is useful for simple examples.")
            StepRow("MID", "Depth 4–6", "Explores more combinations but may take longer.")
            StepRow("HIGH", "Depth 7–8", "Can generate many candidates. Use smaller tile sets when exploring deeper searches.")
        }
    }
}

@Composable
private fun StepRow(number: String, title: String, body: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(13.dp), verticalAlignment = Alignment.Top) {
        Box(Modifier.size(34.dp).background(SoftOrange, CircleShape), contentAlignment = Alignment.Center) {
            Text(number, color = Dark, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, color = Dark, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(body, color = Muted, fontSize = 13.sp, lineHeight = 19.sp)
        }
    }
}

fun main() = androidx.compose.ui.window.application {
    Window(onCloseRequest = ::exitApplication, title = "PCP Solver", resizable = true) {
        MainScreen()
    }
}
