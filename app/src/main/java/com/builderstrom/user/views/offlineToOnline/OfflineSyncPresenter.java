package com.builderstrom.user.views.offlineToOnline;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.builderstrom.user.data.database.databaseModels.DbTimesheet;
import com.builderstrom.user.data.database.databaseModels.Login;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.api.ErrorCodes;
import com.builderstrom.user.data.retrofit.api.RestClient;
import com.builderstrom.user.data.retrofit.modals.AddDiaryModel;
import com.builderstrom.user.data.retrofit.modals.AddDrawingModel;
import com.builderstrom.user.data.retrofit.modals.AddMoreComment;
import com.builderstrom.user.data.retrofit.modals.AddRFIModel;
import com.builderstrom.user.data.retrofit.modals.Attachment;
import com.builderstrom.user.data.retrofit.modals.CompanyDocument;
import com.builderstrom.user.data.retrofit.modals.LocalMetaValues;
import com.builderstrom.user.data.retrofit.modals.PDocsDataModel;
import com.builderstrom.user.data.retrofit.modals.PojoAddGallery;
import com.builderstrom.user.data.retrofit.modals.PojoCommon;
import com.builderstrom.user.data.retrofit.modals.PojoMyItem;
import com.builderstrom.user.data.retrofit.modals.PojoNewSuccess;
import com.builderstrom.user.data.retrofit.modals.PojoSuccessCommon;
import com.builderstrom.user.data.retrofit.modals.PojoToDo;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.MyItemModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class OfflineSyncPresenter {

    private static String TAG = OfflineSyncPresenter.class.getName();

    void projectSignConfirm(Context context, String projectID, String slat, String sLng,
                            String eLat, String eLng, String startTime, String endTime,
                            @Nullable String overtimeList, @Nullable String selectedIdList,
                            @Nullable String standardBreakList, Integer id) {

        RestClient.createService().projectSync(projectID,
                startTime.isEmpty() ? null : CommonMethods.convertDate(CommonMethods.DF_15, startTime, CommonMethods.DF_2),
                startTime.isEmpty() ? null : CommonMethods.convertDate(CommonMethods.DF_15, startTime, CommonMethods.DF_9),
                slat, sLng,
                endTime.isEmpty() ? null : CommonMethods.convertDate(CommonMethods.DF_1, endTime, CommonMethods.DF_2),
                endTime.isEmpty() ? null : CommonMethods.convertDate(CommonMethods.DF_1, endTime, CommonMethods.DF_9),
                endTime.isEmpty() ? null : eLat, endTime.isEmpty() ? null : eLng, overtimeList,
                selectedIdList, standardBreakList).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                try {
                    CommonMethods.setLogs(TAG, "projectSignConfirm", new Gson().toJson(response));
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        BuilderStormApplication.mLocalDatabase.deleteSignInformation(BuilderStormApplication.mPrefs.getSiteId(),
                                BuilderStormApplication.mPrefs.getUserId(), id);

                        /* if is last project then send broadcast to the home fragment to check project sign in/out */
                        if (endTime.isEmpty()) {
                            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(DataNames.INTENT_ACTION_SAVE_SIGN_IN_OUT));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {

            }
        });
    }

    void callDrawingAPI(String drawingName, String projectId, String drawingNumber, String revisionNumber, String pdfLocation, String dwgLocation, String otfLocation, List<LocalMetaValues> metaData, boolean isLastIndex) {
        List<MultipartBody.Part> metaFiles = new ArrayList<>();
        for (LocalMetaValues values : metaData) {
            if (values.getAttachment()) {
                metaFiles.add(CommonMethods.createPartFromFile(TextUtils.concat("file_", values.getCustomFieldId()).toString(), values.getCustomFieldValue()));
            }
        }

        RestClient.createService().addDrawingAPI(projectId, CommonMethods.createPartFromString(drawingName),
                CommonMethods.createPartFromString(drawingNumber), CommonMethods.createPartFromString(revisionNumber),
                null == pdfLocation || pdfLocation.isEmpty() ? null : CommonMethods.createPartFromFile("drawing_pdf", pdfLocation),
                null == dwgLocation || dwgLocation.isEmpty() ? null : CommonMethods.createPartFromFile("drawing_dwg", dwgLocation),
                null == otfLocation || otfLocation.isEmpty() ? null : CommonMethods.createPartFromFile("other_fileDraw", otfLocation),
                CommonMethods.createPartFromString(CommonMethods.filteredMetaDataString(metaData)),
                metaFiles).enqueue(new Callback<AddDrawingModel>() {
            @Override
            public void onResponse(@NonNull Call<AddDrawingModel> call, @NonNull Response<AddDrawingModel> response) {
                try {
                    CommonMethods.setLogs(TAG, "Syn add drawing API", new Gson().toJson(response));
                    if (isLastIndex) {
                        try {
                            BuilderStormApplication.mLocalDatabase.deleteDrawing(BuilderStormApplication.mPrefs.getSiteId(), 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddDrawingModel> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "callDrawingAPI", t.getLocalizedMessage());
            }
        });
    }

    void submitDigitalForm(PojoMyItem myItem, Integer columnId, String projectID, String assigned_user, String templateID, String customDocumentId, String pDocumentId, List<LocalMetaValues> metaData) {
        List<MultipartBody.Part> filesPart = new ArrayList<>();
        /** initialization of index */
        int index = 0;
        String previous_value = "";  //for check the same row_column_id
        for (LocalMetaValues values : metaData) {
            if (values.getAttachment()) {
                if (previous_value != null && !previous_value.isEmpty() && previous_value.equalsIgnoreCase(values.getCustomFieldId())) {
                    index = ++index;
                } else {
                    index = 0;
                }
                filesPart.add(CommonMethods.createPartFromFile(String.format("custom_doc[%s_%s]", values.getCustomFieldId(), index), values.getCustomFieldValue()));
                previous_value = values.getCustomFieldId();
            }
        }
//
//
//        List<MultipartBody.Part> filesPart = new ArrayList<>();
//        int index = 0;
//        String previous_value = "";  //for check the same row_column_id
//        for (LocalMetaValues values : metaData) {
//            if (values.getContainFile() /*&& !values.getCustomFieldValue().isEmpty()*/) {
//                if (previous_value != null && !previous_value.isEmpty() && previous_value.equalsIgnoreCase(values.getCustomFieldId())) {
//                    index = ++index;
//                } else {
//                    index = 0;
//                }
//                if (values.getAttachment()) {
//                    filesPart.add(CommonMethods.createPartFromFile(String.format("custom_doc[%s_%s]", values.getCustomFieldId(), index), values.getCustomFieldValue()));
//                }
//                previous_value = values.getCustomFieldId();
//            }
//        }

//        int index = 0;
//        for (LocalMetaValues values : metaData) {
//            if (values.getAttachment()) {
//                filesPart.add(CommonMethods.createPartFromFile(String.format("custom_doc[%s_%s]", values.getCustomFieldId(), String.valueOf(index++)), values.getCustomFieldValue()));
//            }
//        }

        if (myItem != null) {
            RestClient.createService().addDigitalMyItem(templateID, CommonMethods.createPartFromString(projectID),
                    CommonMethods.createPartFromString(assigned_user),
                    CommonMethods.createPartFromString(new Gson().toJson(metaData)),
                    CommonMethods.createPartFromString("0"),
                    CommonMethods.createPartFromString(pDocumentId),
                    CommonMethods.createPartFromString(myItem.getIssue()),
                    CommonMethods.createPartFromString(myItem.getRecurrence()),
                    CommonMethods.createPartFromString(CommonMethods.convertDate(CommonMethods.DF_2, myItem.getForCompleteDate(),
                            CommonMethods.DF_7)), filesPart).enqueue(new Callback<PojoNewSuccess>() {
                @Override
                public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                    CommonMethods.setLogs(TAG, "submitDigitalForm", new Gson().toJson(response));
                    try {
                        if (response.body() != null && response.body().getSuccess()) {
                            BuilderStormApplication.mLocalDatabase.deleteDDTemplateRow(columnId);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                    CommonMethods.setLogs(TAG, "submitDigitalForm", t.getLocalizedMessage());
                }
            });
        } else {
            RestClient.createService().addDigitalDoc(templateID, CommonMethods.createPartFromString(projectID),
                    CommonMethods.createPartFromString(assigned_user),
                    CommonMethods.createPartFromString(customDocumentId),
                    CommonMethods.createPartFromString(pDocumentId),
                    CommonMethods.createPartFromString(new Gson().toJson(metaData)),
                    CommonMethods.createPartFromString("0"),
                    filesPart).enqueue(new Callback<PojoNewSuccess>() {
                @Override
                public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                    CommonMethods.setLogs(TAG, "submitDigitalForm", new Gson().toJson(response));
                    try {
                        if (response.body() != null && response.body().getSuccess()) {
                            BuilderStormApplication.mLocalDatabase.deleteDDTemplateRow(columnId);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                    CommonMethods.setLogs(TAG, "submitDigitalForm", t.getLocalizedMessage());
                }
            });
        }
    }

    void syncIssueDocument(MyItemModel model, int columnId) {
        RestClient.createService().apiIssueDocument(model.getProjectId(), model.getUsers(), model.getDocId(), model.getIssueId(), model.getRefId(), model.getCompletedFor(),
                model.getIsCompleted()).enqueue(new Callback<PojoCommon>() {
            @Override
            public void onResponse(@NonNull Call<PojoCommon> call, @NonNull Response<PojoCommon> response) {
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        BuilderStormApplication.mLocalDatabase.deleteShareDocsRow(columnId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoCommon> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "Sync Issue document", t.getLocalizedMessage());
            }
        });
    }

    void syncTimeSheet(DbTimesheet timeSheet) {
        Log.e("time sheet data", new Gson().toJson(timeSheet));
        RestClient.createService().syncTimeSheetApi(timeSheet.getOverView(), timeSheet.getNotes()).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        BuilderStormApplication.mLocalDatabase.deleteTimeSheet(timeSheet.getId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                Log.e("Offline failure",t.getLocalizedMessage());

            }
        });

    }

    void syncCompanyDocument(Integer columnId, CompanyDocument data) {

        RestClient.createService().apiAddCompanyDocument(CommonMethods.createPartFromString(data.getTitle()),
                CommonMethods.createPartFromString("0"),
                CommonMethods.createPartFromString(data.getCategory()),
                CommonMethods.createPartFromString(data.getRevision()),
                CommonMethods.createPartFromString(data.getParent_id()),
                CommonMethods.createPartFromString("0"),
                CommonMethods.createPartFromString(data.getSigned_doc())/* isSigned*/,
                CommonMethods.createPartFromString(""),
                CommonMethods.createPartFromString(""),
                CommonMethods.createPartFromString(data.getPinComment()),
                CommonMethods.createPartFromString(data.getDoc_status()),
                CommonMethods.createPartFromString(data.getRegional_doc()),
                CommonMethods.createPartFromString(data.getDocument_no() == null ? "" : data.getDocument_no()),
                CommonMethods.isFileNullOrEmpty(data.getFilelocation()) ? null :
                        CommonMethods.createPartFromFile("document", data.getFilelocation()))
                .enqueue(new Callback<PojoNewSuccess>() {
                    @Override
                    public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                        try {
                            if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                                /* delete document from database*/
                                BuilderStormApplication.mLocalDatabase.deleteCompanyDocById(columnId);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });

    }

    void syncProjectDocument(Integer columnId, String projectId, PDocsDataModel dataModel) {
        RestClient.createService().apiAddDocument(projectId,
                CommonMethods.createPartFromString(dataModel.getTitle()),
                CommonMethods.createPartFromString(dataModel.getCategoryUid() == null ? "" : dataModel.getCategoryUid()),
                CommonMethods.createPartFromString(dataModel.getStatusID()),
                CommonMethods.createPartFromString(dataModel.getRevision()),
                CommonMethods.createPartFromString(dataModel.getSignedDoc()),
                CommonMethods.createPartFromString(dataModel.getNote()),
                CommonMethods.createPartFromString(dataModel.getPincomment()),
                CommonMethods.isFileNullOrEmpty(dataModel.getFile()) ? null : CommonMethods.createPartFromFile("document", dataModel.getFile())
        ).enqueue(new Callback<PojoCommon>() {
            @Override
            public void onResponse(@NonNull Call<PojoCommon> call, @NonNull Response<PojoCommon> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        /* delete document from database*/
                        BuilderStormApplication.mLocalDatabase.deleteProjectDocById(columnId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoCommon> call, @NonNull Throwable t) {

            }
        });

    }

    void sendReport(String description, List<String> parts, Integer reportID) {
        List<MultipartBody.Part> filesPart = new ArrayList<>();
        for (String path : parts) {
            filesPart.add(CommonMethods.createPartFromFile("logs" + parts.indexOf(path), path));
        }

        RestClient.createService().sendReport(CommonMethods.createPartFromString(BuilderStormApplication.mPrefs.getUserEmail()),
                CommonMethods.createPartFromString(description),
                filesPart).enqueue(new Callback<AddDiaryModel>() {
            @Override
            public void onResponse(@NonNull Call<AddDiaryModel> call, @NonNull Response<AddDiaryModel> response) {
                if (response.body() != null && response.body().getResponseCode()) {
                    BuilderStormApplication.mLocalDatabase.deleteReport(reportID);
                }

            }

            @Override
            public void onFailure(@NonNull Call<AddDiaryModel> call, @NonNull Throwable t) {

            }
        });
    }

    void addDiary(String projectID, String title,
                  String description, String date, String submittedTime, List<String> parts,
                  List<LocalMetaValues> metaData, Integer diaryId, String siteLabor) {
        List<MultipartBody.Part> filesPart = new ArrayList<>();
        for (LocalMetaValues values : metaData) {
            if (values.getAttachment()) {
                filesPart.add(CommonMethods.createPartFromFile(String.format("custom_doc[%s]", values.getCustomFieldId()), values.getCustomFieldValue()));
            }
        }

        for (String path : parts) {
            filesPart.add(CommonMethods.createPartFromFile("diaryfiles" + parts.indexOf(path), path));
        }
        RestClient.createService().addDiaryAPI(projectID, /*true,*/
                CommonMethods.createPartFromString("1"),
                CommonMethods.createPartFromString(""),
                CommonMethods.createPartFromString(title),
                CommonMethods.createPartFromString(description),
                CommonMethods.createPartFromString(CommonMethods.convertDate(CommonMethods.DF_8, date, CommonMethods.DF_2)),
                CommonMethods.createPartFromString(""),
                CommonMethods.createPartFromString(""),
                CommonMethods.createPartFromString(CommonMethods.filteredMetaDataString(metaData)),
                CommonMethods.createPartFromString(submittedTime),
                CommonMethods.createPartFromString(siteLabor),
                filesPart).enqueue(new Callback<AddDiaryModel>() {
            @Override
            public void onResponse(@NonNull Call<AddDiaryModel> call, @NonNull Response<AddDiaryModel> response) {

                try {
                    CommonMethods.setLogs(TAG, "addDiary syncing", new Gson().toJson(response));
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        BuilderStormApplication.mLocalDatabase.deleteDiary(diaryId);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(@NonNull Call<AddDiaryModel> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "addDiary syncing", t.getLocalizedMessage());
            }
        });
    }

    void callAddRFI(String title, String dueDate, String description, String submittedTime,
                    String pid, String toUsers, String ccUsers, List<LocalMetaValues> metaData, List<String> parts,
                    Integer deleteId) {

        List<MultipartBody.Part> filesPart = new ArrayList<>();
        for (LocalMetaValues values : metaData) {
            if (values.getAttachment()) {
                filesPart.add(CommonMethods.createPartFromFile(String.format("custom_doc[%s]", values.getCustomFieldId()), values.getCustomFieldValue()));
            }
        }

        for (String filepath : parts) {
            filesPart.add(CommonMethods.createPartFromFile(String.format("photo[%s]", parts.indexOf(filepath)), filepath));
//            filesPart.add(CommonMethods.createPartFromFile("diaryfiles" + parts.indexOf(filepath), filepath));
        }

        RestClient.createService().apiAddRfi(CommonMethods.createPartFromString(title),
                CommonMethods.createPartFromString(pid),
                CommonMethods.createPartFromString(description),
                CommonMethods.createPartFromString(dueDate),
                CommonMethods.createPartFromString(submittedTime),
                CommonMethods.createPartFromString(toUsers),
                CommonMethods.createPartFromString(ccUsers),
                CommonMethods.createPartFromString(CommonMethods.filteredMetaDataString(metaData)),
                filesPart).enqueue(new Callback<AddRFIModel>() {
            @Override
            public void onResponse(@NonNull Call<AddRFIModel> call, @NonNull Response<AddRFIModel> response) {
                CommonMethods.setLogs(TAG, "callAddRFI", new Gson().toJson(response));
                try {
                    if (response.body() != null) {
                        if (deleteId != null) {
                            BuilderStormApplication.mLocalDatabase.deleteRFI(deleteId);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddRFIModel> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "callAddRFI", t.getLocalizedMessage());
            }
        });
    }

    void addAnswerToRfi(String answerString, String rfiId, List<String> files) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        if (files != null) {
            for (String filePath : files) {
                parts.add(CommonMethods.createPartFromFile("diaryFiles" + files.indexOf(filePath), filePath));
            }
        }

        RestClient.createService().apiAddRFIAnswer(rfiId, CommonMethods.createPartFromString(answerString), parts)
                .enqueue(new Callback<PojoNewSuccess>() {
                    @Override
                    public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                        CommonMethods.setLogs(TAG, "callAddRFI", new Gson().toJson(response));

                    }

                    @Override
                    public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                        CommonMethods.setLogs(TAG, "getRFIList", t.getLocalizedMessage());
                    }
                });
    }

    void addSnag(String projectId, String title, String location, String packNo, String toUsers,
                 String ccUsers, String dueDate, String submittedTime, List<String> parts,
                 Integer deleteId) {
        List<MultipartBody.Part> filesPart = new ArrayList<>();
        for (String filepath : parts) {
            filesPart.add(CommonMethods.createPartFromFile("diaryfiles" + parts.indexOf(filepath), filepath));
        }
        RestClient.createService().apiAddSnag(projectId, CommonMethods.createPartFromString(title),
                CommonMethods.createPartFromString(location), CommonMethods.createPartFromString(packNo),
                CommonMethods.createPartFromString(toUsers), CommonMethods.createPartFromString(ccUsers),
                CommonMethods.createPartFromString(dueDate),
                CommonMethods.createPartFromString(submittedTime), filesPart).enqueue(
                new Callback<AddMoreComment>() {
                    @Override
                    public void onResponse(@NonNull Call<AddMoreComment> call, @NonNull Response<AddMoreComment> response) {
                        CommonMethods.setLogs(TAG, "addSnag", new Gson().toJson(response));
                        try {
                            if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                                BuilderStormApplication.mLocalDatabase.deleteSnags(deleteId);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AddMoreComment> call, @NonNull Throwable t) {
                        CommonMethods.setLogs(TAG, "addSnag", t.getLocalizedMessage());
                    }
                }
        );

    }

    void addGalleryMultipartAPI(String projectID, String title,
                                String latitude, String longitude, List<String> files,
                                List<LocalMetaValues> metaData, String galleryRowID) {

        List<MultipartBody.Part> multipartPiles = new ArrayList<>();

        for (LocalMetaValues values : metaData) {
            if (values.getAttachment()) {
                multipartPiles.add(CommonMethods.createPartFromFile(String.format("custom_doc[%s]", values.getCustomFieldId()), values.getCustomFieldValue()));
            }
        }

        for (String filePath : files) {
            multipartPiles.add(CommonMethods.createPartFromFile("galleryFiles" + files.indexOf(filePath), filePath));
        }


        RestClient.createService().apiAddGallery(CommonMethods.createPartFromString(projectID), null,
                CommonMethods.createPartFromString(title), CommonMethods.createPartFromString(latitude),
                CommonMethods.createPartFromString(longitude),
                CommonMethods.createPartFromString(CommonMethods.filteredMetaDataString(metaData)),
                multipartPiles).enqueue(new Callback<PojoAddGallery>() {
            @Override
            public void onResponse(@NonNull Call<PojoAddGallery> call, @NonNull Response<PojoAddGallery> response) {
                CommonMethods.setLogs(TAG, "sync gallery", new Gson().toJson(response));
                if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                    BuilderStormApplication.mLocalDatabase.deleteGallery(galleryRowID);
                }
            }


            @Override
            public void onFailure(@NonNull Call<PojoAddGallery> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "sync gallery", t.getLocalizedMessage());
            }
        });
    }

    void syncToDo(Integer columnId, PojoToDo toDo) {
        List<MultipartBody.Part> filesPart = new ArrayList<>();
        if (toDo.getAttachments() != null) {
            for (Attachment attach : toDo.getAttachments()) {
                filesPart.add(CommonMethods.createPartFromFile("files", attach.getFileUrl()));
            }
        }
        RestClient.createService().apiAddToDo(CommonMethods.createPartFromString(BuilderStormApplication.mPrefs.getProjectId()),
                CommonMethods.createPartFromString(toDo.getTitle()),
                CommonMethods.createPartFromString(String.valueOf(toDo.getCategory())),
                CommonMethods.createPartFromString(toDo.getDescription()),
                CommonMethods.createPartFromString(toDo.getDueDate()),
                CommonMethods.createPartFromString(toDo.getStrToUsers()),
                CommonMethods.createPartFromString(toDo.getStrCcUsers()),
                CommonMethods.createPartFromString(toDo.getCompletedOn() != null ? toDo.getCompletedOn() : ""),
                filesPart).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        if (columnId != null) {
                            BuilderStormApplication.mLocalDatabase.deleteToDoById(columnId);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                Log.e("error", "failure in add at offline");
            }
        });

    }

    void syncLoginStatus(Login mLoginModel) {

        /* empty base site when user logout offline and try to change site with wrong site name
           then base url becomes empty and leads to crash */

        if (!BuilderStormApplication.mPrefs.getBaseSite().isEmpty()) {
            Log.e("Login Data", new Gson().toJson(mLoginModel));
            RestClient.createService().synLoginStatus(String.valueOf(mLoginModel.getIsLogin()), mLoginModel.getFirebase_token()).enqueue(new Callback<PojoSuccessCommon>() {
                @Override
                public void onResponse(@NonNull Call<PojoSuccessCommon> call, @NonNull Response<PojoSuccessCommon> response) {
                    try {
                        if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                            Log.e(TAG, "Login data synced");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PojoSuccessCommon> call, @NonNull Throwable t) {
                    CommonMethods.setLogs(TAG, "sync Login", t.getLocalizedMessage());
                }
            });
        } else {
            Log.e("Login sync", "cannot be done because site name is empty");
        }

    }

    public void markTodo(Integer toDoId, Integer status, Integer columnId, String date) {
        RestClient.createService().apiMarkToDo(toDoId, status, date != null ? date : "").enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        if (columnId != null) {
                            BuilderStormApplication.mLocalDatabase.deleteToDoStatusId(columnId);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                CommonMethods.setLogs(TAG, "sync Todo resolved", t.getLocalizedMessage());
            }
        });
    }

}
