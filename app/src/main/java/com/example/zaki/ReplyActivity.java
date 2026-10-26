package com.example.zaki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ReplyActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.zaki.EXTRA_REPLY";
    private EditText replyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        // 1. Tangkap Intent dan ambil pesan
        Intent intent = getIntent();
        String message = intent.getStringExtra("EXTRA_MESSAGE");

        // 2. Tampilkan pesan pada TextView
        TextView textView = findViewById(R.id.text_message);
        textView.setText(message);

        // Inisialisasi EditText balasan
        replyEditText = findViewById(R.id.replyEditText);
    }

    // Method untuk mengirim pesan kembali
    public void sendReply(View view) {
        String replyMessage = replyEditText.getText().toString();
        
        Intent replyIntent = new Intent(this, MainActivity.class);
        replyIntent.putExtra(EXTRA_REPLY, replyMessage);
        
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}
