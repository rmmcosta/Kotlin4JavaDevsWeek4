package rationals

import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO

open class BigIntegerProgression
internal constructor(
    start: BigInteger, endInclusive: BigInteger, step: BigInteger
) : Iterable<BigInteger> {
    init {
        if (step == ZERO) throw kotlin.IllegalArgumentException("Step must be non-zero.")
    }

    /**
     * The first element in the progression.
     */
    public val first: BigInteger = start

    /**
     * The last element in the progression.
     */
    public val last: BigInteger = getProgressionLastElement(start, endInclusive, step)

    /**
     * The step of the progression.
     */
    public val step: BigInteger = step

    override fun iterator(): BigIntegerIterator = BigIntegerProgressionIterator(first, last, step)

    /**
     * Checks if the progression is empty.
     *
     * Progression with a positive step is empty if its first element is greater than the last element.
     * Progression with a negative step is empty if its first element is less than the last element.
     */
    public open fun isEmpty(): Boolean = if (step > ZERO) first > last else first < last

    override fun equals(other: Any?): Boolean =
        other is BigIntegerProgression && (isEmpty() && other.isEmpty() || first == other.first && last == other.last && step == other.step)

    override fun hashCode(): Int = if (isEmpty()) -1 else (first.hashCode().plus(last.hashCode()).plus(step.hashCode()))

    override fun toString(): String =
        if (step > ZERO) "$first..$last step $step" else "$first downTo $last step ${-step}"

    companion object {
        /**
         * Creates LongProgression within the specified bounds of a closed range.
         *
         * The progression starts with the [rangeStart] value and goes toward the [rangeEnd] value not excluding it, with the specified [step].
         * In order to go backwards the [step] must be negative.
         *
         * [step] must be greater than `Long.MIN_VALUE` and not equal to zero.
         */
        public fun fromClosedRange(
            rangeStart: BigInteger, rangeEnd: BigInteger, step: BigInteger
        ): BigIntegerProgression = BigIntegerProgression(rangeStart, rangeEnd, step)
    }
}

@OptIn(ExperimentalStdlibApi::class)
class BigIntegerRange(start: BigInteger, endInclusive: BigInteger) : BigIntegerProgression(start, endInclusive, ONE),
    ClosedRange<BigInteger>, OpenEndRange<BigInteger> {
    override val start: BigInteger get() = first
    override val endInclusive: BigInteger get() = last

    @SinceKotlin("1.7")
    @ExperimentalStdlibApi
    override val endExclusive: BigInteger
        get() {
            return last.plus(ONE)
        }

    override fun contains(value: BigInteger): Boolean = first <= value && value <= last

    /**
     * Checks whether the range is empty.
     *
     * The range is empty if its start value is greater than the end value.
     */
    override fun isEmpty(): Boolean = first > last

    override fun equals(other: Any?): Boolean =
        other is BigIntegerRange && (isEmpty() && other.isEmpty() || first == other.first && last == other.last)

    override fun hashCode(): Int = if (isEmpty()) -1 else (first.hashCode().plus(last.hashCode()))

    override fun toString(): String = "$first..$last"

    companion object {
        /** An empty range of values of type Long. */
        public val EMPTY: kotlin.ranges.LongRange = LongRange(1, 0)
    }
}

internal fun getProgressionLastElement(start: BigInteger, end: BigInteger, step: BigInteger): BigInteger = when {
    step > ZERO -> if (start >= end) end else end - differenceModulo(end, start, step)
    step < ZERO -> if (start <= end) end else end + differenceModulo(start, end, -step)
    else -> throw kotlin.IllegalArgumentException("Step is zero.")
}

private fun differenceModulo(a: BigInteger, b: BigInteger, c: BigInteger): BigInteger {
    val minusMod = a.mod(c).minus(b.mod(c))
    return minusMod.mod(c)
    //return mod(mod(a, c) - mod(b, c), c)
}

abstract class BigIntegerIterator : Iterator<BigInteger> {
    override final fun next() = nextBigInteger()

    /** Returns the next value in the sequence without boxing. */
    public abstract fun nextBigInteger(): BigInteger
}

/**
 * An iterator over a progression of values of type `Int`.
 * @property step the number by which the value is incremented on each step.
 */
internal class BigIntegerProgressionIterator(first: BigInteger, last: BigInteger, val step: BigInteger) :
    BigIntegerIterator() {
    private val finalElement: BigInteger = last
    private var hasNext: Boolean = if (step > ZERO) first <= last else first >= last
    private var next: BigInteger = if (hasNext) first else finalElement

    override fun hasNext(): Boolean = hasNext

    override fun nextBigInteger(): BigInteger {
        val value = next
        if (value == finalElement) {
            if (!hasNext) throw kotlin.NoSuchElementException()
            hasNext = false
        } else {
            next += step
        }
        return value
    }
}
