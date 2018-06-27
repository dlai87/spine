package com.vasomedical.spinetracer.fragment.analytics;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.database.util.DBAdapter;

import java.util.ArrayList;

/**
 * Created by dehualai on 6/27/18.
 */

public abstract class AnalyticPositionAngleFragment extends AnalyticBaseFragment{


    private BubbleChart mBubbleChartFilter;


    float yAxisRange = 10f;

    protected abstract void initSubclassValues();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initSubclassValues();

        view = inflater.inflate(R.layout.fragment_analytic_opt1, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        assignViews();
        addActionToViews();

        return view;
    }


    @Override
    protected void assignViews() {

        super.assignViews();

        for (Entry e : detectionData){
            float range = Math.abs(e.getY());
            if (range >= yAxisRange){
                yAxisRange = range + 2;
            }
        }

        mBubbleChartFilter = (BubbleChart) view.findViewById(R.id.bubbleChartFilter);
        mBubbleChartFilter.getDescription().setEnabled(false);
        mBubbleChartFilter.setDrawGridBackground(false);
        mBubbleChartFilter.setTouchEnabled(false);
        mBubbleChartFilter.setDragEnabled(false);
        mBubbleChartFilter.setScaleEnabled(true);
        mBubbleChartFilter.setMaxVisibleValueCount(200);
        mBubbleChartFilter.setPinchZoom(true);

        setBubbleChartFilter(filterData);

        // fixme : todo  save data to DB

    }

    @Override
    protected void addActionToViews() {

        super.addActionToViews();

    }



    private void setBubbleChartFilter(ArrayList<Entry> inputData){
        ArrayList<BubbleEntry> yVals1 = new ArrayList<BubbleEntry>();

        float bubbleSize = 0.01f ;
        for(int i = 0 ; i < inputData.size(); i++){
            Entry entry = inputData.get(i);
            yVals1.add(new BubbleEntry(entry.getX()*100, entry.getY(), bubbleSize));
        }

        // create a dataset and give it a type
        BubbleDataSet set1 = new BubbleDataSet(yVals1, "");
        set1.setColor(ColorTemplate.COLORFUL_COLORS[0], 0);
        set1.setDrawValues(true);


        ArrayList<IBubbleDataSet> dataSets = new ArrayList<IBubbleDataSet>();
        dataSets.add(set1); // add the datasets
        // dataSets.add(set3);

        // create a data object with the datasets
        BubbleData data = new BubbleData(dataSets);
        data.setDrawValues(false);
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.BLACK);
        data.setHighlightCircleWidth(1.5f);

        mBubbleChartFilter.setData(data);
        mBubbleChartFilter.invalidate();
    }





}
