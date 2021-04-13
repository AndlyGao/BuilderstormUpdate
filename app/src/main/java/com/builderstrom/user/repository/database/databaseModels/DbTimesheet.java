package com.builderstrom.user.repository.database.databaseModels;

public class DbTimesheet {

    public static final String TABLE_NAME = "timesheets";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SITE_ID = "site_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_PROJECT_ID = "project_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_TIME = "total_time";
    public static final String COLUMN_TYPE = "view_type";
    public static final String COLUMN_TYPE_NAME = "view_name";
    public static final String COLUMN_TIMESTAMP = "time_stamp";

    public static final String CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SITE_ID + " TEXT,"
            + COLUMN_USER_ID + " TEXT,"
            + COLUMN_PROJECT_ID + " TEXT,"
            + COLUMN_DATE + " TEXT,"
            + COLUMN_OVERVIEW + " TEXT,"
            + COLUMN_NOTES + " TEXT,"
            + COLUMN_TIME + " INTEGER,"
            + COLUMN_TYPE + " TEXT,"
            + COLUMN_TYPE_NAME + " TEXT,"
            + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    private Integer id;
    private String siteId;
    private String userId;
    private String projectId;
    private String timeSheetDate;
    private String overView;
    private String notes;
    private Integer totalTime;
    private String type;
    private String typeName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

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

    public String getTimeSheetDate() {
        return timeSheetDate;
    }

    public void setTimeSheetDate(String timeSheetDate) {
        this.timeSheetDate = timeSheetDate;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
