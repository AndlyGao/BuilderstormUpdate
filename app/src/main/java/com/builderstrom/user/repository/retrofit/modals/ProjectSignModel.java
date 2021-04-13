package com.builderstrom.user.repository.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProjectSignModel extends BaseApiModel {

    private Details data;

    public Details getDetails() {
        return data;
    }

    public void setDetails(Details details) {
        this.data = details;
    }


    public class Details {
        private String date = "";
        private String time = "";
        private String myLat = "";
        private String myLong = "";
        private ProjectDetails projectdetails;
        private String project_title = "";
        private Integer allow_overtime = 0;
        private Integer allow_standard_breaks = 0;
        private List<StandardBreaks> standard_breaks = null;


        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public String getMyLat() {
            return myLat;
        }

        public String getMyLong() {
            return myLong;
        }

        public ProjectDetails getProjectDetails() {
            return projectdetails;
        }

        public String getProjectTitle() {
            return project_title;
        }

        public Integer getAllowOvertime() {
            return allow_overtime;
        }

        public Integer getAllowStandardBreaks() {
            return allow_standard_breaks;
        }

        public List<StandardBreaks> getStandardBreaks() {
            return standard_breaks;
        }

    }
}
