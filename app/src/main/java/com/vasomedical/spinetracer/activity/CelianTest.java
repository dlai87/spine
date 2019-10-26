package com.vasomedical.spinetracer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.vasomedical.spinetracer.R;

public class CelianTest extends AppCompatActivity {

    private ImageView imgBg1, imgZhizhen1;
    private ImageView imgBg2, imgZhizhen2;
    private SeekBar seekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrivity_celian_test);
        findViews();
        addAction();
    }

    private void findViews() {
        View celian1 = findViewById(R.id.layout_celian);
        imgZhizhen1 = (ImageView) celian1.findViewById(R.id.imgZhizhen);
        imgBg1 = (ImageView) celian1.findViewById(R.id.imgBg);
        View celian2 = findViewById(R.id.layout_celian2);
        imgZhizhen2 = (ImageView) celian2.findViewById(R.id.imgZhizhen);
        imgBg2 = (ImageView) celian2.findViewById(R.id.imgBg);
        seekbar = (SeekBar) findViewById(R.id.seekbar);

    }

    private void addAction() {
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                float pivoY  = imgZhizhen.getY()+imgZhizhen.getHeight();
                float pivoY = imgZhizhen1.getHeight() - 40;
                imgZhizhen1.setPivotY(pivoY);
                imgZhizhen1.setRotation(progress - 90);

                float x = imgBg2.getWidth() * progress / 180 - (imgZhizhen2.getWidth() / 2) + imgBg2.getX();
                imgZhizhen2.setX(x);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
