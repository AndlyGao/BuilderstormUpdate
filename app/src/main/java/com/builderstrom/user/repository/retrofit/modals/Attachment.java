package com.builderstrom.user.repository.retrofit.modals;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

import com.builderstrom.user.utils.CommonMethods;

@Keep
public class Attachment implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Attachment> CREATOR = new Parcelable.Creator<Attachment>() {
        @Override
        public Attachment createFromParcel(Parcel in) {
            return new Attachment(in);
        }

        @Override
        public Attachment[] newArray(int size) {
            return new Attachment[size];
        }
    };
    private String fileUrl = "";
    private String file_url = "";
    private String fileName = "";
    private String original_name = "";
    private Integer todo_image_id = null;
    private boolean isOffline = false;

    public Attachment(String fileUrl, String fileName, boolean isOffline) {
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.isOffline = isOffline;
    }

    public Attachment(String fileUrl) {
        this.fileUrl = fileUrl;
        this.fileName = CommonMethods.getFileNameFromPath(fileUrl);
        this.isOffline = false;
    }

    public Attachment(String fileUrl, String fileName) {
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.isOffline = false;
    }

    public Attachment(String fileUrl, boolean isOffline) {
        this.fileUrl = isOffline ? fileUrl : "";
        this.file_url = isOffline ? "" : fileUrl;
        this.fileName = CommonMethods.getFileNameFromPath(fileUrl);
        this.original_name = "";
        this.isOffline = isOffline;
    }

    protected Attachment(Parcel in) {
        fileUrl = in.readString();
        file_url = in.readString();
        fileName = in.readString();
        original_name = in.readString();
        todo_image_id = in.readByte() == 0x00 ? null : in.readInt();
        isOffline = in.readByte() != 0x00;
    }

    public String getFileUrl() {
        return fileUrl != null && !fileUrl.isEmpty() ? fileUrl : file_url;
    }

    public String getFileName() {
        return fileName == null || fileName.isEmpty() ? original_name : fileName;
    }

    public Integer getTodo_image_id() {
        return todo_image_id;
    }

    public void setTodo_image_id(Integer todo_image_id) {
        this.todo_image_id = todo_image_id;
    }

    public boolean isOffline() {
        return isOffline;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileUrl);
        dest.writeString(file_url);
        dest.writeString(fileName);
        dest.writeString(original_name);
        if (todo_image_id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(todo_image_id);
        }
        dest.writeByte((byte) (isOffline ? 0x01 : 0x00));
    }
}