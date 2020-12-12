package day10

import Day
import java.io.File

class Day10(values: List<Int>) : Day {
    private val jolts = values.sorted().let { listOf(0) + it + it.last() }
    override fun puzzle1(): Int? = jolts.mapIndexed { i, jolt ->
            jolt - if (i > 0) { jolts[i-1] } else { 0 }
        }.filterNot { it == 2 }.partition { it == 1 }.let { (ones, threes) ->
            ones.size * (threes.size)
        }

    private fun List<Long>.joltArrangements() = mutableMapOf(this.first() to 1L).let { memoization ->
        this.mapIndexed { i, jolt ->
            memoization.getOrPut(jolt) {
                listOf(1, 2, 3).takeWhile { i >= it && this[i - it] >= jolt - 3L }
                        .mapNotNull { memoization[this[i - it]] }.sum()
            }
        }.last()
    }
    override fun puzzle2(): Int? = jolts.map { it.toLong() }.joltArrangements().also {
        println("Solution: $it")
    }.toInt()

    companion object {
        fun buildFromFile(filename: String) = Day10(
                File(filename).readLines().map { it.toInt() }
        )
    }
}