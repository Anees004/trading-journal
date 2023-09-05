package com.example.tradingjournal.UI.dashboard;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class DashboardViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public DashboardViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DashboardViewModel.class)) {
            return (T) new DashboardViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

