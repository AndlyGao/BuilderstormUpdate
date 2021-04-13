package com.builderstrom.user.data.retrofit.modals;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GalleryPicModel implements Parcelable {
    @SerializedName("imageBitmap")
    @Expose
    public Bitmap imageBitmap;
    @SerializedName("image_id")
    @Expose
    private String imageId;
    @SerializedName("is_client")
    @Expose
    private boolean isClient = false;
    @SerializedName("image_name")
    @Expose
    private String imageName;
    @SerializedName("image_comment")
    @Expose
    private String imageComment;

    @SerializedName("image_path")
    @Expose
    private String imagePath;

    public GalleryPicModel() {
    }

    public GalleryPicModel(Bitmap imageBitmap, String imageId, boolean isClient, String imageName,
                           String imageComment, String imagePath) {
        this.imageBitmap = imageBitmap;
        this.imageId = imageId;
        this.isClient = isClient;
        this.imageName = imageName;
        this.imageComment = imageComment;
        this.imagePath = imagePath;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageComment() {
        return imageComment;
    }

    public void setImageComment(String imageComment) {
        this.imageComment = imageComment;
    }

    public boolean isClient() {
        return isClient;
    }

    public void setClient(boolean client) {
        isClient = client;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    protected GalleryPicModel(Parcel in) {
        imageBitmap = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        imageId = in.readString();
        isClient = in.readByte() != 0x00;
        imageName = in.readString();
        imageComment = in.readString();
        imagePath = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(imageBitmap);
        dest.writeString(imageId);
        dest.writeByte((byte) (isClient ? 0x01 : 0x00));
        dest.writeString(imageName);
        dest.writeString(imageComment);
        dest.writeString(imagePath);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GalleryPicModel> CREATOR = new Parcelable.Creator<GalleryPicModel>() {
        @Override
        public GalleryPicModel createFromParcel(Parcel in) {
            return new GalleryPicModel(in);
        }

        @Override
        public GalleryPicModel[] newArray(int size) {
            return new GalleryPicModel[size];
        }
    };
}