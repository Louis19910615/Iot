package com.mmc.lot.data;

import java.util.List;

import android.provider.ContactsContract;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mmc.lot.eventbus.ble.DisConnectEvent;
import com.mmc.lot.eventbus.ui.ShowToastEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by liushuaizheng on 2018/4/5.
 */

public class DataCenter {

    private static final String TAG = "DataCenter";

    private static DataCenter sInstance;

    private DeviceInfo deviceInfo;
    private LogisticsInfo logisticsInfo;
    private UserInfo userInfo;

    private DataCenter () {
        deviceInfo = new DeviceInfo();
        userInfo = new UserInfo();
        logisticsInfo = new LogisticsInfo();

    }

    public static DataCenter getInstance() {
        if (sInstance == null) {
            synchronized (DataCenter.class) {
                if (sInstance == null) {
                    sInstance = new DataCenter();
                }
            }
        }

        return sInstance;
    }

    public void clearAll() {
        deviceInfo = new DeviceInfo();
        userInfo = new UserInfo();
        logisticsInfo = new LogisticsInfo();
    }

    public void clearDeviceInfo() {
        deviceInfo = new DeviceInfo();
    }

    public void clearLogisticsInfo() {
        logisticsInfo = new LogisticsInfo();
    }

    public void setLogisticsInfo(String manifestStr) {
        try {
            logisticsInfo = new Gson().fromJson(manifestStr, LogisticsInfo.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new DisConnectEvent(getDeviceInfo().getDeviceAddress()));
            EventBus.getDefault().post(new ShowToastEvent("异常断开，请重试。"));
        }
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public LogisticsInfo getLogisticsInfo() {
        return logisticsInfo;
    }

    public void setLogisticsInfo(LogisticsInfo logisticsInfo) {
        this.logisticsInfo = logisticsInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public static class SetUserInfo {

        public static void setToken(String token) {
            UserInfo userInfo = DataCenter.getInstance().getUserInfo();
            userInfo.setToken(token);
            DataCenter.getInstance().setUserInfo(userInfo);
            int actor = DataCenter.getInstance().getUserInfo().getActor();
            Log.e(TAG, "actor is " + actor);
        }

        public static void setUserName(String userName) {
            UserInfo userInfo = DataCenter.getInstance().getUserInfo();
            userInfo.setUserName(userName);
            DataCenter.getInstance().setUserInfo(userInfo);
        }

        public static void setGps(double longitude, double latitude) {
            UserInfo userInfo = DataCenter.getInstance().getUserInfo();
            userInfo.setLongitude(longitude);
            userInfo.setLatitude(latitude);
            DataCenter.getInstance().setUserInfo(userInfo);
        }
    }

    public static class SetDeviceInfo {

        public static void addTemperatureData(Double temperatureData) {
            DeviceInfo deviceInfo = DataCenter.getInstance().getDeviceInfo();
            List<Double> temperatureDatas = deviceInfo.getTemperatureDatas();
            temperatureDatas.add(temperatureData);
            deviceInfo.setTemperatureDatas(temperatureDatas);
            DataCenter.getInstance().setDeviceInfo(deviceInfo);
        }

        public static void setStartTime(String startTime) {
            DeviceInfo deviceInfo = DataCenter.getInstance().getDeviceInfo();
            deviceInfo.setStarTime(startTime);
            DataCenter.getInstance().setDeviceInfo(deviceInfo);
        }

        public static void setDeviceAddress(String deviceAddress) {
            DeviceInfo deviceInfo = DataCenter.getInstance().getDeviceInfo();
            deviceInfo.setDeviceAddress(deviceAddress);
            DataCenter.getInstance().setDeviceInfo(deviceInfo);
        }

        public static void setRemainingBattery(int remainingBattery) {
            DeviceInfo deviceInfo = DataCenter.getInstance().getDeviceInfo();
            deviceInfo.setRemainingBattery(remainingBattery);
            DataCenter.getInstance().setDeviceInfo(deviceInfo);
        }

        public static void setTemperatureDatas(List<Double> temperatureDatas) {
            DeviceInfo deviceInfo = DataCenter.getInstance().getDeviceInfo();
            deviceInfo.setTemperatureDatas(temperatureDatas);
            DataCenter.getInstance().setDeviceInfo(deviceInfo);
        }

        public static void setTimeInterval(int timeInterval) {
            DeviceInfo deviceInfo = DataCenter.getInstance().getDeviceInfo();
            deviceInfo.setTimeInterval(timeInterval);
            DataCenter.getInstance().setDeviceInfo(deviceInfo);
        }

        public static void setDeviceName(String deviceName) {
            DeviceInfo deviceInfo = DataCenter.getInstance().getDeviceInfo();
            deviceInfo.setDeviceName(deviceName);
            DataCenter.getInstance().setDeviceInfo(deviceInfo);
        }

        public static void setCategory(String category) {
            DeviceInfo deviceInfo = DataCenter.getInstance().getDeviceInfo();
            deviceInfo.setCategory(category);
            DataCenter.getInstance().setDeviceInfo(deviceInfo);
        }

        public static void setDescription(String description) {
            DeviceInfo deviceInfo = DataCenter.getInstance().getDeviceInfo();
            deviceInfo.setDescription(description);
            DataCenter.getInstance().setDeviceInfo(deviceInfo);
        }
    }

    public static class SetLogisticsInfo {

        // 物流号
        public static void setLogisticsId(String logisticsId) {
            LogisticsInfo logisticsInfo = DataCenter.getInstance().getLogisticsInfo();
            logisticsInfo.setLogisticsId(logisticsId);
            DataCenter.getInstance().setLogisticsInfo(logisticsInfo);
        }

        // 物流公司
        public static void setLogisticsCompany(String logisticsCompany) {
            LogisticsInfo logisticsInfo = DataCenter.getInstance().getLogisticsInfo();
            logisticsInfo.setLogisticsCompany(logisticsCompany);
            DataCenter.getInstance().setLogisticsInfo(logisticsInfo);
        }

        // 发货人姓名
        public static void setShipperName(String shipperName) {
            LogisticsInfo logisticsInfo = DataCenter.getInstance().getLogisticsInfo();
            logisticsInfo.setShipperName(shipperName);
            DataCenter.getInstance().setLogisticsInfo(logisticsInfo);
        }
        // 发货人地址
        public static void setShipperAddress(String shipperAddress) {
            LogisticsInfo logisticsInfo = DataCenter.getInstance().getLogisticsInfo();
            logisticsInfo.setShipperAddress(shipperAddress);
            DataCenter.getInstance().setLogisticsInfo(logisticsInfo);
        }
        // 发货人电话
        public static void setShipperTel(String shipperTel) {
            LogisticsInfo logisticsInfo = DataCenter.getInstance().getLogisticsInfo();
            logisticsInfo.setShipperTel(shipperTel);
            DataCenter.getInstance().setLogisticsInfo(logisticsInfo);
        }

        // 收货人姓名
        public static void setConsigneeName(String consigneeName) {
            LogisticsInfo logisticsInfo = DataCenter.getInstance().getLogisticsInfo();
            logisticsInfo.setConsigneeName(consigneeName);
            DataCenter.getInstance().setLogisticsInfo(logisticsInfo);
        }
        // 收货人地址
        public static void setConsigneeAddress(String consigneeAddress) {
            LogisticsInfo logisticsInfo = DataCenter.getInstance().getLogisticsInfo();
            logisticsInfo.setConsigneeAddress(consigneeAddress);
            DataCenter.getInstance().setLogisticsInfo(logisticsInfo);
        }
        // 收货人电话
        public static void setConsigneeTel(String consigneeTel) {
            LogisticsInfo logisticsInfo = DataCenter.getInstance().getLogisticsInfo();
            logisticsInfo.setConsigneeTel(consigneeTel);
            DataCenter.getInstance().setLogisticsInfo(logisticsInfo);
        }

        // 产品名
        public static void setProductName(String productName) {
            LogisticsInfo logisticsInfo = DataCenter.getInstance().getLogisticsInfo();
            logisticsInfo.setProductName(productName);
            DataCenter.getInstance().setLogisticsInfo(logisticsInfo);
        }
        // 产品种类
        public static void setProductCategory(String productCategory) {
            LogisticsInfo logisticsInfo = DataCenter.getInstance().getLogisticsInfo();
            logisticsInfo.setProductCategory(productCategory);
            DataCenter.getInstance().setLogisticsInfo(logisticsInfo);
        }

        // 最低温度
        public static void setMinTemperature(int minTemperature) {
            LogisticsInfo logisticsInfo = DataCenter.getInstance().getLogisticsInfo();
            logisticsInfo.setMinTemperature(minTemperature);
            DataCenter.getInstance().setLogisticsInfo(logisticsInfo);
        }
        // 最高温度
        public static void setMaxTemperature(int maxTemperature) {
            LogisticsInfo logisticsInfo = DataCenter.getInstance().getLogisticsInfo();
            logisticsInfo.setMaxTemperature(maxTemperature);
            DataCenter.getInstance().setLogisticsInfo(logisticsInfo);
        }
    }
}
