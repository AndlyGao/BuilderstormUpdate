package com.builderstrom.user.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.builderstrom.user.repository.database.databaseModels.DiaryMetaDataDb;
import com.builderstrom.user.repository.database.databaseModels.Drawings;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.api.ErrorCodes;
import com.builderstrom.user.repository.retrofit.api.RestClient;
import com.builderstrom.user.repository.retrofit.modals.AddDrawingModel;
import com.builderstrom.user.repository.retrofit.modals.Datum;
import com.builderstrom.user.repository.retrofit.modals.DiaryMetaDataModel;
import com.builderstrom.user.repository.retrofit.modals.DrawingCommentModel;
import com.builderstrom.user.repository.retrofit.modals.DrawingModel;
import com.builderstrom.user.repository.retrofit.modals.ErrorModel;
import com.builderstrom.user.repository.retrofit.modals.LocalMetaValues;
import com.builderstrom.user.repository.retrofit.modals.MetaDataField;
import com.builderstrom.user.repository.retrofit.modals.MoreMenuModel;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.builderstrom.user.utils.BuilderStormApplication.mLocalDatabase;

@SuppressLint("StaticFieldLeak")
public class DrawingMenuVM extends BaseViewModel {
    public MutableLiveData<Boolean> isSuccess;
    public MutableLiveData<Boolean> isOffline;
    public MutableLiveData<List<DrawingCommentModel.Comment>> commentLiveData;
    public MutableLiveData<List<Datum>> drawingLiveData;
    public MutableLiveData<Integer> offsetLD;
    public MutableLiveData<List<MetaDataField>> metaDataList;
    public MutableLiveData<Boolean> allSyncedStatusLd;
    public MutableLiveData<Boolean> notfyAdaptorLD;
    public MutableLiveData<Boolean> isSyncingLD;
    private List<String> offlineRowList = new ArrayList<>();

    public DrawingMenuVM(@NonNull Application application) {
        super(application);
        isSuccess = new MutableLiveData<>();
        isOffline = new MutableLiveData<>();
        commentLiveData = new MutableLiveData<>();
        drawingLiveData = new MutableLiveData<>();
        offsetLD = new MutableLiveData<>();
        metaDataList = new MutableLiveData<>();
        allSyncedStatusLd = new MutableLiveData<>();
        notfyAdaptorLD = new MutableLiveData<>();
        isSyncingLD = new MutableLiveData<>();
    }

