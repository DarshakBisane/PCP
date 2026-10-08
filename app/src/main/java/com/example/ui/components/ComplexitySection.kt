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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
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
import com.example.ui.theme.PcpError
import com.example.ui.theme.PcpErrorContainer
import com.example.ui.theme.PcpLightBrown
import com.example.ui.theme.PcpMutedBrown
import com.example.ui.theme.PcpPrimaryOrange
import com.example.ui.theme.PcpSoftOrange
import com.example.ui.theme.PcpSurface

@Composable
fun ComplexitySection(modifier: Modifier = Modifier) {
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
                text = "Algorithm Complexity Analysis",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = PcpDarkBrown
            )
            Text(
                text = "Exponential growth in brute-force sequence generation.",
                fontSize = 12.sp,
                color = PcpMutedBrown,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Mathematical formula box
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
                        text = "Candidate Sequences Formula:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PcpLightBrown
                    )
                    Text(
                        text = "Total Candidates = n\u00B9 + n\u00B2 + n\u00B3 + \u2026 + n\u1D48",
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = PcpDarkBrown
                    )
                    Text(
                        text = "where n is the number of tiles and d is the maximum search depth.",
                        fontSize = 12.sp,
                        color = PcpMutedBrown
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Asymptotic upper bound card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(PcpCream)
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Asymptotic Time Complexity:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = PcpDarkBrown
                    )
                    Text(
                        text = "O(n\u1D48)",
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = PcpDarkBrown
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Explicit required statement
            Text(
                text = "The brute-force search becomes expensive as the number of tiles or maximum search depth increases.",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = PcpDarkBrown,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Numerical Example Table
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, PcpBorder, RoundedCornerShape(10.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PcpSoftOrange)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Tiles (n)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PcpDarkBrown)
                    Text(text = "Depth (d)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PcpDarkBrown)
                    Text(text = "Total Sequences", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PcpDarkBrown)
                }

                ComplexityRow("2", "5", "62")
                ComplexityRow("2", "8", "510")
                ComplexityRow("3", "5", "363")
                ComplexityRow("6", "8", "1,999,998")
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Undecidability Theorem
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(PcpCream)
                    .border(1.dp, PcpBorder, RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = PcpLightBrown,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Theoretical Context: Emil Post proved in 1946 that PCP is undecidable. There exists no general algorithm that can determine whether an arbitrary PCP instance has a match. Bounded brute-force search is therefore sound only up to depth d.",
                        fontSize = 11.sp,
                        color = PcpDarkBrown,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ComplexityRow(n: String, d: String, total: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PcpSurface)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = n, fontSize = 12.sp, fontFamily = FontFamily.Monospace, color = PcpDarkBrown)
        Text(text = d, fontSize = 12.sp, fontFamily = FontFamily.Monospace, color = PcpDarkBrown)
        Text(text = total, fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold, color = PcpDarkBrown)
    }
    HorizontalDivider(color = PcpBorder, thickness = 0.5.dp)
}
