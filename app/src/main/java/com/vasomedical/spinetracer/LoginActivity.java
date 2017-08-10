package com.vasomedical.spinetracer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vasomedical.spinetracer.database.util.DBAdapter;
import com.vasomedical.spinetracer.fragment.login.LoginFragment;
import com.vasomedical.spinetracer.util.fragmentTransation.FragmentUtil;
import com.vasomedical.spinetracer.util.fragmentTransation.IMainAppHandler;

/**
 * Created by dehualai on 5/13/17.
 */

public class LoginActivity extends AppCompatActivity implements IMainAppHandler {

    String TAG = "LoginActivity";
    FragmentUtil fragmentUtil;
    Context mContext;

    @Override
    public void onStart(){
        super.onStart();
        mContext = this;
        DBAdapter.getDatabase(mContext);
        fragmentUtil = new FragmentUtil(this);
        fragmentUtil.showFragment(new LoginFragment());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }


    // implement interface IMainAppHandler
    @Override
    public int getMainFragmentID(){
        return R.id.loginFragment;
    }

    @Override
    public void setStepIndicator(int step){}

    public void onBackPressed(){

    }

}
