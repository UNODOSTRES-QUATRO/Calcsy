package com.example.calcsy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CalculatorEngineTest {

    @Test
    public void testBigDecimalExactAddition() {
        String expr = "";
        expr = CalculatorEngine.appendDigit(expr, '0');
        expr = CalculatorEngine.appendDecimal(expr);
        expr = CalculatorEngine.appendDigit(expr, '1');
        expr = CalculatorEngine.appendOperator(expr, '+');
        expr = CalculatorEngine.appendDigit(expr, '0');
        expr = CalculatorEngine.appendDecimal(expr);
        expr = CalculatorEngine.appendDigit(expr, '2');

        String result = CalculatorEngine.evaluate(expr);
        assertEquals("0,3", result);
    }

    @Test
    public void testDivisionByZero() {
        String expr = "";
        expr = CalculatorEngine.appendDigit(expr, '1');
        expr = CalculatorEngine.appendDigit(expr, '0');
        expr = CalculatorEngine.appendOperator(expr, '÷');
        expr = CalculatorEngine.appendDigit(expr, '0');

        String result = CalculatorEngine.evaluate(expr);
        assertEquals(CalculatorEngine.ERROR_DIVIDE_BY_ZERO, result);
    }

    @Test
    public void testOperatorPrecedence() {
        String expr = "2+3×4";
        String result = CalculatorEngine.evaluate(expr);
        assertEquals("14", result);
    }

    @Test
    public void testConsecutiveOperatorsReplacePrevious() {
        String expr = "5";
        expr = CalculatorEngine.appendOperator(expr, '+');
        expr = CalculatorEngine.appendOperator(expr, '×');
        assertEquals("5×", expr);
    }

    @Test
    public void testMultipleDecimalsIgnored() {
        String expr = "1";
        expr = CalculatorEngine.appendDecimal(expr);
        expr = CalculatorEngine.appendDigit(expr, '2');
        expr = CalculatorEngine.appendDecimal(expr);
        expr = CalculatorEngine.appendDigit(expr, '3');
        assertEquals("1.23", expr);
        assertEquals("1,23", CalculatorEngine.formatExpressionForDisplay(expr));
    }

    @Test
    public void testMax15DigitsLimit() {
        String expr = "";
        for (int i = 0; i < 15; i++) {
            expr = CalculatorEngine.appendDigit(expr, '9');
        }
        assertEquals("999999999999999", expr);
        String expr16 = CalculatorEngine.appendDigit(expr, '9');
        assertEquals("999999999999999", expr16);
    }

    @Test
    public void testIndonesianFormatting() {
        String expr = "1250000.5";
        String formatted = CalculatorEngine.formatExpressionForDisplay(expr);
        assertEquals("1.250.000,5", formatted);
    }

    @Test
    public void testMax10DecimalPlacesRounding() {
        String expr = "1";
        expr = CalculatorEngine.appendOperator(expr, '÷');
        expr = CalculatorEngine.appendDigit(expr, '3');
        String result = CalculatorEngine.evaluate(expr);
        assertEquals("0,3333333333", result);
    }

    @Test
    public void testTrailingZerosRemoved() {
        String expr = "2.5000";
        String result = CalculatorEngine.evaluate(expr);
        assertEquals("2,5", result);
    }
}
