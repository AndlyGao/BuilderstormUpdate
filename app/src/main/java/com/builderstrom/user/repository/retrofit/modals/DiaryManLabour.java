package com.builderstrom.user.repository.retrofit.modals;

import android.os.Parcel;
import android.os.Parcelable;

public class DiaryManLabour implements Parcelable {

    private String id = "";
    private String label = "";
    private String work_hours = "";
    private String user_id = "";
    private String project_id = "";
    private String swh = "";
    private String start_time = "";
    private String end_time = "";
    private String pay_type = "";
    private String cost_code = "";
    private String diary_id = "";
    private String add_date = "";

    public DiaryManLabour() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getWorkHours() {
        return work_hours;
    }

    public void setWorkHours(String work_hours) {
        this.work_hours = work_hours;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public String getProjectId() {
        return project_id;
    }

    public void setProjectId(String project_id) {
        this.project_id = project_id;
    }

    public String getSwh() {
        return swh;
    }

    public void setSwh(String swh) {
        this.swh = swh;
    }

    public String getStartTime() {
        return start_time;
    }

    public void setStartTime(String start_time) {
        this.start_time = start_time;
    }

    public String getEndTime() {
        return end_time;
    }

    public void setEndTime(String end_time) {
        this.end_time = end_time;
    }

    public String getPayType() {
        return pay_type;
    }

    public void setPayType(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getCostCode() {
        return cost_code;
    }

    public void setCostCode(String cost_code) {
        this.cost_code = cost_code;
    }

    public String getDiaryId() {
        return diary_id;
    }

    public void setDiaryId(String diary_id) {
        this.diary_id = diary_id;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    protected DiaryManLabour(Parcel in) {
        id = in.readString();
        label = in.readString();
        work_hours = in.readString();
        user_id = in.readString();
        project_id = in.readString();
        swh = in.readString();
        start_time = in.readString();
        end_time = in.readString();
        pay_type = in.readString();
        cost_code = in.readString();
        diary_id = in.readString();
        add_date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(label);
        dest.writeString(work_hours);
        dest.writeString(user_id);
        dest.writeString(project_id);
        dest.writeString(swh);
        dest.writeString(start_time);
        dest.writeString(end_time);
        dest.writeString(pay_type);
        dest.writeString(cost_code);
        dest.writeString(diary_id);
        dest.writeString(add_date);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DiaryManLabour> CREATOR = new Parcelable.Creator<DiaryManLabour>() {
        @Override
        public DiaryManLabour createFromParcel(Parcel in) {
            return new DiaryManLabour(in);
        }

        @Override
        public DiaryManLabour[] newArray(int size) {
            return new DiaryManLabour[size];
        }
    };
}