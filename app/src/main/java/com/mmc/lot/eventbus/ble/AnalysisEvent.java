package com.mmc.lot.eventbus.ble;

/**
 * Created by louis on 2018/3/25.
 */

public class AnalysisEvent {

    public byte[] bytes;

    public AnalysisEvent(byte[] bytes) {
        this.bytes = bytes;
    }
}
