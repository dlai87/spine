package com.vasomedical.spinetracer.fragment.analytics;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.database.table.TBDetection;
import com.vasomedical.spinetracer.database.table.TBPose;
import com.vasomedical.spinetracer.database.util.DBAdapter;
import com.vasomedical.spinetracer.fragment.BaseFragment;
import com.vasomedical.spinetracer.fragment.detect.DetectFragment;
import com.vasomedical.spinetracer.model.DoctorModel;
import com.vasomedical.spinetracer.model.PatientModel;
import com.vasomedical.spinetracer.model.Pose;
import com.vasomedical.spinetracer.util.PdfManager;
import com.vasomedical.spinetracer.util.Util;
import com.vasomedical.spinetracer.util.widget.button.NJButton;
import com.vasomedical.spinetracer.util.widget.button.OnOffButton;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by dehualai on 9/17/17.
 */

public abstract class AnalyticBaseFragment extends BaseFragment {



    public static final String SCORE = "SCORE";
    protected ArrayList<Entry> detectionData;
    protected int score = -1;

    // UI elements
    protected View view;
    // the whole scroll view
    protected LinearLayout invalidDetectionLayout;
    protected ScrollView validLayout;
    // score chart
    protected PieChart mScoreChart;
    // doctor suggestion
    protected ArrayList<String> suggestion;
    protected boolean suggestionInitFlag ;


    protected RelativeLayout chartView;


    Button reTestButton;
    ArrayList<OnOffButton> selectButtonArray = new ArrayList<OnOffButton>();
    LinearLayout list1Layout;
    LinearLayout list2Layout;
    LinearLayout list3Layout;
    // control buttons
    NJButton cancelButton;
    Button saveButton;
    Button pdfButton;


    PatientModel patient;

