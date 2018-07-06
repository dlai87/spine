package com.vasomedical.spinetracer.fragment.detect;


import com.vasomedical.spinetracer.algorithm.AlgorithmFactory;
import com.vasomedical.spinetracer.fragment.BaseFragment;

/**
 * Created by dehualai on 7/4/18.
 */

public class DetectingFactory {


    public static DetectingBaseFragment getFragment(int option){
        switch (option){
            case AlgorithmFactory.DETECT_OPT_1:   // 躯干倾斜角   Pos - Angle
                return new DetectingPosAngleFragment();


            case AlgorithmFactory.DETECT_OPT_3:  // 脊柱弯曲cobb角    Pos - Pos
                return new DetectingPosPosFragment();

            case AlgorithmFactory.DETECT_OPT_4: // 脊柱左右侧弯角      Angle
            case AlgorithmFactory.DETECT_OPT_5:  // 前倾后仰角
            case AlgorithmFactory.DETECT_OPT_6:  // 旋转角
            case AlgorithmFactory.DETECT_OPT_7:  // 身体平衡度
                return new DetectingAngleFragment();

            default:
                return null;
        }
    }
}
