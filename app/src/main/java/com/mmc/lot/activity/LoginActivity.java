package com.mmc.lot.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mmc.lot.R;
import com.mmc.lot.bean.RegisterBean;
import com.mmc.lot.net.Repository;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhangzd on 2018/3/24.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etPhone, etNickName, etNum;
    private TextView tvRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login_layout);

        ImageView ivBack = (ImageView) findViewById(R.id.iv_title_bar_back);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView title = (TextView) findViewById(R.id.tv_title_bar_title);
        title.setText("注册");

        ImageView ivSet = (ImageView) findViewById(R.id.iv_set);
        ivSet.setVisibility(View.GONE);

        etPhone = (EditText) findViewById(R.id.et_phone);
        etNickName = (EditText) findViewById(R.id.et_nickname);
        etNum = (EditText) findViewById(R.id.et_num);
        tvRegister = (TextView) findViewById(R.id.tv_finish);
        tvRegister.setOnClickListener(this);
        initEt();
    }

    private void initEt() {
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

//                etPhone.setText(s);
//                etPhone.setSelection(s.length());
            }
        });
        etNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                etNickName.setText(s);
//                etNickName.setSelection(s.length());
            }
        });

        etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                etNum.setText(s);
//                etNum.setSelection(s.length());
            }

        });
    }

    private void register() {
        Repository.init().register(etNickName.getText().toString(),
                etPhone.getText().toString(),
                etNickName.getText().toString(),
                "1")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<RegisterBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RegisterBean registerBean) {
                        Log.d("zzdebug", registerBean.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("zzdebug", "error:" + e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == tvRegister) {
            register();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
