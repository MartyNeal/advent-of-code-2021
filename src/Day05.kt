import java.lang.Integer.*

private infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}

fun main() {
    fun part1(input: List<String>): Int {
        val field = Array(1000) { Array(1000) { 0 } }
        for (line in input) {
            val (x1, y1, x2, y2) = Regex("(\\d+),(\\d+) -> (\\d+),(\\d+)")
                .find(line)!!
                .destructured
                .toList()
                .map(::parseInt)

            if (x1 == x2) {
                for (y in y1 toward y2)
                    field[y][x1]++
            } else if (y1 == y2) {
                for (x in x1 toward x2)
                    field[y1][x]++
            } else {
//                for ((x,y) in (x1 toward x2).zip(y1 toward y2))
//                    field[y][x]++
            }
        }
//        field.take(10).forEach { row -> println(row.take(10).toList()) }
        return field.sumOf { row -> row.count { it > 1 } }
    }

    fun part2(input: List<String>): Int {
        val field = Array(1000) { Array(1000) { 0 } }
        for (line in input) {
            val (x1, y1, x2, y2) = Regex("(\\d+),(\\d+) -> (\\d+),(\\d+)")
                .find(line)!!
                .destructured
                .toList()
                .map(::parseInt)

            if (x1 == x2) {
                for (y in y1 toward y2)
                    field[y][x1]++
            } else if (y1 == y2) {
                for (x in x1 toward x2)
                    field[y1][x]++
            } else {
                for ((x,y) in (x1 toward x2).zip(y1 toward y2))
                    field[y][x]++
            }
        }
//        field.take(10).forEach { row -> println(row.take(10).toList()) }
        return field.sumOf { row -> row.count { it > 1 } }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readLines("Day05_test")

    println(part1(testInput))
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readLines("Day05")
    println(part1(input))
    println(part2(input))
}
