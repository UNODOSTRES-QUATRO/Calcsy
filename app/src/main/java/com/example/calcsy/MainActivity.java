package com.example.calcsy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity - Kelas utama kalkulator "Calcsy".
 *
 * Penggunaan Intent untuk membuka HistoryActivity,
 * database SQLite untuk menyimpan riwayat permanen, dan
 * daftar sementara (maksimal 4 persamaan) pada bagian atas tampilan kalkulator.
 */
public class MainActivity extends AppCompatActivity {

    // Status kalkulator
    private String rawExpression = "";
    private boolean isEvaluated = false;
    private boolean isError = false;

    // Riwayat sementara di layar utama (Maksimal 4 item, hilang saat aplikasi ditutup)
    private final List<String> tempHistoryList = new ArrayList<>();

    // Komponen Tampilan (UI Views)
    private TextView tvRecentHistory;
    private TextView tvExpression;
    private TextView tvResult;
    private ImageButton btnHistory;

    // Tombol Operator
    private Button btnPlus;
    private Button btnMinus;
    private Button btnMultiply;
    private Button btnDivide;

    // Helper Database SQLite
    private HistoryDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi Database Helper
        dbHelper = new HistoryDatabaseHelper(this);

        // Inisialisasi Tampilan
        tvRecentHistory = findViewById(R.id.tvRecentHistory);
        tvExpression = findViewById(R.id.tvExpression);
        tvResult = findViewById(R.id.tvResult);
        btnHistory = findViewById(R.id.btnHistory);

