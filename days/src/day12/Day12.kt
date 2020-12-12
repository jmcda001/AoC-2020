package day12

import Day
import java.io.File
import kotlin.math.abs

class Day12(private val instructions: List<Instruction>) : Day {
    enum class OPERATION { NORTH, EAST, SOUTH, WEST, FORWARD, LEFT, RIGHT, NONE;

        companion object {
            fun fromChar(letter: Char) = when (letter) {
                'N' -> NORTH
                'E' -> EAST
                'S' -> SOUTH
                'W' -> WEST
                'F' -> FORWARD
                'L' -> LEFT
                'R' -> RIGHT
                else -> NONE
            }
        }
    }
    data class Coordinate(val x: Int = 0, val y: Int = 0) {
        fun move(delta: Coordinate) = Coordinate(this.x + delta.x, this.y + delta.y)
        fun rotateAround(degrees: Int, other: Coordinate = Coordinate()) = when(degrees % 360) {
            90, -270 -> Coordinate(-(this.y-other.y) + other.x, (this.x - other.x) + other.y)
            180, -180 -> Coordinate(-(this.x - other.x) + other.x, -(this.y - other.y) + other.y)
            270, -90 -> Coordinate(this.y - other.y + other.x, -(this.x - other.x) + other.y)
            else -> this
        }
        fun manhattanDistance(other: Coordinate = Coordinate()) = abs(this.x - other.x) + abs(this.y - other.y)
        operator fun times(amount: Int) = Coordinate(this.x * amount, this.y * amount)
        operator fun plus(other: Coordinate) = Coordinate(this.x + other.x, this.y + other.y)
        companion object {
            fun Int.times(other: Coordinate) = other.times(this)
        }
        override fun toString() = "(${this.x},${this.y})"
    }
    data class Instruction(val action: OPERATION, val amount: Int)
    data class Waypoint(val location: Coordinate = Coordinate(10, 1)) {
        fun move(delta: Coordinate) = Waypoint(this.location.move(delta))
        fun rotateAround(amount: Int, anchor: Coordinate = Coordinate()) = Waypoint(this.location.rotateAround(amount, anchor))
        override fun toString() = "W${this.location.toString()}"
    }
    data class ShipTrajectory(
            val location: Coordinate = Coordinate(),
            val heading: Coordinate = Coordinate(1, 0),
            val waypoint: Waypoint = Waypoint(Coordinate(10, 1)) // In init?
    ) {
        fun apply(instruction: Instruction) = when (instruction.action) {
            OPERATION.NORTH -> this.move(Coordinate(0, instruction.amount))
            OPERATION.EAST -> this.move(Coordinate(instruction.amount, 0))
            OPERATION.SOUTH -> this.move(Coordinate(0, -instruction.amount))
            OPERATION.WEST -> this.move(Coordinate(-instruction.amount, 0))
            OPERATION.FORWARD -> this.move(this.heading * instruction.amount)
            OPERATION.RIGHT -> ShipTrajectory(this.location, this.heading.rotateAround(-instruction.amount))
            OPERATION.LEFT -> ShipTrajectory(this.location,this.heading.rotateAround(instruction.amount))
            OPERATION.NONE -> this
        }
        fun applyWithWaypoint(instruction: Instruction) = when(instruction.action) {
            OPERATION.NORTH -> this.moveWaypoint(Coordinate(0, instruction.amount))
            OPERATION.EAST -> this.moveWaypoint(Coordinate(instruction.amount, 0))
            OPERATION.SOUTH -> this.moveWaypoint(Coordinate(0, -instruction.amount))
            OPERATION.WEST -> this.moveWaypoint(Coordinate(-instruction.amount, 0))
            OPERATION.FORWARD -> this.move(this.waypoint.location * instruction.amount)
            OPERATION.RIGHT -> this.rotateWaypoint(-instruction.amount)
            OPERATION.LEFT -> this.rotateWaypoint(instruction.amount)
            OPERATION.NONE -> this
        }
        private fun rotateWaypoint(amount: Int) = this.waypoint.rotateAround(amount).let {
            ShipTrajectory(this.location, this.heading, it)
        }
        private fun moveWaypoint(delta: Coordinate) = this.waypoint.move(delta).let {
            ShipTrajectory(this.location, this.heading, it)
        }
        private fun move(delta: Coordinate) = this.location.move(delta).let {
            ShipTrajectory(it, this.heading, this.waypoint)
        }
        override fun toString(): String = "${this.location} towards ${this.waypoint} at H${this.heading}"
    }

    override fun puzzle1(): Int? = instructions.fold(ShipTrajectory()) { trajectory, instruction ->
        trajectory.apply(instruction)
    }.location.manhattanDistance()
    override fun puzzle2(): Int? = instructions.fold(ShipTrajectory()) { trajectory, instruction ->
        trajectory.applyWithWaypoint(instruction)
    }.location.manhattanDistance()

    companion object {
        private val instrRegex = "(\\w)(\\d+)".toRegex()
        fun buildFromFile(filename: String) = Day12(
                File(filename).readLines().mapNotNull { instrRegex.matchEntire(it)?.groupValues }
                        .map { Instruction(OPERATION.fromChar(it[1].toCharArray().first()), it[2].toInt()) }
        )
    }
}