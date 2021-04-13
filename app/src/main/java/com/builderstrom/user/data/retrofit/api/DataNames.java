package com.builderstrom.user.data.retrofit.api;

/**
 * Created by anil_singhania on 25/09/2018.
 */

public class DataNames {

    public static final String BASE_DEV_URL = "";
    public static final String TERM_CONDITION_URL = "https://www.builderstorm.com/termsandconditions/";
    public static final String PRIVACY_POLICY_URL = "https://www.builderstorm.com/privacy-policy/";
    public static final String DEMO_URL = "https://www.builderstorm.com";

    public static final String DEVICE_TYPE = "ANDROID";
    public static final String KEY_FLAG = "key_flag";
    public static final String PROJECT_INFO = "project_more_information";

    public static final String INTENT_ACTION_ALL_PROJECT_UPDATE = "INTENT_ACTION_ALL_PROJECT_UPDATE";
    public static final String INTENT_ACTION_UPDATE_NOTIFICATION = "INTENT_ACTION_UPDATE_NOTIFICATION";
    public static final String INTENT_ACTION_UPDATE_NETWORK = "INTENT_ACTION_UPDATE_NETWORK";
    public static final String INTENT_ACTION_SAVE_SIGN_IN_OUT = "INTENT_ACTION_SAVE_SIGN_IN_OUT";
    public static final String INTENT_SINGLE_FILE_DOWNLOAD = "INTENT_ACTION_SINGLE_FILE_DOWNLOAD";
    public static final String INTENT_UPDATE_PROJECTS_LIST = "INTENT_UPDATE_PROJECTS_LIST";
    /* Meta Data*/
    public static final String META_DIARY_SECTION = "daily_diary";
    public static final String META_DRAWING_SECTION = "project_drawing";
    public static final String META_GALLERY_SECTION = "project_photos";
    public static final String META_RFI_SECTION = "rfi";
    /* for sharing*/
    public static final int SHARE_RFI = 1;
    public static final int SHARE_SNAG = 2;
    public static final int SHARE_DOCS = 3;
    public static final int SHARE_COMPANY_DOCS = 4;
    /* Users List  Sections*/
    public static final String RFI_SECTION_USERS = "rfi";
    public static final String SNAG_SECTION_USERS = "snag";
    public static final String PROJECT_DOCS_SECTION_USERS = "documents";
    public static final String COMPANY_DOCS_SECTION_USERS = "cloud_storage";
    public static final String TO_DO_SECTION_USERS = "todos";
    public static final String DIGI_DOCS_USERS = "digital_document_users";

    /* CAtegories List  Sections*/
    public static final String DIGITAL_CATEGORIES = "digital_categories";
    public static final String COMPANY_CATEGORIES = "company_categories";
    public static final String DOCUMENT_CATEGORIES = "document_categories";
    public static final String TODO_CATEGORIES = "todo_categories";

    /* job scheduler service */
    public static final int SYNC_SERVICE_ID = 1201;
    public static final int SINGLE_DOWNLOAD_SERVICE_ID = 1202;
    public static final int SYNC_SLOW_CONNECTION_SERVICE_ID = 1203;
    public static final int SYNC_ALL_RFI_SERVICE_ID = 1204;
    public static final int SYNC_ALL_SNAG_SERVICE_ID = 1205;
    public static final int SYNC_ALL_PROJECT_DOCS_SERVICE_ID = 1206;
    public static final int SYNC_ALL_COMPANY_DOCS_SERVICE_ID = 1207;
    public static final int SYNC_ALL_TO_DO_SERVICE_ID = 1208;

    /* Time Sheet Actions*/
    public static final int TIME_SHEET_OVERVIEW = 0;
    public static final int TIME_SHEET_WORK_TIME = 1;
    public static final int TIME_SHEET_TRAVEL = 2;
    public static final int TIME_SHEET_BREAKS = 3;
    public static final int TIME_SHEET_EXPENSES = 4;
    public static final int TIME_SHEET_PRICE_WORK = 5;
    public static final int TIME_SHEET_HOLIDAY = 6;
    public static final int TIME_SHEET_SICK = 7;
    public static final int TIME_SHEET_NOTES = 8;
    public static final int TIME_SHEET_OTHER = 9;
    public static final int TYPE_ERROR_API = 1;
    public static final int TYPE_ERROR_FAILURE = 2;
    public static final int TYPE_ERROR_EXCEPTION = 3;
    public static final int TYPE_SYNCED_SUCCESS = 4;
    public static final String BUTTON_DANGER = "0";
    public static final String BUTTON_SUCCESS = "1";
    public static final String BUTTON_HOLIDAY = "2";
    public static final String BUTTON_WARNING = "3";
    public static final String BUTTON_PRIMARY = "4";

