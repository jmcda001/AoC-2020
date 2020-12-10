package day9

import Day
import java.io.File

class Day9(private val xmasCode: List<Long>) : Day {
    private val preambleLength = 25

    private fun MutableList<Long>.isValidXMAS(num: Long)= this.asSequence().associateBy { num - it }
            .let { adjusted -> this.any { adjusted.contains(it) } }

    private fun List<Long>.findNonXmas(preambleLength: Int) =
        this.take(preambleLength).toMutableList().let { window ->
            this.drop(preambleLength).filterNot { current ->
                window.isValidXMAS(current).also {
                    window.removeAt(0)
                    window.add(current)
                }
            }.first()
        }
    override fun puzzle1(): Int? = xmasCode.findNonXmas(preambleLength).toInt()

    override fun puzzle2(): Int? {
        return xmasCode.findNonXmas(preambleLength).let { target ->
            var window = 0
            val sum = xmasCode.foldIndexed(0L) { i, sum, current ->
                val curr = if (current > target) {
                    window = i + 1
                    0L
                } else if (sum + current > target) {
                    var currentSum = sum + current
                    while (currentSum > target) {
                        currentSum -= xmasCode[window]
                        window++
                    }
                    currentSum
                } else {
                    sum + current
                }
                if (curr == target) {
                    val windowed = xmasCode.subList(window, i + 1)
                    return (windowed.min()!! + windowed.max()!!).toInt()
                }
                curr
            }
            sum
        }.toInt()
    }

    companion object {
        fun buildFromFile(filename: String) = Day9(
                File(filename).readLines().map { it.toLong() }
        )
    }
}