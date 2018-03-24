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
import com.mmc.lot.eventbus.EnableEvent;
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

    private static BluetoothConnectManager connectManager;

    private static void init() {
        if (connectManager == null) {
            connectManager = BluetoothConnectManager.getInstance(IotApplication.getContext());
        }
    }

    public static void connect(String deviceAddress) {

        init();

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

    public static void disconnect(String deviceAddress) {
        connectManager.disconnect(deviceAddress);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public static boolean enableNotify(EnableEvent enableEvent) {
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

    public static boolean write(String deviceAddress, byte[] bytes) {
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
