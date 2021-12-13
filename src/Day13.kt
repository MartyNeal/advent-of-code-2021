import java.lang.Integer.parseInt
import kotlin.math.abs

fun main() {

    data class Point(val x: Int, val y: Int)

    data class FoldLine(val axis: Char, val at: Int)

    fun parseInput(input: List<String>): Pair<Set<Point>, List<FoldLine>> {
        val points = input
            .takeWhile { it.trim() != "" }
            .map { it.split(",").let { Point(parseInt(it[0]), parseInt(it[1])) } }
            .toSet()
        val folds = input
            .subList(points.size + 1, input.size)
            .map { it.split("=").let { FoldLine(it[0].last(), parseInt(it[1])) } }
        return points to folds
    }

    fun fold(points: Set<Point>, fold: FoldLine): Set<Point> =
        points.fold(setOf()) { a, e ->
            a + when (fold.axis) {
                'x' -> Point(fold.at - abs(fold.at - e.x), e.y)
                'y' -> Point(e.x, fold.at - abs(fold.at - e.y))
                else -> error("unknown fold axis")
            }
        }


    fun part1(input: Pair<Set<Point>, List<FoldLine>>): Int = input.let { (points, folds) -> fold(points, folds[0]).size }

    fun toString(points: Set<Point>) = buildString {
        val maxY = points.maxOf { it.y }
        val maxX = points.maxOf { it.x }
        for (y in (0..maxY)) {
            for (x in (0..maxX)) {
                append(if (Point(x, y) in points) "#" else ".")
            }
            appendLine()
        }
    }

    fun part2(input: Pair<Set<Point>, List<FoldLine>>) = input.let { (points, folds) ->
        folds.fold(points, ::fold) // <-- yeah, not confusing at all ;-)
            .let(::toString)
    }

    val testInput = parseInput(readLines("Day13_test"))
    check(part1(testInput).also(::println) == 17)
    println(part2(testInput))

    val input = parseInput(readLines("Day13"))
    showAnswer(part1(input))
    showAnswer(part2(input))
}
