package com.vasomedical.spinetracer.fragment.analytics;

import com.vasomedical.spinetracer.R;

import java.util.ArrayList;

/**
 * Created by dehualai on 9/21/17.
 */

public class AnalyticOptBalanceFragment extends AnalyticAngleRangeFragment{


    @Override
    protected void initSubclassValues() {
        suggestion = new ArrayList<String>();
        suggestion.add("身体平衡度 建议1");
        suggestion.add("身体平衡度 建议2");
        suggestion.add("身体平衡度 建议3");
        suggestion.add("身体平衡度 建议4");
        suggestion.add("身体平衡度 建议5");
        suggestionInitFlag = true;

        angleRange1 = 30;
        angleRnage2 = 30;
        label1 = mContext.getResources().getString(R.string.analytic_label_balance_1);
        label2 = mContext.getResources().getString(R.string.analytic_label_balance_2);
    }
}
