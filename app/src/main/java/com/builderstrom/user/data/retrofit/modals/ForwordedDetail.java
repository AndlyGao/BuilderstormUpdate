package com.builderstrom.user.data.retrofit.modals;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForwordedDetail implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ForwordedDetail> CREATOR = new Parcelable.Creator<ForwordedDetail>() {
        @Override
        public ForwordedDetail createFromParcel(Parcel in) {
            return new ForwordedDetail(in);
        }

        @Override
        public ForwordedDetail[] newArray(int size) {
            return new ForwordedDetail[size];
        }
    };
    @SerializedName("is_email")
    @Expose
    private String isEmail;
    @SerializedName("other_info")
    @Expose
    private String otherInfo;
    @SerializedName("date_added")
    @Expose
    private String dateAdded;
    @SerializedName("short_code")
    @Expose
    private String shortCode;
    @SerializedName("fullname")
    @Expose
    private String fullname;

    protected ForwordedDetail(Parcel in) {
        isEmail = in.readString();
        otherInfo = in.readString();
        dateAdded = in.readString();
        shortCode = in.readString();
        fullname = in.readString();
    }

    public String getIsEmail() {
        return isEmail;
    }

    public void setIsEmail(String isEmail) {
        this.isEmail = isEmail;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(isEmail);
        dest.writeString(otherInfo);
        dest.writeString(dateAdded);
        dest.writeString(shortCode);
        dest.writeString(fullname);
    }
}