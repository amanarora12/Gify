package com.amanarora.gify.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RandomGiphyResponse {

    @SerializedName("data")
    @Expose
    private GifObject data;

    public GifObject getData() {
        return data;
    }

    public void setData(GifObject data) {
        this.data = data;
    }
}