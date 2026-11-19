package com.example.calcsy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * CalculatorEngine - Logika Perhitungan Kalkulator
 *
 * Kelas ini mengolah input berupa string ekspresi matematika (misal "12+5×2"),
 * melakukan parsing (pemisahan angka & operator), mengevaluasi hasil sesuai urutan
 * operasi (Perkalian & Pembagian terlebih dahulu, baru Penjumlahan & Pengurangan),
 * serta memformat angka sesuai format lokal Indonesia (misal: "1.250.000,5").
 */
public class CalculatorEngine {

    private static final int MAX_DIGITS = 15;
    private static final int MAX_DECIMAL_PLACES = 10;
    public static final String ERROR_DIVIDE_BY_ZERO = "Tidak bisa dibagi nol";

    /**
     * Memeriksa apakah suatu karakter merupakan operator matematika.
     */
    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '−' || c == '×' || c == '÷';
    }

    /**
     * Menambahkan angka (digit) ke dalam ekspresi.
     * Batas maksimal digit dalam 1 angka adalah 15 digit.
     */
    public static String appendDigit(String expression, char digit) {
        if (!Character.isDigit(digit)) {
            throw new IllegalArgumentException("Input must be a digit");
        }

        String lastNumberToken = getLastNumberToken(expression);
        int digitCount = 0;
        for (int i = 0; i < lastNumberToken.length(); i++) {
            if (Character.isDigit(lastNumberToken.charAt(i))) {
                digitCount++;
            }
        }

        if (digitCount >= MAX_DIGITS) {
            return expression;
        }

        if (lastNumberToken.equals("0")) {
            if (digit == '0') {
                return expression;
            }
            // Mengganti angka '0' di depan dengan digit baru
            return expression.substring(0, expression.length() - 1) + digit;
        }

        return expression + digit;
    }

    /**
     * Menambahkan titik desimal (.) ke dalam ekspresi.
     */
    public static String appendDecimal(String expression) {
        String lastNumberToken = getLastNumberToken(expression);
        if (lastNumberToken.contains(".")) {
            return expression;
        }

        if (expression.isEmpty() || isOperator(expression.charAt(expression.length() - 1))) {
            return expression + "0.";
        }

        return expression + ".";
    }

    /**
     * Menambahkan operator matematika ke dalam ekspresi.
     * Jika operator dimasukkan berturut-turut, operator lama akan digantikan dengan yang baru.
     */
    public static String appendOperator(String expression, char operator) {
        char stdOp = normalizeOperator(operator);
        if (!isOperator(stdOp)) {
            throw new IllegalArgumentException("Invalid operator: " + operator);
        }

        if (expression.isEmpty()) {
            return (stdOp == '-' || stdOp == '−') ? "−" : "";
        }

        String sanitized = expression;
        if (sanitized.endsWith(".")) {
            sanitized = sanitized.substring(0, sanitized.length() - 1);
        }

        if (sanitized.isEmpty()) {
            return (stdOp == '-' || stdOp == '−') ? "−" : "";
        }

        char lastChar = sanitized.charAt(sanitized.length() - 1);
        if (isOperator(lastChar)) {
            return sanitized.substring(0, sanitized.length() - 1) + stdOp;
        }

        return sanitized + stdOp;
    }

    /**
     * Menghapus karakter terakhir dari ekspresi.
     */
    public static String deleteLastChar(String expression) {
        if (expression == null || expression.isEmpty()) {
            return "";
        }
        return expression.substring(0, expression.length() - 1);
    }

    /**
     * Membersihkan operator atau desimal menggantung di akhir ekspresi.
     */
    public static String getCleanExpression(String expression) {
        if (expression == null || expression.isEmpty()) {
            return "";
        }
        String cleanExpr = expression;
        while (!cleanExpr.isEmpty() && (isOperator(cleanExpr.charAt(cleanExpr.length() - 1)) || cleanExpr.charAt(cleanExpr.length() - 1) == '.')) {
            cleanExpr = cleanExpr.substring(0, cleanExpr.length() - 1);
        }
        return cleanExpr;
    }

    /**
     * Menghitung (evaluasi) hasil dari ekspresi matematika.
     */
    public static String evaluate(String expression) {
        String cleanExpr = getCleanExpression(expression);

        if (cleanExpr.isEmpty() || cleanExpr.equals("-") || cleanExpr.equals("−")) {
            return "";
        }

        List<Object> tokens = tokenize(cleanExpr);
        if (tokens == null || tokens.isEmpty()) {
            return "";
        }

        // Tahap 1: Evaluasi Perkalian (×) dan Pembagian (÷) terlebih dahulu
        List<Object> step1Tokens = new ArrayList<>();
        int i = 0;
        while (i < tokens.size()) {
            Object token = tokens.get(i);
            if (token instanceof Character && ((Character) token == '×' || (Character) token == '÷')) {
                char op = (Character) token;
                BigDecimal left = (BigDecimal) step1Tokens.remove(step1Tokens.size() - 1);
                BigDecimal right = (BigDecimal) tokens.get(i + 1);

                if (op == '÷') {
                    if (right.compareTo(BigDecimal.ZERO) == 0) {
                        return ERROR_DIVIDE_BY_ZERO;
                    }
                    BigDecimal result = left.divide(right, MAX_DECIMAL_PLACES, RoundingMode.HALF_UP);
                    step1Tokens.add(result);
                } else {
                    BigDecimal result = left.multiply(right);
                    step1Tokens.add(result);
                }
                i += 2;
            } else {
                step1Tokens.add(token);
                i++;
            }
        }

        // Tahap 2: Evaluasi Penjumlahan (+) dan Pengurangan (−)
        BigDecimal result = (BigDecimal) step1Tokens.get(0);
        int j = 1;
        while (j < step1Tokens.size()) {
            char op = (Character) step1Tokens.get(j);
            BigDecimal nextVal = (BigDecimal) step1Tokens.get(j + 1);
            if (op == '+') {
                result = result.add(nextVal);
            } else {
                result = result.subtract(nextVal);
            }
            j += 2;
        }

        return formatNumber(result);
    }

    /**
     * Memformat ekspresi matematika untuk ditampilkan pada layar (misal titik untuk ribuan, koma untuk desimal).
     */
    public static String formatExpressionForDisplay(String expression) {
        if (expression == null || expression.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        StringBuilder currentToken = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            boolean isUnaryMinus = (c == '-' || c == '−') && (i == 0 || isOperator(expression.charAt(i - 1)));

            if (isOperator(c) && !isUnaryMinus) {
                if (currentToken.length() > 0) {
                    sb.append(formatNumberToken(currentToken.toString()));
                    currentToken.setLength(0);
                }
                sb.append(" ").append(c).append(" ");
            } else {
                currentToken.append(c);
            }
        }

        if (currentToken.length() > 0) {
            sb.append(formatNumberToken(currentToken.toString()));
        }

        return sb.toString();
    }

    private static char normalizeOperator(char c) {
        return (c == '-') ? '−' : c;
    }

    private static String getLastNumberToken(String expression) {
        for (int i = expression.length() - 1; i >= 0; i--) {
            char c = expression.charAt(i);
            boolean isUnaryMinus = (c == '-' || c == '−') && (i == 0 || isOperator(expression.charAt(i - 1)));
            if (isOperator(c) && !isUnaryMinus) {
                return expression.substring(i + 1);
            }
        }
        return expression;
    }

    private static List<Object> tokenize(String expression) {
        List<Object> tokens = new ArrayList<>();
        StringBuilder currentNumber = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            boolean isUnaryMinus = (c == '-' || c == '−') && (i == 0 || isOperator(expression.charAt(i - 1)));

            if (isOperator(c) && !isUnaryMinus) {
                if (currentNumber.length() > 0) {
                    String numStr = currentNumber.toString().replace('−', '-');
                    try {
                        BigDecimal num = new BigDecimal(numStr);
                        tokens.add(num);
                    } catch (Exception e) {
                        return null;
                    }
                    currentNumber.setLength(0);
                }
                tokens.add(c);
            } else {
                currentNumber.append(c);
            }
        }

        if (currentNumber.length() > 0) {
            String numStr = currentNumber.toString().replace('−', '-');
            try {
                BigDecimal num = new BigDecimal(numStr);
                tokens.add(num);
            } catch (Exception e) {
                return null;
            }
        }

        return tokens;
    }

    private static String formatNumber(BigDecimal bd) {
        BigDecimal rounded = bd.setScale(MAX_DECIMAL_PLACES, RoundingMode.HALF_UP).stripTrailingZeros();
        String plainStr = (rounded.scale() < 0) ? rounded.setScale(0, RoundingMode.HALF_UP).toPlainString() : rounded.toPlainString();
        return formatNumberToken(plainStr);
    }

    public static String formatNumberToken(String token) {
        if (token == null || token.isEmpty()) {
            return "";
        }

        boolean isNegative = token.startsWith("-") || token.startsWith("−");
        String absToken = isNegative ? token.substring(1) : token;

        String[] parts = absToken.split("\\.");
        String integerPartStr = parts[0];
        String decimalPartStr = parts.length > 1 ? parts[1] : null;

        String formattedInt;
        try {
            long integerVal = Long.parseLong(integerPartStr);
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMAN);
            symbols.setGroupingSeparator('.');
            DecimalFormat formatter = new DecimalFormat("#,###", symbols);
            formattedInt = formatter.format(integerVal);
        } catch (NumberFormatException e) {
            formattedInt = integerPartStr;
        }

        String prefix = isNegative ? "−" : "";
        if (absToken.endsWith(".")) {
            return prefix + formattedInt + ",";
        } else if (decimalPartStr != null) {
            return prefix + formattedInt + "," + decimalPartStr;
        } else {
            return prefix + formattedInt;
        }
    }
}
