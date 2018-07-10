package com.vasomedical.spinetracer.fragment.analytics;

import com.github.mikephil.charting.data.Entry;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.util.Util;

import java.util.ArrayList;

/**
 * Created by dehualai on 6/27/18.
 */

public class AnalyticOptRotateFragment extends AnalyticAngleRangeFragment{



    @Override
    protected void preDefineParams(){
        angleRange1 = 60;
        angleRnage2 = 60;
        label1 = mContext.getResources().getString(R.string.analytic_label_rotate_1);
        label2 = mContext.getResources().getString(R.string.analytic_label_rotate_2);
    }


    @Override
    protected void initSubclassValues() {

        float right = 0;
        float left = 0;

        for(Entry entry : filterData ){
            if(entry.getY() > right){
                right = entry.getY();
            }
            if(entry.getY() < left){
                left = entry.getY();
            }
        }

        textView1.setText(Util.degreeToDisplay(Math.abs(right)) + mContext.getResources().getString(R.string.degree_mark));
        textView2.setText(Util.degreeToDisplay(Math.abs(left)) + mContext.getResources().getString(R.string.degree_mark));

    }

    @Override
    protected void defineDoctorComments(){
        doctorComments = new String[]{
                "医生分析 3 -- 1",
                "医生分析 3 -- 2",
                "医生分析  -- 3",
        };
    }
}