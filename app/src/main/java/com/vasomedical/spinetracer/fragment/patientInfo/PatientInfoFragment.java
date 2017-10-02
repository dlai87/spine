package com.vasomedical.spinetracer.fragment.patientInfo;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
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
import com.vasomedical.spinetracer.util.widget.dialog.AlertDialog;
import com.vasomedical.spinetracer.util.widget.dialog.ButtonActionHandler;
import com.vasomedical.spinetracer.util.widget.dialog.DatePickerDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    ImageView existingPatientAvatar;
    EditText existingPatientNameEditText;
    EditText existingPatientGenderEditText;
    TextView existingPatientDateOfBirthText;
    EditText existingPatientContactInfoEditText;
    EditText existingPatientNoteEditText;

    LinearListView patientListView;
    BaseAdapter patientListViewAdapter;

    PatientModel selectedPatient;

    private SQLiteDatabase database = DBAdapter.getDatabase(mContext);
    private TBPatient tbPatient = new TBPatient();
    private ArrayList<PatientModel> patientList = tbPatient.getPatientList(database);

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
        patientListViewAdapter = new BaseAdapter() {
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
        patientListView.setAdapter(patientListViewAdapter);

        // existPatientDetailsLayout
        existingPatientAvatar = (ImageView) view.findViewById(R.id.existing_patient_avatar);
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
                boolean success = false;
                switch (currentStatus){
                    case STATUS_NEW_PATIENT:
                        success = nextButtonPressOnNewPatient();
                        break;
                    case STATUS_EXIST_PATIENT_LIST:
                        success = nextButtonPressOnExistPatientList();
                        break;
                    case STATUS_EXIST_PATIENT_DETAILS:
                        success = nextButtonPressOnExistPatientDetails();
                        break;
                    default:
                        break;
                }

                if (!success){
                    AlertDialog alertDialog = new AlertDialog(mContext);
                    alertDialog.setTitleView("alert");
                    alertDialog.setMessageView("patient information not valid");
                    alertDialog.setButtons("ok", null, null);
                    alertDialog.showDialog();
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

        LinearListView.OnItemClickListener onItemClickListener = new LinearListView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                selectedPatient = patientList.get(position);

                String photoPath = selectedPatient.getPhoto();
                if (photoPath!=null){
                    Bitmap photoBitmap = loadImageFromStorage(photoPath);
                    if (photoBitmap != null) {
                        existingPatientAvatar.setImageBitmap(photoBitmap);
                    }
                }

                existingPatientNameEditText.setText(selectedPatient.getName());
                existingPatientGenderEditText.setText(selectedPatient.getGender());
                existingPatientDateOfBirthText.setText(selectedPatient.getDate_of_birth());
                existingPatientContactInfoEditText.setText(selectedPatient.getPhone());
                existingPatientNoteEditText.setText(selectedPatient.getNote());

                setStatus(STATUS_EXIST_PATIENT_DETAILS);
            }
        };
        patientListView.setOnItemClickListener(onItemClickListener);
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
    private boolean nextButtonPressOnNewPatient(){

        String username = newPatientNameEditText.getText().toString().trim().replace(" ", "").toLowerCase();
        String patientId = username;
        String gender = newPatientGenderEditText.getText().toString();
        String birthOfDate = newPatientDateOfBirthText.getText().toString();
        String phone = newPatientContactInfoEditText.getText().toString();

        if (username==null || username.equals("") ){
            return false;
        }

        PatientModel.PatientBuilder patientBuilder = new PatientModel.PatientBuilder(patientId, username, gender, birthOfDate);
        patientBuilder.phone(phone);

        if(newPatientAvatar!=null && newPatientAvatar.getDrawable() != null){
            // If patient has photo, Save the picture to disk.
            String photoFilename = patientId + ".png";
            saveToInternalStorage(((BitmapDrawable) newPatientAvatar.getDrawable()).getBitmap(), photoFilename);
            patientBuilder.photo(photoFilename);
        }


        PatientModel patient = patientBuilder.build();
        tbPatient.smartInsert(database, patient);
        patientListViewAdapter.notifyDataSetChanged(); // FIXME: seems not working. Needs to update list view data after adding a patient.

        fragmentUtil.showFragment(new DetectionOptionsFragment());

        showDetectionOptions(patient);

        return true;
    }

    /**
     * Next Button  : Existing patient list
     * */
    private boolean nextButtonPressOnExistPatientList(){
        return showDetectionOptions(selectedPatient);
    }

    private boolean showDetectionOptions(PatientModel patient) {
        if (patient == null){
            return false;
        }
        DetectionOptionsFragment fragment = new DetectionOptionsFragment();
        fragment.setPatient(patient);
        fragmentUtil.showFragment(fragment);
        return true;
    }


    /**
     * Next Button : Existing patient details
     * */
    private boolean nextButtonPressOnExistPatientDetails() {
        return showDetectionOptions(selectedPatient);
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

    private String saveToInternalStorage(Bitmap bitmapImage, String filename) {
        ContextWrapper cw = new ContextWrapper(mContext);
        // path to /data/data/yourapp/app_data/photos
        File directory = cw.getDir("photos", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, filename);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private Bitmap loadImageFromStorage(String filename) {
        try {
            ContextWrapper cw = new ContextWrapper(mContext);
            // path to /data/data/yourapp/app_data/photos
            File directory = cw.getDir("photos", Context.MODE_PRIVATE);
            File f = new File(directory, filename);
            return BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
