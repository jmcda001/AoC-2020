package day6

import Day
import java.io.File

class Day6(private val customsForms: List<String>) : Day {
    override fun puzzle1(): Int? = customsForms.map { form -> form.filterNot { it == ' ' }.toCharArray().distinct().size }.sum()
    override fun puzzle2(): Int? = customsForms.map { form ->
        form.trim().split(' ')
                .map { personAnswers -> personAnswers.toSet() }
                .reduce { all, curr -> all.intersect(curr) }
                .size
    }.sum()

    companion object {
        fun buildFromFile(filename: String): Day6 =
                Day6(File(filename).readText().let { lines ->
                    lines.split("\n\n").map { line ->
                        line.replace('\n',' ')
                    }
                })
    }
}