package com.example.tradingjournal.UI.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

// BooleanDropdownAdapter.java
public class BooleanDropdownAdapter extends ArrayAdapter<String> {

    public BooleanDropdownAdapter(Context context, String[] items) {
        super(context, android.R.layout.simple_spinner_item, items);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
}
