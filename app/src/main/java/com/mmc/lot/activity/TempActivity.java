package com.mmc.lot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mmc.lot.R;

/**
 * Created by zhangzd on 2018/3/24.
 */

public class TempActivity extends AppCompatActivity {

    private EditText etTempMax, etTempMin;
    private TextView tvConfirm;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_temp_layout);

        ImageView ivBack = (ImageView) findViewById(R.id.iv_title_bar_back);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView title = (TextView) findViewById(R.id.tv_title_bar_title);
        title.setText("安全温度");

        ImageView ivSet = (ImageView) findViewById(R.id.iv_set);
        ivSet.setVisibility(View.GONE);

        etTempMax = (EditText) findViewById(R.id.et_temp_max);
        etTempMin = (EditText) findViewById(R.id.et_temp_min);
        tvConfirm = (TextView) findViewById(R.id.tv_finish);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String max = etTempMax.getText().toString();
                String min = etTempMin.getText().toString();
                if ((Integer.parseInt(min)) > Integer.parseInt(max)) {
                    Toast.makeText(TempActivity.this, "下限温度不能大于上限温度", Toast.LENGTH_SHORT).show();
                    return;
                }
                String temp = max + "-" + min + "°C";
                Intent intent = new Intent();
                intent.putExtra("min",min);
                intent.putExtra("max",max);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
