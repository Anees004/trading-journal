package com.example.tradingjournal.UI.home;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tradingjournal.R;
import com.example.tradingjournal.UI.EditBalanceDialog;
import com.example.tradingjournal.UI.Input.InputViewModel;
import com.example.tradingjournal.UI.Input.InputViewModelFactory;
import com.example.tradingjournal.UI.allrecords.AllRecordsViewModel;
import com.example.tradingjournal.data.local.entities.StartingBalanceInfo;
import com.example.tradingjournal.databinding.FragmentAddBinding;
import com.example.tradingjournal.databinding.FragmentHomeBinding;

import java.text.DecimalFormat;
import java.util.Date;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    double avgLoss,avgWin;
    DecimalFormat decimalFormat;
    FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        HomeViewModelFactory viewModelFactory = new HomeViewModelFactory(getContext());
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(HomeViewModel.class);
        binding.txtBalance.setText("£" + mViewModel.getTotal());
        double profit = mViewModel.getTotal() - mViewModel.getStartBalance();
        binding.txtProfit.setText("£" + profit);
        binding.txtTotalTrades.setText("" + mViewModel.getTotalEntries());
        decimalFormat = new DecimalFormat("0.00");

        binding.linearStartAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditBalanceDialog dialog = new EditBalanceDialog();
                dialog.show(getChildFragmentManager(), "edit_balance_dialog");
                dialog.setOnBalanceSavedListener(new EditBalanceDialog.OnBalanceSavedListener() {
                    @Override
                    public void saveNewBalance(double newBalance) {
                        binding.txtStartBalance.setText("£" + newBalance);
                        StartingBalanceInfo info = new StartingBalanceInfo();
                        info.setTotalStartingAmount(newBalance);
                        info.setStartingDate((new Date()).toString()); // Current date
                        mViewModel.insertStartingBalanceInfo(info);
                    }
                });
            }
        });
        setStartAmount();
        setAvgWinTxt();
        setAvgLossTxt();
        setRRatio();

        return root;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void setStartAmount() {
        binding.txtStartBalance.setText("£"+mViewModel.getStartingAmount());
    }

    private void setAvgWinTxt() {
        mViewModel.getWinsLossCount("W").observe(getViewLifecycleOwner(), wins -> {
             avgWin = mViewModel.getSumOfWinningProfits() / wins;
            binding.txtAvgWin.setText("£"+avgWin + "");
        });

    }

    private void setAvgLossTxt() {
        mViewModel.getWinsLossCount("L").observe(getViewLifecycleOwner(), loss -> {
             avgLoss = mViewModel.getSumOfLossProfit() / loss;
             avgLoss = Math.abs(avgLoss);
            binding.txtAvgLoss.setText("£"+avgLoss + "");
        });
    }

    private void setRRatio() {
        mViewModel.getWinsLossCount("W").observe(getViewLifecycleOwner(), wins -> {
            mViewModel.getWinsLossCount("L").observe(getViewLifecycleOwner(), loss -> {
                double winPercent = (wins + loss);
                winPercent = wins / winPercent;
                double RR = avgWin/avgLoss;
                binding.txtRExpectancy.setText(decimalFormat.format(winPercent*RR)+"");
                binding.txtRRatio.setText(RR+"");
               });
        });
    }
}