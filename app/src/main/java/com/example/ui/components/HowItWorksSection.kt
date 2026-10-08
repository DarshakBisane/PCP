package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PcpBorder
import com.example.ui.theme.PcpCream
import com.example.ui.theme.PcpDarkBrown
import com.example.ui.theme.PcpLightBrown
import com.example.ui.theme.PcpMutedBrown
import com.example.ui.theme.PcpPrimaryOrange
import com.example.ui.theme.PcpSoftOrange
import com.example.ui.theme.PcpSuccess
import com.example.ui.theme.PcpSuccessContainer
import com.example.ui.theme.PcpSurface
import com.example.ui.theme.PcpSurfaceVariant

@Composable
fun HowItWorksSection(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PcpSurface),
        border = CardDefaults.outlinedCardBorder().copy(width = 1.dp, brush = androidx.compose.ui.graphics.SolidColor(PcpBorder))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "How Post's Correspondence Problem Works",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = PcpDarkBrown
            )
            Text(
                text = "Formal mathematical definition and 6-step brute-force procedure.",
                fontSize = 12.sp,
                color = PcpMutedBrown,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Mathematical Condition Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(PcpCream)
                    .border(1.dp, PcpSoftOrange, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Mathematical Matching Condition:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PcpLightBrown
                    )
                    Text(
                        text = "A[i\u2081] A[i\u2082] \u2026 A[i\u2096] = B[i\u2081] B[i\u2082] \u2026 B[i\u2096]",
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = PcpDarkBrown
                    )
                    Text(
                        text = "where (i\u2081, i\u2082, \u2026, i\u2096) is an identical sequence of 1-based tile indices (with repetitions allowed).",
                        fontSize = 11.sp,
                        color = PcpMutedBrown,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6 Algorithmic Steps
            Text(
                text = "Step-by-Step Procedure:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PcpDarkBrown
            )

            Spacer(modifier = Modifier.height(10.dp))

            val steps = listOf(
                "Take two lists of string tiles (List A and List B of equal size n).",
                "Generate possible candidate index sequences [i\u2081, i\u2082, \u2026, i\u2096] starting from length 1 up to maxDepth.",
                "Use the SAME sequence for both lists (indices may be repeated).",
                "Concatenate the selected tiles: string A and string B.",
                "Compare both strings for exact equality.",
                "If they are equal, a valid PCP solution is found!"
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                steps.forEachIndexed { index, description ->
                    StepRow(stepNumber = index + 1, text = description)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Visual Default Example Demonstration Card
            Text(
                text = "Default Example Walkthrough:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PcpDarkBrown
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(PcpSurfaceVariant)
                    .border(1.dp, PcpBorder, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Tile Set A:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PcpLightBrown)
                            Text(text = "1 \u2192 a", fontFamily = FontFamily.Monospace, fontSize = 13.sp, color = PcpDarkBrown)
                            Text(text = "2 \u2192 ba", fontFamily = FontFamily.Monospace, fontSize = 13.sp, color = PcpDarkBrown)
                        }
                        Column {
                            Text(text = "Tile Set B:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PcpLightBrown)
                            Text(text = "1 \u2192 ab", fontFamily = FontFamily.Monospace, fontSize = 13.sp, color = PcpDarkBrown)
                            Text(text = "2 \u2192 a", fontFamily = FontFamily.Monospace, fontSize = 13.sp, color = PcpDarkBrown)
                        }
                    }

                    HorizontalDivider(color = PcpBorder, thickness = 0.8.dp)

                    Text(
                        text = "Chosen Sequence: [ 1, 2 ]",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = PcpDarkBrown
                    )

                    Text(
                        text = "List A: a + ba = aba",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = PcpDarkBrown
                    )
                    Text(
                        text = "List B: ab + a = aba",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = PcpDarkBrown
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(PcpSuccessContainer)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = PcpSuccess,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Therefore: aba == aba  \u2192  \u2713 MATCH FOUND",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = PcpSuccess
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepRow(stepNumber: Int, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(PcpPrimaryOrange),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$stepNumber",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = PcpDarkBrown
            )
        }
        Text(
            text = text,
            fontSize = 13.sp,
            color = PcpDarkBrown,
            lineHeight = 18.sp,
            modifier = Modifier.weight(1f)
        )
    }
}
