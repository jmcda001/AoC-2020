package day2

import Day
import java.io.File

interface Policy {
    fun isValid(password: String): Boolean
}
data class PolicyPuzzle1(val low: Int, val high: Int, val symbol: Char): Policy {
    override fun isValid(password: String) = password.count { it == symbol } in low..high
}

data class PolicyPuzzle2(val index1: Int, val index2: Int, val symbol: Char): Policy {
    override fun isValid(password: String) = (password[index1-1] == symbol) xor (password[index2-1] == symbol)
}

class Day2(private val passwords: List<Pair<Pair<Pair<Int, Int>, Char>, String>>): Day {
    override fun puzzle1() = passwords.map {
        (policy, password) -> Pair(PolicyPuzzle1(policy.first.first, policy.first.second, policy.second), password)
    }
            .map { (policy, password) -> policy.isValid(password) }
            .filter { it }.size

    override fun puzzle2() = passwords.map {
        (policy, password) -> Pair(PolicyPuzzle2(policy.first.first, policy.first.second, policy.second), password)
    }
            .map { (policy, password) -> policy.isValid(password) }
            .filter { it }.size

    companion object {
        fun buildFromFile(filename: String): Day2 {
            val policyPasswordRegex = "(\\d+)-(\\d+) (\\w): (\\w+)".toRegex()
            return Day2(File(filename).useLines { it.toList() }.asSequence()
                    .map { policyPasswordRegex.matchEntire(it) }
                    .filterNotNull()
                    .map { matchResult ->  matchResult.groupValues.drop(1) }
                    .map { Pair(Pair(Pair(it[0].toInt(), it[1].toInt()), it[2][0]), it[3])}
                    .toList()
            )
        }
    }
}
