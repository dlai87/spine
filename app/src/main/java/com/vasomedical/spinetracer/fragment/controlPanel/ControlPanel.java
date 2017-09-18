package com.vasomedical.spinetracer.fragment.controlPanel;

import com.vasomedical.spinetracer.fragment.BaseFragment;
import com.vasomedical.spinetracer.fragment.detect.DetectionOptionsFragment;
import com.vasomedical.spinetracer.fragment.patientInfo.PatientInfoFragment;

/**
 * Created by dehualai on 7/10/17.
 */

public class ControlPanel {

    public static int currentStep = 0 ;

    public static BaseFragment getFragment(){
        BaseFragment fragment = null;
        switch (currentStep){
            case 0:
                fragment = new PatientInfoFragment();
                break;
            case 1:
                fragment = new DetectionOptionsFragment();
                break;
            case 2:
                //fragment = new AnalyticFragment();
                break;
            default:
                break;
        }
        return fragment;
    }

}
