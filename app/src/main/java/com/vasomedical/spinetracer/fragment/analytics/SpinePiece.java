package com.vasomedical.spinetracer.fragment.analytics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Created by dehualai on 9/20/17.
 */

public class SpinePiece {


    Point p1;
    Point p2;
    float tans;
    boolean isSingluarPoint = false;

    public SpinePiece(Point p1, Point p2){
        this.p1 = p1;
        this.p2 = p2;
        if (p1.x - p2.x != 0){
            tans = (p1.y - p2.y)*1.0f/(p1.x - p2.x);
        }else{
            tans = Float.MAX_VALUE;
        }
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    public float getTans() {
        return tans;
    }

    public boolean isNegative(){
        return tans < 0;
    }

    public void setSingluarPoint(boolean flag){
        this.isSingluarPoint = flag;
    }

    public boolean isSingluarPoint(){
        return this.isSingluarPoint;
    }


    public void drawLines(Canvas canvas){
        drawLineWithExtern(canvas, p1, p2);
        drawVerticalLine(canvas, p1, p2);
    }

    public void drawPoints(Canvas canvas){

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(2);
        canvas.drawCircle(p2.x, p2.y, 20, paint);
    }



    private void drawLineWithExtern(Canvas canvas, Point p1 , Point p2){
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(2);

        int extern = 50;
        float t = (p1.y - p2.y)*1.0f/(p1.x-p2.x);

        canvas.drawLine( p2.x-extern , p2.y - t*extern , p2.x + extern, p2.y + t*extern, paint);
    }

    private void drawVerticalLine(Canvas canvas,  Point p1 , Point p2){
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(2);

        float t = (p1.y - p2.y)*1.0f/(p1.x-p2.x);
        int x = p2.x + 300;
        int y = (int)(-300/t + p2.y);

        canvas.drawLine(p2.x, p2.y, x, y, paint);
    }
}
