import day1.Day1
import day2.Day2
import day3.Day3
import day4.Day4
import day5.Day5
import day6.Day6
import day7.Day7
import day8.Day8

fun main() {
    val evaluateDay = { day: Day -> DayResults(day.puzzle1(), day.puzzle2())}

    println("Day 1: ${evaluateDay(Day1.buildFromFile("day1/input.txt"))}")
    println("Day 2: ${evaluateDay(Day2.buildFromFile("day2/input.txt"))}")
    println("Day 3: ${evaluateDay(Day3.buildFromFile("day3/input.txt"))}")
    println("Day 4: ${evaluateDay(Day4.buildFromFile("day4/input.txt"))}")
    println("Day 5: ${evaluateDay(Day5.buildFromFile("day5/input.txt"))}")
    println("Day 6: ${evaluateDay(Day6.buildFromFile("day6/input.txt"))}")
    println("Day 7: ${evaluateDay(Day7.buildFromFile("day7/input.txt"))}")
    println("Day 8: ${evaluateDay(Day8.buildFromFile("day8/input.txt"))}")
}