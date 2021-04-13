package com.builderstrom.user.repository.database.databaseModels;

public class DbCompanyDocuments {

    public static final String TABLE_NAME = "company_documents";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SITE_ID = "site_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_ROW_ID = "row_id";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_DATA_ITEM = "project_data";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public static final String CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SITE_ID + " TEXT,"
            + COLUMN_CATEGORY_ID + " TEXT,"
            + COLUMN_ROW_ID + " TEXT,"
            + COLUMN_USER_ID + " TEXT,"
            + COLUMN_DATA_ITEM + " TEXT,"
            + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    private Integer id;
    private String rowId;
    private String siteId;
    private String userId;
    private String categoryId;
    private String companyData;
    private String timeStamp;

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

    public String getCompanyData() {
        return companyData;
    }

    public void setCompanyData(String companyData) {
        this.companyData = companyData;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }
}
