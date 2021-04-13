package com.builderstrom.user.repository.retrofit.modals;

import java.util.List;

public class PojoAddGallery extends BaseApiModel {

    private GalleryDataModel data;

    public GalleryDataModel getData() {
        return data;
    }

    private class GalleryDataModel {
        private Integer gallery_id ;
        private List<GalleryImage> images = null;

        public Integer getGalleryId() {
            return gallery_id;
        }

        public List<GalleryImage> getImages() {
            return images;
        }
    }

    public class GalleryImage {
        private String name = "";

        public String getName() {
            return name;
        }
    }


}
