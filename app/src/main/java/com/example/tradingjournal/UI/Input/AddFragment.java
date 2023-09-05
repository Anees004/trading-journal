package com.example.tradingjournal.UI.Input;

import static android.app.Activity.RESULT_OK;

import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tradingjournal.R;
import com.example.tradingjournal.UI.EditBalanceDialog;
import com.example.tradingjournal.UI.adapter.DropdownAdapter;
import com.example.tradingjournal.data.local.entities.StartingBalanceInfo;
import com.example.tradingjournal.data.local.entities.TradeEntry;
import com.example.tradingjournal.databinding.FragmentAddBinding;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddFragment extends Fragment {
    private InputViewModel viewModel;
    private static final int PICK_EXCEL_FILE_REQUEST = 1;
    private Context context;
    private ProgressDialog progressDialog;
    private static final int REQUEST_IMAGE_PICK = 2;
    int checkSelection = 0;
    private File imageFile;

    private AddViewModel mViewModel;
    FragmentAddBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        InputViewModelFactory viewModelFactory = new InputViewModelFactory(getContext());
        viewModel = new ViewModelProvider(this, viewModelFactory).get(InputViewModel.class);
        setupSpinners();
        setupImport();
        if (viewModel.getStartingBalanceCount() > 0)
            binding.textStartingBalance.setText("Starting Balance: $" + viewModel.getTotalStartingBalance());
        else
            binding.textStartingBalance.setText("Starting Balance: $" + 0);
        //...............button image choose ..................
        binding.btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddImageClick(v);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupSpinners() {
        Spinner spinnerPair = binding.spinnerPair;
        Spinner spinnerSESS = binding.spinnerSESS;
        Spinner spinnerLS = binding.spinnerLS;
        Spinner spinnerResult = binding.spinnerResult;
        Spinner spinnerError = binding.spinnerError;
        Spinner spinnerMindset = binding.spinnerMindset;
        Spinner spinnerTP = binding.spinnerTP;
        EditText editDate = binding.editDate;
        EditText editProfit = binding.editProfit;
        TextView textDay = binding.textDay;
        Button btnSave = binding.btnSave;
        TextView textStartingBalance = binding.textStartingBalance;
        textStartingBalance.setOnClickListener(v -> {
            EditBalanceDialog dialog = new EditBalanceDialog();
            dialog.show(getChildFragmentManager(), "edit_balance_dialog");
            dialog.setOnBalanceSavedListener(new EditBalanceDialog.OnBalanceSavedListener() {
                @Override
                public void saveNewBalance(double newBalance) {
                    binding.textStartingBalance.setText("Starting Balance: $" + newBalance);
                    StartingBalanceInfo info = new StartingBalanceInfo();
                    info.setTotalStartingAmount(newBalance);
                    info.setStartingDate((new Date()).toString()); // Current date
                    viewModel.insertStartingBalanceInfo(info);
                }
            });
        });
        viewModel.getPairDropdownItems().observe(getViewLifecycleOwner(), pairItems -> {
            DropdownAdapter adapter = new DropdownAdapter(getContext(), pairItems);
            spinnerPair.setAdapter(adapter);
        });

        viewModel.getSESSDropdownItems().observe(getViewLifecycleOwner(), sessItems -> {
            DropdownAdapter adapter = new DropdownAdapter(getContext(), sessItems);
            spinnerSESS.setAdapter(adapter);
        });

        viewModel.getLSDropdownItems().observe(getViewLifecycleOwner(), lsItems -> {
            DropdownAdapter adapter = new DropdownAdapter(getContext(), lsItems);
            spinnerLS.setAdapter(adapter);
        });

        viewModel.getResultDropdownItems().observe(getViewLifecycleOwner(), resultItems -> {
            DropdownAdapter adapter = new DropdownAdapter(getContext(), resultItems);
            spinnerResult.setAdapter(adapter);
        });

        viewModel.getErrorDropdownItems().observe(getViewLifecycleOwner(), errorItems -> {
            DropdownAdapter adapter = new DropdownAdapter(getContext(), errorItems);
            spinnerError.setAdapter(adapter);
        });

        viewModel.getMindsetDropdownItems().observe(getViewLifecycleOwner(), mindsetItems -> {
            DropdownAdapter adapter = new DropdownAdapter(getContext(), mindsetItems);
            spinnerMindset.setAdapter(adapter);
        });

        viewModel.getTPDropdownItems().observe(getViewLifecycleOwner(), tpItems -> {
            DropdownAdapter adapter = new DropdownAdapter(getContext(), tpItems);
            spinnerTP.setAdapter(adapter);
        });

        editDate.setOnClickListener(v -> {
            // Get current date for initial selection
            Calendar calendar = Calendar.getInstance();
            int initialYear = calendar.get(Calendar.YEAR);
            int initialMonth = calendar.get(Calendar.MONTH);
            int initialDay = calendar.get(Calendar.DAY_OF_MONTH);

            // Create a DatePickerDialog when the date field is clicked
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view, year, monthOfYear, dayOfMonth) -> {
                        // Create a Calendar instance with the selected date
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, monthOfYear, dayOfMonth);

                        // Get the day name (e.g., "Mon", "Tue", etc.)
                        String dayName = new SimpleDateFormat("E", Locale.ENGLISH).format(selectedCalendar.getTime());
                        // Handle selected date
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        textDay.setText(" " + dayName);
                        editDate.setText(selectedDate);
                    },
                    initialYear, initialMonth, initialDay
            );

            datePickerDialog.show();
        });
        editDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editDate.setBackgroundResource(R.drawable.error_edittext_background);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editDate.setBackgroundResource(R.drawable.edittext_background);
            }

            @Override
            public void afterTextChanged(Editable s) {
                editDate.setBackgroundResource(R.drawable.edittext_background);
            }
        });
        binding.editPercentGain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.editPercentGain.setBackgroundResource(R.drawable.error_edittext_background);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.editPercentGain.setBackgroundResource(R.drawable.edittext_background);
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.editPercentGain.setBackgroundResource(R.drawable.edittext_background);
            }
        });

        editProfit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editProfit.setBackgroundResource(R.drawable.error_edittext_background);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editProfit.setBackgroundResource(R.drawable.edittext_background);
            }

            @Override
            public void afterTextChanged(Editable s) {
                editProfit.setBackgroundResource(R.drawable.edittext_background);
            }
        });
        binding.editMaxRR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.editMaxRR.setBackgroundResource(R.drawable.error_edittext_background);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.editMaxRR.setBackgroundResource(R.drawable.edittext_background);
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.editMaxRR.setBackgroundResource(R.drawable.edittext_background);
            }
        });
        btnSave.setOnClickListener(v -> {
            //check all the data is correct or not
// Perform validation and error checking
            boolean isValid = true;

// Check Pair Spinner
            if (spinnerPair.getSelectedItemPosition() == 0) {
                ((TextView) spinnerPair.getSelectedView()).setTextColor(Color.RED);
                isValid = false;
            }

// Check Session Spinner
            if (spinnerSESS.getSelectedItemPosition() == 0) {
                ((TextView) spinnerSESS.getSelectedView()).setTextColor(Color.RED);
                isValid = false;
            }

// Check Date EditText
            String date = editDate.getText().toString();
            if (date.isEmpty()) {
//                editDate.setHintTextColor(Color.RED);
                editDate.setBackgroundResource(R.drawable.error_edittext_background);
                isValid = false;
            }

// Check LS Spinner
            if (spinnerLS.getSelectedItemPosition() == 0) {
                ((TextView) spinnerLS.getSelectedView()).setTextColor(Color.RED);
                isValid = false;
            }

// Check Result Spinner
            if (spinnerResult.getSelectedItemPosition() == 0) {
                ((TextView) spinnerResult.getSelectedView()).setTextColor(Color.RED);
                isValid = false;
            }

// Check Percent Gain EditText
            String percentGainText = binding.editPercentGain.getText().toString();
            if (percentGainText.isEmpty()) {
//                binding.editPercentGain.setHintTextColor(Color.RED);
                binding.editPercentGain.setBackgroundResource(R.drawable.error_edittext_background);
                isValid = false;
            } else {
                try {
                    double percentGain = Double.parseDouble(percentGainText);
                    // Perform additional checks if needed
                } catch (NumberFormatException e) {
                    isValid = false;
                }
            }

// Check Profit EditText
            String profitText = editProfit.getText().toString();
            if (profitText.isEmpty()) {
//                editProfit.setHintTextColor(Color.RED);
                editProfit.setBackgroundResource(R.drawable.error_edittext_background);

                isValid = false;
            } else {
                try {
                    double profit = Double.parseDouble(profitText);
                    // Perform additional checks if needed
                } catch (NumberFormatException e) {
                    isValid = false;
                }
            }

// Check Max RR EditText
            String maxRRText = binding.editMaxRR.getText().toString();
            if (maxRRText.isEmpty()) {
//                binding.editMaxRR.setHintTextColor(Color.RED)
                binding.editMaxRR.setBackgroundResource(R.drawable.error_edittext_background);
                isValid = false;
            } else {
                try {
                    double maxRR = Double.parseDouble(maxRRText);
                    // Perform additional checks if needed
                } catch (NumberFormatException e) {
                    isValid = false;
                }
            }

// Check Error Spinner
            if (spinnerError.getSelectedItemPosition() == 0) {
                ((TextView) spinnerError.getSelectedView()).setTextColor(Color.RED);
                isValid = false;
            }

// Check Mindset Spinner
            if (spinnerMindset.getSelectedItemPosition() == 0) {
                ((TextView) spinnerMindset.getSelectedView()).setTextColor(Color.RED);
                isValid = false;
            }

// Check Day TextView
            String day = textDay.getText().toString();
            if (day.isEmpty()) {
                // Handle the validation of day as needed
            }

// Check TP Spinner
            if (spinnerTP.getSelectedItemPosition() == 0) {
                ((TextView) spinnerTP.getSelectedView()).setTextColor(Color.RED);
                isValid = false;
            }

// Check TV Pic EditText
            String tvPicText = binding.editTVPic.getText().toString();
            if (tvPicText.isEmpty()) {
                binding.editTVPic.setHintTextColor(Color.RED);
                isValid = false;
            }

            if (isValid) {

                try {
                    // Gather user inputs from the UI components
                    String pair = spinnerPair.getSelectedItem().toString();
                    String sess = spinnerSESS.getSelectedItem().toString();
//                    String date = editDate.getText().toString();
                    String ls = spinnerLS.getSelectedItem().toString();
                    String result = spinnerResult.getSelectedItem().toString();
                    double percentGain = Double.parseDouble(binding.editPercentGain.getText().toString());
                    double profit = Double.parseDouble(editProfit.getText().toString());
                    double maxRR = Double.parseDouble(binding.editMaxRR.getText().toString());
                    String error = spinnerError.getSelectedItem().toString();
                    String mindset = spinnerMindset.getSelectedItem().toString();
//                    String day = textDay.getText().toString(); // You need to set getViewLifecycleOwner() value
                    String tp = spinnerTP.getSelectedItem().toString();
                    String tvPic = binding.editTVPic.getText().toString();
                    double stopLoss = Double.parseDouble(binding.editStopLoss.getText().toString());
                    //-ve sign if Loss
                    if (result == "L" && profit >= 0)
                        profit *= -1;
                    // Create a TradeEntry object
                    TradeEntry tradeEntry = new TradeEntry(pair, sess, date, ls, result, percentGain, profit, maxRR, error, mindset, day, tp, tvPic, stopLoss);
                    int wConsistency = 0;
                    int lConsistency = 0;
                    double total = 0;
                    double maxTotal = 0;
                    double drawdown = 0;
                    if (viewModel.getTableExists() > 0) {

                        if ("W".equals(tradeEntry.getResult())) {
                            wConsistency = viewModel.getPreviousWConsistency() + 1;
                            total = viewModel.getPreviousTotal() + tradeEntry.getProfit();
                            lConsistency = 0;
                        } else if ("L".equals(tradeEntry.getResult())) {
                            wConsistency = 0;
                            lConsistency = viewModel.getPreviousLConsistency() + 1;
                            total = viewModel.getPreviousTotal() + tradeEntry.getProfit();
                        }

                        maxTotal = viewModel.getMaxTotal();

                        if (total < maxTotal)
                            drawdown = (total - maxTotal) / total;
                        else
                            drawdown = 0;

                    } else {
                        total = viewModel.getTotalStartingBalance();

                        if ("W".equals(spinnerResult.getSelectedItem())) {
                            wConsistency = 1;
                            lConsistency = 0;
                            total += tradeEntry.getProfit();
                            lConsistency = 0;
                        } else if ("L".equals(spinnerResult.getSelectedItem())) {
                            wConsistency = 0;
                            lConsistency = 1;
                            total -= tradeEntry.getProfit();
                        }
                    }
                    //count and put draw down..........
                    drawdown *= 100;
                    tradeEntry.setDrawdown(drawdown);
                    // Set calculated values
                    tradeEntry.setWconsistency(wConsistency);
                    tradeEntry.setLconsistency(lConsistency);
                    tradeEntry.setTotal(total);


                    showProgressDialog();
                    // Save the trade entry using ViewModel
                    if (viewModel.getStartingBalanceCount() > 0) {
                        viewModel.saveTradeEntry(tradeEntry);
                        Toast.makeText(getContext(), "Saved Successfully " + tradeEntry.getDate(), Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getContext(), "Enter Starting Balance first ", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    // Handle exceptions
                    Toast.makeText(getContext(), "Error saving trade entry: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    // Show an error message to the user if necessary
                } finally {
                    hideProgressDialog();
                }
            }
        });

    }

    private void setupImport() {
        binding.btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel.getStartingBalanceCount() > 0) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    startActivityForResult(intent, PICK_EXCEL_FILE_REQUEST);
                } else
                    Toast.makeText(context, "Enter Starting Balance first ", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Saving...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    //////,........................add image...................
    public void onAddImageClick(View view) {
        // Check for permission to access gallery

        pickImageFromGallery();
    }

    private void pickImageFromGallery() {

        checkSelection = 2;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_IMAGE_PICK) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();
            } else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private File saveImageToAppDirectory(Uri imageUri) {
        // Create a new directory named "images" if not exists
        File imagesDirectory = new File(requireContext().getFilesDir(), "images");
        if (!imagesDirectory.exists()) {
            imagesDirectory.mkdirs();
        }

        // Get the ID of the record and use it as the image name
        String imageName = String.valueOf("recordId");

        // Create the image file in the "images" directory
        File destFile = new File(imagesDirectory, imageName);

        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
            OutputStream outputStream = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
            return destFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_EXCEL_FILE_REQUEST && resultCode == RESULT_OK) {
            Uri fileUri = data.getData();
            viewModel.importExcelData(fileUri);
        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null && checkSelection == 2) {
            Uri selectedImageUri = data.getData();
            imageFile = saveImageToAppDirectory(selectedImageUri);
            binding.editTVPic.setText(imageFile + "");
            // Now you can use 'imageFile' for further processing
        }
    }

}