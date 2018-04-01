package com.mmc.lot.eventbus;

/**
 * Created by louis on 2018/4/2.
 */

public class ScanWithAddressEvent {
    private String deviceAddress;

    public ScanWithAddressEvent(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceAddress() {
        return this.deviceAddress;
    }
}
