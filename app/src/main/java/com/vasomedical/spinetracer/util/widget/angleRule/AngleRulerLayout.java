package com.vasomedical.spinetracer.util.widget.angleRule;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by dehualai on 9/1/17.
 */

public class AngleRulerLayout extends RelativeLayout {
    public AngleRulerLayout(Context context) {
        super(context);
    }

    public AngleRulerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AngleRulerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AngleRulerLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void updateCiclePosition(){

    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
      //  Paint paint = new Paint();
      //  paint.setColor(Color.GRAY);
      //  canvas.drawCircle(10,10,50, paint);
    }
}
