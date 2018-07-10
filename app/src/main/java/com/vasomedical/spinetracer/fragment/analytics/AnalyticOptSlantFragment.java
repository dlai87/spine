package com.vasomedical.spinetracer.fragment.analytics;

import com.github.mikephil.charting.data.Entry;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.util.Util;

import java.util.ArrayList;

/**
 * Created by dehualai on 6/27/18.
 */

public class AnalyticOptSlantFragment extends AnalyticPositionAngleFragment{


    // 躯干倾斜角

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
                "医生分析 1 -- 1",
                "医生分析 1 -- 2",
                "医生分析 1 -- 3",
        };
    }

}
