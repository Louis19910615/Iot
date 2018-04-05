package com.mmc.lot.eventbus.ui;

/**
 * Created by louis on 2018/4/1.
 */

public class ShowToastEvent {
    private String message;
    public ShowToastEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
