import java.lang.Integer.parseInt

data class Board(val numbers: List<Int>) {

    fun isWinning(called: List<Int>) = listOf(
        (0..4).map { it + 0 },
        (0..4).map { it + 5 },
        (0..4).map { it + 10 },
        (0..4).map { it + 15 },
        (0..4).map { it + 20 },
        listOf(0, 5, 10, 15, 20).map { it + 0 },
        listOf(0, 5, 10, 15, 20).map { it + 1 },
        listOf(0, 5, 10, 15, 20).map { it + 2 },
        listOf(0, 5, 10, 15, 20).map { it + 3 },
        listOf(0, 5, 10, 15, 20).map { it + 4 },
    ).any { numbers.slice(it).all { it in called } }

    fun sumOfUncalled(called: List<Int>) = numbers
        .filter { it !in called }
        .sum()

    companion object {
        fun parse(lines: List<String>) = lines
            .flatMap { it.trim().split(" +".toRegex()) }
            .map(::parseInt)
            .let(::Board)
    }
}

fun main() {

    fun part1(allCalled: List<Int>, boards: List<Board>): Int {
        return allCalled
            .indices
            .map(allCalled::take)
            .firstNotNullOf { called ->
                boards
                    .filter { it.isWinning(called) }
                    .map { it.sumOfUncalled(called) * called.last() }
                    .firstOrNull()
            }
    }

    fun part2(allCalled: List<Int>, boards: List<Board>): Int {
        return allCalled
            .indices
            .map(allCalled::dropLast)
            .firstNotNullOf { called ->
                boards
                    .filter { !it.isWinning(called.dropLast(1)) }
                    .map { it.sumOfUncalled(called) * called.last() }
                    .firstOrNull()
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readLines("Day04_test")
    val (testCalled, testBoards) = inputToBoards(testInput)

    check(part1(testCalled, testBoards) == 4512)
    check(part2(testCalled, testBoards) == 1924)

    val input = readLines("Day04")
    val (called, boards) = inputToBoards(input)
    println(part1(called, boards))
    println(part2(called, boards))
}

fun inputToBoards(testInput: List<String>): Pair<List<Int>, List<Board>> {
    val allCalled = testInput[0].split(",").map(::parseInt)
    val boards = testInput
        .drop(2)
        .windowed(5, 6, true)
        .map { Board.parse(it) }
    return Pair(allCalled, boards)
}
