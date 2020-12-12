package day1

import Day
import java.io.File
import kotlin.test.assertEquals

class Day1(private val expenseReport: List<Int>): Day {
    override fun puzzle1(): Int? = expenseReport.asSequence().associateBy { 2020 - it }.let { adjusted ->
        val result = expenseReport.filter { adjusted.contains(it) }
        if (result.size != 2) {
            null
        } else {
            result.first() * result.last()
        }
    }

    override fun puzzle2(): Int? {
        var possibleSums = mutableSetOf<Int>()
        expenseReport.forEachIndexed { i, a ->
            expenseReport.subList(0,i).filter { b -> possibleSums.contains(a + b) }.forEach { b ->
                return a * b * (2020 - (a + b))
            }
            possibleSums.add(2020 - a)
        }
        return null
    }

    companion object {
        fun buildFromFile(filename: String) = Day1(
                File(filename).useLines { it.toList() }.map { it.toInt() }
        )
    }
}

fun `test day 1 puzzle 1 (expense report sum to 2020)`() {
    val input = listOf(1721, 979, 366, 299, 675, 1456)
    val day1 = Day1(input)

    val result = day1.puzzle1()
    assertEquals(514579, result)
}
