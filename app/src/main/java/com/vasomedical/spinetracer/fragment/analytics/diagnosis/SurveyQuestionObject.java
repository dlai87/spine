package com.vasomedical.spinetracer.fragment.analytics.diagnosis;

import java.util.Date;

/**
 * Created by dehualai on 10/12/16.
 */

public class SurveyQuestionObject {

    private String questionContent;

    private String answer;
    private Date answerTime;


    private String questionType;
    private int scaleMin;
    private int scaleMax;

    public SurveyQuestionObject(String questionContent){
        this.questionContent = questionContent;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        setAnswerTime(new Date());
        this.answer = answer;
    }

    public Date getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(Date answerTime) {
        this.answerTime = answerTime;
    }

    public int getScaleMin() {
        return scaleMin;
    }

    public void setScaleMin(int scaleMin) {
        this.scaleMin = scaleMin;
    }

    public int getScaleMax() {
        return scaleMax;
    }

    public void setScaleMax(int scaleMax) {
        this.scaleMax = scaleMax;
    }
}
