package com.mmc.lot.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author  by zhangzd on 2018/3/28.
 */

public class TempBean implements Serializable {


    private static final long serialVersionUID = 1437642572733936868L;
    /**
     * c : 1
     * m : 获取成功
     * o : [{"UPTIME":1522558422000,"TEMP":"[30.11,-20.31]","UPUSERID":"cde6553a1b9d4a7db7770f6dbd191d3f","TAGID":"aabbccddeeff"}]
     * e : null
     */

    private int c;
    private String m;
    private Object e;
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

    public Object getE() {
        return e;
    }

    public void setE(Object e) {
        this.e = e;
    }

    public List<OBean> getO() {
        return o;
    }

    public void setO(List<OBean> o) {
        this.o = o;
    }

    public static class OBean implements Serializable {
        private static final long serialVersionUID = 1695288466194965528L;
        /**
         * UPTIME : 1522558422000
         * TEMP : [30.11,-20.31]
         * UPUSERID : cde6553a1b9d4a7db7770f6dbd191d3f
         * TAGID : aabbccddeeff
         */

        private long UPTIME;
        private String TEMP;
        private String UPUSERID;
        private String TAGID;

        public long getUPTIME() {
            return UPTIME;
        }

        public void setUPTIME(long UPTIME) {
            this.UPTIME = UPTIME;
        }

        public String getTEMP() {
            return TEMP;
        }

        public void setTEMP(String TEMP) {
            this.TEMP = TEMP;
        }

        public String getUPUSERID() {
            return UPUSERID;
        }

        public void setUPUSERID(String UPUSERID) {
            this.UPUSERID = UPUSERID;
        }

        public String getTAGID() {
            return TAGID;
        }

        public void setTAGID(String TAGID) {
            this.TAGID = TAGID;
        }
    }
}
