package com.example.calcsy

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
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
        // Area Display (~35% tinggi layar)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.35f)
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
                // Ekspresi kecil (24sp, abu-abu)
                Text(
                    text = uiState.displayExpression,
                    color = colors.displayExpressionText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.End,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(fontFeatureSettings = "tnum"),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Hasil besar (56sp -> minimal 32sp jika panjang, tebal medium)
                AutoResizingText(
                    text = uiState.result,
                    color = if (uiState.isError) colors.actionButtonText else colors.displayResultText,
                    maxFontSize = if (uiState.isError) 28.sp else 56.sp,
                    minFontSize = 32.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Area Keypad (~65% tinggi layar): Grid 4 kolom x 5 baris
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.65f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Baris 1: AC (lebar 2 kolom), DEL, ÷
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
                    fontSize = 24.sp,
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
                    fontSize = 24.sp,
                    onClick = {
                        triggerHaptic()
                        viewModel.onDelete()
                    },
                    onLongClick = {
                        triggerHaptic()
                        viewModel.onClear()
                    }
                )
                CalcButton(
                    text = "÷",
                    modifier = Modifier.weight(1f),
                    backgroundColor = colors.operatorButtonBg,
                    textColor = colors.operatorButtonText,
                    fontSize = 30.sp,
                    isActiveOperator = uiState.activeOperator == '÷',
                    accentColor = colors.operatorButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onOperatorInput('÷')
                    }
                )
            }

            // Baris 2: 7, 8, 9, ×
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
                    fontSize = 28.sp,
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
                    fontSize = 28.sp,
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
                    fontSize = 28.sp,
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
                    fontSize = 30.sp,
                    isActiveOperator = uiState.activeOperator == '×',
                    accentColor = colors.operatorButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onOperatorInput('×')
                    }
                )
            }

            // Baris 3: 4, 5, 6, −
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
                    fontSize = 28.sp,
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
                    fontSize = 28.sp,
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
                    fontSize = 28.sp,
                    onClick = {
                        triggerHaptic()
                        viewModel.onDigitInput('6')
                    }
                )
                CalcButton(
                    text = "−",
                    modifier = Modifier.weight(1f),
                    backgroundColor = colors.operatorButtonBg,
                    textColor = colors.operatorButtonText,
                    fontSize = 30.sp,
                    isActiveOperator = uiState.activeOperator == '−' || uiState.activeOperator == '-',
                    accentColor = colors.operatorButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onOperatorInput('−')
                    }
                )
            }

            // Baris 4: 1, 2, 3, +
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
                    fontSize = 28.sp,
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
                    fontSize = 28.sp,
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
                    fontSize = 28.sp,
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
                    fontSize = 30.sp,
                    isActiveOperator = uiState.activeOperator == '+',
                    accentColor = colors.operatorButtonText,
                    onClick = {
                        triggerHaptic()
                        viewModel.onOperatorInput('+')
                    }
                )
            }

            // Baris 5: 0 (lebar 2 kolom), . (titik desimal), =
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
                    fontSize = 28.sp,
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
                    fontSize = 28.sp,
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
                    fontSize = 30.sp,
                    onClick = {
                        triggerHaptic()
                        viewModel.onEquals()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalcButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    textColor: Color,
    fontSize: TextUnit = 28.sp,
    isActiveOperator: Boolean = false,
    accentColor: Color = Color.Unspecified,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        label = "buttonScale"
    )

    val borderModifier = if (isActiveOperator && accentColor != Color.Unspecified) {
        Modifier.border(BorderStroke(1.5.dp, accentColor), RoundedCornerShape(20.dp))
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .heightIn(min = 64.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .then(borderModifier)
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                onLongClick = onLongClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            style = TextStyle(fontFeatureSettings = "tnum")
        )
    }
}

@Composable
fun AutoResizingText(
    text: String,
    color: Color,
    maxFontSize: TextUnit = 56.sp,
    minFontSize: TextUnit = 32.sp,
    fontWeight: FontWeight = FontWeight.Medium,
    modifier: Modifier = Modifier
) {
    var currentFontSize by remember(text) { mutableStateOf(maxFontSize) }
    var readyToDraw by remember(text) { mutableStateOf(false) }

    Text(
        text = text,
        color = color,
        fontSize = currentFontSize,
        fontWeight = fontWeight,
        textAlign = TextAlign.End,
        maxLines = 1,
        softWrap = false,
        overflow = TextOverflow.Clip,
        style = TextStyle(fontFeatureSettings = "tnum"),
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.didOverflowWidth && currentFontSize > minFontSize) {
                val nextSize = (currentFontSize.value - 2).coerceAtLeast(minFontSize.value)
                currentFontSize = nextSize.sp
            } else {
                readyToDraw = true
            }
        },
        modifier = modifier.graphicsLayer {
            alpha = if (readyToDraw) 1f else 0f
        }
    )
}
