package com.mmc.lot.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.mmc.lot.IotApplication;

/**
 * Created by zhangzd on 2018/3/25.
 */

public class SharePreUtils {

    private static volatile SharePreUtils sharePreUtils;
    private final SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String PREFERENCE_FILE_NAME = "iot_sp";
    public static final String USER_TOKEN = "user_token";
    public static final String USER_NAME = "user_name";


    private SharePreUtils() {
        sharedPreferences = IotApplication.getInstance().getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static SharePreUtils getInstance() {
        if (sharePreUtils == null) {
            synchronized (SharePreUtils.class) {
                if (sharePreUtils == null) {
                    sharePreUtils = new SharePreUtils();
                }
            }
        }
        return sharePreUtils;
    }

    public void setString(String key, String value) {
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

}
