package com.mmc.lot.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmc.lot.activity.MainActivity;
import com.mmc.lot.R;

/**
 * Created by zhangzd on 2018/3/20.
 */

public class SelectRoleActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvTake, tvSend, tvAdmin, tvConfirm;
    private LinearLayout llTake, llSend, llAdmin;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_select_role_layout);
//        ImageView ivBack = (ImageView) findViewById(R.id.iv_title_bar_back);
//        ivBack.setVisibility(View.VISIBLE);
//        ivBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        TextView title = (TextView) findViewById(R.id.tv_title_bar_title);
//        title.setText("设置");
//
//        ImageView ivSet = (ImageView) findViewById(R.id.iv_set);
//        ivSet.setVisibility(View.GONE);

        llTake = (LinearLayout) findViewById(R.id.ll_take);
        tvTake = (TextView) findViewById(R.id.tv_take);
        llTake.setOnClickListener(this);

        llSend = (LinearLayout) findViewById(R.id.ll_send);
        tvSend = (TextView) findViewById(R.id.tv_send);
        llSend.setOnClickListener(this);


        llAdmin = (LinearLayout) findViewById(R.id.ll_admin);
        tvAdmin = (TextView) findViewById(R.id.tv_admin);
        llAdmin.setOnClickListener(this);

        tvConfirm = (TextView) findViewById(R.id.tv_finish);
        tvConfirm.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        if (v == llTake) {
            Drawable drawableTake = getResources().getDrawable(R.drawable.icon_selected);
            drawableTake.setBounds(0, 0, drawableTake.getMinimumWidth(), drawableTake.getMinimumHeight());
            tvTake.setCompoundDrawables(null, null, drawableTake, null);

            Drawable drawableSend = getResources().getDrawable(R.drawable.icon_unselected);
            drawableSend.setBounds(0, 0, drawableSend.getMinimumWidth(), drawableSend.getMinimumHeight());
            tvSend.setCompoundDrawables(null, null, drawableSend, null);

            Drawable drawable = getResources().getDrawable(R.drawable.icon_unselected);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvAdmin.setCompoundDrawables(null, null, drawable, null);


        } else if (v == llSend) {
            Drawable drawableTake = getResources().getDrawable(R.drawable.icon_unselected);
            drawableTake.setBounds(0, 0, drawableTake.getMinimumWidth(), drawableTake.getMinimumHeight());
            tvTake.setCompoundDrawables(null, null, drawableTake, null);

            Drawable drawableSend = getResources().getDrawable(R.drawable.icon_selected);
            drawableSend.setBounds(0, 0, drawableSend.getMinimumWidth(), drawableSend.getMinimumHeight());
            tvSend.setCompoundDrawables(null, null, drawableSend, null);

            Drawable drawableAdmin = getResources().getDrawable(R.drawable.icon_unselected);
            drawableAdmin.setBounds(0, 0, drawableAdmin.getMinimumWidth(), drawableAdmin.getMinimumHeight());
            tvAdmin.setCompoundDrawables(null, null, drawableAdmin, null);

        } else if (v == llAdmin) {
            Drawable drawable = getResources().getDrawable(R.drawable.icon_selected);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvAdmin.setCompoundDrawables(null, null, drawable, null);

            Drawable drawableTake = getResources().getDrawable(R.drawable.icon_unselected);
            drawableTake.setBounds(0, 0, drawableTake.getMinimumWidth(), drawableTake.getMinimumHeight());
            tvTake.setCompoundDrawables(null, null, drawableTake, null);

            Drawable drawableSend = getResources().getDrawable(R.drawable.icon_unselected);
            drawableSend.setBounds(0, 0, drawableSend.getMinimumWidth(), drawableSend.getMinimumHeight());
            tvSend.setCompoundDrawables(null, null, drawableSend, null);
        } else if (v == tvConfirm) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
