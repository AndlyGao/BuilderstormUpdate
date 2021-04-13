package com.builderstrom.user.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.database.databaseModels.Categories;
import com.builderstrom.user.repository.database.databaseModels.Users;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.api.ErrorCodes;
import com.builderstrom.user.repository.retrofit.api.RestClient;
import com.builderstrom.user.repository.retrofit.modals.CatListing;
import com.builderstrom.user.repository.retrofit.modals.CategoryModel;
import com.builderstrom.user.repository.retrofit.modals.DigiDocLiveModel;
import com.builderstrom.user.repository.retrofit.modals.DigitalDoc;
import com.builderstrom.user.repository.retrofit.modals.DigitalDocumentModel;
import com.builderstrom.user.repository.retrofit.modals.DigitalFormModel;
import com.builderstrom.user.repository.retrofit.modals.ErrorModel;
import com.builderstrom.user.repository.retrofit.modals.LocalMetaValues;
import com.builderstrom.user.repository.retrofit.modals.MetaOptions;
import com.builderstrom.user.repository.retrofit.modals.MyItemLiveDataModel;
import com.builderstrom.user.repository.retrofit.modals.PojoMyItem;
import com.builderstrom.user.repository.retrofit.modals.PojoMyItemRespModel;
import com.builderstrom.user.repository.retrofit.modals.PojoNewSuccess;
import com.builderstrom.user.repository.retrofit.modals.RowFormModel;
import com.builderstrom.user.repository.retrofit.modals.TemplateData;
import com.builderstrom.user.repository.retrofit.modals.User;
import com.builderstrom.user.repository.retrofit.modals.UserModel;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak")
public class DigitalDocumentVM extends BaseViewModel {
    public MutableLiveData<Boolean> isRefreshingLD;
    public MutableLiveData<List<CatListing>> categoriesLD;
    public MutableLiveData<List<User>> usersLD;
    public MutableLiveData<DigiDocLiveModel> docsLD;
    public MutableLiveData<MyItemLiveDataModel> myItemsLD;
    public MutableLiveData<Boolean> isDocumentAdapterLD;
    public MutableLiveData<Boolean> notifyAdapterLD;
    public MutableLiveData<DigitalFormModel.DigitalDocHeader> docHeaderLD;
    public MutableLiveData<Boolean> isStagedLD;
    public MutableLiveData<List<TemplateData>> digitalFormLD;
    public MutableLiveData<Boolean> submitFormLD;
    public MutableLiveData<Integer> rowID;
    public MutableLiveData<Integer> offsetLD;
    private List<String> rowIdList = new ArrayList<>();
    private List<String> myItemsRowIdList = new ArrayList<>();

    public DigitalDocumentVM(@NonNull Application application) {
        super(application);
        isRefreshingLD = new MutableLiveData<>();
        categoriesLD = new MutableLiveData<>();
        usersLD = new MutableLiveData<>();
        docsLD = new MutableLiveData<>();
        myItemsLD = new MutableLiveData<>();
        isDocumentAdapterLD = new MutableLiveData<>();
        docHeaderLD = new MutableLiveData<>();
        isStagedLD = new MutableLiveData<>();
        notifyAdapterLD = new MutableLiveData<>();
        digitalFormLD = new MutableLiveData<>();
        submitFormLD = new MutableLiveData<>();
        rowID = new MutableLiveData<>();
        offsetLD = new MutableLiveData<>();
    }

    public void updateRowIdList() {
        rowIdList.clear();
        rowIdList.addAll(mDatabase.getSyncedDiDocList(mPrefs.getSiteId(), mPrefs.getUserId(), 0));
        myItemsRowIdList.clear();
        myItemsRowIdList.addAll(mDatabase.getSyncedItemDocList(mPrefs.getSiteId(), mPrefs.getUserId(), mPrefs.getProjectId(), 1));
    }

    public void updateRecentDocsList(String catSection, String categoryId) {
        myItemsRowIdList.clear();
        myItemsRowIdList.addAll(mDatabase.getSyncedShortDocList(mPrefs.getSiteId(), mPrefs.getUserId(), mPrefs.getProjectId(), 1, catSection, categoryId));
    }

