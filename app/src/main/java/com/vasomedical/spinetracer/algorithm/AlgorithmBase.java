package com.vasomedical.spinetracer.algorithm;

import com.github.mikephil.charting.data.Entry;
import com.vasomedical.spinetracer.model.Pose;

import java.util.ArrayList;

/**
 * Created by dehualai on 5/28/17.
 */

public abstract class AlgorithmBase  {

    public abstract ArrayList<Entry> processData(ArrayList<Pose> inputData);



}
