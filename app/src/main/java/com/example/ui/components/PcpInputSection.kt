package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logic.PcpSolver
import com.example.model.PresetExample
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
import com.example.ui.theme.PcpSurfaceVariant

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PcpInputSection(
    tilesA: List<String>,
    tilesB: List<String>,
    validationError: String?,
    solverStatus: SolverStatus,
    onUpdateTileA: (Int, String) -> Unit,
    onUpdateTileB: (Int, String) -> Unit,
    onAddTile: () -> Unit,
    onRemoveTile: (Int) -> Unit,
    onLoadPreset: (PresetExample) -> Unit,
    modifier: Modifier = Modifier
) {
    val isEditingDisabled = solverStatus == SolverStatus.RUNNING

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
            // Section Title & Presets Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PCP Tile Sets",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = PcpDarkBrown
                )
                Text(
                    text = "${tilesA.size} / 6 tiles",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = PcpMutedBrown
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Preset Quick-Pick Chips
            Text(
                text = "Academic Test Presets:",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = PcpLightBrown
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                PcpSolver.PRESET_EXAMPLES.forEach { preset ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(PcpCream)
                            .border(1.dp, PcpSoftOrange, RoundedCornerShape(8.dp))
                            .clickable(enabled = !isEditingDisabled) {
                                onLoadPreset(preset)
                            }
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = PcpLightBrown,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = preset.name,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = PcpDarkBrown
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Validation Error Box
            if (validationError != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(PcpErrorContainer)
                        .border(1.dp, PcpError, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = PcpError,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = validationError,
                            color = PcpError,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Tile Rows: Both List A and List B with 1-based indexing
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header row for columns
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(PcpSurfaceVariant)
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "#",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PcpMutedBrown,
                        modifier = Modifier.width(32.dp)
                    )
                    Text(
                        text = "LIST A (Top)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PcpDarkBrown,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "LIST B (Bottom)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PcpDarkBrown,
                        modifier = Modifier.weight(1f)
                    )
                    if (tilesA.size > 1) {
                        Spacer(modifier = Modifier.width(36.dp))
                    }
                }

                val focusManager = LocalFocusManager.current

                tilesA.indices.forEach { index ->
                    val tileNumber = index + 1
                    val valA = tilesA.getOrElse(index) { "" }
                    val valB = tilesB.getOrElse(index) { "" }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(PcpSurface)
                            .border(1.dp, PcpBorder, RoundedCornerShape(10.dp))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 1-based Index Badge
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(PcpSoftOrange),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$tileNumber",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PcpDarkBrown
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Input A
                        OutlinedTextField(
                            value = valA,
                            onValueChange = { onUpdateTileA(index, it) },
                            enabled = !isEditingDisabled,
                            singleLine = true,
                            placeholder = { Text("e.g. a", fontSize = 13.sp, color = PcpMutedBrown) },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PcpPrimaryOrange,
                                unfocusedBorderColor = PcpBorder,
                                focusedTextColor = PcpDarkBrown,
                                unfocusedTextColor = PcpDarkBrown,
                                focusedContainerColor = PcpSurface,
                                unfocusedContainerColor = PcpSurface
                            ),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("tile_a_$tileNumber")
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Input B
                        OutlinedTextField(
                            value = valB,
                            onValueChange = { onUpdateTileB(index, it) },
                            enabled = !isEditingDisabled,
                            singleLine = true,
                            placeholder = { Text("e.g. ab", fontSize = 13.sp, color = PcpMutedBrown) },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PcpPrimaryOrange,
                                unfocusedBorderColor = PcpBorder,
                                focusedTextColor = PcpDarkBrown,
                                unfocusedTextColor = PcpDarkBrown,
                                focusedContainerColor = PcpSurface,
                                unfocusedContainerColor = PcpSurface
                            ),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("tile_b_$tileNumber")
                        )

                        // Remove Tile Button
                        if (tilesA.size > 1) {
                            IconButton(
                                onClick = { onRemoveTile(index) },
                                enabled = !isEditingDisabled,
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("remove_tile_$tileNumber")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove Tile $tileNumber",
                                    tint = if (isEditingDisabled) PcpMutedBrown else PcpError,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Add Tile Button
            if (tilesA.size < 6) {
                Button(
                    onClick = onAddTile,
                    enabled = !isEditingDisabled,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PcpCream,
                        contentColor = PcpDarkBrown
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PcpSoftOrange),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_tile_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = PcpDarkBrown
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "+ Add Tile (Pair ${tilesA.size + 1})",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
