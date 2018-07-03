package com.vasomedical.spinetracer.database.util;

/**
 * Created by dehualai on 12/28/16.
 */

public class DBGlobal {

    public final static String TRANS_STATUS_INSERTED = "inserted";
    public final static String TRANS_STATUS_UPDATED = "updated";
    public final static String TRANS_STATUS_SENT = "sent";


    public static boolean TEST_MODE = true;
    public static String DB = "tango_db";
    public static int DB_VERSION = 1;
    public static boolean DB_ON_SDCARD = true;


    public static String COL_AUTO_ID = "_id";
    public static String COL_TRANSMISSTION_STATUS = "transmission_status";

    public static String TABLE_MOTION = "motion";
    public static String COL_X = "x";
    public static String COL_Y = "y";
    public static String COL_Z = "z";
    public static String COL_R_X = "r_x";
    public static String COL_R_Y = "r_y";
    public static String COL_R_Z = "r_z";
    public static String COL_R_W = "r_W";
    public static String COL_EULER_X = "euler_x";
    public static String COL_EULER_Y = "euler_y";
    public static String COL_EULER_Z = "euler_z";
    public static String COL_TIME = "time";


    public static String TABLE_PATIENT = "patient";
    public static String COL_ID = "id";
    public static String COL_NAME = "name";
    public static String COL_GENDER = "gender";
    public static String COL_DATE_OF_BIRTH = "date_of_birth";
    public static String COL_PHOTO = "photo";
    public static String COL_PHONE_NUMBER = "phone_number";
    public static String COL_EMAIL = "email";
    public static String COL_NOTE = "note";
    public static String COL_ID_DOCTOR = "doctor_id";


    public static String TABLE_DOCTOR = "doctor";
    public static String COL_PASSWORD = "password";
    public static String COL_HOSPITAL = "hospital";
    public static String COL_DEPARTMENT = "department";


    public static String TABLE_DETECTION = "detection";
    public static String COL_TIMESTAMP = "timestamp";
    public static String COL_PATIENT_ID = "patient_id";
    public static String COL_DOCTOR_ID = "doctor_id";
    public static String COL_MOTION_ID = "motion_id";
    public static String COL_DETECTION_TYPE = "detection_type";
    public static String COL_SAVE_CHART_PATH = "save_chart_path";
    public static String COL_POSE_DATA = "pose_data";
    public static String COL_SCORE = "score";
    public static String COL_COMMENT = "comment";

    public static String TABLE_POSE = "pose";
    public static String COL_DETECTION_ID = "detection_id";

    public static String TABLE_DATA_PROCESSED = "data_processed";
    public static String COL_ENTRY_X = "entry_x";
    public static String COL_ENTRY_Y = "entry_y";
    public static String COL_TYPE = "type";

    public static String TABLE_LOGS = "logs";
    public static String COL_LOGS_ID = "logs_id";
    public static String COL_LOGS_DOCTORID = "logs_doctorId";
    public static String COL_LOGS_DOCTORNAME = "logs_doctorName";
    public static String COL_LOGS_THING = "logs_thing";
    public static String COL_LOGS_TIME = "logs_time";


}
