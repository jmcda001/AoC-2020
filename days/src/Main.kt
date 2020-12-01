import day1.Day1

fun main() {
    val evaluateDay = { day: Day -> DayResults(day.puzzle1(), day.puzzle2())}
    print("Day 1: ${evaluateDay(Day1.buildFromFile("day1/input1.txt"))}")
}