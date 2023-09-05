package com.example.tradingjournal.data.local.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "starting_balance_info")
public class StartingBalanceInfo {

    @PrimaryKey()
    private int id = 1;

    private double totalStartingAmount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotalStartingAmount() {
        return totalStartingAmount;
    }

    public void setTotalStartingAmount(double totalStartingAmount) {
        this.totalStartingAmount = totalStartingAmount;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    private String  startingDate;

    // Getters and setters
}
