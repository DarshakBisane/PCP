package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import com.example.ui.theme.PcpSurface
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SearchControlsSection(
    maxDepth: Int,
    totalEstimatedSequences: Long,
    isSearchSpaceLarge: Boolean,
    animationDelayMs: Long,
    solverStatus: SolverStatus,
    onMaxDepthChange: (Int) -> Unit,
    onAnimationDelayChange: (Long) -> Unit,
    onStartSearch: () -> Unit,
    onTogglePause: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isRunning = solverStatus == SolverStatus.RUNNING
    val isPaused = solverStatus == SolverStatus.PAUSED
    val isBusy = isRunning || isPaused

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
            // Depth Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Maximum Search Depth",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = PcpDarkBrown
                    )
                    Text(
                        text = "Maximum length of the index sequence checked by brute force.",
                        fontSize = 12.sp,
                        color = PcpMutedBrown,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(PcpCream)
                        .border(1.dp, PcpSoftOrange, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Depth: $maxDepth",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = PcpDarkBrown
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Depth Slider 1..8
            Slider(
                value = maxDepth.toFloat(),
                onValueChange = { onMaxDepthChange(it.toInt()) },
                valueRange = 1f..8f,
                steps = 6, // 1, 2, 3, 4, 5, 6, 7, 8
                enabled = !isBusy,
                colors = SliderDefaults.colors(
                    thumbColor = PcpDarkBrown,
                    activeTrackColor = PcpPrimaryOrange,
                    inactiveTrackColor = PcpCream
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("depth_slider")
            )

            // Search Space Estimation Info
            val formattedTotal = NumberFormat.getNumberInstance(Locale.US).format(totalEstimatedSequences)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(PcpCream)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = PcpLightBrown,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Max Candidate Sequences:",
                        fontSize = 12.sp,
                        color = PcpDarkBrown
                    )
                }
                Text(
                    text = "$formattedTotal",
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = PcpDarkBrown
                )
            }

            if (isSearchSpaceLarge) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(PcpErrorContainer)
                        .border(1.dp, PcpError, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Warning",
                            tint = PcpError,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Large search space! Brute-force may take several seconds. Fast speed recommended.",
                            fontSize = 11.sp,
                            color = PcpError,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Animation Speed Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = PcpLightBrown,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Animation Speed:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PcpDarkBrown
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    SpeedChip(
                        label = "Normal (120ms)",
                        selected = animationDelayMs == 120L,
                        onClick = { onAnimationDelayChange(120L) }
                    )
                    SpeedChip(
                        label = "Fast (25ms)",
                        selected = animationDelayMs == 25L,
                        onClick = { onAnimationDelayChange(25L) }
                    )
                    SpeedChip(
                        label = "Instant",
                        selected = animationDelayMs == 0L,
                        onClick = { onAnimationDelayChange(0L) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Primary: RUN BRUTE-FORCE SEARCH
                Button(
                    onClick = onStartSearch,
                    enabled = !isRunning,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PcpPrimaryOrange,
                        contentColor = PcpDarkBrown
                    ),
                    modifier = Modifier
                        .weight(1.4f)
                        .testTag("run_search_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = PcpDarkBrown
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isRunning) "SEARCHING..." else "RUN BRUTE-FORCE SEARCH",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                // Pause / Resume (if active)
                if (isBusy) {
                    Button(
                        onClick = onTogglePause,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PcpCream,
                            contentColor = PcpDarkBrown
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PcpSoftOrange),
                        modifier = Modifier
                            .weight(0.8f)
                            .testTag("pause_button")
                    ) {
                        Icon(
                            imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = PcpDarkBrown
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isPaused) "RESUME" else "PAUSE",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                // Secondary: RESET
                OutlinedButton(
                    onClick = onReset,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = PcpDarkBrown
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PcpBorder),
                    modifier = Modifier
                        .weight(0.9f)
                        .testTag("reset_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = PcpDarkBrown
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "RESET",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SpeedChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (selected) PcpPrimaryOrange else PcpSurface)
            .border(1.dp, if (selected) PcpDarkBrown else PcpBorder, RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = PcpDarkBrown
        )
    }
}
