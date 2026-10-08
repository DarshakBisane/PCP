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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PcpBorder
import com.example.ui.theme.PcpCream
import com.example.ui.theme.PcpDarkBrown
import com.example.ui.theme.PcpLightBrown
import com.example.ui.theme.PcpMutedBrown
import com.example.ui.theme.PcpSoftOrange
import com.example.ui.theme.PcpSurface

@Composable
fun StudentInfoAndFooterSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Student Information Academic Card
        Card(
            modifier = Modifier.fillMaxWidth(),
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
                    text = "Academic & Student Information",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PcpDarkBrown
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(PcpCream)
                        .border(1.dp, PcpSoftOrange, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Student Name:", fontSize = 12.sp, color = PcpMutedBrown)
                            Text(text = "DARSHAK K. BISANE", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PcpDarkBrown)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Roll Number:", fontSize = 12.sp, color = PcpMutedBrown)
                            Text(text = "CM25D004", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PcpDarkBrown)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Course / Evaluation:", fontSize = 12.sp, color = PcpMutedBrown)
                            Text(text = "TAE-1 | Project Based Learning (PBL)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = PcpDarkBrown)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Assigned Topic:", fontSize = 12.sp, color = PcpMutedBrown)
                            Text(text = "Post's Correspondence Problem Solver", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = PcpDarkBrown)
                        }
                    }
                }
            }
        }

        // Minimal Academic Footer
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Post's Correspondence Problem (PCP) • Brute-Force Educational Solver",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = PcpLightBrown,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "B.Tech PBL | Theory of Computation & Formal Languages",
                fontSize = 11.sp,
                color = PcpMutedBrown,
                textAlign = TextAlign.Center
            )
        }
    }
}
