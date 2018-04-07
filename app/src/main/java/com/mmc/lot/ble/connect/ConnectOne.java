package com.mmc.lot.ble.connect;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Handler;
import android.util.EventLog;
import android.util.Log;

import com.blakequ.bluetooth_manager_lib.connect.BluetoothConnectManager;
import com.blakequ.bluetooth_manager_lib.connect.BluetoothSubScribeData;
import com.blakequ.bluetooth_manager_lib.connect.ConnectState;
import com.blakequ.bluetooth_manager_lib.connect.ConnectStateListener;
import com.mmc.lot.IotApplication;
import com.mmc.lot.data.DataCenter;
import com.mmc.lot.eventbus.ble.GetMessageEvent;
import com.mmc.lot.eventbus.ble.QueryIntervalEvent;
import com.mmc.lot.eventbus.ble.ReadManifestEvent;
import com.mmc.lot.eventbus.ble.ResetTagEvent;
import com.mmc.lot.eventbus.ble.SaveManifestEvent;
import com.mmc.lot.eventbus.ble.SyncTimeEvent;
import com.mmc.lot.ble.ServiceUuidConstant;
import com.mmc.lot.eventbus.ble.AnalysisEvent;
import com.mmc.lot.eventbus.ble.ConnectEvent;
import com.mmc.lot.eventbus.ble.DisConnectEvent;
import com.mmc.lot.eventbus.ble.EnableEvent;
import com.mmc.lot.eventbus.ble.UploadTemperaturesEvent;
import com.mmc.lot.eventbus.http.GetTransDataEvent;
import com.mmc.lot.eventbus.ui.ShowToastEvent;
import com.mmc.lot.util.CrcUtil;
import com.mmc.lot.util.DataTransfer;
import com.mmc.lot.util.DateParseUtil;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static com.mmc.lot.ble.ServiceUuidConstant.IOT_SERVICE_UUID;

/**
 * Created by louis on 2018/3/25.
 */

public class ConnectOne {

    private static final String TAG = ConnectOne.class.getSimpleName();
    private volatile static ConnectOne sInstance;
    private BluetoothConnectManager connectManager;

    private ConnectOne () {
        init();
    }

    public static ConnectOne getInstance() {
        if (sInstance == null) {
            synchronized (ConnectOne.class) {
                if (sInstance == null) {
                    sInstance = new ConnectOne();
                }
            }
        }

        return sInstance;
    }

    private void init() {

        EventBus.getDefault().register(this);
        if (connectManager == null) {
            connectManager = BluetoothConnectManager.getInstance(IotApplication.getContext());
        }

    }

