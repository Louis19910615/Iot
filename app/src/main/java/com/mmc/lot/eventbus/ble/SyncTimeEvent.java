package com.mmc.lot.eventbus.ble;

/**
 * Created by louis on 2018/4/1.
 */

public class SyncTimeEvent {
    private String deviceAddress;
    private long time;

    public SyncTimeEvent(String deviceAddress, long time) {
        this.deviceAddress = deviceAddress;
        this.time = time;
    }

    public long getTime() {
        return this.time;
    }

    public String getDeviceAddress() {
        return this.deviceAddress;
    }
}
