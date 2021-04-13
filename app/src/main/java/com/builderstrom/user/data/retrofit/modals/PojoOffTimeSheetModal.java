package com.builderstrom.user.data.retrofit.modals;

public class PojoOffTimeSheetModal {
    private String day = "";
    private String workHours = "";
    private String timeButton = "";
    private String viewTypeName = "";
    private boolean isShowPriceWork = false;


    public PojoOffTimeSheetModal(String day, String workHours) {
        this.day = day;
        this.workHours = workHours;
    }

    public PojoOffTimeSheetModal(String day, String workHours, boolean isShowPriceWork) {
        this.day = day;
        this.workHours = workHours;
        this.isShowPriceWork = isShowPriceWork;
    }

    public PojoOffTimeSheetModal(String day, String workHours, boolean isShowPriceWork, String timeButton, String viewTypeName) {
        this.day = day;
        this.workHours = workHours;
        this.isShowPriceWork = isShowPriceWork;
        this.timeButton = timeButton;
        this.viewTypeName = viewTypeName;
    }


    public PojoOffTimeSheetModal(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWorkHours() {
        return workHours;
    }

    public void setWorkHours(String workHours) {
        this.workHours = workHours;
    }

    public boolean isShowPriceWork() {
        return isShowPriceWork;
    }

    public void setShowPriceWork(boolean showPriceWork) {
        isShowPriceWork = showPriceWork;
    }

    public String getTimeButton() {
        return timeButton;
    }

    public void setTimeButton(String timeButton) {
        this.timeButton = timeButton;
    }

    public String getViewTypeName() {
        return viewTypeName;
    }

    public void setViewTypeName(String viewTypeName) {
        this.viewTypeName = viewTypeName;
    }
}
