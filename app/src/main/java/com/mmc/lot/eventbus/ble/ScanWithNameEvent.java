package com.mmc.lot.eventbus.ble;

/**
 * Created by louis on 2018/4/1.
 */

public class ScanWithNameEvent {
    private String deviceName;

    public ScanWithNameEvent(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return this.deviceName;
    }
}
