package com.builderstrom.user.data.retrofit.api;

import com.builderstrom.user.data.retrofit.modals.AddDiaryModel;
import com.builderstrom.user.data.retrofit.modals.AddDrawingModel;
import com.builderstrom.user.data.retrofit.modals.AddMoreComment;
import com.builderstrom.user.data.retrofit.modals.AddRFIModel;
import com.builderstrom.user.data.retrofit.modals.CategoryModel;
import com.builderstrom.user.data.retrofit.modals.DiaryAPIModel;
import com.builderstrom.user.data.retrofit.modals.DiaryMetaDataModel;
import com.builderstrom.user.data.retrofit.modals.DigitalDocumentModel;
import com.builderstrom.user.data.retrofit.modals.DigitalFormModel;
import com.builderstrom.user.data.retrofit.modals.DoumentStatusModel;
import com.builderstrom.user.data.retrofit.modals.DrawingCommentModel;
import com.builderstrom.user.data.retrofit.modals.DrawingModel;
import com.builderstrom.user.data.retrofit.modals.GalleryModel;
import com.builderstrom.user.data.retrofit.modals.LoginModel;
import com.builderstrom.user.data.retrofit.modals.MoreMenuModel;
import com.builderstrom.user.data.retrofit.modals.NotificationDataModel;
import com.builderstrom.user.data.retrofit.modals.PDCommentModel;
import com.builderstrom.user.data.retrofit.modals.PojoAddGallery;
import com.builderstrom.user.data.retrofit.modals.PojoCommon;
import com.builderstrom.user.data.retrofit.modals.PojoCompanyCommentModel;
import com.builderstrom.user.data.retrofit.modals.PojoCompanyDocument;
import com.builderstrom.user.data.retrofit.modals.PojoCompanyStatus;
import com.builderstrom.user.data.retrofit.modals.PojoGalleryComment;
import com.builderstrom.user.data.retrofit.modals.PojoManHourModel;
import com.builderstrom.user.data.retrofit.modals.PojoMyItemRespModel;
import com.builderstrom.user.data.retrofit.modals.PojoNewSuccess;
import com.builderstrom.user.data.retrofit.modals.PojoPriceWorkModel;
import com.builderstrom.user.data.retrofit.modals.PojoRfiComments;
import com.builderstrom.user.data.retrofit.modals.PojoSnagCommentModel;
import com.builderstrom.user.data.retrofit.modals.PojoSnagListModel;
import com.builderstrom.user.data.retrofit.modals.PojoSuccessCommon;
import com.builderstrom.user.data.retrofit.modals.PojoTSHoliday;
import com.builderstrom.user.data.retrofit.modals.PojoTimeSheetTasks;
import com.builderstrom.user.data.retrofit.modals.PojoToDoListResp;
import com.builderstrom.user.data.retrofit.modals.ProjectDocumentModel;
import com.builderstrom.user.data.retrofit.modals.ProjectListModel;
import com.builderstrom.user.data.retrofit.modals.ProjectSignModel;
import com.builderstrom.user.data.retrofit.modals.ProjectsUpdateModel;
import com.builderstrom.user.data.retrofit.modals.RFIDataModel;
import com.builderstrom.user.data.retrofit.modals.RecentCategoryModel;
import com.builderstrom.user.data.retrofit.modals.SiteAccessCheckModel;
import com.builderstrom.user.data.retrofit.modals.TimeSheetListModel;
import com.builderstrom.user.data.retrofit.modals.TimesheetMetaData;
import com.builderstrom.user.data.retrofit.modals.TimesheetOverview;
import com.builderstrom.user.data.retrofit.modals.ToDoCommentsModel;
import com.builderstrom.user.data.retrofit.modals.UserModel;
import com.builderstrom.user.data.retrofit.modals.WorkspaceModel;
import com.builderstrom.user.data.retrofit.modals.PMNotesModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiWebInterface {

    /**
     * Domain authentication
     *
     * @param domain   domain
     * @param deviceId deviceId
     */
    @GET(DataNames.WORKSPACE_URL)
    Call<WorkspaceModel> workspace(@Path(value = "domain") String domain,
                                   @Query("deviceno") String deviceId);

    /**
     * Login with Credential
     *
     * @param userName      userName
     * @param password      Password
     * @param deviceId      DeviceId
     * @param deviceType    DeviceType Android/IOS
     * @param fireBaseToken fireBaseToken
     */
    @FormUrlEncoded
    @POST(DataNames.CREDENTIAL_LOGIN_URL)
    Call<LoginModel> credentialLogin(@Path(value = "domain") String domain,
                                     @Field("username") String userName,
                                     @Field("password") String password,
                                     @Field("deviceid") String deviceId,
                                     @Field("devicetype") String deviceType,
                                     @Field("fbToken") String fireBaseToken);

    /**
     * Login with Pin
     *
     * @param pin           pin
     * @param deviceId      DeviceId
     * @param deviceType    DeviceType Android/IOS
     * @param fireBaseToken fireBaseToken
     */
    @FormUrlEncoded
    @POST(DataNames.PIN_LOGIN_URL)
    Call<LoginModel> pinLogin(@Field("pin") String pin,
                              @Field("deviceid") String deviceId,
                              @Field("devicetype") String deviceType,
                              @Field("fbToken") String fireBaseToken);

    /**
     * Pin upgrade/update
     *
     * @param mobilePin  pin
     * @param deviceId   DeviceId
     * @param deviceType DeviceType Android/IOS
     */
    @FormUrlEncoded
    @POST(DataNames.API_CREATE_PIN_URL)
    Call<PojoSuccessCommon> apiCreatePin(@Field("mobilepin") String mobilePin,
                                         @Field("deviceid") String deviceId,
                                         @Field("devicetype") String deviceType);

    @FormUrlEncoded
    @POST(DataNames.API_CHANGE_PIN_URL)
    Call<PojoSuccessCommon> apiPinChange(@Field("oldmobilepin") String oldPin,
                                         @Field("newmobilepin") String newPin,
                                         @Field("deviceid") String deviceId,
                                         @Field("devicetype") String deviceType);

    @FormUrlEncoded
    @POST(DataNames.API_CHANGE_PASSWORD_URL)
    Call<PojoSuccessCommon> apiChangePassword(@Field("oldpassword") String oldPassword,
                                              @Field("newpassword") String newPassword);

    /**
     * Forgot password API
     *
     * @param email      User email
     * @param deviceType deviceType
     * @param deviceId   deviceId
     */
    @FormUrlEncoded
    @POST(DataNames.FORGOT_PASSWORD_URL)
    Call<PojoSuccessCommon> apiForgotPassword(@Field("email") String email,
                                              @Field("devicetype") String deviceType,
                                              @Field("deviceid") String deviceId);

    @FormUrlEncoded
    @POST(DataNames.FORGOT_PIN_URL)
    Call<PojoSuccessCommon> apiForgotPin(@Field("email") String email,
                                         @Field("devicetype") String deviceType,
                                         @Field("deviceid") String deviceId);

    @GET(DataNames.API_LOGOUT_URL)
    Call<PojoSuccessCommon> apiLogout();

    /**
     * Sync Login Status API
     *
     * @param loginStatus   loginStatus
     * @param fireBaseToken fire_base Token
     */
    @FormUrlEncoded
    @POST(DataNames.API_LOGIN_STATUS)
    Call<PojoSuccessCommon> synLoginStatus(@Field("status") String loginStatus,
                                           @Field("fbToken") String fireBaseToken);

    /**
     * Get all drawings
     *
     * @param projectId projectId
     * @param offset    offset
     * @param limit     limit
     */
    @GET(DataNames.DRAWING_LIST_URL)
    Call<DrawingModel> getDrawing(@Query("pid") String projectId,
                                  @Query("offset") Integer offset,
                                  @Query("limit") Integer limit);

    /**
     * Get all drawings comments
     *
     * @param drawingId drawingid
     */
    @GET(DataNames.DRAWING_GET_COMMENTS_URL)
    Call<DrawingCommentModel> getDrawingComments(@Path(value = "drawingid") String drawingId);

    /**
     * More menu drawing
     *
     * @param projectId     projectId
     * @param drawingId     drawingId
     * @param drawingAction drawingAction
     * @param status        status
     */
    @FormUrlEncoded
    @POST(DataNames.DRAWING_ACTION_URL)
    Call<MoreMenuModel> drawingAction(@Path(value = "pid") String projectId,
                                      @Field("drawingid") String drawingId,
                                      @Field("drawingaction") String drawingAction,
                                      @Field("status") String status
    );

    /**
     * Add comment in drawing
     *
     * @param drawingId    drawingId
     * @param commentTitle commentTitle
     */
    @FormUrlEncoded
    @PUT(DataNames.DRAWING_ADD_COMMENT_URL)
    Call<MoreMenuModel> addDrawingComment(@Path(value = "drawingid") String drawingId,
                                          @Field("commenttitle") String commentTitle);

    /**
     * Add drawings
     *
     * @param drawingName drawingName
     * @param pid         projectId
     * @param drawingNo   drawingNo
     * @param revision    revision
     * @param filePdf     filePdf
     * @param fileDwg     fileDwg
     * @param fileOther   fileOther
     * @param metaData    metaData
     * @param metaFiles   metaFiles
     */
    @Multipart
    @POST(DataNames.ADD_DRAWING_URL)
    Call<AddDrawingModel> addDrawingAPI(@Path(value = "pid") String pid,
                                        @Part("drawingname") RequestBody drawingName,
                                        @Part("drawingno") RequestBody drawingNo,
                                        @Part("revision") RequestBody revision,
                                        @Part MultipartBody.Part filePdf,
                                        @Part MultipartBody.Part fileDwg,
                                        @Part MultipartBody.Part fileOther,
                                        @Part("metadata") RequestBody metaData,
                                        @Part List<MultipartBody.Part> metaFiles);

    /* *****************  Site Access ******************** */

    /**
     * Site Access sign-in
     *
     * @param status 0 for login 1 for logout
     * @param date   DF2 Format
     * @param time   DF9 format
     */
    @FormUrlEncoded
    @POST(DataNames.PROJECT_LOGIN_URL)
    Call<ProjectSignModel> projectSignIn(@Field("status") Integer status,
                                         @Field("lat") String latitude,
                                         @Field("lng") String longitude,
                                         @Field("date") String date,
                                         @Field(value = "time", encoded = true) String time);

    @FormUrlEncoded
    @POST(DataNames.PROJECT_SET_SIGN_URL)
    Call<SiteAccessCheckModel> projectSignConfirm(@Field("pid") String projectId,
                                                  @Field("status") Integer status,
                                                  @Field("lat") String latitude,
                                                  @Field("lng") String longitude,
                                                  @Field("date") String date,
                                                  @Field("time") String time,
                                                  @Field("overtime") String overtime,
                                                  @Field("standard_break_paid") String standard_break_paid,
                                                  @Field("standard_break") String standard_break);

    @FormUrlEncoded
    @POST(DataNames.PROJECT_SYNC_URL)
    Call<PojoNewSuccess> projectSync(@Field("pid") String pid,
                                     @Field("date") String date,
                                     @Field("stime") String startTime,
                                     @Field("slat") String startLatitude,
                                     @Field("slng") String startLongitude,
                                     @Field("date_out") String date_out,
                                     @Field("etime") String endTime,
                                     @Field("elat") String endLatitude,
                                     @Field("elng") String endLongitude,
                                     @Field("overtime") String overtime,
                                     @Field("standard_break_paid") String standardBreakPaid,
                                     @Field("standard_break") String standardBreaks);

    @FormUrlEncoded
    @POST(DataNames.PROJECT_CHECK_SIGN_URL)
    Call<SiteAccessCheckModel> projectSignCheck(@Field("date") String date);

    /* *****************************  Project Photos ***************************  */

    @Multipart
    @POST(DataNames.API_ADD_GALLERY)
    Call<PojoAddGallery> apiAddGallery(@Part("pid") RequestBody pid,
                                       @Part("gallery_id") RequestBody gallery_id,
                                       @Part("title") RequestBody title,
                                       @Part("lat") RequestBody lat,
                                       @Part("lng") RequestBody lng,
                                       @Part("metadata") RequestBody metaData,
                                       @Part List<MultipartBody.Part> files);

    @GET(DataNames.API_GET_ALL_GALLERIES)
    Call<GalleryModel> apiGetAllProjectPhotos(@Query("pid") String projectId,
                                              @Query("offset") Integer offset,
                                              @Query("limit") Integer limit);

    @FormUrlEncoded
    @POST(DataNames.API_DELETE_GALLERY)
    Call<PojoNewSuccess> deleteGallery(@Field("gallery_id") String galleryId);

    @FormUrlEncoded
    @POST(DataNames.API_DELETE_GALLERY_IMAGE)
    Call<PojoNewSuccess> deleteGalleryImage(@Field("image_id") String imageId);

    @GET(DataNames.API_GALLERY_IMAGE_COMMENTS)
    Call<PojoGalleryComment> apiGetImageAllComments(@Query("image_id") String imageId);

    @FormUrlEncoded
    @POST(DataNames.API_ADD_GALLERY_IMAGE_COMMENT)
    Call<PojoNewSuccess> apiAddImageComment(@Field("image_id") String image_id,
                                            @Field("comments") String comments);

    /* **************************  Daily Diary  *****************************/

    /**
     * Get daily diary list
     *
     * @param projectId projectId
     * @param date      date
     * @param limit     limit
     * @param offset    offset
     */
    @GET(DataNames.DIARY_LIST_URL)
    Call<DiaryAPIModel> getDiaryAPI(@Path(value = "pid") String projectId,
                                    @Path(value = "date") String date,
                                    @Query("offset") Integer offset,
                                    @Query("limit") Integer limit);

    /**
     * Get custom fields of daily diary
     *
     * @param projectId   projectId
     * @param sectionName sectionName
     */
    @GET(DataNames.CUSTOM_FIELDS_URL)
    Call<DiaryMetaDataModel> getDiaryAPIMetaData(@Path(value = "pid") String projectId,
                                                 @Path(value = "section_name") String sectionName);

    /**
     * Add a daily diary
     *
     * @param projectId      projectId
     * @param diaryId        diaryId
     * @param title          title
     * @param description    description
     * @param date           date
     * @param tags           tags
     * @param projectManHour projectManHour
     * @param metaData       metaData
     * @param submittedTime  submittedTime
     * @param siteLabour     siteLabour
     * @param files          files
     */
    @Multipart
    @POST(DataNames.ADD_DIARY_URL)
    Call<AddDiaryModel> addDiaryAPI(@Path(value = "pid") String projectId,
                                    @Part("is_offline") RequestBody is_offline,
                                    @Part("diary_id") RequestBody diaryId,
                                    @Part("title") RequestBody title,
                                    @Part("description") RequestBody description,
                                    @Part("date") RequestBody date,
                                    @Part("tags") RequestBody tags,
                                    @Part("projectmanhours") RequestBody projectManHour,
                                    @Part("metadata") RequestBody metaData,
                                    @Part("submittedtime") RequestBody submittedTime,
                                    @Part("siteLabour") RequestBody siteLabour,
                                    @Part List<MultipartBody.Part> files);

    /**
     * Delete a particular daily diary item
     *
     * @param projectId projectId
     * @param diaryId   diaryId
     */
    @FormUrlEncoded
    @POST(DataNames.DELETE_DIARY_URL)
    Call<AddDiaryModel> deleteDiaryAPI(@Path(value = "pid") String projectId,
                                       @Path(value = "diaryid") String diaryId,
                                       @Field("date") String Date);

    /* get Previous man hours in the project*/
    @GET(DataNames.DAIRY_PREVIOUS_MAN_HOURS)
    Call<PojoManHourModel> getManHoursEntries(@Path(value = "pid") String projectId,
                                              @Query("diaryid") Integer diaryId);



    /* ***********************    RFI Section    *********************/

    /**
     * Add RFI
     *
     * @param title         title
     * @param projectId     projectId
     * @param description   description
     * @param dueDate       dueDate
     * @param submittedTime submittedTime
     * @param toUsers       toUsers
     * @param ccUsers       ccUsers
     * @param metaData      metaData
     * @param files         files
     */
    @Multipart
    @POST(DataNames.ADD_RFI_URL)
    Call<AddRFIModel> apiAddRfi(@Part("title") RequestBody title,
                                @Part("pid") RequestBody projectId,
                                @Part("description") RequestBody description,
                                @Part("duedate") RequestBody dueDate,
                                @Part("submittedtime") RequestBody submittedTime,
                                @Part("tousers") RequestBody toUsers,
                                @Part("ccusers") RequestBody ccUsers,
                                @Part("metadata") RequestBody metaData,
                                @Part List<MultipartBody.Part> files);

    /**
     * Get all RFI's
     *
     * @param projectId projectId
     * @param date      date
     * @param offset    offset
     * @param limit     limit
     */
    @GET(DataNames.RFI_LIST_URL)
    Call<RFIDataModel> getRfiList(@Query("pid") String projectId,
                                  @Query("date") String date,
                                  @Query("offset") Integer offset,
                                  @Query("limit") Integer limit);

    /**
     * Get Rfi Comments
     *
     * @param rfiId rfiId
     */
    @GET(DataNames.RFI_GET_COMMENTS_URL)
    Call<PojoRfiComments> apiRfiAllComments(@Query("rfiid") String rfiId);

    /**
     * Add Rfi Comment
     *
     * @param rfiId        rfiId
     * @param commentTitle commentTitle
     * @param filesPart    filesPart
     */
    @Multipart
    @POST(DataNames.RFI_ADD_COMMENTS_URL)
    Call<PojoNewSuccess> apiAddRFIComment(@Part("rfiid") RequestBody rfiId,
                                          @Part("commenttitle") RequestBody commentTitle,
                                          @Part List<MultipartBody.Part> filesPart);

    /**
     * Answer an RFI
     *
     * @param rfiId        rfiId
     * @param commentTitle commentTitle
     * @param filesPart    filesPart
     */
    @Multipart
    @POST(DataNames.RFI_ADD_ANSWER_URL)
    Call<PojoNewSuccess> apiAddRFIAnswer(@Path(value = "rfiid") String rfiId,
                                         @Part("answertitle") RequestBody commentTitle,
                                         @Part List<MultipartBody.Part> filesPart);

    /**
     * Share an RFI
     *
     * @param rfiId     rfiId
     * @param projectId projectId
     * @param toUsers   toUsers
     * @param ccUsers   ccUsers
     */
    @FormUrlEncoded
    @POST(DataNames.RFI_SHARE_URL)
    Call<PojoCommon> apiShareRFI(@Path(value = "rfiid") String rfiId,
                                 @Field("pid") String projectId,
                                 @Field("tousers") String toUsers,
                                 @Field("ccusers") String ccUsers);

    /* Add rfi answer */

    /**
     * Get all users list
     *
     * @param projectId projectId
     * @param regionId  regionId
     * @param section   section
     */
    @GET(DataNames.ALL_USERS_URL)
    Call<UserModel> apiAllUsers(@Query("pid") String projectId,
                                @Query("regionid") String regionId,
                                @Query("section") String section);

    /* **************************** Snag List  ************* */

    @Multipart
    @POST(DataNames.API_ADD_SNAG_URL)
    Call<AddMoreComment> apiAddSnag(@Path(value = "pid") String pId,
                                    @Part("title") RequestBody title,
                                    @Part("location") RequestBody location,
                                    @Part("package_no") RequestBody packageNo,
                                    @Part("tousers") RequestBody toUsers,
                                    @Part("ccusers") RequestBody ccUsers,
                                    @Part("duedate") RequestBody dueDate,/* YYYY-MM-DD*/
                                    @Part("submittedtime") RequestBody submittedTime,/* YYYY-MM-DD HH:MM:ss */
                                    @Part List<MultipartBody.Part> filesPart);

    @GET(DataNames.API_LIST_SNAG_URL)
    Call<PojoSnagListModel> apiGetAllSnags(@Query("pid") String projectId,
                                           @Query("offset") Integer offset,
                                           @Query("limit") Integer limit);

    @GET(DataNames.API_GET_SNAG_COMMENTS)
    Call<PojoSnagCommentModel> apiGetSnagComments(@Query("snagid") String snagId);

    @FormUrlEncoded
    @POST(DataNames.API_ADD_SNAG_COMMENT)
    Call<AddMoreComment> apiAddSnagComment(@Field("snagid") String snagId,
                                           @Field("commenttitle") String commentTitle);

    @FormUrlEncoded
    @POST(DataNames.API_UPDATE_SNAG_MARK_STATUS)
    Call<PojoNewSuccess> apiUpdateSnagMark(@Field("snagid") String snagId,
                                           @Field("markposition") Integer markPosition);

    /**
     * Share an Snag
     *
     * @param snagId    snagId
     * @param projectId projectId
     * @param toUsers   toUsers
     * @param ccUsers   ccUsers
     */
    @FormUrlEncoded
    @POST(DataNames.API_SHARE_SNAG_URL)
    Call<PojoCommon> apiShareSnag(@Path(value = "snagid") String snagId,
                                  @Field("pid") String projectId,
                                  @Field("tousers") String toUsers,
                                  @Field("ccusers") String ccUsers);

    /* ******************* Digital Documents *********************** */

    @GET(DataNames.API_DIGITAL_DOCS_CATEGORIES_URL)
    Call<CategoryModel> apiDigitalDocCategories();

    @GET(DataNames.API_DIGITAL_ITEMS_USERS_URL)
    Call<UserModel> apiDigitalItemUsers();

    @GET(DataNames.API_GET_DIGITAL_DOCUMENT_URL)
    Call<DigitalFormModel> apiGetDigitalDocForm(@Path("templateid") String templateId,
                                                @Query("pageno") Integer pageNo,
                                                @Query("docid") Integer pDocId,
                                                @Query("issue_id") String issue_id,
                                                @Query("pid") String project_Id
    );

    @GET(DataNames.API_ALL_DIGITAL_DOCS_URL)
    Call<DigitalDocumentModel> apiGetAllDigitalDocuments(@Query("categoryid") String categoryId);

    @Multipart
    @POST(DataNames.API_COMPLETE_DIGITAL_DOCUMENT_URL)
    Call<PojoNewSuccess> addDigitalDoc(@Path(value = "templateid") String templateId,
                                       @Part("pid") RequestBody projectId,
                                       @Part("assigned_user") RequestBody assignedUser,
                                       @Part("documentid") RequestBody customDocumentId,
                                       @Part("edit_document_id") RequestBody projectDocumentId,
                                       @Part("document") RequestBody metaData,
                                       @Part("pageno") RequestBody pageNumber,
                                       @Part List<MultipartBody.Part> files);

    @Multipart
    @POST(DataNames.API_COMPLETE_MY_ITEM_URL)
    Call<PojoNewSuccess> addDigitalMyItem(@Path(value = "templateid") String templateId,
                                          @Part("pid") RequestBody projectId,
                                          @Part("assigned_user") RequestBody assignedUser,
                                          @Part("document") RequestBody metaData,
                                          @Part("pageno") RequestBody pageNumber,
                                          @Part("documentid") RequestBody docId,
                                          @Part("issue") RequestBody issue,
                                          @Part("recurrence") RequestBody recurrence,
                                          @Part("complete_for") RequestBody completeFor,/*('d/m/Y')*/
                                          @Part List<MultipartBody.Part> files);

    @GET(DataNames.API_ALL_DIGITAL_MY_ITEMS_URL)
    Call<PojoMyItemRespModel> apiGetDigiMyItems(@Query("doc_title") String docTitle,
                                                @Query("category") String category,
                                                @Query("issue_by") String issueBy,
                                                @Query("project_id") String projectId,
                                                @Query("category_section") String categorySection, /* Added on 25/6/2020 regarding recent document*/
                                                @Query("company_project_cat_id") String companyProjectCatId, /* Added on 25/6/2020 regarding recent document*/
                                                @Query("offset") Integer offset,
                                                @Query("limit") Integer limit);

    @FormUrlEncoded
    @POST(DataNames.API_ISSUE_DIGITAL_DOCUMENT_URL)
    Call<PojoCommon> apiIssueDocument(@Field("project_id") String projectId,
                                      @Field("to_users") String toUsers,
                                      @Field("doc_id") String docId,
                                      @Field("issue_id") String issueId,
                                      @Field("reference_id") String referenceId,
                                      @Field("complete_for") String completeFor,
                                      @Field("is_current_completed") Integer is_current_completed);

    /* **************************  Time Sheets  *************************** */

    @GET(DataNames.API_GET_TIME_SHEET_URL)
    Call<TimeSheetListModel> apiTimeSheet(@Path(value = "date") String date);

    @GET(DataNames.API_GET_TIME_SHEET_USER_ACTIVITIES_URL)
    Call<TimesheetOverview> apiGetUserActivities(@Query("project_id") String project_id,
                                                 @Query("date") String date);

    @GET(DataNames.API_GET_TIME_SHEET_TASKS_URL)
    Call<PojoTimeSheetTasks> getWorkTimeTasks(@Path("pid") String projectId);

    @GET(DataNames.API_GET_TIME_SHEET_PRICE_WORK_URL)
    Call<PojoPriceWorkModel> getProjectPriceWork(@Query("project_id") String projectId);

    @FormUrlEncoded
    @POST(DataNames.API_ADD_TIME_SHEET_WORK_TIME_URL)
    Call<PojoNewSuccess> apiAddWorkTime(@Field("activity_id") String activityId,
                                        @Field("data_type") String data_type,
                                        @Field("project_id") String projectId,
                                        @Field("date") String date,
                                        @Field("is_work_start_end_time") String is_work_start_end_time,
                                        @Field("work_start_time") String work_start_time,
                                        @Field("work_end_time") String work_end_time,
                                        @Field("work_time") String work_time,
                                        @Field("task_ids") String task_ids,
                                        @Field("deleteunfinishedactid") String deleteUnfinishedActId);

    @GET(DataNames.API_GET_TIME_SHEET_META_DATA_URL)
    Call<TimesheetMetaData> getSheetMetaData(@Query("project_id") String projectId);

    @FormUrlEncoded
    @POST(DataNames.API_ADD_TIME_SHEET_STANDARD_BREAK_URL)
    Call<PojoNewSuccess> apiTimeSheetStandardBreaks(@Field("activity_id") String activityId,
                                                    @Field("project_id") String projectId,
                                                    @Field("project_title") String project_title,
                                                    @Field("date") String date,
                                                    @Field("data_type") String dataType,
                                                    @Field("break_ids") String breakIds,
                                                    @Field("breaks_array") String breaks);

    @FormUrlEncoded
    @POST(DataNames.API_ADD_TIME_SHEET_BREAK_TIME_URL)
    Call<PojoNewSuccess> apiTimeSheetBreaks(@Field("activity_id") String activityId,
                                            @Field("project_id") String projectId,
                                            @Field("project_title") String project_title,
                                            @Field("date") String date,
                                            @Field("data_type") String dataType,
                                            @Field("is_break_start_end_time") Integer breakTimeType,
                                            @Field("timeInMinutes") String break_time,
                                            @Field("StartTime") String break_start_time,
                                            @Field("EndTime") String breakEndTime);

    @FormUrlEncoded
    @POST(DataNames.API_ADD_TIME_SHEET_TRAVEL_TIME_URL)
    Call<PojoNewSuccess> apiAddTSTravelTime(@Field("activity_id") String activityId,
                                            @Field("project_id") String projectId,
                                            @Field("project_title") String project_title,
                                            @Field("date") String date,
                                            @Field("data_type") String dataType,
                                            @Field("fromLocationId") String from_location,
                                            @Field("toLocationId") String to_location,
                                            @Field("StartTime") String travel_start_time,
                                            @Field("EndTime") String travel_end_time);

    @FormUrlEncoded
    @POST(DataNames.API_ADD_TIME_SHEET_EXPENSES_URL)
    Call<PojoNewSuccess> addTSExpenseApi(@Field("activity_id") String activityId,
                                         @Field("project_id") String projectId,
                                         @Field("date") String date,
                                         @Field("data_type") String dataType,/*3*/
                                         @Field("expense_item") String expense_item,
                                         @Field("expense_cost") String expense_cost);

    @FormUrlEncoded
    @POST(DataNames.API_ADD_TIME_SHEET_PRICE_WORK_URL)
    Call<PojoNewSuccess> addTSPriceWorkApi(@Field("activity_id") String activityId,
                                           @Field("data_type") String dataType,
                                           @Field("project_id") String projectId,
                                           @Field("date") String date,
                                           @Field("pricework_item") String priceWork_item,
                                           @Field("pricework_item_cost") String priceWork_item_cost,
                                           @Field("pricework_location") String priceWork_location,
                                           @Field("pricework_quantity") String priceWork_quantity);

    @FormUrlEncoded
    @POST(DataNames.API_ADD_TIME_SHEET_META_DATA_URL)
    Call<PojoNewSuccess> addTSMetaDataApi(@Field("activity_id") String activityId,
                                          @Field("project_id") String projectId,
                                          @Field("date") String date,
                                          @Field("data_type") String dataType,/*5*/
                                          @Field("work_type") String workType,/*0*/
                                          @Field("metadata") String metadata);

    @FormUrlEncoded
    @POST(DataNames.API_ADD_TIME_SHEET_HOLIDAY_URL)
    Call<PojoTSHoliday> addTSHolidayApi(@Field("date") String date,
                                        @Field("work_status") String dataType, /* holiday or sickness*/
                                        @Field("holiday_hours") String hours,
                                        @Field("holiday_start_time") String startTime,
                                        @Field("holiday_end_time") String endTime,
                                        @Field("holiday_duration") String durationDays);

    @FormUrlEncoded
    @POST(DataNames.API_ADD_EDIT_TIME_SHEET_NOTE_URL)
    Call<PojoNewSuccess> addTSNotesApi(@Field("edit_note_id") String editNoteId,
                                       @Field("project_id") String project_id,
                                       @Field("date") String date,
                                       @Field("note_description") String note_description);

    @FormUrlEncoded
    @POST(DataNames.API_DELETE_TIME_SHEET_ACTIVITY_URL)
    Call<PojoNewSuccess> deleteTSActivity(@Field("activity_id") String activityId);

    @FormUrlEncoded
    @POST(DataNames.API_DELETE_TIME_SHEET_NOTE_URL)
    Call<PojoNewSuccess> deleteTSNotesApi(@Field("delnoteid") String delNoteId);

    @FormUrlEncoded
    @POST(DataNames.API_SYNC_TIME_SHEET_URL)
    Call<PojoNewSuccess> syncTimeSheetApi(@Field("offline_requests") String offlineRequests,
                                          @Field("offline_notes") String notes);

    /* **************************  Project Document  *************************** */

    /**
     * Project document list
     *
     * @param projectId get project uid
     * @param catId     get project category id
     * @param offset    get list offset
     * @param limit     get items limit
     */
    @GET(DataNames.PROJECT_DOCS_LIST_URL)
    Call<ProjectDocumentModel> apiProjectDocument(@Path(value = "pid") String projectId,
                                                  @Query("cat") String catId,
                                                  @Query("offset") Integer offset,
                                                  @Query("limit") Integer limit);

    /**
     * Project document Comments list
     *
     * @param documentId get project documentId
     */
    @GET(DataNames.PRO_GET_COMMENTS_URL)
    Call<PDCommentModel> apiPDComments(@Path(value = "documentid") String documentId);

    /**
     * Project document add comment
     *
     * @param documentId   get project documentId
     * @param commentTitle get project commentTitle
     */
    @FormUrlEncoded
    @PUT(DataNames.PRO_ADD_COMMENTS_URL)
    Call<PojoNewSuccess> addPDComment(@Path(value = "documentid") String documentId,
                                      @Field("commenttitle") String commentTitle);

    /**
     * Project document share document
     *
     * @param pid        get project id
     * @param documentId get project documentId
     * @param userList   get shared userList
     */
    @FormUrlEncoded
    @POST(DataNames.PRO_DOCS_SHARE_URL)
    Call<PojoNewSuccess> addPDShare(@Field("document_id") String documentId,
                                    @Field("pid") String pid,
                                    @Field("share_list") String userList);

    /**
     * Project document Category list
     *
     * @param pid get project pid
     */
    @GET(DataNames.PRO_DOCS_CAT_URL)
    Call<CategoryModel> apiProjectDocCategory(@Path(value = "pid") String pid);

    /**
     * Perform action on Project document
     *
     * @param documentAction get project documentAction like favourite,track etc.
     * @param pid            get project pid
     * @param documentId     get project documentId
     */
    @FormUrlEncoded
    @POST(DataNames.PROJECT_DOCS_ACTION_URL)
    Call<PojoCommon> apiDocumentAction(@Field("documentaction") String documentAction,
                                       @Field("pid") String pid,
                                       @Field("documentid") Integer documentId);

    /**
     * Add a new Project document
     *
     * @param projectId     get project pid
     * @param title         get project document title
     * @param category      get project document category
     * @param statusId      get project document statusId
     * @param revision      get project document revision
     * @param signed        get project document signed
     * @param note          get project document note
     * @param pinnedComment get project document pinnedComment
     * @param file          get project document attached file
     */
    @Multipart
    @POST(DataNames.PROJECT_ADD_DOCS_URL)
    Call<PojoCommon> apiAddDocument(@Path(value = "pid") String projectId,
                                    @Part("title") RequestBody title,
                                    @Part("category") RequestBody category,
                                    @Part("status_id") RequestBody statusId,
                                    @Part("revision") RequestBody revision,
                                    @Part("signed") RequestBody signed,
                                    @Part("note") RequestBody note,
                                    @Part("pinned_comment") RequestBody pinnedComment,
                                    @Part MultipartBody.Part file);

    /**
     * Perform action on Project document
     *
     * @param projectId     get project pid
     * @param documentId    get project document ID
     * @param title         get project document title
     * @param category      get project document category
     * @param statusId      get project document statusId
     * @param revision      get project document revision
     * @param signed        get project document signed
     * @param note          get project document note
     * @param pinnedComment get project document pinnedComment
     * @param file          get project document attached file
     */
    @Multipart
    @POST(DataNames.PROJECT_EDIT_DOCS_URL)
    Call<PojoCommon> apiEditDocument(@Path(value = "pid") String projectId,
                                     @Part("id") RequestBody documentId,
                                     @Part("title") RequestBody title,
                                     @Part("category") RequestBody category,
                                     @Part("status_id") RequestBody statusId,
                                     @Part("revision") RequestBody revision,
                                     @Part("signed") RequestBody signed,
                                     @Part("note") RequestBody note,
                                     @Part("pinned_comment") RequestBody pinnedComment,
                                     @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST(DataNames.API_DELETE_PROJECT_DOCS_URL)
    Call<PojoNewSuccess> apiDeleteProjectDoc(@Field("documentid") String docId);

    @GET(DataNames.DOCUMENT_STATUS_URL)
    Call<DoumentStatusModel> apiDocumentStatuses();


    /* ******************   Company Documents *********************** */

    @GET(DataNames.API_GET_COMPANY_DOCS_LIST_URL)
    Call<PojoCompanyDocument> apiCompanyDocs(@Query("cat") String category,
                                             @Query("user") Integer user,
                                             @Query("private") Integer privateVar,
                                             @Query("fav") Integer fav,
                                             @Query("track") Integer track,
                                             @Query("title") String title,
                                             @Query("start") String start,
                                             @Query("end") String end,
                                             @Query("tag") String tag,
                                             @Query("fileid") String fileid,
                                             @Query("offset") Integer offset,
                                             @Query("limit") Integer limit);

    @GET(DataNames.COMPANY_DOCS_STATUS_URL)
    Call<PojoCompanyStatus> apiCompanyStatuses();

    @GET(DataNames.API_COMPANY_DOCS_CATEGORIES_URL)
    Call<CategoryModel> apiCompanyCategories();

    @Multipart
    @POST(DataNames.API_COMPANY_DOCS_ADD_URL)
    Call<PojoNewSuccess> apiAddCompanyDocument(@Part("title") RequestBody title,
                                               @Part("access_level") RequestBody accessLevel/* default = 0*/,
                                               @Part("category") RequestBody category,
                                               @Part("revision") RequestBody revision,
                                               @Part("parent_id") RequestBody parentId,
                                               @Part("private_doc") RequestBody privateDoc,
                                               @Part("signed") RequestBody signed,
                                               @Part("note") RequestBody note,
                                               @Part("tags") RequestBody tags,
                                               @Part("pinned_comment") RequestBody pinnedComment,
                                               @Part("status") RequestBody statusId,
                                               @Part("regional_doc") RequestBody regionalDoc,/* 0 or 1 */
                                               @Part("document_no") RequestBody document_no,/* 0 or 1 */
                                               @Part MultipartBody.Part file);

    @Multipart
    @POST(DataNames.API_EDIT_COMPANY_DOCS_URL)
    Call<PojoNewSuccess> apiEditCompanyDocument(@Part("regionid") RequestBody regionId/* default = 0*/,
                                                @Part("docid") RequestBody docId,
                                                @Part("title") RequestBody title,
                                                @Part("access_level") RequestBody accessLevel/* default = 0*/,
                                                @Part("category") RequestBody category,
                                                @Part("revision") RequestBody revision,
                                                @Part("parent_id") RequestBody parentId/* default = 0*/,
                                                @Part("private_doc") RequestBody privateDoc,
                                                @Part("signed") RequestBody signed,
                                                @Part("note") RequestBody note,
                                                @Part("tags") RequestBody tags,
                                                @Part("pinned_comment") RequestBody pinnedComment,
                                                @Part("status") RequestBody statusId,
                                                @Part("regional_doc") RequestBody regionalDoc,/* 0 or 1 */
                                                @Part("document_no") RequestBody document_no,/* 0 or 1 */
                                                @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST(DataNames.API_DELETE_COMPANY_DOCS_URL)
    Call<PojoNewSuccess> apiDeleteCompanyDoc(@Field("docid") String docId);

    @FormUrlEncoded
    @PUT(DataNames.COMPANY_DOCS_TRACK_URL)
    Call<PojoNewSuccess> apiTrackCompanyDoc(@Field("doc_id") String docId,
                                            @Field("regionid") String regionId,
                                            @Field("title") String title);

    @FormUrlEncoded
    @PUT(DataNames.COMPANY_DOCS_FAVORITE_URL)
    Call<PojoNewSuccess> apiFavoriteCompanyDoc(@Field("doc_id") String docId,
                                               @Field("regionid") String regionId,
                                               @Field("title") String title);

    @GET(DataNames.COMPANY_DOCS_GET_ALL_COMMENT_URL)
    Call<PojoCompanyCommentModel> apiGetAllCommentsCompanyDoc(@Path("docid") String docId);

    @FormUrlEncoded
    @PUT(DataNames.COMPANY_DOCS_ADD_COMMENT_URL)
    Call<PojoNewSuccess> apiAddCommentCompanyDoc(@Field("doc_id") String docId,
                                                 @Field("regionid") String regionId,
                                                 @Field("title") String title,
                                                 @Field("comment") String comment);

    @FormUrlEncoded
    @POST(DataNames.API_SHARE_COMPANY_DOC_URL)
    Call<PojoNewSuccess> apiShareCompanyDoc(@Path("docid") String docid,
                                            @Field("noteIssueEmail") Integer noteIssueEmail,
                                            @Field("shareNote") String shareNote,
                                            @Field("title") String title,
                                            @Field("share_list") String shareList);

    /* To Do List*/
    @GET(DataNames.API_GET_TO_DO_LIST_URL)
    Call<PojoToDoListResp> apiGetToDoList(@Query("pid") String pid,
                                          @Query("status") String status,
                                          @Query("category") String categoryId,
                                          @Query("assigned_to") String assigned_userId,
                                          @Query("date_range") String dateRange,
                                          @Query("offset") Integer offset,
                                          @Query("limit") Integer limit);

    @GET(DataNames.API_GET_TO_DO_CATEGORIES_URL)
    Call<CategoryModel> apiGetToDoCategories();

    @Multipart
    @POST(DataNames.API_ADD_TO_DO_URL)
    Call<PojoNewSuccess> apiAddToDo(@Part("pid") RequestBody projectId,
                                    @Part("title") RequestBody title,
                                    @Part("category") RequestBody category,
                                    @Part("description") RequestBody description,
                                    @Part("due_date") RequestBody dueDate,/* YYYY-MM-DD*/
                                    @Part("to_users") RequestBody toUsers,
                                    @Part("cc_users") RequestBody ccUsers,
                                    @Part("completed_on_date") RequestBody completed_on_date,
                                    @Part List<MultipartBody.Part> filesPart);

    @Multipart
    @POST(DataNames.API_EDIT_TO_DO_URL)
    Call<PojoNewSuccess> apiEditToDo(@Part("id") RequestBody toDoId,
                                     @Part("pid") RequestBody projectId,
                                     @Part("title") RequestBody title,
                                     @Part("category") RequestBody category,
                                     @Part("description") RequestBody description,
                                     @Part("due_date") RequestBody dueDate,/* YYYY-MM-DD*/
                                     @Part("to_users") RequestBody toUsers,
                                     @Part("cc_users") RequestBody ccUsers,
                                     @Part("todo_image_id") RequestBody todo_image_id,
                                     @Part List<MultipartBody.Part> filesPart /*file*/);

    @FormUrlEncoded
    @POST(DataNames.API_MARK_TO_DO_URL)
    Call<PojoNewSuccess> apiMarkToDo(@Field("id") Integer toDoId,
                                     @Field("status") Integer status,
                                     @Field("completed_on_date") String completed_on_date
    );

    @GET(DataNames.API_GET_TO_DO_COMMENTS_URL)
    Call<ToDoCommentsModel> apiGetToDoComments(@Query("id") String toDoId);

    @FormUrlEncoded
    @POST(DataNames.API_ADD_TO_DO_COMMENTS_URL)
    Call<PojoSuccessCommon> apiAddToDoComment(@Field("id") String toDoId,
                                              @Field("comment") String comment);

    /* ***********************  support ************************ */

    @Multipart
    @POST(DataNames.SUPPORT_URL)
    Call<AddDiaryModel> sendReport(@Part("email") RequestBody email,
                                   @Part("description") RequestBody description,
                                   @Part List<MultipartBody.Part> files);

    @GET(DataNames.NOTIFICATION_URL)
    Call<NotificationDataModel> getUserNotification(@Query("offset") Integer offset,
                                                    @Query("limit") Integer limit);


    @FormUrlEncoded
    @POST(DataNames.NOTIFICATION_READ_URL)
    Call<PojoSuccessCommon> readNotification(@Field("notification_id") int notification_id);

    @GET(DataNames.RECENT_DOCUMENTS_URL)
    Call<RecentCategoryModel> getRecentCategories(@Query("project_id") String projectId);

    /**
     * Get all Site Projects
     *
     * @param stageId     stageId
     * @param statusId    statusId
     * @param regionId    regionId
     * @param projectId   projectId
     * @param sectionName sectionName
     * @param offset      offset
     * @param limit       limit
     */
    @GET(DataNames.SITE_PROJECT)
    Call<ProjectListModel> getProjectsList(@Query("stage_id") String stageId,
                                           @Query("status_id") String statusId,
                                           @Query("region_id") String regionId,
                                           @Query("project_id") String projectId,
                                           @Query("section_name") String sectionName,
                                           @Query("offset") Integer offset,
                                           @Query("limit") Integer limit);

    /**
     * Get all Site Projects Notes
     *
     * @param projectId projectId
     * @param offset    offset
     * @param limit     limit
     */
    @GET(DataNames.SITE_PROJECT_NOTES)
    Call<PMNotesModel> getProjectsNotes(@Query("project_id") String projectId,
                                        @Query("offset") Integer offset,
                                        @Query("limit") Integer limit);

    @GET(DataNames.USER_ASSIGNED_PROJECTS)
    Call<ProjectsUpdateModel> getUserAssignedProjects();


}






