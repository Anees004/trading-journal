package com.example.tradingjournal.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tradingjournal.data.local.entities.StartingBalanceInfo;

@Dao
public interface StartingBalanceInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStartingBalanceInfo(StartingBalanceInfo info);

    @Query("SELECT * FROM starting_balance_info LIMIT 1")
    LiveData<StartingBalanceInfo> getStartingBalanceInfo();

    @Query("SELECT SUM(totalStartingAmount) FROM starting_balance_info")
    Double getTotalBalance();

    //.........check entries >0 or not
    @Query("SELECT COUNT(*) FROM starting_balance_info")
    int getStartingBalanceCount();
}

