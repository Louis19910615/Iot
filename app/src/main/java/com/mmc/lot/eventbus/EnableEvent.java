package com.mmc.lot.eventbus;

/**
 * Created by louis on 2018/3/25.
 */

public class EnableEvent {

    private String deviceAddress;

    public EnableEvent(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }
}
