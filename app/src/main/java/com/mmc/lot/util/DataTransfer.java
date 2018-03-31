package com.mmc.lot.util;

/**
 * Created by louis on 2018/3/28.
 */

public class DataTransfer {

    public static short byte2short(byte[] res) {
        short targets = (short) (((res[0] & 0x00ff) << 8) | (res[1] & 0x00ff));
        return targets;
    }

    public static byte[] short2byte(short res) {
        byte[] targets = new byte[2];
        targets[0]  = (byte) (0x00ff & (res >> 8));
        targets[1] = (byte) (0x00ff & res);
        return targets;
    }

    public static int byte2int(byte res) {
        return (res & 0xff);
    }
}
