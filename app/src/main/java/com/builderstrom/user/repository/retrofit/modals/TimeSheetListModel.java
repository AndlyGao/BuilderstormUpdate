package com.builderstrom.user.repository.retrofit.modals;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeSheetListModel extends BaseApiModel {


    private TimeSheetListDataModel data;

    public TimeSheetListDataModel getData() {
        return data;
    }

    public class TimeSheetListDataModel {

        private String username;
        private String usertotaltimetobeshown;
        private String status;
        private List<WeekDataModel> weekdata = null;


        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getTotalTime() {
            return usertotaltimetobeshown;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<WeekDataModel> getWeekdata() {
            return weekdata;
        }

        public void setWeekdata(List<WeekDataModel> weekdata) {
            this.weekdata = weekdata;
        }


    }

    public class WeekDataModel {

        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("timebutton")
        @Expose
        private String timebutton;
        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("weekdayheading")
        @Expose
        private String weekdayheading;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTimebutton() {
            return timebutton;
        }

        public void setTimebutton(String timebutton) {
            this.timebutton = timebutton;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getWeekdayheading() {
            return weekdayheading;
        }

        public void setWeekdayheading(String weekdayheading) {
            this.weekdayheading = weekdayheading;
        }

    }

}