    private boolean isDiscovered = false;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void connect(ConnectEvent connectEvent) {
        Logger.e(TAG, "start connect");

        isDiscovered = false;
        ConnectStateListener stateListener = new ConnectStateListener() {
            @Override
            public void onConnectStateChanged(String address, ConnectState state) {
                switch (state) {
                    case CONNECTED:
                        Logger.i(TAG, "connected");

                        break;
                    case CONNECTING:
                        Logger.i(TAG, "connecting");
                        break;
                    case NORMAL:
                        Logger.i(TAG, "normal");
                        break;
                }
            }
        };
        connectManager.addConnectStateListener(stateListener);
        connectManager.setBluetoothGattCallback(new BluetoothGattCallback() {

            @Override
            public void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
                Log.e(TAG, "characteristic read is " + Arrays.toString(characteristic.getValue()));
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Logger.i(TAG, "success to read characteristic");
                }else{
                    Logger.i(TAG, "fail to read characteristic");
                }
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Logger.e(TAG, "success to write characteristic");
                }else{
                    Logger.e(TAG, "fail to write characteristic");
                }
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                Log.e(TAG, "characteristic change is " + Arrays.toString(characteristic.getValue()));
                EventBus.getDefault().post(new AnalysisEvent(characteristic.getValue()));

            }

            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, final int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
                    EventBus.getDefault().post(new ShowToastEvent("连接成功"));
                    gatt.discoverServices();
                } else if (status == 133) {
                        Logger.e(TAG, "-----133-----");
                    }
                else if (newState == BluetoothProfile.STATE_DISCONNECTED){
                    // TODO 通知界面断开
                }
            }

            @Override
            public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                if (status == BluetoothGatt.GATT_SUCCESS){
                    Logger.e(TAG, "onServicesDiscovered success.");
                    if (!isDiscovered) {
                        isDiscovered = true;
                        EventBus.getDefault().post(new EnableEvent(gatt.getDevice().getAddress()));
                    }
                }
            }
        });
        connectManager.connect(connectEvent.getDeviceAddress());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disconnect(DisConnectEvent disConnectEvent) {
        connectManager.disconnect(disConnectEvent.getDeviceAddress());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public boolean enableNotify(final EnableEvent enableEvent) {
        Logger.e(TAG, "enableNotify start");
        BluetoothGatt gatt = connectManager.getBluetoothGatt(enableEvent.getDeviceAddress());
        if (gatt == null) {
            Logger.e(TAG, "gatt is null.");
            connectManager.disconnect(enableEvent.getDeviceAddress());
            return false;
        }
        BluetoothGattService bluetoothGattService = gatt.getService(IOT_SERVICE_UUID);
        if (bluetoothGattService != null) {
            BluetoothGattCharacteristic rxCharacteristic = bluetoothGattService.getCharacteristic(ServiceUuidConstant.RX_CHAR_UUID);
            if (rxCharacteristic != null) {
                connectManager.setServiceUUID(bluetoothGattService.getUuid().toString());
                connectManager.addBluetoothSubscribeData(
                        new BluetoothSubScribeData.Builder().setCharacteristicNotify(rxCharacteristic.getUuid()).build());

                boolean isSuccess = connectManager.startSubscribe(gatt);
                Log.e(TAG, "RX_CHAR_UUID start subscribe is " + isSuccess);
                if (isSuccess) {
                    Log.e(TAG, "使能成功");
//                    EventBus.getDefault().post(new ShowToastBean("使能成功"));
                    int actor = DataCenter.getInstance().getUserInfo().getActor();
                    Log.e(TAG, "actor is " + actor);
                    // send
                    if (actor == 1) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(TAG, "开始从Tag获取货单信息");
                                EventBus.getDefault().post(new ReadManifestEvent(enableEvent.getDeviceAddress(), true));
                            }
                        }, 2000);
                    } else {
                        // take
                        if (actor == 2) {
                            EventBus.getDefault().post(new GetTransDataEvent(DataCenter.getInstance().getDeviceInfo().getTagId(), DataCenter.getInstance().getUserInfo().getToken()));
                        } else {
                            // 快递员
                            if (actor == 3) {
                                EventBus.getDefault().post(new GetTransDataEvent(DataCenter.getInstance().getDeviceInfo().getTagId(), DataCenter.getInstance().getUserInfo().getToken()));
                            }
                        }
                    }
                } else {
                    connectManager.disconnect(enableEvent.getDeviceAddress());
                }
                return isSuccess;
            }
        }

        Log.e(TAG, "使能失败");
        connectManager.disconnect(enableEvent.getDeviceAddress());
        return false;
    }

    // 同步时间
    @Subscribe(threadMode = ThreadMode.MAIN)
    public boolean syncTime(SyncTimeEvent syncTimeEvent) {
        Log.e(TAG, "syncTime start");
        byte[] dateTime = DateParseUtil.format(syncTimeEvent.getTime());

        byte[] data = new byte[10];
        data[0] = 0x01;
        data[1] = 0x07;
        for (int i = 0; i < dateTime.length; i++) {
            data[i + 2] = dateTime[i];
        }

        data[9] = CrcUtil.calcCrc8(data);

        return write(syncTimeEvent.getDeviceAddress(), data);
    }

    // 获取信息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public boolean getMessage(GetMessageEvent getMessageEvent) {
        byte[] data = new byte[]{0x02, 0x00, 0x02};
        return write(getMessageEvent.getDeviceAddress(), data);
    }

    // 上传温度数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public boolean uploadTemperatures(UploadTemperaturesEvent uploadTemperaturesEvent) {
        Log.e(TAG, "uploadTemperatures execute.");
        if (uploadTemperaturesEvent.getIsFirst()) {
            byte[] data = new byte[]{0x03, 0x00, 0x03};
            return write(uploadTemperaturesEvent.getDeviceAddress(), data);
        } else {
            byte[] data = new byte[]{0x13, 0x00, 0x13};
            return write(uploadTemperaturesEvent.getDeviceAddress(), data);
        }
    }

    // 查询温度记录间隔
    @Subscribe(threadMode = ThreadMode.MAIN)
    public boolean queryInterval(QueryIntervalEvent queryIntervalEvent) {
        byte[] data = new byte[]{0x04, 0x00, 0x04};
        return write(queryIntervalEvent.getDeviceAddress(), data);
    }

    // 设置温度记录间隔
    public boolean setInterval(String deviceAddress, int min) {
        byte[] minByte = DataTransfer.short2byte((short) min);
        byte[] data = new byte[5];
        data[0] = 0x05;
        data[1] = 0x02;
        data[2] = minByte[0];
        data[3] = minByte[1];
        data[4] = CrcUtil.calCrc8(data);

        return write(deviceAddress, data);
    }

    private String manifestStr;
    public void setManifestStr(String manifestStr) {
        this.manifestStr = manifestStr;
    }

    private int saveNum;
    public void resetSaveNum() {
        this.saveNum = 0;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public boolean saveManifest(SaveManifestEvent saveManifestEvent) {
        Log.e(TAG, "save manifest start.");
        // string --> byte[]
        try {
            byte[] resBytes = manifestStr.getBytes("UTF-8");
            byte[] resByteOne = new byte[14];
            byte flag = 0x20;

//            for (int i = 0; i < resBytes.length; i = i * 14) {
            if (saveNum < resBytes.length) {
                Arrays.fill(resByteOne, (byte) 0xff);
                for (int j = 0; j < 14; j++) {
                    if (saveNum + j < resBytes.length) {
                        resByteOne[j] = resBytes[saveNum + j];
                    } else {
                        flag = 0x00;
                        break;
                    }
                }
                saveManifest(saveManifestEvent.getDeviceAddress(), resByteOne, flag, saveNum);
                saveNum++;
                saveNum = saveNum * 14;
                Log.e(TAG, "save num is " + saveNum);
            } else {
                Log.e(TAG, "保存货单信息成功");
                EventBus.getDefault().post(new SyncTimeEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress(), System.currentTimeMillis()));
            }
//            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new DisConnectEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
            EventBus.getDefault().post(new ShowToastEvent("设置运单信息错误，请重试"));
            return false;
        }

        return true;
    }

    // 保存货单信息 data固定 = 14
