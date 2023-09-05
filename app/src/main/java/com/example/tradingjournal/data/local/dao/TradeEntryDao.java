package com.example.tradingjournal.data.local.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tradingjournal.data.local.entities.TradeEntry;
import com.example.tradingjournal.models.MonthlyGain;

import java.util.List;

// TradeEntryDao.java
@Dao
public interface TradeEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTradeEntry(TradeEntry tradeEntry);

    @Query("SELECT COUNT(*) FROM journal")
    int getJournalCount();
//.....query all get entries

    @Query("SELECT * FROM journal")
    LiveData<List<TradeEntry>> getAllJournalEntries();

    //for privous Vlaues of Wconsistenct LConsistency and toatal
    @Query("SELECT wConsistency FROM journal ORDER BY id DESC LIMIT 1")
    int getPreviousWConsistency();

    @Query("SELECT lConsistency FROM journal  ORDER BY id DESC LIMIT 1")
    int getPreviousLConsistency();

    @Query("SELECT total FROM journal  ORDER BY id DESC LIMIT 1")
    int getPreviousTotal();

    //get W and L count
    @Query("SELECT COUNT(*) FROM journal WHERE result =:res")
    LiveData<Integer> getWinsLossCount(String res);

    @Query("SELECT COUNT(*) FROM journal WHERE result = 'L'")
    int getLossCount();

    @Query("SELECT SUM(profit) FROM journal WHERE result = 'W' AND profit > 0")
    double getSumOfWinningProfits();

    @Query("SELECT SUM(profit) FROM journal WHERE result = 'L' AND profit < 0")
    double getSumOfLossProfit();

    @Query("SELECT MAX(total) FROM journal")
    double getMaxTotal();

    @Query("SELECT total FROM journal")
    LiveData<List<Double>> getAlltotalValues();

    //................Error Count.........................
    @Query("SELECT COUNT(*) FROM journal WHERE error = :err")
    LiveData<Integer> getErrorCount(String err);

    @Query("SELECT COUNT(*) FROM journal WHERE error = 'Revenge'")
    int getRevengeErrorCount();

    @Query("SELECT COUNT(*) FROM journal WHERE error = 'Overtraded'")
    int getOverratedErrorCount();

    @Query("SELECT COUNT(*) FROM journal WHERE error = 'Bad entry'")
    int getBadEntryErrorCount();

    @Query("SELECT COUNT(*) FROM journal WHERE error = 'No sneaker'")
    int getNoSneakerErrorCount();

    //................Last ten values Chart.............
    @Query("SELECT COUNT(*) FROM journal WHERE result =:res AND id IN (SELECT id FROM journal ORDER BY id DESC LIMIT 10)")
    LiveData<Integer> getLastTenEnteriesWinLoss(String res);

    @Query("SELECT  COUNT(*) FROM journal WHERE result = 'L' AND id IN (SELECT id FROM journal ORDER BY id DESC LIMIT 10)")
    int getLastTenEntriesLoss();


    @Query("SELECT SUM(profit) FROM (SELECT profit FROM journal ORDER BY id DESC LIMIT :count)")
    LiveData<Double> getSumOfLastNProfits(int count);

    //........................nas das and so on.....win ratio
    @Query("SELECT  COUNT(*) FROM journal WHERE result = :res AND session = :sess AND pair = :p")
    int getWinPercentNAS(String sess, String res, String p);

    @Query("SELECT  COUNT(*) FROM journal WHERE ls = :longorShort AND result = :res")
    LiveData<Integer> getWinPercentLS(String longorShort, String res);


    //............mindset percetnage..........
    @Query("SELECT COUNT(*) FROM journal WHERE mindset =:mind")
    LiveData<Integer> getMindsetCalmCount(String mind);

    //..........sessions profit.........
    @Query("SELECT SUM(profit) FROM journal WHERE  result = :res AND pair =:p AND session = :ses")
    double getSessionWinningProfits(String ses, String res, String p);

    //..........Get TP.........
    @Query("SELECT COUNT(*) FROM journal WHERE tp =:TP")
    LiveData<Integer> getTpCount(String TP);

    //............Max Losing Consistency........
    @Query("SELECT MAX(lconsistency) FROM journal")
    int getMaxlConsistency();

    //............Max Winning Consistency.......
    @Query("SELECT MAX(wconsistency) FROM journal")
    int getMaxWConsistency();

    //....NAS weekly........
    @Query("SELECT COUNT(*) FROM journal WHERE day = :day AND result = :res AND pair = 'NAS' AND session = :ses")
    int getNASWeekDayWinLoss(String ses, String res, String day);

    //........WIN LOSS count according to day only..........
    @Query("SELECT COUNT(*) FROM journal WHERE day = :day AND result = :res ")
    int getWeekDayWinLossCount(String res, String day);

    @Query("SELECT COUNT(*) FROM journal WHERE result = :res AND stoploss >= :start AND stoploss <= :end")
    int getSLWinLossCount(String res, int start, int end);

    //..SL win profit...............
    @Query("SELECT SUM(profit) FROM journal WHERE stoploss >= :start AND stoploss <= :end")
    double getSLProfit(int start, int end);

    //.........Montholy report ...............
    @Query("SELECT SUBSTR(date, 4, 2) AS month, SUM(percentGain) AS totalGain, SUM(profit) AS totalProfit " +
            "FROM journal " +
            "WHERE SUBSTR(date, 7, 4) = :selectedYear " +
            "GROUP BY month " +
            "ORDER BY month")
    LiveData<List<MonthlyGain>> getMonthlyGains(String selectedYear);

    //all unique years for spinner
    @Query("SELECT DISTINCT SUBSTR(date, 7, 4) AS year FROM journal WHERE LENGTH(SUBSTR(date, 7, 4)) = 4 ORDER BY year DESC")
    LiveData<List<String>> getUniqueYears();

   //.............Delete all records from the journal.............
    @Query("DELETE FROM journal")
    void deleteAll();
       // Other DAO methods
    // ...
}
