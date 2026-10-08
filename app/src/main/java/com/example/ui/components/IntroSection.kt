package com.example.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PcpBorder
import com.example.ui.theme.PcpCream
import com.example.ui.theme.PcpDarkBrown
import com.example.ui.theme.PcpLightBrown
import com.example.ui.theme.PcpMutedBrown
import com.example.ui.theme.PcpSoftOrange
import com.example.ui.theme.PcpSurface

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IntroSection(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PcpCream),
        border = CardDefaults.outlinedCardBorder().copy(width = 1.dp, brush = androidx.compose.ui.graphics.SolidColor(PcpSoftOrange))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Post's Correspondence Problem",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PcpDarkBrown
            )
            Text(
                text = "Brute-Force Solver",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = PcpLightBrown,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Find a sequence of indices that makes the concatenated strings from List A and List B equal.",
                fontSize = 14.sp,
                color = PcpDarkBrown,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Academic Student Meta Badges
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AcademicMetaBadge(
                    label = "Project",
                    value = "TAE-1 | Project Based Learning"
                )
                AcademicMetaBadge(
                    label = "Student",
                    value = "DARSHAK K. BISANE"
                )
                AcademicMetaBadge(
                    label = "Roll No",
                    value = "CM25D004"
                )
            }
        }
    }
}

@Composable
private fun AcademicMetaBadge(label: String, value: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(PcpSurface)
            .border(1.dp, PcpBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "$label:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = PcpMutedBrown
            )
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = PcpDarkBrown
            )
        }
    }
}
