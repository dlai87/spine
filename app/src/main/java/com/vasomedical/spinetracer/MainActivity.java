package com.vasomedical.spinetracer;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.badoualy.stepperindicator.StepperIndicator;
import com.vasomedical.spinetracer.fragment.controlPanel.ControlPanel;
import com.vasomedical.spinetracer.fragment.detect.DetectFragment;
import com.vasomedical.spinetracer.fragment.detect.DetectionOptionsFragment;
import com.vasomedical.spinetracer.fragment.patientInfo.PatientInfoFragment;
import com.vasomedical.spinetracer.util.Global;
import com.vasomedical.spinetracer.util.fragmentTransation.FragmentUtil;
import com.vasomedical.spinetracer.util.fragmentTransation.IMainAppHandler;
import com.vasomedical.spinetracer.util.menu.DrawerAdapter;
import com.vasomedical.spinetracer.util.menu.DrawerItem;
import com.vasomedical.spinetracer.util.menu.SimpleItem;
import com.vasomedical.spinetracer.util.menu.SpaceItem;
import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener, IMainAppHandler {

    String TAG = "MainActivity";


    FragmentUtil fragmentUtil;
    Context mContext;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private static final int POS_PATIENT_INFO = 0;
    private static final int POS_HISTORY = 1;
    private static final int POS_DOCTOR_SETTINGS = 2;
    private static final int POS_LOGOUT = 4;


    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final int REQUEST_READ_STORAGE = 113;
    private static final int REQUEST_CAMERA = 114;


    StepperIndicator stepperIndicator;
    LinearLayout stepperIndicatLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        fragmentUtil = new FragmentUtil(this);
        fragmentUtil.showFragment(ControlPanel.getFragment());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withDragDistance(140)
                .withRootViewScale(0.8f)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenTitles = loadScreenTitles();
        screenIcons = loadScreenIcons();


        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_PATIENT_INFO).setChecked(true),
                createItemFor(POS_HISTORY),
                //createItemFor(POS_ANALYTICS),
                //createItemFor(POS_SOLUTION),
                createItemFor(POS_DOCTOR_SETTINGS),
                new SpaceItem(100),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_PATIENT_INFO);


        stepperIndicator = (StepperIndicator) findViewById(R.id.stepIndicator);
        stepperIndicatLayout = (LinearLayout) findViewById(R.id.stepIndicatorLayout);


        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Permission to access the SD-CARD is required for this app to Download PDF.")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Clicked");
                        makeRequest();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                makeRequest();
            }
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user");
                } else {
                    Log.i(TAG, "Permission has been granted by user");
                }
                return;
            }
            case REQUEST_READ_STORAGE: {
                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user");
                } else {
                    Log.i(TAG, "Permission has been granted by user");
                }
                return;
            }
            case REQUEST_CAMERA: {
                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user");
                } else {
                    Log.i(TAG, "Permission has been granted by user");
                }
                return;
            }
        }
    }


    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.text_gray_mid))
                .withTextTint(color(R.color.text_gray_mid))
                .withSelectedIconTint(color(R.color.white))
                .withSelectedTextTint(color(R.color.white));
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }


    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA},
                REQUEST_WRITE_STORAGE);
        //ActivityCompat.requestPermissions(this,
        //        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
        //        REQUEST_READ_STORAGE);
        //ActivityCompat.requestPermissions(this,
        //        new String[]{Manifest.permission.CAMERA},
        //        REQUEST_CAMERA);
    }


    /**
     * implement interface IMainAppHandler
     */
    public int getMainFragmentID() {
        return R.id.mainFragment;
    }

    @Override
    public void setStepIndicator(int step) {
        if (step < 0) {
            stepperIndicatLayout.setVisibility(View.GONE);
        } else {
            stepperIndicatLayout.setVisibility(View.VISIBLE);
            stepperIndicator.setCurrentStep(step);
        }

    }


    /**
     * implement interface DrawerAdapter.OnItemSelectedListener
     */
    @Override
    public void onItemSelected(int position) {
        if (position == POS_LOGOUT) {
            Global.login = false;
            Intent targetIntent = new Intent(mContext, SwithActivity.class);
            targetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mContext.startActivity(targetIntent);
        }

    }


}