//    public boolean saveManifest(String deviceAddress, String res) {
//        // string --> byte[]
//        try {
//            byte[] resBytes = res.getBytes("UTF-8");
//            byte[] resByteOne = new byte[14];
//            byte flag = 0x20;
//
//            for (int i = 0; i < resBytes.length; i = i * 14) {
//                Arrays.fill(resByteOne, (byte) 0xff);
//                for (int j = 0; j < 14; j++) {
//                    if (i + j < resBytes.length) {
//                        resByteOne[j] = resBytes[i + j];
//                    } else {
//                        flag = 0x00;
//                        break;
//                    }
//                }
//                saveManifest(deviceAddress, resByteOne, flag, i);
//                i++;
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//        return true;
//    }

    // 保存货单信息 data固定 = 14 单次
    private boolean saveManifest(String deviceAddress, byte[] res, byte directiveFlag, int offset) {

        byte[] data = new byte[19];
        if (directiveFlag == 0x00) {
            data[0] = 0x06;
        } else {
            data[0] = (byte) (0x06 & directiveFlag);
        }

        data[1] = 0x11;
        byte[] offsetByte = DataTransfer.short2byte((short) offset);
        data[2] = offsetByte[0];
        data[3] = offsetByte[1];

        for (int i = 0; i < 14; i++) {
            if (res.length > i) {
                data[i + 3] = res[i];
            } else {
                data[i + 3] = (byte) 0xff;
            }
        }

        data[18] = CrcUtil.calCrc8(data);

        return write(deviceAddress, data);
    }

    // 读取货单信息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public boolean readManifest(ReadManifestEvent readManifestEvent) {
        byte[] data = new byte[3];
        if (readManifestEvent.getIsFirst()) {
            data[0] = 0x07;
            data[1] = 0x00;
            data[2] = 0x07;
        } else {
            data[0] = 0x17;
            data[1] = 0x00;
            data[2] = 0x17;
        }
        return write(readManifestEvent.getDeviceAddress(), data);
    }

    // 服务器确认
    @Subscribe(threadMode = ThreadMode.MAIN)
    public boolean activate(String deviceAddress) {
        byte[] data = new byte[]{0x08, 0x00, 0x08};
        return write(deviceAddress, data);

    }

    // 重置Tag
    @Subscribe(threadMode = ThreadMode.MAIN)
    public boolean resetTag(ResetTagEvent resetTagEvent) {
        byte[] data = new byte[]{0x09, 0x00, 0x09};
        return write(resetTagEvent.getDeviceAddress(), data);
    }

    public boolean write(String deviceAddress, byte[] bytes) {
        BluetoothGatt gatt = connectManager.getBluetoothGatt(deviceAddress);
        if (gatt == null) {
            return false;
        }
        BluetoothGattService bluetoothGattService = gatt.getService(IOT_SERVICE_UUID);
        if (bluetoothGattService != null) {
            BluetoothGattCharacteristic txCharacteristic = bluetoothGattService.getCharacteristic(ServiceUuidConstant.TX_CHAR_UUID);
            if (txCharacteristic != null) {
                connectManager.setServiceUUID(bluetoothGattService.getUuid().toString());
                connectManager.addBluetoothSubscribeData(
                        new BluetoothSubScribeData.Builder().setCharacteristicWrite(txCharacteristic.getUuid(), bytes).build()
                );
                boolean isSuccess = connectManager.startSubscribe(gatt);
                Log.e(TAG, "TX_CHAR_UUID start subscribe is " + isSuccess);
                return isSuccess;
            }
        }
        return false;
    }
}
