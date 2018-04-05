package com.mmc.lot;

import android.app.Application;
import android.content.Context;

import com.mmc.lot.data.DataCenter;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by louis on 2018/3/18.
 */

public class IotApplication extends Application {

    private static IotApplication instance;

    private static Context context;
    private static DataCenter dataCenter;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        ZXingLibrary.initDisplayOpinion(this);
        init();
    }

    public static Context getContext() {
        return context;
    }

    private void init() {
        instance = this;
        dataCenter = DataCenter.getInstance();
    }

    public static IotApplication getInstance() {
        return instance;
    }
}
