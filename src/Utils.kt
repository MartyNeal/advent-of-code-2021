@file:Suppress("unused")

import java.io.File
import java.lang.Integer.parseInt
import java.math.BigDecimal
import java.math.BigInteger
import java.math.BigInteger.*
import java.util.*
import kotlin.NoSuchElementException
import kotlin.collections.HashMap
import kotlin.math.min
import kotlin.math.sqrt

fun readLines(name: String) = File("src", "$name.txt").readLines()
fun readNumbers(name: String) = readLines(name)[0].split(",").map(::parseInt)

fun Iterable<Int>.median() = sorted()[count() / 2]

fun <T> permutations(items: Iterable<T>): Sequence<List<T>> =
    if (items.none()) sequenceOf(listOf())
    else items.asSequence().flatMap { i -> permutations(items - i).map { listOf(i) + it } }
fun <T> choose(items: Iterable<T>, n: Int): Sequence<List<T>> =
    if (n == 0) sequenceOf(listOf())
    else items.asSequence().flatMapIndexed { i, item -> choose(items.drop(i + 1), n - 1).map { listOf(item) + it } }

fun Int.digits(): List<Int> = this.toString().digits()
fun Long.digits(): List<Int> = this.toString().digits()
fun BigInteger.digits(): List<Int> = this.toString().digits()
fun String.digits(): List<Int> = this.map { it - '0' }
fun nats(start: Int) = generateSequence(start) { it + 1 }
fun nats(start: Long = 1L) = generateSequence(start) { it + 1L }
fun nats(start: BigInteger) = generateSequence(start, BigInteger::inc)
fun nthPentagonal(n: Long) = n * (3 * n - 1) / 2
fun invPentagonal(c: Long) = (1 + sqrt(24.0 * c + 1)) / 6
fun isPentagonal(n: Long) = invPentagonal(n).let { it == it.toInt().toDouble() }
fun nthHexagonal(n: Long) = 2 * n * n - n
fun nthTriangle(n: Long) = (n * n + n) / 2
fun invTriangle(n: Long) = (-1 + sqrt(1 + 8.0 * n)) / 2
fun isTriangle(n: Long) = invTriangle(n).let { it == it.toInt().toDouble() }
fun triangleNumbers() = generateSequence(1L) { it.inc() }.runningReduce { a, e -> a + e }
fun <T> Iterable<T>.inits(): List<List<T>> = this.scan(listOf(), List<T>::plus)
fun <T> Iterable<T>.tails(): List<List<T>> = this.mapIndexed { i, _ -> this.drop(i) } + listOf(listOf())
fun CharSequence.inits(): List<CharSequence> = this.scan("", String::plus)
fun CharSequence.tails(): List<CharSequence> = this.mapIndexed { i, _ -> this.drop(i) } + listOf("")
fun BigInteger.isPalindrome() = this.toString().isPalindrome()
fun CharSequence.isPalindrome() = this == this.reversed()
fun String.isPalindrome() = this == this.reversed()
fun <T> List<T>.rotateLeft(n: Int) = List(size) { this[(it + n) % size] }
fun <T> List<T>.rotateRight(n: Int) = List(size) { this[(it - n + size) % size] }
fun String.rotateLeft(n: Int) = substring(n) + substring(0, n)
fun String.rotateRight(n: Int) = substring(length - n) + substring(0, length - n)
fun String.splitAt(n: Int) = listOf(substring(0,n), substring(n))
fun String.splitAt(splits: List<Int>) = (listOf(0) + splits + this.length).zipWithNext(this::substring)
fun rotations(s: String): List<String> = s.indices.map { s.rotateLeft(it) }
fun fac(n: Int): BigInteger = if (n == 0) ONE else n.toBigInteger() * fac(n - 1)
fun fibonaccis() = generateSequence(ONE to ONE) { (a, b) -> b to a + b }.map { it.first }

tailrec fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
tailrec fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
fun <A, R> ((A) -> R).memoize(): (A) -> R = memoize(HashMap())
fun <A, R> ((A) -> R).memoize(cache: MutableMap<A, R>): (A) -> R = { a: A -> cache.getOrPut(a) { this(a) } }
fun <A, B, R> ((A, B) -> R).memoize(): (A, B) -> R = memoize(HashMap())
fun <A, B, R> ((A, B) -> R).memoize(cache: MutableMap<Pair<A, B>, R>): (A, B) -> R = { a: A, b: B ->
    cache.getOrPut(a to b) { this(a, b) }
}

fun <A, B, C, R> ((A, B, C) -> R).memoize(): (A, B, C) -> R = memoize(HashMap())
fun <A, B, C, R> ((A, B, C) -> R).memoize(cache: MutableMap<Triple<A, B, C>, R>): (A, B, C) -> R = { a: A, b: B, c: C ->
    cache.getOrPut(Triple(a, b, c)) { this(a, b, c) }
}


tailrec fun <T> powerSet(left: Collection<T>, acc: Collection<Collection<T>> = listOf(listOf())): Collection<Collection<T>> =
    if (left.isEmpty()) acc
    else powerSet(left.drop(1), acc + acc.map { it + left.first() })

fun <T> Iterable<T>.hasCount(count: Int) = take(count + 1).count() == count
fun <T> Sequence<T>.hasCount(count: Int) = take(count + 1).count() == count
infix fun <T1, T2> Iterable<T1>.combine(other: Iterable<T2>): Sequence<Pair<T1, T2>> = combine(other, ::Pair)

