package com.builderstrom.user.repository.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReturnOverviewhtmldetail {

    @SerializedName("is_processed")
    @Expose
    private Boolean isProcessed;
    @SerializedName("restrictedpermisison")
    @Expose
    private Boolean restrictedpermisison;
    @SerializedName("allow_expenses_paid")
    @Expose
    private Boolean allowExpensesPaid;
    @SerializedName("allow_pricework_paid")
    @Expose
    private Boolean allowPriceworkPaid;
    @SerializedName("hourly_time")
    @Expose
    private Boolean hourlyTime;
    @SerializedName("userPayType")
    @Expose
    private Boolean userPayType;
    @SerializedName("view_type")
    @Expose
    private Boolean viewType;
    @SerializedName("allow_track_tasks")
    @Expose
    private Boolean allowTrackTasks;
    @SerializedName("allow_standard_breaks")
    @Expose
    private Boolean allowStandardBreaks;
    @SerializedName("break_require_times")
    @Expose
    private Boolean breakRequireTimes;
    @SerializedName("break_automaticaly_worktime")
    @Expose
    private Boolean breakAutomaticalyWorktime;
    @SerializedName("deleteunfinishedactid")
    @Expose
    private Integer deleteunfinishedactid;
    @SerializedName("allow_breaks_paid")
    @Expose
    private Boolean allowBreaksPaid;
    @SerializedName("paid_break_details")
    @Expose
    private PaidBreakDetails paidBreakDetails;
    @SerializedName("deduct_break")
    @Expose
    private Boolean deductBreak;
    @SerializedName("deduct_break_details")
    @Expose
    private DeductBreakDetails deductBreakDetails;
    @SerializedName("checkifAbsent")
    @Expose
    private List<Object> checkifAbsent = null;

    private Integer moveToBreak = 1;


    public ReturnOverviewhtmldetail(Boolean isProcessed, Boolean restrictedpermisison, Boolean allowExpensesPaid, Boolean allowPriceworkPaid, Boolean hourlyTime, Boolean userPayType, Boolean viewType, Boolean allowTrackTasks, Boolean allowStandardBreaks, Boolean breakRequireTimes, Boolean breakAutomaticalyWorktime, Integer deleteunfinishedactid, List<Object> checkifAbsent, String minimumWorkTime, String breaKPaidTime) {
        this.isProcessed = isProcessed;
        this.restrictedpermisison = restrictedpermisison;
        this.allowExpensesPaid = allowExpensesPaid;
        this.allowPriceworkPaid = allowPriceworkPaid;
        this.hourlyTime = hourlyTime;
        this.userPayType = userPayType;
        this.viewType = viewType;
        this.allowTrackTasks = allowTrackTasks;
        this.allowStandardBreaks = allowStandardBreaks;
        this.breakRequireTimes = breakRequireTimes;
        this.breakAutomaticalyWorktime = breakAutomaticalyWorktime;
        this.deleteunfinishedactid = deleteunfinishedactid;
        this.checkifAbsent = checkifAbsent;
       /* this.minimumWorkTime = minimumWorkTime;
        this.breaKPaidTime = breaKPaidTime;*/
    }

    public Boolean getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(Boolean isProcessed) {
        this.isProcessed = isProcessed;
    }

    public Boolean getRestrictedpermisison() {
        return restrictedpermisison;
    }

    public void setRestrictedpermisison(Boolean restrictedpermisison) {
        this.restrictedpermisison = restrictedpermisison;
    }

    public Boolean getAllowExpensesPaid() {
        return allowExpensesPaid;
    }

    public void setAllowExpensesPaid(Boolean allowExpensesPaid) {
        this.allowExpensesPaid = allowExpensesPaid;
    }

    public Boolean getAllowPriceworkPaid() {
        return allowPriceworkPaid;
    }

    public void setAllowPriceworkPaid(Boolean allowPriceworkPaid) {
        this.allowPriceworkPaid = allowPriceworkPaid;
    }

    public Boolean getHourlyTime() {
        return hourlyTime;
    }

    public void setHourlyTime(Boolean hourlyTime) {
        this.hourlyTime = hourlyTime;
    }

    public Boolean getUserPayType() {
        return userPayType;
    }

    public void setUserPayType(Boolean userPayType) {
        this.userPayType = userPayType;
    }

    public Boolean getViewType() {
        return viewType;
    }

    public void setViewType(Boolean viewType) {
        this.viewType = viewType;
    }

    public Boolean getAllowTrackTasks() {
        return allowTrackTasks;
    }

    public void setAllowTrackTasks(Boolean allowTrackTasks) {
        this.allowTrackTasks = allowTrackTasks;
    }

    public Boolean getAllowStandardBreaks() {
        return allowStandardBreaks;
    }

    public void setAllowStandardBreaks(Boolean allowStandardBreaks) {
        this.allowStandardBreaks = allowStandardBreaks;
    }

    public Boolean getBreakRequireTimes() {
        return breakRequireTimes;
    }

    public void setBreakRequireTimes(Boolean breakRequireTimes) {
        this.breakRequireTimes = breakRequireTimes;
    }

    public Boolean getBreakAutomaticalyWorktime() {
        return breakAutomaticalyWorktime;
    }

    public void setBreakAutomaticalyWorktime(Boolean breakAutomaticalyWorktime) {
        this.breakAutomaticalyWorktime = breakAutomaticalyWorktime;
    }

    public Integer getDeleteunfinishedactid() {
        return deleteunfinishedactid;
    }

    public void setDeleteunfinishedactid(Integer deleteunfinishedactid) {
        this.deleteunfinishedactid = deleteunfinishedactid;
    }

    public Boolean getAllowBreaksPaid() {
        return allowBreaksPaid;
    }

    public void setAllowBreaksPaid(Boolean allowBreaksPaid) {
        this.allowBreaksPaid = allowBreaksPaid;
    }

    public PaidBreakDetails getPaidBreakDetails() {
        return paidBreakDetails;
    }

    public void setPaidBreakDetails(PaidBreakDetails paidBreakDetails) {
        this.paidBreakDetails = paidBreakDetails;
    }

    public Boolean getDeductBreak() {
        return deductBreak;
    }

    public void setDeductBreak(Boolean deductBreak) {
        this.deductBreak = deductBreak;
    }

    public DeductBreakDetails getDeductBreakDetails() {
        return deductBreakDetails;
    }

    public void setDeductBreakDetails(DeductBreakDetails deductBreakDetails) {
        this.deductBreakDetails = deductBreakDetails;
    }

    public Integer getMoveToBreak() {
        return moveToBreak;
    }

        public List<Object> getCheckifAbsent() {
        return checkifAbsent;
    }

    public void setCheckifAbsent(List<Object> checkifAbsent) {
        this.checkifAbsent = checkifAbsent;
    }


    public class PaidBreakDetails {

        @SerializedName("break_must_work")
        @Expose
        private Boolean breakMustWork;
        @SerializedName("break_hours_to_be_worked")
        @Expose
        private Integer breakHoursToBeWorked;
        @SerializedName("break_hours_paid")
        @Expose
        private Boolean breakHoursPaid;
        @SerializedName("break_minutes_paid")
        @Expose
        private Integer breakMinutesPaid;

        public Boolean getBreakMustWork() {
            return breakMustWork;
        }

        public void setBreakMustWork(Boolean breakMustWork) {
            this.breakMustWork = breakMustWork;
        }

        public Integer getBreakHoursToBeWorked() {
            return breakHoursToBeWorked;
        }

        public void setBreakHoursToBeWorked(Integer breakHoursToBeWorked) {
            this.breakHoursToBeWorked = breakHoursToBeWorked;
        }

        public Boolean getBreakHoursPaid() {
            return breakHoursPaid;
        }

        public void setBreakHoursPaid(Boolean breakHoursPaid) {
            this.breakHoursPaid = breakHoursPaid;
        }

        public Integer getBreakMinutesPaid() {
            return breakMinutesPaid;
        }

        public void setBreakMinutesPaid(Integer breakMinutesPaid) {
            this.breakMinutesPaid = breakMinutesPaid;
        }

    }

    public class DeductBreakDetails {

        @SerializedName("deduct_break_minutes")
        @Expose
        private Integer deductBreakMinutes;
        @SerializedName("break_user_must_work")
        @Expose
        private Boolean breakUserMustWork;
        @SerializedName("break_apply_after_hours_worked")
        @Expose
        private Integer breakApplyAfterHoursWorked;

        public Integer getDeductBreakMinutes() {
            return deductBreakMinutes;
        }

        public void setDeductBreakMinutes(Integer deductBreakMinutes) {
            this.deductBreakMinutes = deductBreakMinutes;
        }

        public Boolean getBreakUserMustWork() {
            return breakUserMustWork;
        }

        public void setBreakUserMustWork(Boolean breakUserMustWork) {
            this.breakUserMustWork = breakUserMustWork;
        }

        public Integer getBreakApplyAfterHoursWorked() {
            return breakApplyAfterHoursWorked;
        }

        public void setBreakApplyAfterHoursWorked(Integer breakApplyAfterHoursWorked) {
            this.breakApplyAfterHoursWorked = breakApplyAfterHoursWorked;
        }

    }


}


