package com.mmc.lot.activity;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.mmc.lot.R;
import com.mmc.lot.data.DataCenter;
import com.mmc.lot.util.IntentUtils;
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
                String token = SharePreUtils.getInstance().getString(SharePreUtils.USER_TOKEN, "");
                String name = SharePreUtils.getInstance().getString(SharePreUtils.USER_NAME, "");
                Log.e("LanuchActivity", "token is " + token);
                if (TextUtils.isEmpty(token)) {
                    Intent intent = new Intent(LanuchActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    DataCenter.SetUserInfo.setUserName(name);
                    DataCenter.SetUserInfo.setToken(token);
                    IntentUtils.startMainActivity(LanuchActivity.this);
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
