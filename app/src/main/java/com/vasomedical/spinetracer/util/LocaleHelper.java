package com.vasomedical.spinetracer.util;

/**
 * Created by dehualai on 7/18/16.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import java.util.Locale;

/**
 * This class is used to change your application locale and persist this change for the next time
 * that your app is going to be used.
 * <p/>
 * You can also change the locale of your application on the fly by using the setLocale method.
 * <p/>
 * Created by gunhansancar on 07/10/15.
 */
public class LocaleHelper {

    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
    private static String defaultLangeuageCode;

    public static void onCreate(Context context) {
        String lang = getPersistedData(context, Locale.getDefault().getLanguage());
        setLocale(context, lang);
    }

    public static void onCreate(Context context, String defaultLanguage) {
        String lang = getPersistedData(context, defaultLanguage);
        setLocale(context, lang);
    }

    public static String getLanguage(Context context) {
        return getPersistedData(context, Locale.getDefault().getLanguage());
    }


    public static void setLocale(Context context, String language) {
        if (language.equals("cn"))
            language = "zh";
        defaultLangeuageCode = language;
        persist(context, language);
        updateResources(context, language);
        Global.USER_CURRENT_LANGUAGE = language;
    }

    public static void tempSetToEnglish(Context context){
        persist(context, "en");
        updateResources(context, "en");
        Global.USER_CURRENT_LANGUAGE = "en";
    }

    public static void resetFromTemp(Context context){
        if (defaultLangeuageCode!=null){
            setLocale(context, defaultLangeuageCode);
        }else{
            setLocale(context, "en");
        }
    }

    private static String getPersistedData(Context context, String defaultLanguage) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage);
    }

    private static void persist(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();
    }

    private static void updateResources(Context context, String language) {
        Locale locale = null;
        if(language.contains("_")){
            //Contains coutry code
            String[] parts = language.split("_");
            if(parts.length == 2){
                String lang = parts[0];
                String region = parts[1];
                locale = new Locale(lang,region);
            }
        }else{
            locale = new Locale(language);
        }
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            setSystemLocale(configuration, locale);
//        } else {
//            setSystemLocaleLegacy(configuration, locale);
//        }
        configuration.locale = locale;
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

    }

//    @SuppressWarnings("deprecation")
//    public static void setSystemLocaleLegacy(Configuration config, Locale locale){
//        config.locale = locale;
//    }
//
//    @TargetApi(Build.VERSION_CODES.N)
//    public static void setSystemLocale(Configuration config, Locale locale){
//        LocaleList localeList = new LocaleList(locale);
//        LocaleList.setDefault(localeList);
//        config.setLocales(localeList);
//    }
}
