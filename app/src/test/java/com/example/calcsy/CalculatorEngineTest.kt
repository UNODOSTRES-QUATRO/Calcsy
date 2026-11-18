package com.example.calcsy

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatorEngineTest {

    @Test
    fun testBigDecimalExactAddition() {
        // 0.1 + 0.2 should equal 0.3
        var expr = ""
        expr = CalculatorEngine.appendDigit(expr, '0')
        expr = CalculatorEngine.appendDecimal(expr)
        expr = CalculatorEngine.appendDigit(expr, '1')
        expr = CalculatorEngine.appendOperator(expr, '+')
        expr = CalculatorEngine.appendDigit(expr, '0')
        expr = CalculatorEngine.appendDecimal(expr)
        expr = CalculatorEngine.appendDigit(expr, '2')

        val result = CalculatorEngine.evaluate(expr)
        assertEquals("0,3", result)
    }

    @Test
    fun testDivisionByZero() {
        var expr = ""
        expr = CalculatorEngine.appendDigit(expr, '1')
        expr = CalculatorEngine.appendDigit(expr, '0')
        expr = CalculatorEngine.appendOperator(expr, '÷')
        expr = CalculatorEngine.appendDigit(expr, '0')

        val result = CalculatorEngine.evaluate(expr)
        assertEquals(CalculatorEngine.ERROR_DIVIDE_BY_ZERO, result)
    }

    @Test
    fun testOperatorPrecedence() {
        // 2 + 3 * 4 = 14
        val expr = "2+3×4"
        val result = CalculatorEngine.evaluate(expr)
        assertEquals("14", result)
    }

    @Test
    fun testConsecutiveOperatorsReplacePrevious() {
        var expr = "5"
        expr = CalculatorEngine.appendOperator(expr, '+')
        expr = CalculatorEngine.appendOperator(expr, '×')
        assertEquals("5×", expr)
    }

    @Test
    fun testMultipleDecimalsIgnored() {
        var expr = "1"
        expr = CalculatorEngine.appendDecimal(expr)
        expr = CalculatorEngine.appendDigit(expr, '2')
        expr = CalculatorEngine.appendDecimal(expr)
        expr = CalculatorEngine.appendDigit(expr, '3')
        assertEquals("1.23", expr)
        assertEquals("1,23", CalculatorEngine.formatExpressionForDisplay(expr))
    }

    @Test
    fun testMax15DigitsLimit() {
        var expr = ""
        for (i in 1..15) {
            expr = CalculatorEngine.appendDigit(expr, '9')
        }
        assertEquals("999999999999999", expr)
        // 16th digit should be ignored
        val expr16 = CalculatorEngine.appendDigit(expr, '9')
        assertEquals("999999999999999", expr16)
    }

    @Test
    fun testIndonesianFormatting() {
        val expr = "1250000.5"
        val formatted = CalculatorEngine.formatExpressionForDisplay(expr)
        assertEquals("1.250.000,5", formatted)
    }
}
