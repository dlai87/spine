/*
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vasomedical.spinetracer;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.atap.tangoservice.Tango;
import com.google.atap.tangoservice.Tango.OnTangoUpdateListener;
import com.google.atap.tangoservice.TangoConfig;
import com.google.atap.tangoservice.TangoCoordinateFramePair;
import com.google.atap.tangoservice.TangoErrorException;
import com.google.atap.tangoservice.TangoEvent;
import com.google.atap.tangoservice.TangoInvalidException;
import com.google.atap.tangoservice.TangoOutOfDateException;
import com.google.atap.tangoservice.TangoPointCloudData;
import com.google.atap.tangoservice.TangoPoseData;
import com.google.atap.tangoservice.TangoXyzIjData;
import com.vasomedical.spinetracer.database.util.DBAdapter;
import com.vasomedical.spinetracer.model.MedianFilter;
import com.vasomedical.spinetracer.model.PoseLog;
import com.vasomedical.spinetracer.util.Util;

import java.util.ArrayList;

/**
 * Main activity class for the Motion Tracking API sample. Handles the connection to the Tango
 * service and propagation of Tango pose data Layout view.
 */
public class HelloMotionTrackingActivity extends Activity {

    private static final String TAG = HelloMotionTrackingActivity.class.getSimpleName();

    private Tango mTango;
    private TangoConfig mConfig;
    private PoseLog poseLog;

    Button startButton;
    Button stopButton;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion_tracking);
        SQLiteDatabase db = DBAdapter.getDatabase(this);
        labelx = (TextView)findViewById(R.id.labelx);
        labely = (TextView)findViewById(R.id.labely);
        labelz = (TextView)findViewById(R.id.labelz);
        labelw = (TextView)findViewById(R.id.labelw);
        labelrx = (TextView)findViewById(R.id.labelrx);
        labelry = (TextView)findViewById(R.id.labelry);
        labelrz = (TextView)findViewById(R.id.labelrz);
        eulerx = (TextView)findViewById(R.id.eulerx);
        eulery = (TextView)findViewById(R.id.eulery);
        eulerz = (TextView)findViewById(R.id.eulerz);
        startButton = (Button)findViewById(R.id.startButton);
        stopButton = (Button)findViewById(R.id.stopButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });



    }

    private void start(){
        // Initialize Tango Service as a normal Android Service. Since we call mTango.disconnect()
        // in onPause, this will unbind Tango Service, so every time onResume gets called we
        // should create a new Tango object.
        mTango = new Tango(HelloMotionTrackingActivity.this, new Runnable() {
            // Pass in a Runnable to be called from UI thread when Tango is ready; this Runnable
            // will be running on a new thread.
            // When Tango is ready, we can call Tango functions safely here only when there are no
            // UI thread changes involved.
            @Override
            public void run() {
                synchronized (HelloMotionTrackingActivity.this) {
                    try {
                        mConfig = setupTangoConfig(mTango);
                        mTango.connect(mConfig);
                        startupTango();
                    } catch (TangoOutOfDateException e) {
                         Log.e(TAG, getString(R.string.exception_out_of_date), e);
                        showsToastAndFinishOnUiThread(R.string.exception_out_of_date);
                    } catch (TangoErrorException e) {
                        Log.e(TAG, getString(R.string.exception_tango_error), e);
                        showsToastAndFinishOnUiThread(R.string.exception_tango_error);
                    } catch (TangoInvalidException e) {
                        Log.e(TAG, getString(R.string.exception_tango_invalid), e);
                        showsToastAndFinishOnUiThread(R.string.exception_tango_invalid);
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
                poseLog.save(this);
            } catch (TangoErrorException e) {
                Log.e(TAG, getString(R.string.exception_tango_error), e);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {

        super.onPause();

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
        mTango.connectListener(framePairs, new OnTangoUpdateListener() {
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
            runOnUiThread(new Runnable() {
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

    /**
     * Display toast on UI thread.
     *
     * @param resId The resource id of the string resource to use. Can be formatted text.
     */
    private void showsToastAndFinishOnUiThread(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(HelloMotionTrackingActivity.this,
                        getString(resId), Toast.LENGTH_LONG).show();
                finish();
            }
          });
    }
}
