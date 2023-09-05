package com.example.tradingjournal.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tradingjournal.data.local.dao.StartingBalanceInfoDao;
import com.example.tradingjournal.data.local.dao.TradeEntryDao;
import com.example.tradingjournal.data.local.entities.StartingBalanceInfo;
import com.example.tradingjournal.data.local.entities.TradeEntry;

// AppDatabase.java
@Database(entities = {TradeEntry.class, StartingBalanceInfo.class}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract TradeEntryDao tradeEntryDao();
    public abstract StartingBalanceInfoDao startingBalanceInfoDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

