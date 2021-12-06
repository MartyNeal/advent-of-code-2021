import java.lang.Integer.parseInt

fun main() {
    fun part1(input: List<Int>, days: Int = 80): Int {
        if (days == 0) return input.size
        return part1(input.map { if(it == 0) 6 else it - 1 } + List(input.count { it == 0 }) { 8 } , days - 1)
    }

    fun part2(input: List<Int>, days: Int = 256): Long {
        fun helper(input: Map<Int, Long>, days: Int): Long =
            if (days == 0)
                input.values.sum()
            else
                helper(input.let { mapOf(
                    0 to it.getOrDefault(1, 0),
                    1 to it.getOrDefault(2, 0),
                    2 to it.getOrDefault(3, 0),
                    3 to it.getOrDefault(4, 0),
                    4 to it.getOrDefault(5, 0),
                    5 to it.getOrDefault(6, 0),
                    6 to it.getOrDefault(7, 0) + it.getOrDefault(0, 0),
                    7 to it.getOrDefault(8, 0),
                    8 to it.getOrDefault(0, 0),
                ) }, days - 1)

        return helper(input
            .groupingBy { it }
            .eachCount() // perhaps my favorite stdlib function
            .mapValues { it.value.toLong() }, days)
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
