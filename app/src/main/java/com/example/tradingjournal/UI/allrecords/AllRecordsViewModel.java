package com.example.tradingjournal.UI.allrecords;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tradingjournal.data.local.entities.TradeEntry;
import com.example.tradingjournal.data.local.repositories.BalanceRepository;
import com.example.tradingjournal.data.local.repositories.TradeRepository;

import java.util.List;

public class AllRecordsViewModel extends ViewModel {

    private TradeRepository tradeRepository;
    private BalanceRepository balanceRepository;
    public AllRecordsViewModel(Context context) {
        tradeRepository = new TradeRepository(context);
        balanceRepository = new BalanceRepository(context);
    }

    public LiveData<List<TradeEntry>> getAllJournalEntries() {
        return tradeRepository.getAllJournalEntries();
    }
}