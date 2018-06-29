package com.vasomedical.spinetracer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.vasomedical.spinetracer.R;

public class CelianTest extends AppCompatActivity {

    private ImageView imgBg, imgZhizhen;
    private SeekBar seekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrivity_celian_test);
        findViews();
        addAction();
    }

    private void findViews() {
        imgZhizhen = (ImageView) findViewById(R.id.imgZhizhen);
        imgBg = (ImageView) findViewById(R.id.imgBg);
        seekbar = (SeekBar) findViewById(R.id.seekbar);

    }

    private void addAction() {
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                float pivoY  = imgZhizhen.getY()+imgZhizhen.getHeight();
                float pivoY = imgZhizhen.getHeight() - 40;
                imgZhizhen.setPivotY(pivoY);
                imgZhizhen.setRotation(progress - 90);
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
