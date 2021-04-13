package com.builderstrom.user.repository.retrofit.modals;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryModel extends BaseApiModel {

    private List<CatListing> data = null;

    public List<CatListing> getData() {
        return data;
    }
}
