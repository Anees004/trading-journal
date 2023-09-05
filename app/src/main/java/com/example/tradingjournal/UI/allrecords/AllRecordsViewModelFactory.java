package com.example.tradingjournal.UI.allrecords;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class AllRecordsViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public AllRecordsViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AllRecordsViewModel.class)) {
            return (T) new AllRecordsViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

