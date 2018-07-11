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
import com.vasomedical.spinetracer.fragment.analytics.AnalyticOptBalanceFragment;
import com.vasomedical.spinetracer.fragment.analytics.AnalyticOptForwardBackFragment;
import com.vasomedical.spinetracer.fragment.analytics.AnalyticOptLeftRightFragment;
import com.vasomedical.spinetracer.fragment.analytics.AnalyticOptRotateFragment;
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
public class DetectingAngleFragment extends DetectingBaseFragment {

    static final String TAG = "DetectingAngleFragment";
    int realtime_display_degree = 1;
    int offset = 0 ;


    ImageView imgBg1;
    ImageView imgZhizhen1;
    TextView degreeText;
    TextView explainText;

    String positionAngleText ;
    String negativeAngleText ;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_detecting_angle, container, false);
        Bundle args = getArguments();


        switch (AlgorithmFactory.detectionOption){
            case AlgorithmFactory.DETECT_OPT_4:{
                realtime_display_degree = 1;
                offset = 0 ;
                positionAngleText = "右倾";
                negativeAngleText = "左倾";
                analyticFragment = new AnalyticOptLeftRightFragment();
            }break;
            case AlgorithmFactory.DETECT_OPT_5:{
                realtime_display_degree = 0;
                offset = -90;
                positionAngleText = "前倾";
                negativeAngleText = "后仰";
                analyticFragment = new AnalyticOptForwardBackFragment();
            }break;
            case AlgorithmFactory.DETECT_OPT_6:{
                realtime_display_degree = 2;
                offset = 90 ;
                positionAngleText = "右旋";
                negativeAngleText = "左旋";
                analyticFragment = new AnalyticOptRotateFragment();
            }break;
            case AlgorithmFactory.DETECT_OPT_7:{
                realtime_display_degree = 1;
                offset = 0 ;
                positionAngleText = "右倾";
                negativeAngleText = "左倾";
                analyticFragment = new AnalyticOptBalanceFragment();
            }break;
            default:{

            }break;
        }


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        InspectionRecord.InspectionRecordBuilder builder = new InspectionRecord.InspectionRecordBuilder(timeStamp, // TEMP: use timestamp as id
                timeStamp,
                Global.patientModel,
                Util.getCurrentDoctor(),
                AlgorithmFactory.detectionOption,
                null,
                0,
                "");
        analyticFragment.setRecord(builder.build());
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    protected void assignViews() {
        View celian1 = view.findViewById(R.id.layout_celian);
        imgZhizhen1 = (ImageView) celian1.findViewById(R.id.imgZhizhen);
        imgBg1 = (ImageView) celian1.findViewById(R.id.imgBg);

        degreeText = (TextView) view.findViewById(R.id.degree_text);
        explainText = (TextView) view.findViewById(R.id.explain_text);
    }

    @Override
    protected void addActionToViews() {

    }

    @Override
    protected void logPose(TangoPoseData pose) {

        final float translation[] = pose.getTranslationAsFloats();
        final float orientation[] = pose.getRotationAsFloats();

        final double[] euler = Util.quaternion2Euler(
                orientation[0],
                orientation[1],
                orientation[2],
                orientation[3]);

        try {

            float degree = Util.radianToDegree((float) euler[realtime_display_degree], offset, realtime_display_degree==2);
            updateUI(degree);
        } catch (Exception e) {
            Log.e(TAG, "catch exception in logPose " + e.getMessage());
        }

        if (detection_status == DetectActivity.DETECTION_STATUS.Start){
            poseLog.recordPoseData(pose, null);
        }
    }



    private void updateUI(final float degree){

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                if(degree > 0 ){
                    explainText.setText(positionAngleText);
                }else if(degree < 0){
                    explainText.setText(negativeAngleText);
                }
                degreeText.setText(Math.abs(degree) + mContext.getResources().getString(R.string.degree_mark));
                float pivoY = imgZhizhen1.getHeight() - 40;
                imgZhizhen1.setPivotY(pivoY);
                imgZhizhen1.setRotation(degree);
            }
        });

    }
}