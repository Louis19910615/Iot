package com.mmc.lot.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blakequ.bluetooth_manager_lib.util.BluetoothUtils;
import com.mmc.lot.IotApplication;
import com.mmc.lot.R;
import com.mmc.lot.bean.BaseBean;
import com.mmc.lot.bean.BindBeanParent;
import com.mmc.lot.bean.FormBean;
import com.mmc.lot.bean.ShowToastBean;
import com.mmc.lot.bean.TransBean;
import com.mmc.lot.ble.analysis.Analysis;
import com.mmc.lot.ble.connect.ConnectOne;
import com.mmc.lot.ble.device.DeviceInfo;
import com.mmc.lot.ble.receiver.BleActionStateChangedReceiver;
import com.mmc.lot.ble.scan.Scanner;
import com.mmc.lot.eventbus.BleStateOnEvent;
import com.mmc.lot.eventbus.ScanWithAddressEvent;
import com.mmc.lot.eventbus.ScanWithNameEvent;
import com.mmc.lot.net.Repository;
import com.mmc.lot.util.IntentUtils;
import com.mmc.lot.util.PermissionConstant;
import com.mmc.lot.util.SharePreUtils;
import com.orhanobut.logger.Logger;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MainActivity";

    private EditText etID, etMsgID;
    private ImageView ivCamera, ivMsgCamera, ivBack, ivSet;
    private TextView tvFinish;
    private BluetoothUtils mBluetoothUtils;
    private RelativeLayout rlTemp;
    private TextView tvTemp, tvSaftTemp;
    private LinearLayout llTag;
    private String min, max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        EventBus.getDefault().register(this);
        mBluetoothUtils = BluetoothUtils.getInstance(this);

//        Log.e(TAG, "你好大北京");
//        try {
//            byte[] bytes = new String("你好大北京").getBytes("UTF8");
//            String str = new String(bytes, "UTF8");
//            Log.e(TAG, "str is " + str);
//            Byte temp = Byte.valueOf("20");
//            Log.e(TAG, "temp is " + String.valueOf(temp));
//            byte[] bytes1 = new byte[]{0x09, (byte) 0xFA};
//            Log.e(TAG, "equal is " + (bytes1[1] == (byte) 0xFA));
//            byte[] bytes2 = new byte[]{(byte) 0xFA, (byte) 0xFE};
//            int bytes20 = (bytes2[0] & 0xff);
//            int bytes21 = (bytes2[1] & 0xff);
//            int bytes22 = (bytes21 | bytes20 << 8) & 0xffff;
//            Log.e(TAG, "bytes22 is " + bytes2[0] + ", " + bytes2[1] + bytes22);
//
//            byte[] bytes3 = DataTransfer.short2byte((short) -254);
//            Log.e(TAG, "bytes3 is " + bytes3[0] + ", " + bytes3[1]);
//            int bytes4 = DataTransfer.byte2short(bytes3);
//            Log.e(TAG, "bytes4 is " + bytes4);
//
//            byte[] data = new byte[]{0x01, 0x07, 0x14, 0x12, 0x03, 0x0c, 0x14, 0x08, 0x1e};
//            byte check = CrcUtil.calCrc8(data);
//            byte[] checks = new byte[1];
//            checks[0] = check;
//            PrintHexBinary.print(checks);
//            byte[] date = DateParseUtil.format(System.currentTimeMillis());
//            PrintHexBinary.print(date);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_title_bar_back);
        ivBack.setVisibility(View.GONE);

        TextView title = (TextView) findViewById(R.id.tv_title_bar_title);

        String token = SharePreUtils.getInstance().getString(SharePreUtils.USER_TOKEN, "");

        if (!TextUtils.isEmpty(token)) {
            //send
            if (token.contains(IntentUtils.providerRole)) {
                title.setText(IntentUtils.TYPE_USER_SEND);
            }
            //take
            else if (token.contains(IntentUtils.clientRole)) {
                title.setText(IntentUtils.TYPE_USER_TAKE);
            }
            //快递员
            else if (token.contains(IntentUtils.courierRole)) {
                title.setText(IntentUtils.TYPE_USER_ADMIN);
            }
        }

        ivSet = (ImageView) findViewById(R.id.iv_set);
        ivSet.setVisibility(View.VISIBLE);
        ivSet.setOnClickListener(this);


        ivCamera = (ImageView) findViewById(R.id.iv_camera);
        ivCamera.setOnClickListener(this);

        ivMsgCamera = (ImageView) findViewById(R.id.iv_send_camera);
        ivMsgCamera.setOnClickListener(this);

        tvFinish = (TextView) findViewById(R.id.tv_finish);
        tvFinish.setOnClickListener(this);
        llTag = (LinearLayout) findViewById(R.id.ll_tag);

        etID = (EditText) findViewById(R.id.et_id);
        etMsgID = (EditText) findViewById(R.id.et_send_id);

        rlTemp = (RelativeLayout) findViewById(R.id.rl_temp);
        rlTemp.setOnClickListener(this);
        tvTemp = (TextView) findViewById(R.id.tv_temp);
        tvSaftTemp = (TextView) findViewById(R.id.tv_temp_txt);

        if (!TextUtils.isEmpty(token)) {

            //send
            if (token.contains(IntentUtils.providerRole)) {
                rlTemp.setVisibility(View.VISIBLE);
                tvTemp.setVisibility(View.VISIBLE);
                tvSaftTemp.setVisibility(View.VISIBLE);
                llTag.setVisibility(View.VISIBLE);

            }
            //take
            else if (token.contains(IntentUtils.clientRole)) {
                rlTemp.setVisibility(View.GONE);
                tvTemp.setVisibility(View.GONE);
                tvSaftTemp.setVisibility(View.GONE);
                llTag.setVisibility(View.VISIBLE);

            }
            //快递员
            else if (token.contains(IntentUtils.courierRole)) {
                rlTemp.setVisibility(View.GONE);
                tvTemp.setVisibility(View.GONE);
                tvSaftTemp.setVisibility(View.GONE);

                llTag.setVisibility(View.VISIBLE);
            }
        }

