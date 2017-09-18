package com.vasomedical.spinetracer.fragment.analytics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.vasomedical.spinetracer.R;

import java.util.ArrayList;

/**
 * Created by dehualai on 9/17/17.
 */

public class AnalyticOpt2Fragment extends AnalyticBaseFragment {


    SurfaceView surfaceView ;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        suggestion = new ArrayList<String>();
        suggestion.add("Suggestion 1");
        suggestion.add("Placeholder 2");
        suggestion.add("some text 3");
        suggestion.add("Suggestion 4");
        suggestion.add("Suggestion 5");
        suggestion.add("Suggestion 6");
        suggestion.add("Suggestion 7");
        suggestion.add("Suggestion 8");
        suggestionInitFlag = true;

        view = inflater.inflate(R.layout.fragment_analytic_opt2, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        assignViews();
        addActionToViews();

        return view;
    }


    @Override
    protected void assignViews() {
        super.assignViews();
        surfaceView = (SurfaceView)view.findViewById(R.id.surface);

    }

    @Override
    protected void addActionToViews() {

        super.addActionToViews();


        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // Do some drawing when surface is ready
                Canvas canvas = holder.lockCanvas();

                // draw on canvas
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                paint.setDither(false);
                paint.setStrokeWidth(30);
                paint.setDither(true);                    // set the dither to true
                paint.setStyle(Paint.Style.STROKE);       // set to STOKE
                paint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
                paint.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too
                paint.setPathEffect(new CornerPathEffect(10) );   // set the path effect when they join.
                paint.setAntiAlias(true);



                draw(canvas, paint);

                holder.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });


    }


    private void draw(Canvas canvas, Paint paint){

        int factor = 5;
        ArrayList<Point> controlPoints = new ArrayList<Point>();
        controlPoints.add(new Point(0*factor,90));
        controlPoints.add(new Point(1*factor,100));
        controlPoints.add(new Point(0*factor,145));
        controlPoints.add(new Point(10*factor,150));
        controlPoints.add(new Point(23*factor,155));
        controlPoints.add(new Point(73*factor,108));
        controlPoints.add(new Point(80*factor,120*factor));
        controlPoints.add(new Point(86*factor,131*factor));
        controlPoints.add(new Point(40*factor,210*factor));
        controlPoints.add(new Point(50*factor,220*factor));
        controlPoints.add(new Point(60*factor,230*factor));
        controlPoints.add(new Point(148*factor,185*factor));
        controlPoints.add(new Point(140*factor,180*factor));
        controlPoints.add(new Point(131*factor,175*factor));
        controlPoints.add(new Point(23*factor,188*factor));
        controlPoints.add(new Point(0*factor,190*factor));


        paint.setColor(Color.BLUE);

        int size = controlPoints.size();
        if (size < 2) {
            return;
        }

        Path curvePath = new Path();
        curvePath.moveTo(controlPoints.get(0).x, controlPoints.get(0).y);
        for (int idx = 1; idx < controlPoints.size(); idx += 3) {
            curvePath.cubicTo(controlPoints.get(idx).x,
                    controlPoints.get(idx).y, controlPoints.get(idx+1).x,
                    controlPoints.get(idx+1).y, controlPoints.get(idx+2).x,
                    controlPoints.get(idx+2).y);
        }

        canvas.drawPath(curvePath, paint);
    }


    private ArrayList<Point> convertDataToPointList(ArrayList<Entry> data, int canvasWidth, int canvasHeight){
        
    }

}
