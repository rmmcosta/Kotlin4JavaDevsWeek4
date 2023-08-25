package rationals

import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO

open class RationalProgression
internal constructor(
    start: Rational, endInclusive: Rational, step: Rational
) : Iterable<Rational> {
    init {
        if (step.numerator == ZERO) throw kotlin.IllegalArgumentException("Step must be non-zero.")
    }

    /**
     * The first element in the progression.
     */
    public val first: Rational = start

    /**
     * The last element in the progression.
     */
    public val last: Rational = getProgressionLastElement(start, endInclusive, step)

    /**
     * The step of the progression.
     */
    public val step: Rational = step

    override fun iterator(): RationalIterator = RationalProgressionIterator(first, last, step)

    /**
     * Checks if the progression is empty.
     *
     * Progression with a positive step is empty if its first element is greater than the last element.
     * Progression with a negative step is empty if its first element is less than the last element.
     */
    public open fun isEmpty(): Boolean = if (step.numerator > ZERO) first > last else first < last

    override fun equals(other: Any?): Boolean =
        other is RationalProgression && (isEmpty() && other.isEmpty() || first == other.first && last == other.last && step == other.step)

    override fun hashCode(): Int = if (isEmpty()) -1 else (first.hashCode().plus(last.hashCode()).plus(step.hashCode()))

    override fun toString(): String =
        if (step.numerator > ZERO) "$first..$last step $step" else "$first downTo $last step ${-step}"

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
            rangeStart: Rational, rangeEnd: Rational, step: Rational
        ): RationalProgression = RationalProgression(rangeStart, rangeEnd, step)
    }
}

@OptIn(ExperimentalStdlibApi::class)
class RationalRange(start: Rational, endInclusive: Rational) :
    RationalProgression(start, endInclusive, findStep(start, endInclusive)),
    ClosedRange<Rational>, OpenEndRange<Rational> {
    override val start: Rational get() = first
    override val endInclusive: Rational get() = last

    @SinceKotlin("1.7")
    @ExperimentalStdlibApi
    override val endExclusive: Rational
        get() {
            return last.plus(Rational(ONE, start.denominator))
        }

    override fun contains(value: Rational): Boolean = first <= value && value <= last

    /**
     * Checks whether the range is empty.
     *
     * The range is empty if its start value is greater than the end value.
     */
    override fun isEmpty(): Boolean = first > last

    override fun equals(other: Any?): Boolean =
        other is RationalRange && (isEmpty() && other.isEmpty() || first == other.first && last == other.last)

    override fun hashCode(): Int = if (isEmpty()) -1 else (first.hashCode().plus(last.hashCode()))

    override fun toString(): String =
        if (step.numerator > ZERO) "$first..$last step $step" else "$first downTo $last step ${-step}"
}

private fun findStep(start: Rational, endInclusive: Rational) = Rational(
    ONE, transformRationalsToSameDenominator(start, endInclusive).first.second
)

internal fun getProgressionLastElement(start: Rational, end: Rational, step: Rational) = when {
    step.numerator > ZERO -> end
    step.numerator < ZERO -> start
    else -> throw kotlin.IllegalArgumentException("Step is zero.")
}

abstract class RationalIterator : Iterator<Rational> {
    override final fun next() = nextRational()

    /** Returns the next value in the sequence without boxing. */
    public abstract fun nextRational(): Rational
}

/**
 * An iterator over a progression of values of type `Int`.
 * @property step the number by which the value is incremented on each step.
 */
internal class RationalProgressionIterator(first: Rational, last: Rational, val step: Rational) :
    RationalIterator() {
    private val finalElement: Rational = last
    private var hasNext: Boolean = if (step.numerator > ZERO) first <= last else first >= last
    private var next: Rational = if (hasNext) first else finalElement

    override fun hasNext(): Boolean = hasNext

    override fun nextRational(): Rational {
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
