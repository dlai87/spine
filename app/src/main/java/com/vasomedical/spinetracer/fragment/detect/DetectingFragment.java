package com.vasomedical.spinetracer.fragment.detect;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.google.atap.tangoservice.Tango;
import com.google.atap.tangoservice.TangoConfig;
import com.google.atap.tangoservice.TangoCoordinateFramePair;
import com.google.atap.tangoservice.TangoErrorException;
import com.google.atap.tangoservice.TangoEvent;
import com.google.atap.tangoservice.TangoInvalidException;
import com.google.atap.tangoservice.TangoOutOfDateException;
import com.google.atap.tangoservice.TangoPointCloudData;
import com.google.atap.tangoservice.TangoPoseData;
import com.google.atap.tangoservice.TangoXyzIjData;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.algorithm.AlgorithmBase;
import com.vasomedical.spinetracer.algorithm.AlgorithmFactory;
import com.vasomedical.spinetracer.algorithm.filters.BasicOperation;
import com.vasomedical.spinetracer.fragment.BaseFragment;
import com.vasomedical.spinetracer.fragment.analytics.AnalyticBaseFragment;
import com.vasomedical.spinetracer.fragment.analytics.AnalyticOptSegment;
import com.vasomedical.spinetracer.model.PatientModel;
import com.vasomedical.spinetracer.model.Pose;
import com.vasomedical.spinetracer.model.PoseLog;
import com.vasomedical.spinetracer.util.Util;
import com.vasomedical.spinetracer.util.widget.button.NJButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dehualai on 5/17/17.
 */

public class DetectingFragment extends BaseFragment {


