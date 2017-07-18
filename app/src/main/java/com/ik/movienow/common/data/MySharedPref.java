package com.ik.movienow.common.data;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

public class MySharedPref {

    public final static int SORT_POPULARITY = 0;
    public final static int SORT_RATINGS = 1;
    public final static int SORT_FAVOURITES = 2;

    private final String MY_SHARED_PREF = "MySharedPref";
    private final static String PREF_SORT = "Sort";
    private SharedPreferences sharedPreferences;


    @Inject
    public MySharedPref(Context context){
        sharedPreferences = context.getSharedPreferences(MY_SHARED_PREF, Context.MODE_PRIVATE);
    }

    public void saveSortPreference(int sortOpSelected){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_SORT, sortOpSelected);
        editor.apply();
    }

    public int getSortPreference(){
        return sharedPreferences.getInt(PREF_SORT, 0);
    }

}
