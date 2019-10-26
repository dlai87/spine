package com.vasomedical.spinetracer.fragment.detect;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

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
import com.vasomedical.spinetracer.activity.DetectActivity;
import com.vasomedical.spinetracer.algorithm.AlgorithmBase;
import com.vasomedical.spinetracer.algorithm.AlgorithmFactory;
import com.vasomedical.spinetracer.algorithm.filters.BasicOperation;
import com.vasomedical.spinetracer.fragment.BaseFragment;
import com.vasomedical.spinetracer.fragment.analytics.AnalyticBaseFragment;
import com.vasomedical.spinetracer.model.PatientModel;
import com.vasomedical.spinetracer.model.Pose;
import com.vasomedical.spinetracer.model.PoseLog;
import com.vasomedical.spinetracer.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dehualai on 7/5/18.
 */

public abstract class DetectingBaseFragment extends BaseFragment {

    private static final String TAG = "DetectingBaseFragment";

    // tango
    protected Tango mTango;
    protected TangoConfig mConfig;

    // analytic
    protected AnalyticBaseFragment analyticFragment;

   // PatientModel patient;


    // initial pose related
    public TangoPoseData initPose = null;
    public float translationInit[] = new float[3];
    public float orientationInit[] = new float[4];

    // status
    public DetectActivity.DETECTION_STATUS detection_status = DetectActivity.DETECTION_STATUS.Init;
    public boolean shouldCalubrate = false;

    // pose log
    public PoseLog poseLog;

    @Override
    protected void assignViews() {

    }

    @Override
    protected void addActionToViews() {

    }


    public void start() {
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
    public void stop() {
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
        String timeStamp = new SimpleDateFormat(Util.FORMAT_DATE_TIME).format(new Date());
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
    protected abstract void logPose(TangoPoseData pose);





}
