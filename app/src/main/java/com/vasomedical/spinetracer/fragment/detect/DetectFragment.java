package com.vasomedical.spinetracer.fragment.detect;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import com.vasomedical.spinetracer.HelloMotionTrackingActivity;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.fragment.BaseFragment;
import com.vasomedical.spinetracer.fragment.view3d.View3dFragment;
import com.vasomedical.spinetracer.model.PoseLog;
import com.vasomedical.spinetracer.util.Util;

import java.util.ArrayList;

/**
 * Created by dehualai on 4/9/17.
 */

public class DetectFragment extends BaseFragment {

    String TAG = "DetectFragment";

    private Tango mTango;
    private TangoConfig mConfig;
    public static PoseLog poseLog;

    Button startButton;
    Button showButton;

    TextView labelx;
    TextView labely;
    TextView labelz;
    TextView labelw;
    TextView labelrx;
    TextView labelry;
    TextView labelrz;
    TextView eulerx;
    TextView eulery;
    TextView eulerz;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_detect, container, false);
        Bundle args = getArguments();

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    protected void assignViews(){
        startButton = (Button)view.findViewById(R.id.startButton);
        showButton = (Button)view.findViewById(R.id.showButton);

        labelx = (TextView)view.findViewById(R.id.labelx);
        labely = (TextView)view.findViewById(R.id.labely);
        labelz = (TextView)view.findViewById(R.id.labelz);
        labelw = (TextView)view.findViewById(R.id.labelw);
        labelrx = (TextView)view.findViewById(R.id.labelrx);
        labelry = (TextView)view.findViewById(R.id.labelry);
        labelrz = (TextView)view.findViewById(R.id.labelrz);
        eulerx = (TextView)view.findViewById(R.id.eulerx);
        eulery = (TextView)view.findViewById(R.id.eulery);
        eulerz = (TextView)view.findViewById(R.id.eulerz);
    }

    @Override
    protected void addActionToViews() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (poseLog != null){
                    fragmentUtil.showFragment(new View3dFragment());
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
                poseLog.save(mActivity);
            } catch (TangoErrorException e) {
                Log.e(TAG, getString(R.string.exception_tango_error), e);
            }
        }
    }


    @Override
    public void onPause(){
        super.onPause();
       // stop();
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
                    labelx.setText("x: " + translation[0]);
                    labely.setText("y: " + translation[1]);
                    labelz.setText("z: " + translation[2]);
                    labelw.setText("w: " + orientation[0]);
                    labelrx.setText("rx: " + orientation[1]);
                    labelry.setText("ry: " + orientation[2]);
                    labelrz.setText("rz: " + orientation[3]);
                    eulerx.setText("eulerx: " + euler[0]);
                    eulery.setText("eulery: " + euler[1]);
                    eulerz.setText("eulerz: " + euler[2]);
                    //labelx.setText(stringBuilder.toString());
                }
            });
        }catch (Exception e){

        }
        poseLog.recordPoseData(pose);
    }
}
