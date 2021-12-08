import kotlin.math.abs

fun main() {
    fun part1(input: List<Int>): Int {
        val median = input.median()
        return input.sumOf { abs(it - median) }
    }

    fun triangle(n: Int) = n * (n + 1) / 2

    fun part2(input: List<Int>) = nats(0)
        .map { n -> input.sumOf { triangle(abs(it - n)) } }
        .zipWithNext()
        .takeWhile { (a, b) -> a > b }
        .last()
        .second

    val testInput = readNumbers("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readNumbers("Day07")
    println(part1(input))
    println(part2(input))
}
