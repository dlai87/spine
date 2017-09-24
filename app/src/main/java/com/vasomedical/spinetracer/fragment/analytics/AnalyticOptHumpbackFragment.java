package com.vasomedical.spinetracer.fragment.analytics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.util.FloatProperty;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.vasomedical.spinetracer.R;

import java.util.ArrayList;

/**
 * Created by dehualai on 9/17/17.
 */

public class AnalyticOptHumpbackFragment extends AnalyticPositionPositionFragment {

    @Override
    protected void initSubclassValues() {
        suggestion = new ArrayList<String>();
        suggestion.add("驼背角 建议1");
        suggestion.add("驼背角 建议2");
        suggestion.add("驼背角 建议3");
        suggestion.add("驼背角 建议4");
        suggestion.add("驼背角 建议5");
        suggestionInitFlag = true;

    }



}
