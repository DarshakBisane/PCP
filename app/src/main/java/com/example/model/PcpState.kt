package com.example.model

data class Tile(
    val index: Int, // 1-based index (1, 2, ...)
    val valueA: String,
    val valueB: String
)

data class SearchStep(
    val stepIndex: Long,
    val sequence: List<Int>, // 1-based indices e.g. [1, 2]
    val selectedTilesA: List<String>,
    val selectedTilesB: List<String>,
    val stringA: String,
    val stringB: String,
    val isMatch: Boolean
) {
    val formulaA: String
        get() = selectedTilesA.joinToString(" + ") + if (selectedTilesA.size > 1) " = $stringA" else ""

    val formulaB: String
        get() = selectedTilesB.joinToString(" + ") + if (selectedTilesB.size > 1) " = $stringB" else ""
}

data class SearchResult(
    val matchFound: Boolean,
    val sequence: List<Int> = emptyList(),
    val matchingString: String = "",
    val formulaA: String = "",
    val formulaB: String = "",
    val sequencesChecked: Long = 0,
    val maxDepth: Int = 5,
    val stoppedEarly: Boolean = false
)

enum class SolverStatus {
    IDLE,
    RUNNING,
    PAUSED,
    COMPLETED
}

data class PresetExample(
    val id: String,
    val name: String,
    val description: String,
    val tilesA: List<String>,
    val tilesB: List<String>,
    val recommendedDepth: Int
)
