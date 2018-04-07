package com.mmc.lot.data;

import com.mmc.lot.util.IntentUtils;

/**
 * Created by liushuaizheng on 2018/4/5.
 */

public class UserInfo {

    private String token;
    private String userName;
    // gps 经度
    private double longitude;
    // gps 纬度
    private double latitude;

    // 角色
    private int actor; // 1 --- 发货方， 2 ---- 收货方， 3 --- 快递员

    public UserInfo() {

    }

    private UserInfo(String token, String userName) {
        this.token = token;
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        //send
        if (token.contains(IntentUtils.providerRole)) {
            this.actor = 1;
        }
        //take
        else if (token.contains(IntentUtils.clientRole)) {
            this.actor = 2;
        }
        //快递员
        else if (token.contains(IntentUtils.courierRole)) {
            this.actor = 3;
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getActor() {
        return this.actor;
    }
}
