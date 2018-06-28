package com.amanarora.gify.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RandomGiphyResponse {

    @SerializedName("data")
    @Expose
    private GifObject data;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public GifObject getData() {
        return data;
    }

    public void setData(GifObject data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}