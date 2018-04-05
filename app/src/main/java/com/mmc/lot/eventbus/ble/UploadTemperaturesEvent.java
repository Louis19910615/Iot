package com.mmc.lot.eventbus.ble;

/**
 * Created by louis on 2018/4/2.
 */

public class UploadTemperaturesEvent {
    private String deviceAddress;
    private boolean isFirst;

    public UploadTemperaturesEvent(String deviceAddress, boolean isFirst) {
        this.deviceAddress = deviceAddress;
        this.isFirst = isFirst;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public boolean getIsFirst() {
        return isFirst;
    }
}
