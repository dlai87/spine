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
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dehualai on 11/27/17.
 */

public class CustomizedShapeChartRenderer extends BarLineScatterCandleBubbleRenderer {

    protected BubbleDataProvider mChart;

    public CustomizedShapeChartRenderer(BubbleDataProvider chart, ChartAnimator animator,
                               ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        mChart = chart;

        mRenderPaint.setStyle(Paint.Style.FILL);

        mHighlightPaint.setStyle(Paint.Style.STROKE);
        mHighlightPaint.setStrokeWidth(Utils.convertDpToPixel(1.5f));
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

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        float phaseY = mAnimator.getPhaseY();

        mXBounds.set(mChart, dataSet);

        sizeBuffer[0] = 0f;
        sizeBuffer[2] = 1f;

        trans.pointValuesToPixel(sizeBuffer);

        boolean normalizeSize = dataSet.isNormalizeSizeEnabled();

        // calcualte the full width of 1 step on the x-axis
        final float maxBubbleWidth = Math.abs(sizeBuffer[2] - sizeBuffer[0]);
        final float maxBubbleHeight = Math.abs(mViewPortHandler.contentBottom() - mViewPortHandler.contentTop());
        final float referenceSize = Math.min(maxBubbleHeight, maxBubbleWidth);

        for (int j = mXBounds.min; j <= mXBounds.range + mXBounds.min; j++) {


            if ( j == mXBounds.min || j == mXBounds.range + mXBounds.min ) {
                final BubbleEntry entry = dataSet.getEntryForIndex(j);

                pointBuffer[0] = entry.getX();
                pointBuffer[1] = (entry.getY()) * phaseY;
                trans.pointValuesToPixel(pointBuffer);

                float shapeHalf = getShapeSize(entry.getSize(), dataSet.getMaxSize(), referenceSize, normalizeSize) / 2f;

                if (!mViewPortHandler.isInBoundsTop(pointBuffer[1] + shapeHalf)
                        || !mViewPortHandler.isInBoundsBottom(pointBuffer[1] - shapeHalf))
                    continue;

                if (!mViewPortHandler.isInBoundsLeft(pointBuffer[0] + shapeHalf))
                    continue;

                if (!mViewPortHandler.isInBoundsRight(pointBuffer[0] - shapeHalf))
                    break;

                final int color = dataSet.getColor((int) entry.getX());

                mRenderPaint.setColor(color);

                c.drawRect( new RectF(pointBuffer[0],pointBuffer[1], pointBuffer[0] + 100, pointBuffer[1] + 100),  mRenderPaint);

            }else{
                // previous point
                final BubbleEntry prevEntry = dataSet.getEntryForIndex(j-1);
                float[] prevPointBuffer = new float[2];
                prevPointBuffer[0] = prevEntry.getX();
                prevPointBuffer[1] = (prevEntry.getY()) * phaseY;
                trans.pointValuesToPixel(prevPointBuffer);

                // post point
                final BubbleEntry postEntry = dataSet.getEntryForIndex(j+1);
                float[] postPointBuffer = new float[2];
                postPointBuffer[0] = postEntry.getX();
                postPointBuffer[1] = (postEntry.getY()) * phaseY;
                trans.pointValuesToPixel(postPointBuffer);

                // current point
                final BubbleEntry entry = dataSet.getEntryForIndex(j);
                pointBuffer[0] = entry.getX();
                pointBuffer[1] = (entry.getY()) * phaseY;
                trans.pointValuesToPixel(pointBuffer);

                final int color = dataSet.getColor((int) entry.getX());
                mRenderPaint.setColor(color);

                float x_pre = prevPointBuffer[0];
                float y_pre = prevPointBuffer[1];
                float x_cur = pointBuffer[0];
                float y_cur = pointBuffer[1];
                float x_pos = postPointBuffer[0];
                float y_pos = postPointBuffer[1];

                Log.e("show", " x_pre " + x_pre + " y_pre " + y_pre
                        + " x_cur " + x_cur + " y_cur " + y_cur
                        + " x_pos " + x_pos + " y_pos " + y_pos
                );



                float l = (x_pos - x_pre) * 0.4f;
                float w = l / 2 ;

                float x1 = x_cur - l/2;
                float y1 = y_cur - w/2;
                float x2 = x_cur - l/2;
                float y2 = y_cur + w/2;
                float x3 = x_cur + l/2;
                float y3 = y_cur + w/2;
                float x4 = x_cur + l/2;
                float y4 = y_cur - w/2;

                /*
                float R = 0.4f;
                float k1 = - (x_cur - x_pre) / (y_cur - y_pre) ;
                float x_c1 = x_cur - R * (x_cur - x_pre);
                float y_c1 = y_cur - R * (y_cur - y_pre);

                Log.e("show", "k1 " + k1 + " x_c1 " + x_c1 + " y_c1 " + y_c1);


                float sqrt_y = (float) Math.sqrt(((x_c1 - x_cur)*(x_c1 - x_cur) + (y_c1-y_cur)*(y_c1-y_cur))/((1/(k1*k1) + 1)*(1/(k1*k1)+1)));
                float sqrt_x = (float) Math.sqrt(((x_c1 - x_cur)*(x_c1 - x_cur) + (y_c1-y_cur)*(y_c1-y_cur))/((k1*k1+1)*(k1*k1+1)));

                float y1 = y_c1-sqrt_y;
                float x1 = x_c1+sqrt_x;
                float y2 = y_c1+sqrt_y;
                float x2 = x_c1-sqrt_x;

                //float y3 = (2*y_cur - y1 - 2*k1 + k1*x2 + k1*k1*y2 + k1*x1)/(k1*k1+1);
                //float x3 = x2 - k1*y3 + k1*y2;
                //float y4 = y3 + y1 -y2;
                //float x4 = x3 + x1 -x2;

                float x3 = 2 * x_cur - x1;
                float y3 = 2 * y_cur - y1;
                float x4 = x3 + x1 - x2; //2 * x_cur - x2;
                float y4 = y3 + y1 - y2; //2 * y_cur - y2;

             //   float d = (float) (Math.abs(k1*x_cur  - y_cur + (-k1*x2 + y2)) / Math.sqrt( k1*k1 + 1));
             //   float y3 = (float) (y2 + 2*d / (Math.sqrt(k1*k1 + 1))) ;
             //   float x3 = x2 - k1*(y2-y3);
             //   float y4 = (float) (y1 + 2*d / (Math.sqrt(k1*k1 + 1))) ;
             //   float x4 = x1 - k1*(y1-y4);

                Log.e("show", "p1 " + x1 + "," + y1 + " p2 " + x2 + "," + y2
                        + " p3 " + x3 + "," + y3 + " p4 " + x4 + "," + y4);

                        */


              //  ArrayList<PointF> pointList = calPoints(prevPointBuffer,pointBuffer,postPointBuffer );
              //  if (pointList!=null){
              //      PointF p1 = pointList.get(0);
              //      PointF p2 = pointList.get(1);
              //      PointF p3 = pointList.get(2);
              //      PointF p4 = pointList.get(3);

              //      mRenderPaint.setStrokeWidth(2);
              //      c.drawLine(p1.x, p1.y, p2.x, p2.y, mRenderPaint);
              //      c.drawLine(p2.x, p2.y, p3.x, p3.y, mRenderPaint);
              //      c.drawLine(p3.x, p3.y, p4.x, p4.y, mRenderPaint);
              //      c.drawLine(p4.x, p4.y, p1.x, p1.y, mRenderPaint);
              //  }else{
                    PointF p1 = new PointF(x1, y1);
                    PointF p2 = new PointF(x2, y2);
                    PointF p3 = new PointF(x3, y3);
                    PointF p4 = new PointF(x4, y4);




                   // mRenderPaint.setStrokeWidth(2);
                 //   c.drawLine(p1.x, p1.y, p2.x, p2.y, mRenderPaint);
                 //   c.drawLine(p2.x, p2.y, p3.x, p3.y, mRenderPaint);
                 //   c.drawLine(p3.x, p3.y, p4.x, p4.y, mRenderPaint);
                 //   c.drawLine(p4.x, p4.y, p1.x, p1.y, mRenderPaint);


                    RectF rectF = new RectF(p1.x, p1.y, p3.x, p3.y);
                    mRenderPaint.setStrokeWidth(2);
                 //   c.drawRect(rectF, mRenderPaint);


                    Matrix m = new Matrix();
                    m.setRotate(45, x_cur, y_cur);
                    //RectF r = new RectF();
                    //m.mapRect(r, rectF);

                float[] points = {x1,y1,x2,y2,x3,y3,x4,y4};


                Log.e("show",
                        "ORI " +points[0]+
                        " " +points[1]+
                        " " +points[2]+
                        " " +points[3]+
                        " " +points[4]+
                        " " +points[5]+
                        " " +points[6]+
                        " " +points[7]);
                m.mapPoints(points);
                Log.e("show",
                        "dst " +points[0]+
                                " " +points[1]+
                                " " +points[2]+
                                " " +points[3]+
                                " " +points[4]+
                                " " +points[5]+
                                " " +points[6]+
                                " " +points[7]);





                    c.drawLine(points[0], points[1], points[2], points[3], mRenderPaint);
                    c.drawLine(points[2], points[3], points[4], points[5], mRenderPaint);
                    c.drawLine(points[4], points[5], points[6], points[7], mRenderPaint);
                    c.drawLine(points[6], points[7], points[0], points[1], mRenderPaint);



               // }


            }

        }
    }




