package com.example.tradingjournal.UI.Input;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tradingjournal.UI.Input.InputViewModel;

public class InputViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public InputViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(InputViewModel.class)) {
            return (T) new InputViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
