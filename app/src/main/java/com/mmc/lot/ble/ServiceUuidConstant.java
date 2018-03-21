package com.mmc.lot.ble;

import java.util.UUID;

/**
 * Created by louis on 2018/3/22.
 */

public class ServiceUuidConstant {

    public static final UUID IOT_SERVICE_UUID = UUID.fromString("00010203-0405-0607-0809-0a0b0c0d1910");
    // module --> phone
    public static final UUID RX_CHAR_UUID = UUID.fromString("00010203-0405-0607-0809-0a0b0c0d2b10");
    // phone --> module
    public static final UUID TX_CHAR_UUID = UUID.fromString("00010203-0405-0607-0809-0a0b0c0d2b11");
}
