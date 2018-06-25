package com.vasomedical.spinetracer.algorithm;

import android.util.Log;

import com.vasomedical.spinetracer.model.Pose;

import java.util.ArrayList;

/**
 * Created by dehualai on 5/14/17.
 */

public class AlgorithmFactory {


    public static final String AlgorithmFactoryDetectOption = "Detect_OPT";
    public static final int DETECT_OPT_1 = 1;  // 躯干倾斜角
    public static final int DETECT_OPT_2 = 2;  // 驼背角
    public static final int DETECT_OPT_3 = 3;  // 脊柱弯曲cobb角
    public static final int DETECT_OPT_4 = 4;  // 脊柱左右侧弯角
    public static final int DETECT_OPT_5 = 5;  // 前倾后仰角
    public static final int DETECT_OPT_6 = 6;  // 旋转角
    public static final int DETECT_OPT_7 = 7;  // 身体平衡度
    public static int detectionOption = 0;


    public AlgorithmBase getAlgorithm(int option){
        switch (option){


            default:
                return new AlgorithmOptBending();
        }
    }





}
