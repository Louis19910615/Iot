package com.mmc.lot.ble.scan;

import android.util.Log;

import com.blakequ.bluetooth_manager_lib.BleManager;
import com.blakequ.bluetooth_manager_lib.scan.BluetoothScanManager;
import com.blakequ.bluetooth_manager_lib.scan.ScanOverListener;
import com.blakequ.bluetooth_manager_lib.scan.bluetoothcompat.ScanCallbackCompat;
import com.blakequ.bluetooth_manager_lib.scan.bluetoothcompat.ScanFilterCompat;
import com.blakequ.bluetooth_manager_lib.scan.bluetoothcompat.ScanResultCompat;
import com.mmc.lot.IotApplication;
import com.mmc.lot.data.DataCenter;
import com.mmc.lot.eventbus.ble.ConnectEvent;
import com.mmc.lot.eventbus.ble.ScanWithAddressEvent;
import com.mmc.lot.eventbus.ble.ScanWithNameEvent;
import com.mmc.lot.eventbus.ui.ShowToastEvent;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by louis on 2018/3/24.
 */

public class Scanner {

    private static final String TAG = Scanner.class.getSimpleName();

    private static BluetoothScanManager scanManager;

    private static ScanResultCompat scanResultCompat;

    // C7:E4:E3:E2:E1:FE
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void scanWithAddress(ScanWithAddressEvent scanWithAddressEvent) {
        scanManager.addScanFilterCompats(new ScanFilterCompat.Builder()
                .setDeviceAddress(scanWithAddressEvent.getDeviceAddress()).build());
        scan();
    }

    // name example: tModul

    public void scanWithName(ScanWithNameEvent scanWithNameEvent) {
        Log.e(TAG, "scanWithName is " + scanWithNameEvent.getDeviceName());
        scanManager.addScanFilterCompats(new ScanFilterCompat.Builder()
                .setDeviceName(scanWithNameEvent.getDeviceName()).build());
        scan();
    }


    public void scan() {

        // reset scanResultCompat
        scanResultCompat = null;

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
                EventBus.getDefault().post(new ShowToastEvent("扫描成功"));

                stopScan();
                Log.e(TAG, "scan device is " + result.getLeDevice().getName()
                        + " : " + result.getLeDevice().getAddress());
                if (scanResultCompat == null) {
                    Logger.e(TAG, "onScanResult send getLeDevice");
                    scanResultCompat = result;
                    // clear related info
                    DataCenter.getInstance().clearDeviceInfo();
                    DataCenter.getInstance().clearLogisticsInfo();
                    // set related info
                    DataCenter.SetDeviceInfo.setDeviceAddress(result.getLeDevice().getAddress());
                    DataCenter.SetDeviceInfo.setDeviceName(result.getLeDevice().getName());
                    EventBus.getDefault().post(new ConnectEvent(result.getLeDevice().getAddress()));
                }
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
            scanManager = BleManager.getScanManager(IotApplication.getContext());
        }
    }

    private static Scanner sInstance;

    private Scanner () {
        init();
    }

    public static Scanner getInstance() {
        if (sInstance == null) {
            synchronized (Scanner.class) {
                if (sInstance == null) {
                    sInstance = new Scanner();
                    EventBus.getDefault().register(sInstance);
                }
            }
        }

        return sInstance;
    }
}
