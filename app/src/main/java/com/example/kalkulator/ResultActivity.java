package com.example.kalkulator;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    private TextView tvKategori;
    private TextView tvHasilAngka;
    private TextView tvSaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

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
        if (bmi < 18.5) {
            tvKategori.setText("KURANG BERAT BADAN");
            tvKategori.setTextColor(Color.parseColor("#EAB308"));
            tvSaran.setText("Berat badan Anda kurang. Disarankan untuk meningkatkan asupan nutrisi seimbang.");
        } else if (bmi >= 18.5 && bmi < 25.0) {
            tvKategori.setText("BERAT BADAN IDEAL");
            tvKategori.setTextColor(Color.parseColor("#16A34A"));
            tvSaran.setText("Kerja bagus! Pertahankan pola makan seimbang dan rutinitas olahraga harianmu.");
        } else if (bmi >= 25.0 && bmi < 30.0) {
            tvKategori.setText("KELEBIHAN BERAT BADAN");
            tvKategori.setTextColor(Color.parseColor("#F97316"));
            tvSaran.setText("Anda memiliki kelebihan berat badan. Cobalah untuk mengatur pola makan dan tingkatkan aktivitas fisik.");
        } else {
            tvKategori.setText("OBESITAS");
            tvKategori.setTextColor(Color.parseColor("#DC2626"));
            tvSaran.setText("Indeks massa tubuh Anda masuk kategori obesitas. Disarankan berkonsultasi dengan profesional kesehatan.");
        }
    }
}