    public void getAllDrawings(Integer offset, Integer limit) {
        isLoadingLD.postValue(true);
        updateSyncedList();
        RestClient.createService().getDrawing(mPrefs.getProjectId(), offset, limit).enqueue(new Callback<DrawingModel>() {
            @Override
            public void onResponse(@NonNull Call<DrawingModel> call, @NonNull Response<DrawingModel> response) {
                isLoadingLD.postValue(false);
                try {
                    setLogs(DrawingMenuVM.class.getName(), "getAllDrawings", new Gson().toJson(response));
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        isOffline.postValue(false);
                        offsetLD.postValue(response.body().getDatalimit().getOffset());
                        List<Datum> dataList = new ArrayList<>();
                        if (null != response.body().getData()) {
                            for (Datum data : response.body().getData()) {
                                if (offlineRowList.contains(data.getId())) {
                                    updateDrawingDataBase(data);
                                    data.setSync(true);
                                }
                                if (data.getQuarantine() != null && data.getQuarantine().equalsIgnoreCase("0") && data.getArchiveStatus() == 0) {
                                    dataList.add(data);
                                }
                            }
                            drawingLiveData.postValue(dataList);
                        }
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DrawingModel> call, @NonNull Throwable t) {
                setLogs(DrawingMenuVM.class.getName(), "getAllDrawings", t.getLocalizedMessage());
                getOfflineDrawings();
               /* if (CommonMethods.isNetworkError(t)) {
                    getOfflineDrawings();
                } else {
                    isLoadingLD.postValue(false);
                    errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
                }*/
            }
        });
    }

    private void updateDrawingDataBase(Datum data) {
        if (CommonMethods.downloadFile(getApplication())) {
            try {
                if (null != data) {
                    if (null != data.getPdflocation() && !data.getPdflocation().isEmpty()) {
                        downloadFile(data.getPdflocation(), data.getPdfname(), false);
                    }
                    if (null != data.getDwglocation() && !data.getDwglocation().isEmpty()) {
                        downloadFile(data.getDwglocation(), data.getDwgname(), false);
                    }
                    updateDrawingToDb(data.getId(), new Gson().toJson(data));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateSyncedList() {
        offlineRowList.clear();
        offlineRowList.addAll(mLocalDatabase.getSyncedDrawings(mPrefs.getSiteId(), mPrefs.getUserId(), mPrefs.getProjectId()));
    }

    public void getMetaData() {
        isLoadingLD.postValue(true);
        RestClient.createService().getDiaryAPIMetaData(mPrefs.getProjectId(), DataNames.META_DRAWING_SECTION).enqueue(
                new Callback<DiaryMetaDataModel>() {
                    @Override
                    public void onResponse(@NonNull Call<DiaryMetaDataModel> call, @NonNull Response<DiaryMetaDataModel> response) {
                        try {
                            setLogs(DrawingMenuVM.class.getName(), "Add getMetaDataDrawing API", new Gson().toJson(response));
                            if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                                insertMetaData(response.body().getFields());
                            } else {
                                isLoadingLD.postValue(false);
                                handleErrorBody(response.errorBody());
                            }
                        } catch (Exception e) {
                            errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<DiaryMetaDataModel> call, @NonNull Throwable t) {
                        setLogs(DrawingMenuVM.class.getName(), "getMetaDataDrawing offline", t.getLocalizedMessage());
                        updateFromDataBase();
                        /*if (CommonMethods.isNetworkError(t)) {
                            updateFromDataBase();
                        } else {
                            isLoadingLD.postValue(false);
                            errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
                        }*/
                    }
                });
    }

    public void getDrawingCommentsAPI(String drawingId) {
        isLoadingLD.postValue(true);
        RestClient.createService().getDrawingComments(drawingId).enqueue(new Callback<DrawingCommentModel>() {
            @Override
            public void onResponse(@NonNull Call<DrawingCommentModel> call, @NonNull Response<DrawingCommentModel> response) {
                try {
                    setLogs(DrawingMenuVM.class.getName(), "GetDrawingComments", new Gson().toJson(response));
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        commentLiveData.postValue(response.body().getComments());
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
                isLoadingLD.postValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<DrawingCommentModel> call, @NonNull Throwable t) {
                setLogs(DrawingMenuVM.class.getName(), "GetDrawingComments", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void callMoreMenuAPI(String drawingAction, String drawingId, String status) {
        isLoadingLD.postValue(true);
        RestClient.createService().drawingAction(mPrefs.getProjectId(), drawingId, drawingAction, status).enqueue(new Callback<MoreMenuModel>() {
            @Override
            public void onResponse(@NonNull Call<MoreMenuModel> call, @NonNull Response<MoreMenuModel> response) {
                isLoadingLD.postValue(false);
                try {
                    setLogs(DrawingMenuVM.class.getName(), "callMoreMenuAPI", new Gson().toJson(response));
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        isSuccess.setValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoreMenuModel> call, @NonNull Throwable t) {
                setLogs(DrawingMenuVM.class.getName(), "callMoreMenuAPI", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void addCommentAPI(String drawingId, String title) {
        isLoadingLD.postValue(true);
        RestClient.createService().addDrawingComment(drawingId, title).enqueue(new Callback<MoreMenuModel>() {
            @Override
            public void onResponse(@NonNull Call<MoreMenuModel> call, @NonNull Response<MoreMenuModel> response) {
                try {
                    setLogs(DrawingMenuVM.class.getName(), "callMoreMenuAPI", new Gson().toJson(response));
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        isSuccess.setValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
                isLoadingLD.postValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<MoreMenuModel> call, @NonNull Throwable t) {
                setLogs(DrawingMenuVM.class.getName(), "callMoreMenuAPI", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void addDrawing(Datum drawingData) {
        isLoadingLD.postValue(true);
        List<MultipartBody.Part> metaFiles = new ArrayList<>();
        for (LocalMetaValues values : drawingData.getMetaData()) {
            if (values.getAttachment()) {
                metaFiles.add(CommonMethods.createPartFromFile(String.format("custom_doc[%s]", values.getCustomFieldId()), values.getCustomFieldValue()));
            }
        }

        RestClient.createService().addDrawingAPI(mPrefs.getProjectId(),
                CommonMethods.createPartFromString(drawingData.getName()),
                CommonMethods.createPartFromString(drawingData.getDrawingNo()),
                CommonMethods.createPartFromString(drawingData.getRevision()),
                CommonMethods.isFileNullOrEmpty(drawingData.getPdflocation()) ? null : CommonMethods.createPartFromFile("drawing_pdf", drawingData.getPdflocation()),
                CommonMethods.isFileNullOrEmpty(drawingData.getDwglocation()) ? null : CommonMethods.createPartFromFile("drawing_dwg", drawingData.getDwglocation()),
                CommonMethods.isFileNullOrEmpty(drawingData.getOftlocation()) ? null : CommonMethods.createPartFromFile("other_fileDraw", drawingData.getOftlocation()),
                CommonMethods.createPartFromString(CommonMethods.filteredMetaDataString(drawingData.getMetaData())),
                metaFiles).enqueue(new Callback<AddDrawingModel>() {
            @Override
            public void onResponse(@NonNull Call<AddDrawingModel> call, @NonNull Response<AddDrawingModel> response) {
                try {
                    setLogs(DrawingMenuVM.class.getName(), "Add drawing API", new Gson().toJson(response));
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        isSuccess.setValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
                isLoadingLD.postValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<AddDrawingModel> call, @NonNull Throwable t) {
                setLogs(DrawingMenuVM.class.getName(), "Add Drawing offline", t.getLocalizedMessage());
                addDrawingDb(drawingData);
                        /*if (CommonMethods.isNetworkError(t)) {
                            addDrawingDb(drawingData);
                        } else {
                            isLoadingLD.postValue(false);
                            errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
                        }*/
            }
        });
    }

    /* DataBase Operations*/
    private void addDrawingDb(Datum drawingData) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                try {
                    return mDatabase.insertDrawing(mPrefs.getSiteId(), mPrefs.getUserId(), mPrefs.getProjectId(), "0", 0,
                            new Gson().toJson(drawingData));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isLoadingLD.postValue(false);
                isSuccess.setValue(true);
                globalSyncService(); /* for global syncing of all periodically */
            }
        }.execute();
    }

    public void updateDrawingToDb(String rowID, String drawing) {
        new AsyncTask<Void, Void, Long>() {

            @Override
            protected Long doInBackground(Void... voids) {
                try {
                    return mDatabase.updateDrawing(mPrefs.getSiteId(), mPrefs.getUserId(), mPrefs.getProjectId(), rowID, 1, drawing);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();


    }

    private void insertMetaData(List<MetaDataField> fields) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                if (null != mDatabase.getMetaDataBySection(mPrefs.getSiteId(),
                        mPrefs.getProjectId(), DataNames.META_DRAWING_SECTION)) {
                    return mDatabase.updateMetaDataBySection(mPrefs.getSiteId(),
                            mPrefs.getProjectId(), DataNames.META_DRAWING_SECTION,
                            new Gson().toJson(fields));
                } else {
                    return mDatabase.insertMetaDataBySection(mPrefs.getSiteId(),
                            mPrefs.getUserId(), mPrefs.getProjectId(),
                            mPrefs.getUserName(), DataNames.META_DRAWING_SECTION, new Gson().toJson(fields));
                }
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                updateFromDataBase();
            }
        }.execute();
    }

    private void getOfflineDrawings() {
        new AsyncTask<Void, Void, List<Drawings>>() {
            @Override
            protected List<Drawings> doInBackground(Void... voids) {
                return mDatabase.getDrawings(mPrefs.getSiteId(), mPrefs.getUserId(), mPrefs.getProjectId());
            }

            @Override
            protected void onPostExecute(List<Drawings> drawings) {
                super.onPostExecute(drawings);
                List<Datum> datumList = new ArrayList<>();

                if (drawings != null) {
                    for (Drawings drawing : drawings) {
                        Datum itemModel = new Gson().fromJson(drawing.getDrawingItem(), new TypeToken<Datum>() {
                        }.getType());

                        itemModel.setSync(drawing.getSync());
                        if (itemModel.getQuarantine() != null && itemModel.getQuarantine().equalsIgnoreCase("0") && itemModel.getArchiveStatus() == 0) {
                            datumList.add(itemModel);
                        }
                    }
                }
                isOffline.postValue(true);
                isLoadingLD.postValue(false);
                drawingLiveData.postValue(datumList);
            }
        }.execute();
    }

    private void updateFromDataBase() {
        DiaryMetaDataDb dbMetaData = mDatabase.getMetaDataBySection(mPrefs.getSiteId(),
                mPrefs.getProjectId(), DataNames.META_DRAWING_SECTION);
        List<MetaDataField> localList;
        if (null != dbMetaData) {
            localList = new Gson().fromJson(dbMetaData.getMetaData(), new TypeToken<List<MetaDataField>>() {
            }.getType());
        } else localList = new ArrayList<>();
        isLoadingLD.postValue(false);
        metaDataList.postValue(localList);
    }

    public void updateAllSyncedStatus(List<Datum> drawingList) {
        boolean allSynced = !drawingList.isEmpty();
        for (Datum data : drawingList) {
            if (!drawingList.contains(data.getId())) {
                allSynced = false;
                break;
            }
        }
        allSyncedStatusLd.postValue(allSynced);

    }


    public void syncDrawingToDB(Datum drawingItem) {
        drawingItem.setSync(true);
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                isSyncingLD.postValue(true);
            }

            @Override
            protected Long doInBackground(Void... voids) {

                try {
                    if (null != drawingItem.getPdflocation() && !drawingItem.getPdflocation().isEmpty()) {
                        downloadFile(drawingItem.getPdflocation(), drawingItem.getPdfname(), false);
                    }
                    if (null != drawingItem.getDwglocation() && !drawingItem.getDwglocation().isEmpty()) {
                        downloadFile(drawingItem.getDwglocation(), drawingItem.getDwgname(), false);
                    }
                    return mDatabase.insertDrawing(mPrefs.getSiteId(), mPrefs.getUserId(), mPrefs.getProjectId(), drawingItem.getId(), 1, new Gson().toJson(drawingItem));
                } catch (Exception e) {
                    e.printStackTrace();

                }
                return null;
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isSyncingLD.postValue(false);
                notfyAdaptorLD.postValue(aLong != null);
            }
        }.execute();

    }
}
