package com.vasomedical.spinetracer.fragment.analytics;

import com.vasomedical.spinetracer.R;

import java.util.ArrayList;

/**
 * Created by dehualai on 6/27/18.
 */

public class AnalyticOptForwardBackFragment extends AnalyticAngleRangeFragment{


    @Override
    protected void initSubclassValues() {
        suggestion = new ArrayList<String>();
        suggestion.add("前倾 后仰角 建议1");
        suggestion.add("前倾 后仰角 建议2");
        suggestion.add("前倾 后仰角 建议3");
        suggestion.add("前倾 后仰角 建议4");
        suggestion.add("前倾 后仰角 建议5");
        suggestionInitFlag = true;

        angleRange1 = 40;
        angleRnage2 = 90;
        label1 = mContext.getResources().getString(R.string.analytic_label_forward_back_1);
        label2 = mContext.getResources().getString(R.string.analytic_label_forward_back_2);
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
