package com.vasomedical.spinetracer.fragment.analytics.diagnosis;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;


import com.vasomedical.spinetracer.R;

import java.util.ArrayList;

/**
 * Created by dehualai on 10/25/16.
 */

public class MultiChoiceCell extends BaseCell {


    MultiChoiceUnit option1;
    MultiChoiceUnit option2;
    MultiChoiceUnit option3;
    MultiChoiceUnit option4;
    MultiChoiceUnit option5;
    MultiChoiceUnit option6;
    MultiChoiceUnit option7;
    ArrayList<MultiChoiceUnit> units ;


    public MultiChoiceCell(Context context) {
        super(context);
    }

    public MultiChoiceCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiChoiceCell(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    protected void initViews(Context context, AttributeSet attrs) {

        super.initViews(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.list_cell_multi_choice, this, true);

        assignViews();
        addActionToViews();

    }


    private void assignViews() {
        questionTextView = (TextView) this.findViewById(R.id.question);

        option1 = (MultiChoiceUnit) this.findViewById(R.id.option1);
        option2 = (MultiChoiceUnit) this.findViewById(R.id.option2);
        option3 = (MultiChoiceUnit) this.findViewById(R.id.option3);
        option4 = (MultiChoiceUnit) this.findViewById(R.id.option4);
        option5 = (MultiChoiceUnit) this.findViewById(R.id.option5);
        option6 = (MultiChoiceUnit) this.findViewById(R.id.option6);
        option7 = (MultiChoiceUnit) this.findViewById(R.id.option7);

        units = new ArrayList<MultiChoiceUnit>();
        units.add(option1);
        units.add(option2);
        units.add(option3);
        units.add(option4);
        units.add(option5);
        units.add(option6);
        units.add(option7);

    }

    private void addActionToViews() {
        questionTextView.setText(questionContent);
        final MultiChoiceUnit.ActionHandler handler = new MultiChoiceUnit.ActionHandler() {
            @Override
            public void onSelect(int selectFromNoneOfAboveButton) {
                if (selectFromNoneOfAboveButton > 0 ){
                    for (MultiChoiceUnit unit : units) {
                        unit.reset();
                    }
                }else{
                    for (MultiChoiceUnit unit : units) {
                        if (unit.getIsNoneOfAboveButton()>0)
                            unit.reset();
                    }
                }
            }
            @Override
            public void onNoticeParent(){
                if (cellHandler!=null){
                    cellHandler.onSelect();
                }
            }
        };
        for (MultiChoiceUnit unit : units) {
            unit.setActionHandler(handler);
        }
    }



    public SurveyQuestionObject getAnswer(){
        String allAnswer = "";
        for (MultiChoiceUnit unit : units) {
            if (!unit.getAnswer().equals("") ){
                allAnswer += unit.getAnswer() + ",";
            }
        }

        Log.i("show", "all answer " + allAnswer);
        if (allAnswer.equals("")){
            return null;
        }else{
            surveyQuestionObject.setAnswer(allAnswer);
            return surveyQuestionObject;
        }
    }

}
