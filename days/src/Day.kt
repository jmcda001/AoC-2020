
data class DayResults(val puzzle1: Int?, val puzzle2: Int?)

interface Day {
    fun puzzle1(): Int?
    fun puzzle2(): Int?
}