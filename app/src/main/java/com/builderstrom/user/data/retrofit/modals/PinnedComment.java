package com.builderstrom.user.data.retrofit.modals;

import android.os.Parcel;
import android.os.Parcelable;

public class PinnedComment implements Parcelable {

    private String id;
    private String comment;
    private String user_id;
    private String pinned_for;
    private String doc_id;
    private String user_type;
    private String create_date;

    public PinnedComment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPinned_for() {
        return pinned_for;
    }

    public void setPinned_for(String pinned_for) {
        this.pinned_for = pinned_for;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }


    protected PinnedComment(Parcel in) {
        id = in.readString();
        comment = in.readString();
        user_id = in.readString();
        pinned_for = in.readString();
        doc_id = in.readString();
        user_type = in.readString();
        create_date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(comment);
        dest.writeString(user_id);
        dest.writeString(pinned_for);
        dest.writeString(doc_id);
        dest.writeString(user_type);
        dest.writeString(create_date);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PinnedComment> CREATOR = new Parcelable.Creator<PinnedComment>() {
        @Override
        public PinnedComment createFromParcel(Parcel in) {
            return new PinnedComment(in);
        }

        @Override
        public PinnedComment[] newArray(int size) {
            return new PinnedComment[size];
        }
    };
}