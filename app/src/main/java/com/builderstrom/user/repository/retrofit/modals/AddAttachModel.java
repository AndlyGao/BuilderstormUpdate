package com.builderstrom.user.repository.retrofit.modals;

import android.graphics.Bitmap;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class AddAttachModel {
    @SerializedName("original_name")
    @Expose
    private String name;
    @SerializedName("filePath")
    @Expose
    private String filePath;
    @SerializedName("bitmap")
    @Expose
    private Bitmap imageBitmap;
    @SerializedName("fileurl")
    @Expose
    private String fileurl;
    @SerializedName("thumbnail_url")
    @Expose
    private String thumbnailUrl;

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}
