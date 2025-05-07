package com.example.shopease.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String PREF_NAME = "shopease_pref";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_PHONE = "user_phone";
    private static final String KEY_USER_ADDRESS = "user_address";
    private static final String KEY_USER_CITY = "user_city";
    private static final String KEY_USER_ZIP_CODE = "user_zip_code";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private static SharedPrefManager mInstance;
    private static Context mContext;

    public SharedPrefManager(Context context) {
        mContext = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    // Store user data in shared preferences
    public void userLogin(int id, String name, String email, String phone,
                          String address, String city, String zipCode) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_USER_ID, id);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_PHONE, phone);
        editor.putString(KEY_USER_ADDRESS, address);
        editor.putString(KEY_USER_CITY, city);
        editor.putString(KEY_USER_ZIP_CODE, zipCode);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);

        editor.apply();
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Logout user by clearing shared preferences
    public void logout() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    // Get user data from shared preferences
    public int getUserId() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public String getUserName() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }

    public String getUserEmail() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_EMAIL, "");
    }

    public String getUserPhone() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_PHONE, "");
    }

    public String getUserAddress() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ADDRESS, "");
    }

    public String getUserCity() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_CITY, "");
    }

    public String getUserZipCode() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ZIP_CODE, "");
    }

    public void clear() {
    }
}