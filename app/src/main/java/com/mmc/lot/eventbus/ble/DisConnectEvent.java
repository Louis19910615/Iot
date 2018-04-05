package com.mmc.lot.eventbus.ble;

/**
 * Created by louis on 2018/4/2.
 */

public class DisConnectEvent {
    private String deviceAddress;
    public DisConnectEvent(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceAddress() {
        return this.deviceAddress;
    }
}
