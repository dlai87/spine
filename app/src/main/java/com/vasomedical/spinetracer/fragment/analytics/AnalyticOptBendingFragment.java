package com.vasomedical.spinetracer.fragment.analytics;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
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
 * Created by dehualai on 9/20/17.
 */

public class AnalyticOptBendingFragment extends AnalyticAngleRangeFragment {


    @Override
    protected void initSubclassValues() {
        suggestion = new ArrayList<String>();
        suggestion.add("11111");
        suggestion.add("22222");
        suggestion.add("33333");
        suggestion.add("44444");
        suggestion.add("Suggestion 5");
        suggestion.add("Suggestion 6");
        suggestion.add("Suggestion 7");
        suggestion.add("Suggestion 8");
        suggestionInitFlag = true;

        angleRange1 = 30;
        angleRnage2 = 45;
    }
}
