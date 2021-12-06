import java.lang.Integer.parseInt

enum class Direction {
    FORWARD, DOWN, UP
}

data class Instruction(val direction: Direction, val amount: Int) {
    companion object {
        fun parse(move: String): Instruction {
            val (dir, amt) = move.split(" ", limit = 2)
            return Instruction(Direction.valueOf(dir.uppercase()), parseInt(amt))
        }
    }
}

data class Position(val horizontal: Long, val depth: Long, val aim: Long) {
    fun move(move: Instruction): Position = when (move.direction) {
        Direction.FORWARD -> this.copy(horizontal = horizontal + move.amount)
        Direction.DOWN -> this.copy(depth = depth + move.amount)
        Direction.UP -> this.copy(depth = depth - move.amount)
    }

    fun aim(instruction: Instruction): Position = when (instruction.direction) {
        Direction.FORWARD -> this.copy(horizontal = horizontal + instruction.amount, depth = depth + aim * instruction.amount)
        Direction.DOWN -> this.copy(aim = aim + instruction.amount)
        Direction.UP -> this.copy(aim = aim - instruction.amount)
    }
}

fun main() {
    val initialPosition = Position(0, 0, 0)
    fun part1(input: List<String>) = input
        .map { Instruction.parse(it) }
        .fold(initialPosition) { pos, instruction -> pos.move(instruction) }
        .let { (horizontal, depth) -> horizontal * depth }

    fun part2(input: List<String>) = input.map { Instruction.parse(it) }
        .fold(initialPosition) { pos, instruction -> pos.aim(instruction) }
        .let { (horizontal, depth) -> horizontal * depth }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150L)
    check(part2(testInput) == 900L)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
