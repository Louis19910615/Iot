package com.mmc.lot.util;

/**
 * Created by louis on 2018/3/28.
 */

public class PrintHexBinary {
    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    public static String print(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }

        System.out.print(r.toString());
        return r.toString();
    }
}
