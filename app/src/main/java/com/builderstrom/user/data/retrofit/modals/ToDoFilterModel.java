package com.builderstrom.user.data.retrofit.modals;

public class ToDoFilterModel {
    String statusId = "";
    String statusName = "";
    String categoryId = "";
    String categoryName = "";
    String userName = "";
    String userId = "";
    String startDate = "";
    String endDate = "";

    public ToDoFilterModel(String statusId, String statusName, String categoryId, String categoryName, String userName, String userId) {
        this.statusId = statusId;
        this.statusName = statusName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.userId = userId;
        this.userName = userName;
    }

    public ToDoFilterModel() {

    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
