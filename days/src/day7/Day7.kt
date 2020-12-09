package day7

import Day
import java.io.File

class Day7(private val bagRules: Map<String, List<Pair<Int, String>>>) : Day {
    private lateinit var containmentGraph: Map<String, List<String>>

    init {
        val graph: MutableMap<String, MutableList<String>> = mutableMapOf()
        bagRules.forEach { (containingBag, contained) ->
            contained.forEach { (amount, bag) ->
                val newEdgeList = graph.getOrDefault(bag, mutableListOf())
                newEdgeList.add(containingBag)
                graph[bag] = newEdgeList
            }
        }
        containmentGraph = graph.toMap().mapValues { (_, values) -> values.toList() }
    }

    private fun containmentBfs(start: String): Map<String, List<String>> {
        val outerBags = mutableMapOf<String, List<String>>()
        data class BfsNode(val node: String, val history: List<String>) {
            val distance: Int
                get() = history.size
        }
        val wavefront = mutableListOf<BfsNode>()
        wavefront.add(BfsNode(start, listOf()))
        while (wavefront.isNotEmpty()) {
            val current = wavefront.removeAt(0)
            val containingBags = containmentGraph[current.node]
            outerBags[current.node] = current.history
            if (containingBags != null && containingBags.isNotEmpty()) {
                containingBags.forEach { bag ->
                    wavefront.add(BfsNode(bag, listOf(current.node) + current.history))
                }
            }
        }
        outerBags.remove(start)
        return outerBags.toMap()
    }

    private fun containsDfs(start: String): Int = bagRules[start]!!.map {
        it.first * (containsDfs(it.second) + 1) }.sum()
    private fun totalBagsContains(start: String) = containsDfs(start)

    override fun puzzle1(): Int? = containmentBfs("shiny gold").size
    override fun puzzle2(): Int? = totalBagsContains("shiny gold")

    companion object {
        fun buildFromFile(filename: String): Day7 {
            val bag = "(\\w+ \\w+)"
            val bagRule = "$bag bags contain (.*)\\.".toRegex()
            val amountColor = "(\\d) $bag bags?".toRegex()
            return Day7(File(filename).readLines()
                    .mapNotNull { bagRule.matchEntire(it)?.groupValues?.drop(1) }
                    .associate { rule ->
                        val contained = rule.last().split(",")
                                .mapNotNull { bag -> amountColor.matchEntire(bag.trim())?.groupValues }
                                .map { amountBag -> Pair(amountBag[1].toInt(), amountBag[2]) }
                        Pair(rule.first(), contained)
                    }
            )
        }
    }
}