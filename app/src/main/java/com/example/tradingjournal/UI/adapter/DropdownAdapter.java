package com.example.tradingjournal.UI.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

// DropdownAdapter.java
public class DropdownAdapter extends ArrayAdapter<String> {

    public DropdownAdapter(Context context, List<String> items) {
        super(context, android.R.layout.simple_spinner_item, items);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
}
