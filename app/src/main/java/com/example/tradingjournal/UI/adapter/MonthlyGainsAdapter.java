package com.example.tradingjournal.UI.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tradingjournal.R;
import com.example.tradingjournal.models.MonthlyGain;

import java.util.List;

public class MonthlyGainsAdapter extends RecyclerView.Adapter<MonthlyGainsAdapter.ViewHolder> {

    private List<MonthlyGain> monthlyGains;
    private final String[] monthsName = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public MonthlyGainsAdapter(List<MonthlyGain> monthlyGains) {
        this.monthlyGains = monthlyGains;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_monthly_gain, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MonthlyGain gain = monthlyGains.get(position);
        int mon = Integer.parseInt(gain.getMonth());
        holder.monthTextView.setText(monthsName[mon-1]);
        holder.profitTextView.setText("£" + String.valueOf(gain.getTotalProfit()));
        holder.percentGainTextView.setText(String.valueOf(gain.getTotalGain() + "%"));
    }

    @Override
    public int getItemCount() {
        return monthlyGains.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView monthTextView;
        TextView profitTextView;
        TextView percentGainTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            monthTextView = itemView.findViewById(R.id.textViewMonth);
            percentGainTextView = itemView.findViewById(R.id.textViewPercentGain);
            profitTextView = itemView.findViewById(R.id.textViewProfit);
        }
    }
}
