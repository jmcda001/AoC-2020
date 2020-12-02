package day2

import Day
import java.io.File
import kotlin.test.assertEquals

data class Policy(val low: Int, val high: Int, val symbol: Char) {
    fun isValid(password: String) = password.count { it == symbol } in low..high
}
class Day2(private val passwords: List<Pair<Policy, String>>): Day {
    override fun puzzle1() = passwords.map { (policy, password) -> policy.isValid(password) }
            .filter { it }.size

    override fun puzzle2() = null

    companion object {
        fun buildFromFile(filename: String): Day2 {
            val policyPasswordRegex = "(\\d+)-(\\d+) (\\w): (\\w+)".toRegex()
            return Day2(File(filename).useLines { it.toList() }.asSequence()
                    .map { policyPasswordRegex.matchEntire(it) }
                    .filterNotNull()
                    .map { matchResult ->  matchResult.groupValues.drop(1) }
                    .map { Pair(Policy(it[0].toInt(), it[1].toInt(), it[2][0]), it[3])}
                    .toList()
            )
        }
    }
}
