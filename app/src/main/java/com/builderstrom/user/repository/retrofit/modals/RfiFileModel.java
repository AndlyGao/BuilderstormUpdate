package com.builderstrom.user.repository.retrofit.modals;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;

public class RfiFileModel {
    @SerializedName("original_name")
    @Expose
    public String name;
    @SerializedName("filePath")
    @Expose
    public String filePath;
    @SerializedName("file")
    @Expose
    public MultipartBody.Part file;
    @SerializedName("bitmap")
    @Expose
    public Bitmap imageBitmap;
    @SerializedName("file_url")
    @Expose
    private String fileurl;
    @SerializedName("thumbnail_url")
    @Expose
    private String thumbnailUrl;
    @SerializedName("image")
    @Expose
    private byte[] byteArray;

    public RfiFileModel() {
    }

    public RfiFileModel(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
    }

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

    public MultipartBody.Part getFile() {
        return file;
    }

    public void setFile(MultipartBody.Part file) {
        this.file = file;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

}
