package com.example.zaki;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView rvChatHistory;
    private EditText etMessageInput;
    private Button btnSendChat;

    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rvChatHistory = findViewById(R.id.rvChatHistory);
        etMessageInput = findViewById(R.id.etMessageInput);
        btnSendChat = findViewById(R.id.btnSendChat);

        chatList = new ArrayList<>();
        
        // Pesan selamat datang awal
        chatList.add(new ChatMessage("Halo Zaki! Selamat datang di layanan Support HRD BursaKerja SMK.", false, getCurrentTime()));
        chatList.add(new ChatMessage("Silakan ajukan pertanyaan terkait lowongan atau status lamaran pekerjaan Anda.", false, getCurrentTime()));

        chatAdapter = new ChatAdapter(chatList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvChatHistory.setLayoutManager(layoutManager);
        rvChatHistory.setAdapter(chatAdapter);

        btnSendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String text = etMessageInput.getText().toString().trim();
        if (!text.isEmpty()) {
            // Tambahkan pesan user ke daftar chat
            chatList.add(new ChatMessage(text, true, getCurrentTime()));
            chatAdapter.notifyItemInserted(chatList.size() - 1);
            rvChatHistory.smoothScrollToPosition(chatList.size() - 1);
            etMessageInput.setText("");

            // Simulasi balasan otomatis dari HRD setelah 1.5 detik
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    chatList.add(new ChatMessage("Terima kasih atas pesan Anda! HRD sedang meninjau informasi: '" + text + "'", false, getCurrentTime()));
                    chatAdapter.notifyItemInserted(chatList.size() - 1);
                    rvChatHistory.smoothScrollToPosition(chatList.size() - 1);
                }
            }, 1500);
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }
}
