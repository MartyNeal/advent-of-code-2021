import java.lang.Integer.parseInt

fun main() {
    fun part1(input: List<String>) = input.map { parseInt(it) }
        .zipWithNext()
        .count { (a, b) -> a < b }

    fun part2(input: List<String>): Int = input.map { parseInt(it) }
        .windowed(3)
        .map { (a, b, c) -> a + b + c }
        .zipWithNext()
        .count { (a, b) -> a < b }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
