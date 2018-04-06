package com.mmc.lot.util;

import com.mmc.lot.bean.FormBean;
import com.mmc.lot.data.DataCenter;

/**
 * Created by louis on 2018/4/6.
 */

public class RequestDataTransfer {

    public static FormBean getFormBean() {
        FormBean bean = new FormBean();
        DataCenter dataCenter = DataCenter.getInstance();
        bean.setToken(dataCenter.getUserInfo().getToken());

        FormBean.TransportInformationBean transBean = new FormBean.TransportInformationBean();
        transBean.setLogisticsCompany(dataCenter.getLogisticsInfo().getLogisticsCompany());
        transBean.setOrderId(dataCenter.getLogisticsInfo().getLogisticsId());

        FormBean.TransportInformationBean.ConsigneeBean consignee = new FormBean.TransportInformationBean.ConsigneeBean(dataCenter.getLogisticsInfo().getConsigneeName(), dataCenter.getLogisticsInfo().getConsigneeAddress(), dataCenter.getLogisticsInfo().getConsigneeTel());
        transBean.setConsignee(consignee);
        FormBean.TransportInformationBean.ConsignorBean consignor = new FormBean.TransportInformationBean.ConsignorBean(dataCenter.getLogisticsInfo().getShipperName(), dataCenter.getLogisticsInfo().getShipperAddress(), dataCenter.getLogisticsInfo().getShipperTel());
        transBean.setConsignor(consignor);
        FormBean.TransportInformationBean.ProductBean productBean = new FormBean.TransportInformationBean.ProductBean(dataCenter.getLogisticsInfo().getProductCategory(), dataCenter.getLogisticsInfo().getProductName());
        transBean.setProduct(productBean);


        FormBean.TransportInformationBean.ValidRangeBean validRangeBean = new FormBean.TransportInformationBean.ValidRangeBean(dataCenter.getLogisticsInfo().getMinTemperature(), dataCenter.getLogisticsInfo().getMaxTemperature());
        transBean.setValidRange(validRangeBean);

        FormBean.TagInformationBean tagInformationBean = new FormBean.TagInformationBean();
        tagInformationBean.setMac(dataCenter.getDeviceInfo().getDeviceAddress());
        tagInformationBean.setTagID(dataCenter.getDeviceInfo().getTagId());
        tagInformationBean.setEnergy(dataCenter.getDeviceInfo().getRemainingBattery());
        tagInformationBean.setIntervalTime(1);
        tagInformationBean.setGps("113.92,22.52");
        tagInformationBean.setCategory("bluetooth");
        tagInformationBean.setDescription("record temperature");

        bean.setTransportInformation(transBean);
        bean.setTagInformation(tagInformationBean);
        return bean;
    }
}
