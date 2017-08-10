package com.vasomedical.spinetracer.fragment.view3d;

import org.rajawali3d.materials.Material;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Line3D;

import java.util.Stack;

/**
 * Created by dehualai on 4/2/17.
 */

public class SpineShape {



    static float x0;
    static float y0;

    public static Line3D getShape(float x, float y, float z, float rotAngle, float scaleFactor, int index){
        x0 = x;
        y0 = y;


        Stack<Vector3> points = new Stack<Vector3>();

        float rotAngleSin = (float) Math.sin(rotAngle);
        float rotAngleCos = (float) Math.cos(rotAngle);

        float x1 = x-1.5f*scaleFactor;
        float y1 = y+0.8f*scaleFactor;
        points.add(new Vector3( rotateX(x1,y1,rotAngle), rotateY(x1,y1,rotAngle), z));
        x1 = x-0.1f*scaleFactor;
        y1 = y+0.8f*scaleFactor;
        points.add(new Vector3( rotateX(x1,y1,rotAngle), rotateY(x1,y1,rotAngle), z));
        x1 = x-0.1f*scaleFactor;
        y1 = y+1.4f*scaleFactor;
        points.add(new Vector3( rotateX(x1,y1,rotAngle), rotateY(x1,y1,rotAngle), z));
        x1 = x+0.1f*scaleFactor;
        y1 = y+1.4f*scaleFactor;
        points.add(new Vector3( rotateX(x1,y1,rotAngle), rotateY(x1,y1,rotAngle), z));
        x1 = x+0.1f*scaleFactor;
        y1 = y+0.8f*scaleFactor;
        points.add(new Vector3( rotateX(x1,y1,rotAngle), rotateY(x1,y1,rotAngle), z));
        x1 = x+1.5f*scaleFactor;
        y1 = y+0.8f*scaleFactor;
        points.add(new Vector3( rotateX(x1,y1,rotAngle), rotateY(x1,y1,rotAngle), z));
        x1 = x+1.f*scaleFactor;
        y1 = y-0.8f*scaleFactor;
        points.add(new Vector3( rotateX(x1,y1,rotAngle), rotateY(x1,y1,rotAngle), z));
        x1 = x-1.f*scaleFactor;
        y1 = y-0.8f*scaleFactor;
        points.add(new Vector3( rotateX(x1,y1,rotAngle), rotateY(x1,y1,rotAngle), z));
        x1 = x-1.5f*scaleFactor;
        y1 = y+0.8f*scaleFactor;
        points.add(new Vector3( rotateX(x1,y1,rotAngle), rotateY(x1,y1,rotAngle), z));


        Line3D shape = new Line3D(points, 5, 0xff0000+index);
        Material material = new Material();
        shape.setMaterial(material);
        return shape;
    }


    private static float rotateX(float x1, float y1, float rotAngle){
        float x2 = new Float( (x1-x0)* Math.cos(rotAngle)  + (y1-y0)* Math.sin(rotAngle) + x0);
        return x2;
    }

    private static float rotateY(float x1, float y1, float rotAngle){
        float y2 = new Float( -(x1-x0)* Math.sin(rotAngle)  + (y1-y0)* Math.cos(rotAngle) + y0);
        return y2;
    }
}
