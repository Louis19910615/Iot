package com.mmc.lot.bean;

import java.util.List;

/**
 * Created by zhangzd on 2018/4/1.
 */

public class BodyData {

    private String name;
    private int ID;
    private List<String> weight;
    private List<Long> current_time;



    public List<Long> getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(List<Long> current_time) {
        this.current_time = current_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public List<String> getWeight() {
        return weight;
    }

    public void setWeight(List<String> weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "BodyData{" +
                "name='" + name + '\'' +
                ", ID=" + ID +
                ", weight=" + weight +
                ", current_time=" + current_time +
                '}';
    }

}
