package com.example.calcsy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * MainActivity - Aplikasi Kalkulator "Calcsy 2.0"
 * Mengintegrasikan Kalkulator, Kalkulator BMI Berbasis Tombol (Button UI), dan Riwayat SQLite
 * dalam satu tampilan terpadu dengan navigasi BottomNavigationView.
 */
public class MainActivity extends AppCompatActivity {

    // Status Kalkulator
    private String rawExpression = "";
    private boolean isEvaluated = false;
    private boolean isError = false;
    private final List<String> tempHistoryList = new ArrayList<>();

    // Status BMI (Nilai default)
    private double currentWeightKg = 65.0;
    private double currentHeightCm = 170.0;
    private boolean isMale = true;

    // View Containers (Tabs)
    private LinearLayout layoutCalculator;
    private ScrollView layoutBmi;
    private LinearLayout layoutHistory;
    private TextView tvTabSubtitle;

    // Komponen UI Kalkulator
    private TextView tvRecentHistory;
    private TextView tvExpression;
    private TextView tvResult;
    private Button btnPlus, btnMinus, btnMultiply, btnDivide;

    // Komponen UI BMI (Button UI)
    private Button btnGenderMale, btnGenderFemale;
    private TextView tvWeightDisplay, tvHeightDisplay;
    private TextView tvBmiCategory, tvBmiScore, tvBmiAdvice;

    // Komponen UI Riwayat
    private RecyclerView rvMainHistory;
    private TextView tvEmptyMainHistory;
    private HistoryAdapter historyAdapter;

    // Helper Database SQLite
    private HistoryDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new HistoryDatabaseHelper(this);

        initViews();
        setupBottomNavigation();
        setupCalculatorLogic();
        setupBmiButtonLogic();
        setupHistoryTabLogic();

