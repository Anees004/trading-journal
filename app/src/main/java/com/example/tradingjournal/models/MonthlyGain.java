package com.example.tradingjournal.models;

public class MonthlyGain {
    private String month;
    private double totalGain;

    public void setTotalGain(double totalGain) {
        this.totalGain = totalGain;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }

    private double totalProfit;

    public MonthlyGain(String month, double totalGain,double totalProfit) {
        this.month = month;
        this.totalGain = totalGain;
        this.totalProfit = totalProfit;
    }

    public String getMonth() {
        return month;
    }

    public double getTotalGain() {
        return totalGain;
    }
}
