import day1.Day1
import day10.Day10
import day11.Day11
import day2.Day2
import day3.Day3
import day4.Day4
import day5.Day5
import day6.Day6
import day7.Day7
import day8.Day8
import day9.Day9

fun main(args: Array<String>) {
    val evaluateDay = { day: Day -> DayResults(day.puzzle1(), day.puzzle2())}
    val days = listOf(
            {fn: String -> evaluateDay(Day1.buildFromFile(fn))},
            {fn: String -> evaluateDay(Day2.buildFromFile(fn))},
            {fn: String -> evaluateDay(Day3.buildFromFile(fn))},
            {fn: String -> evaluateDay(Day4.buildFromFile(fn))},
            {fn: String -> evaluateDay(Day5.buildFromFile(fn))},
            {fn: String -> evaluateDay(Day6.buildFromFile(fn))},
            {fn: String -> evaluateDay(Day7.buildFromFile(fn))},
            {fn: String -> evaluateDay(Day8.buildFromFile(fn))},
            {fn: String -> evaluateDay(Day9.buildFromFile(fn))},
            {fn: String -> evaluateDay(Day10.buildFromFile(fn))},
            {fn: String -> evaluateDay(Day11.buildFromFile(fn))}
    )

    if (args.isEmpty()) {
        days.forEachIndexed { i, day ->
            println("Day ${i + 1}: ${day("day${i + 1}/input.txt")}")
        }
    } else {
        println("Day ${days.size}: ${days.last()("day${days.size}/${args.first()}")}")
    }
}