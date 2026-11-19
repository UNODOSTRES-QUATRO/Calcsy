package com.example.calcsy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BmiActivity extends AppCompatActivity {

    private enum ActiveField {
        WEIGHT,
        HEIGHT
    }

    private TextView tvBerat;
    private TextView tvTinggi;
    private ActiveField activeField = ActiveField.WEIGHT;

    private String rawWeight = "";
    private String rawHeight = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        tvBerat = findViewById(R.id.tvBerat);
        tvTinggi = findViewById(R.id.tvTinggi);

        tvBerat.setOnClickListener(v -> setActiveField(ActiveField.WEIGHT));
        tvTinggi.setOnClickListener(v -> setActiveField(ActiveField.HEIGHT));

        setupNumberButtons();
        setupFunctionButtons();
        updateDisplay();
    }

    private void setActiveField(ActiveField field) {
        this.activeField = field;
        if (field == ActiveField.WEIGHT) {
            tvBerat.setBackgroundResource(R.drawable.bg_input_focused);
            tvTinggi.setBackgroundResource(R.drawable.bg_input_normal);
        } else {
            tvBerat.setBackgroundResource(R.drawable.bg_input_normal);
            tvTinggi.setBackgroundResource(R.drawable.bg_input_focused);
        }
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

    private void setupFunctionButtons() {
        Button btnClear = findViewById(R.id.btnClear);
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnComma = findViewById(R.id.btnComma);
        Button btnNext = findViewById(R.id.btnNext);
        Button btnHitung = findViewById(R.id.btnHitung);

        if (btnClear != null) {
            btnClear.setOnClickListener(v -> {
                if (activeField == ActiveField.WEIGHT) {
                    rawWeight = "";
                } else {
                    rawHeight = "";
                }
                updateDisplay();
            });
            btnClear.setOnLongClickListener(v -> {
                rawWeight = "";
                rawHeight = "";
                updateDisplay();
                return true;
            });
        }

        if (btnDelete != null) {
            btnDelete.setOnClickListener(v -> {
                if (activeField == ActiveField.WEIGHT) {
                    if (!rawWeight.isEmpty()) {
                        rawWeight = rawWeight.substring(0, rawWeight.length() - 1);
                    }
                } else {
                    if (!rawHeight.isEmpty()) {
                        rawHeight = rawHeight.substring(0, rawHeight.length() - 1);
                    }
                }
                updateDisplay();
            });
        }

        if (btnComma != null) {
            btnComma.setOnClickListener(v -> {
                if (activeField == ActiveField.WEIGHT) {
                    if (!rawWeight.contains(".")) {
                        rawWeight = rawWeight.isEmpty() ? "0." : rawWeight + ".";
                    }
                } else {
                    if (!rawHeight.contains(".")) {
                        rawHeight = rawHeight.isEmpty() ? "0." : rawHeight + ".";
                    }
                }
                updateDisplay();
            });
        }

        if (btnNext != null) {
            btnNext.setOnClickListener(v -> {
                if (activeField == ActiveField.WEIGHT) {
                    setActiveField(ActiveField.HEIGHT);
                } else {
                    setActiveField(ActiveField.WEIGHT);
                }
            });
        }

        if (btnHitung != null) {
            btnHitung.setOnClickListener(v -> calculateBMI());
        }
    }

    private void onDigitInput(char digit) {
        if (activeField == ActiveField.WEIGHT) {
            if (rawWeight.length() < 6) {
                if (rawWeight.equals("0")) {
                    rawWeight = String.valueOf(digit);
                } else {
                    rawWeight += digit;
                }
            }
        } else {
            if (rawHeight.length() < 6) {
                if (rawHeight.equals("0")) {
                    rawHeight = String.valueOf(digit);
                } else {
                    rawHeight += digit;
                }
            }
        }
        updateDisplay();
    }

    private void updateDisplay() {
        tvBerat.setText(rawWeight.isEmpty() ? "" : rawWeight);
        tvTinggi.setText(rawHeight.isEmpty() ? "" : rawHeight);
    }

    private void calculateBMI() {
        if (rawWeight.isEmpty() || rawWeight.equals(".")) {
            Toast.makeText(this, "Masukkan berat badan", Toast.LENGTH_SHORT).show();
            setActiveField(ActiveField.WEIGHT);
            return;
        }

        if (rawHeight.isEmpty() || rawHeight.equals(".")) {
            Toast.makeText(this, "Masukkan tinggi badan", Toast.LENGTH_SHORT).show();
            setActiveField(ActiveField.HEIGHT);
            return;
        }

        try {
            double weight = Double.parseDouble(rawWeight);
            double heightCm = Double.parseDouble(rawHeight);

            if (weight <= 0) {
                Toast.makeText(this, "Berat badan harus lebih dari 0", Toast.LENGTH_SHORT).show();
                return;
            }

            if (heightCm <= 0) {
                Toast.makeText(this, "Tinggi badan harus lebih dari 0", Toast.LENGTH_SHORT).show();
                return;
            }

            double bmi = BmiEngine.calculateBMI(weight, heightCm);

            Intent intent = new Intent(BmiActivity.this, BmiResultActivity.class);
            intent.putExtra("EXTRA_BMI", bmi);
            startActivity(intent);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Input tidak valid", Toast.LENGTH_SHORT).show();
        }
    }
}
