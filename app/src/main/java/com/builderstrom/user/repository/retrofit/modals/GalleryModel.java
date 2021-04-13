package com.builderstrom.user.repository.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GalleryModel extends BaseApiModel {

    @SerializedName("data")
    @Expose
    private List<GalleryData> data = null;

    public List<GalleryData> getData() {
        return data;
    }

    public void setData(List<GalleryData> data) {
        this.data = data;
    }
}

