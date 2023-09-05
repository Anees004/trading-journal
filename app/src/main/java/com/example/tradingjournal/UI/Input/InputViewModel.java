package com.example.tradingjournal.UI.Input;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tradingjournal.data.local.entities.StartingBalanceInfo;
import com.example.tradingjournal.data.local.entities.TradeEntry;
import com.example.tradingjournal.data.local.repositories.BalanceRepository;
import com.example.tradingjournal.data.local.repositories.TradeRepository;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class InputViewModel extends ViewModel {
    private TradeRepository tradeRepository;

    // Dropdown items
    private MutableLiveData<List<String>> pairDropdownItems = new MutableLiveData<>();
    private MutableLiveData<List<String>> sessDropdownItems = new MutableLiveData<>();
    private MutableLiveData<List<String>> lsDropdownItems = new MutableLiveData<>();
    private MutableLiveData<List<String>> resultDropdownItems = new MutableLiveData<>();
    private MutableLiveData<List<String>> errorDropdownItems = new MutableLiveData<>();
    private MutableLiveData<List<String>> mindsetDropdownItems = new MutableLiveData<>();
    private MutableLiveData<List<String>> tpDropdownItems = new MutableLiveData<>();

    // Selected values
    private MutableLiveData<String> selectedPair = new MutableLiveData<>();
    private MutableLiveData<String> selectedSESS = new MutableLiveData<>();
    private MutableLiveData<String> selectedLS = new MutableLiveData<>();
    private MutableLiveData<String> selectedResult = new MutableLiveData<>();
    private MutableLiveData<String> selectedError = new MutableLiveData<>();
    private MutableLiveData<String> selectedMindset = new MutableLiveData<>();
    private MutableLiveData<String> selectedTP = new MutableLiveData<>();

    // Initialize dropdowns and selected values in constructor
    private final Context context;
    private BalanceRepository balanceRepository;

    public InputViewModel(Context context) {
        this.context = context;
        tradeRepository = new TradeRepository(context);
        balanceRepository = new BalanceRepository(context);
        initializeDropdowns();
        initializeSelectedValues();
    }

    public Integer getTableExists() {
        return tradeRepository.hasTradeEntries();
    }

    public void saveTradeEntry(TradeEntry tradeEntry) {
        // Check if the table exists
        tradeRepository.insertTradeEntry(tradeEntry);
    }

    public int getPreviousWConsistency() {
        return tradeRepository.getPreviousWConsistency();
    }

    public int getPreviousLConsistency() {
        return tradeRepository.getPreviousLConsistency();
    }

    public double getPreviousTotal() {
        return tradeRepository.getPreviousTotal();
    }

    // Initialize dropdown items here
    private void initializeDropdowns() {
        // Initialize dropdown items here
        List<String> pairItems = Arrays.asList("Pair", "NAS", "DAX", "US30");
        pairDropdownItems.postValue(pairItems);

        List<String> sessItems = Arrays.asList("Session", "L", "M", "N");
        sessDropdownItems.postValue(sessItems);

        List<String> lsItems = Arrays.asList("L/S", "L", "S");
        lsDropdownItems.postValue(lsItems);

        List<String> resultItems = Arrays.asList("Result", "W", "L");
        resultDropdownItems.postValue(resultItems);

        List<String> errorItems = Arrays.asList("Error", "None", "Revenge", "Overtraded", "Bad entry", "No sneaker");
        errorDropdownItems.postValue(errorItems);

        List<String> mindsetItems = Arrays.asList("Mindset", "Calm", "Emotional");
        mindsetDropdownItems.postValue(mindsetItems);

        List<String> tpItems = Arrays.asList("TP", "Hit TP", "Closed early");
        tpDropdownItems.postValue(tpItems);
    }


    private void initializeSelectedValues() {
        // Initialize selected values here
        selectedPair.setValue("test1");
        selectedSESS.setValue("test2");
        selectedLS.setValue("");
        selectedResult.setValue("");
        selectedError.setValue("");
        selectedMindset.setValue("");
        selectedTP.setValue("");
    }

    public Double getTotalStartingBalance() {
        return balanceRepository.getStartingBalance();
    }

    // Provide getters for dropdown items and selected values
    public LiveData<List<String>> getPairDropdownItems() {
        return pairDropdownItems;
    }

    public LiveData<List<String>> getSESSDropdownItems() {
        return sessDropdownItems;
    }

    public LiveData<List<String>> getLSDropdownItems() {
        return lsDropdownItems;
    }

    public LiveData<List<String>> getResultDropdownItems() {
        return resultDropdownItems;
    }

    public LiveData<List<String>> getErrorDropdownItems() {
        return errorDropdownItems;
    }

    public LiveData<List<String>> getMindsetDropdownItems() {
        return mindsetDropdownItems;
    }

    public LiveData<List<String>> getTPDropdownItems() {
        return tpDropdownItems;
    }

    public LiveData<String> getSelectedPair() {
        return selectedPair;
    }

    public LiveData<String> getSelectedSESS() {
        return selectedSESS;
    }

    public LiveData<String> getSelectedLS() {
        return selectedLS;
    }

    public LiveData<String> getSelectedResult() {
        return selectedResult;
    }

    public LiveData<String> getSelectedError() {
        return selectedError;
    }

    public LiveData<String> getSelectedMindset() {
        return selectedMindset;
    }

    public LiveData<String> getSelectedTP() {
        return selectedTP;
    }

    // Provide methods to set selected values
    public void setSelectedPair(String pair) {
        selectedPair.setValue(pair);
    }

    public void setSelectedSESS(String sess) {
        selectedSESS.setValue(sess);
    }

    public void setSelectedLS(String ls) {
        selectedLS.setValue(ls);
    }

    public void setSelectedResult(String result) {
        selectedResult.setValue(result);
    }

    public void setSelectedError(String error) {
        selectedError.setValue(error);
    }

    public void setSelectedMindset(String mindset) {
        selectedMindset.setValue(mindset);
    }

    public void setSelectedTP(String tp) {
        selectedTP.setValue(tp);
    }

    //.....................................Starting Balance info............................
    public void insertStartingBalanceInfo(StartingBalanceInfo info) {
        balanceRepository.insertStartingBalanceInfo(info);
    }

    public LiveData<StartingBalanceInfo> getStartingBalanceInfo() {
        return balanceRepository.getStartingBalanceInfo();
    }

    //check starting balance table has value or not
    public int getStartingBalanceCount() {
        return balanceRepository.getStartingBalanceCount();
    }

    //.......get max total..........
    public double getMaxTotal() {
        return tradeRepository.getMaxTotal();
    }

    //..import from excel...........................
    public void importExcelData(Uri fileUri) {

        try {
            InputStream inputStream = context.getContentResolver().openInputStream(fileUri);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Assuming sheet name is "journal"
            for (Row row : sheet) {
                String pair = row.getCell(0).getStringCellValue();
                String sess = row.getCell(1).getStringCellValue();
                Date date = row.getCell(2).getDateCellValue();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = dateFormat.format(date);
                String ls = row.getCell(3).getStringCellValue();
                String result = row.getCell(4).getStringCellValue();
                double percentGain = Double.parseDouble(String.valueOf(row.getCell(5).getNumericCellValue()));
                percentGain *= 100.0;
                double profit = Double.parseDouble(String.valueOf(row.getCell(6).getNumericCellValue()));
                double maxRR = Double.parseDouble(String.valueOf(row.getCell(7).getNumericCellValue()));
                maxRR *= 100.0;
                String error = row.getCell(8).getStringCellValue();
                String mindset = row.getCell(9).getStringCellValue();
                String day = row.getCell(10).getStringCellValue(); // You need to set this value
                String tp = row.getCell(11).getStringCellValue();
                String tvPic = row.getCell(12).getStringCellValue();
                int wConsistency = (int) Double.parseDouble(String.valueOf(row.getCell(13).getNumericCellValue()));
                int lConsistency = (int) Double.parseDouble(String.valueOf(row.getCell(14).getNumericCellValue()));
                double total = Double.parseDouble(String.valueOf(row.getCell(15).getNumericCellValue()));
                double drawDown = Double.parseDouble(String.valueOf(row.getCell(16).getNumericCellValue()));
                double stopLoss = Double.parseDouble(String.valueOf(row.getCell(18).getNumericCellValue()));
                drawDown *= 100.0;
//                // Similarly, extract other cell values and create a TradeEntry object
//
                TradeEntry tradeEntry = new TradeEntry(pair, sess, formattedDate, ls, result, percentGain, profit, maxRR, error, mindset, day, tp, tvPic,stopLoss);
                tradeEntry.setWconsistency(wConsistency);
                tradeEntry.setLconsistency(lConsistency);
                tradeEntry.setTotal(total);
                tradeEntry.setDrawdown(drawDown);
                saveTradeEntry(tradeEntry);
            }
            Toast.makeText(context, "Save Successfully ", Toast.LENGTH_SHORT).show();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }


//        if (!tradeEntries.isEmpty()) {
//            tradeRepository.insertTradeEntry(tradeEntries);
//        }
    }


}
