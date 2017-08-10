package com.vasomedical.spinetracer.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;


import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.fragment.controlPanel.ControlPanel;
import com.vasomedical.spinetracer.util.fragmentTransation.FragmentUtil;
import com.vasomedical.spinetracer.util.fragmentTransation.IMainAppHandler;

/**
 * Created by dehualai on 1/20/17.
 */

public abstract class BaseFragment extends Fragment {


    protected View view;

    protected Context mContext;
    protected Activity mActivity;
    protected FragmentUtil fragmentUtil;
    protected IMainAppHandler mainAppHandler;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        mActivity = activity;
        fragmentUtil = new FragmentUtil(activity);
        if(activity instanceof IMainAppHandler){
            this.mainAppHandler = (IMainAppHandler) activity;
        } else {
            Log.e("Exception", "The input activity must implement interface IMainAppHandler");
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        assignViews();
        addActionToViews();

        return view;
    }

    protected void setStepIndicator(int step){
        ControlPanel.currentStep = step;
        mainAppHandler.setStepIndicator(step);
    }


    protected abstract void assignViews();

    protected abstract void addActionToViews();



}
