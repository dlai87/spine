package com.vasomedical.spinetracer.fragment.analytics;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.itextpdf.text.Font;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.fragment.BaseFragment;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;


import com.github.mikephil.charting.charts.LineChart;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by dehualai on 5/29/17.
 */

public class AnalyticFragment extends BaseFragment {


    private LineChart mChart;

    Button saveButton;
    Button reTestButton;
    LinearLayout invalidDetectionLayout;
    ScrollView validLayout;
    private ArrayList<Entry> detectionData;
    float yAxisRange = 10f;


    public void setDetectionData(ArrayList<Entry> data){
        detectionData = data;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_analytic, container, false);
        Bundle args = getArguments();

        setStepIndicator(2);

        assignViews();
        addActionToViews();

       // screenShot();
       // createandDisplayPdf("test");

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    protected void assignViews() {

        saveButton = (Button)view.findViewById(R.id.saveButton);
        mChart = (LineChart) view.findViewById(R.id.chart1);
        invalidDetectionLayout = (LinearLayout)view.findViewById(R.id.invalid_detection_layout);
        validLayout = (ScrollView)view.findViewById(R.id.scrollView);
        reTestButton = (Button)view.findViewById(R.id.re_test_button);


        if(detectionData != null){

            if (detectionData.size() <= 0){
                // illegal detection data
                invalidDetectionLayout.setVisibility(View.VISIBLE);
                validLayout.setVisibility(View.GONE);
                return;
            }

            for (Entry e : detectionData){
                float range = Math.abs(e.getY());
                if (range >= yAxisRange){
                    yAxisRange = range + 2;
                }
            }
        }

        mChart.setDrawGridBackground(false);

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
        //xAxis.addLimitLine(llXAxis); // add x-axis limit line


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMaximum(yAxisRange);
        leftAxis.setAxisMinimum(-yAxisRange);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        // add data
        setData();


        mChart.animateX(2500);
        //mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
    }

    @Override
    protected void addActionToViews() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/";
                boolean success = mChart.saveToPath("chart.jpg", "/Android/data/");
                Log.e("show", "save to path success " + success );
                success = mChart.saveToGallery("Chart.jpg",60);
                Log.e("show", "save to gallery success " + success);
                createandDisplayPdf("Test");
            }
        });
    }


    private void setData() {
        createData();
    }


    private void createData() {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        if (detectionData!=null){
            if (detectionData.size() <= 0){
                // illegal detection data
                return;
            }
            yVals1 = detectionData;
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();

        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, "DataSet 1");

            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(2f);
            set1.setCircleRadius(2f);
            //set1.setFillAlpha(65);
            //set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setDrawCircleHole(false);
            set1.setDrawFilled(true);
            //set1.setFormLineDashEffect(new DashPathEffect(new float[]{20f, 20f}, 0f));
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            // create a data object with the datasets
            LineData data = new LineData(set1);
            //   data.setValueTextColor(Color.WHITE);
            //   data.setValueTextSize(9f);

            // set data
            mChart.setData(data);
            mChart.getDescription().setEnabled(false);

        }
    }



    private void screenShot(){

        // image naming and path  to include sd card  appending name you choose for file
        String mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/screen.jpg";
// create bitmap screen capture
        Bitmap bitmap;

        View scrollView = view.findViewById(R.id.scrollView);
        scrollView.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(scrollView.getDrawingCache());
        scrollView.setDrawingCacheEnabled(false);

        OutputStream fout = null;
        File imageFile = new File(mPath);

        try {
            fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
            fout.flush();
            fout.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void createandDisplayPdf(String text) {

        Document doc = new Document();

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/";

            Log.e("show", "path " + path);
            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            File file = new File(dir, "newFile.pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();

            Paragraph p1 = new Paragraph(text);
            p1.setAlignment(Paragraph.ALIGN_CENTER);

            //add paragraph to document
            doc.add(p1);

        } catch (Exception de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        }
        finally {
            doc.close();
        }

    }


}
