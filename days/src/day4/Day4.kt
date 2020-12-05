package day4

import Day
import java.io.File

class Day4(private val passports: List<Map<String, String>>) : Day {
    enum class PassportField {
        BYR, IYR, EYR, HGT, HCL, ECL, PID;//, CID

        override fun toString() = super.toString().toLowerCase()
        private val _eyeColors = listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
        fun validate(value: String) = when (this) {
            BYR -> value.toInt() in 1920..2002
            IYR -> value.toInt() in 2010..2020
            EYR -> value.toInt() in 2020..2030
            HGT -> "(\\d+)(in|cm)".toRegex().matchEntire(value)?.let {
                when (it.groupValues[2]) {
                    "in" -> it.groupValues[1].toInt() in 59..76
                    "cm" -> it.groupValues[1].toInt() in 150..193
                    else -> false
                }
            } ?: false
            ECL -> value in _eyeColors
            HCL -> "#[0-9a-f]{6}".toRegex().matches(value)
            PID -> "\\d{9}".toRegex().matches(value)
        }
    }

    private fun Map<String, String>.isValidPassport(useStrict: Boolean = false): Boolean {
        return PassportField.values().all { field ->
            field.toString() in this && (!useStrict || field.validate(this[field.toString()]!!))
        }
    }

    override fun puzzle1(): Int? = passports.count { it.isValidPassport() }
    override fun puzzle2(): Int? = passports.count { it.isValidPassport(useStrict = true) }

    companion object {
        private fun String.buildPassport() = this.trim().split(' ').associate { keyValue ->
            keyValue.split(':').let {
                Pair(it[0].trim(), it[1])
            }
        }

        fun buildFromFile(filename: String): Day4 {
            var currentCombined = ""
            val passports = mutableListOf<Map<String, String>>()
            File(filename).useLines { lines ->
                lines.forEach { line ->
                    if (line.isEmpty()) {
                        passports.add(currentCombined.buildPassport())
                        currentCombined = ""
                    } else {
                        currentCombined += "$line "
                    }
                }
            }
            passports.add(currentCombined.buildPassport())
            return Day4(passports)
        }
    }
}