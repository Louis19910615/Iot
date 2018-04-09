package com.mmc.lot.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.baidu.location.BDLocation;
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



    /**
     * 保存位置信息  省市县、SIM
     *
     * @param location
     */
    public void setLocation(BDLocation location) {
        try {
            if (location != null) {
                Log.d("LocationHelper", location.getCountry() + "  "
                        + location.getProvince() + " " + location.getCity() + " "
                        + " " + location.getDistrict() + " " + location.getLatitude() + " "+
                        location.getLongitude());
                editor = sharedPreferences.edit();
                editor.putString("country", location.getCountry());
                editor.putString("province", location.getProvince().replaceAll("省", "").replaceAll("市", ""));
                editor.putString("city", location.getCity().replaceAll("市", ""));
                editor.putString("district", location.getDistrict().replaceAll("县", "").replaceAll("区", ""));
                editor.putString("street", location.getStreet());
                editor.putString("buildingName", location.getBuildingName());
                editor.putString("latitude", Double.toString(location.getLatitude()));
                editor.putString("longitude", Double.toString(location.getLongitude()));
                editor.commit();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 获得SIM
     */
    public String getSIM() {
        return sharedPreferences.getString("SIM", "Unknown");
    }

    public String getLatitude() {
        return sharedPreferences.getString("latitude", "");
    }

    public String getLongitude() {
        return sharedPreferences.getString("longitude", "");
    }

    /**
     * 获得国家
     */
    public String getCountry() {
        return sharedPreferences.getString("country", "");
    }

    /**
     * 获得省份
     */
    public String getProvince() {
        return sharedPreferences.getString("province", "");
    }

    /**
     * 获得城市
     */
    public String getCity() {
        return sharedPreferences.getString("city", "");
    }

    /**
     * 获得县（区）
     */
    public String getDistrict() {
        return sharedPreferences.getString("district", "");
    }

    /**
     * 获取街道信息
     */
    public String getStreet() {
        return sharedPreferences.getString("street", "");
    }

    /**
     * 获取建筑信息
     */
    public String getBuildingName() {
        return sharedPreferences.getString("buildingName", "");
    }

}
