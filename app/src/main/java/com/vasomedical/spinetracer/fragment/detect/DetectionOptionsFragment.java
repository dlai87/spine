package com.vasomedical.spinetracer.fragment.detect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.algorithm.AlgorithmFactory;
import com.vasomedical.spinetracer.fragment.BaseFragment;
import com.vasomedical.spinetracer.util.widget.button.NJButton;

/**
 * Created by dehualai on 5/14/17.
 */

public class DetectionOptionsFragment extends BaseFragment {




    NJButton optionButton1;
    NJButton optionButton2;
    NJButton optionButton3;
    NJButton optionButton4;
    NJButton optionButton5;

    Button nextButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_detect_options_new, container, false);
        Bundle args = getArguments();

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    protected void assignViews(){
        optionButton1 = (NJButton)view.findViewById(R.id.option_button_1);
        optionButton2 = (NJButton)view.findViewById(R.id.option_button_2);
        optionButton3 = (NJButton)view.findViewById(R.id.option_button_3);
        optionButton4 = (NJButton)view.findViewById(R.id.option_button_4);
        optionButton5 = (NJButton)view.findViewById(R.id.option_button_5);
        nextButton = (Button)view.findViewById(R.id.next_button);
    }

    @Override
    protected void addActionToViews() {
        optionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlgorithmFactory.detectionOption = AlgorithmFactory.DETECT_OPT_1;
                refresh();
            }
        });

        optionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlgorithmFactory.detectionOption = AlgorithmFactory.DETECT_OPT_2;
                refresh();
            }
        });

        optionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlgorithmFactory.detectionOption = AlgorithmFactory.DETECT_OPT_3;
                refresh();
            }
        });

        optionButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlgorithmFactory.detectionOption = AlgorithmFactory.DETECT_OPT_4;
                refresh();
            }
        });

        optionButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlgorithmFactory.detectionOption = AlgorithmFactory.DETECT_OPT_5;
                refresh();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentUtil.showFragment(new DetectingFragment());
            }
        });
    }


    private void refresh(){

        nextButton.setVisibility(AlgorithmFactory.detectionOption==0?View.GONE:View.VISIBLE);
        optionButton1.setButtonTheme(NJButton.THEME_DEFAULT);
        optionButton2.setButtonTheme(NJButton.THEME_DEFAULT);
        optionButton3.setButtonTheme(NJButton.THEME_DEFAULT);
        optionButton4.setButtonTheme(NJButton.THEME_DEFAULT);
        optionButton5.setButtonTheme(NJButton.THEME_DEFAULT);

        switch (AlgorithmFactory.detectionOption){
            case AlgorithmFactory.DETECT_OPT_1:
                optionButton1.setButtonTheme(NJButton.THEME_INVERSE_DEFAULT);
                break;
            case AlgorithmFactory.DETECT_OPT_2:
                optionButton2.setButtonTheme(NJButton.THEME_INVERSE_DEFAULT);
                break;
            case AlgorithmFactory.DETECT_OPT_3:
                optionButton3.setButtonTheme(NJButton.THEME_INVERSE_DEFAULT);
                break;
            case AlgorithmFactory.DETECT_OPT_4:
                optionButton4.setButtonTheme(NJButton.THEME_INVERSE_DEFAULT);
                break;
            case AlgorithmFactory.DETECT_OPT_5:
                optionButton5.setButtonTheme(NJButton.THEME_INVERSE_DEFAULT);
                break;
        }

    }


}
