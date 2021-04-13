package com.builderstrom.user.repository.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GalleryData {
    @SerializedName("gallery_id")
    @Expose
    private String galleryId;
    @SerializedName("row_id")
    @Expose
    private String rowId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("pics")
    @Expose
    private List<GalleryPicModel> pics = null;

    @SerializedName("custom_field_data")
    @Expose
    private List<List<MetaValues>> custom_field_data = null;


    private List<LocalMetaValues> metaData;

    public GalleryData() {
    }

    public GalleryData(String galleryId, String rowId, String title, String createdDate, List<GalleryPicModel> pics) {
        this.galleryId = galleryId;
        this.rowId = rowId;
        this.title = title;
        this.createdDate = createdDate;
        this.pics = pics;
    }

    public String getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(String galleryId) {
        this.galleryId = galleryId;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public List<GalleryPicModel> getPics() {
        return pics;
    }

    public void setPics(List<GalleryPicModel> pics) {
        this.pics = pics;
    }


    public List<List<MetaValues>> getCustom_field_data() {
        return custom_field_data;
    }

    public void setCustom_field_data(List<List<MetaValues>> custom_field_data) {
        this.custom_field_data = custom_field_data;
    }


    public List<LocalMetaValues> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<LocalMetaValues> metaData) {
        this.metaData = metaData;
    }


}


