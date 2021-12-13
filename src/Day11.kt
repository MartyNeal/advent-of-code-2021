data class Octopus(var energyLevel: Int = 0, val flash: () -> Unit) {
    var flashed: Boolean = false

    fun inc() {
        energyLevel++
        if (energyLevel > 9 && !flashed) {
            flashed = true
            flash()
        }
    }

    fun unflash() {
        if (flashed)
            energyLevel = 0
        flashed = false
    }
}

class OctopusGrid(cells: List<Cell<Octopus>>, rows: Int, cols: Int, var flashCount: Int = 0) :
    Grid<Octopus>(cells, rows, cols) {
    fun step(): OctopusGrid {
        cellValues.forEach(Octopus::inc)
        flashCount += cellValues.count(Octopus::flashed)
        cellValues.forEach(Octopus::unflash)
        return this
    }

    override fun toString(): String {
        return "OctopusGrid(flashCount=$flashCount)\n" +
                cells.joinToString { c -> c.value.energyLevel.toString() }
    }

    companion object {
        fun from(elements: List<List<Int>>): OctopusGrid {
            val cells = mutableListOf<Cell<Octopus>>()
            val grid = OctopusGrid(cells, elements.size, elements[0].size)
            elements
                .flatMapIndexed { i, l ->
                    l.mapIndexed { j, e ->
                        var cell: Cell<Octopus>? = null
                        cell = Cell(i, j, Octopus(e) {
                            for (neighbor in cell!!.allNeighbors) {
                                neighbor.value.inc()
                            }
                        }, grid)
                        cell
                    }
                }
                .forEach(cells::add)
            return grid
        }
    }
}

fun main() {

    fun part1(octopusGrid: OctopusGrid) = generateSequence(octopusGrid, OctopusGrid::step)
        .elementAt(100)
        .flashCount

    fun part2(octopusGrid: OctopusGrid) = generateSequence(octopusGrid, OctopusGrid::step)
        .indexOfFirst { og -> og.cellValues.all { it.energyLevel == 0 } }

    val testInput = readLines("Day11_test").let { OctopusGrid.from(it.map(String::digits)) }
    testInput.cellValues.map(Octopus::copy)
    check(part1(testInput).also(::println) == 1656)
    check(part2(testInput).also(::println) == 195)

    val input = readLines("Day11").let { OctopusGrid.from(it.map(String::digits)) }
//    showAnswer(part1(input))
    showAnswer(part2(input))
}