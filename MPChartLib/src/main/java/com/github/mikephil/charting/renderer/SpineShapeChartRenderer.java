package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

/**
 * Created by dehualai on 1/4/18.
 */

public class SpineShapeChartRenderer extends BarLineScatterCandleBubbleRenderer {

    private int SHAPE_WIDTH = 100 ;



    private HashMap<Entry, float[]> entryPointMap = new HashMap<Entry, float[]>();
    protected BubbleDataProvider mChart;

    private IChartCallback iChartCallback;

    public void setChartCallback(IChartCallback callback){
        this.iChartCallback = callback;
    }

    public SpineShapeChartRenderer(BubbleDataProvider chart, ChartAnimator animator,
                                        ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        mChart = chart;

        mRenderPaint.setStyle(Paint.Style.FILL);

        mHighlightPaint.setStyle(Paint.Style.STROKE);
        mHighlightPaint.setStrokeWidth(Utils.convertDpToPixel(1.5f));
    }


    public interface IChartCallback{
        public void onCobbsAngleCalculate(final float angle);
    }

    @Override
    public void initBuffers() {

    }

    @Override
    public void drawData(Canvas c) {

        BubbleData bubbleData = mChart.getBubbleData();

        for (IBubbleDataSet set : bubbleData.getDataSets()) {

            if (set.isVisible())
                drawDataSet(c, set);
        }
    }

    private float[] sizeBuffer = new float[4];
    private float[] pointBuffer = new float[2];

    protected float getShapeSize(float entrySize, float maxSize, float reference, boolean normalizeSize) {
        final float factor = normalizeSize ? ((maxSize == 0f) ? 1f : (float) Math.sqrt(entrySize / maxSize)) :
                entrySize;
        final float shapeSize = reference * factor;
        return shapeSize;
    }

    protected void drawDataSet(Canvas c, IBubbleDataSet dataSet) {

        mRenderPaint.setColor(Color.BLUE);
        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        float phaseY = mAnimator.getPhaseY();

        mXBounds.set(mChart, dataSet);

        sizeBuffer[0] = 0f;
        sizeBuffer[2] = 1f;

        trans.pointValuesToPixel(sizeBuffer);

        float shapeWidth = SHAPE_WIDTH;

       // boolean normalizeSize = dataSet.isNormalizeSizeEnabled();

        // calcualte the full width of 1 step on the x-axis
        //final float maxBubbleWidth = Math.abs(sizeBuffer[2] - sizeBuffer[0]);
        //final float maxBubbleHeight = Math.abs(mViewPortHandler.contentBottom() - mViewPortHandler.contentTop());
        //final float referenceSize = Math.min(maxBubbleHeight, maxBubbleWidth);

        for (int j = mXBounds.min; j <= mXBounds.range + mXBounds.min; j++) {

            // if first / last segment
            if ( j == mXBounds.min || j == mXBounds.range + mXBounds.min ){
                Coordinate curPoint =  dataEntryToCoor(dataSet.getEntryForIndex(j), trans, phaseY);

                if (!mViewPortHandler.isInBoundsTop(curPoint.y + shapeWidth)
                        || !mViewPortHandler.isInBoundsBottom(curPoint.y - shapeWidth))
                    continue;

                if (!mViewPortHandler.isInBoundsLeft(curPoint.x + shapeWidth))
                    continue;

                if (!mViewPortHandler.isInBoundsRight(curPoint.x - shapeWidth))
                    break;


              //  c.drawCircle(curPoint.x, curPoint.y, shapeWidth, mRenderPaint);
            }else{
                Coordinate prePoint =  dataEntryToCoor(dataSet.getEntryForIndex(j-1), trans, phaseY);
                Coordinate curPoint =  dataEntryToCoor(dataSet.getEntryForIndex(j), trans, phaseY);
                Coordinate posPoint =  dataEntryToCoor(dataSet.getEntryForIndex(j+1), trans, phaseY);

                float l = (posPoint.x - prePoint.x) * 0.45f;
                float x1 = curPoint.x - l/2;
                float y1 = curPoint.y - shapeWidth/2;
                float x2 = curPoint.x - l/2;
                float y2 = curPoint.y + shapeWidth/2;
                float x3 = curPoint.x + l/2;
                float y3 = curPoint.y + shapeWidth/2;
                float x4 = curPoint.x + l/2;
                float y4 = curPoint.y - shapeWidth/2;
                if((posPoint.x - prePoint.x)!=0){
                    float k = (posPoint.y - prePoint.y) / (posPoint.x - prePoint.x);
                    float angle = (float) Math.atan(k);
                    Matrix m = new Matrix();
                    m.setRotate( radianToDegree(angle, 0, false), curPoint.x, curPoint.y);

                    float[] points = {x1,y1,x2,y2,x3,y3,x4,y4};
                    m.mapPoints(points);

                    entryPointMap.put(dataSet.getEntryForIndex(j), points);
                    c.drawLine(points[0], points[1], points[2], points[3], mRenderPaint);
                    c.drawLine(points[2], points[3], points[4], points[5], mRenderPaint);
                    c.drawLine(points[4], points[5], points[6], points[7], mRenderPaint);
                    c.drawLine(points[6], points[7], points[0], points[1], mRenderPaint);


                }

            }

        }
    }


