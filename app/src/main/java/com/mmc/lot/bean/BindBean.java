package com.mmc.lot.bean;

/**
 * Created by zhangzd on 2018/3/29.
 */

public class BindBean {


    /**
     * transportInformation : {"waybillNumber":"123456789abcd"}
     * tagInformation : {"tagID":"aabbccddeeff"}
     */

    private TransportInformationBean transportInformation;
    private TagInformationBean tagInformation;


    public TransportInformationBean getTransportInformation() {
        return transportInformation;
    }

    public void setTransportInformation(TransportInformationBean transportInformation) {
        this.transportInformation = transportInformation;
    }

    public TagInformationBean getTagInformation() {
        return tagInformation;
    }

    public void setTagInformation(TagInformationBean tagInformation) {
        this.tagInformation = tagInformation;
    }

    public static class TransportInformationBean {
        public TransportInformationBean(String waybillNumber) {
            this.waybillNumber = waybillNumber;
        }

        /**
         * waybillNumber : 123456789abcd
         */

        private String waybillNumber;

        public String getWaybillNumber() {
            return waybillNumber;
        }

        public void setWaybillNumber(String waybillNumber) {
            this.waybillNumber = waybillNumber;
        }
    }

    public static class TagInformationBean {
        public TagInformationBean(String tagID) {
            this.tagID = tagID;
        }

        /**
         * tagID : aabbccddeeff
         */

        private String tagID;

        public String getTagID() {
            return tagID;
        }

        public void setTagID(String tagID) {
            this.tagID = tagID;
        }
    }
}
