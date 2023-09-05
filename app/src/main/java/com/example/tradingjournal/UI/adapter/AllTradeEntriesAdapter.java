package com.example.tradingjournal.UI.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tradingjournal.R;
import com.example.tradingjournal.data.local.entities.TradeEntry;
import com.example.tradingjournal.models.MonthlyGain;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AllTradeEntriesAdapter extends RecyclerView.Adapter<AllTradeEntriesAdapter.ViewHolder> {

    private List<TradeEntry> tradeEntries;
//    private final String[] monthsName = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public AllTradeEntriesAdapter(List<TradeEntry> tradeEntries) {
        this.tradeEntries = tradeEntries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_all_records, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TradeEntry tradeEntry = tradeEntries.get(position);
        holder.txtID.setText(tradeEntry.getId()+"");
        holder.txtDate.setText(tradeEntry.getDate());
        holder.txtPair.setText("Pair: "+tradeEntry.getPair());
        holder.txtSession.setText("Session: "+tradeEntry.getSession());
        holder.txtProfit.setText("Profit"+tradeEntry.getProfit()+"");
        holder.txtTotal.setText("Total "+tradeEntry.getTotal()+"");
        holder.txtResult.setText("Result "+tradeEntry.getResult());

    }

    @Override
    public int getItemCount() {
        return tradeEntries.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtID, txtDate,txtPair,txtSession,txtResult,txtProfit,txtTotal;
        ImageView imgTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtID = itemView.findViewById(R.id.txtID);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtPair= itemView.findViewById(R.id.txtPair);
            txtSession= itemView.findViewById(R.id.txtSession);
            txtResult= itemView.findViewById(R.id.txtResult);
            txtProfit= itemView.findViewById(R.id.txtProfit);
            txtTotal= itemView.findViewById(R.id.txtTotal);
        }
    }
}
