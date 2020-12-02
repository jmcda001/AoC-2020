package day3

import Day
import java.io.File

class Day3(private val expenseReport: List<Int>): Day {
    override fun puzzle1() = null

    override fun puzzle2(): Int? {
        return null
    }

    companion object {
        fun buildFromFile(filename: String) = Day3(
                File(filename).useLines { it.toList() }.map { it.toInt() }
        )
    }
}