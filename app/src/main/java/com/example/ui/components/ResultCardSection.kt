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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.example.model.SearchResult
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
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ResultCardSection(
    result: SearchResult,
    modifier: Modifier = Modifier
) {
    val isMatch = result.matchFound
    val formattedChecked = NumberFormat.getNumberInstance(Locale.US).format(result.sequencesChecked)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("result_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isMatch) PcpSuccessContainer else PcpCream
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            width = 1.5.dp,
            brush = androidx.compose.ui.graphics.SolidColor(
                if (isMatch) PcpSuccess else PcpSoftOrange
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (isMatch) Icons.Default.CheckCircle else Icons.Default.SearchOff,
                        contentDescription = null,
                        tint = if (isMatch) PcpSuccess else PcpError,
                        modifier = Modifier.size(26.dp)
                    )
                    Text(
                        text = if (isMatch) "MATCH FOUND" else "NO MATCH FOUND",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isMatch) PcpSuccess else PcpError
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(PcpSurface)
                        .border(1.dp, PcpBorder, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Depth ${result.maxDepth}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PcpDarkBrown
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (isMatch) {
                // Success content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(PcpSurface)
                        .border(1.dp, PcpBorder, RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Sequence Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Sequence:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = PcpMutedBrown
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "[ ${result.sequence.joinToString(", ")} ]",
                                fontSize = 15.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = PcpDarkBrown
                            )
                        }
                    }

                    HorizontalDivider(color = PcpBorder, thickness = 0.8.dp)

                    // Matching String
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Matching String:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = PcpMutedBrown
                        )
                        Text(
                            text = "\"${result.matchingString}\"",
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = PcpSuccess
                        )
                    }

                    HorizontalDivider(color = PcpBorder, thickness = 0.8.dp)

                    // A Breakdown
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "A:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = PcpMutedBrown
                        )
                        Text(
                            text = result.formulaA,
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold,
                            color = PcpDarkBrown
                        )
                    }

                    // B Breakdown
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "B:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = PcpMutedBrown
                        )
                        Text(
                            text = result.formulaB,
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold,
                            color = PcpDarkBrown
                        )
                    }

                    HorizontalDivider(color = PcpBorder, thickness = 0.8.dp)

                    // Sequences Checked
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Sequences Checked:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = PcpMutedBrown
                        )
                        Text(
                            text = formattedChecked,
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = PcpDarkBrown
                        )
                    }
                }
            } else {
                // No Match Found content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(PcpSurface)
                        .border(1.dp, PcpBorder, RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "No matching sequence found within the selected search depth.",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PcpError,
                        lineHeight = 20.sp
                    )

                    HorizontalDivider(color = PcpBorder, thickness = 0.8.dp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Sequences Checked:",
                            fontSize = 13.sp,
                            color = PcpMutedBrown
                        )
                        Text(
                            text = formattedChecked,
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = PcpDarkBrown
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Search Depth:",
                            fontSize = 13.sp,
                            color = PcpMutedBrown
                        )
                        Text(
                            text = "${result.maxDepth}",
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = PcpDarkBrown
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Academic note
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(PcpCream)
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
                                text = "Important: Because Post's Correspondence Problem is undecidable in general, failing to find a match within depth ${result.maxDepth} only means no match exists up to this length—it does NOT mean the PCP instance has no longer solution.",
                                fontSize = 11.sp,
                                color = PcpDarkBrown,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
