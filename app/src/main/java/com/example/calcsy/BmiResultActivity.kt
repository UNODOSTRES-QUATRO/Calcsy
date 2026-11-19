package com.example.calcsy

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class BmiResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi_result)

        val bmi = intent.getDoubleExtra("EXTRA_BMI", 0.0)
        val gender = intent.getStringExtra("EXTRA_GENDER") ?: "Laki-laki"

        val tvGender = findViewById<TextView>(R.id.tvBmiGender)
        val tvScore = findViewById<TextView>(R.id.tvBmiScore)
        val tvCategory = findViewById<TextView>(R.id.tvBmiCategory)
        val tvAdvice = findViewById<TextView>(R.id.tvBmiAdvice)

        tvGender.text = gender
        tvScore.text = String.format(Locale.US, "%.1f", bmi)

        when {
            bmi < 18.5 -> {
                tvCategory.text = "Kurus (Underweight)"
                tvCategory.setTextColor(Color.parseColor("#38BDF8"))
                tvAdvice.text = "Berat badan masih di bawah ideal. Tambah asupan kalori sehat dan latihan kekuatan otot."
            }
            bmi < 25.0 -> {
                tvCategory.text = "Normal (Ideal)"
                tvCategory.setTextColor(Color.parseColor("#4ADE80"))
                tvAdvice.text = "Berat badanmu sudah ideal! Pertahankan pola makan seimbang dan aktivitas fisik rutin."
            }
            bmi < 30.0 -> {
                tvCategory.text = "Gemuk (Overweight)"
                tvCategory.setTextColor(Color.parseColor("#FACC15"))
                tvAdvice.text = "Berat badan sedikit berlebih. Kurangi gula berlebih dan rutin olahraga kardio."
            }
            else -> {
                tvCategory.text = "Obesitas"
                tvCategory.setTextColor(Color.parseColor("#F87171"))
                tvAdvice.text = "Risiko kesehatan lebih tinggi. Atur defisit kalori bertahap dan rutin berolahraga."
            }
        }

        findViewById<Button>(R.id.btnRecalculate).setOnClickListener {
            finish()
        }
    }
}