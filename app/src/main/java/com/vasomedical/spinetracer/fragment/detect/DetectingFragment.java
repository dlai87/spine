package com.vasomedical.spinetracer.fragment.detect;

import android.os.Bundle;
import android.os.DropBoxManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.vasomedical.spinetracer.fragment.BaseFragment;
import com.vasomedical.spinetracer.fragment.analytics.AnalyticBaseFragment;
import com.vasomedical.spinetracer.fragment.analytics.AnalyticFragment;
import com.vasomedical.spinetracer.fragment.analytics.AnalyticOpt1Fragment;
import com.vasomedical.spinetracer.fragment.analytics.AnalyticOpt2Fragment;
import com.vasomedical.spinetracer.model.PoseLog;
import com.vasomedical.spinetracer.util.Util;
import com.vasomedical.spinetracer.util.widget.angleRule.AngleRulerLayout;
import com.vasomedical.spinetracer.util.widget.button.NJButton;
import com.vasomedical.spinetracer.util.widget.progressDialog.NJProgressDialog;

import java.util.ArrayList;

/**
 * Created by dehualai on 5/17/17.
 */

public class DetectingFragment extends BaseFragment {

    String TAG = "DetectingFragment";
    private Tango mTango;
    private TangoConfig mConfig;
    public static PoseLog poseLog;

    private boolean isDetctiong = false;

    NJButton controlButton;
    Button previousButton;
    Button nextButton;
    TextView realTimeDisplay;
    RelativeLayout angleRulerLayout;
    ImageView indicator;


    public static final String REALTIME_DISPLAY_DEGREE = "REALTIME_DISPLAY_DEGREE";
    int realtime_display_degree = 1;  // which degree to display in real time,
                                      // 0 = rotation x;  1 = rotation y; 2 = rotation z


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_detecting_new, container, false);
        Bundle args = getArguments();
        if (args!=null){
            realtime_display_degree = args.getInt(REALTIME_DISPLAY_DEGREE, 1);
        }


        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    protected void assignViews(){
        controlButton = (NJButton)view.findViewById(R.id.control_button);
        nextButton = (Button)view.findViewById(R.id.next_button);
        previousButton = (Button)view.findViewById(R.id.previous_button);
        realTimeDisplay = (TextView)view.findViewById(R.id.real_time_display);
        angleRulerLayout = (RelativeLayout)view.findViewById(R.id.angleRulerLayout);
        indicator = (ImageView)view.findViewById(R.id.indicator);

    }

    @Override
    protected void addActionToViews() {
        controlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDetctiong){
                    isDetctiong = true;
                    start();
                    controlButton.setText(mContext.getResources().getString(R.string.stop));
                 //   NJProgressDialog.showDialog(mContext);
                }else {
                    isDetctiong = false;
                    stop();
                    controlButton.setText(mContext.getResources().getString(R.string.start));
                }

            }
        });

        try{
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    drawIndicator(0);
                }
            });
        }catch (Exception e){

        }
    }


    ////  Tango

    private void start(){
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


    private void stop(){
        synchronized (this) {
            try {
                mTango.disconnect();
            } catch (TangoErrorException e) {
                Log.e(TAG, getString(R.string.exception_tango_error), e);
            }
        }

        AlgorithmFactory algorithmFactory = new AlgorithmFactory();
        AlgorithmBase algorithm = algorithmFactory.getAlgorithm(AlgorithmFactory.DETECT_OPT_1);
        ArrayList<Entry> processedData = algorithm.processData(poseLog.getPoseList());

        AnalyticBaseFragment analyticFragment = new AnalyticOpt2Fragment();
        Bundle args = new Bundle();
        args.putInt(AnalyticFragment.SCORE, algorithm.getScore());
        analyticFragment.setArguments(args);
        analyticFragment.setDetectionData(processedData);

        fragmentUtil.showFragment(analyticFragment);

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
        final double[] euler = Util.quaternion2Euler(orientation[0], orientation[1], orientation[2],orientation[3]);

        try{
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    float degree = Util.radianToDegree((float) euler[realtime_display_degree]);
                    realTimeDisplay.setText( Math.abs(degree) + mContext.getResources().getString(R.string.degree_mark));
                    drawIndicator(degree);
                }
            });
        }catch (Exception e){

        }
        poseLog.recordPoseData(pose);
    }


    private void drawIndicator(float degree){

        float layoutW = angleRulerLayout.getWidth();
        float layoutH = angleRulerLayout.getHeight();
        float indicatorR = indicator.getWidth()/2;

        float radius = layoutH - indicatorR;
        float tempX =  (float) (Math.sin( Math.toRadians(Math.abs(degree)) ) * radius) ;

        if (degree < 0 ){
            indicator.setX( layoutW/2-tempX - indicatorR);
        }else {
            indicator.setX( layoutW/2+tempX - indicatorR);
        }

        float tempY =  (float) (Math.cos( Math.toRadians(Math.abs(degree)) ) * radius) ;

        indicator.setY(tempY - indicatorR);

    }



}
