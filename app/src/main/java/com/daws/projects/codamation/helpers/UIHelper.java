package com.daws.projects.codamation.helpers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.daws.projects.codamation.R;
import com.daws.projects.codamation.databinding.CustomToastBinding;
import com.daws.projects.codamation.utils.components.LineChartMarkerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

public class UIHelper {
    private Context context;

    private UIHelper(){}

    public static UIHelper newInstance(Context context){
        UIHelper uiHelper = new UIHelper();
        uiHelper.setContext(context);
        return uiHelper;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void showToast(String message, int icon, int color){
        CustomToastBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.custom_toast,
                null,
                false
        );

        binding.setIcon(icon);
        binding.setInfo(message);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.layout.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), color));
        }

        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(binding.getRoot());
        toast.setGravity(Gravity.FILL_HORIZONTAL|Gravity.BOTTOM, 0,0);

        toast.show();
    }

    public void showSuccessToast(String message){
        showToast(message, R.drawable.ic_check_circle_white, R.color.colorPrimary);
    }

    public void showErrorToast(String message){
        showToast(message, R.drawable.ic_cancel_white, R.color.softRed);
    }
    
    public void setupLineChart(LineChart lineChart){

        IMarker marker = new LineChartMarkerView(getContext(), R.layout.custom_line_chart_marker);

        // setup line chart interface
        lineChart.setBackgroundColor(Color.TRANSPARENT);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.setMarker(marker);
        lineChart.setViewPortOffsets(0f, 0f, 0f, 0f);

        lineChart.getXAxis().setEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter());
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.animateX(1500);

        // set chart legend
        Legend dataLegend = lineChart.getLegend();
        dataLegend.setForm(Legend.LegendForm.LINE);
        dataLegend.setTextColor(ContextCompat.getColor(getContext(), R.color.textGrey));
    }

    public void setMultipleLineChartData(ArrayList<ArrayList<Entry>> dataEntries, LineChart lineChart){

        ArrayList<ILineDataSet> dataSetLine = new ArrayList<>();

        int i = 0;
        int color;
        for (ArrayList data : dataEntries){
            ArrayList<Entry> dataEntry = data;

            if (i == 0) color = R.color.pending;
            else if (i == 1) color = R.color.colorPrimary;
            else if (i == 2) color = R.color.error;
            else color = R.color.softGrey;

            dataSetLine.add(setLineDataSetNoBackground(dataEntry, color, lineChart));
            i++;
        }

        LineData lineData = new LineData(dataSetLine);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    public void setLineChartData(ArrayList<Entry> dataEntries, LineChart lineChart) {
        setLineChartData(dataEntries, lineChart, ContextCompat.getDrawable(getContext(), R.drawable.gradientbackground));
    }

    public void setLineChartData(ArrayList<Entry> dataEntries, LineChart lineChart, Drawable customBackground){
        LineDataSet datasetCase;

        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            datasetCase = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            datasetCase.setValues(dataEntries);
            datasetCase.notifyDataSetChanged();
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        }
        else {
            datasetCase = setLineDataSet(dataEntries,"Total Kasus Covid-19", customBackground, lineChart);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(datasetCase);

            LineData data = new LineData(dataSets);
            lineChart.setData(data);
        }

        lineChart.invalidate();
    }

    private LineDataSet setLineDataSetNoBackground(ArrayList<Entry> dataEntries, int lineColor, LineChart lineChart){
        LineDataSet datasetCase;

        datasetCase = new LineDataSet(dataEntries, "");
        datasetCase.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        datasetCase.setDrawIcons(false);
        datasetCase.setColor(ContextCompat.getColor(getContext(), lineColor));
        datasetCase.setLineWidth(3f);
        datasetCase.setDrawCircles(false);
        datasetCase.setDrawCircleHole(false);
        datasetCase.setDrawValues(false);
        datasetCase.setDrawHorizontalHighlightIndicator(false);
        datasetCase.setDrawVerticalHighlightIndicator(false);
        datasetCase.setDrawFilled(true);
        datasetCase.setFillFormatter((dataSet, dataProvider) -> lineChart.getAxisLeft().getAxisMinimum());
        datasetCase.setFillColor(ContextCompat.getColor(getContext(), R.color.defaultBackground));

        return datasetCase;
    }

    private LineDataSet setLineDataSet(ArrayList<Entry> dataEntries, String label, Drawable customBackground, LineChart lineChart){

        LineDataSet datasetCase;

        datasetCase = new LineDataSet(dataEntries, label);
        datasetCase.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        datasetCase.setDrawIcons(false);
        datasetCase.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        datasetCase.setLineWidth(3f);
        datasetCase.setDrawCircles(false);
        datasetCase.setDrawCircleHole(false);
        datasetCase.setDrawValues(false);
        datasetCase.setDrawHorizontalHighlightIndicator(false);
        datasetCase.setDrawVerticalHighlightIndicator(false);
        datasetCase.setDrawFilled(true);
        datasetCase.setFillFormatter((dataSet, dataProvider) -> lineChart.getAxisLeft().getAxisMinimum());

        // set color of filled area
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            Drawable drawable = customBackground;
            datasetCase.setFillDrawable(drawable);
        } else {
            datasetCase.setFillColor(ContextCompat.getColor(getContext(), R.color.defaultBackground));
        }

        return datasetCase;
    }
}
