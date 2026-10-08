package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logic.PcpSolver
import com.example.model.PresetExample
import com.example.model.SearchResult
import com.example.model.SearchStep
import com.example.model.SolverStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class PcpUiState(
    val tilesA: List<String> = listOf("a", "ba"),
    val tilesB: List<String> = listOf("ab", "a"),
    val maxDepth: Int = 5,
    val animationDelayMs: Long = 120L,
    val solverStatus: SolverStatus = SolverStatus.IDLE,
    val currentStep: SearchStep? = null,
    val searchResult: SearchResult? = null,
    val validationError: String? = null,
    val selectedTab: Int = 0, // 0 = Solver, 1 = How It Works, 2 = Complexity
    val sequencesChecked: Long = 0L,
    val totalEstimatedSequences: Long = 62L,
    val isSearchSpaceLarge: Boolean = false
)

class PcpViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PcpUiState())
    val uiState: StateFlow<PcpUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private var isPaused = false

    init {
        updateSearchSpaceEstimation()
    }

    fun selectTab(tabIndex: Int) {
        _uiState.update { it.copy(selectedTab = tabIndex) }
    }

    fun updateTileA(index: Int, value: String) {
        if (_uiState.value.solverStatus == SolverStatus.RUNNING) return
        val current = _uiState.value.tilesA.toMutableList()
        if (index in current.indices) {
            current[index] = value.trim()
            _uiState.update { it.copy(tilesA = current, validationError = null, searchResult = null) }
            updateSearchSpaceEstimation()
        }
    }

    fun updateTileB(index: Int, value: String) {
        if (_uiState.value.solverStatus == SolverStatus.RUNNING) return
        val current = _uiState.value.tilesB.toMutableList()
        if (index in current.indices) {
            current[index] = value.trim()
            _uiState.update { it.copy(tilesB = current, validationError = null, searchResult = null) }
            updateSearchSpaceEstimation()
        }
    }

    fun addTile() {
        if (_uiState.value.solverStatus == SolverStatus.RUNNING) return
        if (_uiState.value.tilesA.size >= 6) {
            _uiState.update { it.copy(validationError = "Maximum 6 tiles allowed for brute-force solver.") }
            return
        }
        val newA = _uiState.value.tilesA + ""
        val newB = _uiState.value.tilesB + ""
        _uiState.update {
            it.copy(
                tilesA = newA,
                tilesB = newB,
                validationError = null,
                searchResult = null
            )
        }
        updateSearchSpaceEstimation()
    }

    fun removeTile(index: Int) {
        if (_uiState.value.solverStatus == SolverStatus.RUNNING) return
        if (_uiState.value.tilesA.size <= 1) {
            _uiState.update { it.copy(validationError = "At least 1 tile pair is required.") }
            return
        }
        val newA = _uiState.value.tilesA.filterIndexed { i, _ -> i != index }
        val newB = _uiState.value.tilesB.filterIndexed { i, _ -> i != index }
        _uiState.update {
            it.copy(
                tilesA = newA,
                tilesB = newB,
                validationError = null,
                searchResult = null
            )
        }
        updateSearchSpaceEstimation()
    }

    fun setMaxDepth(depth: Int) {
        if (_uiState.value.solverStatus == SolverStatus.RUNNING) return
        val clamped = depth.coerceIn(1, 8)
        _uiState.update { it.copy(maxDepth = clamped, validationError = null, searchResult = null) }
        updateSearchSpaceEstimation()
    }

    fun setAnimationDelay(delayMs: Long) {
        _uiState.update { it.copy(animationDelayMs = delayMs) }
    }

    fun loadPreset(preset: PresetExample) {
        stopSearchInternal()
        _uiState.update {
            it.copy(
                tilesA = preset.tilesA,
                tilesB = preset.tilesB,
                maxDepth = preset.recommendedDepth,
                validationError = null,
                searchResult = null,
                currentStep = null,
                sequencesChecked = 0L,
                solverStatus = SolverStatus.IDLE
            )
        }
        updateSearchSpaceEstimation()
    }

    private fun updateSearchSpaceEstimation() {
        val count = _uiState.value.tilesA.size
        val depth = _uiState.value.maxDepth
        val total = PcpSolver.calculateTotalSequences(count, depth)
        val isLarge = total > 20000L
        _uiState.update {
            it.copy(
                totalEstimatedSequences = total,
                isSearchSpaceLarge = isLarge
            )
        }
    }

    private fun validateInputs(): String? {
        val state = _uiState.value
        if (state.tilesA.isEmpty() || state.tilesB.isEmpty()) {
            return "Tile lists cannot be empty."
        }
        if (state.tilesA.size != state.tilesB.size) {
            return "List A and List B must contain the same number of tiles."
        }
        if (state.tilesA.size > 6) {
            return "Maximum 6 tiles allowed for brute-force search."
        }
        if (state.maxDepth !in 1..8) {
            return "Search depth must be between 1 and 8."
        }
        for (i in state.tilesA.indices) {
            if (state.tilesA[i].isEmpty()) {
                return "Tile A${i + 1} cannot be empty."
            }
            if (state.tilesB[i].isEmpty()) {
                return "Tile B${i + 1} cannot be empty."
            }
        }
        return null
    }

    fun startSearch() {
        val error = validateInputs()
        if (error != null) {
            _uiState.update { it.copy(validationError = error) }
            return
        }

        stopSearchInternal()
        isPaused = false

        _uiState.update {
            it.copy(
                solverStatus = SolverStatus.RUNNING,
                validationError = null,
                searchResult = null,
                currentStep = null,
                sequencesChecked = 0L
            )
        }

        val tilesA = _uiState.value.tilesA
        val tilesB = _uiState.value.tilesB
        val maxDepth = _uiState.value.maxDepth

        searchJob = viewModelScope.launch {
            var checkedCount = 0L
            var matchFoundStep: SearchStep? = null

            for (seq in PcpSolver.generateCandidateSequences(tilesA.size, maxDepth)) {
                if (!isActive) break

                while (isPaused && isActive) {
                    delay(80)
                }
                if (!isActive) break

                checkedCount++
                val step = PcpSolver.buildStep(seq, tilesA, tilesB, checkedCount)

                _uiState.update {
                    it.copy(
                        currentStep = step,
                        sequencesChecked = checkedCount
                    )
                }

                if (step.isMatch) {
                    matchFoundStep = step
                    break
                }

                val currentDelay = _uiState.value.animationDelayMs
                if (currentDelay > 0) {
                    delay(currentDelay)
                } else if (checkedCount % 200 == 0L) {
                    // Yield periodically if running in zero-delay/fast mode
                    delay(1)
                }
            }

            if (!isActive) return@launch

            if (matchFoundStep != null) {
                val formulaA = matchFoundStep.selectedTilesA.joinToString(" + ") + " \u2192 ${matchFoundStep.stringA}"
                val formulaB = matchFoundStep.selectedTilesB.joinToString(" + ") + " \u2192 ${matchFoundStep.stringB}"
                _uiState.update {
                    it.copy(
                        solverStatus = SolverStatus.COMPLETED,
                        searchResult = SearchResult(
                            matchFound = true,
                            sequence = matchFoundStep.sequence,
                            matchingString = matchFoundStep.stringA,
                            formulaA = formulaA,
                            formulaB = formulaB,
                            sequencesChecked = checkedCount,
                            maxDepth = maxDepth
                        )
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        solverStatus = SolverStatus.COMPLETED,
                        searchResult = SearchResult(
                            matchFound = false,
                            sequencesChecked = checkedCount,
                            maxDepth = maxDepth
                        )
                    )
                }
            }
        }
    }

    fun togglePause() {
        if (_uiState.value.solverStatus == SolverStatus.RUNNING) {
            isPaused = true
            _uiState.update { it.copy(solverStatus = SolverStatus.PAUSED) }
        } else if (_uiState.value.solverStatus == SolverStatus.PAUSED) {
            isPaused = false
            _uiState.update { it.copy(solverStatus = SolverStatus.RUNNING) }
        }
    }

    private fun stopSearchInternal() {
        searchJob?.cancel()
        searchJob = null
        isPaused = false
    }

    fun stopSearch() {
        stopSearchInternal()
        _uiState.update {
            it.copy(
                solverStatus = SolverStatus.IDLE,
                searchResult = if (it.sequencesChecked > 0 && it.searchResult == null) {
                    SearchResult(
                        matchFound = false,
                        sequencesChecked = it.sequencesChecked,
                        maxDepth = it.maxDepth,
                        stoppedEarly = true
                    )
                } else it.searchResult
            )
        }
    }

    fun reset() {
        stopSearchInternal()
        // Reset back to assignment default
        val defaultPreset = PcpSolver.PRESET_EXAMPLES.first()
        _uiState.update {
            PcpUiState(
                tilesA = defaultPreset.tilesA,
                tilesB = defaultPreset.tilesB,
                maxDepth = defaultPreset.recommendedDepth,
                solverStatus = SolverStatus.IDLE,
                currentStep = null,
                searchResult = null,
                validationError = null,
                selectedTab = it.selectedTab,
                sequencesChecked = 0L
            )
        }
        updateSearchSpaceEstimation()
    }
}
