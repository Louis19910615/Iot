package com.mmc.lot.bean;

/**
 * Created by zhangzd on 2018/3/24.
 */

public class RegisterBean {

    private int c;
    private String m;
    private String o;
    private String e;


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

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    @Override
    public String toString() {
        return "RegisterBean{" +
                "c=" + c +
                ", m='" + m + '\'' +
                ", o='" + o + '\'' +
                ", e='" + e + '\'' +
                '}';
    }
}
