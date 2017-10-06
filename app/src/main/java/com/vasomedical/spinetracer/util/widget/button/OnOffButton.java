package com.vasomedical.spinetracer.util.widget.button;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * Created by dehualai on 9/11/17.
 */

public class OnOffButton extends AppCompatButton{

    boolean isOn = false;
    Context context;

    public OnOffButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public OnOffButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public OnOffButton(Context context) {
        super(context);
        this.context = context;
    }

    public void onClick() {
        isOn = !isOn;
    }

    public boolean isOn(){
        return isOn;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (parentWidth < getSuggestedMinimumWidth())
            parentWidth = getSuggestedMinimumWidth();

        if (parentHeight < getSuggestedMinimumHeight())
            parentHeight = getSuggestedMinimumHeight();

        this.setMeasuredDimension(parentWidth, parentHeight);
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start,
                                 final int before, final int after) {
        super.onTextChanged(text, start, before, after);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }


}
