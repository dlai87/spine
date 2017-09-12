package com.vasomedical.spinetracer.fragment.analytics.diagnosis;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by dehualai on 10/12/16.
 */

public abstract class BaseCell extends LinearLayout {

    protected Context mContext;
    protected SurveyQuestionObject surveyQuestionObject;
    protected String questionContent;

    protected TextView questionTextView;

    protected ActionHandler cellHandler;

    public interface ActionHandler{
        public void onSelect();
    }

    public void setActionHandler(ActionHandler handler){
        this.cellHandler = handler;
    }


    public BaseCell(Context context) {
        super(context);
        this.mContext = context;
        initViews(context, null);

    }

    public BaseCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initViews(context, attrs);

    }

    public BaseCell(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        this.mContext = context;
        initViews(context, attrs);
    }

    protected void initViews(Context context, AttributeSet attrs){
            if (context == null) {
                return;
            }
            TypedArray typedArray = null;
            try {
                if (attrs != null) {
                  //  typedArray = context.obtainStyledAttributes(attrs, com.aicure.aiview.R.styleable.EmotionCell);
                  //  questionContent = typedArray.getString(com.aicure.aiview.R.styleable.EmotionCell_question);
                }
            } finally {
                if (typedArray != null) {
                    typedArray.recycle();
                }
            }
    }


    public void setSurveyQuestionObject(SurveyQuestionObject object) {
        this.surveyQuestionObject = object;
        questionContent = object.getQuestionContent();
        if (questionTextView != null) {
            questionTextView.setText(questionContent);
        }
    }


    public abstract SurveyQuestionObject getAnswer();
}
