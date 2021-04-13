package com.builderstrom.user.data.retrofit.modals;

import java.util.List;

public class DeleteGalleryChildImageModel {
    private int groupPosition;
    private List<GalleryPicModel> picList = null;

    public DeleteGalleryChildImageModel(int groupPosition, List<GalleryPicModel> picList) {
        this.groupPosition = groupPosition;
        this.picList = picList;
    }

    public int getGroupPosition() {
        return groupPosition;
    }

    public List<GalleryPicModel> getPicList() {
        return picList;
    }
}
