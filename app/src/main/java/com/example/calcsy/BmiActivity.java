package com.example.calcsy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class BmiActivity extends AppCompatActivity {

    private TextInputEditText etBerat;
    private TextInputEditText etTinggi;
    private MaterialButton btnHitung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        etBerat = findViewById(R.id.etBerat);
        etTinggi = findViewById(R.id.etTinggi);
        btnHitung = findViewById(R.id.btnHitung);

        btnHitung.setOnClickListener(v -> calculateBMI());
    }

    private void calculateBMI() {
        String weightStr = etBerat.getText() != null ? etBerat.getText().toString().trim() : "";
        String heightStr = etTinggi.getText() != null ? etTinggi.getText().toString().trim() : "";

        if (weightStr.isEmpty()) {
            etBerat.setError("Masukkan berat badan");
            etBerat.requestFocus();
            return;
        }

        if (heightStr.isEmpty()) {
            etTinggi.setError("Masukkan tinggi badan");
            etTinggi.requestFocus();
            return;
        }

        try {
            double weight = Double.parseDouble(weightStr);
            double heightCm = Double.parseDouble(heightStr);

            if (weight <= 0) {
                etBerat.setError("Berat badan harus lebih dari 0");
                etBerat.requestFocus();
                return;
            }

            if (heightCm <= 0) {
                etTinggi.setError("Tinggi badan harus lebih dari 0");
                etTinggi.requestFocus();
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
