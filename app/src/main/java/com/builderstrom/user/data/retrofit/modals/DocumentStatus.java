package com.builderstrom.user.data.retrofit.modals;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocumentStatus implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("region_id")
    @Expose
    private Object regionId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("status_type")
    @Expose
    private String statusType;
    @SerializedName("created_on")
    @Expose
    private String createdOn;

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

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }

    protected DocumentStatus(Parcel in) {
        id = in.readString();
        regionId = (Object) in.readValue(Object.class.getClassLoader());
        title = in.readString();
        statusType = in.readString();
        createdOn = in.readString();
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
        dest.writeString(statusType);
        dest.writeString(createdOn);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DocumentStatus> CREATOR = new Parcelable.Creator<DocumentStatus>() {
        @Override
        public DocumentStatus createFromParcel(Parcel in) {
            return new DocumentStatus(in);
        }

        @Override
        public DocumentStatus[] newArray(int size) {
            return new DocumentStatus[size];
        }
    };
}