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
    val isError: Boolean = false
)

class CalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    fun onDigitInput(digit: Char) {
        val currentRaw = _uiState.value.rawExpression
        val newRaw = CalculatorEngine.appendDigit(currentRaw, digit)
        updateState(newRaw)
    }

    fun onDecimalInput() {
        val currentRaw = _uiState.value.rawExpression
        val newRaw = CalculatorEngine.appendDecimal(currentRaw)
        updateState(newRaw)
    }

    fun onOperatorInput(operator: Char) {
        val currentRaw = _uiState.value.rawExpression
        val newRaw = CalculatorEngine.appendOperator(currentRaw, operator)
        updateState(newRaw)
    }

    fun onDelete() {
        val currentRaw = _uiState.value.rawExpression
        val newRaw = CalculatorEngine.deleteLastChar(currentRaw)
        updateState(newRaw)
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
        if (currentState.result.isNotEmpty() && currentState.result != "0" && currentState.result != CalculatorEngine.ERROR_DIVIDE_BY_ZERO) {
            val newRaw = resultToRaw(currentState.result)
            _uiState.update {
                it.copy(
                    rawExpression = newRaw,
                    displayExpression = CalculatorEngine.formatExpressionForDisplay(newRaw),
                    result = ""
                )
            }
        }
    }

    private fun updateState(newRaw: String) {
        val displayExpr = CalculatorEngine.formatExpressionForDisplay(newRaw)
        val evalResult = CalculatorEngine.evaluate(newRaw)
        val isError = evalResult == CalculatorEngine.ERROR_DIVIDE_BY_ZERO

        val displayResult = when {
            isError -> evalResult
            evalResult.isEmpty() -> "0"
            else -> evalResult
        }

        _uiState.update {
            it.copy(
                rawExpression = newRaw,
                displayExpression = displayExpr,
                result = displayResult,
                isError = isError
            )
        }
    }

    private fun resultToRaw(formattedResult: String): String {
        return formattedResult.replace(".", "").replace(",", ".")
    }
}