    public void getDocumentCategories() {
        isLoadingLD.postValue(true);
        RestClient.createService().apiDigitalDocCategories().enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(@NonNull Call<CategoryModel> call, @NonNull Response<CategoryModel> response) {
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        if (response.body().getData() != null) {
                            response.body().getData().add(0, new CatListing("0", "All Categories"));
                            addCategoriesToDatabase(response.body().getData(), DataNames.DIGITAL_CATEGORIES);
                            categoriesLD.postValue(response.body().getData());
                        } else {
                            categoriesLD.postValue(new ArrayList<>());
                        }
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (IOException e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryModel> call, @NonNull Throwable t) {
                getOfflineCategories(DataNames.DIGITAL_CATEGORIES);
            }
        });
    }

    public void getDocumentUsers() {
        RestClient.createService().apiDigitalItemUsers().enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                setLogs(RFIViewModel.class.getName(), "getDigitalUsers", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        /** update users in local database */
                        response.body().getAllUsers().add(0, new User("0", "Select Issued user"));
                        usersLD.postValue(response.body().getAllUsers());
                        updateUsersToDatabase(response.body().getAllUsers(), DataNames.DIGI_DOCS_USERS);
                    } else {
                        usersLD.postValue(new ArrayList<>());
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                setLogs(DigitalDocumentVM.class.getName(), "getDigitalUsers", t.getLocalizedMessage());
                getUsersFromDB();
            }
        });
    }

    public void getAllDigitalDocs(String categoryId) {
        isRefreshingLD.postValue(true);
        RestClient.createService().apiGetAllDigitalDocuments(categoryId).enqueue(
                new Callback<DigitalDocumentModel>() {
                    @Override
                    public void onResponse(@NonNull Call<DigitalDocumentModel> call, @NonNull Response<DigitalDocumentModel> response) {
                        isRefreshingLD.postValue(false);
                        isDocumentAdapterLD.postValue(true);
                        try {
                            if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                                for (DigitalDoc digitalDoc : response.body().getData()) {
                                    digitalDoc.setSynced(rowIdList.contains(digitalDoc.getTemplateId()));
                                }
                                docsLD.postValue(new DigiDocLiveModel(false, response.body().getData()));

                            } else {
                                handleErrorBody(response.errorBody());
                            }
                        } catch (Exception e) {
                            errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<DigitalDocumentModel> call, @NonNull Throwable t) {
                        updateFromDatabase(categoryId);
                    }
                });
    }

    public void getMyDocuments(String categoryId, String issuedBy, String catSection, String companyProjectCatId, int offset, int limit) {
        isRefreshingLD.postValue(true);
        RestClient.createService().apiGetDigiMyItems(null, categoryId, issuedBy,
                mPrefs.getProjectId(), catSection, companyProjectCatId, offset, limit).enqueue(new Callback<PojoMyItemRespModel>() {
            @Override
            public void onResponse(@NonNull Call<PojoMyItemRespModel> call, @NonNull Response<PojoMyItemRespModel> response) {
                isDocumentAdapterLD.postValue(false);
                isRefreshingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        for (PojoMyItem item : response.body().getData()) {
                            item.setSynced(myItemsRowIdList.contains(item.getTemplateId()));
                        }
                        offsetLD.postValue(response.body().getDataLimit().getOffset());
                        myItemsLD.postValue(new MyItemLiveDataModel(response.body().getData(),
                                false));
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoMyItemRespModel> call, @NonNull Throwable t) {
                getOfflineMyItems(categoryId, issuedBy, catSection, companyProjectCatId);
            }
        });
    }

    public void syncDocument(String categoryId, String templateId, String templateName, String recurringType, String issue_Id) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiGetDigitalDocForm(templateId, 0, 0, issue_Id, mPrefs.getProjectId()).enqueue(new Callback<DigitalFormModel>() {
            @Override
            public void onResponse(@NonNull Call<DigitalFormModel> call, @NonNull Response<DigitalFormModel> response) {
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        syncToDatabase(categoryId, templateId, templateName, response.body(), recurringType);
                    } else {
                        isLoadingLD.postValue(false);
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DigitalFormModel> call, @NonNull Throwable t) {
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void getRawFormAPI(String templateID, Integer pDocId, String issue_Id) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiGetDigitalDocForm(templateID, 0, pDocId, issue_Id, mPrefs.getProjectId()).enqueue(new Callback<DigitalFormModel>() {
            @Override
            public void onResponse(@NonNull Call<DigitalFormModel> call, @NonNull Response<DigitalFormModel> response) {
                isLoadingLD.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        docHeaderLD.postValue(response.body().getDataHeader());
                        digitalFormLD.postValue(response.body().getData());
                        isStagedLD.postValue(response.body().getStaged());
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DigitalFormModel> call, @NonNull Throwable t) {
                getOfflineDbForm(templateID);
            }
        });
    }

    public void submitDigitalForm(String recType, Integer rowID, String assigned_user, PojoMyItem myItem, String templateID, Integer pDocumentId, Integer customDocumentId, List<LocalMetaValues> metaData) {
        Log.e("meta value", new Gson().toJson(metaData));
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
        Log.e("File values", new Gson().toJson(filesPart));
        isLoadingLD.postValue(true);
        if (myItem != null) {
            // My Items
            RestClient.createService().addDigitalMyItem(templateID,
                    CommonMethods.createPartFromString(mPrefs.getProjectId()),
                    CommonMethods.createPartFromString(assigned_user),
                    CommonMethods.createPartFromString(new Gson().toJson(metaData)),
                    CommonMethods.createPartFromString("0"),
                    CommonMethods.createPartFromString(String.valueOf(customDocumentId)),
                    CommonMethods.createPartFromString(myItem.getIssue()),
                    CommonMethods.createPartFromString(myItem.getRecurrence()),
                    CommonMethods.createPartFromString(CommonMethods.convertDate(CommonMethods.DF_2, myItem.getForCompleteDate(), CommonMethods.DF_7)),
                    filesPart).enqueue(new Callback<PojoNewSuccess>() {
                @Override
                public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                    isLoadingLD.postValue(false);
                    try {
                        if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                            errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                            updateDocumentReccerance(recType, templateID);
                            submitFormLD.postValue(true);
                        } else {
                            handleErrorBody(response.errorBody());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                    saveValuesToDb(recType, rowID, assigned_user, new Gson().toJson(myItem), templateID, customDocumentId, pDocumentId, metaData);
                }
            });
        } else {
            // Digital Documents
            RestClient.createService().addDigitalDoc(templateID,
                    CommonMethods.createPartFromString(mPrefs.getProjectId()),
                    CommonMethods.createPartFromString(assigned_user),
                    CommonMethods.createPartFromString(String.valueOf(customDocumentId)),
                    CommonMethods.createPartFromString(String.valueOf(pDocumentId)),
                    CommonMethods.createPartFromString(new Gson().toJson(metaData)),
                    CommonMethods.createPartFromString("0"),
                    filesPart).enqueue(new Callback<PojoNewSuccess>() {
                @Override
                public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                    isLoadingLD.postValue(false);
                    try {
                        if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                            errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, response.body().getMessage()));
                            submitFormLD.postValue(true);
                        } else {
                            handleErrorBody(response.errorBody());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                    saveValuesToDb("", null, assigned_user, "", templateID, customDocumentId, pDocumentId, metaData);
                }
            });
        }
    }

    /* Database Operations*/
    private void getOfflineDbForm(String templateID) {
        new AsyncTask<Void, Void, DigitalFormModel>() {
            @Override
            protected DigitalFormModel doInBackground(Void... voids) {
                return mDatabase.getDDTemplateForm(mPrefs.getSiteId(), templateID);
            }

            @Override
            protected void onPostExecute(DigitalFormModel templateDatum) {
                super.onPostExecute(templateDatum);
                isLoadingLD.postValue(false);
                if (templateDatum != null) {
                    docHeaderLD.postValue(templateDatum.getDataHeader());
                    digitalFormLD.postValue(templateDatum.getData());
                    rowID.postValue(templateDatum.getDeleteRowID());
                    isStagedLD.postValue(templateDatum.getStaged());
                } else {
                    errorModelLD.postValue(new ErrorModel(DataNames.TYPE_ERROR_API,
                            getApplication().getString(R.string.retrofit_failure)));
                }
            }
        }.execute();
    }

    private void saveValuesToDb(String recType, Integer rowID, String assigned_user, String myItem, String templateID, Integer customDocumentId, Integer projectDocumentId, List<LocalMetaValues> metaData) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                try {
                    return mDatabase.saveDigitalDocumentValues(mPrefs.getSiteId(), mPrefs.getUserId(), assigned_user,
                            mPrefs.getProjectId(), myItem, templateID, customDocumentId, projectDocumentId, new Gson().toJson(metaData));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isLoadingLD.postValue(false);
                if (null != aLong) {
                    if (null != rowID) {
                        if (!recType.isEmpty() && !recType.equals("0")) {
                            mDatabase.updateCompleteDate(mPrefs.getSiteId(), mPrefs.getUserId(), rowID);
                        } else {
                            mDatabase.deleteDDTemplateRow(rowID);
                        }
                    }
                    errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "Document Submitted successfully"));
                    globalSyncService();
                    submitFormLD.postValue(true);
                }
            }
        }.execute();
    }

    private void updateDocumentReccerance(String recType, String templateId) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    return mDatabase.getMyItemRowId(mPrefs.getSiteId(), mPrefs.getProjectId(), templateId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String columnId) {
                super.onPostExecute(columnId);
                if (null != columnId) {
                    if (!recType.isEmpty() && !recType.equals("0")) {
                        mDatabase.updateCompleteDate(mPrefs.getSiteId(), mPrefs.getUserId(), Integer.parseInt(columnId));
                    } else {
                        mDatabase.deleteDDTemplateRow(Integer.parseInt(columnId));
                    }
                }
            }
        }.execute();
    }

    /* get synced offline template list */
    private void updateFromDatabase(String category) {
        new AsyncTask<Void, Void, List<DigitalDoc>>() {
            @Override
            protected List<DigitalDoc> doInBackground(Void... voids) {
                try {
                    return mDatabase.getOfflineDiDocListByCategory(mPrefs.getSiteId(),
                            mPrefs.getUserId(), category);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<DigitalDoc> digitalDocs) {
                super.onPostExecute(digitalDocs);
                isDocumentAdapterLD.postValue(true);
                isRefreshingLD.postValue(false);
                isLoadingLD.postValue(false);
                docsLD.postValue(new DigiDocLiveModel(true,
                        digitalDocs == null ? new ArrayList<>() : digitalDocs));
            }
        }.execute();
    }

    /* sync document template to database*/
    private void syncToDatabase(String categoryId, String templateId, String templateName, DigitalFormModel templateData, String recurringType) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                try {
                    return mDatabase.insertDigitalDocument(mPrefs.getSiteId(), mPrefs.getUserId(),
                            mPrefs.getProjectId(), templateId, templateName, recurringType,
                            new Gson().toJson(templateData), categoryId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isLoadingLD.postValue(false);
                notifyAdapterLD.postValue(true);
                errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, aLong != null ?
                        "Document synced successfully" : "Operation failed"));
            }
        }.execute();

    }

    private void getOfflineMyItems(String categoryId, String issuedBy, String catSection, String companyProjectCatId) {
        new AsyncTask<Void, Void, List<PojoMyItem>>() {
            @Override
            protected List<PojoMyItem> doInBackground(Void... voids) {
                return mDatabase.getMyItemsDb(mPrefs.getSiteId(), mPrefs.getUserId(),
                        mPrefs.getProjectId(), categoryId, issuedBy, catSection, companyProjectCatId);
            }

            @Override
            protected void onPostExecute(List<PojoMyItem> pojoMyItems) {
                super.onPostExecute(pojoMyItems);
                isDocumentAdapterLD.postValue(false);
                isLoadingLD.postValue(false);
                isRefreshingLD.postValue(false);
                myItemsLD.postValue(new MyItemLiveDataModel(
                        pojoMyItems == null ? new ArrayList<>() : pojoMyItems, true));

            }
        }.execute();
    }

    public void syncMyItemToDb(PojoMyItem myItem, String catSection, String categoryId) {
        isLoadingLD.postValue(true);
        RestClient.createService().apiGetDigitalDocForm(myItem.getTemplateId(), 0,
                Integer.parseInt(myItem.getDoc_id() != null ? myItem.getDoc_id() : "0"),
                myItem.getIssue() != null ? myItem.getIssue() : "", mPrefs.getProjectId()
        ).enqueue(new Callback<DigitalFormModel>() {
            @Override
            public void onResponse(@NonNull Call<DigitalFormModel> call, @NonNull Response<DigitalFormModel> response) {
                try {
                    if (ErrorCodes.checkCode(response.code()) && response.body() != null) {
                        syncMyItemToDatabase(myItem, response.body(), catSection, categoryId);
                    } else {
                        isLoadingLD.postValue(false);
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DigitalFormModel> call, @NonNull Throwable t) {
                isLoadingLD.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    private void syncMyItemToDatabase(PojoMyItem myItem, DigitalFormModel model, String catSection, String categoryId) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                return mDatabase.insertMyItem(mPrefs.getSiteId(), mPrefs.getUserId(),
                        mPrefs.getProjectId(), myItem, new Gson().toJson(model), catSection, categoryId);
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isLoadingLD.postValue(false);
                notifyAdapterLD.postValue(true);
            }
        }.execute();

    }

    /**
     * Retrieve meta values
     */
    public List<LocalMetaValues> getMetaValuesList(List<TemplateData> dataList, PojoMyItem myItem) {
        List<LocalMetaValues> valuesList = new ArrayList<>();
        /** initialization of the Row index and set previous value is empty*/
        int index = 0;
        String previous_value = "";
        /** Row loop*/
        for (TemplateData templateData : dataList) {
            Log.e("datalist", new Gson().toJson(dataList));
            if (previous_value != null && !previous_value.isEmpty() && previous_value.equalsIgnoreCase(templateData.getRowData().getId())) {
                index = ++index;
            } else {
                index = 0;
            }
            /** Columns loop*/
            for (RowFormModel model : templateData.getFormModelList()) {
                switch (model.getType()) {
                    case "text_filler":
                    case "textarea":
                    case "text":
                    case "date":
                    case "date_picker":
                    case "today_date":
                    case "today_date_time":
                    case "multi_drop_text":
                    case "project_name":
                    case "video":
                        valuesList.add(new LocalMetaValues(model.getRowID() + "_" + index + "_" + model.getColumnID(), model.getAnswerString().isEmpty() ? model.getValue() : model.getAnswerString(), model.getType()));
                        break;
                    case "client":
                    case "project_id":
                    case "loggedin_user":
                    case "autoincrement":
                    case "project_address":
                        valuesList.add(new LocalMetaValues(model.getRowID() + "_" + index + "_" + model.getColumnID(), model.getValue().isEmpty() ? model.getAnswerString() : model.getValue(), model.getType()));
                        break;
                    case "checkbox":
                        if (model.getOptions() != null && !model.getOptions().isEmpty()) {
                            valuesList.add(new LocalMetaValues(model.getRowID() + "_" + index + "_" + model.getColumnID(), getMultipleOptionsString(model.getOptions()), model.getType()));
                        }
                        break;
                    case "dropdown":
                    case "multiplication-dropdown":
                        if (model.getOptions() != null && !model.getOptions().isEmpty()) {
                            valuesList.add(new LocalMetaValues(model.getRowID() + "_" + index + "_" + model.getColumnID(), getOptionsStringId(model.getOptions()), model.getType()));
                        }
                        break;
                    case "select":
                    case "radio":
                        if (model.getOptions() != null && !model.getOptions().isEmpty()) {
                            valuesList.add(new LocalMetaValues(model.getRowID() + "_" + index + "_" + model.getColumnID(), getOptionsString(model.getOptions()), model.getType()));
                        }
                        break;
                    case "file":
                    case "photo_gallery":
                        LocalMetaValues fileValues = new LocalMetaValues(model.getRowID() + "_" + index + "_" + model.getColumnID(), (model.getMetaUploadFiles() != null && !model.getMetaUploadFiles().isEmpty()) ? model.getMetaUploadFiles().get(0) : model.getValue(), model.getType());
                        fileValues.setAttachment(model.getMetaUploadFiles() != null && !model.getMetaUploadFiles().isEmpty());
                        valuesList.add(fileValues);
                        break;
                    case "signature":
                        LocalMetaValues signValues = new LocalMetaValues(model.getRowID() + "_" + index + "_" + model.getColumnID(), model.getImageFile() != null ? model.getImageFile().getAbsolutePath() : model.getValue(), model.getType());
                        signValues.setAttachment(model.getImageFile() != null);
                        valuesList.add(signValues);
                        break;
                    case "input_markup_file":
                        LocalMetaValues markUpValues = new LocalMetaValues(model.getRowID() + "_" + index + "_" + model.getColumnID(), model.getAnswerString().isEmpty() ? model.getValue() : model.getAnswerString(), model.getType());
                        markUpValues.setAttachment(!model.getAnswerString().isEmpty());
                        valuesList.add(markUpValues);
                        break;
                    case "input_file":
                    case "input":
                    case "Blank_Filler":
                        break;
                }
                previous_value = model.getRowID();
            }
        }
        return valuesList;
    }

    /* get options list */
    private String getMultipleOptionsString(List<MetaOptions> options) {
        List<String> optionsList = new ArrayList<>();
        for (MetaOptions option : options) {
            if (option.isSelected()) {
                optionsList.add(option.getId().isEmpty() ? option.getOptionName() : option.getId());
            }
        }
        return new Gson().toJson(optionsList);
    }

    /* get Single option */
    private String getOptionsString(List<MetaOptions> options) {
        for (MetaOptions option : options) {
            if (option.isSelected()) {
                return option.getOptionName();
            }
        }
        return "";
    }

    private String getOptionsStringId(List<MetaOptions> options) {
        for (MetaOptions option : options) {
            if (option.isSelected()) {
                return option.getId().equalsIgnoreCase("") ? option.getOptionName() : option.getId();
            }
        }
        return "";
    }

    public boolean isProperFieldMain(List<TemplateData> dataList) {
        boolean isProperField = false;
        for (TemplateData data : dataList) {
            if (isProperFilled(data.getFormModelList())) {
                isProperField = true;
            } else {
                isProperField = false;
                break;
            }
        }
        return isProperField;
    }

    private Boolean isProperFilled(List<RowFormModel> formModelList) {
        for (RowFormModel model : formModelList) {
            if (model.getIsRequired() != null && model.getIsRequired().equals("1") && model.isEditableColumn()) {
                switch (model.getType()) {
                    case "textarea":
                    case "text":
                    case "date":
                    case "date_picker":
                        if (model.getAnswerString().isEmpty()) {
                            errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, model.getLabel() + " must not be empty."));
                            return false;
                        }
                        break;
                    case "select":
                    case "checkbox":
                    case "radio":
                        if (isOptionUnSelected(model.getOptions())) {
                            errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, model.getLabel() + " must not be empty."));
                            return false;
                        }
                        break;
                    case "signature":
                        if (model.getImageFile() == null) {
                            errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, model.getLabel() + " must not be empty."));
                            return false;
                        }
                        break;
                }
            }
        }
        return true;
    }

    private boolean isOptionUnSelected(List<MetaOptions> options) {
        for (MetaOptions option : options) {
            if (option.isSelected()) {
                return false;
            }
        }
        return true;
    }

    private void updateUsersToDatabase(List<User> users, String section) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                if (null != mDatabase.getUserDataBySection(mPrefs.getSiteId(), mPrefs.getUserId(), section)) {
                    return mDatabase.updateUsersList(mPrefs.getSiteId(), mPrefs.getUserId(),
                            section, new Gson().toJson(users));
                } else {
                    return mDatabase.insertUser(mPrefs.getSiteId(), mPrefs.getUserId(),
                            mPrefs.getProjectId(), section, new Gson().toJson(users));
                }
            }
        }.execute();
    }

    private void getUsersFromDB() {
        List<User> allUserList = new ArrayList<>();
        try {
            Users userResponse = mDatabase.getUserDataBySection(mPrefs.getSiteId(), mPrefs.getUserId(), DataNames.DIGI_DOCS_USERS);
            if (userResponse != null && userResponse.getUserData() != null && !userResponse.getUserData().isEmpty()) {
                allUserList = new Gson().fromJson(userResponse.getUserData(), new TypeToken<List<User>>() {
                }.getType());
            } else {
                Users commonResponse = mDatabase.getUserDataBySection(mPrefs.getSiteId(), mPrefs.getUserId(), "");
                if (commonResponse != null && commonResponse.getUserData() != null && !commonResponse.getUserData().isEmpty()) {
                    allUserList = new Gson().fromJson(commonResponse.getUserData(), new TypeToken<List<User>>() {
                    }.getType());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        usersLD.postValue(allUserList);
    }

    private void addCategoriesToDatabase(List<CatListing> category, String section) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                if (null != mDatabase.getCategoryBySection(mPrefs.getSiteId(), mPrefs.getUserId(), section)) {
                    return mDatabase.updateCategoryList(mPrefs.getSiteId(), mPrefs.getUserId(), section, new Gson().toJson(category));
                } else {
                    return mDatabase.insertCategory(mPrefs.getSiteId(), mPrefs.getUserId(), section, new Gson().toJson(category));
                }
            }
        }.execute();
    }

    private void getOfflineCategories(String section) {
        List<CatListing> catList = new ArrayList<>();
        try {
            Categories response = mDatabase.getCategoryBySection(mPrefs.getSiteId(), mPrefs.getUserId(), section);
            if (response != null && response.getItemData() != null && !response.getItemData().isEmpty()) {
                catList = new Gson().fromJson(response.getItemData(), new TypeToken<List<CatListing>>() {
                }.getType());
            } else {
                Categories commonResponse = mDatabase.getAllCategory(mPrefs.getSiteId(), mPrefs.getUserId());
                if (commonResponse != null && commonResponse.getItemData() != null && !commonResponse.getItemData().isEmpty()) {
                    catList = new Gson().fromJson(commonResponse.getItemData(), new TypeToken<List<CatListing>>() {
                    }.getType());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        isLoadingLD.postValue(false);
        categoriesLD.postValue(catList);
    }
}


//    public List<LocalMetaValues> getMetaValuesList(List<TemplateData> dataList, PojoMyItem myItem) {
//        List<LocalMetaValues> valuesList = new ArrayList<>();
//        /** initialization of the Row index and set previous value is empty*/
//        int index = 0;
//        String previous_value = "";
//        /** Row loop*/
//        for (TemplateData templateData : dataList) {
//            Log.e("datalist", new Gson().toJson(dataList));
//            if (previous_value != null && !previous_value.isEmpty() && previous_value.equalsIgnoreCase(templateData.getRowData().getId())) {
//                index = ++index;
//            } else {
//                index = 0;
//            }
//            /** Columns loop*/
//            for (RowFormModel model : templateData.getFormModelList()) {
//                switch (model.getType()) {
//                    case "text_filler":
//                        valuesList.add(new LocalMetaValues(model.getRowID() + "_" + index + "_" + model.getColumnID(), model.getValue(), null, "", model.getType()));
//                        break;
//                    case "textarea":
//                    case "text":
//                    case "date":
//                    case "date_picker":
//                    case "today_date":
//                    case "project_id":
//                    case "loggedin_user":
//                    case "today_date_time":
//                    case "multi_drop_text":
//                    case "project_address":
//                    case "autoincrement":
//                    case "client":
//                    case "project_name":
//                        if (!model.getAnswerString().isEmpty()) {
//                            valuesList.add(new LocalMetaValues(model.getRowID() + "_" + index + "_" + model.getColumnID(), model.getAnswerString(), null, !model.getColorCode().isEmpty() ? model.getColorCode() : "", model.getType()));
//                        }
//                        break;
//                    case "checkbox":
//                        if (model.getOptions() != null && !model.getOptions().isEmpty()) {
//                            valuesList.add(new LocalMetaValues(model.getRowID() + "_" + index + "_" + model.getColumnID(), getMultipleOptionsString(model.getOptions()), null, "", model.getType()));
//                        }
//                        break;
//                    case "dropdown":
//                    case "multiplication-dropdown":
//                        if (model.getOptions() != null && !model.getOptions().isEmpty()) {
//                            valuesList.add(new LocalMetaValues(model.getRowID() + "_" + index + "_" + model.getColumnID(), getOptionsStringId(model.getOptions()), null, "", model.getType()));
//                        }
//                        break;
//                    case "select":
//                    case "radio":
//                        if (model.getOptions() != null && !model.getOptions().isEmpty()) {
//                            valuesList.add(new LocalMetaValues(model.getRowID() + "_" + index + "_" + model.getColumnID(), getOptionsString(model.getOptions()), null, "", model.getType()));
//                        }
//                        break;
//                    case "file":
//                    case "photo_gallery":
//                        if (model.getMetaUploadFiles() != null && !model.getMetaUploadFiles().isEmpty()) {
//                            LocalMetaValues values = new LocalMetaValues(model.getRowID() + "_" + index + "_" + model.getColumnID(), model.getMetaUploadFiles().get(0), null, "", model.getType());
//                            values.setAttachment(true);
//                            values.setContainFile(true);
//                            valuesList.add(values);
//                        } else {
////                            LocalMetaValues values = new LocalMetaValues(model.getRowID() + "_" + index + "_" + model.getColumnID(), model.getAnswerString(), null, model.getType().equalsIgnoreCase("file") ? "old_file" : "old_photo_gallery");
//                            LocalMetaValues values = new LocalMetaValues(model.getRowID() + "_" + index + "_" + model.getColumnID(), model.getAnswerString(), model.getValue(), model.getType());
//                            values.setAttachment(false);
//                            values.setContainFile(true);
//                            valuesList.add(values);
//                        }
//                        break;
//                    case "signature":
//                        if (model.getImageFile() != null) {
//                            LocalMetaValues values = new LocalMetaValues(model.getRowID() + "_" + index + "_" + model.getColumnID(), model.getImageFile().getAbsolutePath(), null, model.getType());
//                            values.setAttachment(true);
//                            values.setContainFile(true);
//                            valuesList.add(values);
//                        } else {
////                            LocalMetaValues values = new LocalMetaValues(model.getRowID() + "_" + index + "_" + model.getColumnID(), model.getAnswerString(), null, "old_signature");
//                            LocalMetaValues values = new LocalMetaValues(model.getRowID() + "_" + index + "_" + model.getColumnID(), model.getAnswerString(), null, model.getType());
//                            values.setAttachment(false);
//                            values.setContainFile(true);
//                            valuesList.add(values);
//                        }
//                        break;
//                    case "input_markup_file":
//                        LocalMetaValues values = new LocalMetaValues(model.getRowID() + "_" + index + "_" + model.getColumnID(), model.getAnswerString().isEmpty() ? "" : model.getAnswerString(), model.getValue(), model.getType());
//                        values.setAttachment(!model.getAnswerString().isEmpty());
//                        values.setContainFile(true);
//                        valuesList.add(values);
//                        break;
//                    case "video":
//                        valuesList.add(new LocalMetaValues(model.getRowID() + "_" + index + "_" + model.getColumnID(), model.getAnswerString(), model.getValue(), model.getType()));
//                        break;
//                    case "input_file":
//                    case "input":
//                    case "Blank_Filler":
//                        break;
//
//                }
//                previous_value = model.getRowID();
//            }
//        }
//        return valuesList;
//    }


