package com.mmc.lot.bean;

import java.util.List;

/**
 * Created by zhangzd on 2018/3/28.
 */

public class TagBean {


    /**
     * token : bbdd44kkcde6553a1b9d4a7db7770f6dbd191d3f1524830753
     * tagInformation : {"tagID":"aabbccddeeff","mac":"00:11:22:33:44:55","startTime":"2018/03/04 15:00:00","intervalTime":1,"energy":100,"gps":"123,789"}
     * tagData : [30.11,-20.31,25.12,-25.16]
     */

    private String token;
    private TagInformationBean tagInformation;
    private List<Double> tagData;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TagInformationBean getTagInformation() {
        return tagInformation;
    }

    public void setTagInformation(TagInformationBean tagInformation) {
        this.tagInformation = tagInformation;
    }

    public List<Double> getTagData() {
        return tagData;
    }

    public void setTagData(List<Double> tagData) {
        this.tagData = tagData;
    }

    public static class TagInformationBean {
        /**
         * tagID : aabbccddeeff
         * mac : 00:11:22:33:44:55
         * startTime : 2018/03/04 15:00:00
         * intervalTime : 1
         * energy : 100
         * gps : 123,789
         */

        private String tagID;
        private String mac;
        private String startTime;
        private int intervalTime;
        private int energy;
        private String gps;

        public String getTagID() {
            return tagID;
        }

        public void setTagID(String tagID) {
            this.tagID = tagID;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public int getIntervalTime() {
            return intervalTime;
        }

        public void setIntervalTime(int intervalTime) {
            this.intervalTime = intervalTime;
        }

        public int getEnergy() {
            return energy;
        }

        public void setEnergy(int energy) {
            this.energy = energy;
        }

        public String getGps() {
            return gps;
        }

        public void setGps(String gps) {
            this.gps = gps;
        }

        @Override
        public String toString() {
            return "{" +
                    "tagID='" + tagID + '\'' +
                    ", mac='" + mac + '\'' +
                    ", startTime='" + startTime + '\'' +
                    ", intervalTime=" + intervalTime +
                    ", energy=" + energy +
                    ", gps='" + gps + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TagBean{" +
                "token='" + token + '\'' +
                ", tagInformation=" + tagInformation.toString() +
                ", tagData=" + tagData +
                '}';
    }
}
