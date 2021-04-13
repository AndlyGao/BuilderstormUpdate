package com.builderstrom.user.data.retrofit.modals;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimesheetOverview extends BaseApiModel {


    private PojoOverViewData data;

    public PojoOverViewData getData() {
        return data;
    }


    public class PojoOverViewData {

        private String topmsg = "";
        private List<TSNote> user_notes = null;
        private ReturnOverviewhtmldetail returnOverviewhtmldetail;
        private List<StandardBreaks> projectStandardBreaks = null;
        private List<ReturnOverviewDetail> returnOverviewDetail = null;

        @SerializedName("priceworklistings")
        @Expose
        private List<String> priceworklistings = null;


        public List<StandardBreaks> getProjectStandardBreaks() {
            return projectStandardBreaks;
        }

        public String getTopmsg() {
            return topmsg;
        }

        public List<String> getPriceworklistings() {
            return priceworklistings;
        }

        public List<TSNote> getUserNotes() {
            return user_notes;
        }

        public List<ReturnOverviewDetail> getOverviewDetail() {
            return returnOverviewDetail;
        }

        public ReturnOverviewhtmldetail getOverviewHtml() {
            return returnOverviewhtmldetail;
        }

    }


}
