package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;

public class PrefConfig {

    private static final String LOCATION_PREF = "com.example.myapplication";
    private static final String PREF_LOCATION_KEY = "pref_location_key";

    public static void savePref(Context context, String[] locations) {
        // TODO - Context.MODE_PRIVATE my require a restart to add new locations
        // TODO - add error checking
        SharedPreferences pref = context.getSharedPreferences(LOCATION_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        for (String i : locations) {
            editor.putString(PREF_LOCATION_KEY, i);
        }

    }

    public static String[] loadLocationPref(Context context){
        String[] locationPrefs = new String[3];

        SharedPreferences pref = context.getSharedPreferences(LOCATION_PREF, Context.MODE_PRIVATE);
        Object[] objects = pref.getStringSet(PREF_LOCATION_KEY, new HashSet<String>() {}).toArray();

        for (int i = 0; i < objects.length; i++) {
            locationPrefs[i] = objects[i].toString();
        }

        return locationPrefs;
    }

}