inline fun <T1, T2, T3> Iterable<T1>.combine(other: Iterable<T2>, crossinline combiner: (T1, T2) -> T3): Sequence<T3>
{
    return this.asSequence().flatMap { e1 -> other.asSequence().map { e2 -> combiner(e1, e2) } }
}

val IntRange.size: Int get() = last - first + 1
fun BigInteger.isPandigital(range: IntRange) = toString().isPandigital(range)
fun String.isPandigital(range: IntRange) = length == range.size && (digits() symmetricDifference range).isEmpty()

infix fun <T> Iterable<T>.symmetricDifference(s2: Iterable<T>): Set<T> = (this union s2) - (this intersect s2)
infix fun Long.pow(e: Int): Long
{
    var res = 1L
    var base = this
    var power = e
    while (power > 0)
    {
        if (power % 2 == 1)
        {
            res *= base
        }
        base *= base
        power /= 2
    }
    return res
}

operator fun BigInteger.rangeTo(other: BigInteger) = BigIntegerRange(this, other)

class BigIntegerRange(override val start: BigInteger, override val endInclusive: BigInteger) : ClosedRange<BigInteger>, Iterable<BigInteger>
{
    override operator fun iterator(): Iterator<BigInteger> = BigIntegerRangeIterator(this)
}

class BigIntegerRangeIterator(private val range: ClosedRange<BigInteger>) : Iterator<BigInteger>
{
    private var current = range.start

    override fun hasNext(): Boolean = current <= range.endInclusive

    override fun next(): BigInteger
    {
        if (!hasNext())
        {
            throw NoSuchElementException()
        }
        return current++
    }
}

fun evenOddCoprimes(): Sequence<Pair<Long, Long>> = sequence {
    yield(2L to 1L)
    for ((m, n) in evenOddCoprimes())
    {
        val mn = m + n
        if (mn > 0)
        { // A bit of overflow checking code
            val mnm = mn + m
            val mnn = mn + n
            if (mnm > 0) yield(mnm to m)
            if (mnn > 0) yield(mnn to n)
        }
        val mmn = m - n + m
        if (mmn > 0) yield(mmn to m)
    }
}

fun primitivePythagoreanTriples() = evenOddCoprimes().map { (m, n) ->
    Triple(m * m - n * n, 2 * m * n, m * m + n * n)
        .let { if (it.first < it.second) it else Triple(it.second, it.first, it.third) }
}

fun <T> Sequence<T>.semisorted(buffer: Int, comparator: Comparator<T>? = null): Sequence<T> = sequence {
    val iter = this@semisorted.iterator()
    val q = PriorityQueue(buffer, comparator)
    repeat(buffer) { if (iter.hasNext()) q.add(iter.next()) else return@repeat }
    while (q.isNotEmpty())
    {
        yield(q.remove())
        if (iter.hasNext()) q.add(iter.next())
    }
}

operator fun <T> Sequence<T>.plus(other: () -> Sequence<T>) = sequence { yieldAll(this@plus); yieldAll(other()) }
fun Iterable<Int>.product() = fold(1, Int::times)
fun Iterable<Long>.product() = fold(1L, Long::times)
fun Iterable<Double>.product() = fold(1.0, Double::times)
fun Iterable<Float>.product() = fold(1.0f, Float::times)
fun Iterable<BigInteger>.product() = fold(ONE, BigInteger::times)
fun Iterable<BigDecimal>.product() = fold(BigDecimal.ONE, BigDecimal::times)
fun Sequence<Int>.product() = fold(1, Int::times)
fun Sequence<Long>.product() = fold(1L, Long::times)
fun Sequence<Double>.product() = fold(1.0, Double::times)
fun Sequence<Float>.product() = fold(1.0f, Float::times)
fun Sequence<BigInteger>.product() = fold(ONE, BigInteger::times)
fun Sequence<BigDecimal>.product() = fold(BigDecimal.ONE, BigDecimal::times)

fun <T: Comparable<T>> Iterable<T>.compareTo(other: Iterable<T>): Int {
    val otherI = other.iterator()
    for (e in this) {
        if (!otherI.hasNext()) return 1 // other has run out of elements, so `this` is larger
        val c = e.compareTo(otherI.next())
        if (c != 0) return c // found a position with a different
    }
    if (otherI.hasNext()) return -1 // `this` has run out of elements, but other has some more, so other is larger
    return 0 // they're the same
}

fun binomial(n: Int, k: Int): BigInteger {
    require(n >= 0 && k >= 0) { "negative numbers not allowed" }
    val kReduced = Integer.min(k, n - k)    // minimize number of steps
    var result = ONE
    var numerator = n
    var denominator = 1
    while (denominator <= kReduced) {
        result *= numerator--.toBigInteger()
        result /= denominator++.toBigInteger()
    }
    return result
}

fun partitions(n: Int) = partitions(n, n, listOf())
fun partitions(n: Int, max: Int, prefix: List<Int>): Sequence<List<Int>> = sequence {
    if (n == 0) {
        yield(prefix)
    }
    for (i in min(max, n) downTo 1) {
        yieldAll(partitions(n - i, i, prefix + i))
    }
}
