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

public class AnalyticOptSlantFragment extends AnalyticPositionAngleFragment{

    @Override
    protected void initSubclassValues() {
        suggestion = new ArrayList<String>();
        suggestion.add("躯干倾斜角 建议1");
        suggestion.add("躯干倾斜角 建议2");
        suggestion.add("躯干倾斜角 建议3");
        suggestion.add("躯干倾斜角 建议4");
        suggestion.add("躯干倾斜角 建议5");
        suggestion.add("躯干倾斜角 建议6");
        suggestion.add("躯干倾斜角 建议7");
        suggestionInitFlag = true;

        /*
        angleRange1 = 45;
        angleRnage2 = 45;
        label1 = mContext.getResources().getString(R.string.analytic_label_left_right_1);
        label2 = mContext.getResources().getString(R.string.analytic_label_left_right_2);
        */
    }

}