    private Coordinate dataEntryToCoor(BubbleEntry entry, Transformer trans, float phaseY){
        float[] pointBuf = new float[2];
        pointBuf[0] = entry.getX();
        pointBuf[1] = (entry.getY()) * phaseY;
        trans.pointValuesToPixel(pointBuf);
        return new Coordinate(pointBuf[0], pointBuf[1]);
    }




    @Override
    public void drawValues(Canvas c) {

        BubbleData bubbleData = mChart.getBubbleData();

        if (bubbleData == null)
            return;

        // if values are drawn
        if (isDrawingValuesAllowed(mChart)) {

            final List<IBubbleDataSet> dataSets = bubbleData.getDataSets();

            float lineHeight = Utils.calcTextHeight(mValuePaint, "1");

            for (int i = 0; i < dataSets.size(); i++) {

                IBubbleDataSet dataSet = dataSets.get(i);

                if (!shouldDrawValues(dataSet))
                    continue;

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet);

                final float phaseX = Math.max(0.f, Math.min(1.f, mAnimator.getPhaseX()));
                final float phaseY = mAnimator.getPhaseY();

                mXBounds.set(mChart, dataSet);

                YAxis.AxisDependency temp1 = dataSet.getAxisDependency();
                Transformer temp2 = mChart.getTransformer(temp1);
                final float[] positions = temp2
                        .generateTransformedValuesBubble(dataSet, phaseY, mXBounds.min, mXBounds.max);

                final float alpha = phaseX == 1 ? phaseY : phaseX;

                MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

                for (int j = 0; j < positions.length; j += 2) {

                    int valueTextColor = dataSet.getValueTextColor(j / 2 + mXBounds.min);
                    valueTextColor = Color.argb(Math.round(255.f * alpha), Color.red(valueTextColor),
                            Color.green(valueTextColor), Color.blue(valueTextColor));

                    float x = positions[j];
                    float y = positions[j + 1];

                    if (!mViewPortHandler.isInBoundsRight(x))
                        break;

                    if ((!mViewPortHandler.isInBoundsLeft(x) || !mViewPortHandler.isInBoundsY(y)))
                        continue;

                    BubbleEntry entry = dataSet.getEntryForIndex(j / 2 + mXBounds.min);

                    if (dataSet.isDrawValuesEnabled()) {
                        drawValue(c, dataSet.getValueFormatter(), entry.getSize(), entry, i, x,
                                y + (0.5f * lineHeight), valueTextColor);
                    }

                    if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {

                        Drawable icon = entry.getIcon();

                        Utils.drawImage(
                                c,
                                icon,
                                (int)(x + iconsOffset.x),
                                (int)(y + iconsOffset.y),
                                icon.getIntrinsicWidth(),
                                icon.getIntrinsicHeight());
                    }
                }

                MPPointF.recycleInstance(iconsOffset);
            }
        }
    }

    @Override
    public void drawExtras(Canvas c) {
    }

    private float[] _hsvBuffer = new float[3];



    @Override
    public void drawHighlightedQueue(Canvas c, Queue<Highlight> highlightQueue){
        if (highlightQueue!=null){

            double[] angles = new double[2];
            int i = 0 ;
            Highlight highlightLeft = null;
            Highlight highlightRight = null;

            if (highlightQueue.size() == 1){
                for(Highlight highlight: highlightQueue){
                    double angle = drawOneHighlight2(c, highlight);
                    if (angle!=Double.MIN_VALUE) {
                        angles[i] = drawOneHighlight2(c, highlight);
                        i ++;
                    }
                }
            }else if (highlightQueue.size() > 1){

                for(Highlight highlight: highlightQueue){
                    if (i==0) highlightLeft = highlight;
                    if (i==1) highlightRight = highlight;
                    i ++;
                }

                if(highlightLeft.getX() > highlightRight.getX()){
                    Highlight temp = highlightLeft;
                    highlightLeft = highlightRight;
                    highlightRight = temp;
                }

                i = 0  ;
                double angle = drawOneHighlight2(c, highlightLeft);
                if (angle!=Double.MIN_VALUE) {
                    angles[i] = drawOneHighlight2(c, highlightLeft);
                }

                i ++;
                angle = drawOneHighlight(c, highlightRight);
                if (angle!=Double.MIN_VALUE) {
                    angles[i] = drawOneHighlight(c, highlightRight);
                }
                i++;
            }

            /*
            for(Highlight highlight: highlightQueue){

                double angle = drawOneHighlight2(c, highlight);
                if (angle!=Double.MIN_VALUE) {
                    angles[i] = drawOneHighlight2(c, highlight);
                    i ++;
                }

            }

            */
            if(i > 1 ){
                Log.e("show", "angle 1 " + angles[0]);
                Log.e("show", "angle 1 " + angles[1]);
                double theta = Math.abs(angles[0] - angles[1]);
                Log.e("show", "theta " + radianToDegree((float) theta, 0, false));
                if (iChartCallback!=null){
                    iChartCallback.onCobbsAngleCalculate(radianToDegree((float) theta, 0, false));
                }
            }


        }else{
            Log.e("show", "===== drawHighlightedQueue ==== null");
        }

    }


    private double drawOneHighlight2(Canvas c, Highlight high){
        Log.e("show", "Y bond range " + mXBounds);

        BubbleData bubbleData = mChart.getBubbleData();
        IBubbleDataSet set = bubbleData.getDataSetByIndex(high.getDataSetIndex());
        final BubbleEntry entry = set.getEntryForXValue(high.getX(), high.getY());

        float[] points = entryPointMap.get(entry);

        if (points!=null){
            final int originalColor = set.getColor((int) entry.getX());

            Color.RGBToHSV(Color.red(originalColor), Color.green(originalColor),
                    Color.blue(originalColor), _hsvBuffer);
            _hsvBuffer[2] *= 0.5f;
            final int color = Color.HSVToColor(Color.alpha(originalColor), _hsvBuffer);

            mHighlightPaint.setColor(color);
            mHighlightPaint.setStrokeWidth(4);

            c.drawLine(points[0], points[1], points[2], points[3], mHighlightPaint);
            c.drawLine(points[2], points[3], points[4], points[5], mHighlightPaint);
            c.drawLine(points[4], points[5], points[6], points[7], mHighlightPaint);
            c.drawLine(points[6], points[7], points[0], points[1], mHighlightPaint);


            float yMin = 100;
            float yMax = 1600;

            float tan = (points[1] - points[3]) / (points[0] - points[2]) ;
            float xMin = (yMin - points[1]) / tan  +   points[0];
            float xMax = points[2] - ((points[3] - yMax)/tan);

            mHighlightPaint.setStrokeWidth(1.5f);
            c.drawLine(xMin, yMin, xMax, yMax, mHighlightPaint); // draw externed line


            if ((points[4] - points[2]) == 0) {
                return  Math.PI / 2;
            }else{
                float k = (points[3] - points[5]) / (points[4] - points[2]) ;
                return  Math.atan(k);
            }
        }


        return Double.MIN_VALUE;
    }

    private double drawOneHighlight(Canvas c, Highlight high){


        Log.e("show", "Y bond range " + mXBounds);

        BubbleData bubbleData = mChart.getBubbleData();
        IBubbleDataSet set = bubbleData.getDataSetByIndex(high.getDataSetIndex());
        final BubbleEntry entry = set.getEntryForXValue(high.getX(), high.getY());

        float[] points = entryPointMap.get(entry);

        if (points!=null){
            final int originalColor = set.getColor((int) entry.getX());

            Color.RGBToHSV(Color.red(originalColor), Color.green(originalColor),
                    Color.blue(originalColor), _hsvBuffer);
            _hsvBuffer[2] *= 0.5f;
            final int color = Color.HSVToColor(Color.alpha(originalColor), _hsvBuffer);

            mHighlightPaint.setColor(color);
            mHighlightPaint.setStrokeWidth(4);

            c.drawLine(points[0], points[1], points[2], points[3], mHighlightPaint);
            c.drawLine(points[2], points[3], points[4], points[5], mHighlightPaint);
            c.drawLine(points[4], points[5], points[6], points[7], mHighlightPaint);
            c.drawLine(points[6], points[7], points[0], points[1], mHighlightPaint);


            float yMin = 100;
            float yMax = 1600;

            float tan = (points[7] - points[5]) / (points[6] - points[4]) ;
            float xMin = (yMin - points[7]) / tan  +   points[6];
            float xMax = points[4] - ((points[5] - yMax)/tan);

            mHighlightPaint.setStrokeWidth(1.5f);
            c.drawLine(xMin, yMin, xMax, yMax, mHighlightPaint); // draw externed line


            if ((points[4] - points[2]) == 0) {
                return  Math.PI / 2;
            }else{
                float k = (points[3] - points[5]) / (points[4] - points[2]) ;
                return  Math.atan(k);
            }
        }


        return Double.MIN_VALUE;

    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {



    }



    public static float radianToDegree(float radian, int offset, boolean reverse){
        float degree = (float) Math.toDegrees(radian);
        degree += offset;
        if (reverse) degree = -degree;
        Log.d("temp", " radian "  + radian + " degree " + degree + " Math degree ");
        DecimalFormat decimalFormat = new DecimalFormat("#.#"); // output is one digital after point
        try {
            float normDegree = Float.valueOf(decimalFormat.format(degree));
            return normDegree ;
        }catch (Exception e){
            return -99999;
        }
    }
}







