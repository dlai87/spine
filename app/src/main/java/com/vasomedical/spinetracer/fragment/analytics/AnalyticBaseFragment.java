package com.vasomedical.spinetracer.fragment.analytics;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
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

import com.github.mikephil.charting.data.Entry;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.fragment.BaseFragment;
import com.vasomedical.spinetracer.util.Util;
import com.vasomedical.spinetracer.util.widget.button.OnOffButton;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by dehualai on 9/17/17.
 */

public abstract class AnalyticBaseFragment extends BaseFragment {

    protected ArrayList<Entry> detectionData;
    public static final String SCORE = "SCORE";
    protected int score = -1;

    // UI elements
    protected View view;
    // the whole scroll view
    protected LinearLayout invalidDetectionLayout;
    protected ScrollView validLayout;
    Button reTestButton;

    // indicator
    protected ImageView arrowIndicator0;
    protected ImageView arrowIndicator1;
    protected ImageView arrowIndicator2;
    protected ImageView arrowIndicator3;
    protected ImageView arrowIndicator4;
    protected ImageView arrowIndicator5;
    // doctor suggestion
    protected ArrayList<String> suggestion;
    protected boolean suggestionInitFlag ;
    ArrayList<OnOffButton> selectButtonArray = new ArrayList<OnOffButton>();
    LinearLayout list1Layout;
    LinearLayout list2Layout;
    LinearLayout list3Layout;
    // control buttons
    Button cancelButton;
    Button saveButton;
    Button pdfButton;




    public void setDetectionData(ArrayList<Entry> data){
        detectionData = data;
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

        arrowIndicator0 = (ImageView)view.findViewById(R.id.arrow_indicator_0);
        arrowIndicator1 = (ImageView)view.findViewById(R.id.arrow_indicator_1);
        arrowIndicator2 = (ImageView)view.findViewById(R.id.arrow_indicator_2);
        arrowIndicator3 = (ImageView)view.findViewById(R.id.arrow_indicator_3);
        arrowIndicator4 = (ImageView)view.findViewById(R.id.arrow_indicator_4);
        arrowIndicator5 = (ImageView)view.findViewById(R.id.arrow_indicator_5);

        list1Layout = (LinearLayout)view.findViewById(R.id.list1);
        list2Layout = (LinearLayout)view.findViewById(R.id.list2);
        list3Layout = (LinearLayout)view.findViewById(R.id.list3);

        cancelButton = (Button)view.findViewById(R.id.cancel_button);
        saveButton = (Button)view.findViewById(R.id.save_button);
        pdfButton = (Button)view.findViewById(R.id.pdf_button);

    }

    protected void addActionToViews(){

        displayScore();

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


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        pdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    protected void displayScore(){
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
