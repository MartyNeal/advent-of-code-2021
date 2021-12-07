import java.lang.Integer.parseInt

fun main() {
    fun part1(input: List<Int>, days: Int = 80): Int {
        if (days == 0) return input.size
        return part1(input.map { if (it == 0) 6 else it - 1 } + List(input.count { it == 0 }) { 8 }, days - 1)
    }

    fun part2(input: List<Int>, days: Int = 256): Long {
        fun helper(input: Map<Int, Long>, days: Int): Long =
            if (days == 0)
                input.values.sum()
            else {
                helper((0..8).associateWith { input.getValue((it + 1) % 9) + if (it == 6) input.getValue(0) else 0 }, days - 1)
            }

        return helper(input
            .groupingBy { it }
            .eachCount() // perhaps my favorite stdlib function
            .mapValues { it.value.toLong() }
            .withDefault { 0L }, days
        )
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")[0].split(",").map(::parseInt)
    check(part1(testInput) == 5934)
    check(part2(testInput) == 26984457539L)
//
    val input = readInput("Day06")[0].split(",").map(::parseInt)
    println(part1(input))
    println(part2(input))
}
