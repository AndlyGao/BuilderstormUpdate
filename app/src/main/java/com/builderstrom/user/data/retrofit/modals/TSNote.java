package com.builderstrom.user.data.retrofit.modals;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TSNote implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id = "";

    @SerializedName("added_by")
    @Expose
    private String addedBy = "";

    @SerializedName("note_description")
    @Expose
    private String noteDescription = "";

    @SerializedName("added_on")
    @Expose
    private String addedOn = "";

    @SerializedName("date_for")
    @Expose
    private String dateFor = "";

    @SerializedName("fullname")
    @Expose
    private String fullName = "";


    public TSNote() {
    }

    public TSNote(String addedBy, String noteDescription, String addedOn, String fullName) {
        this.addedBy = addedBy;
        this.noteDescription = noteDescription;
        this.addedOn = addedOn;
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public String getDateFor() {
        return dateFor;
    }

    public void setDateFor(String dateFor) {
        this.dateFor = dateFor;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    protected TSNote(Parcel in) {
        id = in.readString();
        addedBy = in.readString();
        noteDescription = in.readString();
        addedOn = in.readString();
        dateFor = in.readString();
        fullName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(addedBy);
        dest.writeString(noteDescription);
        dest.writeString(addedOn);
        dest.writeString(dateFor);
        dest.writeString(fullName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TSNote> CREATOR = new Parcelable.Creator<TSNote>() {
        @Override
        public TSNote createFromParcel(Parcel in) {
            return new TSNote(in);
        }

        @Override
        public TSNote[] newArray(int size) {
            return new TSNote[size];
        }
    };
}