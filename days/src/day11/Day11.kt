package day11

import Day
import java.io.File

class Day11(private val seatingMap: List<List<SeatStatus>>) : Day {
    data class Coordinate(val row: Int, val column: Int)
    enum class SeatStatus(val symbol: Char) { EMPTY('L'), FLOOR('.'), OCCUPIED('#'), NONE(' ') }

    private fun List<List<SeatStatus>>.getStatus(seat: Coordinate) =
            this.getOrElse(seat.row) {  listOf() }.getOrElse(seat.column) { SeatStatus.NONE }
    private fun List<List<SeatStatus>>.isStatus(seat: Coordinate, status: SeatStatus) =
            this.getStatus(seat) == status

    private fun lineOfSight(seat: Coordinate, delta: Coordinate) = generateSequence(seat) {
        Coordinate(it.row + delta.row, it.column + delta.column)
    }
    private fun List<List<SeatStatus>>.inSight(seat: Coordinate, delta: Coordinate) =
            lineOfSight(seat, delta).drop(1).first { this.getStatus(it).let { status ->
                status == SeatStatus.OCCUPIED || status == SeatStatus.EMPTY
            } }.let { this.getStatus(it) == SeatStatus.OCCUPIED }
    private fun List<List<SeatStatus>>.adjacent(seat: Coordinate, delta: Coordinate) =
            this.getStatus(lineOfSight(seat, delta).first()) == SeatStatus.OCCUPIED

    private fun List<List<SeatStatus>>.occupiedSeats(seat: Coordinate, adjacentOnly: Boolean = true) =
        (-1..1).fold(0) { sum, rowDelta ->
            (-1..1).fold(sum) { rowSum, columnDelta ->
                rowSum + if(adjacentOnly) {
                    if (this.adjacent(seat, Coordinate(rowDelta, columnDelta))) { 1 } else { 0 }
                } else {
                    if (this.inSight(seat, Coordinate(rowDelta, columnDelta))) { 1 } else { 0 }
                }
            }
        }

    private fun List<List<SeatStatus>>.applyRound(adjacentOnly: Boolean) = this.let {
        val maxOccupied = if (adjacentOnly) { 4 } else { 5 } // Counts itself
        this.mapIndexed { r, row ->
            row.mapIndexed { c, seat ->
                when (seat) {
                    SeatStatus.NONE -> SeatStatus.NONE
                    SeatStatus.FLOOR -> SeatStatus.FLOOR
                    SeatStatus.EMPTY -> if (this.occupiedSeats(Coordinate(r, c), adjacentOnly) == 0) {
                        SeatStatus.OCCUPIED
                    } else {
                        SeatStatus.EMPTY
                    }
                    SeatStatus.OCCUPIED -> if (this.occupiedSeats(Coordinate(r, c), adjacentOnly) > maxOccupied) {
                        SeatStatus.EMPTY
                    } else {
                        SeatStatus.OCCUPIED
                    }
                }
            }
        }
    }.let {
        Round(this.diffWith(it), it)
    }
    private fun List<List<SeatStatus>>.diffWith(other: List<List<SeatStatus>>, doPrint: Boolean = false) =
        this.foldIndexed(0) { r, differences, row ->
            if (doPrint) {
                row.forEach { seat -> print(seat.symbol) }
                print(" $r ")
                other[r].forEach { seat -> print(seat.symbol) }
                println()
            }
            row.foldIndexed(differences) { c, differences, seat ->
                differences + if (seat == other[r][c]) { 0 } else { 1 }
            }
        }
    private fun List<List<SeatStatus>>.countStatus(status: SeatStatus) = this.fold(0) { count, row ->
        row.fold(count) { count, seat ->
            count + if (seat == status) { 1 } else { 0 }
        }
    }
    data class Round(val differences: Int, val map: List<List<SeatStatus>>)

    override fun puzzle1() = generateSequence(Round(0,seatingMap)) { it.map.applyRound(true) }
            .drop(1).first { it.differences == 0 }.map.countStatus(SeatStatus.OCCUPIED)

    override fun puzzle2(): Int? = null
//    generateSequence(Round(0,seatingMap)) { it.map.applyRound(false) }
//            .drop(1).first { it.differences == 0 }.map.countStatus(SeatStatus.OCCUPIED)

    companion object {
        fun buildFromFile(filename: String) = Day11(
                File(filename).readLines().map { row -> row.map { seat -> when(seat) {
                    'L' -> SeatStatus.EMPTY
                    '#' -> SeatStatus.OCCUPIED
                    else -> SeatStatus.FLOOR
                } } }
        )
    }
}