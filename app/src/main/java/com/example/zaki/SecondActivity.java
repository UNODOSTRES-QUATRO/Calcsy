package com.example.zaki;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    // Deklarasi variabel komponen
    EditText editText;
    RadioButton radio1, radio2, radio3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Inisialisasi ID komponen dari layout XML
        editText = findViewById(R.id.editText);
        radio1 = findViewById(R.id.radioButton1);
        radio2 = findViewById(R.id.radioButton2);
        radio3 = findViewById(R.id.radioButton3);
    }

    // Method yang dipanggil saat tombol SUBMIT diklik (android:onClick="showText")
    public void showText(View view) {
        // Menerima input teks nomor telepon
        String nophone = editText.getText().toString();
        String pilih = "";

        // Pengecekan RadioButton mana yang dipilih
        if (radio1.isChecked()) {
            pilih = "Telp Rumah";
        } else if (radio2.isChecked()) {
            pilih = "Mobile";
        } else if (radio3.isChecked()) {
            pilih = "Telp Kantor";
        }

        // Menampilkan hasil berupa Toast
        Toast.makeText(this, pilih + " : " + nophone, Toast.LENGTH_SHORT).show();
    }
}
