package com.example.calcsy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BmiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)

        val rbMale = findViewById<RadioButton>(R.id.rbMale)
        val etWeight = findViewById<EditText>(R.id.etWeight)
        val etHeight = findViewById<EditText>(R.id.etHeight)

        findViewById<Button>(R.id.btnBackBmi).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnCalculateBmi).setOnClickListener {
            val weightStr = etWeight.text.toString().trim()
            val heightStr = etHeight.text.toString().trim()

            if (weightStr.isEmpty() || heightStr.isEmpty()) {
                Toast.makeText(this, "Isi berat dan tinggi badan dulu ya!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val weight = weightStr.toDoubleOrNull() ?: 0.0
            val heightCm = heightStr.toDoubleOrNull() ?: 0.0

            if (weight <= 0 || heightCm <= 0) {
                Toast.makeText(this, "Nilai harus lebih dari 0!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val heightM = heightCm / 100.0
            val bmi = weight / (heightM * heightM)
            val gender = if (rbMale.isChecked) "Laki-laki" else "Perempuan"

            val intent = Intent(this, BmiResultActivity::class.java).apply {
                putExtra("EXTRA_BMI", bmi)
                putExtra("EXTRA_GENDER", gender)
            }
            startActivity(intent)
        }
    }
}