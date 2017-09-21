package com.vasomedical.spinetracer.fragment.analytics;

import com.vasomedical.spinetracer.R;

import java.util.ArrayList;

/**
 * Created by dehualai on 9/21/17.
 */


/**
 * 旋转角 分析界面
 * */
public class AnalyticOptRotateFragment extends AnalyticAngleRangeFragment{


    @Override
    protected void initSubclassValues() {
        suggestion = new ArrayList<String>();
        suggestion.add("旋转角 建议1");
        suggestion.add("旋转角 建议2");
        suggestion.add("旋转角 建议3");
        suggestion.add("旋转角 建议4");
        suggestion.add("旋转角 建议5");
        suggestionInitFlag = true;

        angleRange1 = 60;
        angleRnage2 = 60;
        label1 = mContext.getResources().getString(R.string.analytic_label_rotate_1);
        label2 = mContext.getResources().getString(R.string.analytic_label_rotate_2);
    }
}