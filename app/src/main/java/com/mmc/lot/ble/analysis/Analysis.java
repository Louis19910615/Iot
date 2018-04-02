package com.mmc.lot.ble.analysis;

import android.util.Log;

import com.mmc.lot.bean.ShowToastBean;
import com.mmc.lot.ble.device.DeviceInfo;
import com.mmc.lot.eventbus.AnalysisEvent;
import com.mmc.lot.eventbus.UploadTemperaturesEvent;
import com.mmc.lot.util.CrcUtil;
import com.mmc.lot.util.DataTransfer;

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

                // 获取信息
                case 0x12:
                case 0x52:
                    getMessage(analysisEvent.bytes);
                    break;

                // 上传温度数据
                case 0x53:
                    uploadTemperatures(analysisEvent.bytes);
                    if (DeviceInfo.getInstance().getTempDatas().size() < 3) {
                        for (int i = 0; i < 20; i++) {
                            DeviceInfo.getInstance().addTempData(28.45, 2);
                        }
                    }
                    EventBus.getDefault().post(new UploadTemperaturesEvent(DeviceInfo.getInstance().getDeviceAddress(), false));
                    EventBus.getDefault().post(new ShowToastBean("数据接受成功, 请点击完成"));

                    // TODO 服务端上传数据
                    break;
                case 0x73:
                    uploadTemperatures(analysisEvent.bytes);
                    EventBus.getDefault().post(new UploadTemperaturesEvent(DeviceInfo.getInstance().getDeviceAddress(), false));
                    break;

                // 查询温度记录间隔
                case 0x14:
                case 0x54:
                    queryInterval(analysisEvent.bytes);
                    break;

                // 设置温度记录
                case 0x15:
                    setInterval(analysisEvent.bytes);
                    break;

                // 保存货单信息
                case 0x16:
                    saveManifest(analysisEvent.bytes);
                    break;

                // 读取货单信息
                case 0x77:
                case 0x57:
                    readManifest(analysisEvent.bytes);
                    break;

                // 服务器确认
                case 0x18:
                    activate(analysisEvent.bytes);
                    break;

                // 重置Tag
                case 0x19:
                    resetTag(analysisEvent.bytes);
                    break;

                default:
                    System.out.print(Arrays.toString(analysisEvent.bytes));
                    break;


            }
        }

    }

    private void syncTime(byte[] bytes) {
        System.out.print(Arrays.toString(bytes));
        if (bytes[1] == (byte) 0xff) {

            // TODO eventbus 同步时间失败
            Log.e(TAG, "同步时间失败");

        } else {
            // TODO eventbus 同步时间成功
            Log.e(TAG, "同步时间成功");

        }
    }

    private void getMessage(byte[] bytes) {
        System.out.print(Arrays.toString(bytes));
        if (bytes[0] == 0x12) {
            // TODO eventbus 获取信息失败
            Log.e(TAG, "获取信息失败");
        } else {
            // TODO eventbus 获取信息成功
            Log.e(TAG, "获取信息成功");
        }
    }

    private void uploadTemperatures(byte[] bytes) {
        System.out.print(Arrays.toString(bytes));
        // data length
        int datalength = DataTransfer.byte2int(bytes[1]);
        // offset
        byte[] offset = new byte[] {bytes[2], bytes[3]};
        int offsetInt = DataTransfer.byte2short(offset);
        Log.e(TAG, "offsetInt is " + offsetInt);
        // TODO 乱序
        // data
        byte[] data = new byte[2];
        for (int i = 0; i < datalength / 2; i++) {
//            2i  ---  2i + 1
            Arrays.fill(data, (byte) 0x00);
            data[0] = bytes[2 * i + 4];
            data[1] = bytes[2 * i + 1 + 4];
            int dataInt = DataTransfer.byte2short(data);
            Log.e(TAG, "dataInt is " + dataInt);
            // TODO 添加进入设备管理 插入位置为offset + i

            DeviceInfo.getInstance().addTempData(dataInt / 10.0, (offsetInt + i));
        }
    }

    private void queryInterval(byte[] bytes) {
        System.out.print(Arrays.toString(bytes));
        //
        if (bytes[1] == (byte) 0xff) {
            // TODO 获取温度间隔失败
            return;
        }
        // data
        byte[] data = new byte[]{bytes[2], bytes[3]};
        int dataInt = DataTransfer.byte2short(data);
        // TODO 记录温度记录间隔
    }

    private void setInterval(byte[] bytes) {
        System.out.print(Arrays.toString(bytes));
        //
        if (bytes[1] == 0x00) {
            // TODO 设置温度记录间隔成功
        } else {
            // TODO 设置温度记录间隔失败
        }
    }

    private void saveManifest(byte[] bytes) {
        System.out.print(Arrays.toString(bytes));
        if (bytes[1] == 0x00) {
            // TODO 保存货单信息成功
        } else {
            // TODO 保存货单信息失败
        }
    }

    private void readManifest(byte[] bytes) {
        System.out.print(Arrays.toString(bytes));
        if (bytes[1] == (byte) 0xff) {
            // TODO 读取货单信息失败
            return;
        }

        // data length
        int datalength = DataTransfer.byte2int(bytes[1]);
        // offset
        byte[] offset = new byte[] {bytes[2], bytes[3]};
        int offsetInt = DataTransfer.byte2short(offset);
        // TODO 乱序
        // data
        byte[] data = new byte[datalength];
        for (int i = 0; i < datalength; i++) {
            data[i] = bytes[i + 4];
            // TODO 添加进入读取货单信息 插入位置为offset
        }
    }

    private void activate(byte[] bytes) {
        if (bytes[1] == 0x00) {
            // TODO 服务器确认成功
        } else {
            //TODO 服务器确认失败
        }
    }

    private void resetTag(byte[] bytes) {
        if (bytes[1] == 0x00) {
            // TODO 重置Tag成功
        } else {
            // TODO 重置Tag失败
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

        if (data[data.length - 1] == CrcUtil.calCrc8(buf)) {
            return true;
        }

        return false;
    }
}
