package com.vasomedical.spinetracer.fragment.analytics;

import java.util.ArrayList;

/**
 * Created by dehualai on 6/27/18.
 */

public class AnalyticOptBendingFragment  extends AnalyticAngleRangeFragment {


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
