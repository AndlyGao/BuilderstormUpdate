package com.builderstrom.user.data.retrofit.modals;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskListing implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("task_title")
    @Expose
    private String taskTitle;
    @SerializedName("project_id")
    @Expose
    private String projectId;
    @SerializedName("added_by")
    @Expose
    private String addedBy;
    @SerializedName("orderno")
    @Expose
    private Object orderno;
    @SerializedName("date_added")
    @Expose
    private String dateAdded;
    @SerializedName("isSelected")
    @Expose
    private boolean isSelected = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public Object getOrderno() {
        return orderno;
    }

    public void setOrderno(Object orderno) {
        this.orderno = orderno;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return taskTitle;
    }


    protected TaskListing(Parcel in) {
        id = in.readString();
        taskTitle = in.readString();
        projectId = in.readString();
        addedBy = in.readString();
        orderno = (Object) in.readValue(Object.class.getClassLoader());
        dateAdded = in.readString();
        isSelected = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(taskTitle);
        dest.writeString(projectId);
        dest.writeString(addedBy);
        dest.writeValue(orderno);
        dest.writeString(dateAdded);
        dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TaskListing> CREATOR = new Parcelable.Creator<TaskListing>() {
        @Override
        public TaskListing createFromParcel(Parcel in) {
            return new TaskListing(in);
        }

        @Override
        public TaskListing[] newArray(int size) {
            return new TaskListing[size];
        }
    };
}