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
import android.widget.TextView;
import android.widget.Toast;

import com.mmc.lot.IotApplication;
import com.mmc.lot.R;
import com.mmc.lot.bean.BaseBean;
import com.mmc.lot.net.Repository;
import com.mmc.lot.util.SharePreUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author by zhangzd on 2018/3/25.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etNickName, etNum;
    private TextView tvLogin, tvRegitster;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login_layout);
        ImageView ivBack = (ImageView) findViewById(R.id.iv_title_bar_back);
        ivBack.setVisibility(View.GONE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView title = (TextView) findViewById(R.id.tv_title_bar_title);
        title.setText("登录");

        ImageView ivSet = (ImageView) findViewById(R.id.iv_set);
        ivSet.setVisibility(View.GONE);

        etNickName = (EditText) findViewById(R.id.et_nickname);
        String nickName = SharePreUtils.getInstance().getString(SharePreUtils.USER_NAME, "");
        if (!TextUtils.isEmpty(nickName)) {
            etNickName.setText(nickName);
        }
        etNum = (EditText) findViewById(R.id.et_num);
        tvLogin = (TextView) findViewById(R.id.tv_finish);
        tvLogin.setOnClickListener(this);

        tvRegitster = (TextView) findViewById(R.id.tv_register);
        tvRegitster.setOnClickListener(this);

    }

    private void login() {
        Repository.init().login(etNickName.getText().toString(),
                etNum.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final BaseBean baseBean) {
                        Log.d("zzdebug", "" + baseBean.toString());
                        if (baseBean != null) {
                            if (baseBean.getC() == 1) {
                                Log.d("zzDebug", "token:" + baseBean.getO());

                                SharePreUtils.getInstance().setString(SharePreUtils.USER_TOKEN, baseBean.getO());
                                SharePreUtils.getInstance().setString(SharePreUtils.USER_NAME, etNickName.getText().toString());
                                Toast.makeText(IotApplication.getContext(), "登录成功", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this, SettingActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, baseBean.getM(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("zzdebug", "error:" + e.getMessage());
                        Toast.makeText(IotApplication.getContext(), "登录失败", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        if (v == tvLogin) {
            if (TextUtils.isEmpty(etNickName.getText().toString().trim()) || TextUtils.isEmpty(etNum.getText().toString().trim())) {
                Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            login();

        } else if (v == tvRegitster) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }
}
