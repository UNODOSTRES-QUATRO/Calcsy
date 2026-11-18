package com.example.calcsy

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object CalculatorEngine {

    private const val MAX_DIGITS = 15
    const val ERROR_DIVIDE_BY_ZERO = "Tidak bisa dibagi nol"

    private val operators = setOf('+', '-', '×', '÷')

    fun isOperator(c: Char): Boolean = c in operators

    fun appendDigit(expression: String, digit: Char): String {
        require(digit.isDigit()) { "Input must be a digit" }

        val lastNumberToken = getLastNumberToken(expression)
        val digitCount = lastNumberToken.count { it.isDigit() }
        if (digitCount >= MAX_DIGITS) {
            return expression
        }

        if (lastNumberToken == "0") {
            if (digit == '0') return expression
            // Replace leading single '0' with the new digit
            return expression.substring(0, expression.length - 1) + digit
        }

        return expression + digit
    }

    fun appendDecimal(expression: String): String {
        val lastNumberToken = getLastNumberToken(expression)
        if (lastNumberToken.contains('.')) {
            return expression
        }

        if (expression.isEmpty() || isOperator(expression.last())) {
            return expression + "0."
        }

        return "$expression."
    }

    fun appendOperator(expression: String, operator: Char): String {
        require(isOperator(operator)) { "Invalid operator: $operator" }

        if (expression.isEmpty()) {
            return if (operator == '-') "-" else ""
        }

        var sanitized = expression
        if (sanitized.endsWith('.')) {
            sanitized = sanitized.dropLast(1)
        }

        if (sanitized.isEmpty()) {
            return if (operator == '-') "-" else ""
        }

        val lastChar = sanitized.last()
        if (isOperator(lastChar)) {
            // Replace previous operator with new operator
            return sanitized.dropLast(1) + operator
        }

        return sanitized + operator
    }

    fun deleteLastChar(expression: String): String {
        if (expression.isEmpty()) return ""
        return expression.dropLast(1)
    }

    fun clear(): String = ""

    fun evaluate(expression: String): String {
        if (expression.isEmpty()) return ""

        var cleanExpr = expression
        while (cleanExpr.isNotEmpty() && (isOperator(cleanExpr.last()) || cleanExpr.last() == '.')) {
            cleanExpr = cleanExpr.dropLast(1)
        }

        if (cleanExpr.isEmpty() || cleanExpr == "-") return ""

        val tokens = tokenize(cleanExpr) ?: return ""
        if (tokens.isEmpty()) return ""

        // Phase 1: Multiplication and Division
        val step1Tokens = mutableListOf<Any>()
        var i = 0
        while (i < tokens.size) {
            val token = tokens[i]
            if (token is Char && (token == '×' || token == '÷')) {
                val left = step1Tokens.removeAt(step1Tokens.size - 1) as BigDecimal
                val right = tokens[i + 1] as BigDecimal
                if (token == '÷') {
                    if (right.compareTo(BigDecimal.ZERO) == 0) {
                        return ERROR_DIVIDE_BY_ZERO
                    }
                    val result = left.divide(right, 10, RoundingMode.HALF_UP)
                    step1Tokens.add(result)
                } else {
                    val result = left.multiply(right)
                    step1Tokens.add(result)
                }
                i += 2
            } else {
                step1Tokens.add(token)
                i++
            }
        }

        // Phase 2: Addition and Subtraction
        var result = step1Tokens[0] as BigDecimal
        var j = 1
        while (j < step1Tokens.size) {
            val op = step1Tokens[j] as Char
            val nextVal = step1Tokens[j + 1] as BigDecimal
            result = if (op == '+') {
                result.add(nextVal)
            } else {
                result.subtract(nextVal)
            }
            j += 2
        }

        return formatNumber(result)
    }

    fun formatExpressionForDisplay(expression: String): String {
        if (expression.isEmpty()) return ""

        val sb = StringBuilder()
        val currentToken = StringBuilder()

        var i = 0
        while (i < expression.length) {
            val c = expression[i]
            val isUnaryMinus = c == '-' && (i == 0 || isOperator(expression[i - 1]))

            if (isOperator(c) && !isUnaryMinus) {
                if (currentToken.isNotEmpty()) {
                    sb.append(formatNumberToken(currentToken.toString()))
                    currentToken.clear()
                }
                sb.append(" ").append(c).append(" ")
            } else {
                currentToken.append(c)
            }
            i++
        }

        if (currentToken.isNotEmpty()) {
            sb.append(formatNumberToken(currentToken.toString()))
        }

        return sb.toString()
    }

    private fun getLastNumberToken(expression: String): String {
        var i = expression.length - 1
        while (i >= 0) {
            val c = expression[i]
            val isUnaryMinus = c == '-' && (i == 0 || isOperator(expression[i - 1]))
            if (isOperator(c) && !isUnaryMinus) {
                return expression.substring(i + 1)
            }
            i--
        }
        return expression
    }

    private fun tokenize(expression: String): List<Any>? {
        val tokens = mutableListOf<Any>()
        var currentNumber = StringBuilder()

        var i = 0
        while (i < expression.length) {
            val c = expression[i]
            val isUnaryMinus = c == '-' && (i == 0 || isOperator(expression[i - 1]))

            if (isOperator(c) && !isUnaryMinus) {
                if (currentNumber.isNotEmpty()) {
                    val num = currentNumber.toString().toBigDecimalOrNull() ?: return null
                    tokens.add(num)
                    currentNumber.clear()
                }
                tokens.add(c)
            } else {
                currentNumber.append(c)
            }
            i++
        }

        if (currentNumber.isNotEmpty()) {
            val num = currentNumber.toString().toBigDecimalOrNull() ?: return null
            tokens.add(num)
        }

        return tokens
    }

    private fun formatNumber(bd: BigDecimal): String {
        val stripped = bd.stripTrailingZeros()
        val plainStr = if (stripped.scale() < 0) stripped.setScale(0).toPlainString() else stripped.toPlainString()
        return formatNumberToken(plainStr)
    }

    fun formatNumberToken(token: String): String {
        if (token.isEmpty()) return ""

        val isNegative = token.startsWith('-')
        val absToken = if (isNegative) token.substring(1) else token

        val parts = absToken.split('.')
        val integerPartStr = parts[0]
        val decimalPartStr = parts.getOrNull(1)

        val integerVal = integerPartStr.toLongOrNull()
        val formattedInt = if (integerVal != null) {
            val symbols = DecimalFormatSymbols(Locale.GERMAN).apply {
                groupingSeparator = '.'
            }
            val formatter = DecimalFormat("#,###", symbols)
            formatter.format(integerVal)
        } else {
            integerPartStr
        }

        val prefix = if (isNegative) "-" else ""
        return when {
            absToken.endsWith('.') -> "$prefix$formattedInt,"
            decimalPartStr != null -> "$prefix$formattedInt,$decimalPartStr"
            else -> "$prefix$formattedInt"
        }
    }
}