    /* API TimeSheet Actions*/
    public static final String ADD_ACTION_WORK_TIME = "0";
    public static final String ADD_ACTION_TRAVEL = "1";
    public static final String ADD_ACTION_BREAKS = "2";
    public static final String ADD_ACTION_EXPENSES = "3";
    public static final String ADD_ACTION_PRICE_WORK = "4";
    public static final String ADD_ACTION_METADATA = "5";
    public static final String ADD_ACTION_STANDARD_BREAKS = "6";
    public static final String ADD_ACTION_HOLIDAY = "1";
    public static final String ADD_ACTION_SICKNESS = "2";

    /* Company Actions*/
    public static final int ACTION_DOC_DELETE = 1;
    public static final int ACTION_DOC_FAVORITE = 2;
    public static final int ACTION_DOC_TRACK = 3;
    public static final int ACTION_DOC_COMMENT = 4;
    public static final int ACTION_DOC_DEFAULT = 5;

    /* API Project document action */
    public static final String DIALOG_FLAG = "dialog_flag";
    public static final String TODO_NORMAL = "normal";
    public static final String TODO_WARNING = "warning";
    public static final String TODO_OVERDUE = "overdue";
    public static final String ACTION_FAVOURITE = "favourite";
    public static final String ACTION_TRACK = "track";
    public static final String ACTION_QUARANTINE = "quarantine";
    public static final String ACTION_ARCHIVE = "archive";
    public static final String WORK = "Worktime";
    public static final String BREAKS = "Breaktime";
    public static final String EXPENSE = "expense";
    public static final String TRAVEL = "Travel";
    public static final String PRICE = "Pricework";
    public static final String HOLIDAY = "Holiday";
    public static final String SICK = "Sick";
    public static final String NOTES = "Notes";

