package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.PcpViewModel
import com.example.ui.components.ComplexitySection
import com.example.ui.components.HeaderSection
import com.example.ui.components.HowItWorksSection
import com.example.ui.components.IntroSection
import com.example.ui.components.PcpInputSection
import com.example.ui.components.ResultCardSection
import com.example.ui.components.SearchControlsSection
import com.example.ui.components.StudentInfoAndFooterSection
import com.example.ui.components.VisualizationSection
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PcpBackground

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PcpBackground)
                ) { innerPadding ->
                    PcpApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PcpApp(
    modifier: Modifier = Modifier,
    viewModel: PcpViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PcpBackground),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 680.dp)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Header with Tab Navigation (Solver, How It Works, Complexity)
            HeaderSection(
                selectedTab = uiState.selectedTab,
                onTabSelected = { viewModel.selectTab(it) }
            )

            // 2. Short Project Introduction & Student Information
            IntroSection()

            when (uiState.selectedTab) {
                0 -> {
                    // TAB 0: PCP SOLVER MAIN WORKFLOW

                    // 3. Tile Input Section
                    PcpInputSection(
                        tilesA = uiState.tilesA,
                        tilesB = uiState.tilesB,
                        validationError = uiState.validationError,
                        solverStatus = uiState.solverStatus,
                        onUpdateTileA = { idx, value -> viewModel.updateTileA(idx, value) },
                        onUpdateTileB = { idx, value -> viewModel.updateTileB(idx, value) },
                        onAddTile = { viewModel.addTile() },
                        onRemoveTile = { idx -> viewModel.removeTile(idx) },
                        onLoadPreset = { preset -> viewModel.loadPreset(preset) }
                    )

                    // 4. Search Controls Section
                    SearchControlsSection(
                        maxDepth = uiState.maxDepth,
                        totalEstimatedSequences = uiState.totalEstimatedSequences,
                        isSearchSpaceLarge = uiState.isSearchSpaceLarge,
                        animationDelayMs = uiState.animationDelayMs,
                        solverStatus = uiState.solverStatus,
                        onMaxDepthChange = { viewModel.setMaxDepth(it) },
                        onAnimationDelayChange = { viewModel.setAnimationDelay(it) },
                        onStartSearch = { viewModel.startSearch() },
                        onTogglePause = { viewModel.togglePause() },
                        onReset = { viewModel.reset() }
                    )

                    // 5. Brute-Force Search Visualization
                    VisualizationSection(
                        currentStep = uiState.currentStep,
                        sequencesChecked = uiState.sequencesChecked,
                        totalEstimatedSequences = uiState.totalEstimatedSequences,
                        solverStatus = uiState.solverStatus
                    )

                    // 6. Final Result Card (shown whenever result is produced)
                    uiState.searchResult?.let { result ->
                        ResultCardSection(result = result)
                    }
                }

                1 -> {
                    // TAB 1: HOW PCP WORKS
                    HowItWorksSection()
                }

                2 -> {
                    // TAB 2: COMPLEXITY ANALYSIS
                    ComplexitySection()
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 7. Student Academic Details & Footer
            StudentInfoAndFooterSection()

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
