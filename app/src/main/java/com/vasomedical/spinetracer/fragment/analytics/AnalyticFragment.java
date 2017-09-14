package com.vasomedical.spinetracer.fragment.analytics;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.fragment.BaseFragment;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;



import com.github.mikephil.charting.charts.LineChart;
import com.vasomedical.spinetracer.util.Util;
import com.vasomedical.spinetracer.util.widget.button.OnOffButton;

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

    ImageView arrowIndicator0;
    ImageView arrowIndicator1;
    ImageView arrowIndicator2;
    ImageView arrowIndicator3;
    ImageView arrowIndicator4;
    ImageView arrowIndicator5;

    LinearLayout list1Layout;
    LinearLayout list2Layout;
    LinearLayout list3Layout;
    boolean flag = true;

    String[] suggestion = {"thaq", "adfhad", "jyj", "thjrjarqja", "htah ath ", "htjmaoin", "htahaeh"};

    ArrayList<OnOffButton> selectButtonArray = new ArrayList<OnOffButton>();
    private ArrayList<Entry> detectionData;
    float yAxisRange = 10f;


    public static final String SCORE = "SCORE";
    private int score = -1;

    public void setDetectionData(ArrayList<Entry> data){
        detectionData = data;
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_analytic, container, false);

        setStepIndicator(2);

        Bundle args = getArguments();
        if (args!=null){
            score = args.getInt(SCORE);
        }

        try {
            assignViews();
            addActionToViews();
        }catch (Exception e){
            Log.e("show", "Exception " + e.getMessage());
        }

        keyboardAdvance(view);

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    protected void assignViews() {

        saveButton = (Button)view.findViewById(R.id.saveButton);
        mChart = (LineChart) view.findViewById(R.id.chart1);
        invalidDetectionLayout = (LinearLayout)view.findViewById(R.id.invalid_detection_layout);
        validLayout = (ScrollView)view.findViewById(R.id.scrollView);
        reTestButton = (Button)view.findViewById(R.id.re_test_button);

        arrowIndicator0 = (ImageView)view.findViewById(R.id.arrow_indicator_0);
        arrowIndicator1 = (ImageView)view.findViewById(R.id.arrow_indicator_1);
        arrowIndicator2 = (ImageView)view.findViewById(R.id.arrow_indicator_2);
        arrowIndicator3 = (ImageView)view.findViewById(R.id.arrow_indicator_3);
        arrowIndicator4 = (ImageView)view.findViewById(R.id.arrow_indicator_4);
        arrowIndicator5 = (ImageView)view.findViewById(R.id.arrow_indicator_5);

        list1Layout = (LinearLayout)view.findViewById(R.id.list1);
        list2Layout = (LinearLayout)view.findViewById(R.id.list2);
        list3Layout = (LinearLayout)view.findViewById(R.id.list3);

        if (flag){
            flag = false;
            for(int i = 0 ; i < suggestion.length; i++){
                final OnOffButton button = new OnOffButton(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(500, 30);
                params.setMargins(10, 5, 10, 5);
                button.setLayoutParams(params);
                button.setBackground(mContext.getResources().getDrawable(R.drawable.grey_round_rect));
                button.setText(suggestion[i]);
                if (i%3==0){
                    list1Layout.addView(button);
                }else if(i%3==1){
                    list2Layout.addView(button);
                }else{
                    list3Layout.addView(button);
                }
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        button.onClick();
                        button.setBackground(button.isOn()?
                                mContext.getResources().getDrawable(R.drawable.green_round_rect):
                                mContext.getResources().getDrawable(R.drawable.grey_round_rect)
                        );
                    }
                });

                selectButtonArray.add(button);
            }
        }


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
        mChart.getDescription().setEnabled(false);
        mChart.setTouchEnabled(false);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setPinchZoom(false);
        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(8f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(15f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMaximum(yAxisRange);
        leftAxis.setAxisMinimum(-yAxisRange);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(true);
        mChart.getAxisRight().setEnabled(false);

        setData(detectionData);

        // add data
        mChart.animateX(2500);
        mChart.getLegend().setEnabled(false);

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


        switch (score){
            case 0:
                arrowIndicator0.setVisibility(View.VISIBLE);
                arrowIndicator1.setVisibility(View.GONE);
                arrowIndicator2.setVisibility(View.GONE);
                arrowIndicator3.setVisibility(View.GONE);
                arrowIndicator4.setVisibility(View.GONE);
                arrowIndicator5.setVisibility(View.GONE);
                break;
            case 1:
                arrowIndicator0.setVisibility(View.GONE);
                arrowIndicator1.setVisibility(View.VISIBLE);
                arrowIndicator2.setVisibility(View.GONE);
                arrowIndicator3.setVisibility(View.GONE);
                arrowIndicator4.setVisibility(View.GONE);
                arrowIndicator5.setVisibility(View.GONE);
                break;
            case 2:
                arrowIndicator0.setVisibility(View.GONE);
                arrowIndicator1.setVisibility(View.GONE);
                arrowIndicator2.setVisibility(View.VISIBLE);
                arrowIndicator3.setVisibility(View.GONE);
                arrowIndicator4.setVisibility(View.GONE);
                arrowIndicator5.setVisibility(View.GONE);
                break;
            case 3:
                arrowIndicator0.setVisibility(View.GONE);
                arrowIndicator1.setVisibility(View.GONE);
                arrowIndicator2.setVisibility(View.GONE);
                arrowIndicator3.setVisibility(View.VISIBLE);
                arrowIndicator4.setVisibility(View.GONE);
                arrowIndicator5.setVisibility(View.GONE);
                break;
            case 4:
                arrowIndicator0.setVisibility(View.GONE);
                arrowIndicator1.setVisibility(View.GONE);
                arrowIndicator2.setVisibility(View.GONE);
                arrowIndicator3.setVisibility(View.GONE);
                arrowIndicator4.setVisibility(View.VISIBLE);
                arrowIndicator5.setVisibility(View.GONE);
                break;
            case 5:
                arrowIndicator0.setVisibility(View.GONE);
                arrowIndicator1.setVisibility(View.GONE);
                arrowIndicator2.setVisibility(View.GONE);
                arrowIndicator3.setVisibility(View.GONE);
                arrowIndicator4.setVisibility(View.GONE);
                arrowIndicator5.setVisibility(View.VISIBLE);
                break;
            default:
                arrowIndicator0.setVisibility(View.GONE);
                arrowIndicator1.setVisibility(View.GONE);
                arrowIndicator2.setVisibility(View.GONE);
                arrowIndicator3.setVisibility(View.GONE);
                arrowIndicator4.setVisibility(View.GONE);
                arrowIndicator5.setVisibility(View.GONE);
                break;
        }
    }



    private void setData(ArrayList<Entry> inputData) {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        if (inputData!=null){
            if (inputData.size() <= 0){
                // illegal detection data
                return;
            }
            yVals1 = inputData;
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
            set1 = new LineDataSet(yVals1, null);

            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setCircleColor(Color.TRANSPARENT);
            set1.setLineWidth(20f);
            set1.setCircleRadius(0f);
            set1.setFillAlpha(0);
            set1.setDrawCircleHole(false);
            set1.setDrawFilled(true);
            //set1.setFormLineDashEffect(new DashPathEffect(new float[]{20f, 20f}, 0f));
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setValueTextColor(Color.TRANSPARENT);

            // create a data object with the datasets
            LineData data = new LineData(set1);

            // set data
            mChart.setData(data);


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


    /**
     *
     * Enable dismiss keyboard when touch out-side of the edit box
     *
     * */
    public void keyboardAdvance(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    Util.hideSoftKeyboard((Activity) mContext);
                    return false;
                }

            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                keyboardAdvance(innerView);
            }
        }
    }
}
