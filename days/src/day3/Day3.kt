package day3

import Day
import java.io.File

class Day3(private val terrain: List<CharArray>): Day {
    data class Slope(val down: Int, val right: Int)
    data class SlopeCount(val slope: Slope, var col: Int = 0, var trees: Int = 0) {
        fun addTree(row: Int, terrain: CharArray) = this.also {
            if (row % slope.down == 0 && terrain[col] == '#') { trees += 1 }
        }
        fun move(row: Int, max: Int) = this.also{
            if (row % slope.down == 0) { col = (col + slope.right) % max }
        }
    }

    private fun List<SlopeCount>.simulate(startCol: Int = 0) = this.also {
        this.forEach { it.trees = 0; it.col = startCol }
        terrain.forEachIndexed { row, terrain ->
            this.forEach {
                it.addTree(row, terrain).move(row, terrain.size)
            }
        }
    }

    override fun puzzle1()= listOf(SlopeCount(Slope(1, 3))).simulate().fold(0) { sum, it ->
        sum + it.trees }

    override fun puzzle2() = arrayOf(Slope(1,1), Slope(1,3), Slope(1,5),
                Slope(1,7), Slope(2,1))
            .map { slope -> SlopeCount(slope) }
            .simulate()
            .fold(1) { product, current -> product * current.trees }

    companion object {
        fun buildFromFile(filename: String) = Day3(
                File(filename).useLines { it.toList() }.map { it.toCharArray() }
        )
    }
}