package com.builderstrom.user.repository.retrofit.modals;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatListing implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CatListing> CREATOR = new Parcelable.Creator<CatListing>() {
        @Override
        public CatListing createFromParcel(Parcel in) {
            return new CatListing(in);
        }

        @Override
        public CatListing[] newArray(int size) {
            return new CatListing[size];
        }
    };
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("region_id")
    @Expose
    private Object regionId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("parent_category")
    @Expose
    private String parentCategory;
    @SerializedName("visibility")
    @Expose
    private String visibility;
    @SerializedName("item_order")
    @Expose
    private String itemOrder;

    public CatListing() {
    }

    public CatListing(String id, String title) {
        this.id = id;
        this.title = title;
    }

    protected CatListing(Parcel in) {
        id = in.readString();
        regionId = in.readValue(Object.class.getClassLoader());
        title = in.readString();
        parentCategory = in.readString();
        visibility = in.readString();
        itemOrder = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getRegionId() {
        return regionId;
    }

    public void setRegionId(Object regionId) {
        this.regionId = regionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(String itemOrder) {
        this.itemOrder = itemOrder;
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeValue(regionId);
        dest.writeString(title);
        dest.writeString(parentCategory);
        dest.writeString(visibility);
        dest.writeString(itemOrder);
    }
}