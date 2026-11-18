package com.example.calcsy

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CalculatorUiState(
    val rawExpression: String = "",
    val displayExpression: String = "",
    val result: String = "0",
    val isError: Boolean = false,
    val isEvaluated: Boolean = false,
    val activeOperator: Char? = null
)

class CalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    fun onDigitInput(digit: Char) {
        val currentState = _uiState.value
        val baseRaw = if (currentState.isEvaluated || currentState.isError) "" else currentState.rawExpression
        val newRaw = CalculatorEngine.appendDigit(baseRaw, digit)
        updateState(newRaw, isEvaluated = false)
    }

    fun onDecimalInput() {
        val currentState = _uiState.value
        val baseRaw = if (currentState.isEvaluated || currentState.isError) "" else currentState.rawExpression
        val newRaw = CalculatorEngine.appendDecimal(baseRaw)
        updateState(newRaw, isEvaluated = false)
    }

    fun onOperatorInput(operator: Char) {
        val currentState = _uiState.value
        val baseRaw = if (currentState.isEvaluated && !currentState.isError) {
            resultToRaw(currentState.result)
        } else if (currentState.isError) {
            ""
        } else {
            currentState.rawExpression
        }
        val newRaw = CalculatorEngine.appendOperator(baseRaw, operator)
        updateState(newRaw, isEvaluated = false)
    }

    fun onDelete() {
        val currentState = _uiState.value
        if (currentState.isEvaluated) {
            onClear()
            return
        }
        val newRaw = CalculatorEngine.deleteLastChar(currentState.rawExpression)
        updateState(newRaw, isEvaluated = false)
    }

    fun onClear() {
        _uiState.value = CalculatorUiState()
    }

    fun onEquals() {
        val currentState = _uiState.value
        if (currentState.isError) {
            onClear()
            return
        }
        if (currentState.rawExpression.isEmpty()) return

        val evalResult = CalculatorEngine.evaluate(currentState.rawExpression)
        val isError = evalResult == CalculatorEngine.ERROR_DIVIDE_BY_ZERO

        if (isError) {
            _uiState.update {
                it.copy(
                    result = evalResult,
                    isError = true,
                    isEvaluated = true
                )
            }
        } else if (evalResult.isNotEmpty()) {
            _uiState.update {
                it.copy(
                    result = evalResult,
                    isError = false,
                    isEvaluated = true,
                    activeOperator = null
                )
            }
        }
    }

    private fun updateState(newRaw: String, isEvaluated: Boolean) {
        val displayExpr = CalculatorEngine.formatExpressionForDisplay(newRaw)
        val evalResult = CalculatorEngine.evaluate(newRaw)
        val isError = evalResult == CalculatorEngine.ERROR_DIVIDE_BY_ZERO

        val displayResult = when {
            isError -> evalResult
            evalResult.isEmpty() -> "0"
            else -> evalResult
        }

        val activeOp = if (newRaw.isNotEmpty() && CalculatorEngine.isOperator(newRaw.last())) {
            newRaw.last()
        } else {
            null
        }

        _uiState.update {
            it.copy(
                rawExpression = newRaw,
                displayExpression = displayExpr,
                result = displayResult,
                isError = isError,
                isEvaluated = isEvaluated,
                activeOperator = activeOp
            )
        }
    }

    private fun resultToRaw(formattedResult: String): String {
        return formattedResult.replace(".", "").replace(",", ".").replace("−", "-")
    }
}
