package com.mmc.lot.eventbus.ble;

/**
 * Created by louis on 2018/4/7.
 */

public class ActivateEvent {
    private String deviceAddress;
    public ActivateEvent(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceAddress() {
        return this.deviceAddress;
    }
}
