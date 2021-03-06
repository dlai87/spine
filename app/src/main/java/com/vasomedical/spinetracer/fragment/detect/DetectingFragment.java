package com.vasomedical.spinetracer.fragment.detect;

import android.os.Bundle;
import android.os.DropBoxManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.vasomedical.spinetracer.fragment.analytics.AnalyticFragment;
import com.vasomedical.spinetracer.model.PoseLog;
import com.vasomedical.spinetracer.util.Util;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_detecting_new, container, false);
        Bundle args = getArguments();

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    protected void assignViews(){
        controlButton = (NJButton)view.findViewById(R.id.control_button);
        nextButton = (Button)view.findViewById(R.id.next_button);
        previousButton = (Button)view.findViewById(R.id.previous_button);
        realTimeDisplay = (TextView)view.findViewById(R.id.real_time_display);

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
        for (Entry entry : processedData){
            Log.e("show", "entry " + entry.toString());
        }
        AnalyticFragment analyticFragment = new AnalyticFragment();
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
        final StringBuilder stringBuilder = new StringBuilder();

        final float translation[] = pose.getTranslationAsFloats();
        stringBuilder.append("Position: " +
                translation[0] + ", " + translation[1] + ", " + translation[2]);

        final float orientation[] = pose.getRotationAsFloats();
        stringBuilder.append(". Orientation: " +
                orientation[0] + ", " + orientation[1] + ", " +
                orientation[2] + ", " + orientation[3]);


        final double[] euler = Util.quaternion2Euler(orientation[0], orientation[1], orientation[2],orientation[3]);

        //Log.i(TAG, stringBuilder.toString());
        try{
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    float degree = Util.radianToDegree((float) euler[1]);
                    realTimeDisplay.setText( degree + mContext.getResources().getString(R.string.degree_mark));
                }
            });
        }catch (Exception e){

        }

        poseLog.recordPoseData(pose);
    }

}
