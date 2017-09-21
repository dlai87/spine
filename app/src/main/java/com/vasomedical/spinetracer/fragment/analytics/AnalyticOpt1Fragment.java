package com.vasomedical.spinetracer.fragment.analytics;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.vasomedical.spinetracer.R;

import java.util.ArrayList;

/**
 * Created by dehualai on 9/17/17.
 */

public class AnalyticOpt1Fragment extends AnalyticBaseFragment{

    private LineChart mChart;

    //Button saveButton;

    float yAxisRange = 10f;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        suggestion = new ArrayList<String>();
        suggestion.add("Suggestion 1");
        suggestion.add("Placeholder 2");
        suggestion.add("some text 3");
        suggestion.add("Suggestion 4");
        suggestion.add("Suggestion 5");
        suggestion.add("Suggestion 6");
        suggestion.add("Suggestion 7");
        suggestion.add("Suggestion 8");
        suggestionInitFlag = true;

        view = inflater.inflate(R.layout.fragment_analytic_opt1, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        assignViews();
        addActionToViews();

        return view;
    }


    @Override
    protected void assignViews() {

        super.assignViews();

        mChart = (LineChart) view.findViewById(R.id.chart1);

            for (Entry e : detectionData){
                float range = Math.abs(e.getY());
                if (range >= yAxisRange){
                    yAxisRange = range + 2;
                }
            }


        mChart.setDrawGridBackground(false);
        mChart.getDescription().setEnabled(false);
        mChart.setTouchEnabled(false);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setPinchZoom(false);
        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(8f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(15f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMaximum(yAxisRange);
        leftAxis.setAxisMinimum(-yAxisRange);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(true);
        mChart.getAxisRight().setEnabled(false);

        setData(detectionData);

        // add data
        mChart.animateX(2500);
        mChart.getLegend().setEnabled(false);

    }

    @Override
    protected void addActionToViews() {

        super.addActionToViews();

    }



    private void setData(ArrayList<Entry> inputData) {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        if (inputData!=null){
            if (inputData.size() <= 0){
                // illegal detection data
                return;
            }
            yVals1 = inputData;
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();

        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, null);

            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setCircleColor(Color.TRANSPARENT);
            set1.setLineWidth(20f);
            set1.setCircleRadius(0f);
            set1.setFillAlpha(0);
            set1.setDrawCircleHole(false);
            set1.setDrawFilled(true);
            //set1.setFormLineDashEffect(new DashPathEffect(new float[]{20f, 20f}, 0f));
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setValueTextColor(Color.TRANSPARENT);

            // create a data object with the datasets
            LineData data = new LineData(set1);

            // set data
            mChart.setData(data);


        }
    }





}
