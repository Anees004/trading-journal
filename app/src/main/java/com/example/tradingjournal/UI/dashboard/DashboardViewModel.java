package com.example.tradingjournal.UI.dashboard;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.tradingjournal.data.local.repositories.BalanceRepository;
import com.example.tradingjournal.data.local.repositories.TradeRepository;
import com.example.tradingjournal.models.MonthlyGain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class DashboardViewModel extends ViewModel {
    private TradeRepository tradeRepository;
    private BalanceRepository balanceRepository;
    private final MutableLiveData<String> selectedYear = new MutableLiveData<>();
    private final LiveData<List<MonthlyGain>> monthlyGains;

    public DashboardViewModel(Context context) {
        tradeRepository = new TradeRepository(context);
        balanceRepository = new BalanceRepository(context);
        monthlyGains = Transformations.switchMap(selectedYear, year -> tradeRepository.getMonthlyGains(year));

    }

    public int hastableEntriesCount() {
        return tradeRepository.hasTradeEntries();
    }

    //L and W count return
    public LiveData<Integer> getWinsLossCount(String result){
        return tradeRepository.getWinsLossCount(result);
    }


    public double getSumOfWinningProfits() {
        return tradeRepository.getSumOfWinningProfits();
    }

    public double getSumOfLossProfit() {
        return tradeRepository.getSumOfLossProfit();
    }

    public LiveData<List<Double>> getAlltotalValues() {
        return tradeRepository.getAlltotalValues();
    }

    public Double getTotalStartingBalance() {
        return balanceRepository.getStartingBalance();
    }

    //.....................Error Count.............
    public LiveData<Integer> getErrorCount(String error) {
        return tradeRepository.getErrorCount(error);
    }

    public int getOverratedErrorCount() {
        return tradeRepository.getOverratedErrorCount();
    }

    public int getRevengeErrorCount() {
        return tradeRepository.getRevengeErrorCount();
    }

    public int getBadEntryErrorCount() {
        return tradeRepository.getBadEntryErrorCount();
    }

    public int getNoSneakerErrorCount() {
        return tradeRepository.getNoSneakerErrorCount();
    }

    //..........For Last ten Entries win loss Chart
    public LiveData<Integer> getLastTenEnteriesWinLoss(String result){
        return  tradeRepository.getLastTenEnteriesWinLoss(result);
    }
//    public int getLastTenEntriesLoss() {
//        return tradeRepository.getLastTenEntriesLoss();
//    }


    public LiveData<List<Double>> getLastNSumOfProfits(int n) {
        MediatorLiveData<List<Double>> resultLiveData = new MediatorLiveData<>();

        List<LiveData<Double>> sumLiveDataList = new ArrayList<>();
        for (int i = n; i > 0; i--) {
            LiveData<Double> sumLiveData = tradeRepository.getSumOfLastNProfits(i);
            sumLiveDataList.add(sumLiveData);
            resultLiveData.addSource(sumLiveData, result -> {
                List<Double> sumValues = new ArrayList<>();
                for (LiveData<Double> liveData : sumLiveDataList) {
                    Double value = liveData.getValue();
                    if (value != null) {
                        sumValues.add(value);
                    }
                }
                resultLiveData.setValue(sumValues);
            });
        }

        return resultLiveData;
    }

    //......................NAS DAS AND US 30 CHART////////////
    public int getWinPercentNAS(String session, String result, String pair) {
        return tradeRepository.getWinPercentNAS(session, result, pair);
    }

    //......................Long or Short Win percentage.......
    public LiveData<Integer> getWinPercentLS(String ls, String result) {
        return tradeRepository.getWinPercentLS(ls, result);
    }

    //....................get mindset Calm Count............
    public LiveData<Integer> getMindsetCalmCount(String mindset) {
        return tradeRepository.getMindsetCalmCount(mindset);
    }

    //..........sessions profit.........
    public double getSessionWinningProfits(String session, String result, String pair) {
        return tradeRepository.getSessionWinningProfits(session, result, pair);
    }

    //...........Tp count for closed early percentage..........
    public LiveData<Integer> getTpCount(String tp) {
        return tradeRepository.getTpCount(tp);
    }

//.........get Max Losing Consistency.........

    public int getMaxlConsistency() {
        return tradeRepository.getMaxlConsistency();
    }

    //.........get Max Winning Consistency.........

    public int getMaxWConsistency() {
        return tradeRepository.getMaxWConsistency();
    }

    //.......get NAS weekly........
    public int getNASWeekDayWinLoss(String session, String result, String day) {
        return tradeRepository.getNASWeekDayWinLoss(session, result, day);
    }

    //........WIN LOSS count according to day only..........
    public int getWeekDayWinLossCount(String result, String day) {
        return tradeRepository.getWeekDayWinLossCount(result, day);
    }

    //...................SL WIN RATE.........
    public int getSLWinLossCount(String result, int start, int end) {
        return tradeRepository.getSLWinLossCount(result, start, end);
    }

    //SL profit loss
    public double getSLProfit(int start, int end) {
        return tradeRepository.getSLProfit(start, end);
    }

    //Montly report get
    public LiveData<List<MonthlyGain>> getMonthlyGains(String selectedYear) {
        return tradeRepository.getMonthlyGains(selectedYear);
    }

    public void setSelectedYear(String year) {
        selectedYear.setValue(year);
    }

    public LiveData<List<MonthlyGain>> getMonthlyGains() {
        return monthlyGains;
    }

    //.............
    //............All years for spinner.........
    public LiveData<List<String>> getUniqueYears() {
        return tradeRepository.getUniqueYears();
    }

}