    static int argb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.argb(a, r, g, b);
    }

    public void setDetectionData(ArrayList<Entry> data){
        detectionData = data;
    }

    public void setPatient(PatientModel newPatient) {
        patient = newPatient;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setStepIndicator(2);
        Bundle args = getArguments();
        if (args!=null){
            score = args.getInt(SCORE);
        }
        keyboardAdvance(view);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void assignViews(){

        invalidDetectionLayout = (LinearLayout)view.findViewById(R.id.invalid_detection_layout);
        validLayout = (ScrollView)view.findViewById(R.id.scrollView);
        reTestButton = (Button)view.findViewById(R.id.re_test_button);
        chartView = (RelativeLayout) view.findViewById(R.id.chartView);

        mScoreChart = (PieChart) view.findViewById(R.id.scoreChart);

        if (detectionData == null){
            // illegal detection data
            invalidDetectionLayout.setVisibility(View.VISIBLE);
            validLayout.setVisibility(View.GONE);
            return;
        }

        if (detectionData.size() <= 0){
            // illegal detection data
            invalidDetectionLayout.setVisibility(View.VISIBLE);
            validLayout.setVisibility(View.GONE);
            return;
        }


        list1Layout = (LinearLayout)view.findViewById(R.id.list1);
        list2Layout = (LinearLayout)view.findViewById(R.id.list2);
        list3Layout = (LinearLayout)view.findViewById(R.id.list3);

        cancelButton = (NJButton)view.findViewById(R.id.cancel_button);
        saveButton = (NJButton)view.findViewById(R.id.save_button);
        pdfButton = (NJButton)view.findViewById(R.id.pdf_button);

    }

    protected void addActionToViews(){

        displayScoreChart();

        if (suggestionInitFlag){
            suggestionInitFlag = false;
            for(int i = 0 ; i < suggestion.size(); i++){
                final OnOffButton button = new OnOffButton(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(500, 30);
                params.setMargins(10, 5, 10, 5);
                button.setLayoutParams(params);
                button.setBackground(mContext.getResources().getDrawable(R.drawable.grey_round_rect));
                button.setText(suggestion.get(i));
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


        // re-test button, when input data is invalid, ask to re test
        reTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancelButton.updateTheme(
                mContext.getResources().getColor(R.color.njbutton_cherry_red),
                mContext.getResources().getColor(R.color.njbutton_cherry_red),
                mContext.getResources().getColor(R.color.njbutton_red_pressed)
               );
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDatabase();
            }
        });

        pdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createandDisplayPdf();
            }
        });
    }

    void saveToDatabase() {
        // TEMP: use timestamp as detection id
        Long tsLong = System.currentTimeMillis() / 1000;
        String detectionId = tsLong.toString();
        TBPose tbPose = new TBPose();
        SQLiteDatabase database = DBAdapter.getDatabase(mContext);
        ArrayList<Pose> poses = DetectFragment.poseLog.getPoseList();
        for (Pose pose : poses) {
            tbPose.smartInsert(database, pose, detectionId);
        }

        TBDetection tbDetection = new TBDetection();
        DoctorModel doctor = Util.getCurrentDoctor();
        tbDetection.smartInsert(database, detectionId, detectionId, doctor, patient);
    }

    void displayScoreChart(){

        int count = 4;
        String[] mLabels = {mContext.getResources().getString(R.string.result_score_0),
                mContext.getResources().getString(R.string.result_score_1),
                mContext.getResources().getString(R.string.result_score_2),
                mContext.getResources().getString(R.string.result_score_3)
        };

        ArrayList<int[]> colorSets = new ArrayList<int[]>();
        colorSets.add(new int[]{argb("#ff22ff00"),argb("#23cccc10"),argb("#23ff8000"),argb("#23cc0000")});
        colorSets.add(new int[]{argb("#2322ff00"),argb("#ffcccc10"),argb("#23ff8000"),argb("#23cc0000")});
        colorSets.add(new int[]{argb("#2322ff00"),argb("#23cccc10"),argb("#ffff8000"),argb("#23cc0000")});
        colorSets.add(new int[]{argb("#2322ff00"),argb("#23cccc10"),argb("#23ff8000"),argb("#ffcc0000")});

        mScoreChart.setBackgroundColor(Color.WHITE);

        mScoreChart.setUsePercentValues(true);
        mScoreChart.getDescription().setEnabled(false);
        mScoreChart.setCenterText(generateCenterSpannableText());

        mScoreChart.setDrawHoleEnabled(true);
        mScoreChart.setHoleColor(Color.WHITE);

        mScoreChart.setTransparentCircleColor(Color.WHITE);
        mScoreChart.setTransparentCircleAlpha(110);

        mScoreChart.setHoleRadius(58f);
        mScoreChart.setTransparentCircleRadius(61f);

        mScoreChart.setDrawCenterText(true);

        mScoreChart.setRotationEnabled(false);
        mScoreChart.setHighlightPerTapEnabled(false);

        mScoreChart.setMaxAngle(180f); // HALF CHART
        mScoreChart.setRotationAngle(180f);
        mScoreChart.setCenterTextOffset(0, -20);


        ArrayList<PieEntry> values = new ArrayList<PieEntry>();
        for (int i = 0; i < count; i++) {
            values.add(new PieEntry(25, mLabels[i % mLabels.length]));
        }
        PieDataSet dataSet = new PieDataSet(values, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(colorSets.get(score % colorSets.size()));
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setDrawValues(false);
        mScoreChart.setData(data);

        mScoreChart.invalidate();

        mScoreChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        mScoreChart.getLegend().setEnabled(false);

        // entry label styling
        mScoreChart.setEntryLabelColor(Color.WHITE);
        mScoreChart.setEntryLabelTextSize(18f);
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString(mContext.getResources().getString(R.string.result_score));
        s.setSpan(new RelativeSizeSpan(1.7f), 0, s.length(), 0);
        return s;
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





    public void createandDisplayPdf() {

        PdfManager pdfManager = new PdfManager(mActivity);
        pdfManager.generatePDF(patient,
                chartView,
                "",
                "");

    }
}
