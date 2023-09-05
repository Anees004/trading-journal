package com.example.tradingjournal.UI.setting;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SettingViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public SettingViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SettingViewModel.class)) {
            return (T) new SettingViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
