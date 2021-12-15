import java.util.*

data class Node(val i: Int, val j: Int)

interface Graph<T> {
    fun nodes(): List<T>
    fun neighbors(node: T): List<T>
    fun length(u: T, v: T): Long
    fun dijkstra(start: T): Map<T, T> {
        val dist = mutableMapOf<T, Long>()
        val prev = mutableMapOf<T, T>()
        val q = PriorityQueue(compareBy<T> { dist[it] })
        dist[start] = 0L
        q += start

        for (u in generateSequence { q.poll() }) {
            val uDist = dist[u] ?: continue
            for (v in this.neighbors(u)) {
                val newPath = uDist + this.length(u, v)
                if (dist[v]?.let { newPath < it } != false) {
                    dist[v] = newPath
                    prev[v] = u
                    q -= v
                    q += v
                }
            }
        }

        return prev.toMap()
    }
}

abstract class MatrixGraph(val m: List<List<Long>>) : Graph<Node> {
    override fun nodes() = (0..m.lastIndex).flatMap { i -> (0..m.lastIndex).map { j -> Node(i, j) } }
    override fun length(u: Node, v: Node) = m[v.i][v.j]
    fun pathSum(start: Node, end: Node) = pathSum(start, end, dijkstra(start))
    fun pathSum(start: Node, end: Node, paths: Map<Node, Node>) =
        generateSequence(Node(end.i, end.j)) { paths[it] }
            .toList()
            .reversed()
            .zipWithNext(::length).sum() + m[start.i][start.j]
}

class Graph4Way(m: List<List<Long>>) : MatrixGraph(m)
{
    override fun neighbors(node: Node) = node.let { (i, j) ->
        listOf(
            Node(i - 1, j),
            Node(i + 1, j),
            Node(i, j - 1),
            Node(i, j + 1))
            .filter { (i, j) -> i in 0..m.lastIndex && j in 0..m.lastIndex }
    }
}

fun main() {

    fun part1(lines: List<String>): Long {
        val m = lines.map { it.digits().map(Int::toLong) }
        return Graph4Way(m).pathSum(Node(0, 0), Node(m.lastIndex, m.lastIndex)) - m[0][0]
    }
    fun part2(lines: List<String>): Long {
        val tile = lines.map { it.digits().map(Int::toLong) }

        //... and I call myself a programmer... shameful
        val m =
        tile.map { line -> line.map { ((it + 0) - 1) % 9 + 1 } + line.map { ((it + 1) - 1) % 9 + 1 } + line.map { ((it + 2) - 1) % 9 + 1 } + line.map { ((it + 3) - 1) % 9 + 1 } + line.map { ((it + 4) - 1) % 9 + 1 } } +
        tile.map { line -> line.map { ((it + 1) - 1) % 9 + 1 } + line.map { ((it + 2) - 1) % 9 + 1 } + line.map { ((it + 3) - 1) % 9 + 1 } + line.map { ((it + 4) - 1) % 9 + 1 } + line.map { ((it + 5) - 1) % 9 + 1 } } +
        tile.map { line -> line.map { ((it + 2) - 1) % 9 + 1 } + line.map { ((it + 3) - 1) % 9 + 1 } + line.map { ((it + 4) - 1) % 9 + 1 } + line.map { ((it + 5) - 1) % 9 + 1 } + line.map { ((it + 6) - 1) % 9 + 1 } } +
        tile.map { line -> line.map { ((it + 3) - 1) % 9 + 1 } + line.map { ((it + 4) - 1) % 9 + 1 } + line.map { ((it + 5) - 1) % 9 + 1 } + line.map { ((it + 6) - 1) % 9 + 1 } + line.map { ((it + 7) - 1) % 9 + 1 } } +
        tile.map { line -> line.map { ((it + 4) - 1) % 9 + 1 } + line.map { ((it + 5) - 1) % 9 + 1 } + line.map { ((it + 6) - 1) % 9 + 1 } + line.map { ((it + 7) - 1) % 9 + 1 } + line.map { ((it + 8) - 1) % 9 + 1 } }
        return Graph4Way(m).pathSum(Node(0, 0), Node(m.lastIndex, m.lastIndex)) - m[0][0]
    }

    val testInput = readLines("Day15_test")
    check(part1(testInput).also(::println) == 40L)
    check(part2(testInput).also(::println) == 315L)

    val input = readLines("Day15")
    showAnswer(part1(input))
    showAnswer(part2(input))
}