    public static PoseLog poseLog;
    String TAG = "DetectingFragment";
    NJButton controlButton;
    Button previousButton;
    Button nextButton;
    TextView instructionText;
    TextView realTimeDisplay;
    RelativeLayout angleRulerLayout;
    ImageView indicator;
    LinearLayout movementLayout;
    TextView verticalMoveText;
    TextView horizontalMoveText;
    PatientModel patient;
    TangoPoseData initPose = null;
    float translationInit[] = new float[3];
    float orientationInit[] = new float[4];
    DETECTION_STATUS detectionStatus = DETECTION_STATUS.Init;
    int realtime_display_mode = 1;  // 0 = angle mode ;  1 = position mode
    int realtime_display_degree = 1;  // which degree to display in real time,
    //private boolean isDetctiong = false;
    // 0 = rotation x;  1 = rotation y; 2 = rotation z
    int realtime_display_horizontal_axis = 1; // 0 = x Axis; 1 = y Axis
    AnalyticBaseFragment analyticFragment;
    private Tango mTango;
    private TangoConfig mConfig;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_detecting_new, container, false);
        Bundle args = getArguments();

        // Fixme : clean data
        {
       //     TBDataProcessed.clean(DBAdapter.getDatabase(mContext));
       //     TBMotion.clean(DBAdapter.getDatabase(mContext));
       //     TBPose.clean(DBAdapter.getDatabase(mContext));
       //     TBDetection.clean(DBAdapter.getDatabase(mContext));

        }

        /*
        switch (AlgorithmFactory.detectionOption){
            case AlgorithmFactory.DETECT_OPT_1:{
                realtime_display_mode = 0 ;
                realtime_display_degree = 1;
                analyticFragment = new AnalyticOptSlantFragment();
            }break;
            case AlgorithmFactory.DETECT_OPT_2:{
                realtime_display_mode = 1;
                realtime_display_horizontal_axis = 1;
                analyticFragment = new AnalyticOptHumpbackFragment();
            }break;
            case AlgorithmFactory.DETECT_OPT_3:{
                realtime_display_mode = 1;
                realtime_display_horizontal_axis = 0;
                analyticFragment = new AnalyticOptSegment();

            }break;
            case AlgorithmFactory.DETECT_OPT_4:{
                realtime_display_mode = 0 ;
                realtime_display_degree = 1;
                analyticFragment = new AnalyticOptLeftRightFragment();

            }break;
            case AlgorithmFactory.DETECT_OPT_5:{
                realtime_display_mode = 0 ;
                realtime_display_degree = 0;
                analyticFragment = new AnalyticOptForwardBackFragment();
            }break;
            case AlgorithmFactory.DETECT_OPT_6:{
                realtime_display_mode = 0 ;
                realtime_display_degree = 2;
                analyticFragment = new AnalyticOptRotateFragment();
            }break;
            case AlgorithmFactory.DETECT_OPT_7:{
                realtime_display_mode = 0 ;
                realtime_display_degree = 1;
                analyticFragment = new AnalyticOptBalanceFragment();
            }break;
            default:{

            }break;
        }

*/

        realtime_display_mode = 1;
        realtime_display_horizontal_axis = 0;
        analyticFragment = new AnalyticOptSegment();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void updateUI(){
        switch (detectionStatus){
            case Init:
                controlButton.setText(mContext.getResources().getString(R.string.start));
                instructionText.setVisibility(View.GONE);
                break;
            case Calibrating:
                controlButton.setText(mContext.getResources().getString(R.string.start));
                instructionText.setVisibility(View.VISIBLE);
                break;
            case Start:
                controlButton.setText(mContext.getResources().getString(R.string.stop));
                instructionText.setVisibility(View.GONE);
                break;

        }
    }

    public void setPatient(PatientModel newPatient) {
        patient = newPatient;
    }

    @Override
    protected void assignViews() {
        controlButton = (NJButton) view.findViewById(R.id.control_button);
        nextButton = (Button) view.findViewById(R.id.next_button);
        previousButton = (Button) view.findViewById(R.id.previous_button);
        realTimeDisplay = (TextView) view.findViewById(R.id.real_time_display);
        angleRulerLayout = (RelativeLayout) view.findViewById(R.id.angleRulerLayout);
        indicator = (ImageView) view.findViewById(R.id.indicator);
        movementLayout = (LinearLayout) view.findViewById(R.id.movementLayout);
        verticalMoveText = (TextView) view.findViewById(R.id.vertical_move_text);
        horizontalMoveText = (TextView) view.findViewById(R.id.horizontal_move_text);
        instructionText = (TextView)view.findViewById(R.id.instruction_text);
    }

    @Override
    protected void addActionToViews() {

        updateUI();

        controlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (detectionStatus){
                    case Init:
                        // fixme : calibrating or not 
                        /*
                        if (AlgorithmFactory.detectionOption == AlgorithmFactory.DETECT_OPT_2
                                ||AlgorithmFactory.detectionOption == AlgorithmFactory.DETECT_OPT_3)
                        {
                            detectionStatus = DETECTION_STATUS.Calibrating;
                        }else {
                            detectionStatus = DETECTION_STATUS.Start;
                        }*/
                        detectionStatus = DETECTION_STATUS.Calibrating;
                        start();
                        break;
                    case Calibrating:
                        detectionStatus = DETECTION_STATUS.Start;
                        break;
                    case Start:
                        detectionStatus = DETECTION_STATUS.Init;
                        initPose = null;
                        translationInit[0] = 0;
                        translationInit[1] = 0;
                        translationInit[2] = 0;
                        orientationInit[0] = 0;
                        orientationInit[1] = 0;
                        orientationInit[2] = 0;
                        orientationInit[3] = 0;
                        stop();
                        break;

                }

                updateUI();

            }
        });

        switch (realtime_display_mode) {
            case 0: {
                try {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            angleRulerLayout.setVisibility(View.VISIBLE);
                            movementLayout.setVisibility(View.GONE);
                            drawIndicator(0);
                        }
                    });
                } catch (Exception e) {

                }
            }
            break;
            case 1: {
                try {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            angleRulerLayout.setVisibility(View.GONE);
                            movementLayout.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (Exception e) {

                }
            }
            break;
            default:
                break;
        }

    }

    private void start() {
        // Initialize Tango Service as a normal Android Service. Since we call mTango.disconnect()
        // in onPause, this will unbind Tango Service, so every time onResume gets called we
        // should create a new Tango object.
        mTango = new Tango(mActivity, new Runnable() {
            // Pass in a Runnable to be called from UI thread when Tango is ready; this Runnable
            // will be running on a new thread.
            // When Tango is ready, we can call Tango functions safely here only when there are no
            // UI thread changes involved.
            @Override
            public void run() {
                synchronized (mActivity) {
                    try {
                        mConfig = setupTangoConfig(mTango);
                        mTango.connect(mConfig);
                        startupTango();
                    } catch (TangoOutOfDateException e) {
                        Log.e(TAG, getString(R.string.exception_out_of_date), e);
                    } catch (TangoErrorException e) {
                        Log.e(TAG, getString(R.string.exception_tango_error), e);
                    } catch (TangoInvalidException e) {
                        Log.e(TAG, getString(R.string.exception_tango_invalid), e);
                    }
                }
            }
        });
        poseLog = new PoseLog();
    }


    ////  Tango

    private void stop() {
        synchronized (this) {
            try {
                mTango.disconnect();
            } catch (TangoErrorException e) {
                Log.e(TAG, getString(R.string.exception_tango_error), e);
            }
        }

        AlgorithmFactory algorithmFactory = new AlgorithmFactory();
        AlgorithmBase algorithm = algorithmFactory.getAlgorithm(AlgorithmFactory.detectionOption);
        ArrayList<Entry> processedData = algorithm.processData(poseLog.getPoseList());

        BasicOperation basicOperation = new BasicOperation();
        ArrayList<Pose> list = basicOperation.removeHeadAndTale(poseLog.getPoseList(), 0.05f, 0.05f);
        ArrayList<Entry> trimProcessedData = algorithm.processData(list);
        ArrayList<Pose> listFilterData = basicOperation.midianFilter(poseLog.getPoseList());
        ArrayList<Entry> filterData = algorithm.processData(listFilterData);




        Bundle args = new Bundle();
        args.putInt(AnalyticBaseFragment.SCORE, algorithm.getScore());
        analyticFragment.setArguments(args);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        /*
        InspectionRecord.InspectionRecordBuilder builder = new InspectionRecord.InspectionRecordBuilder(timeStamp, // TEMP: use timestamp as id
                timeStamp,
                patient,
                Util.getCurrentDoctor(),
                AlgorithmFactory.detectionOption,
                poseLog.getPoseList(),
                0,
                "");
                */
        analyticFragment.setRawData(list);
        analyticFragment.setDetectionData(processedData);
        analyticFragment.setTrimData(trimProcessedData);
        analyticFragment.setFilterData(filterData);
        //analyticFragment.setRecord(builder.build());

        fragmentUtil.showFragment(analyticFragment);
        // poseLog.save(mActivity);

    }

    /**
     * Sets up the tango configuration object. Make sure mTango object is initialized before
     * making this call.
     */
    private TangoConfig setupTangoConfig(Tango tango) {
        // Create a new Tango Configuration and enable the HelloMotionTrackingActivity API.
        TangoConfig config = tango.getConfig(TangoConfig.CONFIG_TYPE_DEFAULT);
        config.putBoolean(TangoConfig.KEY_BOOLEAN_MOTIONTRACKING, true);

        // Tango Service should automatically attempt to recover when it enters an invalid state.
        config.putBoolean(TangoConfig.KEY_BOOLEAN_AUTORECOVERY, true);
        return config;
    }

    /**
     * Set up the callback listeners for the Tango Service and obtain other parameters required
     * after Tango connection.
     * Listen to new Pose data.
     */
    private void startupTango() {
        // Lock configuration and connect to Tango.
        // Select coordinate frame pair.
        final ArrayList<TangoCoordinateFramePair> framePairs =
                new ArrayList<TangoCoordinateFramePair>();
        framePairs.add(new TangoCoordinateFramePair(
                TangoPoseData.COORDINATE_FRAME_START_OF_SERVICE,
                TangoPoseData.COORDINATE_FRAME_DEVICE));

        // Listen for new Tango data.
        mTango.connectListener(framePairs, new Tango.OnTangoUpdateListener() {
            @Override
            public void onPoseAvailable(final TangoPoseData pose) {
                // NJProgressDialog.dismiss();
                logPose(pose);
            }

            @Override
            public void onXyzIjAvailable(TangoXyzIjData xyzIj) {
                // We are not using onXyzIjAvailable for this app.
            }

            @Override
            public void onPointCloudAvailable(TangoPointCloudData pointCloud) {
                // We are not using onPointCloudAvailable for this app.
            }

            @Override
            public void onTangoEvent(final TangoEvent event) {
                // Ignoring TangoEvents.
            }

            @Override
            public void onFrameAvailable(int cameraId) {
                // We are not using onFrameAvailable for this application.
            }
        });
    }

    /**
     * Log the Position and Orientation of the given pose in the Logcat as information.
     *
     * @param pose the pose to log.
     */
    private void logPose(TangoPoseData pose) {


        final float translation[] = pose.getTranslationAsFloats();
        final float orientation[] = pose.getRotationAsFloats();

        final double[] euler = Util.quaternion2Euler(
                orientation[0],
                orientation[1],
                orientation[2],
                orientation[3]);

        try {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (realtime_display_mode){
                        case 0:{
                            int offset = 0 ;
                            switch (realtime_display_degree){
                                case 0:
                                    offset = -90;
                                    break;
                                case 1:
                                    offset = 0 ;
                                    break;
                                case 2:
                                    offset = 90 ;
                                    break;
                            }
                            float degree = Util.radianToDegree((float) euler[realtime_display_degree], offset, realtime_display_degree==2);
                            realTimeDisplay.setText(Math.abs(degree) + mContext.getResources().getString(R.string.degree_mark));
                            drawIndicator(degree);
                        }break;
                        case 1:{
                            verticalMoveText.setText(mContext.getResources().getString(R.string.vertical_move) + " : " + Util.positionToDisplay(-translation[2] - (-translationInit[2]) ) + "cm");
                            horizontalMoveText.setText(mContext.getResources().getString(R.string.horizontal_move) + " : " +   Util.positionToDisplay(translation[realtime_display_horizontal_axis] - translationInit[realtime_display_horizontal_axis]) + "cm");
                        }break;
                        default:
                            break;
                    }

                }
            });
        } catch (Exception e) {

        }





        if (detectionStatus == DETECTION_STATUS.Start){
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

    private void drawIndicator(float degree) {

        float layoutW = angleRulerLayout.getWidth();
        float layoutH = angleRulerLayout.getHeight();
        float indicatorR = indicator.getWidth() / 2;

        float radius = layoutH - indicatorR;
        float tempX = (float) (Math.sin(Math.toRadians(Math.abs(degree))) * radius);

        if (degree < 0) {
            indicator.setX(layoutW / 2 - tempX - indicatorR);
        } else {
            indicator.setX(layoutW / 2 + tempX - indicatorR);
        }

        float tempY = (float) (Math.cos(Math.toRadians(Math.abs(degree))) * radius);

        indicator.setY(tempY - indicatorR);

    }


    enum DETECTION_STATUS {
        Init,
        Calibrating,
        Start
    }


}
