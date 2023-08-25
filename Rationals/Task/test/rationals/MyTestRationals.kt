package rationals

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigInteger

class MyTestRationals {
    @Test
    fun shouldReturnTheSameWhenAlreadySimplified() {
        val rational = Rational(BigInteger.ONE, BigInteger.TWO)
        assertEquals("1/2", rational.toString())
    }

    @Test
    fun shouldReturnSimplified() {
        var rational = Rational(BigInteger.valueOf(2), BigInteger.valueOf(4))
        assertEquals("1/2", rational.toString())
        rational = Rational(BigInteger.valueOf(3), BigInteger.valueOf(6))
        assertEquals("1/2", rational.toString())
        rational = Rational(BigInteger.valueOf(5), BigInteger.valueOf(15))
        assertEquals("1/3", rational.toString())
        rational = Rational(BigInteger.valueOf(3), BigInteger.valueOf(15))
        assertEquals("1/5", rational.toString())
    }

    @Test
    fun shouldSumTwoRationalsWithSameDenominator() {
        val rational1 = Rational(BigInteger.valueOf(1), BigInteger.valueOf(3))
        val rational2 = Rational(BigInteger.valueOf(4), BigInteger.valueOf(3))
        val result = Rational(BigInteger.valueOf(5), BigInteger.valueOf(3))
        assertEquals(result, rational1.plus(rational2))
        assertEquals(result, rational1 + rational2)
    }

    @Test
    fun shouldSumTwoRationalsWithDifferentDenominators() {
        val rational1 = Rational(BigInteger.valueOf(1), BigInteger.valueOf(4))
        val rational2 = Rational(BigInteger.valueOf(4), BigInteger.valueOf(3))
        //3/12 + 16/12
        val result = Rational(BigInteger.valueOf(19), BigInteger.valueOf(12))
        println("$rational1, $rational2, $result")
        assertEquals(result, rational1.plus(rational2))
        assertEquals(result, rational1 + rational2)
    }

    @Test
    fun shouldSimplifyToOneNumeratorEqualsDenominator() {
        val rational = Rational(BigInteger.valueOf(3), BigInteger.valueOf(3))
        assertEquals("1", rational.toString())
    }

    @Test
    fun shouldDivideTwoRationals() {
        val rational1 = Rational(BigInteger.valueOf(1), BigInteger.valueOf(4))
        val rational2 = Rational(BigInteger.valueOf(2), BigInteger.valueOf(1))
        val result = Rational(BigInteger.valueOf(1), BigInteger.valueOf(8))
        assertEquals(result, rational1.div(rational2))
    }

    @Test
    fun shouldSubtractTwoRationalsWithSameDenominatorFirstIsBigger() {
        val rational1 = Rational(BigInteger.valueOf(3), BigInteger.valueOf(4))
        val rational2 = Rational(BigInteger.valueOf(1), BigInteger.valueOf(4))
        val result = Rational(BigInteger.valueOf(1), BigInteger.valueOf(2))
        assertEquals(result, rational1.minus(rational2))
    }

    @Test
    fun shouldSubtractTwoRationalsWithSameDenominatorSecondIsBigger() {
        val rational1 = Rational(BigInteger.valueOf(1), BigInteger.valueOf(4))
        val rational2 = Rational(BigInteger.valueOf(3), BigInteger.valueOf(4))
        val result = Rational(BigInteger.valueOf(-1), BigInteger.valueOf(2))
        println(result)
        println(rational1.minus(rational2))
        assertEquals(result, rational1.minus(rational2))
    }

    @Test
    fun shouldMultiplyTwoRationals() {
        val rational1 = Rational(BigInteger.valueOf(1), BigInteger.valueOf(4))
        val rational2 = Rational(BigInteger.valueOf(2), BigInteger.valueOf(5))
        val result = Rational(BigInteger.valueOf(1), BigInteger.valueOf(10))
        assertEquals(result, rational1.times(rational2))
    }

