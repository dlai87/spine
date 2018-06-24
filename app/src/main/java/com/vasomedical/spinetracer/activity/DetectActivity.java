package com.vasomedical.spinetracer.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.fragment.BaseFragment;
import com.vasomedical.spinetracer.fragment.detect.DetectingFragment;
import com.vasomedical.spinetracer.util.fragmentTransation.FragmentUtil;
import com.vasomedical.spinetracer.util.fragmentTransation.IMainAppHandler;


/**
 * Created by dehualai on 6/24/18.
 */

public class DetectActivity extends AppCompatActivity implements IMainAppHandler {

    String TAG = "DetectActivity";

    FragmentUtil fragmentUtil;
    Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        Bundle bundle = getIntent().getExtras();
        BaseFragment fragment = new DetectingFragment();
        fragment.setArguments(bundle);
        fragmentUtil = new FragmentUtil(this);
        fragmentUtil.showFragment(fragment);
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
