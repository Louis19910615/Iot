package com.mmc.lot.ble.analysis;

import android.util.Log;

import com.mmc.lot.eventbus.AnalysisEvent;
import com.mmc.lot.util.CrcUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;

/**
 * Created by louis on 2018/3/25.
 */

public class Analysis {
    private static final String TAG = Analysis.class.getSimpleName();

    private volatile static Analysis sInstance;

    private Analysis() {
        EventBus.getDefault().register(this);
    }

    public static Analysis getInstance() {
        if (sInstance == null) {
            synchronized (Analysis.class) {
                if (sInstance == null) {
                    sInstance = new Analysis();
                }
            }
        }

        return sInstance;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void analysis(AnalysisEvent analysisEvent) {

        if (crcCkeck(analysisEvent.bytes)) {
            switch (analysisEvent.bytes[0]) {
                // 同步时间
                case 0x11:
                    syncTime(analysisEvent.bytes);
                    break;
            }
        }

    }

    private void syncTime(byte[] bytes) {
        System.out.print(Arrays.toString(bytes));
        if (bytes[1] == 0xff) {

            // TODO eventbus 同步时间失败
            Log.e(TAG, "同步时间失败");

        } else {
            // TODO eventbus 同步时间成功
            Log.e(TAG, "同步时间成功");

        }
    }

    private boolean crcCkeck(byte[] data) {

        if (data == null || data.length == 0) {
            return false;
        }

        byte[] buf = new byte[data.length];
        for (int i = 0; i < data.length - 1; i++) {
            buf[i] = data[i];
        }

        if (data[data.length - 1] == CrcUtil.calcCrc8(buf)) {
            return true;
        }

        return false;
    }
}
