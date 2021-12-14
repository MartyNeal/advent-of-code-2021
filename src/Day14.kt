typealias Polymer = Map<Pair<Char, Char>, Long> // a count of each time a pair occurs

typealias Rules = Map<Pair<Char, Char>, Char>

fun polymerFromString(template: String) = template
    .asSequence()
    .plus(' ')
    .zipWithNext()
    .groupingBy { it }
    .fold(0L) { a, _ -> a + 1L }

fun Polymer.stepWithRules(rules: Rules): Polymer = buildMap {
    putAll(this@stepWithRules.filter { it.key !in rules })
    for ((pair, value) in this@stepWithRules.filter { it.key in rules }) {
        val (a, b) = pair
        put(a to rules[pair]!!, (this[a to rules[pair]] ?: 0L) + value)
        put(rules[pair]!! to b, (this[rules[pair] to b] ?: 0L) + value)
    }
}

val Polymer.elementCounts: Map<Char, Long>
    get() = this.entries.groupingBy { it.key.first }.fold(0L) { a, e -> a + e.value }

fun main() {

    data class Instructions(val template: String, val rules: Map<Pair<Char, Char>, Char>) {
        constructor(input: List<String>) : this(
            template = input[0],
            rules = input
                .drop(2)
                .associate { (it[0] to it[1]) to it.last() })
    }

    fun elementCountMaxDelta(instructions: Instructions, steps: Int): Long =
        generateSequence(polymerFromString(instructions.template)) { it.stepWithRules(instructions.rules) }
            .elementAt(steps)
            .elementCounts
            .values
            .sorted()
            .let { it.last() - it.first() }

    fun part1(instructions: Instructions) = elementCountMaxDelta(instructions, 10)
    fun part2(instructions: Instructions) = elementCountMaxDelta(instructions, 40)

    val testInput = Instructions(readLines("Day14_test"))
    check(part1(testInput).also(::println) == 1588L)
    check(part2(testInput).also(::println) == 2188189693529L)

    val input = Instructions(readLines("Day14"))
    showAnswer(part1(input))
    showAnswer(part2(input))
}
