import java.lang.Integer.parseInt

fun main() {
    fun part1(input: List<String>): Int {
        val sum = input.fold(List(input[0].length) { 0 }) { a, e ->
            e.map(Char::digitToInt).zip(a) { x, y -> if (x == 0) y - 1 else y + 1 }
        }
        val gamma = sum.joinToString("") { s -> if (s < 0) "0" else "1" }.let { parseInt(it, 2) }
        val epsilon = sum.joinToString("") { s -> if (s < 0) "1" else "0" }.let { parseInt(it, 2) }
        return gamma * epsilon
    }

    fun part2(input: List<String>): Int {
        val l = input.toMutableList()
        var oxy = 0
        for (i in 0..input[0].length) {
            val mostCommon = l.fold(0) { a, e -> if (e[i] == '0') a - 1 else a + 1 }.let { if (it < 0) '0' else '1' }
            l.removeIf { it[i] != mostCommon }
            if (l.size == 1) {
                oxy = parseInt(l[0], 2)
                break
            }
        }

        l.clear()
        l.addAll(input)
        var co2 = 0
        for (i in 0..input[0].length) {
            val mostCommon = l.fold(0) { a, e -> if (e[i] == '0') a - 1 else a + 1 }.let { if (it < 0) '0' else '1' }
            l.removeIf { it[i] == mostCommon }
            if (l.size == 1) {
                co2 = parseInt(l[0], 2)
                break
            }
        }
        return oxy * co2
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readLines("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readLines("Day03")
    println(part1(input)) // 2648450
    println(part2(input)) // 2845944
}
