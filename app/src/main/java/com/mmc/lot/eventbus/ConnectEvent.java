package com.mmc.lot.eventbus;

/**
 * Created by louis on 2018/4/1.
 */

public class ConnectEvent {

    private String deviceAddress;
    public ConnectEvent(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceAddress() {
        return this.deviceAddress;
    }
}
