package com.example.zaki;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> chatList;

    public ChatAdapter(List<ChatMessage> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_bubble, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage chat = chatList.get(position);
        holder.tvMessage.setText(chat.getMessage());
        holder.tvTime.setText(chat.getTime());

        LinearLayout.LayoutParams paramsBox = (LinearLayout.LayoutParams) holder.chatBubbleBox.getLayoutParams();
        LinearLayout.LayoutParams paramsContainer = (LinearLayout.LayoutParams) holder.chatBubbleContainer.getLayoutParams();

        if (chat.isSentByUser()) {
            // Sent message (Right aligned, WhatsApp Green background)
            paramsContainer.gravity = Gravity.END;
            holder.chatBubbleBox.setBackgroundColor(Color.parseColor("#DCF8C6"));
        } else {
            // Received message (Left aligned, White background)
            paramsContainer.gravity = Gravity.START;
            holder.chatBubbleBox.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        holder.chatBubbleBox.setLayoutParams(paramsBox);
        holder.chatBubbleContainer.setLayoutParams(paramsContainer);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        LinearLayout chatBubbleContainer, chatBubbleBox;
        TextView tvMessage, tvTime;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatBubbleContainer = itemView.findViewById(R.id.chatBubbleContainer);
            chatBubbleBox = itemView.findViewById(R.id.chatBubbleBox);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
