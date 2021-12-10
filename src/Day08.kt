import java.lang.Integer.parseInt

fun main() {
    val digits = mapOf(
        "abcefg" to 0,
        "cf" to 1,
        "acdeg" to 2,
        "acdfg" to 3,
        "bcdf" to 4,
        "abdfg" to 5,
        "abdefg" to 6,
        "acf" to 7,
        "abcdefg" to 8,
        "abcdfg" to 9,
    )

    fun decode(scrambled: String, mapping: Map<Char, Char>): Int? = scrambled
        .map(mapping::getValue)
        .sorted()
        .joinToString("")
        .let(digits::get)

    fun decodeLine(line: String): List<Int> {
        val (evidences, outputs) = line.split(" | ").map { it.split(" ") }
        return permutations("abcdefg".toList())
            .map { it.zip("abcdefg".toList()).toMap() }
            .first { mapping -> evidences.all { decode(it, mapping) != null } }
            .let { mapping -> outputs.map { decode(it, mapping)!! } }
    }

    fun part1(input: List<String>) = input
        .flatMap(::decodeLine)
        .count { it in listOf(1, 4, 7, 8) }

    fun part2(input: List<String>) = input
        .map(::decodeLine)
        .sumOf { it.joinToString("").let(::parseInt) }

    val input = readLines("Day08")
    showAnswer(part1(input))
    showAnswer(part2(input))
}
