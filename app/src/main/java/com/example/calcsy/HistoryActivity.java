package com.example.calcsy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * HistoryActivity - Halaman untuk menampilkan riwayat perhitungan dari database SQLite.
 * Berpindah halaman menggunakan Intent.
 */
public class HistoryActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView btnClearHistory;
    private TextView tvEmptyHistory;
    private RecyclerView rvHistory;

    private HistoryDatabaseHelper dbHelper;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = new HistoryDatabaseHelper(this);

        btnBack = findViewById(R.id.btnBack);
        btnClearHistory = findViewById(R.id.btnClearHistory);
        tvEmptyHistory = findViewById(R.id.tvEmptyHistory);
        rvHistory = findViewById(R.id.rvHistory);

        // Tombol Back menggunakan Intent untuk kembali ke MainActivity
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Tombol Hapus Riwayat
        btnClearHistory.setOnClickListener(v -> {
            dbHelper.clearAllHistory();
            loadHistoryData();
        });

        // Mengatur LayoutManager agar posisi item di-stack dari bawah ke atas
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvHistory.setLayoutManager(layoutManager);

        loadHistoryData();
    }

    private void loadHistoryData() {
        List<HistoryItem> historyList = dbHelper.getAllHistory();
        if (historyList.isEmpty()) {
            tvEmptyHistory.setVisibility(View.VISIBLE);
            rvHistory.setVisibility(View.GONE);
        } else {
            tvEmptyHistory.setVisibility(View.GONE);
            rvHistory.setVisibility(View.VISIBLE);
            adapter = new HistoryAdapter(historyList);
            rvHistory.setAdapter(adapter);
            rvHistory.scrollToPosition(historyList.size() - 1);
        }
    }
}
