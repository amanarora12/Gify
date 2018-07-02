
package com.amanarora.gify.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GiphyResponse {

    @SerializedName("data")
    @Expose
    private List<GifObject> data = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;

    public List<GifObject> getData() {
        return data;
    }

    public void setData(List<GifObject> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
