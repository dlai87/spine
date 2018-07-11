package com.vasomedical.spinetracer.fragment.analytics;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.SelProjectcAtivity;
import com.vasomedical.spinetracer.activity.TestModeActivity;
import com.vasomedical.spinetracer.database.table.TBDetection;
import com.vasomedical.spinetracer.database.table.TBPose;
import com.vasomedical.spinetracer.database.util.DBAdapter;
import com.vasomedical.spinetracer.dialog.DoctorCommentDialog;
import com.vasomedical.spinetracer.dialog.ReportDialog;
import com.vasomedical.spinetracer.fragment.BaseFragment;
import com.vasomedical.spinetracer.model.InspectionRecord;
import com.vasomedical.spinetracer.model.Pose;
import com.vasomedical.spinetracer.util.Global;
import com.vasomedical.spinetracer.util.Util;
import com.vasomedical.spinetracer.util.widget.button.NJButton;
import com.vasomedical.spinetracer.util.widget.dialog.AlertDialog;

import java.util.ArrayList;

/**
 * Created by dehualai on 6/24/18.
 */

public abstract class AnalyticBaseFragment extends BaseFragment implements DoctorCommentDialog.DoctorCommentInterface {



    public static final String SCORE = "SCORE";
    protected ArrayList<Entry> detectionData;
    protected ArrayList<Entry> trimData;
    protected ArrayList<Entry> filterData;
    protected ArrayList<Pose> rawData;

    protected int score = -1;

    // UI elements
    protected View view;
    // the whole scroll view
    protected LinearLayout invalidDetectionLayout;
    protected ScrollView validLayout;
    // score chart
   // protected PieChart mScoreChart;
    // doctor suggestion
    protected ArrayList<String> suggestion;
    protected boolean suggestionInitFlag ;

    protected RelativeLayout chartView;


    protected TextView textView1;
    protected TextView textView2;

    //Button reTestButton;

    // control buttons
    Button saveButton;
    Button abandonButton;
    Button diagnosisButton;
    InspectionRecord record;

    Spinner spinner;

    DoctorCommentDialog doctorCommentDialog;
    protected String[] doctorComments;
    protected String docComment = "";



    protected abstract void defineDoctorComments();


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
        for (int i =0 ; i < data.size() ; i++){
            Log.e("show", "detection data :::" + data.get(i).getX() + " x " + data.get(i).getY());
        }
    }

    public void setRawData(ArrayList<Pose> rawData){
        this.rawData = rawData;
    }

    public void setTrimData(ArrayList<Entry> data){
        trimData = data;
    }

    public void setFilterData(ArrayList<Entry> data){
        filterData = data;
    }

    public void setRecord(InspectionRecord newRecord) {
        record = newRecord;
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
        textView1 = (TextView) view.findViewById(R.id.tvLabel1);
        textView2 = (TextView) view.findViewById(R.id.tvLabel2);

        diagnosisButton = (Button) view.findViewById(R.id.buttonDiagnosis);
        saveButton = (Button)view.findViewById(R.id.buttonSave);
        abandonButton = (Button)view.findViewById(R.id.buttonAbandon);

        spinner = (Spinner)view.findViewById(R.id.scoreSpinner);


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



        doctorCommentDialog = new DoctorCommentDialog(mContext);
        doctorCommentDialog.setCommentOptions(doctorComments);
        doctorCommentDialog.setDoctorCommentInterface(this);
    }

    protected void addActionToViews(){

        displayScoreChart();

        String[] scoreOptions = {"优", "良", "健康","不健康"};

        ArrayAdapter<CharSequence> dataAdapter = new ArrayAdapter(mContext, R.layout.list_cell, scoreOptions);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                // select_language = item;
                // fixme select socre
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        diagnosisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defineDoctorComments();
                doctorCommentDialog.setCommentOptions(doctorComments);
                doctorCommentDialog.show();
            }
        });

        abandonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, TestModeActivity.class);
                startActivity(intent);
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Global.patientModel == null || Global.userModel == null){
                    Toast.makeText(mContext, "试用模式下不能保存", Toast.LENGTH_SHORT).show();
                    return;
                }
                record.setDoctorComments(docComment);
                saveToDatabase();
            }
        });



    }

    void saveToDatabase() {

        TBPose tbPose = new TBPose();
        SQLiteDatabase database = DBAdapter.getDatabase(mContext);
    //    for (Pose pose : record.getInspectionData()) {
            // FIXME: should be only one write
    //        tbPose.smartInsert(database, pose, record.getId());
    //    }

        record.setDoctorComments(docComment);
        record.setScore(64); // TODO
        TBDetection tbDetection = new TBDetection();
        tbDetection.smartInsert(database, record);


        AlertDialog alertDialog = new AlertDialog(mContext);
        alertDialog.setTitleView("成功");
        alertDialog.setMessageView("测量数据已保存");
        alertDialog.setButtons("好", null, null);
        alertDialog.showDialog();
    }

    void displayScoreChart(){


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



    // Override interface DoctorCommentDialog.DoctorCommentInterface
    @Override
    public void setDocComment(String docComment){
        this.docComment = docComment;
        Log.i("show", "doc comment " + this.docComment);
    }








}
