package com.mmc.lot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mmc.lot.IotApplication;
import com.mmc.lot.R;
import com.mmc.lot.bean.TempBean;
import com.mmc.lot.bean.TransBean;
import com.mmc.lot.net.Repository;
import com.mmc.lot.util.IntentUtils;

import java.text.SimpleDateFormat;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhangzd on 2018/3/18.
 */

public class ConfirmActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvId, tvMsgId, tvTemp, tvTime, tvFinish, tvStartTime;
    private LinearLayout llTemp;
    private static TempBean tempBean;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_confirm_layout);

        ImageView ivBack = (ImageView) findViewById(R.id.iv_title_bar_back);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView title = (TextView) findViewById(R.id.tv_title_bar_title);
        title.setText("确认货单信息");

        String tagid = getIntent().getStringExtra("mac");
        String orderId = getIntent().getStringExtra("orderId");
        String saft_temp = getIntent().getStringExtra("saft_temp");
        TransBean transBean = (TransBean) getIntent().getSerializableExtra("transBean");

        llTemp = (LinearLayout) findViewById(R.id.ll_temp);
        llTemp.setOnClickListener(this);

        tvId = (TextView) findViewById(R.id.tv_id);

        tvMsgId = (TextView) findViewById(R.id.tv_msg_id);
        tvMsgId.setText(orderId);

        tvTemp = (TextView) findViewById(R.id.tv_temp);

        tvStartTime = (TextView) findViewById(R.id.tv_start_time);


        tvFinish = (TextView) findViewById(R.id.tv_finish);
        tvFinish.setOnClickListener(this);

        tempBean = new TempBean();
        if (transBean != null) {
            TransBean.OBean bean = transBean.getO();
            if (bean != null) {
                tvId.setText(bean.getTAGID());
                tvStartTime.setText(timeStamp2Date(bean.getUPTIME()));
                String tempature = bean.getMINRANGE() + "-" + bean.getMAXRANGE() + "°C";
                tvTemp.setText(tempature);
            }
        }

        requestTempData();
    }

    private String timeStamp2Date(long currentMission) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(currentMission);
    }


    private void requestTempData() {
        Repository.init().getTemp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TempBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TempBean tempBean) {
                        if (tempBean != null) {
                            if (tempBean.getC() == 1) {
//                                Toast.makeText(IotApplication.getContext(), "温度请求成功", Toast.LENGTH_SHORT).show();
                                ConfirmActivity.tempBean = tempBean;
                            } else {
                                Toast.makeText(IotApplication.getContext(), tempBean.getM(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(IotApplication.getContext(), "温度请求失败", Toast.LENGTH_SHORT).show();
                        ;

                    }

                    @Override
                    public void onComplete() {

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
            IntentUtils.startMainActivity(this);
            finish();
        } else if (v == llTemp) {
            if (tempBean != null && tempBean.getO() != null
                    && tempBean.getO().size() > 0 &&
                    !TextUtils.isEmpty(tempBean.getO().get(0).getTEMP())) {
                if ("[]".equals(tempBean.getO().get(0).getTEMP())) {
                    Toast.makeText(ConfirmActivity.this, "获取温度数据失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(ConfirmActivity.this, CharActivity.class);
                intent.putExtra("tempBean", tempBean);
                startActivity(intent);
            } else {
                Toast.makeText(ConfirmActivity.this, "获取温度数据失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
