package com.builderstrom.user.repository.retrofit.modals;

import java.util.List;

public class PojoGalleryComment extends BaseApiModel {

    private List<GalleryComment> data = null;

    public List<GalleryComment> getAllComments() {
        return data;
    }

}
