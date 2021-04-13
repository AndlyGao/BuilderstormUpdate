package com.builderstrom.user.data.database.databaseModels;

public class DbDigitalDocs {
    public static final String TABLE_NAME = "digital_documents";
    public static final String COLUMN_ROW_ID = "row_id";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SITE_ID = "site_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_ASSIGNED_USER = "assigned_user";
    public static final String COLUMN_PROJECT_ID = "project_id";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_TEMPLATE_ID = "template_id";
    public static final String COLUMN_CUSTOM_DOCUMENT_ID = "custom_document_id";
    public static final String COLUMN_PROJECT_DOCUMENT_ID = "project_document_id";
    public static final String COLUMN_CAT_SECTION = "cat_section";
    public static final String COLUMN_COMPANY_CAT_ID = "company_project_cat_id";
    public static final String COLUMN_TEMPLATE_NAME = "template_name";
    public static final String COLUMN_DOCUMENT = "document_data";
    public static final String COLUMN_COMPLETE_DATE = "complete_date";
    public static final String COLUMN_RECURRENCE_TYPE = "recurrence_type";
    public static final String COLUMN_DOCUMENT_SAVED_DATA = "saved_document_data";
    public static final String COLUMN_TIMESTAMP = "time_stamp";
    public static final String COLUMN_DOC_TYPE = "document_type"; /* is document(0) or my item(1)*/
    /* My Items Params*/
    public static final String COLUMN_ISSUED_BY = "issued_by";
    public static final String COLUMN_MY_ITEM = "my_idem_data";

    public static final String CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SITE_ID + " TEXT,"
            + COLUMN_USER_ID + " TEXT,"
            + COLUMN_ASSIGNED_USER + " TEXT,"
            + COLUMN_PROJECT_ID + " TEXT,"
            + COLUMN_ROW_ID + " TEXT,"
            + COLUMN_CATEGORY_ID + " TEXT,"
            + COLUMN_TEMPLATE_ID + " TEXT,"
            + COLUMN_CUSTOM_DOCUMENT_ID + " TEXT,"
            + COLUMN_PROJECT_DOCUMENT_ID + " TEXT,"
            + COLUMN_CAT_SECTION + " TEXT,"
            + COLUMN_COMPANY_CAT_ID + " TEXT,"
            + COLUMN_TEMPLATE_NAME + " TEXT,"
            + COLUMN_DOCUMENT + " TEXT,"
            + COLUMN_DOCUMENT_SAVED_DATA + " TEXT,"
            + COLUMN_DOC_TYPE + " INTEGER,"
            + COLUMN_ISSUED_BY + " TEXT,"
            + COLUMN_RECURRENCE_TYPE + " TEXT,"
            + COLUMN_COMPLETE_DATE + " TEXT,"
            + COLUMN_MY_ITEM + " TEXT,"
            + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";

    private Integer id;
    private String rowId;
    private Integer docType;
    private String siteId = "";
    private String userId = "";
    private String assigned_user = "";
    private String templateId = "";
    private String catSection = "";
    private String companyProjectCatId = "";
    private String customDocumentId = "";
    private String projectDocumentId = "";
    private String templateName = "";
    private String projectId = "";
    private String document = "";
    private String savedData = "";
    private String categoryId = "";
    private String timeStamp = "";
    private String issuedBy = "";
    private String myItem = "";
    private String recurrenceType = "";
    private String completeDate = "";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
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

    public String getAssigned_user() {
        return assigned_user;
    }

    public void setAssigned_user(String assigned_user) {
        this.assigned_user = assigned_user;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getSavedData() {
        return savedData;
    }

    public void setSavedData(String savedData) {
        this.savedData = savedData;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getCatSection() {
        return catSection;
    }

    public void setCatSection(String catSection) {
        this.catSection = catSection;
    }

    public String getCompanyProjectCatId() {
        return companyProjectCatId;
    }

    public void setCompanyProjectCatId(String companyProjectCatId) {
        this.companyProjectCatId = companyProjectCatId;
    }

    public String getCustomDocumentId() {
        return customDocumentId;
    }

    public void setCustomDocumentId(String customDocumentId) {
        this.customDocumentId = customDocumentId;
    }

    public String getProjectDocumentId() {
        return projectDocumentId;
    }

    public void setProjectDocumentId(String projectDocumentId) {
        this.projectDocumentId = projectDocumentId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getDocType() {
        return docType;
    }

    public void setDocType(Integer docType) {
        this.docType = docType;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getMyItem() {
        return myItem;
    }

    public void setMyItem(String myItem) {
        this.myItem = myItem;
    }

    public String getRecurrenceType() {
        return recurrenceType;
    }

    public void setRecurrenceType(String recurrenceType) {
        this.recurrenceType = recurrenceType;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }
}
