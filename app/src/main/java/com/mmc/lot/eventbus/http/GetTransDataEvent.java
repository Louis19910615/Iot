package com.mmc.lot.eventbus.http;

/**
 * Created by louis on 2018/4/6.
 */

public class GetTransDataEvent {

    private String tagId;
    private String token;

    public GetTransDataEvent(String tagId, String token) {
        this.tagId = tagId;
        this.token = token;
    }

    public String getTagId() {
        return this.tagId;
    }

    public String getToken() {
        return this.token;
    }
}
