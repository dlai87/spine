package com.vasomedical.spinetracer.fragment.login;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.util.Util;
import com.vasomedical.spinetracer.util.fragmentTransation.FragmentUtil;
import com.vasomedical.spinetracer.util.fragmentTransation.IMainAppHandler;
import com.vasomedical.spinetracer.util.widget.button.NJButton;
import com.vasomedical.spinetracer.util.widget.dialog.NJProgressDialog;

import java.io.File;
import java.util.HashMap;


/**
 * Created by dehualai on 1/8/17.
 */

public class SignUpFragment extends Fragment {

    String TAG = "SignUpFragment";
    Context mContext;
    FragmentUtil fragmentUtil;
    IMainAppHandler mainAppHandler;

    NJProgressDialog progressDialog;


    View view;
    EditText usernameField;
    EditText emailField;
    EditText passwordField;
    EditText confirmPasswordField;
    NJButton signUpButton;
    Button cancelButton;
    TextView errorMessage;
    LinearLayout usernameLayout;
    LinearLayout emailLayout;
    LinearLayout passwordLayout;
    LinearLayout confirmPasswordLayout;
    LinearLayout usernameIcon;
    LinearLayout emailIcon;
    LinearLayout passwordIcon;
    LinearLayout confirmPasswordIcon;

    String username;
    String email;
    String password;

    int input_status;

    final int NO_ERROR = 9999;
    final int ERROR_FIELD_EMPTY = 5000;
    final int ERROR_INVALID_EMAIL_FORMAT = 5001;
    final int ERROR_USERNAME_EXIST = 5002;
    final int ERROR_EMAIL_EXIST = 5003;
    final int ERROR_PASSWORD_NOT_MATCH = 5004;

    @Override
    public void onAttach(Activity activity) {
        Log.i(TAG, "onAttach");
        super.onAttach(activity);
        mContext = activity;
        fragmentUtil = new FragmentUtil(activity);
        if (activity instanceof IMainAppHandler) {
            this.mainAppHandler = (IMainAppHandler) activity;
        } else {
            Log.e(TAG, "The input activity must implement interface IMainAppHandler");
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_sign_up_new, container, false);
        Bundle args = getArguments();
        keyboardAdvance(view);

        assignViews();
        addActionToViews();

        return view;
    }





    private void assignViews(){

        usernameField = (EditText)view.findViewById(R.id.input_username);
        emailField = (EditText)view.findViewById(R.id.input_email);
        passwordField = (EditText)view.findViewById(R.id.input_password);
        confirmPasswordField = (EditText)view.findViewById(R.id.input_confirm_password);

        signUpButton = (NJButton) view.findViewById(R.id.sign_up_button);
        cancelButton = (Button)view.findViewById(R.id.go_to_login_button);

        errorMessage = (TextView)view.findViewById(R.id.error_message);

        usernameLayout = (LinearLayout)view.findViewById(R.id.username_layout);
        emailLayout = (LinearLayout)view.findViewById(R.id.email_layout);
        passwordLayout = (LinearLayout)view.findViewById(R.id.password_layout);
        confirmPasswordLayout = (LinearLayout)view.findViewById(R.id.confirm_password_layout);

        usernameIcon = (LinearLayout)view.findViewById(R.id.username_icon);
        emailIcon = (LinearLayout)view.findViewById(R.id.email_icon);
        passwordIcon = (LinearLayout)view.findViewById(R.id.password_icon);
        confirmPasswordIcon = (LinearLayout)view.findViewById(R.id.c_password_icon);
    }

    private void addActionToViews(){
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentUtil.showFragment(new LoginFragment());
            }
        });
    }



    // Enable dismiss keyboard when touch out-side of the edit box
    public void keyboardAdvance(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    Util.hideSoftKeyboard((Activity) mContext);
                    return false;
                }

            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                keyboardAdvance(innerView);
            }
        }
    }

    // verify input fields
    private int verifyInputFields(){
        // clean hight light first
        Util.highlightLayout(usernameLayout, false, mContext);
        Util.highlightLayout(emailLayout, false, mContext);
        Util.highlightLayout(passwordLayout, false, mContext);
        Util.highlightLayout(confirmPasswordLayout, false, mContext);

        // get content
        String username = usernameField.getText().toString().replace(" ", "").toLowerCase();
        String email = emailField.getText().toString().replace(" ", "").toLowerCase();
        String password = passwordField.getText().toString().replace(" ", "");
        String confirmPassword = confirmPasswordField.getText().toString().replace(" ", "");

        if (username == null || username.equals("") ||
                email == null || email.equals("") ||
                password == null || password.equals("") ||
                confirmPassword == null || confirmPassword.equals("")
                ){
            if (username == null || username.equals("")) Util.highlightLayout(usernameLayout, true, mContext);
            if (email == null || email.equals("")) Util.highlightLayout(emailLayout, true, mContext);
            if (password == null || password.equals("")) Util.highlightLayout(passwordLayout, true, mContext);
            if (confirmPassword == null || confirmPassword.equals("")) Util.highlightLayout(confirmPasswordLayout, true, mContext);
            errorMessage.setText(mContext.getResources().getString(R.string.please_fill_up_the_form));
            return ERROR_FIELD_EMPTY;
        }
        if (!Util.validateEmailFormat(email)){
            Util.highlightLayout(emailLayout, true, mContext);
            errorMessage.setText(mContext.getResources().getString(R.string.incorrect_email_format));
            return ERROR_INVALID_EMAIL_FORMAT;
        }
        if (!password.equals(confirmPassword)){
            Util.highlightLayout(passwordLayout, true, mContext);
            Util.highlightLayout(confirmPasswordLayout, true, mContext);
            errorMessage.setText(mContext.getResources().getString(R.string.password_not_match));
            return ERROR_PASSWORD_NOT_MATCH;
        }
        errorMessage.setText("");
        this.username = username;
        this.email = email;
        this.password = password;
        return NO_ERROR;

    }




}
