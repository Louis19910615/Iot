package com.mmc.lot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mmc.lot.R;

/**
 * Created by zhangzd on 2018/3/18.
 */

public class SendDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvId, tvMsgId, tvTemp, tvFinish;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_send_detail_layout);

        ImageView ivBack = (ImageView) findViewById(R.id.iv_title_bar_back);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView title = (TextView) findViewById(R.id.tv_title_bar_title);
        title.setText("发货单详情");

        tvId = (TextView) findViewById(R.id.tv_id);
        tvMsgId = (TextView) findViewById(R.id.tv_msg_id);
        tvTemp = (TextView) findViewById(R.id.tv_temp);
        tvFinish = (TextView) findViewById(R.id.tv_finish);
        tvFinish.setOnClickListener(this);


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
            Intent intent = new Intent(this, ConfirmActivity.class);
            startActivity(intent);
        }
    }
}
