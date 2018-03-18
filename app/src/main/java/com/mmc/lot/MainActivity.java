package com.mmc.lot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etID, etMsgID;
    private ImageView ivCamera,ivMsgCamera;
    private TextView tvFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        ImageView ivBack = (ImageView) findViewById(R.id.iv_title_bar_back);
        ivBack.setVisibility(View.GONE);

        TextView title = (TextView) findViewById(R.id.tv_title_bar_title);
        title.setText("发货方");

        ImageView ivSet = (ImageView) findViewById(R.id.iv_set);
        ivSet.setVisibility(View.VISIBLE);


        ivCamera = (ImageView) findViewById(R.id.iv_camera);
        ivCamera.setOnClickListener(this);

        ivMsgCamera = (ImageView) findViewById(R.id.iv_send_camera);
        ivMsgCamera.setOnClickListener(this);

        tvFinish = (TextView) findViewById(R.id.tv_finish);
        tvFinish.setOnClickListener(this);

        etID = (EditText) findViewById(R.id.et_id);
        etMsgID = (EditText) findViewById(R.id.et_send_id);

        etID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etID.setText(s);
                etID.setSelection(s.length());
            }
        });

        etMsgID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etMsgID.setText(s);
                etMsgID.setSelection(s.length());
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
            Intent intent = new Intent(this, SendDetailActivity.class);
            startActivity(intent);
        }
    }
}