    /* Retrofit Links */
    static final String WORKSPACE_URL = "/mobileapp/domain/{domain}";
    static final String CREDENTIAL_LOGIN_URL = "/mobileapp/login/{domain}";
    static final String PIN_LOGIN_URL = "/mobileapp/loginpin";
    static final String API_LOGOUT_URL = "/mobileapp/logout";
    static final String FORGOT_PIN_URL = "/mobileapp/forgetpin";
    static final String FORGOT_PASSWORD_URL = "/mobileapp/forgetpassword";
    static final String API_CHANGE_PASSWORD_URL = "/mobileapp/changepassword";
    static final String API_CREATE_PIN_URL = "/mobileapp/setpin";
    static final String API_CHANGE_PIN_URL = "/mobileapp/updatepin";
    static final String API_LOGIN_STATUS = "/mobileapp/loggedinstatesync";
    static final String DRAWING_ADD_COMMENT_URL = "/mobileapp/addcommentdrawing/{drawingid}";
    static final String DRAWING_GET_COMMENTS_URL = "/mobileapp/getdrawingcomment/{drawingid}";
    static final String ALL_USERS_URL = "/mobileapp/getallusers";
    static final String DRAWING_LIST_URL = "/mobileapp/listingDrawings";
    static final String ADD_DRAWING_URL = "/mobileapp/addDrawing/{pid}";
    static final String DRAWING_ACTION_URL = "/mobileapp/drawingAction/{pid}";
    static final String RFI_LIST_URL = "/mobileapp/getrfilist";
    static final String ADD_RFI_URL = "/mobileapp/addrfi";
    static final String RFI_GET_COMMENTS_URL = "/mobileapp/getrficomments";
    static final String RFI_ADD_COMMENTS_URL = "/mobileapp/addrficomments";
    static final String RFI_ADD_ANSWER_URL = "/mobileapp/rfianswer/{rfiid}";
    static final String RFI_SHARE_URL = "/mobileapp/sharerfi/{rfiid}";
    static final String DIARY_LIST_URL = "/mobileapp/getdailydiarylist/{pid}/{date}";
    static final String DELETE_DIARY_URL = "/mobileapp/deletedailydiary/{pid}/{diaryid}";
    static final String ADD_DIARY_URL = "/mobileapp/addupdatedailydiary/{pid}";
    static final String CUSTOM_FIELDS_URL = "/mobileapp/getcustomfields/{pid}/{section_name}";
    static final String DAIRY_PREVIOUS_MAN_HOURS = "/mobileapp/getdiarymanhours/{pid}";
    /* Site Access */
    static final String PROJECT_LOGIN_URL = "mobileapp/getdatetime";
    static final String PROJECT_SET_SIGN_URL = "mobileapp/setsignin";
    static final String PROJECT_CHECK_SIGN_URL = "mobileapp/checksignin";
    static final String PROJECT_SYNC_URL = "mobileapp/siteaccessoff";
    /* Project Photos Section */
    static final String API_GET_ALL_GALLERIES = "/mobileapp/getprojectgallery";
    static final String API_ADD_GALLERY = "/mobileapp/addgallery";
    static final String API_DELETE_GALLERY = "/mobileapp/deletegallery";
    static final String API_GALLERY_IMAGE_COMMENTS = "/mobileapp/getimagecomment";
    static final String API_ADD_GALLERY_IMAGE_COMMENT = "/mobileapp/addimagecomment";
    static final String API_DELETE_GALLERY_IMAGE = "/mobileapp/deletegalleryimage";
    /* Snag section */
    static final String API_LIST_SNAG_URL = "/mobileapp/getsnaglist";
    static final String API_ADD_SNAG_URL = "/mobileapp/addsnag/{pid}";
    static final String API_GET_SNAG_COMMENTS = "/mobileapp/getsnagcomments";
    static final String API_ADD_SNAG_COMMENT = "/mobileapp/addsnagcomment";
    static final String API_UPDATE_SNAG_MARK_STATUS = "/mobileapp/updatemark_as";
    static final String API_SHARE_SNAG_URL = "/mobileapp/sharesnag/{snagid}";
    /* Digital Documents */
    static final String API_ALL_DIGITAL_DOCS_URL = "/mobileapp/getdigitaldoc";
    static final String API_ALL_DIGITAL_MY_ITEMS_URL = "mobileapp/myitems";
    static final String API_ISSUE_DIGITAL_DOCUMENT_URL = "mobileapp/issuedocument";
    static final String API_DIGITAL_DOCS_CATEGORIES_URL = "/mobileapp/ddoccategories";
    static final String API_DIGITAL_ITEMS_USERS_URL = "mobileapp/getfilterusers";
    static final String API_GET_DIGITAL_DOCUMENT_URL = "/mobileapp/ddoctemplate/{templateid}";
    static final String API_COMPLETE_DIGITAL_DOCUMENT_URL = "mobileapp/ddoccomplete/{templateid}";
    static final String API_COMPLETE_MY_ITEM_URL = "mobileapp/ddoccompletemyitems/{templateid}";
    /* TimeSheet Section*/
    static final String API_GET_TIME_SHEET_URL = "mobileapp/getusertimesheetdata/{date}";
    static final String API_GET_TIME_SHEET_TASKS_URL = "mobileapp/timesheettasks/{pid}";
    static final String API_GET_TIME_SHEET_USER_ACTIVITIES_URL = "mobileapp/getuseractivity";
    static final String API_GET_TIME_SHEET_PRICE_WORK_URL = "mobileapp/getPriceworkListing";
    static final String API_GET_TIME_SHEET_META_DATA_URL = "mobileapp/getTimesheetMetadata";
    static final String API_ADD_EDIT_TIME_SHEET_NOTE_URL = "mobileapp/saveTimeSheetNotes";
    static final String API_DELETE_TIME_SHEET_NOTE_URL = "mobileapp/deleteTimeSheetNotes";
    static final String API_ADD_TIME_SHEET_WORK_TIME_URL = "mobileapp/addWorktime";
    static final String API_ADD_TIME_SHEET_TRAVEL_TIME_URL = "mobileapp/addtraveltime";
    static final String API_ADD_TIME_SHEET_EXPENSES_URL = "mobileapp/addExpenses";
    static final String API_ADD_TIME_SHEET_META_DATA_URL = "mobileapp/addMetadata";
    static final String API_ADD_TIME_SHEET_BREAK_TIME_URL = "mobileapp/isbreakstartendtime";
    static final String API_ADD_TIME_SHEET_STANDARD_BREAK_URL = "mobileapp/addstandardbreaks";
    static final String API_ADD_TIME_SHEET_PRICE_WORK_URL = "mobileapp/addPricework";
    static final String API_ADD_TIME_SHEET_HOLIDAY_URL = "mobileapp/addHoliday";
    static final String API_DELETE_TIME_SHEET_ACTIVITY_URL = "mobileapp/deleteActivity";
    static final String API_SYNC_TIME_SHEET_URL = "mobileapp/timesheetoffline";
    /*  Project Document */
    static final String PROJECT_DOCS_LIST_URL = "/mobileapp/listingPDoc/{pid}";
    static final String PRO_DOCS_CAT_URL = "/mobileapp/pcategories/{pid}";
    static final String PRO_GET_COMMENTS_URL = "/mobileapp/getdocumentcomments/{documentid}";
    static final String PRO_ADD_COMMENTS_URL = "/mobileapp/addcommentdocument/{documentid}";
    static final String PRO_DOCS_SHARE_URL = "/mobileapp/shareProjectDocument";
    static final String PROJECT_ADD_DOCS_URL = "/mobileapp/addPDco/{pid}";
    static final String PROJECT_EDIT_DOCS_URL = "/mobileapp/editPDco/{pid}";
    static final String PROJECT_DOCS_ACTION_URL = "/mobileapp/getProjectDocumentActions";
    static final String API_DELETE_PROJECT_DOCS_URL = "/mobileapp/deleteProjectDoc";
    static final String DOCUMENT_STATUS_URL = "/mobileapp/pdocumentstatuslist";
    /* Company Documents*/
    static final String API_GET_COMPANY_DOCS_LIST_URL = "mobileapp/cdocumentlisting";
    static final String API_COMPANY_DOCS_CATEGORIES_URL = "mobileapp/ccategories";
    static final String API_COMPANY_DOCS_ADD_URL = "mobileapp/addcdocument";
    static final String API_EDIT_COMPANY_DOCS_URL = "mobileapp/editcdocument";
    static final String COMPANY_DOCS_ADD_COMMENT_URL = "mobileapp/addcommentcdocument";
    static final String COMPANY_DOCS_GET_ALL_COMMENT_URL = "mobileapp/listingcdocumentcomment/{docid}";
    static final String API_DELETE_COMPANY_DOCS_URL = "mobileapp/deletecdocument";
    static final String COMPANY_DOCS_TRACK_URL = "mobileapp/trackcdocument";
    static final String COMPANY_DOCS_FAVORITE_URL = "mobileapp/favouritecdocument";
    static final String COMPANY_DOCS_STATUS_URL = "mobileapp/cdocumentstatus";
    static final String API_SHARE_COMPANY_DOC_URL = "mobileapp/sharecdocument/{docid}";
    /* To Do List */
    static final String API_GET_TO_DO_LIST_URL = "mobileapp/gettodolisting";
    static final String API_ADD_TO_DO_URL = "/mobileapp/addtodo";
    static final String API_EDIT_TO_DO_URL = "/mobileapp/updatetodo";
    static final String API_MARK_TO_DO_URL = "/mobileapp/markcomplete";
    static final String API_GET_TO_DO_CATEGORIES_URL = "mobileapp/gettodocategories";
    static final String API_GET_TO_DO_COMMENTS_URL = "mobileapp/gettodocomments";
    static final String API_ADD_TO_DO_COMMENTS_URL = "mobileapp/addtodocomment";
    /* App support */
    static final String SUPPORT_URL = "mobileapp/sendreport";
    /* User Notification */
    static final String NOTIFICATION_URL = "mobileapp/getUserNotifications";
    static final String NOTIFICATION_READ_URL = "mobileapp/readNotification";
    /* Recent Documents/MyItems */
    static final String RECENT_DOCUMENTS_URL = "mobileapp/getdigitalcategoriesshortcuts";
    /* Site Projects*/
    static final String SITE_PROJECT = "/mobileapp/getProjectsListing";
    static final String SITE_PROJECT_NOTES = "/mobileapp/getProjectNotesListing";
    static final String USER_ASSIGNED_PROJECTS = "mobileapp/getUserAssignedProjects";


    public static final String WH_MON_ST = "P_MON_START_TIME";
    public static final String WH_MON_ED = "P_MON_END_TIME";
    public static final String WH_TUE_ST = "P_TUE_START_TIME";
    public static final String WH_TUE_ED = "P_TUE_END_TIME";
    public static final String WH_WED_ST = "P_WED_START_TIME";
    public static final String WH_WED_ED = "P_WED_END_TIME";
    public static final String WH_THU_ST = "P_THU_START_TIME";
    public static final String WH_THU_ED = "P_THU_END_TIME";
    public static final String WH_FRI_ST = "P_FRI_START_TIME";
    public static final String WH_FRI_ED = "P_FRI_END_TIME";
    public static final String WH_SAT_ST = "P_SAT_START_TIME";
    public static final String WH_SAT_ED = "P_SAT_END_TIME";
    public static final String WH_SUN_ST = "P_SUN_START_TIME";
    public static final String WH_SUN_ED = "P_SUN_END_TIME";


}
