package com.example.tradingjournal.data.local.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.tradingjournal.data.local.dao.StartingBalanceInfoDao;
import com.example.tradingjournal.data.local.database.AppDatabase;
import com.example.tradingjournal.data.local.entities.StartingBalanceInfo;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BalanceRepository {
    private StartingBalanceInfoDao startingBalanceInfoDao;

    public BalanceRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        startingBalanceInfoDao = database.startingBalanceInfoDao();

    }
    public Double getStartingBalance() {
        Future<Double> future = Executors.newSingleThreadExecutor().submit(() -> {
            return startingBalanceInfoDao.getTotalBalance();
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public Integer getStartingBalanceCount() {
        Future<Integer> future = Executors.newSingleThreadExecutor().submit(() -> {
            return startingBalanceInfoDao.getStartingBalanceCount();
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //..................Starting Balance ...............................
    public void insertStartingBalanceInfo(StartingBalanceInfo info) {
        Executors.newSingleThreadExecutor().execute(() -> {
            startingBalanceInfoDao.insertStartingBalanceInfo(info);
        });
    }

    public LiveData<StartingBalanceInfo> getStartingBalanceInfo() {
        return startingBalanceInfoDao.getStartingBalanceInfo();
    }



}
