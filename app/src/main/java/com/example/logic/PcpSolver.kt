package com.example.logic

import com.example.model.PresetExample
import com.example.model.SearchResult
import com.example.model.SearchStep

object PcpSolver {

    val PRESET_EXAMPLES = listOf(
        PresetExample(
            id = "default",
            name = "Default Example (Assignment)",
            description = "A=[a, ba], B=[ab, a] -> Sequence [1, 2] -> 'aba'",
            tilesA = listOf("a", "ba"),
            tilesB = listOf("ab", "a"),
            recommendedDepth = 5
        ),
        PresetExample(
            id = "test_2",
            name = "Test 2: No Match",
            description = "A=[a], B=[b] -> Incompatible symbols, no match within depth",
            tilesA = listOf("a"),
            tilesB = listOf("b"),
            recommendedDepth = 4
        ),
        PresetExample(
            id = "test_3",
            name = "Test 3: Single Index Match",
            description = "A=[x, y], B=[x, z] -> Matches at index [1] ('x')",
            tilesA = listOf("x", "y"),
            tilesB = listOf("x", "z"),
            recommendedDepth = 3
        ),
        PresetExample(
            id = "test_4",
            name = "Test 4: Identical Single Tile",
            description = "A=[a], B=[a] -> Immediate match at [1] ('a')",
            tilesA = listOf("a"),
            tilesB = listOf("a"),
            recommendedDepth = 3
        ),
        PresetExample(
            id = "test_5",
            name = "Test 5: Repeated Index Verification",
            description = "A=[ab, b], B=[a, bb] -> Sequence [1, 2] -> 'abb'",
            tilesA = listOf("ab", "b"),
            tilesB = listOf("a", "bb"),
            recommendedDepth = 5
        ),
        PresetExample(
            id = "classic_3",
            name = "Classic 3-Tile Instance",
            description = "A=[a, ab, bba], B=[baa, aa, bb] -> Sequence [3, 2, 3, 1]",
            tilesA = listOf("a", "ab", "bba"),
            tilesB = listOf("baa", "aa", "bb"),
            recommendedDepth = 4
        )
    )

    fun calculateTotalSequences(tileCount: Int, maxDepth: Int): Long {
        if (tileCount <= 0 || maxDepth <= 0) return 0L
        if (tileCount == 1) return maxDepth.toLong()
        var total = 0L
        var currentPower = tileCount.toLong()
        for (d in 1..maxDepth) {
            total += currentPower
            // Prevent overflow if numbers grow too large
            if (Long.MAX_VALUE / tileCount < currentPower && d < maxDepth) {
                return Long.MAX_VALUE
            }
            if (d < maxDepth) {
                currentPower *= tileCount
            }
        }
        return total
    }

    fun buildStep(
        sequence: List<Int>,
        tilesA: List<String>,
        tilesB: List<String>,
        stepIndex: Long
    ): SearchStep {
        // Sequence contains 1-based indices
        val selectedA = sequence.map { idx -> tilesA[idx - 1] }
        val selectedB = sequence.map { idx -> tilesB[idx - 1] }

        val concatA = selectedA.joinToString("")
        val concatB = selectedB.joinToString("")

        return SearchStep(
            stepIndex = stepIndex,
            sequence = sequence,
            selectedTilesA = selectedA,
            selectedTilesB = selectedB,
            stringA = concatA,
            stringB = concatB,
            isMatch = concatA == concatB
        )
    }

    /**
     * Sequence sequence generator:
     * Generates all sequences of 1-based indices from length 1 up to maxDepth.
     * Generates in order:
     * Length 1: [1], [2], ..., [n]
     * Length 2: [1, 1], [1, 2], ..., [2, 1], ...
     * Repeated indices are naturally supported.
     */
    fun generateCandidateSequences(tileCount: Int, maxDepth: Int): Sequence<List<Int>> = sequence {
        if (tileCount <= 0 || maxDepth <= 0) return@sequence

        for (length in 1..maxDepth) {
            yieldAll(generateSequencesOfLength(tileCount, length))
        }
    }

    private fun generateSequencesOfLength(tileCount: Int, length: Int): Sequence<List<Int>> = sequence {
        val current = IntArray(length) { 1 }
        while (true) {
            yield(current.toList())
            // Increment like an odometer in base (tileCount), 1-indexed
            var pos = length - 1
            while (pos >= 0 && current[pos] == tileCount) {
                current[pos] = 1
                pos--
            }
            if (pos < 0) break
            current[pos]++
        }
    }

    /**
     * Solve immediately without delays (for instant results or tests).
     */
    fun solveSynchronous(tilesA: List<String>, tilesB: List<String>, maxDepth: Int): SearchResult {
        if (tilesA.isEmpty() || tilesA.size != tilesB.size) {
            return SearchResult(
                matchFound = false,
                sequencesChecked = 0,
                maxDepth = maxDepth
            )
        }

        var checkedCount = 0L
        for (seq in generateCandidateSequences(tilesA.size, maxDepth)) {
            checkedCount++
            val step = buildStep(seq, tilesA, tilesB, checkedCount)
            if (step.isMatch) {
                val formulaA = step.selectedTilesA.joinToString(" + ") + " \u2192 ${step.stringA}"
                val formulaB = step.selectedTilesB.joinToString(" + ") + " \u2192 ${step.stringB}"
                return SearchResult(
                    matchFound = true,
                    sequence = seq,
                    matchingString = step.stringA,
                    formulaA = formulaA,
                    formulaB = formulaB,
                    sequencesChecked = checkedCount,
                    maxDepth = maxDepth
                )
            }
        }

        return SearchResult(
            matchFound = false,
            sequencesChecked = checkedCount,
            maxDepth = maxDepth
        )
    }
}
