package com.vasomedical.spinetracer.algorithm;

import android.util.Log;

import com.vasomedical.spinetracer.model.Pose;

import java.util.ArrayList;

/**
 * Created by dehualai on 5/14/17.
 */

public class AlgorithmFactory {

    public static final int DETECT_OPT_1 = 1;  // 脊柱侧弯倾角
    public static final int DETECT_OPT_2 = 2;  // 矢面旋转角
    public static final int DETECT_OPT_3 = 3;  // 前倾 后伸
    public static final int DETECT_OPT_4 = 4;  // 左倾 右倾
    public static final int DETECT_OPT_5 = 5;  // 驼背角
    public static int detectionOption = 0;


    public AlgorithmBase getAlgorithm(int option){
        switch (option){
            case DETECT_OPT_1:
                return new AlgorithmOpt1();

            default:
                return new AlgorithmOpt1();
        }
    }






}
