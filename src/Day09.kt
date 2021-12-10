class Grid<T>(val cells: List<Cell<T>>, val rows: Int, val cols: Int) {
    companion object {
        fun <T> from(elements: List<List<T>>): Grid<T> {
            val cells = mutableListOf<Cell<T>>()
            val grid = Grid(cells, elements.size, elements[0].size)
            elements
                .flatMapIndexed { i, l -> l.mapIndexed { j, e -> Cell(i, j, e, grid) } }
                .forEach(cells::add)
            return grid
        }
    }
}

data class Cell<T>(val row: Int, val col: Int, val value: T, val grid: Grid<T>)

val <T> Cell<T>.up: Cell<T>?
    get() = if (row == 0) null else grid.cells[(row - 1) * grid.cols + col]
val <T> Cell<T>.down: Cell<T>?
    get() = if (row == grid.rows - 1) null else grid.cells[(row + 1) * grid.cols + col]
val <T> Cell<T>.left: Cell<T>?
    get() = if (col == 0) null else grid.cells[row * grid.cols + col - 1]
val <T> Cell<T>.right: Cell<T>?
    get() = if (col == grid.cols - 1) null else grid.cells[row * grid.cols + col + 1]
val <T> Cell<T>.neighbors: List<Cell<T>>
    get() = listOfNotNull(up, left, right, down)

fun main() {
    fun part1(input: List<String>) = Grid.from(input.map(String::digits))
        .cells
        .filter { cell -> cell.neighbors.all { n -> n.value > cell.value } }
        .sumOf { it.value + 1 }

    fun part2(input: List<String>): Int {
        val basins = mutableListOf<MutableList<Cell<Int>>>()
        for (cell in Grid.from(input.map(String::digits)).cells.filter { it.value != 9 }) {
            basins
                .filter { (it intersect cell.neighbors).any() }
                .also {
                    when (it.size) {
                        0 -> basins.add(mutableListOf(cell))
                        1 -> it[0].add(cell)
                        2 -> {
                            // merge the basins
                            it[0].addAll(it[1] + cell)
                            basins.remove(it[1])
                        }
                    }
                }
        }
        return basins.map { it.size }.sortedDescending().take(3).product()
    }

    val testInput = readLines("Day09_test")
    check(part1(testInput).also(::println) == 15)
    check(part2(testInput).also(::println) == 1134)

    val input = readLines("Day09")
    showAnswer(part1(input))
    showAnswer(part2(input))
}