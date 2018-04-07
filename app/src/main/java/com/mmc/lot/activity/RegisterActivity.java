package com.mmc.lot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mmc.lot.IotApplication;
import com.mmc.lot.R;
import com.mmc.lot.bean.BaseBean;
import com.mmc.lot.net.Repository;
import com.mmc.lot.util.AccountValidatorUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhangzd on 2018/3/24.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etPhone, etNickName, etNum;
    private TextView tvRegister;
    private RadioGroup rgPerson;
    //供货商、客户、快递员
    private RadioButton rbSendPerson, rbPerson, rbCourier;
    private String userType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_register_first_layout);

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

        rgPerson = (RadioGroup) findViewById(R.id.rg_person);
        rbSendPerson = (RadioButton) findViewById(R.id.rb_send_person);
        rbPerson = (RadioButton) findViewById(R.id.rb_person);
        rbCourier = (RadioButton) findViewById(R.id.rb_courier);
        userType = "1";
        rgPerson.check(rbSendPerson.getId());
        rgPerson.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.rb_send_person:
                        userType = "1";
                        break;
                    case R.id.rb_person:
                        userType = "2";
                        break;
                    case R.id.rb_courier:
                        userType = "3";

                        break;
                    default:
                        break;
                }
            }
        });

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
                etNum.getText().toString(),
                userType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final BaseBean baseBean) {
                        Log.d("zzdebug", "baseBean：" + baseBean.toString());
                        if (baseBean != null) {
                            if (baseBean.getC() == 1) {
                                Toast.makeText(IotApplication.getContext(), "注册成功", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterActivity.this, baseBean.getM(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("zzdebug", "error:" + e.getMessage());
                        Toast.makeText(IotApplication.getContext(), "注册失败", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == tvRegister) {
            if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
                Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!AccountValidatorUtil.isMobile(etPhone.getText().toString())) {
                Toast.makeText(this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(etNickName.getText().toString().trim()) || TextUtils.isEmpty(etNum.getText().toString().trim())) {
                Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            register();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
