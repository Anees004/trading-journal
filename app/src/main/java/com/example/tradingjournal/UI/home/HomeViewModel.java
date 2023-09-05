package com.example.tradingjournal.UI.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tradingjournal.data.local.entities.StartingBalanceInfo;
import com.example.tradingjournal.data.local.repositories.BalanceRepository;
import com.example.tradingjournal.data.local.repositories.TradeRepository;

public class HomeViewModel extends ViewModel {
    private final Context context;
    private BalanceRepository balanceRepository;
    private TradeRepository tradeRepository;

    public HomeViewModel(Context context) {
        this.context = context;
        tradeRepository = new TradeRepository(context);
        balanceRepository = new BalanceRepository(context);
    }

    public double getTotal() {
        return tradeRepository.getPreviousTotal();
    }

    public double getStartBalance() {
        return balanceRepository.getStartingBalance();
    }

    public Integer getTotalEntries() {
        return tradeRepository.hasTradeEntries();
    }

    public double getSumOfWinningProfits() {
        return tradeRepository.getSumOfWinningProfits();
    }

    public double getSumOfLossProfit() {
        return tradeRepository.getSumOfLossProfit();
    }

    public LiveData<Integer> getWinsLossCount(String result) {
        return tradeRepository.getWinsLossCount(result);
    }

    public double getStartingAmount() {
        return balanceRepository.getStartingBalance();
    }

    //.....................................Starting Balance info............................
    public void insertStartingBalanceInfo(StartingBalanceInfo info) {
        balanceRepository.insertStartingBalanceInfo(info);
    }
}