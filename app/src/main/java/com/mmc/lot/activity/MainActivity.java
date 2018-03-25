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
import com.mmc.lot.R;

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
        } else if (requestCode == 201) {
            if (data != null) {
                String temp = data.getStringExtra("max_min");
                tvTemp.setText(temp);
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

}
