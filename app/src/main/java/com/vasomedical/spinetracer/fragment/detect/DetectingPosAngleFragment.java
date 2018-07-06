package com.vasomedical.spinetracer.fragment.detect;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.atap.tangoservice.TangoPoseData;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.activity.DetectActivity;
import com.vasomedical.spinetracer.fragment.analytics.AnalyticOptSlantFragment;
import com.vasomedical.spinetracer.util.Util;

/**
 * Created by dehualai on 7/4/18.
 */

public class DetectingPosAngleFragment extends DetectingBaseFragment {

    static final String TAG = "PosAngleFragment";

    ImageView imgBg1;
    ImageView imgZhizhen1;
    TextView moveLengthText;
    TextView degreeText;
    float initialMove = Integer.MIN_VALUE;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_detecting_pos_angle, container, false);
        Bundle args = getArguments();

        analyticFragment = new AnalyticOptSlantFragment();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
        initialMove = Integer.MIN_VALUE;
    }

    @Override
    protected void assignViews() {

        View celian1 = view.findViewById(R.id.layout_celian);
        imgZhizhen1 = (ImageView) celian1.findViewById(R.id.imgZhizhen);
        imgBg1 = (ImageView) celian1.findViewById(R.id.imgBg);

        degreeText = (TextView) view.findViewById(R.id.degree_text);
        moveLengthText = (TextView)view.findViewById(R.id.move_length_text);

    }

    @Override
    protected void addActionToViews() {

    }

    @Override
    protected void logPose(TangoPoseData pose){

        final int realtime_display_degree = 1;
        final int offset = 0 ;

        final float translation[] = pose.getTranslationAsFloats();
        final float orientation[] = pose.getRotationAsFloats();

        final double[] euler = Util.quaternion2Euler(
                orientation[0],
                orientation[1],
                orientation[2],
                orientation[3]);

        try {
            float move = 0;
            if(initialMove == Integer.MIN_VALUE){
                initialMove = Util.meterToCM(translation[1], 0, true);
            }else{
                move = Util.meterToCM(translation[1], initialMove, true);
            }
            float degree = Util.radianToDegree((float) euler[realtime_display_degree], offset, realtime_display_degree==2);
            updateUI(degree, move);
        } catch (Exception e) {
            Log.e(TAG, "catch exception in logPose " + e.getMessage());
        }

        if (detection_status == DetectActivity.DETECTION_STATUS.Start){
            poseLog.recordPoseData(pose, null);
        }
    }


    private void updateUI(final float degree, final float move){

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                moveLengthText.setText("" + (move));
                degreeText.setText(Math.abs(degree) + mContext.getResources().getString(R.string.degree_mark));
                float pivoY = imgZhizhen1.getHeight() - 40;
                imgZhizhen1.setPivotY(pivoY);
                imgZhizhen1.setRotation(degree);
            }
        });

    }
}
