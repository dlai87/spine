package com.vasomedical.spinetracer.algorithm.spineObj;

import com.vasomedical.spinetracer.model.Pose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dehualai on 6/24/18.
 */

public class SpineSegmentManager {


    int numPieces = 20;


    public <T> List<List<T>> splitList(List<T> list, int n) {
        List<List<T>> strList = new ArrayList<>();
        if (list == null) return strList;
        int size = list.size();
        int quotient = size / n; // 商数
        int remainder = size % n; // 余数
        int offset = 0; // 偏移量
        int len = quotient > 0 ? n : remainder; // 循环长度
        int start = 0;  // 起始下标
        int end = 0;    // 结束下标
        List<T> tempList = null;
        for (int i = 0; i < len; i++) {
            if (remainder != 0) {
                remainder--;
                offset = 1;
            } else {
                offset = 0;
            }
            end = start + quotient + offset;
            tempList = list.subList(start, end);
            start = end;
            strList.add(tempList);
        }
        return strList;
    }




    public ArrayList<SpineShape> covert(ArrayList<Pose> rawData){
        ArrayList<SpineShape> spineList = new ArrayList<SpineShape>();

        List<List<Pose>> subData = splitList(rawData, numPieces);
        for(List<Pose> sub : subData){
            spineList.add(new SpineShape(sub));
        }

        return spineList;
    }



    public ArrayList<SpineShape> covert2(ArrayList<ArrayList<Pose>> rawData){
        ArrayList<SpineShape> spineList = new ArrayList<SpineShape>();

        for(List<Pose> sub : rawData){
            spineList.add(new SpineShape(sub));
        }

        return spineList;
    }
}
