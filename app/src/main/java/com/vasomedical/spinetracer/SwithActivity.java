package com.vasomedical.spinetracer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;

import com.vasomedical.spinetracer.database.util.DBAdapter;
import com.vasomedical.spinetracer.util.Global;
import com.vasomedical.spinetracer.util.LocaleHelper;

import java.util.ArrayList;

/**
 * Created by dehualai on 7/1/17.
 */

public class SwithActivity extends AppCompatActivity {

    String TAG = SwithActivity.class.getSimpleName();
    private Context mContext = null;



    @Override
    public void onStart() {
        super.onStart();

        mContext = this;


        LocaleHelper.setLocale(mContext, Global.USER_CURRENT_LANGUAGE);
        // initial database
        SQLiteDatabase db = DBAdapter.getDatabase(mContext);

        Intent targetIntent = null;
        if (!Global.login){
            targetIntent = new Intent(mContext, LoginActivity.class);
        }else{
            targetIntent = new Intent(mContext, MainActivity.class);
        }

        if(targetIntent!=null){
            targetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mContext.startActivity(targetIntent);
        }



    }
}
