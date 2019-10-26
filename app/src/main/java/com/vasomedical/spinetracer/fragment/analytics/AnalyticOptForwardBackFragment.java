package com.vasomedical.spinetracer.fragment.analytics;

import com.github.mikephil.charting.data.Entry;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.util.Util;

import java.util.ArrayList;

/**
 * Created by dehualai on 6/27/18.
 */

public class AnalyticOptForwardBackFragment extends AnalyticAngleRangeFragment{


    //前后倾角


    @Override
    protected void preDefineParams(){
        angleRange1 = 40;
        angleRnage2 = 90;
        label1 = mContext.getResources().getString(R.string.analytic_label_forward_back_1);
        label2 = mContext.getResources().getString(R.string.analytic_label_forward_back_2);
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
                "医生分析 5 -- 1",
                "医生分析 5 -- 2",
                "医生分析 5 -- 3",
        };
    }

}