    private ArrayList<PointF> calPoints(float[] pre, float[] cur, float[] post){
        if (pre==null || pre.length<=0 || cur==null || cur.length<=0 || post==null || post.length<=0) {
            return null;
        }

        float x_pre = pre[0];
        float y_pre = pre[1];
        float x_cur = cur[0];
        float y_cur = cur[1];
        float x_pos = post[0];
        float y_pos = post[0];

        float k = -(y_pre-y_pos)/(x_pre-x_pos);

        if (k < 0.00001) {
            return null;
        }

        float W = (float) (0.4 * Math.sqrt ((x_pos-x_pre)*(x_pos-x_pre) + (y_pos-y_pre)*(y_pos-y_pre)));
        float theda = (float) Math.atan(k);
        float alpha = (float) Math.PI/6 - theda;
        float beta = (float) Math.PI/6 + theda;

        float x1 = (float) (x_cur - W / (Math.sqrt(1+Math.tan(alpha)*Math.tan(alpha))));
        float x3 = (float) (x_cur + W / (Math.sqrt(1+Math.tan(alpha)*Math.tan(alpha))));
        float y1 = (float) (y_cur - Math.tan(alpha)*x_cur - Math.tan(alpha)*x1);
        float y3 = (float) (y_cur - Math.tan(alpha)*x_cur + Math.tan(alpha)*x1);

        float x2 = (float) (x_cur - W / (Math.sqrt(1+Math.tan(beta)*Math.tan(beta))));
        float y2 = (float) (y_cur - Math.tan(beta)*x_cur + Math.tan(beta)*x2);
        float x4 = (float) (x_cur + W / (Math.sqrt(1+Math.tan(beta)*Math.tan(beta))));
        float y4 = (float) (y_cur - Math.tan(beta)*x_cur - Math.tan(beta)*x2);

        ArrayList<PointF> result = new ArrayList<PointF>();

        result.add(new PointF(x1, y1));
        result.add(new PointF(x2, y2));
        result.add(new PointF(x3, y3));
        result.add(new PointF(x4, y4));


        return result;

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

                final float[] positions = mChart.getTransformer(dataSet.getAxisDependency())
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
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        BubbleData bubbleData = mChart.getBubbleData();

        float phaseY = mAnimator.getPhaseY();

        for (Highlight high : indices) {

            IBubbleDataSet set = bubbleData.getDataSetByIndex(high.getDataSetIndex());

            if (set == null || !set.isHighlightEnabled())
                continue;

            final BubbleEntry entry = set.getEntryForXValue(high.getX(), high.getY());

            if (entry.getY() != high.getY())
                continue;

            if (!isInBoundsX(entry, set))
                continue;

            Transformer trans = mChart.getTransformer(set.getAxisDependency());

            sizeBuffer[0] = 0f;
            sizeBuffer[2] = 1f;

            trans.pointValuesToPixel(sizeBuffer);

            boolean normalizeSize = set.isNormalizeSizeEnabled();

            // calcualte the full width of 1 step on the x-axis
            final float maxBubbleWidth = Math.abs(sizeBuffer[2] - sizeBuffer[0]);
            final float maxBubbleHeight = Math.abs(
                    mViewPortHandler.contentBottom() - mViewPortHandler.contentTop());
            final float referenceSize = Math.min(maxBubbleHeight, maxBubbleWidth);

            pointBuffer[0] = entry.getX();
            pointBuffer[1] = (entry.getY()) * phaseY;
            trans.pointValuesToPixel(pointBuffer);

            high.setDraw(pointBuffer[0], pointBuffer[1]);

            float shapeHalf = getShapeSize(entry.getSize(),
                    set.getMaxSize(),
                    referenceSize,
                    normalizeSize) / 2f;

            if (!mViewPortHandler.isInBoundsTop(pointBuffer[1] + shapeHalf)
                    || !mViewPortHandler.isInBoundsBottom(pointBuffer[1] - shapeHalf))
                continue;

            if (!mViewPortHandler.isInBoundsLeft(pointBuffer[0] + shapeHalf))
                continue;

            if (!mViewPortHandler.isInBoundsRight(pointBuffer[0] - shapeHalf))
                break;

            final int originalColor = set.getColor((int) entry.getX());

            Color.RGBToHSV(Color.red(originalColor), Color.green(originalColor),
                    Color.blue(originalColor), _hsvBuffer);
            _hsvBuffer[2] *= 0.5f;
            final int color = Color.HSVToColor(Color.alpha(originalColor), _hsvBuffer);

            mHighlightPaint.setColor(color);
            mHighlightPaint.setStrokeWidth(set.getHighlightCircleWidth());
            c.drawCircle(pointBuffer[0], pointBuffer[1], shapeHalf, mHighlightPaint);
        }
    }
}
