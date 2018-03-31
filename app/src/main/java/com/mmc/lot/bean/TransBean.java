package com.mmc.lot.bean;

/**
 * Created by zhangzd on 2018/3/31.
 */

public class TransBean {


    /**
     * c : 1
     * m : 获取运输信息成功
     * o : {"ENERGY":"100","CONTACTNUMBER":"+8613800138000","MAXRANGE":"60","PRCATEGORY":"流感疫苗","INTERVALTIME":"1","TAGID":"aabbccddeeff","TAGCATEGORY":"bluetooth","MAC":"00:11:22:33:44:55","GPSY":"123","CGNAME":"深圳市人民医院","PRCOMPANY":"中国医药集团","GPSX":"32","NAME":"中国医药集团","CGADDRESS":"广东省深圳市罗湖区东门北路1017号","UPTIME":1522289568000,"MINRANGE":"-30","ADDRESS":"北京市海淀区知春路20号","TAGDESCRIPTION":"record temperature","TRANSUSERID":"cde6553a1b9d4a7db7770f6dbd191d3f","LOGISTICSCOMPANY":"顺丰速运","CGCONTACTNUMBER":"+8613811138111"}
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
         * ENERGY : 100
         * CONTACTNUMBER : +8613800138000
         * MAXRANGE : 60
         * PRCATEGORY : 流感疫苗
         * INTERVALTIME : 1
         * TAGID : aabbccddeeff
         * TAGCATEGORY : bluetooth
         * MAC : 00:11:22:33:44:55
         * GPSY : 123
         * CGNAME : 深圳市人民医院
         * PRCOMPANY : 中国医药集团
         * GPSX : 32
         * NAME : 中国医药集团
         * CGADDRESS : 广东省深圳市罗湖区东门北路1017号
         * UPTIME : 1522289568000
         * MINRANGE : -30
         * ADDRESS : 北京市海淀区知春路20号
         * TAGDESCRIPTION : record temperature
         * TRANSUSERID : cde6553a1b9d4a7db7770f6dbd191d3f
         * LOGISTICSCOMPANY : 顺丰速运
         * CGCONTACTNUMBER : +8613811138111
         */

        private String ENERGY;
        private String CONTACTNUMBER;
        private String MAXRANGE;
        private String PRCATEGORY;
        private String INTERVALTIME;
        private String TAGID;
        private String TAGCATEGORY;
        private String MAC;
        private String GPSY;
        private String CGNAME;
        private String PRCOMPANY;
        private String GPSX;
        private String NAME;
        private String CGADDRESS;
        private long UPTIME;
        private String MINRANGE;
        private String ADDRESS;
        private String TAGDESCRIPTION;
        private String TRANSUSERID;
        private String LOGISTICSCOMPANY;
        private String CGCONTACTNUMBER;

        public String getENERGY() {
            return ENERGY;
        }

        public void setENERGY(String ENERGY) {
            this.ENERGY = ENERGY;
        }

        public String getCONTACTNUMBER() {
            return CONTACTNUMBER;
        }

        public void setCONTACTNUMBER(String CONTACTNUMBER) {
            this.CONTACTNUMBER = CONTACTNUMBER;
        }

        public String getMAXRANGE() {
            return MAXRANGE;
        }

        public void setMAXRANGE(String MAXRANGE) {
            this.MAXRANGE = MAXRANGE;
        }

        public String getPRCATEGORY() {
            return PRCATEGORY;
        }

        public void setPRCATEGORY(String PRCATEGORY) {
            this.PRCATEGORY = PRCATEGORY;
        }

        public String getINTERVALTIME() {
            return INTERVALTIME;
        }

        public void setINTERVALTIME(String INTERVALTIME) {
            this.INTERVALTIME = INTERVALTIME;
        }

        public String getTAGID() {
            return TAGID;
        }

        public void setTAGID(String TAGID) {
            this.TAGID = TAGID;
        }

        public String getTAGCATEGORY() {
            return TAGCATEGORY;
        }

        public void setTAGCATEGORY(String TAGCATEGORY) {
            this.TAGCATEGORY = TAGCATEGORY;
        }

        public String getMAC() {
            return MAC;
        }

        public void setMAC(String MAC) {
            this.MAC = MAC;
        }

        public String getGPSY() {
            return GPSY;
        }

        public void setGPSY(String GPSY) {
            this.GPSY = GPSY;
        }

        public String getCGNAME() {
            return CGNAME;
        }

        public void setCGNAME(String CGNAME) {
            this.CGNAME = CGNAME;
        }

        public String getPRCOMPANY() {
            return PRCOMPANY;
        }

        public void setPRCOMPANY(String PRCOMPANY) {
            this.PRCOMPANY = PRCOMPANY;
        }

        public String getGPSX() {
            return GPSX;
        }

        public void setGPSX(String GPSX) {
            this.GPSX = GPSX;
        }

        public String getNAME() {
            return NAME;
        }

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }

        public String getCGADDRESS() {
            return CGADDRESS;
        }

        public void setCGADDRESS(String CGADDRESS) {
            this.CGADDRESS = CGADDRESS;
        }

        public long getUPTIME() {
            return UPTIME;
        }

        public void setUPTIME(long UPTIME) {
            this.UPTIME = UPTIME;
        }

        public String getMINRANGE() {
            return MINRANGE;
        }

        public void setMINRANGE(String MINRANGE) {
            this.MINRANGE = MINRANGE;
        }

        public String getADDRESS() {
            return ADDRESS;
        }

        public void setADDRESS(String ADDRESS) {
            this.ADDRESS = ADDRESS;
        }

        public String getTAGDESCRIPTION() {
            return TAGDESCRIPTION;
        }

        public void setTAGDESCRIPTION(String TAGDESCRIPTION) {
            this.TAGDESCRIPTION = TAGDESCRIPTION;
        }

        public String getTRANSUSERID() {
            return TRANSUSERID;
        }

        public void setTRANSUSERID(String TRANSUSERID) {
            this.TRANSUSERID = TRANSUSERID;
        }

        public String getLOGISTICSCOMPANY() {
            return LOGISTICSCOMPANY;
        }

        public void setLOGISTICSCOMPANY(String LOGISTICSCOMPANY) {
            this.LOGISTICSCOMPANY = LOGISTICSCOMPANY;
        }

        public String getCGCONTACTNUMBER() {
            return CGCONTACTNUMBER;
        }

        public void setCGCONTACTNUMBER(String CGCONTACTNUMBER) {
            this.CGCONTACTNUMBER = CGCONTACTNUMBER;
        }
    }
}
