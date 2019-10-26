package com.vasomedical.spinetracer.util;

import android.Manifest;
import android.os.Environment;

import com.vasomedical.spinetracer.model.DoctorModel;
import com.vasomedical.spinetracer.model.PatientModel;


/**
 * Created by dehualai on 12/28/16.
 */

public class Global {

    public static String APP_FONT = "fonts/Roboto-Regular.ttf"; //"fonts/FZPWJW.TTF"; //"fonts/han_yi_zong_yi.ttf"; //

    public static final int REQUEST_PERMISSIONS = 1;

    public static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
          //  Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
          //  Manifest.permission.READ_PHONE_STATE,
    };

    public final static String FOLDER_APP_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.vasomed.spinetracker/";
    public final static String FOLDER_CHART = FOLDER_APP_ROOT + "chart/";
    public final static String FOLDER_AVATAR_IMG = FOLDER_APP_ROOT + "avatar/";

    public static boolean login = false;
    public static DoctorModel userModel;
    public static PatientModel patientModel;

    public static String USER_CURRENT_LANGUAGE = "zh";


}
