package com.riseofplanets;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "UserProfile";
    private static final String IS_NEW_USER = "IsNewUser";

    public UserManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean isNewUser() {
        return sharedPreferences.getBoolean(IS_NEW_USER, true);
    }

    public void setUserAsOld() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_NEW_USER, false);
        editor.apply();
    }
}
