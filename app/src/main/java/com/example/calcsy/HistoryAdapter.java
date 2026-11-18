package com.example.calcsy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * HistoryAdapter - Adapter RecyclerView untuk menampilkan daftar riwayat perhitungan.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<HistoryItem> historyList;

    public HistoryAdapter(List<HistoryItem> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryItem item = historyList.get(position);
        holder.tvItemExpression.setText(item.getExpression());
        holder.tvItemResult.setText("= " + item.getResult());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemExpression;
        TextView tvItemResult;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemExpression = itemView.findViewById(R.id.tvItemExpression);
            tvItemResult = itemView.findViewById(R.id.tvItemResult);
        }
    }
}
