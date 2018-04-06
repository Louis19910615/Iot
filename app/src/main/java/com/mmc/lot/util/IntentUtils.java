package com.mmc.lot.util;

import android.content.Context;
import android.content.Intent;

import com.mmc.lot.activity.ChartActivity2;
import com.mmc.lot.activity.ConfirmActivity;
import com.mmc.lot.activity.LoginActivity;
import com.mmc.lot.activity.MainActivity;
import com.mmc.lot.activity.OrderActivity;
import com.mmc.lot.activity.SendDetailActivity;
import com.mmc.lot.bean.TransBean;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zhangzd on 2018/3/31.
 */

public class IntentUtils {

    public static final String TYPE_USER_SEND = "发货方";
    public static final String TYPE_USER_TAKE = "收货方";
    public static final String TYPE_USER_ADMIN = "快递员";

    //超级管理员
    public final static String superRole = "aahh33jj";

    //供应商  发货方
    public final static String providerRole = "bbdd44kk";

    //客户  收货方
    public final static String clientRole = "ddtt22kk";

    //管理员
    public final static String adminRole = "llaa55kk";

    //快递员
    public final static String courierRole = "wwoo44pp";


    public static void startSendDetailActivity(Context context, String mac, String orderId, String temp) {
        Intent intent = new Intent(context, SendDetailActivity.class);
        intent.putExtra("mac", mac);
        intent.putExtra("orderId", orderId);
        intent.putExtra("saft_temp", temp);
        context.startActivity(intent);
    }

    public static void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void startConfirmActivity(Context context, String mac, String orderId, String temp, TransBean transBean) {
        Intent intent = new Intent(context, ConfirmActivity.class);
        intent.putExtra("mac", mac);
        intent.putExtra("orderId", orderId);
        intent.putExtra("saft_temp", temp);
        intent.putExtra("transBean", transBean);
        context.startActivity(intent);
    }

    public static void statLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void startOrderActivity(Context context) {
        Intent intent = new Intent(context, OrderActivity.class);
        context.startActivity(intent);
    }
}