//        etID.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
////                etID.setText(s);
////                etID.setSelection(s.length());
//            }
//        });
//
//        etMsgID.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
////                etMsgID.setText(s);
////                etMsgID.setSelection(s.length());
//            }
//        });
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

            String token = SharePreUtils.getInstance().getString(SharePreUtils.USER_TOKEN, "");
            //send
            if (token.contains(IntentUtils.providerRole)) {
                if (TextUtils.isEmpty(etID.getText().toString()) || TextUtils.isEmpty(etMsgID.getText().toString())) {
                    Toast.makeText(this, "id 不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendTagData(etID.getText().toString(), etMsgID.getText().toString(), tvTemp.getText().toString(), DeviceInfo.getInstance().getTempDatas());
            }
            //take
            else if (token.contains(IntentUtils.clientRole)) {
                if (TextUtils.isEmpty(etID.getText().toString()) || TextUtils.isEmpty(etMsgID.getText().toString())) {
                    Toast.makeText(this, "id 不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                requestTransData(etID.getText().toString(), etMsgID.getText().toString());
            }
            //快递员
            else if (token.contains(IntentUtils.courierRole)) {
                if (TextUtils.isEmpty(etMsgID.getText().toString())) {
                    Toast.makeText(this, "id 不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                requestTransData(etID.getText().toString(), etMsgID.getText().toString());
            }

        } else if (v == ivSet) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        } else if (v == rlTemp) {
            Intent intent = new Intent(this, TempActivity.class);
            startActivityForResult(intent, 201);
        } else if (v == ivCamera) {
            String addressName = etID.getText().toString().toUpperCase();

            if (addressName.split(":").length == 6) {
                startCheckPermission();
            } else {
                Toast.makeText(this, "请输入有效Tag名称", Toast.LENGTH_LONG).show();
            }
//            Intent intent = new Intent(this, ZxingActivity.class);
//            startActivityForResult(intent, 202);
        } else if (v == ivMsgCamera) {
            Intent intent = new Intent(this, ZxingActivity.class);
            startActivityForResult(intent, 203);
        }
    }

    /**
     * 1. 先进行数据上传接口
     * 2. 表单数据上传接口
     * 3. 回复绑定接口
     */
    private void sendTagData(String mac, String orderId, String temp, List<Double> tempData) {
        Repository.init().sendTagData(mac, orderId, temp, tempData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        if (baseBean != null) {
                            if (baseBean.getC() == 1) {
//                                Toast.makeText(MainActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                sendFormData();
                            } else {
                                Toast.makeText(MainActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("zzDebug", "error:" + e.getMessage());
                        Toast.makeText(MainActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //表单数据
    private void sendFormData() {
        //表单数据bean(tagid, orderId, safttemp)
        FormBean bean = getFormBean();

        Repository.init().sendFormData(bean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        if (baseBean != null) {
                            if (baseBean.getC() == 1) {
//                                Toast.makeText(MainActivity.this, "表单提交成功", Toast.LENGTH_SHORT).show();
                                sendBindData(etID.getText().toString());
                            } else {
                                Toast.makeText(MainActivity.this, "表单提交失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("zzDebug", "error:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @NonNull
    private FormBean getFormBean() {
        FormBean bean = new FormBean();
        bean.setToken(SharePreUtils.getInstance().getString(SharePreUtils.USER_TOKEN, ""));

        FormBean.TransportInformationBean transBean = new FormBean.TransportInformationBean();
        transBean.setLogisticsCompany("顺丰速运");
        transBean.setOrderId(etMsgID.getText().toString());

        FormBean.TransportInformationBean.ConsigneeBean consignee = new FormBean.TransportInformationBean.ConsigneeBean("中国医药集团", "北京市海淀区知春路20号", "8613800138000");
        transBean.setConsignee(consignee);
        FormBean.TransportInformationBean.ConsignorBean consignor = new FormBean.TransportInformationBean.ConsignorBean("深圳市人民医院", "广东省深圳市罗湖区东门北路1017号", "8613811138111");
        transBean.setConsignor(consignor);
        FormBean.TransportInformationBean.ProductBean productBean = new FormBean.TransportInformationBean.ProductBean("中国医药集团", "流感疫苗");
        transBean.setProduct(productBean);


        FormBean.TransportInformationBean.ValidRangeBean validRangeBean = new FormBean.TransportInformationBean.ValidRangeBean(Integer.parseInt(min), Integer.parseInt(max));
        transBean.setValidRange(validRangeBean);

        FormBean.TagInformationBean tagInformationBean = new FormBean.TagInformationBean();
        tagInformationBean.setMac("00:11:22:33:44:55");
        tagInformationBean.setTagID(etID.getText().toString());
        tagInformationBean.setEnergy(100);
        tagInformationBean.setIntervalTime(1);
        tagInformationBean.setGps("123,789");
        tagInformationBean.setCategory("bluetooth");
        tagInformationBean.setDescription("record temperature");

        bean.setTransportInformation(transBean);
        bean.setTagInformation(tagInformationBean);
        return bean;
    }


    private void sendBindData(String tagid) {
        Repository.init().bindData(tagid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BindBeanParent>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BindBeanParent baseBean) {
                        if (baseBean != null) {
                            if (baseBean.getC() == 1) {
                                Log.d("zzDebug", "success");
                                Toast.makeText(MainActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
                                IntentUtils.startSendDetailActivity(MainActivity.this,
                                        etID.getText().toString(),
                                        etMsgID.getText().toString(),
                                        tvTemp.getText().toString());
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "绑定失败", Toast.LENGTH_SHORT).show();
                                Log.d("zzDebug", "failed");
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("zzDebug", "error:" + e.getMessage());
                        Toast.makeText(MainActivity.this, "绑定失败", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 获取物流信息接口
     */
    private void requestTransData(String mac, String orderId) {
        Repository.init().getTransData(mac, orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TransBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TransBean baseBean) {
                        if (baseBean != null) {
                            if (baseBean.getC() == 1) {
//                                Toast.makeText(IotApplication.getContext(), "物流信息请求成功", Toast.LENGTH_SHORT).show();
                                IntentUtils.startConfirmActivity(MainActivity.this,
                                        etID.getText().toString(),
                                        etMsgID.getText().toString(),
                                        tvTemp.getText().toString(), baseBean);
//                                finish();
                            } else {
                                Toast.makeText(IotApplication.getContext(), baseBean.getM(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(IotApplication.getContext(), "物流信息请求失败", Toast.LENGTH_SHORT).show();
                        ;

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showToast(ShowToastBean showToastBean) {
        Toast.makeText(this, showToastBean.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private boolean startCheckPermission() {
        if (checkLocationPermission()) {
            if (isGpsProviderEnabled(this)) {
                if (checkIsBleState()) {
                    EventBus.getDefault().post(new BleStateOnEvent());
                    return true;
                } else {
                    enableBluetooth();
                    registerBleActionReceiver(this);
                    return false;
                }
            } else {
                startLocationSettings(this, PermissionConstant.LOCATION_SETTING_REQUESTCODE);
                return false;
            }
        } else {
            requestLocationPermissions();
            return false;
        }
    }

    private boolean isBleStateOn = false;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bleStateOn(BleStateOnEvent bleStateOnEvent) {
        if (!isBleStateOn) {
            Logger.e(TAG, "isBleStateOn is " + isBleStateOn);
            // init scanner
            Scanner.getInstance();
            // init connect one
            ConnectOne.getInstance();
            // init analysis
            Analysis.getInstance();
            // post to scan
            EventBus.getDefault().post(new ScanWithAddressEvent(etID.getText().toString().toUpperCase()));

            isBleStateOn = true;
            unregisterBleActionReceiver(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PermissionConstant.LOCATION_PERMISSIN_REQUESTCODE) {
            if (grantResults == null || grantResults.length == 0) {
                return;
            }
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "已获取位置权限", Toast.LENGTH_LONG).show();
                startCheckPermission();
            } else {
                Toast.makeText(this, "请在设置界面打开位置权限", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        Log.e("MainActivity", "requestCode:" + requestCode + ", resultCode:" + resultCode);
        if (requestCode == PermissionConstant.LOCATION_SETTING_REQUESTCODE) {
            if (isGpsProviderEnabled(this)) {
                Toast.makeText(this, "已打开定位", Toast.LENGTH_LONG).show();
                startCheckPermission();
            } else {
                Toast.makeText(this, "已拒绝打开定位", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PermissionConstant.ENABLE_BLUETOOTH_REQUESTCODE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "已打开蓝牙", Toast.LENGTH_LONG).show();
                startCheckPermission();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "已拒绝打开蓝牙", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == 201) {
            if (data != null) {
                min = data.getStringExtra("min");
                max = data.getStringExtra("max");
                String temp = min + "-" + max + "°C";
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
        if (Build.VERSION.SDK_INT >= 23) {
            return checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) || checkPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        return true;
    }

    private boolean checkPermission(final String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PermissionConstant.LOCATION_PERMISSIN_REQUESTCODE);

    }

    public static boolean isGpsProviderEnabled(Context context) {
        LocationManager service = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        return service.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void startLocationSettings(Activity context, int requestCode) {
        Intent enableLocationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivityForResult(enableLocationIntent, requestCode);
    }

    private boolean checkIsBleState() {
        if (!BluetoothUtils.isBluetoothLeSupported(this)) {
            Toast.makeText(this, "此设备不支持蓝牙", Toast.LENGTH_LONG).show();
        } else if (!mBluetoothUtils.isBluetoothIsEnable()) {
            return false;
        } else {
            return true;
        }
        return false;
    }

    private void enableBluetooth() {
        mBluetoothUtils.askUserToEnableBluetoothIfNeeded(this);

    }

    private BleActionStateChangedReceiver bleActionStateChangedReceiver;

    private void registerBleActionReceiver(Context context) {

        if (bleActionStateChangedReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

            bleActionStateChangedReceiver = new BleActionStateChangedReceiver();

            context.registerReceiver(bleActionStateChangedReceiver, filter);
        }
    }

    private void unregisterBleActionReceiver(Context context) {
        if (bleActionStateChangedReceiver != null) {
            context.unregisterReceiver(bleActionStateChangedReceiver);
            bleActionStateChangedReceiver = null;
        }
    }

}
