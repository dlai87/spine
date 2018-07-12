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
import com.vasomedical.spinetracer.algorithm.AlgorithmFactory;
import com.vasomedical.spinetracer.fragment.BaseFragment;
import com.vasomedical.spinetracer.fragment.analytics.AnalyticOptSegment;
import com.vasomedical.spinetracer.fragment.analytics.AnalyticOptSlantFragment;
import com.vasomedical.spinetracer.model.InspectionRecord;
import com.vasomedical.spinetracer.util.Global;
import com.vasomedical.spinetracer.util.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dehualai on 7/5/18.
 */

public class DetectingPosPosFragment extends DetectingBaseFragment {

    static final String TAG = "PosPosFragment";

    ImageView imgBg1;
    ImageView imgZhizhen1;
    TextView moveLengthText;
    TextView degreeText;
    TextView calibrateText;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_detecting_pos_pos, container, false);
        Bundle args = getArguments();

        analyticFragment = new AnalyticOptSegment();

        String timeStamp = new SimpleDateFormat(Util.FORMAT_DATE_TIME).format(new Date());
        InspectionRecord.InspectionRecordBuilder builder = new InspectionRecord.InspectionRecordBuilder(timeStamp, // TEMP: use timestamp as id
                timeStamp,
                Global.patientModel,
                Global.userModel,
                AlgorithmFactory.detectionOption,
                null,
                "",
                "");
        analyticFragment.setRecord(builder.build());

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onResume(){
        super.onResume();
        shouldCalubrate = true;
    }


    @Override
    protected void assignViews() {
        View celian2 = view.findViewById(R.id.layout_celian2);
        imgZhizhen1 = (ImageView) celian2.findViewById(R.id.imgZhizhen);
        imgBg1 = (ImageView) celian2.findViewById(R.id.imgBg);

        degreeText = (TextView) view.findViewById(R.id.degree_text);
        moveLengthText = (TextView)view.findViewById(R.id.move_length_text);
        calibrateText = (TextView) view.findViewById(R.id.status_message);
    }

    @Override
    protected void addActionToViews() {

    }

    @Override
    protected void logPose(TangoPoseData pose){


        final float translation[] = pose.getTranslationAsFloats();
        final float orientation[] = pose.getRotationAsFloats();

        final double[] euler = Util.quaternion2Euler(
                orientation[0],
                orientation[1],
                orientation[2],
                orientation[3]);

        try {

            float move = Util.meterToCM(translation[1], -translationInit[1], true);
            float degree = Util.meterToCM(translation[0], -translationInit[0], true);


            updateUI(degree, move);


        } catch (Exception e) {
            Log.e(TAG, "catch exception in logPose " + e.getMessage());
        }

        if (detection_status == DetectActivity.DETECTION_STATUS.Start){
            if (initPose==null){
                initPose = pose;
                float arrTrans[] = initPose.getTranslationAsFloats();
                float arrOrient[] = initPose.getRotationAsFloats();
                translationInit[0] = arrTrans[0];
                translationInit[1] = arrTrans[1];
                translationInit[2] = arrTrans[2];
                orientationInit[0] = arrOrient[0];
                orientationInit[1] = arrOrient[1];
                orientationInit[2] = arrOrient[2];
                orientationInit[3] = arrOrient[3];
            }
            poseLog.recordPoseData(pose, null);
        }



    }


    private void updateUI(final float degree, final float move){

        mActivity.runOnUiThread(new Runnable() {
            public void run() {

                if(detection_status == DetectActivity.DETECTION_STATUS.Init){
                    calibrateText.setText("准备校准");
                }else if(detection_status == DetectActivity.DETECTION_STATUS.Calibrating){
                    calibrateText.setText("校准完成，请再按一次控制按钮开始测量");
                }else{
                    calibrateText.setText("开始测量");
                }
                moveLengthText.setText("" + (move));
                degreeText.setText(Math.abs(degree) + "");
                float adjustDegree  = -degree;
                adjustDegree = 2*adjustDegree;
                adjustDegree = adjustDegree + 90;
                float x = imgBg1.getWidth() * adjustDegree / 180 - (imgZhizhen1.getWidth() / 2) + imgBg1.getX();
                imgZhizhen1.setX(x);
            }
        });

    }
}