        // Intent ke HistoryActivity saat tombol History di kanan atas diklik
        if (btnHistory != null) {
            btnHistory.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            });
        }

        // Intent ke BmiActivity saat tombol BMI di kanan atas diklik
        ImageButton btnBmi = findViewById(R.id.btnBmi);
        if (btnBmi != null) {
            btnBmi.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, BmiActivity.class);
                startActivity(intent);
            });
        }

        // Menghubungkan Tombol
        setupNumberButtons();
        setupOperatorButtons();
        setupFunctionButtons();

        // Fitur Implicit Intent untuk membagikan hasil kalkulasi saat tvResult ditekan lama
        tvResult.setOnLongClickListener(v -> {
            shareResultWithIntent();
            return true;
        });

        updateUi();
    }

    private void setupNumberButtons() {
        int[] numberButtonIds = new int[]{
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };

        for (int id : numberButtonIds) {
            Button btn = findViewById(id);
            if (btn != null) {
                btn.setOnClickListener(v -> {
                    char digit = ((Button) v).getText().charAt(0);
                    onDigitInput(digit);
                });
            }
        }
    }

    private void setupOperatorButtons() {
        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        btnMultiply = findViewById(R.id.btnMultiply);
        btnDivide = findViewById(R.id.btnDivide);

        View.OnClickListener opListener = v -> {
            char op = ((Button) v).getText().charAt(0);
            onOperatorInput(op);
        };

        if (btnPlus != null) btnPlus.setOnClickListener(opListener);
        if (btnMinus != null) btnMinus.setOnClickListener(opListener);
        if (btnMultiply != null) btnMultiply.setOnClickListener(opListener);
        if (btnDivide != null) btnDivide.setOnClickListener(opListener);
    }

    private void setupFunctionButtons() {
        Button btnClear = findViewById(R.id.btnClear);
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnComma = findViewById(R.id.btnComma);
        Button btnEquals = findViewById(R.id.btnEquals);

        if (btnClear != null) {
            btnClear.setOnClickListener(v -> onClear());
        }

        if (btnDelete != null) {
            btnDelete.setOnClickListener(v -> onDelete());
            btnDelete.setOnLongClickListener(v -> {
                onClear();
                return true;
            });
        }

        if (btnComma != null) {
            btnComma.setOnClickListener(v -> onDecimalInput());
        }

        if (btnEquals != null) {
            btnEquals.setOnClickListener(v -> onEquals());
        }
    }

    private void onDigitInput(char digit) {
        if (isEvaluated) {
            rawExpression = "";
            isEvaluated = false;
        }
        String baseRaw = isError ? "" : rawExpression;
        rawExpression = CalculatorEngine.appendDigit(baseRaw, digit);
        updateUi();
    }

    private void onDecimalInput() {
        if (isEvaluated) {
            rawExpression = "";
            isEvaluated = false;
        }
        String baseRaw = isError ? "" : rawExpression;
        rawExpression = CalculatorEngine.appendDecimal(baseRaw);
        updateUi();
    }

    private void onOperatorInput(char operator) {
        if (isEvaluated) {
            isEvaluated = false;
        }
        String baseRaw = isError ? "" : rawExpression;
        rawExpression = CalculatorEngine.appendOperator(baseRaw, operator);
        updateUi();
    }

    private void onDelete() {
        if (isEvaluated) {
            onClear();
            return;
        }
        rawExpression = CalculatorEngine.deleteLastChar(rawExpression);
        isEvaluated = false;
        updateUi();
    }

    private void onClear() {
        rawExpression = "";
        isEvaluated = false;
        isError = false;
        updateUi();
    }

    /**
     * Tombol Sama Dengan (=):
     * 1. Menghitung hasil.
     * 2. Menyimpan ke database SQLite.
     * 3. Menambahkan ke daftar sementara (maksimal 4 item) di atas layar.
     * 4. Mengubah output menjadi input baru agar pengguna dapat melanjutkan perhitungan.
     */
    private void onEquals() {
        if (isError) {
            onClear();
            return;
        }
        String cleanExpr = CalculatorEngine.getCleanExpression(rawExpression);
        if (cleanExpr.isEmpty() || cleanExpr.equals("-") || cleanExpr.equals("−")) return;

        String evalResult = CalculatorEngine.evaluate(cleanExpr);
        isError = CalculatorEngine.ERROR_DIVIDE_BY_ZERO.equals(evalResult);

        if (isError) {
            tvResult.setText(evalResult);
            tvResult.setTextColor(0xFFFF7A7A);
            isEvaluated = true;
        } else if (!evalResult.isEmpty()) {
            String displayExpr = CalculatorEngine.formatExpressionForDisplay(cleanExpr);
            String fullEquation = displayExpr + " = " + evalResult;

            // 1. Simpan ke Database SQLite lokal
            dbHelper.insertHistory(displayExpr, evalResult);

            // 2. Simpan ke daftar riwayat sementara di layar (Maksimal 4 item)
            tempHistoryList.add(fullEquation);
            if (tempHistoryList.size() > 4) {
                tempHistoryList.remove(0);
            }
            updateRecentHistoryDisplay();

            // 3. Mengubah output menjadi input baru
            rawExpression = resultToRaw(evalResult);
            isEvaluated = true;

            tvExpression.setText(displayExpr);
            tvResult.setText(evalResult);
            tvResult.setTextColor(0xFFF2F4F8);
        }

        highlightActiveOperator();
    }

    private void updateRecentHistoryDisplay() {
        if (tempHistoryList.isEmpty()) {
            tvRecentHistory.setText("");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tempHistoryList.size(); i++) {
            sb.append(tempHistoryList.get(i));
            if (i < tempHistoryList.size() - 1) {
                sb.append("\n");
            }
        }
        tvRecentHistory.setText(sb.toString());
    }

    private void updateUi() {
        String displayExpr = CalculatorEngine.formatExpressionForDisplay(rawExpression);
        tvExpression.setText(displayExpr);

        if (!isEvaluated) {
            String evalResult = CalculatorEngine.evaluate(rawExpression);
            isError = CalculatorEngine.ERROR_DIVIDE_BY_ZERO.equals(evalResult);

            if (isError) {
                tvResult.setText(evalResult);
                tvResult.setTextColor(0xFFFF7A7A);
            } else if (evalResult.isEmpty()) {
                tvResult.setText("0");
                tvResult.setTextColor(0xFFF2F4F8);
            } else {
                tvResult.setText(evalResult);
                tvResult.setTextColor(0xFFF2F4F8);
            }
        }

        highlightActiveOperator();
    }

    private void highlightActiveOperator() {
        char activeOp = ' ';
        if (!rawExpression.isEmpty()) {
            char last = rawExpression.charAt(rawExpression.length() - 1);
            if (CalculatorEngine.isOperator(last)) {
                activeOp = last;
            }
        }

        if (btnPlus != null) {
            btnPlus.setBackgroundResource(activeOp == '+' ? R.drawable.bg_button_operator_active : R.drawable.bg_button_operator);
        }
        if (btnMinus != null) {
            btnMinus.setBackgroundResource((activeOp == '-' || activeOp == '−') ? R.drawable.bg_button_operator_active : R.drawable.bg_button_operator);
        }
        if (btnMultiply != null) {
            btnMultiply.setBackgroundResource(activeOp == '×' ? R.drawable.bg_button_operator_active : R.drawable.bg_button_operator);
        }
        if (btnDivide != null) {
            btnDivide.setBackgroundResource(activeOp == '÷' ? R.drawable.bg_button_operator_active : R.drawable.bg_button_operator);
        }
    }

    private String resultToRaw(String formattedResult) {
        return formattedResult.replace(".", "").replace(",", ".").replace("−", "-");
    }

    private void shareResultWithIntent() {
        String textToShare = tvResult.getText().toString();
        if (textToShare.isEmpty() || textToShare.equals("0")) {
            Toast.makeText(this, "Tidak ada hasil untuk dibagikan", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hasil perhitungan Calcsy: " + textToShare);
        startActivity(Intent.createChooser(shareIntent, "Bagikan hasil perhitungan"));
    }
}
