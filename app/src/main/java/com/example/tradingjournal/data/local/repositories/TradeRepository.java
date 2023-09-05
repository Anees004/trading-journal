package com.example.tradingjournal.data.local.repositories;

import android.content.Context;
import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tradingjournal.data.local.dao.StartingBalanceInfoDao;
import com.example.tradingjournal.data.local.dao.TradeEntryDao;
import com.example.tradingjournal.data.local.database.AppDatabase;
import com.example.tradingjournal.data.local.entities.StartingBalanceInfo;
import com.example.tradingjournal.data.local.entities.TradeEntry;
import com.example.tradingjournal.models.MonthlyGain;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// TradeRepository.java
public class TradeRepository {
    //............................................................................
    private TradeEntryDao tradeEntryDao;


    public TradeRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        tradeEntryDao = database.tradeEntryDao();
    }

    public void insertTradeEntry(TradeEntry tradeEntry) {
        // Execute database operation on a background thread
        Executors.newSingleThreadExecutor().execute(() -> tradeEntryDao.insertTradeEntry(tradeEntry));
    }

    //...............get all entries .............
    public LiveData<List<TradeEntry>> getAllJournalEntries() {
        Future<LiveData<List<TradeEntry>>> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getAllJournalEntries();
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return tradeEntryDao.getAllJournalEntries();
        }

    }

    //.........for checking is journal table data available or not
    public Integer hasTradeEntries() {
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getJournalCount();
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //........for starting balance checking available or nto
    public int getPreviousTotal() {
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getPreviousTotal();
        });


        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //..........get max total value..........
    public double getMaxTotal() {
        Future<Double> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getMaxTotal();
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public int getPreviousWConsistency() {
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getPreviousWConsistency();
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getPreviousLConsistency() {
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getPreviousLConsistency();
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }


    //L and W count return

    public LiveData<Integer> getWinsLossCount(String result) {
        Future<LiveData<Integer>> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getWinsLossCount(result);
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return tradeEntryDao.getWinsLossCount(result);
        }
    }

    //........Total winning and lossing profit
    public double getSumOfWinningProfits() {
        Future<Double> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getSumOfWinningProfits();
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public double getSumOfLossProfit() {
        Future<Double> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getSumOfLossProfit();
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    //.............for lconsistency Max value,...................
    public int getMaxlConsistency() {
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getMaxlConsistency();
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //.............for Win consistency Max value,...................
    public int getMaxWConsistency() {
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getMaxWConsistency();
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public LiveData<List<Double>> getAlltotalValues() {
        Future<LiveData<List<Double>>> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getAlltotalValues();
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return tradeEntryDao.getAlltotalValues();
        }

    }

    //..........................Error count................

    public LiveData<Integer> getErrorCount(String error) {
        Future<LiveData<Integer>> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getErrorCount(error);
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return tradeEntryDao.getErrorCount(error);
        }
    }

    public int getRevengeErrorCount() {
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getRevengeErrorCount();
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getOverratedErrorCount() {
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getOverratedErrorCount();
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getBadEntryErrorCount() {
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getBadEntryErrorCount();
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getNoSneakerErrorCount() {
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getNoSneakerErrorCount();
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //....................For Last ten WIn and loss...............
    public LiveData<Integer> getLastTenEnteriesWinLoss(String result) {
        Future<LiveData<Integer>> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getLastTenEnteriesWinLoss(result);
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return tradeEntryDao.getLastTenEnteriesWinLoss(result);
        }
    }

    public int getLastTenEntriesLoss() {
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getLastTenEntriesLoss();
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public LiveData<Double> getSumOfLastNProfits(int i) {
        Future<LiveData<Double>> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getSumOfLastNProfits(i);
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return tradeEntryDao.getSumOfLastNProfits(i);
        }

    }

    //...................get SESSION WIN COUNT,,,,,.........................
    public int getWinPercentNAS(String session, String result, String pair) {
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getWinPercentNAS(session, result, pair);
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return tradeEntryDao.getWinPercentNAS(session, result, pair);
        }

    }

    //......................get Long Short win percentage............
    public LiveData<Integer> getWinPercentLS(String ls, String result) {
        Future<LiveData<Integer>> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getWinPercentLS(ls, result);
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return tradeEntryDao.getWinPercentLS(ls, result);
        }

    }

    //........get mindset count CALM........
    public LiveData<Integer> getMindsetCalmCount(String mindset) {
        Future<LiveData<Integer>> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getMindsetCalmCount(mindset);
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return tradeEntryDao.getMindsetCalmCount(mindset);
        }

    }

    //..........sessions profit.........
    public double getSessionWinningProfits(String session, String result, String pair) {
        Future<Double> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getSessionWinningProfits(session, result, pair);
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    //...........Tp count for closed early percentage..........
    public LiveData<Integer> getTpCount(String tp) {
        Future<LiveData<Integer>> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getTpCount(tp);
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return tradeEntryDao.getTpCount(tp);
        }

    }

    //..............NAS weekly.............
    public int getNASWeekDayWinLoss(String ses, String res, String day) {
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getNASWeekDayWinLoss(ses, res, day);
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }

    }

    //........WIN LOSS count according to day only..........
    public int getWeekDayWinLossCount(String result, String day) {
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getWeekDayWinLossCount(result, day);
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }

    }

    //...................SL WIN RATE.........
    public int getSLWinLossCount(String result, int start, int end) {
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getSLWinLossCount(result, start, end);
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }

    }

    //SL profit loss
    public double getSLProfit(int start, int end) {
        Future<Double> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getSLProfit(start, end);
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public LiveData<List<MonthlyGain>> getMonthlyGains(String selectedYear) {
        Future<LiveData<List<MonthlyGain>>> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getMonthlyGains(selectedYear);
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return tradeEntryDao.getMonthlyGains(selectedYear);
        }
    }

    //............All years for spinner.........
    public LiveData<List<String>> getUniqueYears() {
        Future<LiveData<List<String>>> future = Executors.newSingleThreadExecutor().submit(() -> {
            return tradeEntryDao.getUniqueYears();
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return tradeEntryDao.getUniqueYears();
        }
    }

    //................delete all records..............
    public void deleteAllFromjournal() {
        Executors.newSingleThreadExecutor().execute(() -> tradeEntryDao.deleteAll());
    }

    //.............................get total Balance..............

}