package com.example.tradingjournal.UI.setting;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tradingjournal.UI.Input.InputActivity;
import com.example.tradingjournal.UI.ProgressDialogFragment;
import com.example.tradingjournal.data.local.entities.TradeEntry;
import com.example.tradingjournal.databinding.FragmentSettingBinding;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

public class SettingFragment extends Fragment {

    private SettingViewModel mViewModel;
    FragmentSettingBinding binding;
    private static final int REQUEST_CODE_OPEN_DIRECTORY = 1;
    ProgressDialogFragment progressDialog;
    private static final int REQUEST_CODE_PERMISSION = 2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        SettingViewModelFactory viewModelFactory = new SettingViewModelFactory(getContext());
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(SettingViewModel.class);
        progressDialog = ProgressDialogFragment.newInstance();
        binding.btnLinearLayoutExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show(getFragmentManager(), "ProgressDialog");
                String fileName = "(" + (new Date()).toString() + ") journal_entries.xlsx";
                File file = new File(getContext().getExternalFilesDir(null), fileName);
                new Handler().postDelayed(() -> {
                    mViewModel.exportToExcel(file, SettingFragment.this);
                    progressDialog.dismiss(); // Dismiss the dialog when the task is complete
                }, 5000); // Simulate a 3-second task
            }
        });

        binding.linearLayoutExportPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show(getFragmentManager(), "ProgressDialog");
                new Handler().postDelayed(() -> {
                    exportJournalToPdf();
                    progressDialog.dismiss(); // Dismiss the dialog when the task is complete
                }, 5000); // Simulate a 3-second task
            }
        });

        //..............................delete all records...........
        binding.linearLayoutDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show(getFragmentManager(), "ProgressDialog");
                new Handler().postDelayed(() -> {
                    mViewModel.deleteAllJournal();
                    progressDialog.dismiss(); // Dismiss the dialog when the task is complete
                    startActivity(new Intent(getContext(), InputActivity.class));
                    getActivity().finish();
                }, 5000); // Simulate a 3-second task
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void exportJournalToPdf() {
        exportJournalEntriesToPdf();
    }

    public void exportJournalEntriesToPdf() {

        try {
            String fileName = "(" + (new Date()).toString() + ") journal_entries.pdf";
            File file = new File(getContext().getExternalFilesDir(null), fileName);
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(file));
            PageSize pageSize = PageSize.A0; // Wider page size
            pdfDocument.setDefaultPageSize(pageSize);
            Document document = new Document(pdfDocument);

            mViewModel.getAllJournalEntries().observe(this, tradeEntries -> {
                float[] columnWidths = new float[19]; // Adjust the number of columns
                for (int i = 0; i < columnWidths.length; i++) {
                    columnWidths[i] = 1f; // Adjust the column widths as needed
                }
                Table table = new Table(columnWidths);
                table.addHeaderCell("ID");
                table.addHeaderCell("Pair");
                table.addHeaderCell("Date");
                table.addHeaderCell("Session");
                table.addHeaderCell("L/S");
                table.addHeaderCell("Result");
                table.addHeaderCell("%Gain");
                table.addHeaderCell("Profit");
                table.addHeaderCell("maxRR");
                table.addHeaderCell("error");
                table.addHeaderCell("mindSet");
                table.addHeaderCell("day");
                table.addHeaderCell("TP");
                table.addHeaderCell("tvPic");
                table.addHeaderCell("WConsistency");
                table.addHeaderCell("LConsistency");
                table.addHeaderCell("Total");
                table.addHeaderCell("StopLoss");
                table.addHeaderCell("DropDown");

                for (TradeEntry entry : tradeEntries) {
                    table.addCell(String.valueOf(entry.getId()));
                    table.addCell(entry.getPair());
                    table.addCell(entry.getSession());
                    table.addCell(entry.getDate());
                    table.addCell(entry.getLs());
                    table.addCell(entry.getResult());
                    table.addCell(String.valueOf(entry.getPercentGain()));
                    table.addCell(String.valueOf(entry.getProfit()));
                    table.addCell(String.valueOf(entry.getMaxRR()));
                    table.addCell(entry.getError());
                    table.addCell(entry.getMindset());
                    table.addCell(entry.getDay());
                    table.addCell(entry.getTp());
                    table.addCell(entry.getTvPic());
                    table.addCell(String.valueOf(entry.getWconsistency()));
                    table.addCell(String.valueOf(entry.getLconsistency()));
                    table.addCell(String.valueOf(entry.getTotal()));
                    table.addCell(String.valueOf(entry.getStoploss()));
                    table.addCell(String.valueOf(entry.getDrawdown()));
                }
                Toast.makeText(getContext(), "Saved Successfully exported to " + file, Toast.LENGTH_SHORT).show();
                document.add(table);
                document.close();
            });
        } catch (FileNotFoundException e) {
            Toast.makeText(getContext(), "Failed to Save",Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
    }

}