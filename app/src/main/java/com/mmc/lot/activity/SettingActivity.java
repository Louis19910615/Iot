package com.mmc.lot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mmc.lot.R;
import com.mmc.lot.util.IntentUtils;
import com.mmc.lot.util.SharePreUtils;

/**
 * Created by zhangzd on 2018/3/20.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rl_selete_role;
    private TextView tvLoginOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting_layout);
        ImageView ivBack = (ImageView) findViewById(R.id.iv_title_bar_back);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView title = (TextView) findViewById(R.id.tv_title_bar_title);
        title.setText("设置");

        ImageView ivSet = (ImageView) findViewById(R.id.iv_set);
        ivSet.setVisibility(View.GONE);

        rl_selete_role = (RelativeLayout) findViewById(R.id.rl_selete_role);
        rl_selete_role.setOnClickListener(this);

        tvLoginOut = (TextView) findViewById(R.id.tv_finish);
        tvLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePreUtils.getInstance().setString(SharePreUtils.USER_TOKEN, "");
                IntentUtils.statLoginActivity(SettingActivity.this);
                finish();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        if (v == rl_selete_role) {
            Intent intent = new Intent(this, SelectRoleActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
