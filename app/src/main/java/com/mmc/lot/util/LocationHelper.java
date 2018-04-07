package com.mmc.lot.util;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.mmc.lot.IotApplication;
import com.mmc.lot.data.DataCenter;

/**
 * @author zhaogaofeng
 * @date 2017/11/22
 * description:
 */

public final class LocationHelper {
    public LocationClient mLocationClient;
    private LocationListener mListener;
    private static LocationHelper mHelper;

    private LocationHelper() {
        init();
    }

    public static void getLocation() {
        if (mHelper == null) {
            synchronized (LocationHelper.class) {
                if (mHelper == null) {
                    mHelper = new LocationHelper();
                }
            }
        }
    }

    private void init() {
        try {
            mLocationClient = new LocationClient(IotApplication.getInstance());
            mListener = new LocationListener();
            mLocationClient.registerLocationListener(mListener);
            LocationClientOption option = new LocationClientOption();
            // 1、高精度模式定位策略：这种定位模式下，会同时使用网络定位和GPS定位，优先返回最高精度的定位结果；
            // 2、低功耗模式定位策略：该定位模式下，不会使用GPS，只会使用网络定位（Wi-Fi和基站定位）；
            // 3、仅用设备模式定位策略：这种定位模式下，不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位。
            // 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            // option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
            option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
            option.setScanSpan(1000); // 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
            option.disableCache(false);//禁止启用缓存定位
            option.setOpenGps(false); //关闭开启GPS操作
            option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            mLocationClient.setLocOption(option);
            mLocationClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class LocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            try {
                SharePreUtils.getInstance().setLocation(location);
                DataCenter.SetUserInfo.setGps(location.getLongitude(), location.getLatitude());
                Log.e("LocationHelper", "设置经纬度" + location.getLongitude() + ", " + location.getLatitude());
                mLocationClient.stop();
                mLocationClient.unRegisterLocationListener(mListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
