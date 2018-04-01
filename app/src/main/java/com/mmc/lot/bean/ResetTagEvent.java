package com.mmc.lot.bean;

/**
 * Created by louis on 2018/4/1.
 */

public class ResetTagEvent {
    private String deviceAddress;

    public ResetTagEvent(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceAddress() {
        return this.deviceAddress;
    }
}
