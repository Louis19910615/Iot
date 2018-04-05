package com.mmc.lot.eventbus.ble;

/**
 * Created by louis on 2018/4/1.
 */

public class GetMessageEvent {

    private String deviceAddress;

    public GetMessageEvent(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceAddress() {
        return this.deviceAddress;
    }
}
