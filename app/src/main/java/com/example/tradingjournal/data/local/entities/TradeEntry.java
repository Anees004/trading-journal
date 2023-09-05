package com.example.tradingjournal.data.local.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "journal", indices = {@Index(value = "id", unique = true)})
public class TradeEntry {
    public TradeEntry() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;


    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLs() {
        return ls;
    }

    public void setLs(String ls) {
        this.ls = ls;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public double getPercentGain() {
        return percentGain;
    }

    public void setPercentGain(double percentGain) {
        this.percentGain = percentGain;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }


    public double getMaxRR() {
        return maxRR;
    }

    public void setMaxRR(double maxRR) {
        this.maxRR = maxRR;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMindset() {
        return mindset;
    }

    public void setMindset(String mindset) {
        this.mindset = mindset;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    public String getTvPic() {
        return tvPic;
    }

    public void setTvPic(String tvPic) {
        this.tvPic = tvPic;
    }

    public int getWconsistency() {
        return wconsistency;
    }

    public void setWconsistency(int wconsistency) {
        this.wconsistency = wconsistency;
    }

    public int getLconsistency() {
        return lconsistency;
    }

    public void setLconsistency(int lconsistency) {
        this.lconsistency = lconsistency;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDrawdown() {
        return drawdown;
    }

    public void setDrawdown(double drawdown) {
        this.drawdown = drawdown;
    }

    public double getStoploss() {
        return stoploss;
    }

    public void setStoploss(double stoploss) {
        this.stoploss = stoploss;
    }

    public TradeEntry(String pair, String session, String date, String ls, String result, double percentGain, double profit, double maxRR, String error, String mindset, String day, String tp, String tvPic, double stopLoss) {

        this.pair = pair;
        this.session = session;
        this.date = date;
        this.ls = ls;
        this.result = result;
        this.percentGain = percentGain;
        this.profit = profit;
        this.maxRR = maxRR;
        this.error = error;
        this.mindset = mindset;
        this.day = day;
        this.tp = tp;
        this.tvPic = tvPic;
        this.stoploss = stopLoss;
    }


    private String pair;
    private String session;
    private String date;
    private String ls;
    private String result;
    private double percentGain;
    private double profit;
    private double maxRR;
    private String error;
    private String mindset;
    private String day;
    private String tp;
    private String tvPic;
    private int wconsistency, lconsistency;
    private double total;
    private double stoploss;
    private double drawdown;

}
