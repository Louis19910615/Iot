package com.mmc.lot.bean;

/**
 * Created by zhangzd on 2018/3/25.
 */

public class TagInfoBean {

    private String tagID;
    private String mac;
    private String startTime;
    private int intervalTime;
    private int energy;
    private String gps;

    public TagInfoBean(String tagID, String mac, String startTime, int intervalTime, int energy, String gps) {
        this.tagID = tagID;
        this.mac = mac;
        this.startTime = startTime;
        this.intervalTime = intervalTime;
        this.energy = energy;
        this.gps = gps;
    }

    public String getTagID() {

        return tagID;
    }

    public void setTagID(String tagID) {
        this.tagID = tagID;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }
}
