package com.mmc.lot;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.blakequ.bluetooth_manager_lib.BleManager;
import com.blakequ.bluetooth_manager_lib.connect.BluetoothConnectManager;
import com.blakequ.bluetooth_manager_lib.connect.ConnectState;
import com.blakequ.bluetooth_manager_lib.connect.ConnectStateListener;
import com.blakequ.bluetooth_manager_lib.connect.GattError;
import com.blakequ.bluetooth_manager_lib.device.resolvers.GattAttributeResolver;
import com.blakequ.bluetooth_manager_lib.scan.BluetoothScanManager;
import com.blakequ.bluetooth_manager_lib.scan.ScanOverListener;
import com.blakequ.bluetooth_manager_lib.scan.bluetoothcompat.ScanCallbackCompat;
import com.blakequ.bluetooth_manager_lib.scan.bluetoothcompat.ScanFilterCompat;
import com.blakequ.bluetooth_manager_lib.scan.bluetoothcompat.ScanResultCompat;
import com.blakequ.bluetooth_manager_lib.util.BluetoothUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MainActivity";

    private EditText etID, etMsgID;
    private ImageView ivCamera,ivMsgCamera;
    private TextView tvFinish;
    private BluetoothUtils mBluetoothUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        mBluetoothUtils = BluetoothUtils.getInstance(this);
