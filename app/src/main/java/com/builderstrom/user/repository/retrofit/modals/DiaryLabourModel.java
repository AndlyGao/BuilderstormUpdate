package com.builderstrom.user.repository.retrofit.modals;

public class DiaryLabourModel {
    private String labourType;
    private String payType;
    private String numberOfSites;
    private String startTime;
    private String finishTime;
    private String totalHours;
    private String costCode;
    private String id;
    private String swh;


    /*String labourType, String payType, String noSite, String startTime, String finishTime, String totalHours, String costCode*/
    public DiaryLabourModel() {
        this.id = null;
        this.labourType = null;
        this.payType = null;
        this.numberOfSites = null;
        this.startTime = null;
        this.finishTime = null;
        this.totalHours = null;
        this.costCode = null;
        this.swh = null;

    }

    public String getLabourType() {
        return labourType;
    }

    public void setLabourType(String labourType) {
        this.labourType = labourType;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getNoSite() {
        return numberOfSites;
    }

    public void setNoSite(String noSite) {
        this.numberOfSites = noSite;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(String totalHours) {
        this.totalHours = totalHours;
    }

    public String getCostCode() {
        return costCode;
    }

    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSwh() {
        return swh;
    }

    public void setSwh(String swh) {
        this.swh = swh;
    }
}
