package com.builderstrom.user.repository.retrofit.modals;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String name = "";
    private String email = "";
    private String uid;

    public User(String id, String name) {
        this.uid = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getUserId() {
        return uid;
    }

    public void setUserId(String userid) {
        this.uid = userid;
    }

    @Override
    public String toString() {
        return name;
    }

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        uid = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(uid);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}