//        bleTest();
//        scan(this);
    }

    private void initView() {
        ImageView ivBack = (ImageView) findViewById(R.id.iv_title_bar_back);
        ivBack.setVisibility(View.GONE);

        TextView title = (TextView) findViewById(R.id.tv_title_bar_title);
        title.setText("发货方");

        ImageView ivSet = (ImageView) findViewById(R.id.iv_set);
        ivSet.setVisibility(View.VISIBLE);


        ivCamera = (ImageView) findViewById(R.id.iv_camera);
        ivCamera.setOnClickListener(this);

        ivMsgCamera = (ImageView) findViewById(R.id.iv_send_camera);
        ivMsgCamera.setOnClickListener(this);

        tvFinish = (TextView) findViewById(R.id.tv_finish);
        tvFinish.setOnClickListener(this);

        etID = (EditText) findViewById(R.id.et_id);
        etMsgID = (EditText) findViewById(R.id.et_send_id);

        etID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etID.setText(s);
                etID.setSelection(s.length());
            }
        });

        etMsgID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etMsgID.setText(s);
                etMsgID.setSelection(s.length());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        if (v == tvFinish) {
            Intent intent = new Intent(this, SendDetailActivity.class);
            startActivity(intent);
        }
    }

    private void bleTest() {
        if (checkLocationPermission()) {
            if (isGpsProviderEnabled(this)) {
                checkIsBleState();
            } else {
                startLocationSettings(this, 12);
            }
        } else {
            requestLocationPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        if (requestCode == 10001)
        {
            if (grantResults == null || grantResults.length == 0) {
                return;
            }
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "已获取位置权限", Toast.LENGTH_LONG).show();
            }
            else
            {
                gotoOpenPermission();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data)
    {
        Log.e("MainActivity", "requestCode:" + requestCode + ", resultCode:" + resultCode);
        if (requestCode == 12) {
            if (isGpsProviderEnabled(this)) {
                Toast.makeText(this, "已打开定位", Toast.LENGTH_LONG).show();
                checkIsBleState();
            } else {
                Toast.makeText(this, "已拒绝打开定位", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == 2001) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "已打开蓝牙", Toast.LENGTH_LONG).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "已拒绝打开蓝牙", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private boolean checkLocationPermission() {
        return checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) || checkPermission(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private boolean checkPermission(final String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 10001);

    }

    private void gotoOpenPermission()
    {
        Toast.makeText(this, "请在设置界面打开位置权限", Toast.LENGTH_LONG).show();
    }

    public static boolean isGpsProviderEnabled(Context context){
        LocationManager service = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        return service.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private boolean checkIsBleState(){
        if (!BluetoothUtils.isBluetoothLeSupported(this)){
            Toast.makeText(this, "此设备不支持蓝牙", Toast.LENGTH_LONG).show();
        }else if(!mBluetoothUtils.isBluetoothIsEnable()){
            mBluetoothUtils.askUserToEnableBluetoothIfNeeded(this);
        }else{
            return true;
        }
        return false;
    }

    public static void startLocationSettings(Activity context, int requestCode){
        Intent enableLocationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivityForResult(enableLocationIntent, requestCode);
    }

    private boolean isConnectting = false;
    public void scan(Context context) {
        final BluetoothScanManager scanManager = BleManager.getScanManager(context);
        scanManager.addScanFilterCompats(new ScanFilterCompat.Builder().setDeviceName("tModul").build());
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
                String deviceName = result.getScanRecord().getDeviceName();
                Logger.i("scan device "+result.getLeDevice().getAddress()+" "+deviceName);
                if (!isConnectting) {
                    scanManager.stopCycleScan();
                    connect(result.getLeDevice().getAddress(), deviceName);
                    isConnectting = true;
                }
            }

            @Override
            public void onBatchScanResults(List<ScanResultCompat> results) {
                super.onBatchScanResults(results);
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
            }
        });

//        scanManager.startCycleScan();
        scanManager.startScanNow();
    }

    private void connect(String deviceAddress, String deviceName) {
        final BluetoothConnectManager connectManager = BluetoothConnectManager.getInstance(this);
        ConnectStateListener stateListener = new ConnectStateListener() {
            @Override
            public void onConnectStateChanged(String address, ConnectState state) {
                switch (state) {
                    case CONNECTED:
                        Logger.e(TAG, "connected");
                        break;
                    case CONNECTING:
                        Logger.e(TAG, "connecting");
                        break;
                    case NORMAL:
                        Logger.e(TAG, "normal");
                        break;
                }
            }
        };
        connectManager.addConnectStateListener(stateListener);
        connectManager.setBluetoothGattCallback(new BluetoothGattCallback() {

            @Override
            public void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.e(TAG, "success to read characteristic");
                }else{
                    Log.e(TAG, "fail to read characteristic");
                }
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.e(TAG, "success to write characteristic");
                }else{
                    Log.e(TAG, "fail to write characteristic");
                }
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
            }

            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, final int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                if (newState == BluetoothProfile.STATE_DISCONNECTED){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Disconnect! error："+ GattError.parseConnectionError(status), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                if (status == BluetoothGatt.GATT_SUCCESS){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // gatt.getService(UUID.fromString("00010203-0405-0607-0809-0a0b0c0d2b10"));
                            // Read Notify UUID
                            // 00010203-0405-0607-0809-0a0b0c0d2b10
                            // Read Write Without Response
                            // 00010203-0405-0607-0809-0a0b0c0d2b11
                            displayGattServices(gatt.getServices());
                            connectManager.startSubscribe(gatt);
                        }
                    });
                }
            }
        });
        connectManager.connect(deviceAddress);
    }

    private void displayGattServices(final List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;

        String uuid = null;

        // Loops through available GATT Services.
        for (final BluetoothGattService gattService : gattServices) {

            final List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            final List<BluetoothGattCharacteristic> charas = new ArrayList<>();

            // Loops through available Characteristics.
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                uuid = gattCharacteristic.getUuid().toString();
                String property = getPropertyString(gattCharacteristic.getProperties());
                System.out.println("-----property is:" + property);
                System.out.println("-----chat uuid:"+ uuid);
                for (BluetoothGattDescriptor gattDescriptor:gattCharacteristic.getDescriptors()){
                    System.out.println("--------des name:" + gattDescriptor.getUuid());
                    System.out.println("--------des uuid:" + gattDescriptor.getValue()+" "+gattDescriptor.getPermissions());
                }
            }
        }

    }

    private String getPropertyString(int property){
        StringBuilder sb = new StringBuilder("(");
        //Read
        if ((property & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
            sb.append("Read ");
        }
        //Write
        if ((property & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0
                || (property & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
            sb.append("Write ");
        }
        //Notify
        if ((property & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0
                || (property & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
            sb.append("Notity Indicate ");
        }
        //Broadcast
        if ((property & BluetoothGattCharacteristic.PROPERTY_BROADCAST) > 0){
            sb.append("Broadcast ");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }


}
