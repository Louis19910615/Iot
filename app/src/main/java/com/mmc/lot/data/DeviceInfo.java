package com.mmc.lot.data;

import java.util.List;

/**
 * Created by liushuaizheng on 2018/4/5.
 */

public class DeviceInfo {
    private String starTime;
    private String deviceAddress;
    private int remainingBattery;
    private List<Double> temperatureDatas;
    private int timeInterval;
    private String deviceName;
    private String category;
    private String description;
    private String tagId;

    public DeviceInfo() {

    }

    public String getStarTime() {
        return starTime;
    }

    public void setStarTime(String starTime) {
        this.starTime = starTime;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
        this.tagId = deviceAddress.replace(":", "");
    }

    public int getRemainingBattery() {
        return remainingBattery;
    }

    public void setRemainingBattery(int remainingBattery) {
        this.remainingBattery = remainingBattery;
    }

    public List<Double> getTemperatureDatas() {
        return temperatureDatas;
    }

    public void setTemperatureDatas(List<Double> temperatureDatas) {
        this.temperatureDatas = temperatureDatas;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTagId() {
        return this.tagId;
    }
}
