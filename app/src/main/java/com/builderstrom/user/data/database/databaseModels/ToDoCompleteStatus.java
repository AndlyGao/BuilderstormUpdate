package com.builderstrom.user.data.database.databaseModels;

public class ToDoCompleteStatus {
    public static final String TABLE_NAME = "to_do_resolved";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SITE_ID = "site_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_TODO_ID = "todo_id";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_DATE = "completed_on_date";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SITE_ID + " TEXT,"
            + COLUMN_USER_ID + " TEXT,"
            + COLUMN_TODO_ID + " TEXT,"
            + COLUMN_STATUS + " TEXT,"
            + COLUMN_DATE + " TEXT,"
            + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    private Integer id;
    private String siteId;
    private String userId;
    private String date;
    private Integer todoId;
    private Integer status;

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

    public Integer getTodoId() {
        return todoId;
    }

    public void setTodoId(Integer todoId) {
        this.todoId = todoId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
