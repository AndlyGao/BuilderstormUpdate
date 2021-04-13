package com.builderstrom.user.data.database.databaseModels;

public class DbSnag {
    public static final String TABLE_NAME = "Snag";
    public static final String COLUMN_ROW_ID = "row_id";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SITE_ID = "site_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_PROJECT_ID = "project_id";
    public static final String COLUMN_USER_NAME = "username";
    public static final String COLUMN_TO_USERS = "to_users";
    public static final String COLUMN_CC_USERS = "cc_users";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_PACK_NO = "package_no";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DUE_DATE = "due_date";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_MARK_STATUS = "mark_status";
    public static final String COLUMN_ITEM_DATA = "item_data";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public static final String CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SITE_ID + " TEXT,"
            + COLUMN_USER_ID + " TEXT,"
            + COLUMN_ROW_ID + " TEXT,"
            + COLUMN_PROJECT_ID + " TEXT,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_TO_USERS + " TEXT,"
            + COLUMN_CC_USERS + " TEXT,"
            + COLUMN_LOCATION + " TEXT,"
            + COLUMN_PACK_NO + " TEXT,"
            + COLUMN_DESCRIPTION + " TEXT,"
            + COLUMN_DUE_DATE + " TEXT,"
            + COLUMN_ITEM_DATA + " TEXT,"
            + COLUMN_STATUS + " TEXT,"
            + COLUMN_MARK_STATUS + " TEXT,"
            + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    private Integer id;
    private String markStatus;
    private String siteId = "";
    private String userId = "";
    private String projectId = "";
    private String userName = "";
    private String toUsers = "";
    private String ccUsers = "";
    private String location = "";
    private String dueDate = "";
    private String packageNo = "";
    private String description = "";
    private String rowId = "";
    private String status = "";
    private String timeStamp = "";
    private String itemData = "";

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToUsers() {
        return toUsers;
    }

    public void setToUsers(String toUsers) {
        this.toUsers = toUsers;
    }

    public String getCcUsers() {
        return ccUsers;
    }

    public void setCcUsers(String ccUsers) {
        this.ccUsers = ccUsers;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getPackageNo() {
        return packageNo;
    }

    public void setPackageNo(String packageNo) {
        this.packageNo = packageNo;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getItemData() {
        return itemData;
    }

    public void setItemData(String itemData) {
        this.itemData = itemData;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getMarkStatus() {
        return markStatus;
    }

    public void setMarkStatus(String markStatus) {
        this.markStatus = markStatus;
    }

    @Override
    public String toString() {
        return rowId;
    }
}
