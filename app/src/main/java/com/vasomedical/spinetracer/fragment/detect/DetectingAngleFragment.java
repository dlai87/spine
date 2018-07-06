package com.vasomedical.spinetracer.fragment.detect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.atap.tangoservice.TangoPoseData;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.fragment.BaseFragment;

/**
 * Created by dehualai on 7/5/18.
 */
public class DetectingAngleFragment extends DetectingBaseFragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_detecting_angle, container, false);
        Bundle args = getArguments();

        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
    protected void assignViews() {

    }

    @Override
    protected void addActionToViews() {

    }

    @Override
    protected void logPose(TangoPoseData pose) {

    }
}