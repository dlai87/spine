package com.vasomedical.spinetracer.fragment.patientInfo;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linearlistview.LinearListView;
import com.vasomedical.spinetracer.R;
import com.vasomedical.spinetracer.database.table.TBPatient;
import com.vasomedical.spinetracer.database.util.DBAdapter;
import com.vasomedical.spinetracer.fragment.BaseFragment;
import com.vasomedical.spinetracer.fragment.detect.DetectionOptionsFragment;
import com.vasomedical.spinetracer.model.PatientModel;
import com.vasomedical.spinetracer.util.Util;
import com.vasomedical.spinetracer.util.widget.button.NJButton;
import com.vasomedical.spinetracer.util.widget.dialog.ButtonActionHandler;
import com.vasomedical.spinetracer.util.widget.dialog.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by dehualai on 5/14/17.
 */

public class PatientInfoFragment extends BaseFragment {


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    final int STATUS_NEW_PATIENT = 0 ;
    final int STATUS_EXIST_PATIENT_LIST = 1;
    final int STATUS_EXIST_PATIENT_DETAILS = 2;
    int currentStatus = -1;
    NJButton chooseFromExistingButton;
    NJButton createNewButton;

    LinearLayout newPatientLayout;
    LinearLayout existPatientListLayout;
    LinearLayout existPatientDetailsLayout;

    Button nextButton;


    ImageView newPatientAvatar;
    Button takePhotoButton;
    EditText newPatientNameEditText;
    EditText newPatientGenderEditText;
    LinearLayout newPatientDateOfBirthEditText;
    TextView newPatientDateOfBirthText;
    EditText newPatientContactInfoEditText;
    EditText newPatientNoteEditText;

    EditText existingPatientNameEditText;
    EditText existingPatientGenderEditText;
    TextView existingPatientDateOfBirthText;
    EditText existingPatientContactInfoEditText;
    EditText existingPatientNoteEditText;

    LinearListView patientListView;

