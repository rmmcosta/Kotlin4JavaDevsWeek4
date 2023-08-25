package rationals

import java.math.BigInteger
import java.math.BigInteger.*

class Rational(numerator: BigInteger, denominator: BigInteger = ONE) : Comparable<Rational> {
    val numerator: BigInteger
    val denominator: BigInteger

    init {
        if (denominator == ZERO) {
            throw IllegalArgumentException("The Denominator can't be zero")
        } else {
            val simplified = getSimplified(numerator, denominator)
            this.numerator = if (simplified.second > ZERO) simplified.first else simplified.first.times(-ONE)
            this.denominator = if (simplified.second > ZERO) simplified.second else simplified.second.times(-ONE)
        }
    }

    override fun equals(other: Any?): Boolean =
        if (other is Rational) this.numerator == other.numerator && this.denominator == other.denominator else false

    override fun hashCode(): Int = numerator.add(denominator).times((numerator.minus(denominator))).toInt()

    override fun toString(): String =
        if (denominator == ONE) numerator.toString() else getNormalized(numerator, denominator)

    private fun getNormalized(numerator: BigInteger, denominator: BigInteger): String {
        if (numerator == denominator) {
            return "1"
        }
        val simplified = getSimplified(numerator, denominator)
        return "${simplified.first}/${simplified.second}"
    }

    operator fun plus(other: Rational): Rational {
        val (factor1Rational, factor2Rational) = transformRationalsToSameDenominator(this, other)
        return Rational(
            factor1Rational.first.add(factor2Rational.first),
            factor1Rational.second
        )
    }

    operator fun minus(other: Rational): Rational {
        val (factor1Rational, factor2Rational) = transformRationalsToSameDenominator(this, other)
        return Rational(
            factor1Rational.first.minus(factor2Rational.first),
            factor1Rational.second
        )
    }

    operator fun times(other: Rational): Rational =
        Rational(this.numerator.times(other.numerator), this.denominator.times(other.denominator))

    operator fun div(other: Rational): Rational =
        Rational(this.numerator.times(other.denominator), this.denominator.times(other.numerator))

    operator fun unaryMinus(): Rational = Rational(this.numerator.times(-ONE), this.denominator)

    override operator fun compareTo(other: Rational): Int {
        val diff = this - other
        return when {
            diff.numerator == ZERO -> 0
            diff.numerator < ZERO -> -1
            else -> 1
        }
    }

    operator fun rangeTo(other: Rational): RationalRange = RationalRange(this, other)
}

private fun getSimplified(numerator: BigInteger, denominator: BigInteger): Pair<BigInteger, BigInteger> {
    var localNumerator = numerator
    var localDenominator = denominator
    val (isDivsBy10Times: Boolean, divsBy10Times) = checkDivsByTenTimes(numerator, denominator)
    if (isDivsBy10Times) {
        localNumerator = localNumerator.div(divsBy10Times)
        localDenominator = localDenominator.div(divsBy10Times)
    }
    if (localNumerator == localDenominator) {
        return Pair(ONE, ONE)
    }
    //println("after check divs by ten times, $localNumerator, $localDenominator")
    val maxCommonMultiple = getGreatestCommonDivisor(localNumerator, localDenominator)
    return Pair(localNumerator.div(maxCommonMultiple), localDenominator.div(maxCommonMultiple))
}

private fun checkDivsByTenTimes(numerator: BigInteger, denominator: BigInteger): Pair<Boolean, BigInteger> {
    var tenFactor: BigInteger = valueOf(10)
    var isDivByTenFactor = false
    while (numerator.remainder(tenFactor) == ZERO && denominator.remainder(tenFactor) == ZERO) {
        isDivByTenFactor = true
        tenFactor *= valueOf(10)
    }
    return Pair(isDivByTenFactor, tenFactor.div(valueOf(10)))
}

fun getGreatestCommonDivisor(
    numerator: BigInteger, denominator: BigInteger
): BigInteger = numerator.gcd(denominator)

fun transformRationalsToSameDenominator(
    first: Rational,
    second: Rational
): Pair<Pair<BigInteger, BigInteger>, Pair<BigInteger, BigInteger>> {
    val (factor1, factor2) = getFactorPairs(first, second)
    //println("factor pairs: $factor1, $factor2")
    val factor1Rational = Pair(first.numerator.times(factor1), first.denominator.times(factor1))
    val factor2Rational = Pair(second.numerator.times(factor2), second.denominator.times(factor2))
    //println("$factor1Rational, $factor2Rational")
    if (factor1Rational.second != factor2Rational.second) {
        throw Exception("Denominators should be the same after applying the respective factors $factor1Rational, $factor2Rational")
    }
    return Pair(factor1Rational, factor2Rational)
}

private fun getFactorPairs(first: Rational, second: Rational): Pair<BigInteger, BigInteger> {
    if (first.denominator == second.denominator) {
        return Pair(ONE, ONE)
    }
    return Pair(second.denominator, first.denominator)
}

fun String.toRational(): Rational {
    if (this.indexOf('/') == -1 || this.count { it == '/' } != 1) {
        return Rational(this.toBigInteger(), ONE)
    }
    val (strNumerator, strDenominator) = this.split('/')
    return Rational(strNumerator.toBigInteger(), strDenominator.toBigInteger())
}

infix fun Int.divBy(other: Int): Rational = Rational(this.toBigInteger(), other.toBigInteger())

infix fun Long.divBy(other: Long): Rational = Rational(this.toBigInteger(), other.toBigInteger())

infix fun BigInteger.divBy(other: BigInteger): Rational = Rational(this, other)

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    //println(half)
    //println(third)
    //println(twoThirds)
    //println(third..twoThirds)
    println(half in third..twoThirds)

    println((2000000000L divBy 4000000000L) == (1 divBy 2))

    //println(2000000000L divBy 4000000000L)

    println(
        "912016490186296920119201192141970416029".toBigInteger() divBy "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2
    )
}
