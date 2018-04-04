package com.mmc.lot.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mmc.lot.R;
import com.mmc.lot.bean.FormBean;
import com.mmc.lot.net.Request;

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
        etSendName = (EditText) findViewById(R.id.et_send_phone);

        etTakeName = (EditText) findViewById(R.id.et_take_name);
        etTakeAddr = (EditText) findViewById(R.id.et_take_addr_id);
        etTakePhone = (EditText) findViewById(R.id.et_take_phone);

        etGood = (EditText) findViewById(R.id.et_good_name);
        etGoodCompany = (EditText) findViewById(R.id.et_good_company);

        tvSafeTemp = (TextView) findViewById(R.id.tv_temp);
        tvFinish = (TextView) findViewById(R.id.tv_finish);
        tvFinish.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        if (v == tvFinish) {
            if (checkParsms()) {
                geneTransBean();
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

    private void geneTransBean() {
        FormBean.TransportInformationBean transBean = new FormBean.TransportInformationBean();
        transBean.setOrderId(etOrder.getText().toString());
        transBean.setLogisticsCompany(etCompany.getText().toString());
        //收货人
        FormBean.TransportInformationBean.ConsigneeBean consignee = new FormBean.TransportInformationBean.ConsigneeBean(etTakeName.getText().toString(), etTakeAddr.getText().toString(), etTakePhone.getText().toString());
        transBean.setConsignee(consignee);
        //托运人
        FormBean.TransportInformationBean.ConsignorBean consignor = new FormBean.TransportInformationBean.ConsignorBean(etSendName.getText().toString(), etSendAddr.getText().toString(), etSendPhone.getText().toString());
        transBean.setConsignor(consignor);
        //产品
        FormBean.TransportInformationBean.ProductBean productBean = new FormBean.TransportInformationBean.ProductBean(etGoodCompany.getText().toString(), etGood.getText().toString());
        transBean.setProduct(productBean);


        FormBean.TransportInformationBean.ValidRangeBean validRangeBean = new FormBean.TransportInformationBean.ValidRangeBean(Integer.parseInt(min), Integer.parseInt(max));
        transBean.setValidRange(validRangeBean);

        Request.setTransBean(transBean);
    }
}
