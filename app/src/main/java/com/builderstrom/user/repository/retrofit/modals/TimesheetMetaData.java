package com.builderstrom.user.repository.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TimesheetMetaData extends BaseApiModel {


    private ReturnData data;


    public ReturnData getReturnData() {
        return data;
    }


    public class ReturnData {

        private List<MetaSetup> Timesheetmetadatasetup = null;

        public List<MetaSetup> getTimesheetMetaData() {
            return Timesheetmetadatasetup;
        }

        public void setTimesheetMetaData(List<MetaSetup> Timesheetmetadatasetup) {
            this.Timesheetmetadatasetup = Timesheetmetadatasetup;
        }
    }

    public class MetaSetup {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("metadata_name")
        @Expose
        private String metadata_name;
        @SerializedName("metadata_value")
        @Expose
        private String metadata_value;
        @SerializedName("is_visible_worktime")
        @Expose
        private String is_visible_worktime;
        @SerializedName("show_on_timesheet_view")
        @Expose
        private String show_on_timesheet_view;
        @SerializedName("show_on_company_view")
        @Expose
        private String show_on_company_view;
        @SerializedName("show_on_approval_view")
        @Expose
        private String show_on_approval_view;
        @SerializedName("show_on_approved_view")
        @Expose
        private String show_on_approved_view;
        @SerializedName("added_by")
        @Expose
        private String added_by;
        @SerializedName("added_on")
        @Expose
        private String added_on;
        @SerializedName("modified_by")
        @Expose
        private String modified_by;
        @SerializedName("modified_on")
        @Expose
        private String modified_on;
        @SerializedName("region_id")
        @Expose
        private Object region_id;
        @SerializedName("is_allow")
        @Expose
        private String is_allow;

        private boolean checked = false;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMetadata_name() {
            return metadata_name;
        }

        public void setMetadata_name(String metadata_name) {
            this.metadata_name = metadata_name;
        }

        public String getMetadata_value() {
            return metadata_value;
        }

        public void setMetadata_value(String metadata_value) {
            this.metadata_value = metadata_value;
        }

        public String getIs_visible_worktime() {
            return is_visible_worktime;
        }

        public void setIs_visible_worktime(String is_visible_worktime) {
            this.is_visible_worktime = is_visible_worktime;
        }

        public String getShow_on_timesheet_view() {
            return show_on_timesheet_view;
        }

        public void setShow_on_timesheet_view(String show_on_timesheet_view) {
            this.show_on_timesheet_view = show_on_timesheet_view;
        }

        public String getShow_on_company_view() {
            return show_on_company_view;
        }

        public void setShow_on_company_view(String show_on_company_view) {
            this.show_on_company_view = show_on_company_view;
        }

        public String getShow_on_approval_view() {
            return show_on_approval_view;
        }

        public void setShow_on_approval_view(String show_on_approval_view) {
            this.show_on_approval_view = show_on_approval_view;
        }

        public String getShow_on_approved_view() {
            return show_on_approved_view;
        }

        public void setShow_on_approved_view(String show_on_approved_view) {
            this.show_on_approved_view = show_on_approved_view;
        }

        public String getAdded_by() {
            return added_by;
        }

        public void setAdded_by(String added_by) {
            this.added_by = added_by;
        }

        public String getAdded_on() {
            return added_on;
        }

        public void setAdded_on(String added_on) {
            this.added_on = added_on;
        }

        public String getModified_by() {
            return modified_by;
        }

        public void setModified_by(String modified_by) {
            this.modified_by = modified_by;
        }

        public String getModified_on() {
            return modified_on;
        }

        public void setModified_on(String modified_on) {
            this.modified_on = modified_on;
        }

        public Object getRegion_id() {
            return region_id;
        }

        public void setRegion_id(Object region_id) {
            this.region_id = region_id;
        }

        public String getIs_allow() {
            return is_allow;
        }

        public void setIs_allow(String is_allow) {
            this.is_allow = is_allow;
        }

        @Override
        public String toString() {
            return metadata_name;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }
}
