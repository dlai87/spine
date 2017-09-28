package com.vasomedical.spinetracer.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.algorithm.AlgorithmBase;
import com.vasomedical.spinetracer.model.DoctorModel;
import com.vasomedical.spinetracer.model.Pose;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dehualai on 5/13/17.
 */

public class Util {

    public static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static String FORMAT_DATE = "yyyy-MM-dd";
    public static String FORMAT_TIME = "HH:mm:ss";
    static String TAG = "Util";
    private static DoctorModel currentDoctor;

    public static DoctorModel getCurrentDoctor() {
        return currentDoctor;
    }

    public static void setCurrentDoctor(DoctorModel currentDoctor) {
        Util.currentDoctor = currentDoctor;
    }

    public static String generateUniqueID(int len){
        final String STR = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( STR.charAt( rnd.nextInt(STR.length())));
        return sb.toString();
    }


    public static String convertTimeToString(Date date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(TimeZone.getDefault());
        return df.format(date);
    }

    public static Date convertStringToTime(String time, String formatString) {
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        format.setTimeZone(TimeZone.getDefault());
        try {
            Date date = format.parse(time);
            return date;
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void highlightLayout(LinearLayout layout, boolean highLight, Context mContext ){
        if (highLight)
            layout.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        else
            layout.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
    }

    public static boolean validateEmailFormat(String emailStr){
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }


    public static float radianToDegree(float radian, int offset, boolean reverse){
        float degree = (float) Math.toDegrees(radian);
        degree += offset;
        if (reverse) degree = -degree;
        Log.d("temp", " radian "  + radian + " degree " + degree + " Math degree ");
        DecimalFormat decimalFormat = new DecimalFormat("#.#"); // output is one digital after point
        try {
            float normDegree = Float.valueOf(decimalFormat.format(degree));
            return normDegree ;
        }catch (Exception e){
            return -99999;
        }
    }

    public static float radianToDegree(float radian){

        float degree = (float) Math.toDegrees(radian);

        Log.d("temp", " radian "  + radian + " degree " + degree + " Math degree ");
        DecimalFormat decimalFormat = new DecimalFormat("#.#"); // output is one digital after point
        try {
            float normDegree = Float.valueOf(decimalFormat.format(degree));
            return normDegree ;
        }catch (Exception e){
            return -99999;
        }
    }


    public static float positionToDisplay(float value){

        DecimalFormat decimalFormat = new DecimalFormat("#.#"); // output is one digital after point
        try {
            float normPosition = Float.valueOf(decimalFormat.format(value*100));
            return normPosition ;
        }catch (Exception e){
            return -99999;
        }
    }

    ///////////////////////// Test code  ////////

    public static double[] quaternion2Euler(double rotation_w,
                                            double rotation_x,
                                            double rotation_y,
                                            double rotation_z){

        double[] reuslt = new double[3];
        double sqw = rotation_w*rotation_w;
        double sqx = rotation_x*rotation_x;
        double sqy = rotation_y*rotation_y;
        double sqz = rotation_z*rotation_z;
        double unit = sqx + sqy + sqz + sqw; // if normalised is one, otherwise is correction factor
        double test = rotation_x*rotation_y + rotation_z*rotation_w;
        if (test > 0.499*unit) { // singularity at north pole
            double heading =   (2 * Math.atan2(rotation_x,rotation_w));
            double attitude =   Math.PI/2;
            double bank = 0;
            reuslt[0] = heading;
            reuslt[1] = attitude;
            reuslt[2] = bank;
            return reuslt;
        }
        if (test < -0.499*unit) { // singularity at south pole
            double heading =   (-2 * Math.atan2(rotation_x,rotation_w));
            double attitude = - Math.PI/2;
            double bank = 0;
            reuslt[0] = heading;
            reuslt[1] = attitude;
            reuslt[2] = bank;
            return reuslt;
        }
        double heading =   Math.atan2(2*rotation_y*rotation_w-2*rotation_x*rotation_z , sqx - sqy - sqz + sqw);
        double attitude =   Math.asin(2*test/unit);
        double bank =   Math.atan2(2*rotation_x*rotation_w-2*rotation_y*rotation_z , -sqx + sqy - sqz + sqw);
        reuslt[0] = heading;
        reuslt[1] = attitude;
        reuslt[2] = bank;
        return reuslt;
    }



    /**
     *
     *
     *
     * */
    public static  float valueOfCoordinate(Pose pose, AlgorithmBase.Coordinate c){
        switch (c){
            case x:
                return pose.getX();
            case y:
                return pose.getY();
            case z:
                return pose.getZ();
            case rx:
                return pose.getEuler_x();
            case ry:
                return pose.getEuler_y();
            case rz:
                return pose.getEuler_z();
        }
        return -1;
    }

}
