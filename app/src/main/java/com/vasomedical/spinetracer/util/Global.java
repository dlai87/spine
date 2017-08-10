package com.vasomedical.spinetracer.util;

import android.os.Environment;


/**
 * Created by dehualai on 12/28/16.
 */

public class Global {

    public static String APP_FONT =  "fonts/Roboto-Regular.ttf"; //"fonts/FZPWJW.TTF"; //"fonts/han_yi_zong_yi.ttf"; //


    public final static String FOLDER_APP_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.vasomed.spinetracker/";
    public final static String FOLDER_CHART = FOLDER_APP_ROOT + "chart/";
    public final static String FOLDER_AVATAR_IMG = FOLDER_APP_ROOT + "avatar/";

    public static boolean login = false;

    public static String USER_CURRENT_LANGUAGE = "zh";


}
