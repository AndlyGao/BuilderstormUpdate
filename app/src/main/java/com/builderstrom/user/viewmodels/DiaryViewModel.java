package com.builderstrom.user.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.builderstrom.user.repository.database.databaseModels.Diary;
import com.builderstrom.user.repository.database.databaseModels.DiaryMetaDataDb;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.api.ErrorCodes;
import com.builderstrom.user.repository.retrofit.api.RestClient;
import com.builderstrom.user.repository.retrofit.modals.AddAttachModel;
import com.builderstrom.user.repository.retrofit.modals.AddDiaryModel;
import com.builderstrom.user.repository.retrofit.modals.DiaryAPIModel;
import com.builderstrom.user.repository.retrofit.modals.DiaryData;
import com.builderstrom.user.repository.retrofit.modals.DiaryLabourModel;
import com.builderstrom.user.repository.retrofit.modals.DiaryLiveModel;
import com.builderstrom.user.repository.retrofit.modals.DiaryManLabour;
import com.builderstrom.user.repository.retrofit.modals.DiaryMetaDataModel;
import com.builderstrom.user.repository.retrofit.modals.ErrorModel;
import com.builderstrom.user.repository.retrofit.modals.LabourModel;
import com.builderstrom.user.repository.retrofit.modals.LocalMetaValues;
import com.builderstrom.user.repository.retrofit.modals.MetaDataField;
import com.builderstrom.user.repository.retrofit.modals.PojoManHourModel;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.views.activities.AddDiary;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiaryViewModel extends BaseViewModel {

    public MutableLiveData<Boolean> isSuccess;
    public MutableLiveData<Boolean> isOffline;
    public MutableLiveData<Integer> offsetLD;
    public MutableLiveData<String> dialogLD;
    public MutableLiveData<DiaryLiveModel> diaryLiveData;
    public MutableLiveData<List<MetaDataField>> metaLiveData;
    public MutableLiveData<List<DiaryManLabour>> manHoursLD;

    public DiaryViewModel(@NonNull Application application) {
        super(application);
        isSuccess = new MutableLiveData<>();
        isOffline = new MutableLiveData<>();
        offsetLD = new MutableLiveData<>();
        dialogLD = new MutableLiveData<>();
        diaryLiveData = new MutableLiveData<>();
        metaLiveData = new MutableLiveData<>();
        manHoursLD = new MutableLiveData<>();
    }

    public void getAllDiaries(String date, Integer offset, Integer limit) {
        isLoadingLD.postValue(true);
        RestClient.createService().getDiaryAPI(mPrefs.getProjectId(), date, offset, limit).enqueue(new Callback<DiaryAPIModel>() {
            @Override
            public void onResponse(@NonNull Call<DiaryAPIModel> call, @NonNull Response<DiaryAPIModel> response) {
                isLoadingLD.postValue(false);
                try {
                    setLogs(DiaryViewModel.class.getName(), "getAllDiaries", new Gson().toJson(response));
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        offsetLD.postValue(response.body().getDatalimit().getOffset());
                        diaryLiveData.postValue(new DiaryLiveModel(response.body().getData(), false));
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DiaryAPIModel> call, @NonNull Throwable t) {
                setLogs(DiaryViewModel.class.getName(), "getAllDiaries", t.getLocalizedMessage());
                getOfflineDiaries(CommonMethods.convertDate(CommonMethods.DF_2, date, CommonMethods.DF_8));
               /* if (CommonMethods.isNetworkError(t)) {
                    getOfflineDiaries(CommonMethods.convertDate(CommonMethods.DF_2, date, CommonMethods.DF_8));
                } else {
                    isLoadingLD.postValue(false);
                    errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
                }*/

            }
        });
    }

    public void deleteDiaryAPI(String diaryID) {
        isLoadingLD.postValue(true);
        RestClient.createService().deleteDiaryAPI(mPrefs.getProjectId(), diaryID, null).enqueue(new Callback<AddDiaryModel>() {
            @Override
            public void onResponse(@NonNull Call<AddDiaryModel> call, @NonNull Response<AddDiaryModel> response) {
                isLoadingLD.postValue(false);
                try {
                    setLogs(DiaryViewModel.class.getName(), "Delete diary", new Gson().toJson(response));
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        isSuccess.postValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddDiaryModel> call, @NonNull Throwable t) {
                setLogs(DiaryViewModel.class.getName(), "Delete diary", t.getLocalizedMessage());
                isLoadingLD.postValue(false);
                errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void addDiary(String diaryId, String title, String description, String date, String submittedTime, List<String> parts,
                         List<LocalMetaValues> metaData, String metaView, List<DiaryLabourModel> labourDataList) {
        List<MultipartBody.Part> filesPart = new ArrayList<>();
        for (LocalMetaValues values : metaData) {
            if (values.getAttachment()) {
                filesPart.add(CommonMethods.createPartFromFile(String.format("custom_doc[%s]", values.getCustomFieldId()), values.getCustomFieldValue()));
            }
        }

        for (String path : parts) {
            filesPart.add(CommonMethods.createPartFromFile("diaryfiles" + parts.indexOf(path), path));
        }

        isLoadingLD.postValue(true);
        RestClient.createService().addDiaryAPI(mPrefs.getProjectId(), /*false,*/
                CommonMethods.createPartFromString("0"),
                CommonMethods.createPartFromString(diaryId),
                CommonMethods.createPartFromString(title),
                CommonMethods.createPartFromString(description),
                CommonMethods.createPartFromString(CommonMethods.convertDate(CommonMethods.DF_8, date, CommonMethods.DF_2)),
                CommonMethods.createPartFromString(""),
                CommonMethods.createPartFromString(""),
                CommonMethods.createPartFromString(CommonMethods.filteredMetaDataString(metaData)),
                CommonMethods.createPartFromString(submittedTime),
                CommonMethods.createPartFromString(labourDataList != null ? new Gson().toJson(labourDataList) : ""),
                filesPart).enqueue(new Callback<AddDiaryModel>() {
            @Override
            public void onResponse(@NonNull Call<AddDiaryModel> call, @NonNull Response<AddDiaryModel> response) {
                isLoadingLD.postValue(false);
                try {
                    setLogs(DiaryViewModel.class.getName(), "addDiary", new Gson().toJson(response));
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        isSuccess.postValue(true);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddDiaryModel> call, @NonNull Throwable t) {
                setLogs(DrawingMenuVM.class.getName(), "addDiary", t.getLocalizedMessage());
                if (getDiariesList(date).isEmpty()) {
                    insertDiaryInDB(title, description, date, new Gson().toJson(parts),
                            new Gson().toJson(metaData), metaView, labourDataList);
                } else {
                    isLoadingLD.postValue(false);
                    dialogLD.postValue(new Gson().toJson(parts));
                }
            }
        });
    }

    public void getDiaryAPIMetaData() {
        isLoadingLD.postValue(true);
        RestClient.createService().getDiaryAPIMetaData(mPrefs.getProjectId(), DataNames.META_DIARY_SECTION).enqueue(
                new Callback<DiaryMetaDataModel>() {
                    @Override
                    public void onResponse(@NonNull Call<DiaryMetaDataModel> call, @NonNull Response<DiaryMetaDataModel> response) {
                        isLoadingLD.postValue(false);
                        try {
                            setLogs(DiaryViewModel.class.getName(), "getDiaryAPIMetaData", new Gson().toJson(response));
                            if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                                insertDiaryMetaData(response.body().getFields());
                            } else {
                                handleErrorBody(response.errorBody());
                            }
                        } catch (Exception e) {
                            errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<DiaryMetaDataModel> call, @NonNull Throwable t) {
                        setLogs(AddDiary.class.getName(), "getDiaryAPIMetaData", t.getLocalizedMessage());
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

    public void getManHoursEntries(@Nullable Integer diaryId) {
        isLoadingLD.postValue(true);
        RestClient.createService().getManHoursEntries(mPrefs.getProjectId(), diaryId).enqueue(new Callback<PojoManHourModel>() {
            @Override
            public void onResponse(@NonNull Call<PojoManHourModel> call,
                                   @NonNull Response<PojoManHourModel> response) {
                isLoadingLD.postValue(false);
                try {
                    setLogs(DiaryViewModel.class.getName(), "get Man Hours", new Gson().toJson(response));
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        manHoursLD.postValue(response.body().getData());
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoManHourModel> call, @NonNull Throwable t) {
                isLoadingLD.postValue(false);
                setLogs(DiaryViewModel.class.getName(), "get Man Hours", t.getLocalizedMessage());

            }
        });
    }

    /* Database operations */
    @SuppressLint("StaticFieldLeak")
    private void getOfflineDiaries(String inputDate) {

        new AsyncTask<Void, Void, List<Diary>>() {
            @Override
            protected List<Diary> doInBackground(Void... voids) {
                return mDatabase.getProjectDailyDiary(mPrefs.getSiteId(), mPrefs.getUserId(), mPrefs.getProjectId(), inputDate);
            }

            @Override
            protected void onPostExecute(List<Diary> diaries) {
                super.onPostExecute(diaries);
                List<DiaryData> modelData = new ArrayList<>();

                if (null != diaries) {
                    for (Diary diary : diaries) {
                        DiaryData mDiaryModel = new DiaryData();
                        mDiaryModel.setId(diary.getId());
                        mDiaryModel.setUsername(diary.getUsername());
                        mDiaryModel.setTitle(diary.getTitle());
                        mDiaryModel.setDescription(diary.getDescription());
                        mDiaryModel.setCreatedOn(diary.getDate());
                        mDiaryModel.setTime(diary.getTime());
                        List<AddAttachModel> attachmentsList = new ArrayList<>();

                        /* saved files */
                        List<String> files = new Gson().fromJson(diary.getDiaryData(), new TypeToken<List<String>>() {
                        }.getType());
                        for (String filePath : files) {
                            AddAttachModel fileModel = new AddAttachModel();
                            String fileName = filePath.substring(filePath.lastIndexOf("/"));
                            if (CommonMethods.isImageUrl(fileName.replace("/", ""))) {
                                if (filePath.isEmpty()) {
                                    try {
                                        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                                        fileModel.setImageBitmap(bitmap);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            fileModel.setFilePath(filePath);
                            fileModel.setName(fileName.replace("/", ""));
                            attachmentsList.add(fileModel);
                        }

                        mDiaryModel.setAttachments(attachmentsList);
                        List<LocalMetaValues> metaData = new ArrayList<>(new Gson().fromJson(diary.getMetaDataView(), new TypeToken<List<LocalMetaValues>>() {
                        }.getType()));
                        mDiaryModel.setLocalMetaData(metaData);

                        /* get Labour data*/
                        List<DiaryManLabour> manLabList = new ArrayList<>();
                        if (diary.getSiteLabor() != null && !diary.getSiteLabor().isEmpty()) {
                            List<DiaryLabourModel> laborList = new ArrayList<>(new Gson().fromJson(diary.getSiteLabor(), new TypeToken<List<DiaryLabourModel>>() {
                            }.getType()));
                            for (DiaryLabourModel lModel : laborList) {
                                DiaryManLabour manLabour = new DiaryManLabour();
                                manLabour.setId(lModel.getId());
                                manLabour.setCostCode(lModel.getCostCode());
                                manLabour.setLabel(lModel.getLabourType());
                                manLabour.setStartTime(lModel.getStartTime());
                                manLabour.setEndTime(lModel.getFinishTime());
                                manLabour.setWorkHours(lModel.getNoSite());
                                manLabour.setPayType(lModel.getPayType());
                                manLabour.setSwh(lModel.getSwh());
                                manLabList.add(manLabour);
                            }
                        }
                        mDiaryModel.setDiarymanhours(manLabList);
                        modelData.add(mDiaryModel);
                    }
                }
                isLoadingLD.postValue(false);
                diaryLiveData.postValue(new DiaryLiveModel(modelData, true));
            }
        }.execute();

    }

    private List<Diary> getDiariesList(String selectedDate) {
        List<Diary> diaries = new ArrayList<>();
        try {
            diaries.addAll(mDatabase.getProjectDailyDiary(mPrefs.getSiteId(), mPrefs.getUserId(), mPrefs.getProjectId(), selectedDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return diaries;
    }

    @SuppressLint("StaticFieldLeak")
    public void insertDiaryInDB(String title, String description, String selectedDate, String offlineListData, String metaValuesList, String metaValuesView, List<DiaryLabourModel> labourDataList) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                try {
                    return mDatabase.insertDiary(mPrefs.getSiteId(), mPrefs.getUserId(), mPrefs.getProjectId(), mPrefs.getUserName(), title, description,
                            CommonMethods.getCurrentDate(CommonMethods.DF_1), selectedDate, metaValuesList, metaValuesView, offlineListData, labourDataList != null ? new Gson().toJson(labourDataList) : "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isLoadingLD.postValue(false);
                globalSyncService();
                isSuccess.postValue(true);
//                CommonMethods.displayToast(getApplication(), String.format("Daily diary has been added for %s", CommonMethods.getCurrentDate(CommonMethods.DF_4)));
                CommonMethods.displayToast(getApplication(), String.format("Daily diary has been added for %s", CommonMethods.convertDate(CommonMethods.DF_8, selectedDate, CommonMethods.DF_4)));
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void insertDiaryMetaData(List<MetaDataField> fields) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                if (null != mDatabase.getMetaDataBySection(mPrefs.getSiteId(), mPrefs.getProjectId(), DataNames.META_DIARY_SECTION)) {
                    return mDatabase.updateMetaDataBySection(mPrefs.getSiteId(), mPrefs.getProjectId(), DataNames.META_DIARY_SECTION, new Gson().toJson(fields));
                } else {
                    return mDatabase.insertMetaDataBySection(mPrefs.getSiteId(),
                            mPrefs.getUserId(), mPrefs.getProjectId(), mPrefs.getUserName(), DataNames.META_DIARY_SECTION, new Gson().toJson(fields));
                }
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                updateFromDataBase();
            }
        }.execute();
    }

    private void updateFromDataBase() {
        DiaryMetaDataDb dbMetaData = mDatabase.getMetaDataBySection(mPrefs.getSiteId(), mPrefs.getProjectId(), DataNames.META_DIARY_SECTION);
        List<MetaDataField> localList;
        if (null != dbMetaData) {
            localList = new Gson().fromJson(dbMetaData.getMetaData(), new TypeToken<List<MetaDataField>>() {
            }.getType());
        } else localList = new ArrayList<>();
        isLoadingLD.postValue(false);
        metaLiveData.postValue(localList != null ? localList : new ArrayList<>());
    }

    @SuppressLint("StaticFieldLeak")
    public void updateProjectDiary(int id, String title, String description, String currentDate, String offlineList, List<DiaryLabourModel> labourDataList) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                try {
                    return mDatabase.updateProjectDiary(id, title, description, currentDate, offlineList,
                            new Gson().toJson(labourDataList));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isSuccess.postValue(true);
            }
        }.execute();
    }
}
