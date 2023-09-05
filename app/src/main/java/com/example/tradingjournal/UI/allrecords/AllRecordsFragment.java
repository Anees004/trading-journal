package com.example.tradingjournal.UI.allrecords;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tradingjournal.R;
import com.example.tradingjournal.UI.adapter.AllTradeEntriesAdapter;
import com.example.tradingjournal.UI.adapter.MonthlyGainsAdapter;
import com.example.tradingjournal.UI.dashboard.DashboardViewModel;
import com.example.tradingjournal.UI.dashboard.DashboardViewModelFactory;
import com.example.tradingjournal.databinding.FragmentAllRecordsBinding;
import com.example.tradingjournal.databinding.FragmentDashboardBinding;

public class AllRecordsFragment extends Fragment {

    private AllRecordsViewModel viewModel;
    private FragmentAllRecordsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAllRecordsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        AllRecordsViewModelFactory viewModelFactory = new AllRecordsViewModelFactory(getContext());
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AllRecordsViewModel.class);
        setRecycularView();
        return root;
    }

    void setRecycularView()
    {
        viewModel.getAllJournalEntries().observe(getViewLifecycleOwner(),tradeEntries -> {
        RecyclerView recyclerView = binding.recycularTradeEntry;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AllTradeEntriesAdapter adapter = new AllTradeEntriesAdapter(tradeEntries); // Pass your data here
        recyclerView.setAdapter(adapter);
        });
    }
}