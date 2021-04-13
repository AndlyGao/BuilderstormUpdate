package com.builderstrom.user.data.retrofit.modals;

import java.util.List;

public class PhotosDataModel {
    private List<GalleryData> dataList = null;
    private boolean isOffline = false;

    public PhotosDataModel(List<GalleryData> dataList, boolean isOffline) {
        this.dataList = dataList;
        this.isOffline = isOffline;
    }

    public List<GalleryData> getDataList() {
        return dataList;
    }

    public boolean isOffline() {
        return isOffline;
    }
}
