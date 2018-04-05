package com.mmc.lot.data;

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
}
