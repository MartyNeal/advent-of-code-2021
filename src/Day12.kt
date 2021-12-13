import java.util.*

typealias Graph = Map<String, Set<String>>
typealias Path = List<String>

fun main() {

    fun parseInput(input: List<String>): Graph = input
        .flatMap { it.split("-").let { listOf(it, it.reversed()) } }
        .groupBy({ it[0] }) { it[1] }
        .mapValues { if (it.key == "end") setOf() else it.value.filter { it != "start" }.toSet() }

    fun part1(input: Graph) =
        sequence {
            val q = ArrayDeque(listOf(listOf("start")))
            for (path in generateSequence { q.poll() }) {
                input.getValue(path.last())
                    .filter { it.all(Char::isUpperCase) || it !in path }
                    .forEach { if (it == "end") yield(path) else q.add(path + it) }
            }
        }.count()

    fun Path.hasNotVisitedSmallCaveTwice(): Boolean {
        val s = mutableSetOf<String>()
        return asSequence().filter { it.all(Char::isLowerCase) }.all { s.add(it) }
    }

    fun part2(input: Graph) =
        sequence {
            val q = ArrayDeque(listOf(listOf("start")))
            for (path in generateSequence { q.poll() }) {
                input.getValue(path.last())
                    .filter { it.all(Char::isUpperCase) || it !in path || path.hasNotVisitedSmallCaveTwice() }
                    .forEach { if (it == "end") yield(path) else q.add(path + it) }
            }
        }.count()

    val testInput = parseInput(readLines("Day12_test"))
    check(part1(testInput).also(::println) == 19)
    check(part2(testInput).also(::println) == 103)

    val input = parseInput(readLines("Day12"))
    showAnswer(part1(input))
    showAnswer(part2(input))
}
