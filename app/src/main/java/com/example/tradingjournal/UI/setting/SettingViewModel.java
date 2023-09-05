package com.example.tradingjournal.UI.setting;

import static org.apache.logging.log4j.ThreadContext.getContext;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tradingjournal.data.local.entities.TradeEntry;
import com.example.tradingjournal.data.local.repositories.BalanceRepository;
import com.example.tradingjournal.data.local.repositories.TradeRepository;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class SettingViewModel extends ViewModel {
    private final Context context;

    private BalanceRepository balanceRepository;
    private TradeRepository tradeRepository;

    public SettingViewModel(Context context) {
        this.context = context;
        tradeRepository = new TradeRepository(context);
        balanceRepository = new BalanceRepository(context);
    }

    public LiveData<List<TradeEntry>> getAllJournalEntries() {
        return tradeRepository.getAllJournalEntries();
    }

    public void exportToExcel(File file, SettingFragment fragment) {


        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Journal Entries");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Pair");
        headerRow.createCell(3).setCellValue("Date");
        headerRow.createCell(2).setCellValue("Session");
        headerRow.createCell(4).setCellValue("L/S");
        headerRow.createCell(5).setCellValue("Result");
        headerRow.createCell(6).setCellValue("%Gain");
        headerRow.createCell(7).setCellValue("Profit");
        headerRow.createCell(8).setCellValue("maxRR");
        headerRow.createCell(9).setCellValue("error");
        headerRow.createCell(10).setCellValue("mindSet");
        headerRow.createCell(11).setCellValue("day");
        headerRow.createCell(12).setCellValue("TP");
        headerRow.createCell(13).setCellValue("tvPic");
        headerRow.createCell(14).setCellValue("WConsistency");
        headerRow.createCell(15).setCellValue("LConsistency");
        headerRow.createCell(16).setCellValue("Total");
        headerRow.createCell(17).setCellValue("StopLoss");
        headerRow.createCell(18).setCellValue("DropDown");
        // Add more columns as needed
        getAllJournalEntries().observe(fragment, journalEntries -> {
            // Add data rows
            int rowIdx = 1;
            for (TradeEntry entry : journalEntries) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(entry.getId());
                row.createCell(1).setCellValue(entry.getPair());
                row.createCell(2).setCellValue(entry.getSession());
                row.createCell(3).setCellValue(entry.getDate());
                row.createCell(4).setCellValue(entry.getLs());
                row.createCell(5).setCellValue(entry.getResult());
                row.createCell(6).setCellValue(entry.getPercentGain());
                row.createCell(7).setCellValue(entry.getProfit());
                row.createCell(8).setCellValue(entry.getMaxRR());
                row.createCell(9).setCellValue(entry.getError());
                row.createCell(10).setCellValue(entry.getMindset());
                row.createCell(11).setCellValue(entry.getDay());
                row.createCell(12).setCellValue(entry.getTp());
                row.createCell(13).setCellValue(entry.getTvPic());
                row.createCell(14).setCellValue(entry.getWconsistency());
                row.createCell(15).setCellValue(entry.getLconsistency());
                row.createCell(16).setCellValue(entry.getTotal());
                row.createCell(17).setCellValue(entry.getStoploss());
                row.createCell(18).setCellValue(entry.getDrawdown());
                // Add more cells for other fields
            }

            // Save workbook to the external storage
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                workbook.write(fos);
                fos.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Toast.makeText(context, "Journal entries exported to " + file, Toast.LENGTH_SHORT).show();

        });
    }

    //.............
    //................delete all records..............
    public void deleteAllJournal()
    {
        tradeRepository.deleteAllFromjournal();
    }
}