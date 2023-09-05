package com.example.tradingjournal.UI.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tradingjournal.R;
import com.example.tradingjournal.UI.Input.InputActivity;
import com.example.tradingjournal.UI.adapter.MonthlyGainsAdapter;
import com.example.tradingjournal.UI.dashboard.DashboardViewModel;
import com.example.tradingjournal.UI.dashboard.DashboardViewModelFactory;
import com.example.tradingjournal.databinding.FragmentDashboardBinding;
import com.example.tradingjournal.models.MonthlyGain;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {
    DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    DecimalFormat decimalFormat;
    int noneErrorCount, revengeErrorCount, overratedErrorCount, noSneakerErrorCount, badEntryErrorCount;
    double winPercent, lossPercent, winningAmount, lossingAmount;
    int maxlConsistency;
    int textColor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModelFactory viewModelFactory = new DashboardViewModelFactory(getContext());
        dashboardViewModel = new ViewModelProvider(this, viewModelFactory).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        textColor = ContextCompat.getColor(getContext(), R.color.text_color); // Replace "your_color_name" with the actual color resource name

        decimalFormat = new DecimalFormat("0.00");
        winningAmount = dashboardViewModel.getSumOfWinningProfits();
        lossingAmount = dashboardViewModel.getSumOfLossProfit();
        //...............................................................
        dashboardViewModel.getWinsLossCount("W").observe(getViewLifecycleOwner(), wins -> {
            dashboardViewModel.getWinsLossCount("L").observe(getViewLifecycleOwner(), loss -> {
                maxlConsistency = dashboardViewModel.getMaxlConsistency();
                winPercent = (wins + loss);
                winPercent = wins / winPercent;
                winPercent *= 100;
                lossPercent = winPercent - 100;
                winPercentageChart(wins, loss, winPercent);
                profitFactorChart(winningAmount, lossingAmount, wins, loss, winPercent, lossPercent);
                maxDrawDownChar(maxlConsistency);
            });
        });


        //........ go to Input Activity if table has no data.........
        if (dashboardViewModel.hastableEntriesCount() < 1) {
            Intent intent = new Intent(getContext(), InputActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        allAmountLineChart();
        errorCountChart();
        lastTenEntriesResultChar();
        lastTenProfitsResultChar();
        displayLineChartLasTenProfit();
        sessionPerformanceChart();
        longShortWinLossChart();
        mindsetPieChart();
        sessionProfitsChart();
        tpClosedDecipline();
        weeklyLondonNasChart();
        weeklyMixedNasChart();
        weeklyNYNasChart();
        dayWinLossPosNegChart();
        slWinRateChart();
        slProfitLossBarChart();
        monthlyProfitBarChart();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    //...........................................for Winning  chart...............................
    void winPercentageChart(int wins, int loss, double winPercent) {
        PieChart pieChartWin = binding.pieChartWin;
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(wins, "Wins"));
        entries.add(new PieEntry(loss, "Losses"));

        // Create a dataset with the data entries
        PieDataSet dataSet = new PieDataSet(entries, "Trade Results");
        dataSet.setColors(Color.rgb(76, 175, 80), Color.rgb(183, 28, 28));

        // Create a PieData object from the dataset
        PieData pieData = new PieData(dataSet);
        dataSet.setDrawValues(true);
        dataSet.setValueTextSize(12);
        dataSet.setValueTextColor(Color.WHITE);
        // Customize the chart
        pieChartWin.setUsePercentValues(false);
        pieChartWin.setData(pieData);
        pieChartWin.setCenterTextSize(20);
        pieChartWin.setCenterText(decimalFormat.format(winPercent) + "%");
        pieChartWin.getDescription().setEnabled(false);
        Legend legend = pieChartWin.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setTextColor(textColor);
        legend.setDrawInside(false);
        legend.setXEntrySpace(2f);
        legend.setYEntrySpace(0f);
        legend.setYOffset(10f);
        pieChartWin.invalidate(); // Refresh the chart
        pieChartWin.animateY(2000);
    }
    //...........................................for Second chart...............................

    void profitFactorChart(double profit, double loss, int tWinsCount, int tLossCount, double winPercent, double lossPercent) {
        double avgProfitAmount = profit / tWinsCount;
        double avgLossAmount = loss / tLossCount;

        double profitFactor = (avgProfitAmount * winPercent) / (avgLossAmount * lossPercent);

        PieChart pieChartProfit = binding.pieChartProfit;
        ArrayList<PieEntry> pieChartProfitentries = new ArrayList<>();
        pieChartProfitentries.add(new PieEntry(100, ""));
        PieDataSet dataSet = new PieDataSet(pieChartProfitentries, "Profit Factors");
        dataSet.setColors(ColorTemplate.rgb("#009900"));
        PieData pieData = new PieData(dataSet);
        pieChartProfit.setUsePercentValues(true);
        pieChartProfit.setData(pieData);
        dataSet.setDrawValues(false);
        pieChartProfit.setCenterTextSize(20);
        pieChartProfit.setCenterText(decimalFormat.format(profitFactor));
        pieChartProfit.getDescription().setEnabled(false);
        Legend legend = pieChartProfit.getLegend();
        legend.setTextColor(textColor);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        pieChartProfit.invalidate(); // Refresh the chart
        pieChartProfit.animateY(2000);

    }

    void maxDrawDownChar(int maxDrawDown) {
        PieChart pieChart = binding.pieChartMaxDrawdown;
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(maxlConsistency, ""));


        // Create a dataset with the data entries
        PieDataSet dataSet = new PieDataSet(entries, "Max DrawDown");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        // Create a PieData object from the dataset
        PieData pieData = new PieData(dataSet);

        // Customize the chart
        pieChart.setUsePercentValues(true);
        pieChart.setData(pieData);
        dataSet.setDrawValues(false);
        pieChart.setCenterTextSize(20);
        pieChart.setCenterText(decimalFormat.format(maxDrawDown) + "%");
        pieChart.getDescription().setEnabled(false);
        Legend legend = pieChart.getLegend();
        legend.setTextColor(textColor);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        pieChart.invalidate(); // Refresh the chart
        pieChart.animateY(2000);
    }

    void allAmountLineChart() {
        LineChart lineChart = binding.lineChartTotalAmount;
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.getAxisRight().setTextColor(textColor);
        lineChart.getAxisLeft().setTextColor(textColor);
        lineChart.zoomIn();
        lineChart.getDescription().setEnabled(false);

        dashboardViewModel.getAlltotalValues().observe(getViewLifecycleOwner(), profits -> {
            List<Entry> entries = new ArrayList<>();
            entries.add(new Entry(0, dashboardViewModel.getTotalStartingBalance().floatValue()));
            for (int i = 0; i < profits.size(); i++) {
                Entry entry = new Entry(i + 1, profits.get(i).floatValue());
                entries.add(entry);
            }

            LineDataSet dataSet = new LineDataSet(entries, "Profit Values");
            dataSet.setMode(LineDataSet.Mode.LINEAR);
            dataSet.setColor(textColor);
            dataSet.setValueTextColor(textColor);
            dataSet.setDrawCircles(true);
            dataSet.setCircleColor(textColor);
            dataSet.setCircleColorHole(textColor);
            dataSet.setDrawValues(false);
            dataSet.setDrawFilled(true);
            dataSet.setDrawValues(true);
            dataSet.setHighlightEnabled(true);

//            dataSet.setFillDrawable(getDrawable(R.drawable.gradiant_light_red)); // Use your gradient drawable

            List<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet);

            LineData lineData = new LineData(dataSets);

            lineChart.setData(lineData);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter() {
                public String getAxisLabel(float value, AxisBase axis) {
                    return String.valueOf(value);
                }
            });
            xAxis.setTextSize(textColor);
            Legend legend = lineChart.getLegend();
            legend.setTextColor(textColor);
            lineChart.animateX(800, Easing.EasingOption.EaseInOutQuart);
            lineChart.invalidate();
        });
    }


    void errorCountChart() {
        PieChart halfPieChart = binding.pieChartErrors;

        dashboardViewModel.getErrorCount("None").observe(getViewLifecycleOwner(), errorCount -> {

            noneErrorCount = errorCount;
            revengeErrorCount = dashboardViewModel.getRevengeErrorCount();
            overratedErrorCount = dashboardViewModel.getOverratedErrorCount();
            badEntryErrorCount = dashboardViewModel.getBadEntryErrorCount();
            noSneakerErrorCount = dashboardViewModel.getNoSneakerErrorCount();
            ArrayList<PieEntry> entries = new ArrayList<>();
            if (noneErrorCount > 0) {
                entries.add(new PieEntry(noneErrorCount, "None"));
            }
            if (revengeErrorCount > 0) {
                entries.add(new PieEntry(revengeErrorCount, "Revenge"));
            }
            if (overratedErrorCount > 0) {
                entries.add(new PieEntry(overratedErrorCount, "Overtraded"));
            }
            if (badEntryErrorCount > 0) {
                entries.add(new PieEntry(badEntryErrorCount, "Bad Entry"));
            }
            if (noSneakerErrorCount > 0) {
                entries.add(new PieEntry(noSneakerErrorCount, "No Sneaker"));
            }

            PieDataSet dataSet = new PieDataSet(entries, "Error Types");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            dataSet.setValueTextColor(Color.WHITE); // Set value text color to white
            dataSet.setValueTextSize(12f); // Set value text size

            PieData pieData = new PieData(dataSet);
            pieData.setValueFormatter(new DefaultValueFormatter(0)); // Show values as integers

            halfPieChart.setData(pieData);
            halfPieChart.getDescription().setEnabled(false);
            halfPieChart.setRotationEnabled(true);
            halfPieChart.setCenterText("Error Types");
            halfPieChart.animateXY(800, 800);

            // Setup Legend
            Legend legend = halfPieChart.getLegend();
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            legend.setOrientation(Legend.LegendOrientation.VERTICAL);
            legend.setDrawInside(false);
            legend.setXEntrySpace(2f);
            legend.setYEntrySpace(0f);
            legend.setYOffset(10f); // Adjust offset for better positioning
            legend.setTextColor(textColor);
            halfPieChart.invalidate();
        });
    }


    void lastTenEntriesResultChar() {
        dashboardViewModel.getLastTenEnteriesWinLoss("W").observe(getViewLifecycleOwner(), wins -> {
            dashboardViewModel.getLastTenEnteriesWinLoss("L").observe(getViewLifecycleOwner(), loss -> {
                int win = wins;
                double percent = win / 10.0;
                percent *= 100;
                PieChart pieChartWin = binding.pieChartLastTenResults;
                ArrayList<PieEntry> entries = new ArrayList<>();
                entries.add(new PieEntry(wins, "Wins"));
                entries.add(new PieEntry(loss, "Loss"));
                // Create a dataset with the data entries
                PieDataSet dataSet = new PieDataSet(entries, "Last Ten Trades");
                dataSet.setColors(Color.rgb(76, 175, 80), Color.rgb(183, 28, 28));
                dataSet.setValueTextColor(Color.WHITE); // Set value text color to white
                dataSet.setValueTextSize(12f); // Set value text size
                // Create a PieData object from the dataset
                PieData pieData = new PieData(dataSet);

                // Customize the chart
//        pieChartWin.setUsePercentValues(true);
                pieChartWin.setData(pieData);
                pieChartWin.setCenterTextSize(14);
                pieChartWin.setEntryLabelTextSize(9f);
                pieData.setValueFormatter(new DefaultValueFormatter(0)); // Show values as integers
                pieChartWin.setCenterText((int) (percent) + "%");
                pieChartWin.getDescription().setEnabled(false);
                Legend legend = pieChartWin.getLegend();
                legend.setTextColor(textColor);
                pieChartWin.invalidate(); // Refresh the chart
                pieChartWin.animateY(2000);

            });
        });
    }

    void lastTenProfitsResultChar() {
        BarChart barChart = binding.barChartLastten;
        dashboardViewModel.getLastNSumOfProfits(10).observe(getViewLifecycleOwner(), sums -> {
            ArrayList<BarEntry> entries = new ArrayList<>();
            for (int i = sums.size() - 1; i >= 0; i--) {
                float value = (i < sums.size() - 1) ? sums.get(i).floatValue() - sums.get(i + 1).floatValue() : sums.get(i).floatValue();
                entries.add(new BarEntry(i + 1, value));
            }

            if (!entries.isEmpty()) {
                BarDataSet dataSet = new BarDataSet(entries, "Days Statistics");

                ArrayList<Integer> colors = new ArrayList<>();
                for (BarEntry entry : entries) {
                    if (entry.getY() >= 0) {
                        colors.add(ColorTemplate.rgb("FF4CAF50")); // Green for positive values
                    } else {
                        colors.add(ColorTemplate.rgb("FFB71C1C")); // Red for negative values
                    }
                }
                dataSet.setColors(colors);

                BarData barData = new BarData(dataSet);
                barChart.setData(barData);
                barChart.getDescription().setEnabled(false);
                barData.setValueTextColor(textColor);
                barChart.getAxisRight().setTextColor(textColor);
                barChart.getXAxis().setTextColor(textColor);
                // Customize X-axis labels
                XAxis xAxis = barChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setTextColor(textColor);

                // Customize Y-axis
                YAxis yAxis = barChart.getAxisLeft();
                yAxis.setValueFormatter(new IndexAxisValueFormatter() {
                    public String getFormattedValue(float value) {
                        return Math.abs(value) + ""; // Display positive values only
                    }
                });

                // Customize data labels
                Legend legend = barChart.getLegend();
                legend.setTextColor(textColor);
                dataSet.setDrawValues(false);
                barChart.invalidate();
            } else {
                barChart.clear();
            }
        });
    }


    private void displayLineChartLasTenProfit() {
        LineChart lineChart = binding.lastTenProfitsLineChart;
        dashboardViewModel.getLastNSumOfProfits(10).observe(getViewLifecycleOwner(), sums -> {
            ArrayList<Entry> entries = new ArrayList<>();
            for (int i = 0; i < sums.size(); i++) {
                entries.add(new Entry(i + 1, sums.get(i).floatValue()));
            }

            LineDataSet dataSet = new LineDataSet(entries, "Last 10 Profits");
            dataSet.setColor(Color.RED);
            dataSet.setDrawCircles(false);
            dataSet.setLineWidth(2f); // Make the line width thicker
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Use a smoother curve
            dataSet.setValueTextColor(textColor);
            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);
            lineChart.getAxisRight().setTextColor(textColor);
            // Customize X-axis appearance
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextColor(textColor);
            xAxis.setDrawGridLines(false); // Hide vertical grid lines

            // Customize Y-axis appearance
            YAxis leftAxis = lineChart.getAxisLeft();
            leftAxis.setTextColor(textColor);
            leftAxis.setDrawGridLines(false); // Hide horizontal grid lines
            leftAxis.setZeroLineColor(textColor);
            // Customize chart appearance
            lineChart.getDescription().setEnabled(false);
            lineChart.setDrawGridBackground(false);
            lineChart.setDrawBorders(true);
            lineChart.setPinchZoom(true); // Enable zooming
            Legend legend = lineChart.getLegend();
            legend.setTextColor(textColor);
            lineChart.animateX(800);
            lineChart.invalidate();
        });
    }


    //........Indices Session Performance....................
    private void sessionPerformanceChart() {
        BarChart barChart = binding.barChartSession;
        List<BarEntry> entries = new ArrayList<>();

        final String[] entityNames = new String[]{"London", "New York", "Mixed"};
        final String[] pair = new String[]{"NAS", "DAX", "US30"};
        final int[] entityColors = new int[]{Color.rgb(76, 175, 80), Color.rgb(183, 28, 28)};


        for (int i = 0; i < pair.length; i++) {
            for (int j = 0; j < entityNames.length; j++) {
                int winCount = dashboardViewModel.getWinPercentNAS(entityNames[j].substring(0, 1), "W", pair[i]);
                int lossCount = dashboardViewModel.getWinPercentNAS(entityNames[j].substring(0, 1), "L", pair[i]);

                if (winCount + lossCount > 0) { // Only create chart if there is data
                    double winPercentage = (double) winCount / (winCount + lossCount) * 100;
                    double lossPercentage = 100 - winPercentage;
                    entries.add(new BarEntry(j, new float[]{(float) winPercentage, (float) lossPercentage}));
                }
            }
        }

        if (!entries.isEmpty()) { // Only create chart if there are entries
            BarDataSet dataSet = new BarDataSet(entries, "Indicies Session Performance");
            dataSet.setColors(entityColors);
            dataSet.setStackLabels(new String[]{"Win", "Loss"});

            BarData barData = new BarData(dataSet);
            barChart.setData(barData);

            // Customize appearance and labels
            barChart.getDescription().setEnabled(false);
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            barChart.getXAxis().setGranularity(1f);
            barChart.getXAxis().setTextColor(textColor);
            // Customize Y-axis appearance
            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setAxisMinimum(0f);
            leftAxis.setTextColor(textColor);
            barChart.getAxisRight().setTextColor(textColor);
            // Customize data label appearance
            dataSet.setValueTextColor(Color.WHITE);
            dataSet.setValueTextSize(14f);

            // Set custom value formatter for data labels
            dataSet.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return String.format("%.0f%%", value);// Add percentage sign to the value
                }
            });

            // Set custom X-axis labels with pair and entity names
            final String[] xLabels = new String[pair.length * entityNames.length];
            int index = 0;
            for (int i = 0; i < pair.length; i++) {
                for (int j = 0; j < entityNames.length; j++) {
                    xLabels[index] = pair[i] + " " + entityNames[j];
                    index++;
                }
            }
            Legend legend = barChart.getLegend();
            legend.setTextColor(textColor);
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabels));
            barChart.invalidate();
        } else {
            barChart.clear(); // Clear the chart if there are no entries
        }
    }

    //..........win loss short long chart.......
    private void longShortWinLossChart() {
        HorizontalBarChart barChart = binding.barChartWinLoss;
        dashboardViewModel.getWinPercentLS("L", "W").observe(getViewLifecycleOwner(), wincountLong -> {
            dashboardViewModel.getWinPercentLS("L", "L").observe(getViewLifecycleOwner(), losscountLong -> {
                dashboardViewModel.getWinPercentLS("S", "W").observe(getViewLifecycleOwner(), wincountShort -> {
                    dashboardViewModel.getWinPercentLS("S", "L").observe(getViewLifecycleOwner(), losscountShort -> {
                        int winCountLong = wincountLong;
                        int lossCountLong = losscountLong;
                        int winCountShort = wincountShort;
                        int lossCountShort = losscountShort;
                        float winPercentageLong = ((float) winCountLong / (winCountLong + lossCountLong)) * 100;
                        float lossPercentageLong = 100 - winPercentageLong;
                        float winPercentageShort = ((float) winCountShort / (winCountShort + lossCountShort)) * 100;
                        float lossPercentageShort = 100 - winPercentageShort;

                        List<BarEntry> entries = new ArrayList<>();
                        entries.add(new BarEntry(0, new float[]{winPercentageLong, lossPercentageLong}));
                        entries.add(new BarEntry(1, new float[]{winPercentageShort, lossPercentageShort}));

                        BarDataSet dataSet = new BarDataSet(entries, "Long/Short Win Chart");
                        dataSet.setLabel("Long/Short Win Chart");
                        dataSet.setColors(new int[]{Color.rgb(183, 28, 28), Color.rgb(76, 175, 80)});
                        BarData barData = new BarData(dataSet);
                        barChart.setData(barData);

                        barChart.setDrawValueAboveBar(false);
                        barChart.getDescription().setEnabled(false);
                        barChart.getLegend().setEnabled(false);

                        barChart.getAxisLeft().setEnabled(false);

                        barChart.getAxisRight().setEnabled(false);

                        barChart.getXAxis().setEnabled(false);
                        dataSet.setValueTextColor(Color.WHITE);
                        dataSet.setValueTextSize(14f);
                        dataSet.setValueFormatter(new IValueFormatter() {
                            @Override
                            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                                return String.format("%.0f%%      ", value);
                            }
                        });
                        Legend legend = barChart.getLegend();
                        legend.setTextColor(textColor);
                        barChart.animateY(800, Easing.EasingOption.EaseInOutQuart);
                        barChart.invalidate();
                    });
                });
            });
        });


    }

    //..........Mindset Pie Chart..........
    void mindsetPieChart() {
        PieChart halfPieChart = binding.pieChartMindset;

        dashboardViewModel.getMindsetCalmCount("Calm").observe(getViewLifecycleOwner(), calm -> {
            dashboardViewModel.getMindsetCalmCount("Emotional").observe(getViewLifecycleOwner(), emotional -> {
                ArrayList<PieEntry> entries = new ArrayList<>();
                int calmMindsetCount = calm;
                int emotionalMindsetCount = emotional;
                float calmPercentage = ((float) calmMindsetCount / (calmMindsetCount + emotionalMindsetCount)) * 100;
                float emotionalPercentage = 100 - calmPercentage;
                if (calmMindsetCount > 0 || emotionalMindsetCount > 0) {
                    entries.add(new PieEntry(calmPercentage, "Calm"));
                    entries.add(new PieEntry(emotionalPercentage, "Emotional"));
                }

                PieDataSet dataSet = new PieDataSet(entries, "Emotions");
                dataSet.setColors(ColorTemplate.rgb("FF4CAF50"), ColorTemplate.rgb("FFB71C1C"));
                dataSet.setValueTextColor(Color.WHITE); // Set value text color to white
                dataSet.setValueTextSize(12f); // Set value text size

                PieData pieData = new PieData(dataSet);
                dataSet.setValueTextColor(Color.WHITE);
                dataSet.setValueTextSize(14f);
                dataSet.setValueFormatter(new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        return String.format("%.0f%%", value);
                    }
                });

                halfPieChart.setData(pieData);
                halfPieChart.getDescription().setEnabled(false);
                halfPieChart.setRotationEnabled(true);
                halfPieChart.setCenterText("Emotions");
                halfPieChart.animateXY(800, 800);

                // Setup Legend
                Legend legend = halfPieChart.getLegend();
                legend.setTextColor(textColor);
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                legend.setOrientation(Legend.LegendOrientation.VERTICAL);
                legend.setDrawInside(false);
                legend.setXEntrySpace(2f);
                legend.setYEntrySpace(0f);
                legend.setYOffset(10f);

                halfPieChart.invalidate();
            });
        });
    }

    //...........sessoion profit horizontal bar chart...........
    private void sessionProfitsChart() {
        Spinner pairSpinner = binding.pairDropDown;
        final String[] pair = new String[]{"NAS", "DAX", "US30"};
        HorizontalBarChart barChart = binding.sessionPerformanceChart;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, pair);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pairSpinner.setAdapter(adapter);
        pairSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPair = parent.getItemAtPosition(position).toString();
                List<BarEntry> entries = new ArrayList<>();
                final String[] entityNames = new String[]{"London", "New York", "Mixed"};

                final String[] xLabels = new String[3];
                int index = -1;
                for (int j = 0; j < entityNames.length; j++) {
                    double profits = dashboardViewModel.getSessionWinningProfits(entityNames[j].substring(0, 1), "W", selectedPair);
                    double loss = dashboardViewModel.getSessionWinningProfits(entityNames[j].substring(0, 1), "L", selectedPair);

                    if (profits > 0) { // Only create chart if there is data
                        entries.add(new BarEntry(j, (float) (profits + loss)));
                        xLabels[++index] = selectedPair + " " + entityNames[j];
                    }
                }


                if (!entries.isEmpty()) {
                    BarDataSet dataSet = new BarDataSet(entries, "Session Profits");
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    BarData barData = new BarData(dataSet);
                    barChart.setData(barData);
                    barChart.getXAxis().setGranularity(1f);
                    barChart.setDrawValueAboveBar(false);
                    barChart.getDescription().setEnabled(false);
                    barChart.getLegend().setEnabled(true);
                    dataSet.setDrawValues(true);
                    dataSet.setValueTextColor(textColor);
                    dataSet.setValueTextSize(14f);
                    barChart.getAxisLeft().setEnabled(true);
                    barChart.getAxisLeft().setTextColor(textColor);
                    barChart.getXAxis().setEnabled(true);
                    barChart.getXAxis().setTextSize(12f);
                    barChart.getXAxis().setTextColor(textColor);
                    barChart.getAxisRight().setTextColor(textColor);
                    barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabels));
                    Legend legend = barChart.getLegend();
                    legend.setTextColor(textColor);
                    barChart.animateY(800, Easing.EasingOption.EaseInOutQuart);
                    barChart.invalidate();
                } else {
                    barChart.clear();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    //..........Display values Closed Early, Decipline and Win/Loss Streak.
    private void tpClosedDecipline() {
        TextView closedEarlyPercent = binding.closeEarlytxt;
        TextView desciplneTxt = binding.deciplineRatingtxt;
        TextView winStreakMax = binding.winStreaktxt;
        TextView lossStreakMax = binding.losingStreaktxt;
        dashboardViewModel.getTpCount("Closed early").observe(getViewLifecycleOwner(), closeEarly -> {
            dashboardViewModel.getTpCount("Hit TP").observe(getViewLifecycleOwner(), hit -> {
                int closedEarlyTp = closeEarly;
                int hitTp = hit;

                float closedPercent = (float) (closedEarlyTp / (float) (closedEarlyTp + hitTp)) * 100;
                closedEarlyPercent.setText(String.format("%.2f%%", closedPercent));

                //...........Descipline..........
                float deciplinePercent = (float) (noneErrorCount) / (float) (noneErrorCount + badEntryErrorCount + noSneakerErrorCount + revengeErrorCount + overratedErrorCount);
                desciplneTxt.setText(String.format("%.0f%%", deciplinePercent * 100));
                lossStreakMax.setText(maxlConsistency + "");
                winStreakMax.setText(dashboardViewModel.getMaxWConsistency() + "");
            });
        });

    }

    //.............London NAS weely chart............
    private void weeklyLondonNasChart() {
        BarChart barChart = binding.barChartNASLondon;
        final String[] dayNames = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

        List<BarEntry> entriesLondon = new ArrayList<>();

        for (int i = 0; i < dayNames.length; i++) {
            //...........................London............................
            double win = dashboardViewModel.getNASWeekDayWinLoss("L", "W", dayNames[i]);
            double loss = dashboardViewModel.getNASWeekDayWinLoss("L", "L", dayNames[i]);
            double winpercent = (win / (win + loss)) * 100;
            Log.d("win and loss ", win + "" + loss + dayNames[i]);
            if (win > 0 || loss >= 0) { // Only create chart if there is data
                entriesLondon.add(new BarEntry(i, (float) (winpercent), dayNames[i]));
            }
        }
        if (!entriesLondon.isEmpty()) {
            BarDataSet dataSet = new BarDataSet(entriesLondon, "NAS London");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            BarData barData = new BarData(dataSet);
            barChart.setData(barData);
            barChart.getXAxis().setGranularity(1f);

            barChart.setDrawValueAboveBar(false);
            barChart.getDescription().setEnabled(false);
            barChart.getLegend().setEnabled(true);

            barChart.getAxisLeft().setEnabled(true);
            barChart.getAxisLeft().setTextColor(textColor);

            barChart.getAxisRight().setEnabled(false);

            barChart.getXAxis().setEnabled(true);
            barChart.getXAxis().setTextSize(12f);
            barChart.getXAxis().setTextColor(textColor);
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dayNames));
            dataSet.setDrawValues(true);
            dataSet.setValueTextColor(Color.WHITE);
            dataSet.setValueTextSize(14f);
            Legend legend = barChart.getLegend();
            legend.setTextColor(textColor);
            barChart.animateY(800, Easing.EasingOption.EaseInOutQuart);
            barChart.invalidate();
        } else {
            barChart.clear();
        }

    }

    //.............NAS Mixed weely chart............
    private void weeklyMixedNasChart() {
        BarChart barChart = binding.barChartNASMixed;
        final String[] dayNames = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < dayNames.length; i++) {
            double win = dashboardViewModel.getNASWeekDayWinLoss("M", "W", dayNames[i]);
            double loss = dashboardViewModel.getNASWeekDayWinLoss("M", "L", dayNames[i]);
            double winpercent = (win / (win + loss)) * 100;
            if (win > 0 || loss >= 0) { // Only create chart if there is data
                entries.add(new BarEntry(i, (float) (winpercent), dayNames[i]));
            }
        }
        if (!entries.isEmpty()) {
            BarDataSet dataSet = new BarDataSet(entries, "NAS Mixed");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            BarData barData = new BarData(dataSet);
            barChart.setData(barData);
            barChart.getXAxis().setGranularity(1f);

            barChart.setDrawValueAboveBar(false);
            barChart.getDescription().setEnabled(false);
            barChart.getLegend().setEnabled(true);

            barChart.getAxisLeft().setEnabled(true);
            barChart.getAxisLeft().setTextColor(textColor);

            barChart.getAxisRight().setEnabled(false);

            barChart.getXAxis().setEnabled(true);
            barChart.getXAxis().setTextSize(12f);
            barChart.getXAxis().setTextColor(textColor);
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dayNames));
            dataSet.setDrawValues(true);
            dataSet.setValueTextColor(Color.WHITE);
            dataSet.setValueTextSize(14f);
            Legend legend = barChart.getLegend();
            legend.setTextColor(textColor);
            barChart.animateY(800, Easing.EasingOption.EaseInOutQuart);
            barChart.invalidate();
        } else {
            barChart.clear();
        }

    }

    //.............NAS NY weely chart............
    private void weeklyNYNasChart() {
        BarChart barChart = binding.barChartNASNewYork;
        final String[] dayNames = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < dayNames.length; i++) {
            double win = dashboardViewModel.getNASWeekDayWinLoss("N", "W", dayNames[i]);
            double loss = dashboardViewModel.getNASWeekDayWinLoss("N", "L", dayNames[i]);
            double winpercent = (win / (win + loss)) * 100;
            if (win > 0 || loss >= 0) { // Only create chart if there is data
                entries.add(new BarEntry(i, (float) (winpercent), dayNames[i]));
            }
        }
        if (!entries.isEmpty()) {
            BarDataSet dataSet = new BarDataSet(entries, "NAS New York");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            BarData barData = new BarData(dataSet);
            barChart.setData(barData);
            barChart.getXAxis().setGranularity(1f);

            barChart.setDrawValueAboveBar(false);
            barChart.getDescription().setEnabled(false);
            barChart.getLegend().setEnabled(true);

            barChart.getAxisLeft().setEnabled(true);
            barChart.getAxisLeft().setTextColor(textColor);

            barChart.getAxisRight().setEnabled(false);

            barChart.getXAxis().setEnabled(true);
            barChart.getXAxis().setTextSize(12f);
            barChart.getXAxis().setTextColor(textColor);
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dayNames));
            dataSet.setDrawValues(true);
            dataSet.setValueTextColor(Color.WHITE);
            dataSet.setValueTextSize(14f);
            Legend legend = barChart.getLegend();
            legend.setTextColor(textColor);
            barChart.animateY(800, Easing.EasingOption.EaseInOutQuart);
            barChart.invalidate();
        } else {
            barChart.clear();
        }

    }

    //.....day win loss count +ve -ve bar chart
    private void dayWinLossPosNegChart() {
        BarChart barChart = binding.barChartDayWinsLosses;
        barChart.getAxisRight().setTextColor(textColor);
        final String[] dayNames = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        List<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < dayNames.length; i++) {
            double win = dashboardViewModel.getWeekDayWinLossCount("W", dayNames[i]);
            double loss = dashboardViewModel.getWeekDayWinLossCount("L", dayNames[i]);

            if (win > 0 || loss >= 0) { // Only create chart if there is data
                float winValue = (float) win;
                float lossValue = (float) -loss; // Inverse loss value for negative side

                entries.add(new BarEntry(i, new float[]{winValue, lossValue}));
            }
        }

        if (!entries.isEmpty()) {
            BarDataSet dataSet = new BarDataSet(entries, "Days Statistics");
            dataSet.setColors(ColorTemplate.rgb("FF4CAF50"), ColorTemplate.rgb("FFB71C1C"));
            dataSet.setStackLabels(new String[]{"Wins", "Losses"});
            BarData barData = new BarData(dataSet);
            barData.setValueTextColor(textColor);
            barChart.setData(barData);
            barChart.getDescription().setEnabled(false);

            // Customize X-axis labels
            barChart.getXAxis().setEnabled(true);
            barChart.getXAxis().setTextSize(8f);
            barChart.getXAxis().setTextColor(textColor);
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dayNames));
            // Customize data labels
            dataSet.setValueTextSize(12f);
            Legend legend = barChart.getLegend();
            legend.setTextColor(textColor);
            barChart.invalidate();
        } else {
            barChart.clear();
        }
    }

    //..........Sl win rate.......................\
    private void slWinRateChart() {
        int start = 10, end = 14;
        final String[] xLabels = new String[10];
        BarChart barChart = binding.barSLWinRate;
        List<BarEntry> entries = new ArrayList<>();
        int i = 0;
        while (end <= 59) {
            int winCount = dashboardViewModel.getSLWinLossCount("W", start, end);
            int lossCount = dashboardViewModel.getSLWinLossCount("L", start, end);

            if (winCount + lossCount > 0) { // Only create chart if there is data
                double winPercentage = (double) winCount / (winCount + lossCount) * 100;
                double lossPercentage = 100 - winPercentage;
                entries.add(new BarEntry(i, new float[]{(float) winPercentage, (float) lossPercentage}));
                xLabels[i] = start + "-" + end;
                i++;
            }
            start = end + 1;
            end += 5;
        }


        final int[] entityColors = new int[]{Color.rgb(76, 175, 80), Color.rgb(183, 28, 28)};
        if (!entries.isEmpty()) { // Only create chart if there are entries
            BarDataSet dataSet = new BarDataSet(entries, "SL win Rate");
            dataSet.setColors(entityColors);
            dataSet.setStackLabels(new String[]{"Win", "Loss"});

            BarData barData = new BarData(dataSet);
            barChart.setData(barData);

            // Customize appearance and labels
            barChart.getDescription().setEnabled(false);
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            barChart.getXAxis().setGranularity(1f);
            barChart.getXAxis().setTextColor(textColor);
            // Customize Y-axis appearance
            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setAxisMinimum(0f);
            leftAxis.setTextColor(textColor);
            barChart.getAxisRight().setTextColor(textColor);
            // Customize data label appearance
            dataSet.setValueTextColor(Color.WHITE);
            dataSet.setValueTextSize(14f);
            // Set custom value formatter for data labels
            dataSet.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return String.format("%.0f%%", value);// Add percentage sign to the value
                }
            });// Set custom X-axis labels with pair and entity names
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabels));
            barChart.getXAxis().setLabelCount(i); // Set the correct label count based on the number of entries
            barChart.getXAxis().setLabelRotationAngle(-45); // Rotate labels for better readability
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            Legend legend = barChart.getLegend();
            legend.setTextColor(textColor);
            barChart.getXAxis().setGranularity(1f);
            barChart.invalidate();
        } else {
            barChart.clear(); // Clear the chart if there are no entries
        }
    }

    //..........Sl Profit Loss.......................\
    private void slProfitLossBarChart() {
        int start = 10, end = 14;
        final String[] xLabels = new String[10];
        HorizontalBarChart barChart = binding.barChartSLProfit;
        List<BarEntry> entries = new ArrayList<>();
        int i = 0;
        while (end <= 59) {
            double slProfit = dashboardViewModel.getSLProfit(start, end);
            entries.add(new BarEntry(i, (float) slProfit));
            xLabels[i] = start + "-" + end;
            i++;
            start = end + 1;
            end += 5;
        }

        if (!entries.isEmpty()) {
            BarDataSet dataSet = new BarDataSet(entries, "SL Profit/Loss");

            // Determine colors for positive and negative values
            List<Integer> colors = new ArrayList<>();
            for (BarEntry entry : entries) {
                colors.add(entry.getY() >= 0 ? ColorTemplate.rgb("FF4CAF50") : ColorTemplate.rgb("FFB71C1C"));
            }
            dataSet.setColors(colors);

            BarData barData = new BarData(dataSet);
            barChart.setData(barData);

            // Customize appearance and labels
            barChart.getDescription().setEnabled(false);
            barChart.getLegend().setEnabled(true);

            barChart.getAxisLeft().setEnabled(true);
            barChart.getAxisLeft().setTextColor(Color.RED);
            barChart.getXAxis().setEnabled(true);
            barChart.getXAxis().setTextSize(8f);
            barChart.getXAxis().setTextColor(textColor);
            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabels));
            barChart.getXAxis().setLabelRotationAngle(-45);
            barChart.getXAxis().setLabelCount(xLabels.length);

            // Customize data label appearance
            dataSet.setValueTextColor(textColor);
            dataSet.setValueTextSize(10f);

            // Add profit limits scale at bottom
            XAxis xAxis = barChart.getXAxis();
            xAxis.setDrawGridLines(false); // Remove vertical grid lines
            xAxis.setGranularity(1f); // Set label granularity
            xAxis.setLabelCount(xLabels.length); // Set label count
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set label position
            xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
            Legend legend = barChart.getLegend();
            legend.setTextColor(textColor);
            barChart.animateY(800, Easing.EasingOption.EaseInOutQuart);
            barChart.invalidate();
        } else {
            barChart.clear();
        }
    }

    //............Monthly gain report barchart.......
    private void monthlyProfitBarChart() {
        Spinner yearSpinner = binding.yearDropdown;
        BarChart barChartMonthlyGains = binding.monthlyReportChart;
        Legend legend = barChartMonthlyGains.getLegend();
        legend.setTextColor(textColor);
        // Populate yearSpinner with years from your data
        //..........for spinner setting from database......................
        dashboardViewModel.getUniqueYears().observe(getViewLifecycleOwner(), years -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, years);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            yearSpinner.setAdapter(adapter);
        });
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = parent.getItemAtPosition(position).toString();
                dashboardViewModel.setSelectedYear(selectedYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        dashboardViewModel.getMonthlyGains().observe(getViewLifecycleOwner(), monthlyGains -> {
            setupMonthlyGainsChart(monthlyGains, barChartMonthlyGains);
        });
    }


    private void setupMonthlyGainsChart(List<MonthlyGain> monthlyGains, BarChart barChartMonthlyGains) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> months = new ArrayList<>();
        final String[] monthsName = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        int gainIndex = 0;
        for (int i = 0; i < monthsName.length; i++) {
            if (gainIndex < monthlyGains.size()) {
                MonthlyGain gain = monthlyGains.get(gainIndex);
                int x = Integer.parseInt(gain.getMonth());

                if (i + 1 == x) {
                    entries.add(new BarEntry(i, (float) gain.getTotalGain()));
                    months.add(gain.getMonth());
                    gainIndex++;
                } else {
                    entries.add(new BarEntry(i, 0.0F));
                }
            } else {
                entries.add(new BarEntry(i, 0.0F));
            }
        }

        BarDataSet dataSet = new BarDataSet(entries, "Monthly Performance");
        dataSet.setColors(Color.rgb(76, 175, 80)); // Set the color for positive gains
        BarData barData = new BarData(dataSet);
        barChartMonthlyGains.setData(barData);
        barData.setValueTextColor(textColor);
        XAxis xAxis = barChartMonthlyGains.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(monthsName));
        xAxis.setTextColor(textColor);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10);
        barData.setValueTextSize(10);
        barChartMonthlyGains.getDescription().setEnabled(false);
        barChartMonthlyGains.invalidate();

        RecyclerView recyclerView = binding.recyclerViewMonthlyProfit;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MonthlyGainsAdapter adapter = new MonthlyGainsAdapter(monthlyGains); // Pass your data here
        recyclerView.setAdapter(adapter);
    }


}
