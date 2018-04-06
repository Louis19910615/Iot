package com.mmc.lot.eventbus.ble;

/**
 * Created by louis on 2018/4/7.
 */

public class QueryIntervalEvent {
    private String deviceAddress;
    public QueryIntervalEvent(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceAddress() {
        return this.deviceAddress;
    }
}
