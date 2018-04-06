package com.mmc.lot.data;

import com.mmc.lot.bean.TransBean;

/**
 * Created by louis on 2018/4/6.
 */

public class DataCenterUtil {

    public static void parseTransBean(TransBean transBean) {
        TransBean.OBean oBean = transBean.getO();

        DataCenter.SetLogisticsInfo.setLogisticsId(oBean.getTRANSUSERID());
        DataCenter.SetLogisticsInfo.setLogisticsCompany(oBean.getLOGISTICSCOMPANY());

        DataCenter.SetLogisticsInfo.setShipperName(oBean.getNAME());
        DataCenter.SetLogisticsInfo.setShipperAddress(oBean.getADDRESS());
        DataCenter.SetLogisticsInfo.setShipperTel(oBean.getCONTACTNUMBER());

        DataCenter.SetLogisticsInfo.setConsigneeName(oBean.getCGNAME());
        DataCenter.SetLogisticsInfo.setConsigneeAddress(oBean.getCGADDRESS());
        DataCenter.SetLogisticsInfo.setConsigneeTel(oBean.getCGCONTACTNUMBER());

        DataCenter.SetLogisticsInfo.setProductName(oBean.getPRCATEGORY());
        DataCenter.SetLogisticsInfo.setProductCategory(oBean.getPRCATEGORY());

        DataCenter.SetLogisticsInfo.setMinTemperature(Integer.valueOf(oBean.getMINRANGE()));
        DataCenter.SetLogisticsInfo.setMaxTemperature(Integer.valueOf(oBean.getMAXRANGE()));

    }
}
