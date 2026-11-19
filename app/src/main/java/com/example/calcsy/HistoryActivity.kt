package com.example.calcsy

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {

    private lateinit var adapter: HistoryAdapter
    private lateinit var tvEmpty: TextView
    private lateinit var rvHistory: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        tvEmpty = findViewById(R.id.tvEmptyHistory)
        rvHistory = findViewById(R.id.rvHistory)

        rvHistory.layoutManager = LinearLayoutManager(this)
        adapter = HistoryAdapter(emptyList())
        rvHistory.adapter = adapter

        findViewById<Button>(R.id.btnBackHistory).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnClearHistory).setOnClickListener {
            HistoryManager.clearHistory(this)
            loadHistory()
        }

        loadHistory()
    }

    private fun loadHistory() {
        val items = HistoryManager.getHistory(this)
        adapter.updateData(items)
        if (items.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
            rvHistory.visibility = View.GONE
        } else {
            tvEmpty.visibility = View.GONE
            rvHistory.visibility = View.VISIBLE
        }
    }
}