    private SQLiteDatabase database = DBAdapter.getDatabase(mContext);
    private TBPatient tbPatient = new TBPatient();
    private ArrayList<PatientModel> patientList = tbPatient.getPatientList(database);
    private BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return patientList.size();
        }

        @Override
        public Object getItem(int i) {
            return patientList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.patient_line, viewGroup, false);
            }
            TextView nameTextView = (TextView) view.findViewById(R.id.patient_line_headline);
            TextView descriptionTextView = (TextView) view.findViewById(R.id.patient_line_sidehead);
            ImageView avatarImageView = (ImageView) view.findViewById(R.id.patient_line_avatar);
            PatientModel patientModel = (PatientModel) getItem(i);
            nameTextView.setText(patientModel.getName());
            descriptionTextView.setText(patientModel.getNote());
            return view;
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_patient_info_new, container, false);
        Bundle args = getArguments();


        keyboardAdvance(view);
        setStepIndicator(0);

        View superView = super.onCreateView(inflater, container, savedInstanceState);

        setStatus(STATUS_NEW_PATIENT);

        return superView;

    }

    @Override
    protected void assignViews(){

        nextButton = (Button)view.findViewById(R.id.next_button);
        chooseFromExistingButton = (NJButton)view.findViewById(R.id.choose_exist_button);
        createNewButton = (NJButton)view.findViewById(R.id.create_new_button);
        newPatientLayout = (LinearLayout)view.findViewById(R.id.new_patient_layout);
        existPatientListLayout = (LinearLayout)view.findViewById(R.id.exist_petient_list_layout);
        existPatientDetailsLayout = (LinearLayout)view.findViewById(R.id.exist_petient_details_layout);
        newPatientDateOfBirthText = (TextView) view.findViewById(R.id.new_patient_date_of_birth_text);

        // newPatientLayout
        newPatientAvatar = (ImageView)view.findViewById(R.id.new_patient_avatar);
        takePhotoButton = (Button)view.findViewById(R.id.take_photo_button);
        newPatientNameEditText = (EditText)view.findViewById(R.id.new_patient_name);
        newPatientGenderEditText = (EditText)view.findViewById(R.id.new_patient_gender);
        newPatientDateOfBirthEditText = (LinearLayout)view.findViewById(R.id.new_patient_date_of_birth);
        newPatientContactInfoEditText = (EditText)view.findViewById(R.id.new_patient_contact_info);
        newPatientNoteEditText = (EditText)view.findViewById(R.id.new_patient_note);


        // existPatientListLayout
        patientListView = (LinearListView) view.findViewById(R.id.list);
        patientListView.setAdapter(adapter);

        // existPatientDetailsLayout
        // exisitngPatientAvatar = (ImageView)view.findViewById(R.id.exisiting_patient_avatar);
        existingPatientNameEditText = (EditText) view.findViewById(R.id.existing_patient_name);
        existingPatientGenderEditText = (EditText) view.findViewById(R.id.existing_patient_gender);
        existingPatientDateOfBirthText = (TextView) view.findViewById(R.id.existing_patient_date_of_birth_text);
        existingPatientContactInfoEditText = (EditText) view.findViewById(R.id.existing_patient_contact_info);
        existingPatientNoteEditText = (EditText) view.findViewById(R.id.existing_patient_note);
    }

    @Override
    protected void addActionToViews() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentStatus){
                    case STATUS_NEW_PATIENT:
                        nextButtonPressOnNewPatient();
                        break;
                    case STATUS_EXIST_PATIENT_LIST:
                        nextButtonPressOnExistPatientList();
                        break;
                    case STATUS_EXIST_PATIENT_DETAILS:
                        nextButtonPressOnExistPatientDetails();
                        break;
                    default:
                        break;
                }
                /*
                setStepIndicator(1);
                fragmentUtil.showFragment(ControlPanel.getFragment());
                */
            }
        });

        createNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatus(STATUS_NEW_PATIENT);
            }
        });

        chooseFromExistingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatus(STATUS_EXIST_PATIENT_LIST);
            }
        });



        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }

            }
        });

        newPatientDateOfBirthEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatePickerDialog dialog = new DatePickerDialog(mContext);
                dialog.setTitleView(null);
                dialog.setMessageView(mContext.getResources().getString(R.string.select_you_date_of_birth));
                dialog.setButtons(mContext.getResources().getString(R.string.cancel),
                        mContext.getResources().getString(R.string.ok),
                        new ButtonActionHandler() {
                            @Override
                            public void button1Pressed() {

                            }

                            @Override
                            public void button2Pressed() {
                                Date date = dialog.getPickerDate();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
                                String dateString = dateFormat.format(date);
                                newPatientDateOfBirthText.setText(dateString);
                            }
                        });
                dialog.showDialog();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Saving the picture just taken as avatar.
            newPatientAvatar.setBackground(null);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            newPatientAvatar.setImageBitmap(imageBitmap);
        }
    }

    /**
     * Next Button  : New Patient
     * */
    private void nextButtonPressOnNewPatient(){

        String username = newPatientNameEditText.getText().toString().trim().replace(" ", "").toLowerCase();
        String gender = newPatientGenderEditText.getText().toString();
        String birthOfDate = newPatientDateOfBirthText.getText().toString();
        String phone = newPatientContactInfoEditText.getText().toString();

        PatientModel.PatientBuilder patientBuilder = new PatientModel.PatientBuilder(username, username, gender, birthOfDate);
        patientBuilder.phone(phone);
        tbPatient.smartInsert(database, patientBuilder.build());

        fragmentUtil.showFragment(new DetectionOptionsFragment());


    }

    /**
     * Next Button  : Existing patient list
     * */
    private void nextButtonPressOnExistPatientList(){
        setStatus(STATUS_EXIST_PATIENT_DETAILS);

        PatientModel patientModel = patientList.get(0);

        existingPatientNameEditText.setText(patientModel.getName());
        existingPatientGenderEditText.setText(patientModel.getGender());
        existingPatientDateOfBirthText.setText(patientModel.getDate_of_birth());
        existingPatientContactInfoEditText.setText(patientModel.getPhone());
        existingPatientNoteEditText.setText(patientModel.getNote());
    }


    /**
     * Next Button : Existing patient details
     * */
    private void nextButtonPressOnExistPatientDetails(){

    }

    private void setStatus(int status){
        currentStatus = status;
        switch (status){
            case STATUS_NEW_PATIENT:
                createNewButton.setButtonTheme(4);
                chooseFromExistingButton.setButtonTheme(3);
                newPatientLayout.setVisibility(View.VISIBLE);
                existPatientListLayout.setVisibility(View.GONE);
                existPatientDetailsLayout.setVisibility(View.GONE);
                break;
            case STATUS_EXIST_PATIENT_LIST:
                createNewButton.setButtonTheme(3);
                chooseFromExistingButton.setButtonTheme(4);
                newPatientLayout.setVisibility(View.GONE);
                existPatientListLayout.setVisibility(View.VISIBLE);
                existPatientDetailsLayout.setVisibility(View.GONE);
                break;
            case STATUS_EXIST_PATIENT_DETAILS:
                createNewButton.setButtonTheme(3);
                chooseFromExistingButton.setButtonTheme(4);
                newPatientLayout.setVisibility(View.GONE);
                existPatientListLayout.setVisibility(View.GONE);
                existPatientDetailsLayout.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }


    /**
     *
     * Enable dismiss keyboard when touch out-side of the edit box
     *
     * */
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



}
