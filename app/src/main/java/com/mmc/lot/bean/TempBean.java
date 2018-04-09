package com.mmc.lot.bean;

import java.io.Serializable;

/**
 * @author  by zhangzd on 2018/3/28.
 */

public class TempBean implements Serializable {


    /**
     * c : 1
     * m : 获取成功
     * o : {"UPTIME":"2018-04-09 10:33:52","TEMP":"30.11,-20.31,25.12,-25.16,40.11,-10.31,55.12,-35.16,40.11,-10.31,55.12,-35.16,30.11,-20.31,25.12,-25.16,40.11,-10.31,55.12,-35.16,40.11,-10.31,55.12,-35.16","STARTTIME":"2018-04-09 9:48:54","UPUSERID":"cc9ccd7e94af4bad85bac90dfd95c4d1","INTERVALTIME":10,"TAGID":"12345678910"}
     * e : null
     */

    private int c;
    private String m;
    private OBean o;
    private Object e;

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

    public OBean getO() {
        return o;
    }

    public void setO(OBean o) {
        this.o = o;
    }

    public Object getE() {
        return e;
    }

    public void setE(Object e) {
        this.e = e;
    }

    public static class OBean {
        /**
         * UPTIME : 2018-04-09 10:33:52
         * TEMP : 30.11,-20.31,25.12,-25.16,40.11,-10.31,55.12,-35.16,40.11,-10.31,55.12,-35.16,30.11,-20.31,25.12,-25.16,40.11,-10.31,55.12,-35.16,40.11,-10.31,55.12,-35.16
         * STARTTIME : 2018-04-09 9:48:54
         * UPUSERID : cc9ccd7e94af4bad85bac90dfd95c4d1
         * INTERVALTIME : 10
         * TAGID : 12345678910
         */

        private String UPTIME;
        private String TEMP;
        private String STARTTIME;
        private String UPUSERID;
        private int INTERVALTIME;
        private String TAGID;

        public String getUPTIME() {
            return UPTIME;
        }

        public void setUPTIME(String UPTIME) {
            this.UPTIME = UPTIME;
        }

        public String getTEMP() {
            return TEMP;
        }

        public void setTEMP(String TEMP) {
            this.TEMP = TEMP;
        }

        public String getSTARTTIME() {
            return STARTTIME;
        }

        public void setSTARTTIME(String STARTTIME) {
            this.STARTTIME = STARTTIME;
        }

        public String getUPUSERID() {
            return UPUSERID;
        }

        public void setUPUSERID(String UPUSERID) {
            this.UPUSERID = UPUSERID;
        }

        public int getINTERVALTIME() {
            return INTERVALTIME;
        }

        public void setINTERVALTIME(int INTERVALTIME) {
            this.INTERVALTIME = INTERVALTIME;
        }

        public String getTAGID() {
            return TAGID;
        }

        public void setTAGID(String TAGID) {
            this.TAGID = TAGID;
        }
    }
}
