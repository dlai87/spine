package com.vasomedical.spinetracer.fragment.analytics;

import android.app.Fragment;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.database.table.TBDataProcessed;
import com.vasomedical.spinetracer.database.util.DBAdapter;
import com.vasomedical.spinetracer.fragment.pdf.PdfViewFragment;
import com.vasomedical.spinetracer.util.Global;
import com.vasomedical.spinetracer.util.PdfManager;
import com.vasomedical.spinetracer.util.widget.button.OnOffButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by dehualai on 9/22/17.
 */

public abstract class AnalyticPositionPositionFragment
        extends AnalyticBaseFragment {

    SurfaceView surfaceView;
    ArrayList<Point> controlPoints;
    ArrayList<SpinePiece> piecesInterested;
    ArrayList<OnOffButton> piecesButtons = new ArrayList<OnOffButton>();
    LinearLayout pointsButtonLayout;
    TextView pointsText;
    TextView cobbsAngleText;

    protected Float cobbsAngle = null;
    boolean pointsLayoutFlag = false;
    Bitmap bitmap;


    LinkedBlockingQueue<String> buttonQueue = new LinkedBlockingQueue<String>(2);

    int CANVAS_MARGIN_TOP = 100;
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
        surfaceView = (SurfaceView) view.findViewById(R.id.surface);
        pointsText = (TextView) view.findViewById(R.id.points_button_text);
        pointsButtonLayout = (LinearLayout) view.findViewById(R.id.points_button_layout);
        cobbsAngleText = (TextView) view.findViewById(R.id.cobbs_angle_text);
    }

    @Override
    protected void addActionToViews() {

        super.addActionToViews();


        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // Do some drawing when surface is ready
                final Canvas canvas = holder.lockCanvas();
                //Bitmap  bitmap = Bitmap.createBitmap( surfaceView.getWidth(), surfaceView.getHeight(), Bitmap.Config.ARGB_8888);
                //canvas.setBitmap(bitmap);
                // generate control points
                controlPoints = convertDataToPointList(detectionData, canvas.getWidth(), canvas.getHeight());
                // draw on canvas
                drawGrid(canvas);
                draw(canvas);
                int numPoints = createPointButtons();
                if (numPoints == 2){
                    calculateCobbsTwoPoints();
                }else if (numPoints > 2){
                    calculateCobbs();
                }


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


    private ArrayList<Point> convertDataToPointList(ArrayList<Entry> data, int canvasWidth, int canvasHeight) {

        CANVAS_MARGIN_LEFT = canvasWidth / 3;
        int drawingAreaHeight = canvasHeight - CANVAS_MARGIN_TOP;
        int drawingAreaWidth = canvasWidth - CANVAS_MARGIN_LEFT;

        for (Entry p : data) {
            Log.d("temp", "Entry " + p);
        }

        TBDataProcessed tbDataProcessed = new TBDataProcessed();
        tbDataProcessed.smartInsert(DBAdapter.getDatabase(mContext), data, "posVSpos");

        Log.d("temp", "===========");
        // find data range
        for (Entry entry : data) {
            if (entry.getX() < minDataX) minDataX = entry.getX();
            if (entry.getX() > maxDataX) maxDataX = entry.getX();
            if (entry.getY() < minDataY) minDataY = entry.getY();
            if (entry.getY() > maxDataY) maxDataY = entry.getY();
        }

        float dataHeight = maxDataY - minDataY;
        float scaleFactor = dataHeight / drawingAreaHeight;

        ArrayList<Point> controlPoints = new ArrayList<Point>();

        //int shift = canvasWidth/3;
        // scale and shift
        for (Entry entry : data) {
            int x = (int) ((entry.getX() - minDataX) / scaleFactor) + CANVAS_MARGIN_LEFT;
            int y = (int) ((entry.getY() - minDataY) / scaleFactor) + CANVAS_MARGIN_TOP;
            controlPoints.add(new Point(x, y));
        }

        for (Point p : controlPoints) {
            Log.d("temp", "points " + p);
            if (p.x < minPointX) minPointX = p.x;
            if (p.y < minPointY) minPointY = p.y;
            if (p.x > maxPointX) maxPointX = p.x;
            if (p.y > maxPointY) maxPointY = p.y;
        }

        return controlPoints;
    }


    private void draw(Canvas canvas) {

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
        for (int idx = 1; idx < controlPoints.size() - 3; idx += 3) {
            curvePath.cubicTo(controlPoints.get(idx).x,
                    controlPoints.get(idx).y, controlPoints.get(idx + 1).x,
                    controlPoints.get(idx + 1).y, controlPoints.get(idx + 2).x,
                    controlPoints.get(idx + 2).y);
        }

        canvas.drawPath(curvePath, paint);


        AnalyticUtil util = new AnalyticUtil();

        piecesInterested = util.findPieceOfInterested(controlPoints);

        for (int i = 0; i < piecesInterested.size(); i++) {
            SpinePiece piece = piecesInterested.get(i);
            piece.drawLines(canvas);
            piece.drawPoints(canvas);
            piece.setLabel("P" + (i + 1));
            piece.drawText(canvas);
        }


    }


    private void drawGrid(Canvas canvas) {

        // draw white background
        Paint paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        paint2.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint2);

        Paint paint1 = new Paint();
        paint1.setStrokeWidth(2);
        paint1.setStyle(Paint.Style.STROKE);       // set to STOKE
        paint1.setAntiAlias(true);
        paint1.setColor(Color.GRAY);


        canvas.drawLine(minPointX - 50, maxPointY, minPointX - 50, minPointY, paint1);
        canvas.drawLine(minPointX - 50, maxPointY, minPointX - 60, maxPointY, paint1);
        canvas.drawLine(minPointX - 50, minPointY, minPointX - 60, minPointY, paint1);

        canvas.drawLine(minPointX, minPointY - 30, maxPointX, minPointY - 30, paint1);
        canvas.drawLine(minPointX, minPointY - 30, minPointX, minPointY - 40, paint1);
        canvas.drawLine(maxPointX, minPointY - 30, maxPointX, minPointY - 40, paint1);

        String width = String.format("%.1f", (maxDataX - minDataX) * 100) + "cm";
        String height = String.format("%.1f", (maxDataY - minDataY) * 100) + "cm";
        paint1.setTextSize(30);
        paint1.setStyle(Paint.Style.FILL);
        canvas.drawText(width, minPointX + 50, minPointY - 50, paint1);
        canvas.drawText(height, minPointX - 200, maxPointY / 2 - 50, paint1);

    }


    private int createPointButtons() {
        if (piecesInterested != null) {

            if(piecesInterested.size() <= 1){  // 1 point or less
                invalidDetectionLayout.setVisibility(View.VISIBLE);
                validLayout.setVisibility(View.GONE);
            }
            else if (piecesInterested.size() == 2){  // total 2 points
                calculateCobbsTwoPoints();
            }
            else{   // total  2+ points
                if (!pointsLayoutFlag) {
                    pointsText.setVisibility(View.VISIBLE);
                    pointsLayoutFlag = true;

                    for (final SpinePiece piece : piecesInterested) {
                        final OnOffButton button = new OnOffButton(mContext);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(500, 30);
                        params.setMargins(10, 5, 10, 5);
                        button.setLayoutParams(params);
                        button.setBackground(mContext.getResources().getDrawable(R.drawable.grey_round_rect));
                        button.setText(piece.getLabel());
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                button.onClick();
                                button.setBackground(button.isOn() ?
                                        mContext.getResources().getDrawable(R.drawable.green_round_rect) :
                                        mContext.getResources().getDrawable(R.drawable.grey_round_rect));

                                if (buttonQueue.remainingCapacity() <= 0) {
                                    buttonQueue.poll();
                                }
                                buttonQueue.offer(piece.getLabel());


                                for (Button b : piecesButtons) {
                                    String str = String.valueOf(b.getText());
                                    b.setBackground(buttonQueue.contains(str) ?
                                            mContext.getResources().getDrawable(R.drawable.green_round_rect) :
                                            mContext.getResources().getDrawable(R.drawable.grey_round_rect));

                                }


                                calculateCobbs();

                            }
                        });
                        piecesButtons.add(button);
                    }
                    for (OnOffButton button : piecesButtons) {
                        pointsButtonLayout.addView(button);
                    }
                }
            }
            return piecesInterested.size();
        }else{
            // illegal
            invalidDetectionLayout.setVisibility(View.VISIBLE);
            validLayout.setVisibility(View.GONE);
            return 0;
        }
    }



    private void calculateCobbs(){
        Object[] selectPoints = buttonQueue.toArray();
        if (selectPoints!=null && selectPoints.length == 2 && !selectPoints[0].equals(selectPoints[1])){
            float angle1 = 0 ;
            float angle2 = 0 ;
            for (SpinePiece s : piecesInterested){
                if (s.getLabel().equals(selectPoints[0]))
                    angle1 = s.getAbsAngle();
                if (s.getLabel().equals(selectPoints[1]))
                    angle2 = s.getAbsAngle();
            }
            cobbsAngle = 180 - (angle1+angle2);
            Log.d("show", "angle1 " + angle1 + " angle2 " + angle2 + " cobbs " + cobbsAngle) ;

            cobbsAngleText.setText(mContext.getResources().getString(R.string.cobbs_angle)
                    + ":" + String.format("%.1f", cobbsAngle)
            + mContext.getResources().getString(R.string.degree_mark));
        }else{
            cobbsAngle = null;
            cobbsAngleText.setText(mContext.getResources().getString(R.string.cobbs_angle) + ":" + "NaN");
        }
    }


    private void calculateCobbsTwoPoints(){
        float angle1 = 0 ;
        float angle2 = 0 ;
        for (SpinePiece s : piecesInterested){
            angle1 = s.getAbsAngle();
            angle2 = s.getAbsAngle();
        }
        cobbsAngle = 180 - (angle1+angle2);
        Log.d("show", "angle1 " + angle1 + " angle2 " + angle2 + " cobbs " + cobbsAngle) ;

        cobbsAngleText.setText(mContext.getResources().getString(R.string.cobbs_angle)
                + ":" + String.format("%.1f", cobbsAngle)
                + mContext.getResources().getString(R.string.degree_mark));
    }



    @Override
    protected void createandDisplayPdf() {

        if (bitmap!=null){
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(Global.FOLDER_PDF + "bmp.png");
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.createandDisplayPdf();


    }


}
