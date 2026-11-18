package com.example.calcsy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = LocalCalculatorColors.current
    val haptic = LocalHapticFeedback.current

    val triggerHaptic = {
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.screenBackground)
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        // Top section: Display Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.2f)
                .clip(RoundedCornerShape(24.dp))
                .background(colors.displayBackground)
                .padding(20.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Small expression display
                Text(
                    text = uiState.displayExpression,
                    color = colors.displayExpressionText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.End,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Big result display
                Text(
                    text = uiState.result,
                    color = if (uiState.isError) colors.actionButtonText else colors.displayResultText,
                    fontSize = if (uiState.isError) 28.sp else 44.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bottom section: 4 Columns x 5 Rows Grid
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2.2f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Row 1: AC (2 col), DEL, ÷
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CalcButton(
                    text = "AC",
                    modifier = Modifier.weight(2f),
                    backgroundColor = colors.actionButtonBg,
                    textColor = colors.actionButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onClear()
                    }
                )
                CalcButton(
                    text = "DEL",
                    modifier = Modifier.weight(1f),
                    backgroundColor = colors.actionButtonBg,
                    textColor = colors.actionButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onDelete()
                    }
                )
                CalcButton(
                    text = "÷",
                    modifier = Modifier.weight(1f),
                    backgroundColor = colors.operatorButtonBg,
                    textColor = colors.operatorButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onOperatorInput('÷')
                    }
                )
            }

            // Row 2: 7, 8, 9, ×
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CalcButton(
                    text = "7",
                    modifier = Modifier.weight(1f),
                    backgroundColor = colors.numberButtonBg,
                    textColor = colors.numberButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onDigitInput('7')
                    }
                )
                CalcButton(
                    text = "8",
                    modifier = Modifier.weight(1f),
                    backgroundColor = colors.numberButtonBg,
                    textColor = colors.numberButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onDigitInput('8')
                    }
                )
                CalcButton(
                    text = "9",
                    modifier = Modifier.weight(1f),
                    backgroundColor = colors.numberButtonBg,
                    textColor = colors.numberButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onDigitInput('9')
                    }
                )
                CalcButton(
                    text = "×",
                    modifier = Modifier.weight(1f),
                    backgroundColor = colors.operatorButtonBg,
                    textColor = colors.operatorButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onOperatorInput('×')
                    }
                )
            }

            // Row 3: 4, 5, 6, -
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CalcButton(
                    text = "4",
                    modifier = Modifier.weight(1f),
                    backgroundColor = colors.numberButtonBg,
                    textColor = colors.numberButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onDigitInput('4')
                    }
                )
                CalcButton(
                    text = "5",
                    modifier = Modifier.weight(1f),
                    backgroundColor = colors.numberButtonBg,
                    textColor = colors.numberButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onDigitInput('5')
                    }
                )
                CalcButton(
                    text = "6",
                    modifier = Modifier.weight(1f),
                    backgroundColor = colors.numberButtonBg,
                    textColor = colors.numberButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onDigitInput('6')
                    }
                )
                CalcButton(
                    text = "-",
                    modifier = Modifier.weight(1f),
                    backgroundColor = colors.operatorButtonBg,
                    textColor = colors.operatorButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onOperatorInput('-')
                    }
                )
            }

            // Row 4: 1, 2, 3, +
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CalcButton(
                    text = "1",
                    modifier = Modifier.weight(1f),
                    backgroundColor = colors.numberButtonBg,
                    textColor = colors.numberButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onDigitInput('1')
                    }
                )
                CalcButton(
                    text = "2",
                    modifier = Modifier.weight(1f),
                    backgroundColor = colors.numberButtonBg,
                    textColor = colors.numberButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onDigitInput('2')
                    }
                )
                CalcButton(
                    text = "3",
                    modifier = Modifier.weight(1f),
                    backgroundColor = colors.numberButtonBg,
                    textColor = colors.numberButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onDigitInput('3')
                    }
                )
                CalcButton(
                    text = "+",
                    modifier = Modifier.weight(1f),
                    backgroundColor = colors.operatorButtonBg,
                    textColor = colors.operatorButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onOperatorInput('+')
                    }
                )
            }

            // Row 5: 0 (2 col), , (decimal point), =
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CalcButton(
                    text = "0",
                    modifier = Modifier.weight(2f),
                    backgroundColor = colors.numberButtonBg,
                    textColor = colors.numberButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onDigitInput('0')
                    }
                )
                CalcButton(
                    text = ",",
                    modifier = Modifier.weight(1f),
                    backgroundColor = colors.numberButtonBg,
                    textColor = colors.numberButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onDecimalInput()
                    }
                )
                CalcButton(
                    text = "=",
                    modifier = Modifier.weight(1f),
                    backgroundColor = colors.equalsButtonBg,
                    textColor = colors.equalsButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onEquals()
                    }
                )
            }
        }
    }
}

@Composable
fun CalcButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: androidx.compose.ui.graphics.Color,
    textColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .heightIn(min = 64.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}
