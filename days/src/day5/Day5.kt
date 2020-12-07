package day5

import Day
import java.io.File

class Day5(private val boardingPasses: List<String>): Day {
    private fun seatId(ticket: String) = ticket.fold(0) { id, curr ->
        id * 2 + if (curr in listOf('B', 'R')) { 1 } else { 0 }
    }

    override fun puzzle1(): Int? = boardingPasses.map { seatId(it) }.max()
    override fun puzzle2(): Int? = boardingPasses.map { seatId(it) }.sorted().let {passes ->
        passes.filterIndexed { i, id -> i >= 1 && passes[i-1] != id - 1 }
                .first() - 1
    }

    companion object {
        fun buildFromFile(filename: String) = Day5(File(filename).readLines())
    }
}