//public class ReturnOverviewhtmldetail {
//
//    @SerializedName("is_processed")
//    @Expose
//    private Boolean isProcessed;
//    @SerializedName("restrictedpermisison")
//    @Expose
//    private Boolean restrictedpermisison;
//    @SerializedName("allow_expenses_paid")
//    @Expose
//    private Boolean allowExpensesPaid;
//    @SerializedName("allow_pricework_paid")
//    @Expose
//    private Boolean allowPriceworkPaid;
//    @SerializedName("hourly_time")
//    @Expose
//    private Boolean hourlyTime;
//    @SerializedName("userPayType")
//    @Expose
//    private Boolean userPayType;
//    @SerializedName("view_type")
//    @Expose
//    private Boolean viewType;
//    @SerializedName("allow_track_tasks")
//    @Expose
//    private Boolean allowTrackTasks;
//    @SerializedName("allow_standard_breaks")
//    @Expose
//    private Boolean allowStandardBreaks;
//    @SerializedName("break_require_times")
//    @Expose
//    private Boolean breakRequireTimes;
//    @SerializedName("break_automaticaly_worktime")
//    @Expose
//    private Boolean breakAutomaticalyWorktime;
//    @SerializedName("starthh")
//    @Expose
//    private String starthh;
//    @SerializedName("minimum_work")
//    @Expose
//    private String minimumWorkTime;
//    @SerializedName("break_paid")
//    @Expose
//    private String breaKPaidTime;
//    @SerializedName("startmm")
//    @Expose
//    private String startmm;
//    @SerializedName("deleteunfinishedactid")
//    @Expose
//    private Integer deleteunfinishedactid;
//    @SerializedName("checkifAbsent")
//    @Expose
//    private List<Object> checkifAbsent = null;
//
//    private Integer moveToBreak = 1;
//
//
//    public ReturnOverviewhtmldetail(Boolean isProcessed, Boolean restrictedpermisison, Boolean allowExpensesPaid, Boolean allowPriceworkPaid, Boolean hourlyTime, Boolean userPayType, Boolean viewType, Boolean allowTrackTasks, Boolean allowStandardBreaks, Boolean breakRequireTimes, Boolean breakAutomaticalyWorktime, Integer deleteunfinishedactid, List<Object> checkifAbsent, String minimumWorkTime, String breaKPaidTime) {
//        this.isProcessed = isProcessed;
//        this.restrictedpermisison = restrictedpermisison;
//        this.allowExpensesPaid = allowExpensesPaid;
//        this.allowPriceworkPaid = allowPriceworkPaid;
//        this.hourlyTime = hourlyTime;
//        this.userPayType = userPayType;
//        this.viewType = viewType;
//        this.allowTrackTasks = allowTrackTasks;
//        this.allowStandardBreaks = allowStandardBreaks;
//        this.breakRequireTimes = breakRequireTimes;
//        this.breakAutomaticalyWorktime = breakAutomaticalyWorktime;
//        this.deleteunfinishedactid = deleteunfinishedactid;
//        this.checkifAbsent = checkifAbsent;
//        this.minimumWorkTime = minimumWorkTime;
//        this.breaKPaidTime = breaKPaidTime;
//    }
//
//    public Boolean getIsProcessed() {
//        return isProcessed;
//    }
//
//    public void setIsProcessed(Boolean isProcessed) {
//        this.isProcessed = isProcessed;
//    }
//
//    public Boolean getRestrictedpermisison() {
//        return restrictedpermisison;
//    }
//
//    public void setRestrictedpermisison(Boolean restrictedpermisison) {
//        this.restrictedpermisison = restrictedpermisison;
//    }
//
//    public Boolean getAllowExpensesPaid() {
//        return allowExpensesPaid;
//    }
//
//    public void setAllowExpensesPaid(Boolean allowExpensesPaid) {
//        this.allowExpensesPaid = allowExpensesPaid;
//    }
//
//    public Boolean getAllowPriceworkPaid() {
//        return allowPriceworkPaid;
//    }
//
//    public void setAllowPriceworkPaid(Boolean allowPriceworkPaid) {
//        this.allowPriceworkPaid = allowPriceworkPaid;
//    }
//
//    public Boolean getHourlyTime() {
//        return hourlyTime;
//    }
//
//    public void setHourlyTime(Boolean hourlyTime) {
//        this.hourlyTime = hourlyTime;
//    }
//
//    public Boolean getUserPayType() {
//        return userPayType;
//    }
//
//    public void setUserPayType(Boolean userPayType) {
//        this.userPayType = userPayType;
//    }
//
//    public Boolean getViewType() {
//        return viewType;
//    }
//
//    public void setViewType(Boolean viewType) {
//        this.viewType = viewType;
//    }
//
//    public Boolean getAllowTrackTasks() {
//        return allowTrackTasks;
//    }
//
//    public void setAllowTrackTasks(Boolean allowTrackTasks) {
//        this.allowTrackTasks = allowTrackTasks;
//    }
//
//    public Boolean getAllowStandardBreaks() {
//        return allowStandardBreaks;
//    }
//
//    public void setAllowStandardBreaks(Boolean allowStandardBreaks) {
//        this.allowStandardBreaks = allowStandardBreaks;
//    }
//
//    public Boolean getBreakRequireTimes() {
//        return breakRequireTimes;
//    }
//
//    public void setBreakRequireTimes(Boolean breakRequireTimes) {
//        this.breakRequireTimes = breakRequireTimes;
//    }
//
//    public Boolean getBreakAutomaticalyWorktime() {
//        return breakAutomaticalyWorktime;
//    }
//
//    public void setBreakAutomaticalyWorktime(Boolean breakAutomaticalyWorktime) {
//        this.breakAutomaticalyWorktime = breakAutomaticalyWorktime;
//    }
//
//    public String getStarthh() {
//        return starthh;
//    }
//
//    public void setStarthh(String starthh) {
//        this.starthh = starthh;
//    }
//
//    public String getStartmm() {
//        return startmm;
//    }
//
//    public void setStartmm(String startmm) {
//        this.startmm = startmm;
//    }
//
//    public Integer getDeleteunfinishedactid() {
//        return deleteunfinishedactid;
//    }
//
//    public void setDeleteunfinishedactid(Integer deleteunfinishedactid) {
//        this.deleteunfinishedactid = deleteunfinishedactid;
//    }
//
//    public List<Object> getCheckifAbsent() {
//        return checkifAbsent;
//    }
//
//    public void setCheckifAbsent(List<Object> checkifAbsent) {
//        this.checkifAbsent = checkifAbsent;
//    }
//
//
//    public Integer getMoveToBreak() {
//        return moveToBreak;
//    }
//
//    public String getMinimumWork() {
//        return minimumWorkTime;
//    }
//
//    public void setMinimumWork(String minimumWorkTime) {
//        this.minimumWorkTime = minimumWorkTime;
//    }
//
//    public String getBreaKPaidTime() {
//        return breaKPaidTime;
//    }
//
//    public void setBreaKPaidTime(String breaKPaidTime) {
//        this.breaKPaidTime = breaKPaidTime;
//    }
//}
