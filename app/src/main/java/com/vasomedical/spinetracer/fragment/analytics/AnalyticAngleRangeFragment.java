package com.vasomedical.spinetracer.fragment.analytics;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.vasomedical.spinetracer.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by dehualai on 6/27/18.
 */

public abstract class AnalyticAngleRangeFragment extends AnalyticBaseFragment {

    private HorizontalBarChart mChart;

    TextView labelTextView1;
    TextView labelTextView2;


    protected int angleRange1;
    protected int angleRnage2;
    protected String label1;
    protected String label2;
    float minAngle = Float.MAX_VALUE;
    float maxAngle = Float.MIN_VALUE;

    protected abstract void preDefineParams();
    protected abstract void initSubclassValues();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        preDefineParams();

        view = inflater.inflate(R.layout.fragment_analytic_opt3, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        assignViews();
        addActionToViews();
        initSubclassValues();

        return view;
    }


    @Override
    protected void assignViews() {

        super.assignViews();

        if (!getMinMaxValueFromDetectionData()){
            // illegal detection data
            invalidDetectionLayout.setVisibility(View.VISIBLE);
            validLayout.setVisibility(View.GONE);
            return;
        }

        labelTextView1 = (TextView) view.findViewById(R.id.label1);
        labelTextView2 = (TextView) view.findViewById(R.id.label2);
        labelTextView1.setText(label1);
        labelTextView2.setText(label2);

        mChart = (HorizontalBarChart) view.findViewById(R.id.chart1);
        mChart.setDrawGridBackground(false);
        mChart.getDescription().setEnabled(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setHighlightFullBarEnabled(false);

        mChart.getAxisLeft().setEnabled(false);

        mChart.getAxisRight().setAxisMaximum(angleRnage2);
        mChart.getAxisRight().setAxisMinimum(-angleRange1);
        mChart.getAxisRight().setDrawGridLines(false);
        mChart.getAxisRight().setDrawZeroLine(true);
        mChart.getAxisRight().setLabelCount(7, false);
        mChart.getAxisRight().setValueFormatter(new CustomFormatter());
        mChart.getAxisRight().setTextSize(9f);
        mChart.getXAxis().setEnabled(false);
        mChart.getLegend().setEnabled(false);


        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        yValues.add(new BarEntry(5, new float[]{ minAngle, maxAngle }));
        BarDataSet set = new BarDataSet(yValues, "");
        set.setValueFormatter(new CustomFormatter());
        set.setValueTextSize(16f);
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set.setColors(new int[] {Color.rgb(67,67,72), Color.rgb(124,181,236)});

        BarData data = new BarData(set);
        data.setBarWidth(8.5f);
        mChart.setData(data);
        mChart.invalidate();
    }



    private boolean getMinMaxValueFromDetectionData(){

        for (Entry entry : detectionData){
            if (entry.getY() < minAngle) minAngle = entry.getY();
            if (entry.getY() > maxAngle) maxAngle = entry.getY();
        }

        if (minAngle > 0) minAngle = 0 ;
        if (maxAngle < 0) maxAngle = 0 ;
        return minAngle <= maxAngle;
    }

    @Override
    protected void addActionToViews() {

        super.addActionToViews();

        if (label1!=null){
  //          textView1.setText(label1);
        }

        if (label2!=null){
  //          textView2.setText(label2);
        }

    }



    private class CustomFormatter implements IValueFormatter, IAxisValueFormatter
    {

        private DecimalFormat mFormat;

        public CustomFormatter() {
            mFormat = new DecimalFormat("###");
        }

        // data
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(Math.abs(value)) + mContext.getResources().getString(R.string.degree_mark);
        }

        // YAxis
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(Math.abs(value)) + mContext.getResources().getString(R.string.degree_mark);
        }
    }
}
