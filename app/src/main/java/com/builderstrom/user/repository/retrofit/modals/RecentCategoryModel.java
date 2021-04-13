package com.builderstrom.user.repository.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecentCategoryModel extends BaseApiModel {
    @SerializedName("data")
    @Expose
    private List<RecentCategory> data = null;

    public List<RecentCategory> getData() {
        return data;
    }

    public void setData(List<RecentCategory> data) {
        this.data = data;
    }



}
