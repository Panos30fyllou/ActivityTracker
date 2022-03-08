package com.example.activitytracker.helpers;

import android.content.Context;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import java.util.Locale;

public final class LanguageHelper {

    public static String getLanguagePreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString("MyLang", "");
    }

    public static void setLanguagePreference(Context context, String language){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("MyLang", language).apply();
    }

    public static Configuration getNewConfig(String localeString){
        Locale locale = new Locale(localeString);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        return configuration;
    }

}
