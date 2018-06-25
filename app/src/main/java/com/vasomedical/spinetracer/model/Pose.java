package com.vasomedical.spinetracer.model;

import android.support.annotation.NonNull;

import com.vasomedical.spinetracer.util.Coor;
import com.vasomedical.spinetracer.util.Util;

import java.util.Date;

public class Pose implements Comparable{

    public static Coor sortBy = Coor.py; // default sort by;

    // raw data
    float x;
    float y;
    float z;
    float rotation_x;
    float rotation_y;
    float rotation_z;
    float rotation_w;

    // calculated data
    float euler_x;
    float euler_y;
    float euler_z;

    // time stamp
    Date timeStamp;

    public Pose(float x,
                float y,
                float z,
                float rotation_x,
                float rotation_y,
                float rotation_z,
                float rotation_w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation_x = rotation_x;
        this.rotation_y = rotation_y;
        this.rotation_z = rotation_z;
        this.rotation_w = rotation_w;

        final double[] euler = Util.quaternion2Euler(rotation_x, rotation_y, rotation_z,rotation_w);
        this.euler_x = (float) euler[0];
        this.euler_y = (float) euler[1];
        this.euler_z = (float) euler[2];
        this.timeStamp = new Date();
    }


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getRotation_x() {
        return rotation_x;
    }

    public float getRotation_y() {
        return rotation_y;
    }

    public float getRotation_z() {
        return rotation_z;
    }

    public float getRotation_w() {
        return rotation_w;
    }

    public float getEuler_x() {
        return euler_x;
    }

    public float getEuler_y() {
        return euler_y;
    }

    public float getEuler_z() {
        return euler_z;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString(){
        return "x:" + x +
                ",y:" + y +
                ",z:" + z +
                ",rx:" + euler_x +
                ",ry:" + euler_y +
                ",rz:" + euler_z;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Pose other = (Pose) o;
        switch (sortBy){
            case px:
                if (this.x < other.getX()) return -1;
                if (this.x > other.getX()) return 1;
                break;
            case py:
                if (this.y < other.getY()) return -1;
                if (this.y > other.getY()) return 1;
                break;
            case pz:
                if (this.z < other.getZ()) return -1;
                if (this.z > other.getZ()) return 1;
                break;
        }
        return 0;
    }
}
