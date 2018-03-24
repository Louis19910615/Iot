package com.mmc.lot;

import android.app.Application;
import android.content.Context;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by louis on 2018/3/18.
 */

public class IotApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        EventBus.getDefault().register(this);
    }

    public static Context getContext() {
        return context;
    }
}
