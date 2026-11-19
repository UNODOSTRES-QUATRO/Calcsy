package com.example.calcsy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView
    private var currentInput = ""
    private var lastCalculated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvExpression = findViewById(R.id.tvExpression)
        tvResult = findViewById(R.id.tvResult)

        findViewById<Button>(R.id.btnOpenBmi).setOnClickListener {
            startActivity(Intent(this, BmiActivity::class.java))
        }

        findViewById<Button>(R.id.btnOpenHistory).setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        val numberButtons = mapOf(
            R.id.btn0 to "0",
            R.id.btnDoubleZero to "00",
            R.id.btn1 to "1",
            R.id.btn2 to "2",
            R.id.btn3 to "3",
            R.id.btn4 to "4",
            R.id.btn5 to "5",
            R.id.btn6 to "6",
            R.id.btn7 to "7",
            R.id.btn8 to "8",
            R.id.btn9 to "9",
            R.id.btnDot to "."
        )

        for ((id, value) in numberButtons) {
            findViewById<Button>(id).setOnClickListener { appendToken(value, isOperator = false) }
        }

        val operatorButtons = mapOf(
            R.id.btnPlus to "+",
            R.id.btnMinus to "-",
            R.id.btnMultiply to "×",
            R.id.btnDivide to "÷",
            R.id.btnPercent to "%"
        )

        for ((id, op) in operatorButtons) {
            findViewById<Button>(id).setOnClickListener { appendToken(op, isOperator = true) }
        }

        findViewById<Button>(R.id.btnClear).setOnClickListener {
            currentInput = ""
            lastCalculated = false
            tvExpression.text = ""
            tvResult.text = "0"
        }

        findViewById<Button>(R.id.btnBackspace).setOnClickListener {
            if (currentInput.isNotEmpty()) {
                currentInput = currentInput.dropLast(1)
                tvResult.text = if (currentInput.isEmpty()) "0" else currentInput
            }
        }

        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            calculateFinal()
        }
    }

    private fun appendToken(token: String, isOperator: Boolean) {
        if (lastCalculated) {
            if (!isOperator) {
                currentInput = ""
                tvExpression.text = ""
            }
            lastCalculated = false
        }

        if (isOperator && currentInput.isEmpty() && token != "-") return

        if (isOperator && currentInput.isNotEmpty()) {
            val lastChar = currentInput.last()
            if (lastChar in listOf('+', '-', '×', '÷')) {
                currentInput = currentInput.dropLast(1)
            }
        }

        currentInput += token
        tvResult.text = currentInput
    }

    private fun calculateFinal() {
        if (currentInput.isEmpty()) return

        try {
            val rawResult = evaluateExpression(currentInput)
            val formattedResult = if (rawResult % 1.0 == 0.0) {
                rawResult.toLong().toString()
            } else {
                BigDecimal.valueOf(rawResult).stripTrailingZeros().toPlainString()
            }

            tvExpression.text = currentInput
            tvResult.text = formattedResult

            HistoryManager.addHistory(this, currentInput, formattedResult)
            currentInput = formattedResult
            lastCalculated = true
        } catch (e: Exception) {
            tvExpression.text = currentInput
            tvResult.text = "Error"
            currentInput = ""
            lastCalculated = true
        }
    }

    private fun evaluateExpression(expr: String): Double {
        val cleanExpr = expr
            .replace("×", "*")
            .replace("÷", "/")
            .replace("%", "/100.0")
            .trimEnd('+', '-', '*', '/', '.')

        if (cleanExpr.isEmpty()) return 0.0

        return object : Any() {
            var pos = -1
            var ch = 0

            fun nextChar() {
                ch = if (++pos < cleanExpr.length) cleanExpr[pos].code else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.code) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < cleanExpr.length) throw RuntimeException("Unexpected character")
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    when {
                        eat('+'.code) -> x += parseTerm()
                        eat('-'.code) -> x -= parseTerm()
                        else -> return x
                    }
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    when {
                        eat('*'.code) -> x *= parseFactor()
                        eat('/'.code) -> {
                            val divisor = parseFactor()
                            if (divisor == 0.0) throw ArithmeticException("Division by zero")
                            x /= divisor
                        }
                        else -> return x
                    }
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.code)) return parseFactor()
                if (eat('-'.code)) return -parseFactor()

                val startPos = pos
                if ((ch >= '0'.code && ch <= '9'.code) || ch == '.'.code) {
                    while ((ch >= '0'.code && ch <= '9'.code) || ch == '.'.code) nextChar()
                    return cleanExpr.substring(startPos, pos).toDouble()
                } else {
                    throw RuntimeException("Unexpected character")
                }
            }
        }.parse()
    }
}