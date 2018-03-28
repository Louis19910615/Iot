package com.mmc.lot.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blakequ.bluetooth_manager_lib.util.BluetoothUtils;
import com.mmc.lot.activity.SendDetailActivity;
import com.mmc.lot.activity.SettingActivity;
import com.mmc.lot.ble.ServiceUuidConstant;
import com.mmc.lot.util.CrcUtil;
import com.mmc.lot.util.DataTransfer;
import com.mmc.lot.util.DateParseUtil;
import com.mmc.lot.util.PrintHexBinary;
import com.orhanobut.logger.Logger;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import com.mmc.lot.R;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MainActivity";

    private EditText etID, etMsgID;
    private ImageView ivCamera, ivMsgCamera, ivBack, ivSet;
    private TextView tvFinish;
    private BluetoothUtils mBluetoothUtils;
    private RelativeLayout rlTemp;
    private TextView tvTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        mBluetoothUtils = BluetoothUtils.getInstance(this);
//        bleTest();
        Log.e(TAG, "你好大北京");
        try {
            byte[] bytes = new String("你好大北京").getBytes("UTF8");
            String str = new String(bytes, "UTF8");
            Log.e(TAG, "str is " + str);
            Byte temp = Byte.valueOf("20");
            Log.e(TAG, "temp is " + String.valueOf(temp));
            byte[] bytes1 = new byte[]{0x09, (byte) 0xFA};
            Log.e(TAG, "equal is " + (bytes1[1] == (byte) 0xFA));
            byte[] bytes2 = new byte[] {(byte) 0xFA, (byte) 0xFE};
            int bytes20 = (bytes2[0] & 0xff);
            int bytes21 = (bytes2[1] & 0xff);
            int bytes22 = (bytes21 | bytes20 << 8) & 0xffff;
            Log.e(TAG, "bytes22 is " + bytes2[0] + ", " + bytes2[1] + bytes22);

            byte[] bytes3 = DataTransfer.short2byte((short) -254);
            Log.e(TAG, "bytes3 is " + bytes3[0] + ", " + bytes3[1]);
            int bytes4 = DataTransfer.byte2short(bytes3);
            Log.e(TAG, "bytes4 is " + bytes4);

            byte[] data = new byte[]{0x01, 0x07, 0x14, 0x12, 0x03, 0x0c, 0x14, 0x08, 0x1e};
            byte check = CrcUtil.calCrc8(data);
            byte[] checks = new byte[1];
            checks[0]= check;
            PrintHexBinary.print(checks);
            byte[] date = DateParseUtil.format(System.currentTimeMillis());
            PrintHexBinary.print(date);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_title_bar_back);
        ivBack.setVisibility(View.GONE);

        TextView title = (TextView) findViewById(R.id.tv_title_bar_title);
        title.setText("发货方");

        ivSet = (ImageView) findViewById(R.id.iv_set);
        ivSet.setVisibility(View.VISIBLE);


        ivCamera = (ImageView) findViewById(R.id.iv_camera);
        ivCamera.setOnClickListener(this);

        ivMsgCamera = (ImageView) findViewById(R.id.iv_send_camera);
        ivMsgCamera.setOnClickListener(this);

        tvFinish = (TextView) findViewById(R.id.tv_finish);
        tvFinish.setOnClickListener(this);

        etID = (EditText) findViewById(R.id.et_id);
        etMsgID = (EditText) findViewById(R.id.et_send_id);

        rlTemp = (RelativeLayout) findViewById(R.id.rl_temp);
        rlTemp.setOnClickListener(this);
        tvTemp = (TextView) findViewById(R.id.tv_temp);

        etID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                etID.setText(s);
//                etID.setSelection(s.length());
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
//                etMsgID.setText(s);
//                etMsgID.setSelection(s.length());
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
            intent.putExtra("mac", etID.getText().toString());
            intent.putExtra("orderId", etMsgID.getText().toString());
            intent.putExtra("saft_temp", tvTemp.getText());
            startActivity(intent);
        } else if (v == ivSet) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        } else if (v == rlTemp) {
            Intent intent = new Intent(this, TempActivity.class);
            startActivityForResult(intent, 201);
        } else if (v == ivCamera) {
            Intent intent = new Intent(this, ZxingActivity.class);
            startActivityForResult(intent, 202);
        } else if (v == ivMsgCamera) {
            Intent intent = new Intent(this, ZxingActivity.class);
            startActivityForResult(intent, 203);
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
                                           @NonNull int[] grantResults) {
        if (requestCode == 10001) {
            if (grantResults == null || grantResults.length == 0) {
                return;
            }
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "已获取位置权限", Toast.LENGTH_LONG).show();
            } else {
                gotoOpenPermission();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {
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
        } else if (requestCode == 201) {
            if (data != null) {
                String temp = data.getStringExtra("max_min");
                tvTemp.setText(temp);
            }
        } else if (requestCode == 202) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    etID.setText(result);
                    etID.setSelection(result.length());
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == 203) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    etMsgID.setText(result);
                    etMsgID.setSelection(result.length());
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
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

    private void gotoOpenPermission() {
        Toast.makeText(this, "请在设置界面打开位置权限", Toast.LENGTH_LONG).show();
    }

    public static boolean isGpsProviderEnabled(Context context) {
        LocationManager service = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        return service.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private boolean checkIsBleState() {
        if (!BluetoothUtils.isBluetoothLeSupported(this)) {
            Toast.makeText(this, "此设备不支持蓝牙", Toast.LENGTH_LONG).show();
        } else if (!mBluetoothUtils.isBluetoothIsEnable()) {
            mBluetoothUtils.askUserToEnableBluetoothIfNeeded(this);
        } else {
            return true;
        }
        return false;
    }

    public static void startLocationSettings(Activity context, int requestCode) {
        Intent enableLocationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivityForResult(enableLocationIntent, requestCode);
    }

}
