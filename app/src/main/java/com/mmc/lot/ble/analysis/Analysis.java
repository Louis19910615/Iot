package com.mmc.lot.ble.analysis;

import android.provider.ContactsContract;
import android.util.Log;

import com.mmc.lot.data.DataCenter;
import com.mmc.lot.eventbus.ble.AnalysisEvent;
import com.mmc.lot.eventbus.ble.DisConnectEvent;
import com.mmc.lot.eventbus.ble.GetMessageEvent;
import com.mmc.lot.eventbus.ble.QueryIntervalEvent;
import com.mmc.lot.eventbus.ble.ReadManifestEvent;
import com.mmc.lot.eventbus.ble.SaveManifestEvent;
import com.mmc.lot.eventbus.ble.UploadTemperaturesEvent;
import com.mmc.lot.eventbus.http.GetTransDataEvent;
import com.mmc.lot.eventbus.http.RequestTempDataEvent;
import com.mmc.lot.eventbus.http.SendFormDataEvent;
import com.mmc.lot.eventbus.http.SendTagDataEvent;
import com.mmc.lot.eventbus.ui.GotoCharActivityEvent;
import com.mmc.lot.eventbus.ui.ShowToastEvent;
import com.mmc.lot.util.CrcUtil;
import com.mmc.lot.util.DataTransfer;
import com.mmc.lot.util.DateParseUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        Log.e(TAG, "analysis is " + Arrays.toString(analysisEvent.bytes));
        if (crcCkeck(analysisEvent.bytes)) {
            switch (analysisEvent.bytes[0]) {
                // 同步时间
                case 0x11:
                case 0x51:
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
                    if (DataCenter.getInstance().getDeviceInfo().getTemperatureDatas().size() < 3) {
                        for (int i = 0; i < 20; i++) {
                            DataCenter.SetDeviceInfo.addTemperatureData(28.45);
                        }
                    }
                    Log.e(TAG, "数据接受成功");
                    EventBus.getDefault().post(new UploadTemperaturesEvent(DataCenter.getInstance().getDeviceInfo()
                            .getDeviceAddress(),
                            false));
                    EventBus.getDefault().post(new SendTagDataEvent());
//                    EventBus.getDefault().post(new ShowToastEvent("数据接受成功, 请点击完成"));

                    break;
                case 0x73:
                    uploadTemperatures(analysisEvent.bytes);
                    EventBus.getDefault().post(new UploadTemperaturesEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress(), false));
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
                case 0x17:
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

            Log.e(TAG, "同步时间失败");
            EventBus.getDefault().post(new DisConnectEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
            EventBus.getDefault().post(new ShowToastEvent("同步时间失败，请重试"));

        } else {
            // TODO eventbus 同步时间成功
            Log.e(TAG, "同步时间成功");
            // year
            int yearHighInt = DataTransfer.byte2int(bytes[2]);
            int yearLowInt = DataTransfer.byte2int(bytes[3]);
            // month
            int monthInt = DataTransfer.byte2int(bytes[4]);
            // day
            int dayInt = DataTransfer.byte2int(bytes[5]);
            // hour
            int hourInt = DataTransfer.byte2int(bytes[6]);
            // min
            int minInt = DataTransfer.byte2int(bytes[7]);
            // sec
            int secInt = DataTransfer.byte2int(bytes[8]);

            String startTime = DateParseUtil.format(yearHighInt, yearLowInt, monthInt, dayInt, hourInt, minInt, secInt);
            DataCenter.SetDeviceInfo.setStartTime(startTime);
            Log.e(TAG, "Start time is " + startTime);
            int actor = DataCenter.getInstance().getUserInfo().getActor();
            if (actor == 1) {
                EventBus.getDefault().post(new GetMessageEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
            } else {
                if (actor == 2) {
                    EventBus.getDefault().post(new QueryIntervalEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
                } else {
                    if (actor == 3) {
                        EventBus.getDefault().post(new QueryIntervalEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
                    }
                }
            }
        }
    }

    private void getMessage(byte[] bytes) {
        System.out.print(Arrays.toString(bytes));
        if (bytes[0] == 0x12) {
            Log.e(TAG, "获取信息失败");
            EventBus.getDefault().post(new DisConnectEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
            EventBus.getDefault().post(new ShowToastEvent("获取信息失败，请重试"));
        } else {
            Log.e(TAG, "获取信息成功");
            byte[] remainingBatteryBytes = {bytes[8], bytes[9]};
            int remainingBattery = DataTransfer.byte2short(remainingBatteryBytes);
            Log.e(TAG, "remainingBattery is " + remainingBattery);
            DataCenter.SetDeviceInfo.setRemainingBattery(remainingBattery);
            int actor = DataCenter.getInstance().getUserInfo().getActor();
            if (actor == 1) {
                EventBus.getDefault().post(new SendFormDataEvent());
            } else {
                if (actor == 2) {
                    EventBus.getDefault().post(new UploadTemperaturesEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress(), true));
                } else if (actor == 3) {
                    EventBus.getDefault().post(new UploadTemperaturesEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress(), true));
                } else {

                }
            }
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
        for (int i = 0; i < (datalength - 2) / 2; i++) {
//            2i  ---  2i + 1
            Arrays.fill(data, (byte) 0x00);
            data[0] = bytes[2 * i + 4];
            data[1] = bytes[2 * i + 1 + 4];
            int dataInt = DataTransfer.byte2short(data);
            Log.e(TAG, "dataInt is " + dataInt);
            // TODO 添加进入设备管理 插入位置为offset/2 + i

            DataCenter.SetDeviceInfo.addTemperatureData(dataInt / 10.0);
        }
    }

    private void queryInterval(byte[] bytes) {
        System.out.print(Arrays.toString(bytes));
        //
        if (bytes[1] == (byte) 0xff) {
            Log.e(TAG, "获取温度时间间隔失败，请重试");
            EventBus.getDefault().post(new DisConnectEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
            EventBus.getDefault().post(new ShowToastEvent("获取时间间隔失败，请重试"));
            return;
        }
        // data
        byte[] data = new byte[]{bytes[2], bytes[3]};
        int dataInt = DataTransfer.byte2short(data);
        DataCenter.SetDeviceInfo.setTimeInterval(dataInt);
        Log.e(TAG, "获取温度时间间隔成功");
        EventBus.getDefault().post(new GetMessageEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));

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
            EventBus.getDefault().post(new SaveManifestEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
        } else {
            Log.e(TAG, "保存货单信息失败");
            EventBus.getDefault().post(new DisConnectEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
        }
    }

    private List<Byte> manifest = new ArrayList<>();
    private void readManifest(byte[] bytes) {
        System.out.print(Arrays.toString(bytes));
        if (bytes[0] == 0x17) {
            Log.e(TAG, "读取货单信息失败");
            EventBus.getDefault().post(new GetTransDataEvent(DataCenter.getInstance().getDeviceInfo().getTagId(),
                    DataCenter.getInstance().getUserInfo().getToken()));
            return;
        }
        if (bytes[1] == (byte) 0xff) {
            Log.e(TAG, "读取货单信息失败");
            EventBus.getDefault().post(new GetTransDataEvent(DataCenter.getInstance().getDeviceInfo().getTagId(),
                    DataCenter.getInstance().getUserInfo().getToken()));
            return;
        } else {
            // TODO 读取货单信息成功
//            EventBus.getDefault().post(new DisConnectEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
//            EventBus.getDefault().post(new ShowToastEvent("该Tag已有货单信息"));
        }


        // data length
        int datalength = DataTransfer.byte2int(bytes[1]);
        // offset
        byte[] offset = new byte[] {bytes[2], bytes[3]};
        int offsetInt = DataTransfer.byte2short(offset);
        // TODO 乱序
        // data
//        byte[] data = new byte[datalength];
        for (int i = 0; i < datalength; i++) {
//            data[i] = bytes[i + 4];
            // TODO 添加进入读取货单信息 插入位置为offset
            manifest.add(bytes[i + 4]);
        }
        if (bytes[0] == 0x57) {
            byte[] manifestBytes = new byte[manifest.size()];
            for (int i = 0; i < manifest.size(); i ++) {
                manifestBytes[i] = manifest.get(i);
            }
            try {
                String manifestStr = new String(manifestBytes, "UTF-8");
                Log.e(TAG, "manifest is " + manifestStr);
                DataCenter.getInstance().setLogisticsInfo(manifestStr);
                manifest.clear();
                EventBus.getDefault().post(new ReadManifestEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress(), false));
                EventBus.getDefault().post(new DisConnectEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
                EventBus.getDefault().post(new ShowToastEvent("该Tag已有货单信息:" + manifestStr));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                EventBus.getDefault().post(new DisConnectEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
                EventBus.getDefault().post(new ShowToastEvent("异常断开，请重试。"));
            }
        }
    }

    private void activate(byte[] bytes) {
//        if (bytes[1] == 0x00) {
            int actor = DataCenter.getInstance().getUserInfo().getActor();
            if (actor == 1) {
                EventBus.getDefault().post(new ShowToastEvent("已完成"));
                EventBus.getDefault().post(new DisConnectEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
            } else {
                if (actor == 2) {
                    EventBus.getDefault().post(new DisConnectEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
                    EventBus.getDefault().post(new RequestTempDataEvent());
                } else {
                    if (actor == 3) {
                        EventBus.getDefault().post(new DisConnectEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
                        EventBus.getDefault().post(new GotoCharActivityEvent());
                    } else {

                    }
                }
            }
//        } else {
//            //TODO 服务器确认失败
//            EventBus.getDefault().post(new ShowToastEvent("与Tag确认失败，请重试"));
//            EventBus.getDefault().post(new DisConnectEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
//        }
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
