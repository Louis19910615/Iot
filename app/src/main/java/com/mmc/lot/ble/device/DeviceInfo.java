package com.mmc.lot.ble.device;

import com.mmc.lot.bean.BindBean;
import com.mmc.lot.bean.FormBean;
import com.mmc.lot.bean.TagBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 2018/4/1.
 */

public class DeviceInfo {

    private String deviceAddress;
    private String deviceName;
    private String token;
    // 上传数据
    private TagBean tagBean;
    // 上传运单信息
    private FormBean formBean;
    // 绑定tagID及运单号
    private BindBean bindBean;

    // 温度数据
    private List<Double> tempDatas;
    private static DeviceInfo sInstance;

    private DeviceInfo () {
        tempDatas = new ArrayList<>();
    }

    public static DeviceInfo getInstance() {
        if (sInstance == null) {
            synchronized (DeviceInfo.class) {
                if (sInstance == null) {
                    sInstance = new DeviceInfo();
                }
            }
        }

        return sInstance;
    }

    public List<Double> getTempDatas() {
        return this.tempDatas;
    }

    public void addTempData(Double data, int offset) {
        tempDatas.add(data);
    }

    public void onDestory() {
        sInstance = null;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TagBean getTagBean() {
        return tagBean;
    }

    public void setTagBean(TagBean tagBean) {
        this.tagBean = tagBean;
    }

    public FormBean getFormBean() {
        return formBean;
    }

    public void setFormBean(FormBean formBean) {
        this.formBean = formBean;
    }

    public BindBean getBindBean() {
        return bindBean;
    }

    public void setBindBean(BindBean bindBean) {
        this.bindBean = bindBean;
    }

    public static DeviceInfo getsInstance() {
        return sInstance;
    }

    public static void setsInstance(DeviceInfo sInstance) {
        DeviceInfo.sInstance = sInstance;
    }
}
