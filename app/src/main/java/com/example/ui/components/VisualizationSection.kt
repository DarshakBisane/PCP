package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SearchStep
import com.example.model.SolverStatus
import com.example.ui.theme.PcpBorder
import com.example.ui.theme.PcpCream
import com.example.ui.theme.PcpDarkBrown
import com.example.ui.theme.PcpError
import com.example.ui.theme.PcpErrorContainer
import com.example.ui.theme.PcpLightBrown
import com.example.ui.theme.PcpMutedBrown
import com.example.ui.theme.PcpPrimaryOrange
import com.example.ui.theme.PcpSoftOrange
import com.example.ui.theme.PcpSuccess
import com.example.ui.theme.PcpSuccessContainer
import com.example.ui.theme.PcpSurface
import com.example.ui.theme.PcpSurfaceVariant
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VisualizationSection(
    currentStep: SearchStep?,
    sequencesChecked: Long,
    totalEstimatedSequences: Long,
    solverStatus: SolverStatus,
    modifier: Modifier = Modifier
) {
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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Brute-Force Search Visualization",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = PcpDarkBrown
                )

                val formattedChecked = NumberFormat.getNumberInstance(Locale.US).format(sequencesChecked)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(PcpCream)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Checked: $formattedChecked",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        color = PcpDarkBrown
                    )
                }
            }

            // Progress bar if running
            if (solverStatus == SolverStatus.RUNNING || solverStatus == SolverStatus.PAUSED) {
                Spacer(modifier = Modifier.height(10.dp))
                val progress = if (totalEstimatedSequences > 0) {
                    (sequencesChecked.toFloat() / totalEstimatedSequences.toFloat()).coerceIn(0f, 1f)
                } else 0f
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = PcpPrimaryOrange,
                    trackColor = PcpCream
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (currentStep == null) {
                // Idle state placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(PcpSurfaceVariant)
                        .border(1.dp, PcpBorder, RoundedCornerShape(12.dp))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Search Engine Ready",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = PcpDarkBrown
                        )
                        Text(
                            text = "Tap 'RUN BRUTE-FORCE SEARCH' to watch sequences generated & evaluated in real-time.",
                            fontSize = 13.sp,
                            color = PcpMutedBrown,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                // Live Candidate Sequence Display
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 1. Current Sequence
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(PcpCream)
                            .border(1.dp, PcpSoftOrange, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Current Candidate Sequence:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = PcpLightBrown
                                )
                                Text(
                                    text = "Length: ${currentStep.sequence.size}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = PcpMutedBrown
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.testTag("current_sequence_row")
                            ) {
                                currentStep.sequence.forEachIndexed { idx, tileIndex ->
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(PcpPrimaryOrange)
                                            .border(1.5.dp, PcpDarkBrown, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "$tileIndex",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = PcpDarkBrown
                                        )
                                    }
                                    if (idx < currentStep.sequence.size - 1) {
                                        Text(
                                            text = "\u2192",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = PcpLightBrown
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 2. Selected A & B Domino Tiles
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(PcpSurfaceVariant)
                            .border(1.dp, PcpBorder, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Top List A Tiles
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Selected A:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PcpDarkBrown,
                                modifier = Modifier.width(86.dp)
                            )
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                currentStep.selectedTilesA.forEach { tileStr ->
                                    TileBubble(text = tileStr, isTop = true)
                                }
                            }
                        }

                        // Bottom List B Tiles
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Selected B:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PcpDarkBrown,
                                modifier = Modifier.width(86.dp)
                            )
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                currentStep.selectedTilesB.forEach { tileStr ->
                                    TileBubble(text = tileStr, isTop = false)
                                }
                            }
                        }
                    }

                    // 3. Mathematical Concatenation breakdown
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(PcpSurface)
                            .border(1.dp, PcpBorder, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Top (A):",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PcpMutedBrown
                            )
                            Text(
                                text = currentStep.formulaA,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold,
                                color = PcpDarkBrown
                            )
                        }

                        HorizontalDivider(color = PcpBorder, thickness = 0.8.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Bottom (B):",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PcpMutedBrown
                            )
                            Text(
                                text = currentStep.formulaB,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold,
                                color = PcpDarkBrown
                            )
                        }

                        HorizontalDivider(color = PcpBorder, thickness = 0.8.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Comparison:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PcpMutedBrown
                            )
                            Text(
                                text = "\"${currentStep.stringA}\" ${if (currentStep.isMatch) "==" else "\u2260"} \"${currentStep.stringB}\"",
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = if (currentStep.isMatch) PcpSuccess else PcpDarkBrown
                            )
                        }
                    }

                    // 4. Status Badge
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (currentStep.isMatch) PcpSuccessContainer
                                else if (solverStatus == SolverStatus.RUNNING) PcpCream
                                else PcpErrorContainer
                            )
                            .border(
                                1.dp,
                                if (currentStep.isMatch) PcpSuccess
                                else if (solverStatus == SolverStatus.RUNNING) PcpSoftOrange
                                else PcpError,
                                RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = when {
                                    currentStep.isMatch -> Icons.Default.CheckCircle
                                    solverStatus == SolverStatus.RUNNING -> Icons.Default.HourglassTop
                                    else -> Icons.Default.Close
                                },
                                contentDescription = null,
                                tint = when {
                                    currentStep.isMatch -> PcpSuccess
                                    solverStatus == SolverStatus.RUNNING -> PcpDarkBrown
                                    else -> PcpError
                                },
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = when {
                                    currentStep.isMatch -> "\u2713 MATCH FOUND"
                                    solverStatus == SolverStatus.RUNNING -> "Evaluating next sequence..."
                                    else -> "NOT MATCH"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = when {
                                    currentStep.isMatch -> PcpSuccess
                                    solverStatus == SolverStatus.RUNNING -> PcpDarkBrown
                                    else -> PcpError
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TileBubble(text: String, isTop: Boolean) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isTop) PcpCream else PcpSurface)
            .border(1.dp, PcpBorder, RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = PcpDarkBrown
        )
    }
}