    @Test
    fun shouldDetectInRangeSameDenominator() {
        val lower = Rational(BigInteger.valueOf(1), BigInteger.valueOf(5))
        val upper = Rational(BigInteger.valueOf(3), BigInteger.valueOf(5))
        val middle = Rational(BigInteger.valueOf(2), BigInteger.valueOf(5))
        assertTrue(middle in lower..upper)
    }

    @Test
    fun shouldDetectOutRangeSameDenominator() {
        val lower = Rational(BigInteger.valueOf(1), BigInteger.valueOf(5))
        val upper = Rational(BigInteger.valueOf(3), BigInteger.valueOf(5))
        val outer = Rational(BigInteger.valueOf(4), BigInteger.valueOf(5))
        assertTrue(outer !in lower..upper)
    }

    @Test
    fun shouldDetectInRangeDifferentDenominator() {
        val lower = Rational(BigInteger.valueOf(1), BigInteger.valueOf(3))
        val upper = Rational(BigInteger.valueOf(5), BigInteger.valueOf(7))
        val middle = Rational(BigInteger.valueOf(1), BigInteger.valueOf(2))
        assertTrue(middle in lower..upper)
    }

    @Test
    fun shouldDetectOutRangeDifferentDenominator() {
        val lower = Rational(BigInteger.valueOf(1), BigInteger.valueOf(3))
        val upper = Rational(BigInteger.valueOf(4), BigInteger.valueOf(7))
        val outer = Rational(BigInteger.valueOf(8), BigInteger.valueOf(9))
        assertTrue(outer !in lower..upper)
    }

    @Test
    fun shouldSimplifyLongs() {
        val rational1: Rational = 2000000000L divBy 4000000000L
        val rational2: Rational = 1 divBy 2
        assertEquals(rational1.numerator, rational2.numerator)
        assertEquals(rational1.denominator, rational2.denominator)
        assertEquals(rational1, rational2)
    }

    @Test
    fun shouldHalfBeBetweenOneThirdAndTwoThirds() {
        val half = 1 divBy 2
        val third = 1 divBy 3
        val twoThirds = 2 divBy 3
        println(half)
        println(third)
        println(twoThirds)
        println(third..twoThirds)
        println(1..2)
        assertTrue(half in third..twoThirds)
    }

    @Test
    fun shouldSubtractTwoBigRationals() {
        val rational1 = Rational(
            BigInteger.valueOf(17311206), BigInteger.valueOf(15881920)
        )
        val rational2 = Rational(
            BigInteger.valueOf(349928488), BigInteger.valueOf(277922736)
        )
        val result = Rational(BigInteger.valueOf(-8267885027), BigInteger.valueOf(48896076960))
        assertEquals(result, rational1 - rational2)
    }

    @Test
    fun shouldMultiplyTwoBigRationals() {
        val rational1 = Rational(
            BigInteger.valueOf(2339496978), BigInteger.valueOf(2849004564)
        )
        val rational2 = Rational(
            BigInteger.valueOf(6292023), BigInteger.valueOf(4423077)
        )
        val result = Rational(BigInteger.valueOf(46121885693), BigInteger.valueOf(39483160566))
        assertEquals(result, rational1 * rational2)
    }

    @Test
    fun shouldFindGCD4() {
        val numerator = BigInteger.valueOf(4)
        val denominator = BigInteger.valueOf(16)
        assertEquals(BigInteger.valueOf(4), numerator.gcd(denominator))
    }

    @Test
    fun shouldFindGCD1() {
        val numerator = BigInteger.valueOf(1)
        val denominator = BigInteger.valueOf(5)
        assertEquals(BigInteger.valueOf(1), numerator.gcd(denominator))
    }

    @Test
    fun shouldSumTwoHugeRationals() {
        val rational1 = Rational(
            "828099487587993325537".toBigInteger(), "44002379163849686934".toBigInteger()
        )
        val rational2 = Rational(
            "597728771407450572129".toBigInteger(), "542645811175759848891".toBigInteger()
        )
        val result = Rational(
            "17617266896778903272923516079952426936739".toBigInteger(),
            "884359508704835805965897828865092484822".toBigInteger()
        )
        assertEquals(result, rational1 + rational2)
    }
}