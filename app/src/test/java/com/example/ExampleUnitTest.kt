package com.example

import com.example.logic.PcpSolver
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun test1_defaultAssignmentExample() {
        val tilesA = listOf("a", "ba")
        val tilesB = listOf("ab", "a")
        val result = PcpSolver.solveSynchronous(tilesA, tilesB, maxDepth = 5)

        assertTrue("Expected match found for default example", result.matchFound)
        assertEquals(listOf(1, 2), result.sequence)
        assertEquals("aba", result.matchingString)
    }

    @Test
    fun test2_noMatchingSequenceWithinDepth() {
        val tilesA = listOf("a")
        val tilesB = listOf("b")
        val result = PcpSolver.solveSynchronous(tilesA, tilesB, maxDepth = 4)

        assertFalse("Expected no match for incompatible symbols", result.matchFound)
        assertEquals(4, result.maxDepth)
    }

    @Test
    fun test3_singleIndexMatch() {
        val tilesA = listOf("x", "y")
        val tilesB = listOf("x", "z")
        val result = PcpSolver.solveSynchronous(tilesA, tilesB, maxDepth = 3)

        assertTrue(result.matchFound)
        assertEquals(listOf(1), result.sequence)
        assertEquals("x", result.matchingString)
    }

    @Test
    fun test4_identicalSingleTile() {
        val tilesA = listOf("a")
        val tilesB = listOf("a")
        val result = PcpSolver.solveSynchronous(tilesA, tilesB, maxDepth = 3)

        assertTrue(result.matchFound)
        assertEquals(listOf(1), result.sequence)
        assertEquals("a", result.matchingString)
    }

    @Test
    fun test5_verifyRepeatedIndicesCanBeGenerated() {
        val sequences = PcpSolver.generateCandidateSequences(tileCount = 2, maxDepth = 3).toList()

        // Verify repetitions exist, such as [1, 1], [2, 2], [1, 1, 1], [1, 2, 1]
        assertTrue(sequences.contains(listOf(1, 1)))
        assertTrue(sequences.contains(listOf(2, 2)))
        assertTrue(sequences.contains(listOf(1, 2, 1)))
        assertTrue(sequences.contains(listOf(2, 1, 2)))

        // Total sequences for n=2, d=3 is 2 + 4 + 8 = 14
        assertEquals(14, sequences.size)
        assertEquals(14L, PcpSolver.calculateTotalSequences(2, 3))
    }
}
