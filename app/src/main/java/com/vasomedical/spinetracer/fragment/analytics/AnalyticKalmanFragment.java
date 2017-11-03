package com.vasomedical.spinetracer.fragment.analytics;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.algorithm.kalman.KalmanOperation;
import com.vasomedical.spinetracer.fragment.BaseFragment;
import com.vasomedical.spinetracer.util.widget.button.NJButton;

import java.util.ArrayList;

/**
 * Created by dehualai on 10/30/17.
 */

public class AnalyticKalmanFragment extends AnalyticBaseFragment {

    private BubbleChart mChart;

    ArrayList<Entry> detectionData;

    public void setDetectionData(ArrayList<Entry> data) {this.detectionData = data; }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_analytic_kalman, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        assignViews();
        addActionToViews();
        return view;
    }

    protected void assignViews(){
        mChart = (BubbleChart) view.findViewById(R.id.chart1);
        mChart.getDescription().setEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setMaxVisibleValueCount(200);
        mChart.setPinchZoom(true);
        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        YAxis yl = mChart.getAxisLeft();
        yl.setSpaceTop(30f);
        yl.setSpaceBottom(30f);
        yl.setDrawZeroLine(false);
        mChart.getAxisRight().setEnabled(false);
        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);

    }

    protected void addActionToViews(){
        setData();
    }


    private void setData(){

        KalmanOperation operation = new KalmanOperation();
        ArrayList<Entry> kalmanData = operation.process(detectionData);


        ArrayList<BubbleEntry> yVals1 = new ArrayList<BubbleEntry>();
        ArrayList<BubbleEntry> yVals2 = new ArrayList<BubbleEntry>();

        for(int i = 0 ; i < detectionData.size(); i++){
            Entry entry = detectionData.get(i);
            yVals1.add(new BubbleEntry(entry.getX(), entry.getY(), 1));
        }

        for(int i = 0 ; i < kalmanData.size(); i++){
            Entry entry = kalmanData.get(i);
            yVals2.add(new BubbleEntry(entry.getX(), entry.getY(), 1));
        }
        // create a dataset and give it a type
        BubbleDataSet set1 = new BubbleDataSet(yVals1, "DS 1");
        set1.setColor(ColorTemplate.COLORFUL_COLORS[0], 255);
        set1.setDrawValues(true);
        BubbleDataSet set2 = new BubbleDataSet(yVals2, "DS 2");
        set2.setColor(ColorTemplate.COLORFUL_COLORS[3], 255);
        set2.setDrawValues(true);


        ArrayList<IBubbleDataSet> dataSets = new ArrayList<IBubbleDataSet>();
        dataSets.add(set1); // add the datasets
        dataSets.add(set2);
        // dataSets.add(set3);

        // create a data object with the datasets
        BubbleData data = new BubbleData(dataSets);
        data.setDrawValues(false);
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.BLACK);
        data.setHighlightCircleWidth(1.5f);

        mChart.setData(data);
        mChart.invalidate();
    }
}
