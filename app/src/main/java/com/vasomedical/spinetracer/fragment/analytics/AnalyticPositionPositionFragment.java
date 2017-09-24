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

import com.github.mikephil.charting.data.Entry;
import com.vasomedical.spinetracer.R;

import java.util.ArrayList;

/**
 * Created by dehualai on 9/22/17.
 */

public abstract class AnalyticPositionPositionFragment
        extends AnalyticBaseFragment {

    SurfaceView surfaceView ;
    ArrayList<Point> controlPoints;

    int CANVAS_MARGIN_TOP = 30;
    int CANVAS_MARGIN_LEFT = 100;

    float minDataX = Float.MAX_VALUE;
    float minDataY = Float.MAX_VALUE;
    float maxDataX = Float.MIN_VALUE;
    float maxDataY = Float.MIN_VALUE;

    int minPointX = Integer.MAX_VALUE;
    int minPointY = Integer.MAX_VALUE;
    int maxPointX = Integer.MIN_VALUE;
    int maxPointY = Integer.MIN_VALUE;

    protected abstract void initSubclassValues();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initSubclassValues();

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
                final Canvas canvas = holder.lockCanvas();
                // generate control points
                controlPoints = convertDataToPointList(detectionData, canvas.getWidth(), canvas.getHeight());

                // draw on canvas

                drawGrid(canvas);
                draw(canvas);


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


    private ArrayList<Point> convertDataToPointList(ArrayList<Entry> data, int canvasWidth, int canvasHeight){

        for(Entry p : data){
            Log.d("temp", "Entry " + p);
        }
        Log.d("temp", "===========");
        // find data range
        for (Entry entry : data){
            if (entry.getX() < minDataX) minDataX = entry.getX();
            if (entry.getX() > maxDataX) maxDataX = entry.getX();
            if (entry.getY() < minDataY) minDataY = entry.getY();
            if (entry.getY() > maxDataY) maxDataY = entry.getY();
        }

        float dataHeight = maxDataY - minDataY;
        float scaleFactor = dataHeight / canvasHeight;

        ArrayList<Point> controlPoints = new ArrayList<Point>();

        int shift = canvasWidth/3;
        // scale and shift
        for (Entry entry : data){
            int x = (int)((entry.getX() - minDataX)/scaleFactor) + shift;
            int y = (int)((entry.getY() - minDataY)/scaleFactor);
            controlPoints.add(new Point(x,y));
        }

        for(Point p : controlPoints){
            Log.d("temp", "points " + p);
        }

        return controlPoints;
    }



    private void draw(Canvas canvas){

        // draw Spine shape
        Paint paint = new Paint();
        paint.setStrokeWidth(30);
        paint.setDither(true);                    // set the dither to true
        paint.setStyle(Paint.Style.STROKE);       // set to STOKE
        paint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        paint.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too
        paint.setPathEffect(new CornerPathEffect(10));   // set the path effect when they join.
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);

        int size = controlPoints.size();
        if (size < 2) {
            return;
        }

        Path curvePath = new Path();
        curvePath.moveTo(controlPoints.get(0).x, controlPoints.get(0).y);
        for (int idx = 1; idx < controlPoints.size()-3; idx += 3) {
            curvePath.cubicTo(controlPoints.get(idx).x,
                    controlPoints.get(idx).y, controlPoints.get(idx+1).x,
                    controlPoints.get(idx+1).y, controlPoints.get(idx+2).x,
                    controlPoints.get(idx+2).y);
        }

        canvas.drawPath(curvePath, paint);


        AnalyticUtil util = new AnalyticUtil();

        ArrayList<SpinePiece> pieces = util.findPieceOfInterested(controlPoints);
        for (SpinePiece piece : pieces) {
            piece.drawLines(canvas);
            piece.drawPoints(canvas);
        }


    }



    private void drawGrid(Canvas canvas){

        // draw white background
        Paint paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        paint2.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint2);

    }

}
