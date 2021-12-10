import java.util.*

fun main() {

    val errorPoints = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137,
    )

    val matchingPair = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>',
    )

    fun syntaxErrorScore(line: String): Int {
        line.fold(ArrayDeque<Char>()) { a, e ->
            when (e) {
                in "([{<" -> a.apply { push(e) }
                matchingPair[a.pop()] -> a
                else -> return errorPoints[e]!!
            }
        }
        return 0
    }

    fun completionScore(line: String) = line.fold(0L) { a, e -> a * 5L + " )]}>".indexOf(e) }

    fun part1(input: List<String>) = input.sumOf(::syntaxErrorScore)

    fun closingString(line: String) = line
        .fold(ArrayDeque<Char>()) { a, e ->
            when (e) {
                in "([{<" -> a.apply { push(e) }
                matchingPair[a.pop()] -> a
                else -> error("corrupt")
            }
        }
        .map(matchingPair::get)
        .joinToString("")

    fun part2(input: List<String>) = input
        .filter { syntaxErrorScore(it) == 0 }
        .map(::closingString)
        .map(::completionScore)
        .median()

    val testInput = readLines("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readLines("Day10")
    showAnswer(part1(input))
    showAnswer(part2(input))
}
