package com.example.tradingjournal.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.tradingjournal.R;

public class EditBalanceDialog extends DialogFragment {
    private OnBalanceSavedListener balanceSavedListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_balance, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView)
                .setTitle("Edit Starting Balance")
                .setPositiveButton("Save", (dialog, id) -> {
                    EditText editTextBalance = dialogView.findViewById(R.id.editTextBalance);
                    double newBalance = Double.parseDouble(editTextBalance.getText().toString());
                    balanceSavedListener.saveNewBalance(newBalance);
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    // Do nothing, simply close the dialog
                });

        return builder.create();

    }
    public void setOnBalanceSavedListener(OnBalanceSavedListener listener) {
        this.balanceSavedListener = listener;
    }
    public interface OnBalanceSavedListener {
        void saveNewBalance(double newbalance);
    }
}
