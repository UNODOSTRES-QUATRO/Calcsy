package com.example.zaki;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    
    // Deklarasi variabel komponen global
    private Button btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // PROSES INFLATE: Mengubah layout XML menjadi objek nyata di layar
        setContentView(R.layout.activity_main);

        // Menghubungkan variabel Java dengan ID komponen di file XML
        btnApply = findViewById(R.id.btnApply);

        // Menangani Interaksi Pengguna (Event Handling tombol lamar)
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Menampilkan notifikasi popup saat tombol diklik
                Toast.makeText(MainActivity.this, "Lamaran berhasil dikirim ke perusahaan!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
