package day4

import Day
import java.io.File

class Day4(private val passports: List<Map<String, String>>) : Day {
    private fun Map<String, String>.isValidPassport(): Boolean {
        val requiredFields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")//, "cid")
        val optional = listOf("cid")
        return requiredFields.all { field -> field in this }
    }

    override fun puzzle1(): Int? = passports.count { it.isValidPassport() }
    override fun puzzle2(): Int? = null

    companion object {
        fun buildFromFile(filename: String): Day4 {
            var currentCombined: String = ""
            val passports = mutableListOf<Map<String, String>>()
            var count = 0
            val fields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid", "cid")
            File(filename).useLines { lines ->
                lines.forEach { line ->
                    if (line.isEmpty()) {
                        val keys = currentCombined.trim().split(' ').map {kv ->
                            kv.split(':').let { it[0].trim() }
                        }
                        if (keys.size != keys.distinct().size || keys.all { it in fields }) {
                            println("$keys (${keys.size}:${keys.distinct().size}")
                        }
                        if (keys.size == 8 || (keys.size == 7 && !keys.contains("cid"))) {
                            count++
                        }
                        passports.add(currentCombined.trim().split(' ').associate { keyValue ->
                            keyValue.split(':').let {
                                Pair(it[0].trim(), it[1]) }
                        })
                        currentCombined = ""
                    } else {
                        currentCombined += line + " "
                    }
                }
            }
            println("${passports.size} passports to process, $count truly valid")
            return Day4(passports)
        }
    }
}