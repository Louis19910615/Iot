package com.mmc.lot.bean;

import java.util.List;

/**
 * @author  by zhangzd on 2018/3/28.
 */

public class TempBean {


    /**
     * c : 1
     * m : 获取成功
     * o : [{"UPTIME":"123","TEMP":[30.11,-20.31],"UPSERID":"004e"}]
     * e :
     */

    private int c;
    private String m;
    private String e;
    private List<OBean> o;

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public List<OBean> getO() {
        return o;
    }

    public void setO(List<OBean> o) {
        this.o = o;
    }

    public static class OBean {
        /**
         * UPTIME : 123
         * TEMP : [30.11,-20.31]
         * UPSERID : 004e
         */

        private String UPTIME;
        private String UPSERID;
        private String TEMP;

        public String getUPTIME() {
            return UPTIME;
        }

        public void setUPTIME(String UPTIME) {
            this.UPTIME = UPTIME;
        }

        public String getUPSERID() {
            return UPSERID;
        }

        public void setUPSERID(String UPSERID) {
            this.UPSERID = UPSERID;
        }

        public String getTEMP() {
            return TEMP;
        }

        public void setTEMP(String TEMP) {
            this.TEMP = TEMP;
        }
    }
}
