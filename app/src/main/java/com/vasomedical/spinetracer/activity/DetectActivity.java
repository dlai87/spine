package com.vasomedical.spinetracer.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.algorithm.AlgorithmFactory;
import com.vasomedical.spinetracer.fragment.detect.DetectingBaseFragment;
import com.vasomedical.spinetracer.fragment.detect.DetectingFactory;
import com.vasomedical.spinetracer.util.fragmentTransation.FragmentUtil;
import com.vasomedical.spinetracer.util.fragmentTransation.IMainAppHandler;


/**
 * Created by dehualai on 6/24/18.
 */

public class DetectActivity extends AppCompatActivity implements IMainAppHandler {

    String TAG = "DetectActivity";
    //DETECTION_STATUS detectionStatus = DETECTION_STATUS.Init;

    FragmentUtil fragmentUtil;
    Context mContext;
    DetectingBaseFragment fragment;


    public enum DETECTION_STATUS {
        Init,
        Calibrating,
        Start
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        Bundle bundle = getIntent().getExtras();
        int optionSelected = bundle.getInt(AlgorithmFactory.AlgorithmFactoryDetectOption);
        fragment = DetectingFactory.getFragment(optionSelected);
        fragment.setArguments(bundle);
        fragmentUtil = new FragmentUtil(this);
        fragmentUtil.showFragment(fragment);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "key Event " + keyCode);
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) || (keyCode == KeyEvent.KEYCODE_VOLUME_UP)){
            if(fragment == null){
                return false;
            }
            switch (fragment.detection_status){
                case Init:
                //    detectionStatus = shouldCalubrate? DETECTION_STATUS.Calibrating : DETECTION_STATUS.Start;
                    fragment.detection_status = DETECTION_STATUS.Start;
                    fragment.start();
                    break;
                case Calibrating:
                    fragment.detection_status = DETECTION_STATUS.Start;
                    break;
                case Start:
                    fragment.detection_status = DETECTION_STATUS.Init;
                    fragment.stop();
                    break;
            }
        }
        return true;
    }



    /**
     * implement interface IMainAppHandler
     */
    public int getMainFragmentID() {
        return R.id.mainFragment;
    }

    @Override
    public void setStepIndicator(int step) {
    }
}
