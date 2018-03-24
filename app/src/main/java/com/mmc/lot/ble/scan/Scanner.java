package com.mmc.lot.ble.scan;

import com.blakequ.bluetooth_manager_lib.BleManager;
import com.blakequ.bluetooth_manager_lib.scan.BluetoothScanManager;
import com.blakequ.bluetooth_manager_lib.scan.ScanOverListener;
import com.blakequ.bluetooth_manager_lib.scan.bluetoothcompat.ScanCallbackCompat;
import com.blakequ.bluetooth_manager_lib.scan.bluetoothcompat.ScanFilterCompat;
import com.blakequ.bluetooth_manager_lib.scan.bluetoothcompat.ScanResultCompat;
import com.mmc.lot.IotApplication;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by louis on 2018/3/24.
 */

public class Scanner {

    private static final String TAG = Scanner.class.getSimpleName();

    private static BluetoothScanManager scanManager;

    private static ScanResultCompat scanResultCompat;

    public void scanWithAddress(String address) {
        scanManager.addScanFilterCompats(new ScanFilterCompat.Builder()
                .setDeviceAddress(address).build());
        scan();
    }

    // name example: tModul
    public void scanWithName(String name) {
        scanManager.addScanFilterCompats(new ScanFilterCompat.Builder()
                .setDeviceName(name).build());
        scan();
    }

    public void scan() {

        init();

        scanManager.setScanOverListener(new ScanOverListener() {
            @Override
            public void onScanOver() {
                // scan over of one times
            }
        });
        scanManager.setScanCallbackCompat(new ScanCallbackCompat() {
            @Override
            public void onScanResult(int callbackType, ScanResultCompat result) {
                super.onScanResult(callbackType, result);
                scanManager.stopCycleScan();
                Logger.i(TAG, "scan device is " + result.getScanRecord().getDeviceName()
                        + " : " + result.getLeDevice().getAddress());
                if (scanResultCompat == null || !(result.getLeDevice().getAddress()
                        .equals(scanResultCompat.getLeDevice().getAddress()))) {
                    scanResultCompat = result;
                }
                // TODO EventBus 传递给connect Address
            }

            @Override
            public void onBatchScanResults(List<ScanResultCompat> results) {
                super.onBatchScanResults(results);
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                // TODO EventBus 传递给界面，重新开始扫描
            }
        });

        scanManager.startScanOnce();
    }

    public static void stopScan() {
        scanManager.stopCycleScan();
    }

    public static boolean isScanning() {
        return scanManager.isScanning();
    }

    private static void init() {
        if (scanManager == null) {
            BleManager.getScanManager(IotApplication.getContext());
        }
    }
}
