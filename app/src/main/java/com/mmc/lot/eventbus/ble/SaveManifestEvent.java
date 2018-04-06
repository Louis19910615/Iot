package com.mmc.lot.eventbus.ble;

/**
 * Created by louis on 2018/4/6.
 */

public class SaveManifestEvent {
    private String deviceAddress;

    public SaveManifestEvent(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

}
