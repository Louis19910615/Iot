package com.mmc.lot.ble.connect;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

import com.blakequ.bluetooth_manager_lib.connect.BluetoothConnectManager;
import com.blakequ.bluetooth_manager_lib.connect.BluetoothSubScribeData;
import com.blakequ.bluetooth_manager_lib.connect.ConnectState;
import com.blakequ.bluetooth_manager_lib.connect.ConnectStateListener;
import com.mmc.lot.IotApplication;
import com.mmc.lot.ble.ServiceUuidConstant;
import com.mmc.lot.eventbus.AnalysisEvent;
import com.mmc.lot.eventbus.EnableEvent;
import com.mmc.lot.util.CrcUtil;
import com.mmc.lot.util.DataTransfer;
import com.mmc.lot.util.DateParseUtil;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    public void connect(String deviceAddress) {

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
                Logger.e(TAG, "characteristic read is " + Arrays.toString(characteristic.getValue()));
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
                // TODO EVentbus 上报数据
                Logger.e(TAG, "characteristic change is " + Arrays.toString(characteristic.getValue()));
                EventBus.getDefault().post(new AnalysisEvent(characteristic.getValue()));

            }

            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, final int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);

                if (newState == BluetoothProfile.STATE_DISCONNECTED){
                    // TODO 通知界面断开
                }
            }

            @Override
            public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                if (status == BluetoothGatt.GATT_SUCCESS){
                    EventBus.getDefault().post(new EnableEvent(gatt.getDevice().getAddress()));
                }
            }
        });
        connectManager.connect(deviceAddress);
    }

    public void disconnect(String deviceAddress) {
        connectManager.disconnect(deviceAddress);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public boolean enableNotify(EnableEvent enableEvent) {
        BluetoothGatt gatt = connectManager.getBluetoothGatt(enableEvent.getDeviceAddress());
        if (gatt == null) {
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
                if (!isSuccess) {
                    connectManager.disconnect(enableEvent.getDeviceAddress());
                }
                return isSuccess;
            }
        }

        connectManager.disconnect(enableEvent.getDeviceAddress());
        return false;
    }

    // 同步时间
    public boolean syncTime(String deviceAddress, long time) {
        byte[] dateTime = DateParseUtil.format(time);

        byte[] data = new byte[10];
        data[0] = 0x01;
        data[1] = 0x07;
        for (int i = 0; i < dateTime.length; i++) {
            data[i + 2] = dateTime[i];
        }

        data[10] = CrcUtil.calcCrc8(data);

        return write(deviceAddress, data);
    }

    // 获取信息
    public boolean getMessage(String deviceAddress) {
        byte[] data = new byte[]{0x02, 0x00, 0x02};
        return write(deviceAddress, data);
    }

    // 上传温度数据
    public boolean uploadTemperatures(String deviceAddress, boolean isFirst) {
        if (isFirst) {
            byte[] data = new byte[]{0x03, 0x00, 0x03};
            return write(deviceAddress, data);
        } else {
            byte[] data = new byte[]{0x13, 0x00, 0x13};
            return write(deviceAddress, data);
        }
    }

    // 查询温度记录间隔
    public boolean queryInterval(String deviceAddress) {
        byte[] data = new byte[]{0x04, 0x00, 0x04};
        return write(deviceAddress, data);
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

    // 保存货单信息 data固定 = 14
    public boolean saveManifest(String deviceAddress, byte[] res, byte directiveFlag, int offset) {

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

    //

    public boolean write(String deviceAddress, byte[] bytes) {
        BluetoothGatt gatt = connectManager.getBluetoothGatt(deviceAddress);
        if (gatt == null) {
            return false;
        }
        BluetoothGattService bluetoothGattService = gatt.getService(IOT_SERVICE_UUID);
        if (bluetoothGattService != null) {
            BluetoothGattCharacteristic txCharacteristic = bluetoothGattService.getCharacteristic(ServiceUuidConstant.TX_CHAR_UUID);
            if (txCharacteristic != null) {
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
