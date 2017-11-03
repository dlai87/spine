package com.vasomedical.spinetracer.algorithm.kalman;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.database.util.DBAdapter;
import com.vasomedical.spinetracer.fragment.login.LoginFragment;
import com.vasomedical.spinetracer.util.fragmentTransation.FragmentUtil;

import java.util.ArrayList;
import java.util.Random;

import jama.Matrix;
import jkalman.JKalman;

/**
 * Created by dehualai on 10/29/17.
 */

public class JKalmanActivity extends Activity {

    Context mContext ;


    ArrayList<Entry> dataSet1 = new ArrayList<Entry>();
    ArrayList<Entry> dataSet2 = new ArrayList<Entry>();


    private BubbleChart mChart;



    @Override
    public void onStart(){
        super.onStart();
        mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jkalman);
        test();


        mChart = (BubbleChart) findViewById(R.id.chart1);
        mChart.getDescription().setEnabled(false);

        mChart.setDrawGridBackground(false);

        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        mChart.setMaxVisibleValueCount(200);
        mChart.setPinchZoom(true);

        //   mSeekBarX.setProgress(10);
        //   mSeekBarY.setProgress(50);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        YAxis yl = mChart.getAxisLeft();
        yl.setSpaceTop(30f);
        yl.setSpaceBottom(30f);
        yl.setDrawZeroLine(false);

        mChart.getAxisRight().setEnabled(false);

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);

        setData();
    }


    private void setData(){
        int count = 50;
        int range = 50;

        ArrayList<BubbleEntry> yVals1 = new ArrayList<BubbleEntry>();
        ArrayList<BubbleEntry> yVals2 = new ArrayList<BubbleEntry>();
        //   ArrayList<BubbleEntry> yVals3 = new ArrayList<BubbleEntry>();

        /*
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range);
            float size = (float) (Math.random() * range);
            size = 3;
            yVals1.add(new BubbleEntry(i, val, size));
        }

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range);
            float size = (float) (Math.random() * range);
            size = 2;
            yVals2.add(new BubbleEntry(i, val, size));
        }

        */

        for(int i = 0 ; i < dataSet1.size(); i++){
            Entry entry = dataSet1.get(i);
            yVals1.add(new BubbleEntry(entry.getX(), entry.getY(), 3));
        }

        for(int i = 0 ; i < dataSet2.size(); i++){
            Entry entry = dataSet2.get(i);
            yVals2.add(new BubbleEntry(entry.getX(), entry.getY(), 3));
        }
        // create a dataset and give it a type
        BubbleDataSet set1 = new BubbleDataSet(yVals1, "DS 1");
        set1.setColor(ColorTemplate.COLORFUL_COLORS[0], 255);
        set1.setDrawValues(true);
        BubbleDataSet set2 = new BubbleDataSet(yVals2, "DS 2");
        set2.setColor(ColorTemplate.COLORFUL_COLORS[1], 255);
        set2.setDrawValues(true);


        ArrayList<IBubbleDataSet> dataSets = new ArrayList<IBubbleDataSet>();
        dataSets.add(set1); // add the datasets
        dataSets.add(set2);
        // dataSets.add(set3);

        // create a data object with the datasets
        BubbleData data = new BubbleData(dataSets);
        data.setDrawValues(false);
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.BLACK);
        data.setHighlightCircleWidth(1.5f);

        mChart.setData(data);
        mChart.invalidate();
    }

    /**
     * Main method
\     */
    void test() {

        try {
            JKalman kalman = new JKalman(4, 2);

            Random rand = new Random(System.currentTimeMillis() % 3000);
            double x = 0;
            double y = 0;
            // constant velocity
            double dx = rand.nextDouble();
            double dy = rand.nextDouble();

            // init
            Matrix s = new Matrix(4, 1); // state [x, y, dx, dy, dxy]
            Matrix c = new Matrix(4, 1); // corrected state [x, y, dx, dy, dxy]

            Matrix m = new Matrix(2, 1); // measurement [x]
            m.set(0, 0, x);
            m.set(1, 0, y);

            // transitions for x, y, dx, dy
            double[][] tr = {
                    {1, 0, 1, 0},
                    {0, 1, 0, 1},
                    {0, 0, 1, 0},
                    {0, 0, 0, 1} };
            kalman.setTransition_matrix(new Matrix(tr));

            // 1s somewhere?
            kalman.setError_cov_post(kalman.getError_cov_post().identity());

            // init first assumption similar to first observation (cheat :)
            // kalman.setState_post(kalman.getState_post());

            // report what happend first :)
            Log.i("System", "first x:" + x + ", y:" + y + ", dx:" + dx + ", dy:" + dy);
            Log.i("System", "no; x; y; dx; dy; predictionX; predictionY; predictionDx; predictionDy; correctionX; correctionY; correctionDx; correctionDy;");

            // For debug only
            for (int i = 0; i < 100; ++i) {

                // check state before
                s = kalman.Predict();

                // function init :)
                // m.set(1, 0, rand.nextDouble());
                x = rand.nextGaussian();
                y = rand.nextGaussian();

                m.set(0, 0, m.get(0, 0) + dx + rand.nextGaussian());
                m.set(1, 0, m.get(1, 0) + dy + rand.nextGaussian());

                // a missing value (more then 1/4 times)
                if (rand.nextGaussian() < -0.8) {
                    Log.i("System", "" + i + ";;;;;"
                            + s.get(0, 0) + ";" + s.get(1, 0) + ";" + s.get(2, 0) + ";" + s.get(3, 0) + ";");
                }
                else { // measurement is ok :)
                    // look better
                    c = kalman.Correct(m);

                    Log.i("System", "" + i + ";" +  m.get(0, 0) + ";" + m.get(1, 0) + ";" + x + ";" + y + ";"
                            + s.get(0, 0) + ";" + s.get(1, 0) + ";" + s.get(2, 0) + ";" + s.get(3, 0) + ";"
                            + c.get(0, 0) + ";" + c.get(1, 0) + ";" + c.get(2, 0) + ";" + c.get(3, 0) + ";");
                }

                dataSet1.add(new Entry((float) s.get(0, 0), (float) s.get(1, 0)));
                dataSet2.add(new Entry((float) c.get(0, 0), (float) c.get(1, 0)));
            }
        } catch (Exception ex) {
            Log.i("System", ex.getMessage());
        }
    }
}