        updateCalculatorUi();
        updateBmiDisplays();
        calculateBmiAndShowResult();
    }

    private void initViews() {
        tvTabSubtitle = findViewById(R.id.tvTabSubtitle);
        layoutCalculator = findViewById(R.id.layoutCalculator);
        layoutBmi = findViewById(R.id.layoutBmi);
        layoutHistory = findViewById(R.id.layoutHistory);

        // Views Kalkulator
        tvRecentHistory = findViewById(R.id.tvRecentHistory);
        tvExpression = findViewById(R.id.tvExpression);
        tvResult = findViewById(R.id.tvResult);

        // Views BMI
        btnGenderMale = findViewById(R.id.btnGenderMale);
        btnGenderFemale = findViewById(R.id.btnGenderFemale);
        tvWeightDisplay = findViewById(R.id.tvWeightDisplay);
        tvHeightDisplay = findViewById(R.id.tvHeightDisplay);
        tvBmiCategory = findViewById(R.id.tvBmiCategory);
        tvBmiScore = findViewById(R.id.tvBmiScore);
        tvBmiAdvice = findViewById(R.id.tvBmiAdvice);

        // Views Riwayat
        rvMainHistory = findViewById(R.id.rvMainHistory);
        tvEmptyMainHistory = findViewById(R.id.tvEmptyMainHistory);

        // Sharing hasil kalkulasi via Long Click
        tvResult.setOnLongClickListener(v -> {
            shareResultWithIntent();
            return true;
        });
    }

    // ==========================================
    // 1. NAVIGATION LOGIC
    // ==========================================
    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_calculator) {
                switchTab(0);
                return true;
            } else if (itemId == R.id.nav_bmi) {
                switchTab(1);
                return true;
            } else if (itemId == R.id.nav_history) {
                switchTab(2);
                loadHistoryData();
                return true;
            }
            return false;
        });
    }

    private void switchTab(int index) {
        layoutCalculator.setVisibility(index == 0 ? View.VISIBLE : View.GONE);
        layoutBmi.setVisibility(index == 1 ? View.VISIBLE : View.GONE);
        layoutHistory.setVisibility(index == 2 ? View.VISIBLE : View.GONE);

        if (index == 0) {
            tvTabSubtitle.setText("Kalkulator");
        } else if (index == 1) {
            tvTabSubtitle.setText("BMI Calculator");
        } else {
            tvTabSubtitle.setText("Riwayat");
        }
    }

    // ==========================================
    // 2. KALKULATOR LOGIC
    // ==========================================
    private void setupCalculatorLogic() {
        setupNumberButtons();
        setupOperatorButtons();
        setupFunctionButtons();
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

        if (btnClear != null) btnClear.setOnClickListener(v -> onClear());
        if (btnDelete != null) {
            btnDelete.setOnClickListener(v -> onDelete());
            btnDelete.setOnLongClickListener(v -> {
                onClear();
                return true;
            });
        }
        if (btnComma != null) btnComma.setOnClickListener(v -> onDecimalInput());
        if (btnEquals != null) btnEquals.setOnClickListener(v -> onEquals());
    }

    private void onDigitInput(char digit) {
        if (isEvaluated) {
            rawExpression = "";
            isEvaluated = false;
        }
        String baseRaw = isError ? "" : rawExpression;
        rawExpression = CalculatorEngine.appendDigit(baseRaw, digit);
        updateCalculatorUi();
    }

    private void onDecimalInput() {
        if (isEvaluated) {
            rawExpression = "";
            isEvaluated = false;
        }
        String baseRaw = isError ? "" : rawExpression;
        rawExpression = CalculatorEngine.appendDecimal(baseRaw);
        updateCalculatorUi();
    }

    private void onOperatorInput(char operator) {
        if (isEvaluated) {
            isEvaluated = false;
        }
        String baseRaw = isError ? "" : rawExpression;
        rawExpression = CalculatorEngine.appendOperator(baseRaw, operator);
        updateCalculatorUi();
    }

    private void onDelete() {
        if (isEvaluated) {
            onClear();
            return;
        }
        rawExpression = CalculatorEngine.deleteLastChar(rawExpression);
        isEvaluated = false;
        updateCalculatorUi();
    }

    private void onClear() {
        rawExpression = "";
        isEvaluated = false;
        isError = false;
        updateCalculatorUi();
    }

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

            dbHelper.insertHistory(displayExpr, evalResult);

            tempHistoryList.add(fullEquation);
            if (tempHistoryList.size() > 4) {
                tempHistoryList.remove(0);
            }
            updateRecentHistoryDisplay();

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
            if (i < tempHistoryList.size() - 1) sb.append("\n");
        }
        tvRecentHistory.setText(sb.toString());
    }

    private void updateCalculatorUi() {
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
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hasil perhitungan Calcsy 2.0: " + textToShare);
        startActivity(Intent.createChooser(shareIntent, "Bagikan hasil perhitungan"));
    }

    // ==========================================
    // 3. BMI LOGIC (BUTTON UI - NO TYPING)
    // ==========================================
    private void setupBmiButtonLogic() {
        // Gender Toggle Buttons
        btnGenderMale.setOnClickListener(v -> {
            isMale = true;
            btnGenderMale.setBackgroundResource(R.drawable.bg_gender_active);
            btnGenderMale.setTextColor(0xFFF2F4F8);
            btnGenderFemale.setBackgroundResource(R.drawable.bg_gender_inactive);
            btnGenderFemale.setTextColor(0xFF8A93A6);
        });

        btnGenderFemale.setOnClickListener(v -> {
            isMale = false;
            btnGenderFemale.setBackgroundResource(R.drawable.bg_gender_active);
            btnGenderFemale.setTextColor(0xFFF2F4F8);
            btnGenderMale.setBackgroundResource(R.drawable.bg_gender_inactive);
            btnGenderMale.setTextColor(0xFF8A93A6);
        });

        // Stepper Buttons for Weight
        findViewById(R.id.btnWeightMinus10).setOnClickListener(v -> adjustWeight(-10.0));
        findViewById(R.id.btnWeightMinus1).setOnClickListener(v -> adjustWeight(-1.0));
        findViewById(R.id.btnWeightMinusHalf).setOnClickListener(v -> adjustWeight(-0.5));
        findViewById(R.id.btnWeightPlusHalf).setOnClickListener(v -> adjustWeight(0.5));
        findViewById(R.id.btnWeightPlus1).setOnClickListener(v -> adjustWeight(1.0));
        findViewById(R.id.btnWeightPlus10).setOnClickListener(v -> adjustWeight(10.0));

        // Preset Chips for Weight
        findViewById(R.id.chipWeight50).setOnClickListener(v -> setWeight(50.0));
        findViewById(R.id.chipWeight60).setOnClickListener(v -> setWeight(60.0));
        findViewById(R.id.chipWeight70).setOnClickListener(v -> setWeight(70.0));
        findViewById(R.id.chipWeight80).setOnClickListener(v -> setWeight(80.0));
        findViewById(R.id.chipWeight90).setOnClickListener(v -> setWeight(90.0));

        // Stepper Buttons for Height
        findViewById(R.id.btnHeightMinus10).setOnClickListener(v -> adjustHeight(-10.0));
        findViewById(R.id.btnHeightMinus1).setOnClickListener(v -> adjustHeight(-1.0));
        findViewById(R.id.btnHeightPlus1).setOnClickListener(v -> adjustHeight(1.0));
        findViewById(R.id.btnHeightPlus10).setOnClickListener(v -> adjustHeight(10.0));

        // Preset Chips for Height
        findViewById(R.id.chipHeight150).setOnClickListener(v -> setHeight(150.0));
        findViewById(R.id.chipHeight160).setOnClickListener(v -> setHeight(160.0));
        findViewById(R.id.chipHeight170).setOnClickListener(v -> setHeight(170.0));
        findViewById(R.id.chipHeight180).setOnClickListener(v -> setHeight(180.0));
        findViewById(R.id.chipHeight190).setOnClickListener(v -> setHeight(190.0));

        // Calculate Button
        findViewById(R.id.btnCalculateBmi).setOnClickListener(v -> calculateBmiAndShowResult());
    }

    private void adjustWeight(double delta) {
        currentWeightKg = Math.max(10.0, Math.min(300.0, currentWeightKg + delta));
        updateBmiDisplays();
        calculateBmiAndShowResult();
    }

    private void setWeight(double weight) {
        currentWeightKg = weight;
        updateBmiDisplays();
        calculateBmiAndShowResult();
    }

    private void adjustHeight(double delta) {
        currentHeightCm = Math.max(50.0, Math.min(250.0, currentHeightCm + delta));
        updateBmiDisplays();
        calculateBmiAndShowResult();
    }

    private void setHeight(double height) {
        currentHeightCm = height;
        updateBmiDisplays();
        calculateBmiAndShowResult();
    }

    private void updateBmiDisplays() {
        tvWeightDisplay.setText(String.format(Locale.US, "%.1f kg", currentWeightKg));
        tvHeightDisplay.setText(String.format(Locale.US, "%.0f cm", currentHeightCm));
    }

    private void calculateBmiAndShowResult() {
        double bmi = BmiEngine.calculateBMI(currentWeightKg, currentHeightCm);
        tvBmiScore.setText(String.format(Locale.US, "%.1f", bmi));

        String category = BmiEngine.getCategory(bmi);
        String hexColor = BmiEngine.getCategoryColorHex(bmi);
        String advice = BmiEngine.getAdvice(bmi, isMale);

        tvBmiCategory.setText(category);
        tvBmiCategory.setTextColor(Color.parseColor(hexColor));
        tvBmiAdvice.setText(advice);
    }

    // ==========================================
    // 4. RIWAYAT LOGIC
    // ==========================================
    private void setupHistoryTabLogic() {
        View btnClearMainHistory = findViewById(R.id.btnClearMainHistory);
        if (btnClearMainHistory != null) {
            btnClearMainHistory.setOnClickListener(v -> {
                dbHelper.clearAllHistory();
                loadHistoryData();
            });
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvMainHistory.setLayoutManager(layoutManager);
    }

    private void loadHistoryData() {
        List<HistoryItem> historyList = dbHelper.getAllHistory();
        if (historyList.isEmpty()) {
            tvEmptyMainHistory.setVisibility(View.VISIBLE);
            rvMainHistory.setVisibility(View.GONE);
        } else {
            tvEmptyMainHistory.setVisibility(View.GONE);
            rvMainHistory.setVisibility(View.VISIBLE);
            historyAdapter = new HistoryAdapter(historyList, item -> {
                rawExpression = resultToRaw(item.getResult());
                isEvaluated = true;
                updateCalculatorUi();
                BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
                if (bottomNav != null) {
                    bottomNav.setSelectedItemId(R.id.nav_calculator);
                }
                Toast.makeText(MainActivity.this, "Hasil dimuat ke kalkulator", Toast.LENGTH_SHORT).show();
            });
            rvMainHistory.setAdapter(historyAdapter);
            rvMainHistory.scrollToPosition(historyList.size() - 1);
        }
    }
}
