package com.vasomedical.spinetracer.fragment.analytics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.util.FloatProperty;
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


    MyTans tansPoint1;
    MyTans tansPoint2;

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
                final Canvas canvas = holder.lockCanvas();



                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // draw on canvas
                        Paint paint = new Paint();
                        paint.setStrokeWidth(30);
                        paint.setDither(true);                    // set the dither to true
                        paint.setStyle(Paint.Style.STROKE);       // set to STOKE
                        paint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
                        paint.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too
                        paint.setPathEffect(new CornerPathEffect(10) );   // set the path effect when they join.
                        paint.setAntiAlias(true);

                        draw(canvas, paint);

                    }
                });

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

        // draw white background
        Paint paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        paint2.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint2);

        ArrayList<Point> controlPoints = convertDataToPointList(detectionData, canvas.getWidth(), canvas.getHeight());

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
        for (SpinePiece piece : pieces){
            piece.drawLines(canvas);
            piece.drawPoints(canvas);
        }

    }


    private ArrayList<Point> convertDataToPointList(ArrayList<Entry> data, int canvasWidth, int canvasHeight){

        for(Entry p : data){
            Log.d("temp", "Entry " + p);
        }
        Log.d("temp", "===========");
        // find data range
        float minDataX = Float.MAX_VALUE;
        float minDataY = Float.MAX_VALUE;
        float maxDataX = Float.MIN_VALUE;
        float maxDataY = Float.MIN_VALUE;
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




    private ArrayList<MyTans> calTan(ArrayList<Point> points){
        ArrayList<MyTans> tans = new ArrayList<MyTans>();
        for(int i = 0 ; i < points.size()-1; i++){
            tans.add(new MyTans(points.get(i), points.get(i+1)));
        }

        for (int i =0 ; i<tans.size(); i++){
            Log.d("tans", i + " : " + tans.get(i));
        }

        analyze(tans);
        return tans;
    }



    private void drawLineWithExtern(Canvas canvas, Paint paint, Point p1 , Point p2){
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(2);

        int extern = 50;
        float t = (p1.y - p2.y)*1.0f/(p1.x-p2.x);

        canvas.drawLine( p2.x-extern , p2.y - t*extern , p2.x + extern, p2.y + t*extern, paint);
    }

    private void drawVerticalLine(Canvas canvas, Paint paint, Point p1 , Point p2){
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(2);

        float t = (p1.y - p2.y)*1.0f/(p1.x-p2.x);
        int x = p2.x + 300;
        int y = (int)(-300/t + p2.y);

        canvas.drawLine(p2.x, p2.y, x, y, paint);
    }


    private void analyze(ArrayList<MyTans> tansList){
        ArrayList<ArrayList<MyTans>> segments = new ArrayList<ArrayList<MyTans>>();

        ArrayList<MyTans> tempSeg = new ArrayList<MyTans>();
        // segmentation
        for(int i= 0 ; i < tansList.size(); i++ ){

            MyTans t = tansList.get(i);
            if(tempSeg.isEmpty()){
                tempSeg.add(t);
            }else{
                // same positive or negative
                if(t.getTans() * tempSeg.get(tempSeg.size()-1).getTans() > 0){
                    tempSeg.add(t);
                }else{
                    segments.add(tempSeg);
                    tempSeg = new ArrayList<MyTans>();
                    tempSeg.add(t);
                }
            }
            // add last segment
            if (i == tansList.size()-1){
                segments.add(tempSeg);
            }
        }
        // remove short segment
        for(int i=0; i < segments.size(); i++){
            int length = segments.get(i).size();
            Log.e("tans", "length  " + length);
            if (length < 4) {
                segments.remove(i);

            }
        }

        // find min points in each segment
        ArrayList<MyTans> minTansList = new ArrayList<MyTans>();
        for(int i = 0 ; i < segments.size(); i++){
            minTansList.add(minTansInList(segments.get(i)));
        }


        for (MyTans m : minTansList){
            if (tansPoint1==null){
                tansPoint1 = m;
            }else if(tansPoint2==null){
                tansPoint2 = m ;
            }
            Log.e("tans", "min tans " + m);
        }
        // draw line

        // calculate cobb's angle
    }


    private MyTans minTansInList(ArrayList<MyTans> segment){
        int i = 0 ;
        float minTan = Float.MAX_VALUE;
        for(int j=0; j < segment.size(); j++){
            if ( Math.abs(segment.get(j).getTans()) < minTan){
                minTan = Math.abs(segment.get(j).getTans());
                i = j;
            }
        }

        return segment.get(i);
    }


    class Segment{
        ArrayList<MyTans> pieces;
        public Segment(){
            pieces = new ArrayList<MyTans>();
        }

        public void addOnePiece(){

        }

        public void addSegment(){

        }
    }
    class MyTans{
        Point p1;
        Point p2;
        float tans;
        public MyTans(Point p1, Point p2){
            this.p1 = p1;
            this.p2 = p2;
            if (p1.x - p2.x != 0){
                tans = (p1.y - p2.y)*1.0f/(p1.x - p2.x);
            }else{
                tans = Float.MAX_VALUE;
            }
        }

        public float getTans(){
            return tans;
        }

        public Point getPoint1(){
            return p1;
        }

        public Point getPoint2(){
            return p2;
        }

        public void setTans(float tans){
            this.tans = tans;
        }

        public String toString(){
            return "Tans " + tans + " P1 " + p1 + " P2 " + p2;
        }

    }
}
