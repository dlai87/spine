package com.vasomedical.spinetracer.fragment.view3d;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.fragment.BaseFragment;
import com.vasomedical.spinetracer.fragment.detect.DetectFragment;
import com.vasomedical.spinetracer.model.Pose;
import com.vasomedical.spinetracer.model.PoseLog;

import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation;
import org.rajawali3d.animation.RotateOnAxisAnimation;
import org.rajawali3d.cameras.ArcballCamera;
import org.rajawali3d.lights.ALight;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Line3D;
import org.rajawali3d.renderer.ISurfaceRenderer;
import org.rajawali3d.view.ISurface;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by dehualai on 4/9/17.
 */

public class View3dFragment extends AExampleFragment implements
        View.OnTouchListener {



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view3d, container, false);
        Bundle args = getArguments();


        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    protected void assignViews(){

    }

    @Override
    protected void addActionToViews() {

    }



    // implement OnTouchListener
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float xpos = 0;
        float ypos = 0 ;
        float touchTurn = 0;
        float touchTurnUp =0 ;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.e("show", "ACTION_DOWN");

            xpos = event.getX();
            ypos = event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.e("show", "ACTION_UP");

            float xd = event.getX() - xpos;
            float yd = event.getY() - ypos;

            ((LinesRenderer) mRenderer).move();

            xpos = -1;
            ypos = -1;
            touchTurn = 0;
            touchTurnUp = 0;



        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            Log.e("show", "ACTION_MOVE");
            float xd = event.getX() - xpos;
            float yd = event.getY() - ypos;

            xpos = event.getX();
            ypos = event.getY();

            touchTurn = xd;
            touchTurnUp = yd;
        }

        Log.e("show", "onTouch x " + xpos + " y " + ypos + " touchTurn " + touchTurn + " touchTurnUp " + touchTurnUp);



        try {
            Thread.sleep(15);
        } catch (Exception e) {

        }

        return true;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onBeforeApplyRenderer() {
        mRajawaliSurface.setAntiAliasingMode(ISurface.ANTI_ALIASING_CONFIG.MULTISAMPLING);
        mRajawaliSurface.setSampleCount(2);
        ((View) mRajawaliSurface).setOnTouchListener(this);

        super.onBeforeApplyRenderer();
    }


    @Override
    public ISurfaceRenderer createRenderer() {
        return new LinesRenderer(mContext);
    }


    private final class LinesRenderer extends AExampleRenderer {

        Object3D mObjectGroup = new Object3D();


        public LinesRenderer(Context context) {
            super(context);
        }



        public void move(){
            // MARK Touch Linstener
           // mObjectGroup.moveRight(0.5);
           // mObjectGroup.setRotX(180);
           // mObjectGroup.setScaleZ(2);
           // mObjectGroup.setScaleX(2);
           // mObjectGroup.setScaleY(2);
        }

        @Override
        protected void initScene() {
            drawPose();
            getCurrentScene().addChild(mObjectGroup);

            ArcballCamera arcballCamera = new ArcballCamera(mContext, (View) mRajawaliSurface);
            arcballCamera.setTarget(mObjectGroup);
            arcballCamera.setPosition(0,0,3f); //optional
            getCurrentScene().replaceAndSwitchCamera(getCurrentCamera(), arcballCamera);


        }





        private void addShape(float x, float y, float z, float rotate, float scale, int index){
            Line3D line = SpineShape.getShape(x,y,z,rotate, scale, index);
           // getCurrentScene().addChild(line);

            mObjectGroup.addChild(line);

            /*
            Vector3 axis = new Vector3(2, 0.5f, 0.8f);
            axis.normalize();
            RotateOnAxisAnimation anim = new RotateOnAxisAnimation(axis, 360);
            anim.setDurationMilliseconds(16000);
            anim.setRepeatMode(Animation.RepeatMode.INFINITE);
            anim.setTransformable3D(line);
            getCurrentScene().registerAnimation(anim);
            anim.play();
            */
        }



        public void drawPose(){

            int sample_num = 33;
            ArrayList<Pose> dataList = DetectFragment.poseLog.getPoseList();

            int index = 0 ;
            int takeSample = dataList.size() / sample_num;
            for(int i = 0 ; i < dataList.size(); i ++){
                if(i % takeSample == 0){
                    Pose pose = dataList.get(i);
                    addShape(pose.getZ()*2  , pose.getX()  , pose.getY()*5 , pose.getEuler_y(), 0.1f, index * 4);
                    index ++;
                }
            }
            Log.e("show", "dataList " + dataList.size());

        }


    }

}
