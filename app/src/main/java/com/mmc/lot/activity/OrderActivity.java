package com.mmc.lot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mmc.lot.R;
import com.mmc.lot.bean.FormBean;
import com.mmc.lot.ble.connect.ConnectOne;
import com.mmc.lot.data.DataCenter;
import com.mmc.lot.eventbus.ble.SaveManifestEvent;
import com.mmc.lot.eventbus.ui.ShowToastEvent;
import com.mmc.lot.net.Request;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhangzd on 2018/4/4.
 */

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etOrder, etCompany;
    private EditText etSendName, etSendAddr, etSendPhone;
    private EditText etTakeName, etTakeAddr, etTakePhone;
    private EditText etGood, etGoodCompany;
    private TextView tvSafeTemp, tvFinish;
    private String min = "-30", max = "60";
    private ImageView iv_camera, iv_send_camera;
    private RelativeLayout rl_temp;

    private final static int REQUEST_ORDER_CODE = 0x10;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_trans_order_layout);
        ImageView ivBack = (ImageView) findViewById(R.id.iv_title_bar_back);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView title = (TextView) findViewById(R.id.tv_title_bar_title);
        title.setText("物流信息");
        initView();
    }

    private void initView() {
        etOrder = (EditText) findViewById(R.id.et_id);
        etCompany = (EditText) findViewById(R.id.et_send_id);
        etSendName = (EditText) findViewById(R.id.et_send_name);
        etSendAddr = (EditText) findViewById(R.id.et_send_addr_id);
        etSendPhone = (EditText) findViewById(R.id.et_send_phone);

        etTakeName = (EditText) findViewById(R.id.et_take_name);
        etTakeAddr = (EditText) findViewById(R.id.et_take_addr_id);
        etTakePhone = (EditText) findViewById(R.id.et_take_phone);

        etGood = (EditText) findViewById(R.id.et_good_name);
        etGoodCompany = (EditText) findViewById(R.id.et_good_company);

        rl_temp = (RelativeLayout) findViewById(R.id.rl_temp);
        rl_temp.setOnClickListener(this);
        tvSafeTemp = (TextView) findViewById(R.id.tv_temp);
        tvFinish = (TextView) findViewById(R.id.tv_finish);
        tvFinish.setOnClickListener(this);

        iv_camera = (ImageView) findViewById(R.id.iv_camera);
        iv_camera.setOnClickListener(this);
//        iv_send_camera = (ImageView) findViewById(R.id.iv_send_camera);
//        iv_send_camera.setOnClickListener(this);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        if (v == tvFinish) {
            if (checkParsms()) {
                Log.e("OrderActivity", "");
                setLogisticsInfo();
                ConnectOne.getInstance().setManifestStr(new Gson().toJson(DataCenter.getInstance().getLogisticsInfo()));
                ConnectOne.getInstance().resetSaveNum();
                EventBus.getDefault().post(new SaveManifestEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
//                this.finish();
            } else {
                EventBus.getDefault().post(new ShowToastEvent("请补全相关信息"));
            }
        } else if (v == iv_camera) {
            Intent intent = new Intent(this, ZxingActivity.class);
            startActivityForResult(intent, REQUEST_ORDER_CODE);
        } else if (v == rl_temp) {
            Intent intent = new Intent(this, TempActivity.class);
            startActivityForResult(intent, 201);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ORDER_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }

                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    etOrder.setText(result);
                    etOrder.setSelection(result.length());
//                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(OrderActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == 201) {
            if (data != null) {
                min = data.getStringExtra("min");
                max = data.getStringExtra("max");
                String temp = min + "-" + max + "°C";
                tvSafeTemp.setText(temp);
            }
        }
    }

    private boolean checkParsms() {
        if (TextUtils.isEmpty(etOrder.getText().toString()) ||
                TextUtils.isEmpty(etCompany.getText().toString()) ||
                TextUtils.isEmpty(etTakeName.getText().toString()) ||
                TextUtils.isEmpty(etTakeAddr.getText().toString()) ||
                TextUtils.isEmpty(etTakePhone.getText().toString()) ||
                TextUtils.isEmpty(etSendName.getText().toString()) ||
                TextUtils.isEmpty(etSendAddr.getText().toString()) ||
                TextUtils.isEmpty(etSendPhone.getText().toString()) ||
                TextUtils.isEmpty(etGood.getText().toString())){
            return false;
        }
        return true;

    }

//    private void geneTransBean() {
//        FormBean.TransportInformationBean transBean = new FormBean.TransportInformationBean();
//        transBean.setOrderId(etOrder.getText().toString());
//        transBean.setLogisticsCompany(etCompany.getText().toString());
//        //收货人
//        FormBean.TransportInformationBean.ConsigneeBean consignee = new FormBean.TransportInformationBean.ConsigneeBean(etTakeName.getText().toString(), etTakeAddr.getText().toString(), etTakePhone.getText().toString());
//        transBean.setConsignee(consignee);
//        //托运人
//        FormBean.TransportInformationBean.ConsignorBean consignor = new FormBean.TransportInformationBean.ConsignorBean(etSendName.getText().toString(), etSendAddr.getText().toString(), etSendPhone.getText().toString());
//        transBean.setConsignor(consignor);
//        //产品
//        FormBean.TransportInformationBean.ProductBean productBean = new FormBean.TransportInformationBean.ProductBean(etGoodCompany.getText().toString(), etGood.getText().toString());
//        transBean.setProduct(productBean);
//
//
//        FormBean.TransportInformationBean.ValidRangeBean validRangeBean = new FormBean.TransportInformationBean.ValidRangeBean(Integer.parseInt(min), Integer.parseInt(max));
//        transBean.setValidRange(validRangeBean);
//
//        Request.setTransBean(transBean);
//    }

    private void setLogisticsInfo() {
        DataCenter.SetLogisticsInfo.setLogisticsId(etOrder.getText().toString());
        DataCenter.SetLogisticsInfo.setLogisticsCompany(etOrder.getText().toString());

        DataCenter.SetLogisticsInfo.setShipperName(etSendName.getText().toString());
        DataCenter.SetLogisticsInfo.setShipperAddress(etSendAddr.getText().toString());
        DataCenter.SetLogisticsInfo.setShipperTel(etSendPhone.getText().toString());

        DataCenter.SetLogisticsInfo.setConsigneeName(etTakeName.getText().toString());
        DataCenter.SetLogisticsInfo.setConsigneeAddress(etTakeAddr.getText().toString());
        DataCenter.SetLogisticsInfo.setConsigneeTel(etTakePhone.getText().toString());

        DataCenter.SetLogisticsInfo.setProductCategory(etGoodCompany.getText().toString());
        DataCenter.SetLogisticsInfo.setProductName(etGood.getText().toString());

        DataCenter.SetLogisticsInfo.setMinTemperature(Integer.parseInt(min));
        DataCenter.SetLogisticsInfo.setMaxTemperature(Integer.parseInt(max));
    }
}
