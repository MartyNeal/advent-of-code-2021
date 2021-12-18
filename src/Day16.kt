fun String.bits(): Sequence<Int> = asSequence()
    .flatMap { c ->
        c.digitToInt(16)
            .toString(2)
            .padStart(4, '0')
            .map(Char::digitToInt)
    }

sealed class Packet(private val bits: Sequence<Int>, val typeID: Int) {
    val version: Int
        get() = bits.take(3).fold(0) { a, e -> a * 2 + e }
    abstract val size: Int
    abstract fun evaluate(): Long

    companion object {
        fun from(hex: String) = from(hex.bits())
        fun from(bits: Sequence<Int>) = when (bits.drop(3).take(3).fold(0) { a, e -> a * 2 + e }) {
            SumPacket.TYPE_ID -> SumPacket(bits)
            ProductPacket.TYPE_ID -> ProductPacket(bits)
            MinPacket.TYPE_ID -> MinPacket(bits)
            MaxPacket.TYPE_ID -> MaxPacket(bits)
            LiteralPacket.TYPE_ID -> LiteralPacket(bits)
            GreaterThanPacket.TYPE_ID -> GreaterThanPacket(bits)
            LessThanPacket.TYPE_ID -> LessThanPacket(bits)
            EqualsPacket.TYPE_ID -> EqualsPacket(bits)
            else -> error("unknown operator typeID")
        }
    }
}

class LiteralPacket(bits: Sequence<Int>) : Packet(bits, TYPE_ID) {
    private var value: Long = 0L
    override var size: Int = 6
        private set

    init {
        for (chunk in bits.drop(6).chunked(5)) {
            size += 5
            value = chunk.drop(1).fold(value) { a, e -> a * 2 + e }
            if (chunk[0] == 0) break
        }
    }

    override fun evaluate() = value

    companion object {
        const val TYPE_ID = 4
    }
}

sealed class OperatorPacket(bits: Sequence<Int>, typeID: Int) : Packet(bits, typeID) {

    private val lengthTypeID = bits.elementAt(6)
    private val lengthBits = if (lengthTypeID == 0) 15 else 11
    private val length = bits.drop(7).take(lengthBits).fold(0) { a, e -> a * 2 + e }

    final override var size: Int = 7 + lengthBits
        private set

    val subPackets = generateSequence { from(bits.drop(size)) }
        .let {
            if (lengthTypeID == 0) it.takeWhile { size < length + 7 + lengthBits }
            else it.take(length)
        }
        .onEach { size += it.size }
        .toList()
}

class SumPacket(bits: Sequence<Int>) : OperatorPacket(bits, TYPE_ID) {
    override fun evaluate(): Long = subPackets.sumOf { it.evaluate() }

    companion object {
        const val TYPE_ID = 0
    }
}

class ProductPacket(bits: Sequence<Int>) : OperatorPacket(bits, TYPE_ID) {
    override fun evaluate(): Long = subPackets.fold(1) { a, e -> a * e.evaluate() }

    companion object {
        const val TYPE_ID = 1
    }
}

class MinPacket(bits: Sequence<Int>) : OperatorPacket(bits, TYPE_ID) {
    override fun evaluate(): Long = subPackets.minOf { it.evaluate() }

    companion object {
        const val TYPE_ID = 2
    }
}

class MaxPacket(bits: Sequence<Int>) : OperatorPacket(bits, TYPE_ID) {
    override fun evaluate(): Long = subPackets.maxOf { it.evaluate() }

    companion object {
        const val TYPE_ID = 3
    }
}

class GreaterThanPacket(bits: Sequence<Int>) : OperatorPacket(bits, TYPE_ID) {
    override fun evaluate(): Long = if (subPackets[0].evaluate() > subPackets[1].evaluate()) 1 else 0

    companion object {
        const val TYPE_ID = 5
    }
}

class LessThanPacket(bits: Sequence<Int>) : OperatorPacket(bits, TYPE_ID) {
    override fun evaluate(): Long = if (subPackets[0].evaluate() < subPackets[1].evaluate()) 1 else 0

    companion object {
        const val TYPE_ID = 6
    }
}

class EqualsPacket(bits: Sequence<Int>) : OperatorPacket(bits, TYPE_ID) {
    override fun evaluate(): Long = if (subPackets[0].evaluate() == subPackets[1].evaluate()) 1 else 0

    companion object {
        const val TYPE_ID = 7
    }
}

fun main() {
    fun part1(transmission: String): Int {
        fun versionSum(p: Packet): Int =
            when (p) {
                is LiteralPacket -> p.version
                is OperatorPacket -> p.version + p.subPackets.sumOf { versionSum(it) }
            }

        return versionSum(Packet.from(transmission))
    }

    fun part2(transmission: String) = Packet.from(transmission).evaluate()

    val input = readLines("Day16")[0]
    showAnswer(part1(input))
    showAnswer(part2(input))
}
