package com.vasomedical.spinetracer.fragment.analytics;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.renderer.SpineShapeChartRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.model.Pose;
import com.vasomedical.spinetracer.util.Global;
import com.vasomedical.spinetracer.util.Util;

import java.util.ArrayList;

/**
 * Created by dehualai on 6/24/18.
 */

public class AnalyticOptSegment extends AnalyticBaseFragment{

    protected BubbleChart mBubbleChartSpineSegment;
    private TextView cobbsAngleText;




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_analytic_segment, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        assignViews();
        addActionToViews();

        return view;
    }


    @Override
    protected void assignViews() {
        super.assignViews();

        cobbsAngleText = (TextView)view.findViewById(R.id.cobbs_angle_text_view);
        mBubbleChartSpineSegment = (BubbleChart) view.findViewById(R.id.bubbleChartSpineSegment);
        mBubbleChartSpineSegment.getDescription().setEnabled(false);
        mBubbleChartSpineSegment.setDrawGridBackground(false);
        mBubbleChartSpineSegment.setTouchEnabled(true);
        mBubbleChartSpineSegment.setDragEnabled(false);
        mBubbleChartSpineSegment.setScaleEnabled(false);
        mBubbleChartSpineSegment.setMaxVisibleValueCount(200);
        mBubbleChartSpineSegment.setPinchZoom(false);
        mBubbleChartSpineSegment.setChartCallback(new SpineShapeChartRenderer.IChartCallback() {
            @Override
            public void onCobbsAngleCalculate(final float angle) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // fixme :  update cobb's angle in real time
                        cobbsAngleText.setText(angle + mContext.getResources().getString(R.string.degree_mark));
                    }
                });
            }
        });


        setmBubbleChartSpineSegment(detectionData);


    }


    @Override
    protected void saveChart(){
        String saveFilename = "chart" + System.currentTimeMillis();
        chartSavePath = Global.FOLDER_CHART + saveFilename + ".png";
        mBubbleChartSpineSegment.saveToPath(saveFilename, Global.FOLDER_CHART  );
    }

    private void setmBubbleChartSpineSegment(ArrayList<Entry> inputData){
        ArrayList<BubbleEntry> yVals1 = new ArrayList<BubbleEntry>();

        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;
        for(int i = 0 ; i < inputData.size(); i++){
            Entry entry = inputData.get(i);
            if (entry.getY()  < minY)
                minY = entry.getY();
            if (entry.getY() > minY)
                minY = entry.getY();
        }

        float bubbleSize = (maxY - minY)/ 120000 ;
        for(int i = 0 ; i < inputData.size(); i++){
            Entry entry = inputData.get(i);
            yVals1.add(new BubbleEntry( entry.getY()*100, entry.getX()*100, bubbleSize));
        }


        // create a dataset and give it a type
        BubbleDataSet set1 = new BubbleDataSet(yVals1, "");
        set1.setColor(ColorTemplate.COLORFUL_COLORS[0], 255);
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

        mBubbleChartSpineSegment.setData(data);
        mBubbleChartSpineSegment.invalidate();
    }



    @Override
    protected void defineDoctorComments(){
        doctorComments = new String[]{
                "医生分析 2 -- 1",
                "医生分析 2 -- 2",
                "医生分析 2 -- 3",
        };
    }
}
