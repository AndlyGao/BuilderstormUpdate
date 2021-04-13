package com.builderstrom.user.data.retrofit.modals;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReturnOverviewDetail implements Parcelable {

    @SerializedName("Datatype")
    @Expose
    private String datatype;

    @SerializedName("timestring")
    @Expose
    private String timestring;

    @SerializedName("totalhrswithrate")
    @Expose
    private String totalhrswithrate;

    @SerializedName("ProjectTitle")
    @Expose
    private String projectTitle;

    @SerializedName("settingTrue")
    @Expose
    private String settingTrue;

    @SerializedName("activityid")
    @Expose
    private String activityid = "0";

    @SerializedName("data_type")
    @Expose
    private String data_type;

    @SerializedName("projectId")
    @Expose
    private String projectId;

    @SerializedName("selectedTaskids")
    @Expose
    private String selectedTaskids;

    @SerializedName("StartTime")
    @Expose
    private String StartTime;

    @SerializedName("EndTime")
    @Expose
    private String EndTime;

    @SerializedName("FromLocation")
    @Expose
    private String FromLocation;

    @SerializedName("ToLocation")
    @Expose
    private String ToLocation;

    @SerializedName("Item")
    @Expose
    private String Item;

    @SerializedName("timeInMinutes")
    @Expose
    private String timeInMinutes;

    @SerializedName("Location")
    @Expose
    private String Location;

    @SerializedName("Quantity")
    @Expose
    private String Quantity;

    @SerializedName("duration")
    @Expose
    private String duration;

    private Integer totalTimeInMinutes;

    private Integer breakTimeAdded;

    private boolean isSettingAllow = false;

    private String viewTypeName;


    /* uploading Parameters  */
    private String toLocationId = "";
    private String fromLocationId = "";
    private String expensePrice = "";

    /* workTime Parameters */
    public String date = "";
    public String is_work_start_end_time = "";
    public String deleteunfinishedactid = "";

    /* Break Parameters */

    public String is_break_start_end_time = "";
    public String break_ids = "";
    public String breaks_array = "";


    /* Price Work */
    public String pricework_item_cost = "";


    /* For holiday and sickness */
    public String work_status = "";


    public ReturnOverviewDetail() {
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getFromLocation() {
        return FromLocation;
    }

    public void setFromLocation(String fromLocation) {
        FromLocation = fromLocation;
    }

    public String getToLocation() {
        return ToLocation;
    }

    public void setToLocation(String toLocation) {
        ToLocation = toLocation;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getTimestring() {
        return timestring;
    }

    public void setTimestring(String timestring) {
        this.timestring = timestring;
    }

    public String getTotalhrswithrate() {
        return totalhrswithrate;
    }

    public void setTotalhrswithrate(String totalhrswithrate) {
        this.totalhrswithrate = totalhrswithrate;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getActivityid() {
        return activityid;
    }

    public void setActivityid(String activityid) {
        this.activityid = activityid;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getSettingTrue() {
        return settingTrue;
    }

    public void setSettingTrue(String settingTrue) {
        this.settingTrue = settingTrue;
    }


    public String getSelectedTaskids() {
        return selectedTaskids;
    }

    public void setSelectedTaskids(String selectedTaskids) {
        this.selectedTaskids = selectedTaskids;
    }


    public String getTimeInMinutes() {
        return timeInMinutes;
    }

    public void setTimeInMinutes(String timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getToLocationId() {
        return toLocationId;
    }

    public void setToLocationId(String toLocationId) {
        this.toLocationId = toLocationId;
    }

    public String getFromLocationId() {
        return fromLocationId;
    }

    public void setFromLocationId(String fromLocationId) {
        this.fromLocationId = fromLocationId;
    }

    public String getExpensePrice() {
        return expensePrice;
    }

    public void setExpensePrice(String expensePrice) {
        this.expensePrice = expensePrice;
    }


    public Integer getTotalTimeInMinutes() {
        return totalTimeInMinutes;
    }

    public void setTotalTimeInMinutes(Integer totalTimeInMinutes) {
        this.totalTimeInMinutes = totalTimeInMinutes;
    }

    public Integer getBreakTimeAdded() {
        return breakTimeAdded;
    }

    public void setBreakTimeAdded(Integer breakTimeAdded) {
        this.breakTimeAdded = breakTimeAdded;
    }

    public String getViewTypeName() {
        return viewTypeName;
    }

    public void setViewTypeName(String viewTypeName) {
        this.viewTypeName = viewTypeName;
    }

    public boolean isSettingAllow() {
        return isSettingAllow;
    }

    public void setSettingAllow(boolean settingAllow) {
        isSettingAllow = settingAllow;
    }

    protected ReturnOverviewDetail(Parcel in) {
        datatype = in.readString();
        timestring = in.readString();
        totalhrswithrate = in.readString();
        projectTitle = in.readString();
        settingTrue = in.readString();
        activityid = in.readString();
        data_type = in.readString();
        projectId = in.readString();
        selectedTaskids = in.readString();
        StartTime = in.readString();
        EndTime = in.readString();
        FromLocation = in.readString();
        ToLocation = in.readString();
        Item = in.readString();
        timeInMinutes = in.readString();
        Location = in.readString();
        Quantity = in.readString();
        duration = in.readString();
        totalTimeInMinutes = in.readByte() == 0x00 ? null : in.readInt();
        breakTimeAdded = in.readByte() == 0x00 ? null : in.readInt();
        isSettingAllow = in.readByte() != 0x00;
        viewTypeName = in.readString();
        toLocationId = in.readString();
        fromLocationId = in.readString();
        expensePrice = in.readString();
        date = in.readString();
        is_work_start_end_time = in.readString();
        deleteunfinishedactid = in.readString();
        is_break_start_end_time = in.readString();
        break_ids = in.readString();
        breaks_array = in.readString();
        pricework_item_cost = in.readString();
        work_status = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(datatype);
        dest.writeString(timestring);
        dest.writeString(totalhrswithrate);
        dest.writeString(projectTitle);
        dest.writeString(settingTrue);
        dest.writeString(activityid);
        dest.writeString(data_type);
        dest.writeString(projectId);
        dest.writeString(selectedTaskids);
        dest.writeString(StartTime);
        dest.writeString(EndTime);
        dest.writeString(FromLocation);
        dest.writeString(ToLocation);
        dest.writeString(Item);
        dest.writeString(timeInMinutes);
        dest.writeString(Location);
        dest.writeString(Quantity);
        dest.writeString(duration);
        if (totalTimeInMinutes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(totalTimeInMinutes);
        }
        if (breakTimeAdded == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(breakTimeAdded);
        }
        dest.writeByte((byte) (isSettingAllow ? 0x01 : 0x00));
        dest.writeString(viewTypeName);
        dest.writeString(toLocationId);
        dest.writeString(fromLocationId);
        dest.writeString(expensePrice);
        dest.writeString(date);
        dest.writeString(is_work_start_end_time);
        dest.writeString(deleteunfinishedactid);
        dest.writeString(is_break_start_end_time);
        dest.writeString(break_ids);
        dest.writeString(breaks_array);
        dest.writeString(pricework_item_cost);
        dest.writeString(work_status);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ReturnOverviewDetail> CREATOR = new Parcelable.Creator<ReturnOverviewDetail>() {
        @Override
        public ReturnOverviewDetail createFromParcel(Parcel in) {
            return new ReturnOverviewDetail(in);
        }

        @Override
        public ReturnOverviewDetail[] newArray(int size) {
            return new ReturnOverviewDetail[size];
        }
    };
}