package com.mmc.lot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.mmc.lot.R;
import com.mmc.lot.util.SharePreUtils;

/**
 * @author zhangzd on 2018/3/25.
 */

public class LanuchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lanuch_layout);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(SharePreUtils.getInstance().getString(SharePreUtils.USER_TOKEN, ""))) {
                    Intent intent = new Intent(LanuchActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(LanuchActivity.this, SettingActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
