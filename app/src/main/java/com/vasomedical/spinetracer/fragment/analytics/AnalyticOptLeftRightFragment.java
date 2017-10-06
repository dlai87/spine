package com.vasomedical.spinetracer.fragment.analytics;

import com.vasomedical.spinetracer.R;

import java.util.ArrayList;

/**
 * Created by dehualai on 9/21/17.
 */


/**
 * 脊柱左右侧弯角 分析界面
 * */
public class AnalyticOptLeftRightFragment extends AnalyticAngleRangeFragment{


    @Override
    protected void initSubclassValues() {
        suggestion = new ArrayList<String>();
        suggestion.add("脊柱左右侧弯角 建议1");
        suggestion.add("脊柱左右侧弯角 建议2");
        suggestion.add("脊柱左右侧弯角 建议3");
        suggestion.add("脊柱左右侧弯角 建议4");
        suggestion.add("脊柱左右侧弯角 建议5");
        suggestionInitFlag = true;

        angleRange1 = 45;
        angleRnage2 = 45;
        label1 = mContext.getResources().getString(R.string.analytic_label_left_right_1);
        label2 = mContext.getResources().getString(R.string.analytic_label_left_right_2);
    }
}
