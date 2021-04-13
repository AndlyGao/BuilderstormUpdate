package com.builderstrom.user.repository.retrofit.modals;

import android.os.Parcel;
import android.os.Parcelable;

public class MyItemsStatusModel implements Parcelable {

    private String username = "";
    private String between_text = "";
    private String strong_text = "";
    private String date = "";
    private String label_date = "";
    private String type = "";

    public MyItemsStatusModel() {
    }

    public String getUsername() {
        return username;
    }

    public String getBetweenText() {
        return between_text;
    }

    public String getStrongText() {
        return strong_text;
    }

    public String getDate() {
        return date;
    }

    public String getLabelDate() {
        return label_date;
    }

    public String getType() {
        return type;
    }

    protected MyItemsStatusModel(Parcel in) {
        username = in.readString();
        between_text = in.readString();
        strong_text = in.readString();
        date = in.readString();
        label_date = in.readString();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(between_text);
        dest.writeString(strong_text);
        dest.writeString(date);
        dest.writeString(label_date);
        dest.writeString(type);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MyItemsStatusModel> CREATOR = new Parcelable.Creator<MyItemsStatusModel>() {
        @Override
        public MyItemsStatusModel createFromParcel(Parcel in) {
            return new MyItemsStatusModel(in);
        }

        @Override
        public MyItemsStatusModel[] newArray(int size) {
            return new MyItemsStatusModel[size];
        }
    };
}