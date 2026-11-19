package com.example.calcsy;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class BmiResultActivity extends AppCompatActivity {

    private TextView tvKategori;
    private TextView tvHasilAngka;
    private TextView tvSaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_result);

        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        tvKategori = findViewById(R.id.tvKategori);
        tvHasilAngka = findViewById(R.id.tvHasilAngka);
        tvSaran = findViewById(R.id.tvSaran);
        MaterialButton btnHitungUlang = findViewById(R.id.btnHitungUlang);

        double bmi = getIntent().getDoubleExtra("EXTRA_BMI", 0.0);

        tvHasilAngka.setText(String.format(Locale.US, "%.1f", bmi));

        displayCategoryAndAdvice(bmi);

        btnHitungUlang.setOnClickListener(v -> finish());
    }

    private void displayCategoryAndAdvice(double bmi) {
        tvKategori.setText(BmiEngine.getCategory(bmi));
        tvKategori.setTextColor(Color.parseColor(BmiEngine.getCategoryColorHex(bmi)));
        tvSaran.setText(BmiEngine.getAdvice(bmi));
    }
}
