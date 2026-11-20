package com.example.calcsy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class BmiActivity extends AppCompatActivity {

    private double currentWeightKg = 65.0;
    private double currentHeightCm = 170.0;
    private boolean isMale = true;

    private Button btnGenderMale, btnGenderFemale;
    private TextView tvWeightDisplay, tvHeightDisplay;
    private TextView tvBmiCategory, tvBmiScore, tvBmiAdvice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        btnGenderMale = findViewById(R.id.btnGenderMale);
        btnGenderFemale = findViewById(R.id.btnGenderFemale);
        tvWeightDisplay = findViewById(R.id.tvWeightDisplay);
        tvHeightDisplay = findViewById(R.id.tvHeightDisplay);
        tvBmiCategory = findViewById(R.id.tvBmiCategory);
        tvBmiScore = findViewById(R.id.tvBmiScore);
        tvBmiAdvice = findViewById(R.id.tvBmiAdvice);

        setupBmiButtonLogic();
        updateBmiDisplays();
        calculateBmiAndShowResult();
    }

    private void setupBmiButtonLogic() {
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
        findViewById(R.id.btnCalculateBmi).setOnClickListener(v -> {
            calculateBmiAndShowResult();
            double bmi = BmiEngine.calculateBMI(currentWeightKg, currentHeightCm);
            Intent intent = new Intent(BmiActivity.this, BmiResultActivity.class);
            intent.putExtra("EXTRA_BMI", bmi);
            startActivity(intent);
        });
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
}
