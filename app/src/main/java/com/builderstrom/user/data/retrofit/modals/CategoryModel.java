package com.builderstrom.user.data.retrofit.modals;


import java.util.List;

public class CategoryModel extends BaseApiModel {

    private List<CatListing> data = null;

    public List<CatListing> getData() {
        return data;
    }
}
