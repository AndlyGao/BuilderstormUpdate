package com.builderstrom.user.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.builderstrom.user.R;
import com.builderstrom.user.data.database.databaseModels.AccessSettingTS;
import com.builderstrom.user.data.database.databaseModels.DbTimeSheetTask;
import com.builderstrom.user.data.database.databaseModels.DbTimesheet;
import com.builderstrom.user.data.database.databaseModels.UserProjects;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.api.ErrorCodes;
import com.builderstrom.user.data.retrofit.api.RestClient;
import com.builderstrom.user.data.retrofit.modals.ErrorModel;
import com.builderstrom.user.data.retrofit.modals.PojoNewSuccess;
import com.builderstrom.user.data.retrofit.modals.PojoOffTimeSheetModal;
import com.builderstrom.user.data.retrofit.modals.PojoPriceWorkModel;
import com.builderstrom.user.data.retrofit.modals.PojoTSHoliday;
import com.builderstrom.user.data.retrofit.modals.PojoTimeSheetTasks;
import com.builderstrom.user.data.retrofit.modals.ProjectDetails;
import com.builderstrom.user.data.retrofit.modals.ReturnOverviewDetail;
import com.builderstrom.user.data.retrofit.modals.ReturnOverviewhtmldetail;
import com.builderstrom.user.data.retrofit.modals.TSNote;
import com.builderstrom.user.data.retrofit.modals.TaskListing;
import com.builderstrom.user.data.retrofit.modals.TimeSheetListModel;
import com.builderstrom.user.data.retrofit.modals.TimesheetMetaData;
import com.builderstrom.user.data.retrofit.modals.TimesheetOverview;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak")
public class TimeSheetViewModel extends BaseViewModel {

    private static String TAG = TimeSheetViewModel.class.getName();

    public MutableLiveData<Boolean> isRefreshing;
    public MutableLiveData<List<TimeSheetListModel.WeekDataModel>> timeSheetLiveData;
    public MutableLiveData<List<PojoOffTimeSheetModal>> timeSheetOfflineLiveData;
    public MutableLiveData<List<ProjectDetails>> projectsList;
    public MutableLiveData<TimesheetOverview.PojoOverViewData> overviewLiveData;
    public MutableLiveData<List<ReturnOverviewDetail>> offlineOverViewLiveData;
    public MutableLiveData<List<TSNote>> offlineNotesLiveData;
    public MutableLiveData<List<TaskListing>> tasksLiveList;
    public MutableLiveData<List<PojoPriceWorkModel.PriceWorkListing>> priceWorkLiveList;
    public MutableLiveData<List<TimesheetMetaData.MetaSetup>> metadataLiveList;
    public MutableLiveData<String> totalHoursLiveData;
    public MutableLiveData<String> successfulLiveData;
    public MutableLiveData<String> successfulLiveData2;
    public MutableLiveData<Integer> deleteActivityLD;
    public MutableLiveData<Integer> delNoteLiveData;
    public MutableLiveData<String> toastMetaData;
    public MutableLiveData<String> weekDateLiveData;
    public MutableLiveData<ReturnOverviewhtmldetail> menuSetting;

    public TimeSheetViewModel(@NonNull Application application) {
        super(application);
        isRefreshing = new MutableLiveData<>();
        timeSheetLiveData = new MutableLiveData<>();
        timeSheetOfflineLiveData = new MutableLiveData<>();
        projectsList = new MutableLiveData<>();
        totalHoursLiveData = new MutableLiveData<>();
        overviewLiveData = new MutableLiveData<>();
        offlineOverViewLiveData = new MutableLiveData<>();
        offlineNotesLiveData = new MutableLiveData<>();
        successfulLiveData = new MutableLiveData<>();
        successfulLiveData2 = new MutableLiveData<>();
        tasksLiveList = new MutableLiveData<>();
        priceWorkLiveList = new MutableLiveData<>();
        metadataLiveList = new MutableLiveData<>();
        deleteActivityLD = new MutableLiveData<>();
        delNoteLiveData = new MutableLiveData<>();
        toastMetaData = new MutableLiveData<>();
        weekDateLiveData = new MutableLiveData<>();
        menuSetting = new MutableLiveData<>();
    }

    /* View TimeSheet functions */
    public void updateTimeSheet(String date, boolean updateTextDate) {
        isRefreshing.setValue(true);
        weekDateLiveData.setValue(updateTextDate ? CommonMethods.convertDate(CommonMethods.DF_2, date, CommonMethods.DF_8) : null);
        RestClient.createService().apiTimeSheet(date).enqueue(new Callback<TimeSheetListModel>() {
            @Override
            public void onResponse(@NonNull Call<TimeSheetListModel> call, @NonNull Response<TimeSheetListModel> response) {
                setLogs(TAG, "updateTimeSheet", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        timeSheetLiveData.setValue(response.body().getData().getWeekdata());
                        totalHoursLiveData.setValue(response.body().getData().getTotalTime());
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
                isRefreshing.setValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<TimeSheetListModel> call, @NonNull Throwable t) {
                setLogs(TAG, "updateTimeSheet", t.getLocalizedMessage());
                errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
                updateOfflineWeekData(date);
            }
        });
    }

    public void getOverview(String projectId, String date) {
        isRefreshing.setValue(true);
        RestClient.createService().apiGetUserActivities(projectId, date).enqueue(new Callback<TimesheetOverview>() {
            @Override
            public void onResponse(@NonNull Call<TimesheetOverview> call, @NonNull Response<TimesheetOverview> response) {
                setLogs(TAG, "getOverview", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        overviewLiveData.setValue(response.body().getData());
                        if (response.body().getData().getOverviewHtml() != null) {
                            syncSetting(new Gson().toJson(response.body().getData().getOverviewHtml()));
                        } else {
                            isRefreshing.setValue(false);
                        }
                    } else {
                        isRefreshing.setValue(false);
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    isRefreshing.setValue(false);
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<TimesheetOverview> call, @NonNull Throwable t) {
                setLogs(TAG, "getOverview", t.getLocalizedMessage());
                if (CommonMethods.isNetworkError(t)) {
                    getOfflineOverViewList(date);
                } else {
                    isRefreshing.setValue(false);
                    errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
                }
            }
        });
    }

    private boolean checkForPriceWork(List<ReturnOverviewDetail> ovList) {
        for (ReturnOverviewDetail detail : ovList) {
            if (detail.getData_type() == null || !detail.getData_type().equalsIgnoreCase(DataNames.ADD_ACTION_PRICE_WORK)) {
                return false;
            }
        }
        return true;
    }

    public void getTimeSheetTasks(String projectID) {
        isRefreshing.setValue(true);
        RestClient.createService().getWorkTimeTasks(projectID).enqueue(new Callback<PojoTimeSheetTasks>() {
            @Override
            public void onResponse(@NonNull Call<PojoTimeSheetTasks> call, @NonNull Response<PojoTimeSheetTasks> response) {
                setLogs(TAG, "getTimeSheetTasks", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        tasksLiveList.setValue(response.body().getData());
                        syncTasksToDatabase(projectID, true, new Gson().toJson(response.body().getData()));
                    } else {
                        isRefreshing.setValue(false);
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoTimeSheetTasks> call, @NonNull Throwable t) {
                setLogs(TAG, "getTimeSheetTasks", t.getLocalizedMessage());
                getOfflineTimeSheetTasks(projectID);
//                if (CommonMethods.isNetworkError(t)) {
//                    getOfflineTimeSheetTasks(projectID);
//                } else {
//                    isRefreshing.setValue(false);
//                    errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
//                }
            }
        });

    }

    public void getPriceWorkList(String projectID) {
        isRefreshing.setValue(true);
        RestClient.createService().getProjectPriceWork(projectID).enqueue(new Callback<PojoPriceWorkModel>() {
            @Override
            public void onResponse(@NonNull Call<PojoPriceWorkModel> call, @NonNull Response<PojoPriceWorkModel> response) {
                setLogs(TAG, "getPriceWorkList", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        syncTasksToDatabase(projectID, false, new Gson().toJson(response.body().getData().getPriceWorkListing()));
                        priceWorkLiveList.setValue(response.body().getData().getPriceWorkListing());
                    } else {
                        isRefreshing.setValue(false);
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoPriceWorkModel> call, @NonNull Throwable t) {
                setLogs(TAG, "getPriceWorkList", t.getLocalizedMessage());
//                isRefreshing.setValue(false);
                getOfflinePriceWorkList(projectID);

//                if (CommonMethods.isNetworkError(t)) {
//                    getOfflinePriceWorkList(projectID);
//                } else {
//                    isRefreshing.setValue(false);
//                    errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
//                }

            }
        });
    }

    public void getSheetMetaData(String projectID) {
        isRefreshing.setValue(true);
        RestClient.createService().getSheetMetaData(projectID).enqueue(new Callback<TimesheetMetaData>() {
            @Override
            public void onResponse(@NonNull Call<TimesheetMetaData> call, @NonNull Response<TimesheetMetaData> response) {
                setLogs(TAG, "getSheetMetaData", new Gson().toJson(response));
                isRefreshing.setValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        metadataLiveList.setValue(response.body().getReturnData().getTimesheetMetaData());
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<TimesheetMetaData> call, @NonNull Throwable t) {
                isRefreshing.setValue(false);
                setLogs(TAG, "getSheetMetaData", t.getLocalizedMessage());
                errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void apiAddWorkTime(String projectID, String date, String isWorkStartEnd, String startTime, String endTime, String workHours, String taskID, String deleteunfinishedactid, String activity_id, boolean showBreakTime) {
        isRefreshing.setValue(true);
        RestClient.createService().apiAddWorkTime(activity_id, DataNames.ADD_ACTION_WORK_TIME,
                projectID, date, isWorkStartEnd, startTime, endTime, workHours, taskID,
                deleteunfinishedactid).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                setLogs(TAG, "apiTSAddWorkTime", new Gson().toJson(response));
                isRefreshing.postValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
//                        if (showBreakTime == null || showBreakTime == 0) {
                        if (showBreakTime) {
                            successfulLiveData2.postValue(response.body().getMessage());
                        } else {
                            successfulLiveData.postValue(response.body().getMessage());
                        }
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                setLogs(TAG, "apiTSAddWorktime", t.getLocalizedMessage());
                isRefreshing.postValue(false);
                errorModelLD.postValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void addBreakTime(String activityId, String projectId, String projectTitle, String date,
                             String dataType, Integer breakTimeType, String breakTime,
                             String breakStartTime, String breakEndTime, String breakIds, String breaksList) {
        isRefreshing.setValue(true);
        if (dataType.equals(DataNames.ADD_ACTION_STANDARD_BREAKS)) {
            RestClient.createService().apiTimeSheetStandardBreaks(activityId, projectId, projectTitle,
                    date, dataType, breakIds, breaksList).enqueue(new Callback<PojoNewSuccess>() {
                @Override
                public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                    isRefreshing.setValue(false);
                    setLogs(TAG, "addTSBreakTime", new Gson().toJson(response));
                    try {
                        if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                            /* add work time success */
                            successfulLiveData.setValue(response.body().getMessage());
                        } else {
                            handleErrorBody(response.errorBody());
                        }
                    } catch (Exception e) {
                        errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                    isRefreshing.setValue(false);
                    setLogs(TAG, "addTSBreakTime", t.getLocalizedMessage());
                    errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
                }
            });
        } else {
            RestClient.createService().apiTimeSheetBreaks(activityId, projectId, projectTitle, date,
                    dataType, breakTimeType, breakTime, breakStartTime, breakEndTime
            ).enqueue(new Callback<PojoNewSuccess>() {
                @Override
                public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                    isRefreshing.setValue(false);
                    setLogs(TAG, "addTSBreakTime", new Gson().toJson(response));
                    try {
                        if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                            /* add work time success */
                            successfulLiveData.setValue(response.body().getMessage());
                        } else {
                            handleErrorBody(response.errorBody());
                        }
                    } catch (Exception e) {
                        errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                    isRefreshing.setValue(false);
                    setLogs(TAG, "addTSBreakTime", t.getLocalizedMessage());
                    errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
                }
            });
        }


    }

    public void addTravelTime(String activityId, String projectId, String projectTitle, String date, String from, String to, String startTime, String endTime) {
        isRefreshing.setValue(true);
        RestClient.createService().apiAddTSTravelTime(activityId, projectId, projectTitle, date,
                DataNames.ADD_ACTION_TRAVEL, from, to, startTime,
                endTime).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                isRefreshing.setValue(false);
                setLogs(TAG, "addTSTravelTime", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        /* add work time success */
                        successfulLiveData.setValue(response.body().getMessage());
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                isRefreshing.setValue(false);
                setLogs(TAG, "addTSTravelTime", t.getLocalizedMessage());
                errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void addExpense(String activityId, String projectId, String date, String expItem, String itemCost) {
        isRefreshing.setValue(true);
        RestClient.createService().addTSExpenseApi(activityId, projectId, date,
                DataNames.ADD_ACTION_EXPENSES, expItem, itemCost).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                isRefreshing.setValue(false);
                setLogs(TAG, "addTSExpense", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        /* add work time success */
                        successfulLiveData.setValue(response.body().getMessage());
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                isRefreshing.setValue(false);
                setLogs(TAG, "addTSExpense", t.getLocalizedMessage());
                errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void addPriceWork(String activityId, String projectId, String date, String priceWork_item, String priceWork_item_cost, String priceWork_location, String priceWork_quantity) {
        isRefreshing.setValue(true);
        RestClient.createService().addTSPriceWorkApi(activityId, DataNames.ADD_ACTION_PRICE_WORK,
                projectId, date, priceWork_item, priceWork_item_cost, priceWork_location,
                priceWork_quantity).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                isRefreshing.setValue(false);
                setLogs(TAG, "addPriceWork", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        /* add Price Work success */
                        successfulLiveData.setValue(response.body().getMessage());
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                isRefreshing.setValue(false);
                setLogs(TAG, "addTravelTime", t.getLocalizedMessage());
                errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void addHoliday(String date, String workType, String time, String startTime, String endTime, String duration) {
        isRefreshing.setValue(true);
        RestClient.createService().addTSHolidayApi(date, workType, time, startTime,
                endTime, duration).enqueue(new Callback<PojoTSHoliday>() {
            @Override
            public void onResponse(@NonNull Call<PojoTSHoliday> call, @NonNull Response<PojoTSHoliday> response) {
                setLogs(TAG, "addTSHoliday", new Gson().toJson(response));
                isRefreshing.setValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        /* add work time success */
                        successfulLiveData.setValue(holidaySuccessString(workType, response.body()));
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoTSHoliday> call, @NonNull Throwable t) {
                setLogs(TAG, "addTSHoliday", t.getLocalizedMessage());
                isRefreshing.setValue(false);
                errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    private String holidaySuccessString(String workType, PojoTSHoliday body) {
        StringBuilder message = new StringBuilder();
        for (PojoTSHoliday.PojoHoliday holiday : body.getData()) {
            if (holiday.getHoliday_added() == 0) {
                String convertedDate = CommonMethods.convertDate(CommonMethods.DF_2,
                        holiday.getRequested_date(), CommonMethods.DF_5);
                if (null != convertedDate) {
                    if (message.length() == 0) message = new StringBuilder(convertedDate);
                    else message.append(", ").append(convertedDate);
                }

            }
        }
        return message.toString().isEmpty() ? getApplication().getString(R.string.success_data_saved) :
                String.format(" %s already booked %s for %s", mPrefs.getUserName(),
                        workType.equals("1") ? "Holiday" : "Sickness", message);
    }

    public void addNotes(String projectId, String noteId, String date, String description) {
        isRefreshing.setValue(true);
        RestClient.createService().addTSNotesApi(noteId, projectId, date, description).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                isRefreshing.setValue(false);
                setLogs(TAG, "addTSNotes", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        /* add notes success */
                        toastMetaData.setValue(response.body().getMessage());
                        successfulLiveData.setValue(response.body().getMessage());
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                isRefreshing.setValue(false);
                setLogs(TAG, "addTSNotes", t.getLocalizedMessage());
                errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void addMetaData(String projectId, String date, String metaData) {
        isRefreshing.setValue(true);
        RestClient.createService().addTSMetaDataApi("0", projectId, date, DataNames.ADD_ACTION_METADATA,
                "0", metaData).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                setLogs(TAG, "addMetaData", new Gson().toJson(response));
                isRefreshing.setValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        successfulLiveData.setValue(response.body().getMessage());
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                setLogs(TAG, "addMetaData", t.getLocalizedMessage());
                isRefreshing.setValue(false);
                errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void deleteOverviewActivity(String activityId, Integer position) {
        isRefreshing.setValue(true);
        RestClient.createService().deleteTSActivity(activityId).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                isRefreshing.setValue(false);
                setLogs(TAG, "addTSHoliday", new Gson().toJson(response));
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        deleteActivityLD.setValue(position);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                isRefreshing.setValue(false);
                setLogs(TAG, "addTSHoliday", t.getLocalizedMessage());
                errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    public void deleteNote(String noteId, Integer position) {
        isRefreshing.setValue(true);
        RestClient.createService().deleteTSNotesApi(noteId).enqueue(new Callback<PojoNewSuccess>() {
            @Override
            public void onResponse(@NonNull Call<PojoNewSuccess> call, @NonNull Response<PojoNewSuccess> response) {
                setLogs(TAG, "deleteNote", new Gson().toJson(response));
                isRefreshing.setValue(false);
                try {
                    if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                        delNoteLiveData.setValue(position);
                    } else {
                        handleErrorBody(response.errorBody());
                    }
                } catch (Exception e) {
                    errorModelLD.setValue(new ErrorModel(e, e.getLocalizedMessage()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PojoNewSuccess> call, @NonNull Throwable t) {
                isRefreshing.setValue(false);
                setLogs(TAG, "deleteNote", t.getLocalizedMessage());
                errorModelLD.setValue(new ErrorModel(t, t.getLocalizedMessage()));
            }
        });
    }

    /* **********************    Database Operations *********************** */

    public void getUserProjects() {
        try {
            new AsyncTask<Void, Void, UserProjects>() {
                @Override
                protected UserProjects doInBackground(Void... voids) {
                    return mDatabase.getUserProjects(mPrefs.getSiteId(), mPrefs.getUserId());
                }

                @Override
                protected void onPostExecute(UserProjects projects) {
                    super.onPostExecute(projects);
                    // Access projects from local database
                    projectsList.postValue(projects != null ? new Gson().fromJson(projects.getUserProjects(),
                            new TypeToken<List<ProjectDetails>>() {
                            }.getType()) : null);
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getUserSetting() {
        isRefreshing.setValue(true);
        new AsyncTask<Void, Void, AccessSettingTS>() {
            @Override
            protected AccessSettingTS doInBackground(Void... voids) {
                return mDatabase.timeSheetSetting(mPrefs.getSiteId(), mPrefs.getUserId());
            }

            @Override
            protected void onPostExecute(AccessSettingTS settingTS) {
                super.onPostExecute(settingTS);
                ReturnOverviewhtmldetail setting;
                if (null != settingTS) {
                    setting = (new Gson().fromJson(settingTS.getSetting(),
                            new TypeToken<ReturnOverviewhtmldetail>() {
                            }.getType()));

                } else {
                    setting = new ReturnOverviewhtmldetail(false, false, false, false, true, false, false, false, false, false, false, 0, null, null, null);
                }
                menuSetting.setValue(setting);
                isRefreshing.setValue(false);
            }
        }.execute();

    }

    public void updateOfflineWeekData(String date) {
        int totalHours = 0;
        List<PojoOffTimeSheetModal> offlineList = new ArrayList<>();
        List<String> weekDays = CommonMethods.getWholeWeekList(date);
        for (String weekDay : weekDays) {
            DbTimesheet dbTimesheet = mDatabase.getTimeSheet(mPrefs.getSiteId(), mPrefs.getUserId(),
                    CommonMethods.convertDate(CommonMethods.DF_11, weekDay, CommonMethods.DF_2));
            int currentHour = 0;
            String viewType = "0";
            String viewTypeName = "";
            boolean isShowPriceWork = false;
            if (dbTimesheet != null) {
                currentHour = dbTimesheet.getTotalTime();
                if (CommonMethods.isNotEmptyArray(dbTimesheet.getOverView())) {
                    List<ReturnOverviewDetail> ovList = new Gson().fromJson(dbTimesheet.getOverView(),
                            new TypeToken<List<ReturnOverviewDetail>>() {
                            }.getType());
                    isShowPriceWork = checkForPriceWork(ovList);
                }
                viewType = dbTimesheet.getType();
                viewTypeName = dbTimesheet.getTypeName();
            }
            totalHours += currentHour;
            offlineList.add(new PojoOffTimeSheetModal(weekDay, String.valueOf(currentHour), isShowPriceWork, viewType, viewTypeName));
        }
        timeSheetOfflineLiveData.postValue(offlineList);
        totalHoursLiveData.postValue(CommonMethods.convertMinToHours(totalHours));
        isRefreshing.postValue(false);
    }

    private void getOfflineOverViewList(String date) {
        new AsyncTask<Void, Void, DbTimesheet>() {
            @Override
            protected DbTimesheet doInBackground(Void... voids) {
                return mDatabase.getTimeSheet(mPrefs.getSiteId(), mPrefs.getUserId(), date);
            }

            @Override
            protected void onPostExecute(DbTimesheet timesheet) {
                super.onPostExecute(timesheet);
                List<TSNote> tsNotes = new ArrayList<>();
                List<ReturnOverviewDetail> detailList = new ArrayList<>();
                if (null != timesheet) {
                    if (!CommonMethods.isEmptyList(timesheet.getOverView())) {
                        detailList.addAll(new Gson().fromJson(timesheet.getOverView(), new TypeToken<List<ReturnOverviewDetail>>() {
                        }.getType()));
                    }
                    if (!CommonMethods.isEmptyList(timesheet.getNotes())) {
                        tsNotes.addAll(new Gson().fromJson(timesheet.getNotes(), new TypeToken<List<TSNote>>() {
                        }.getType()));
                    }
                }

                offlineOverViewLiveData.setValue(detailList);
                offlineNotesLiveData.setValue(tsNotes);
                isRefreshing.setValue(false);
            }
        }.execute();
    }

    public void addOfflineData(String typeName, String type, String date, List<ReturnOverviewDetail> overviewDetails, List<TSNote> notes, String viewType, Boolean breakAutomaticalyWorktime) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                Integer totalHours = 0;
                for (ReturnOverviewDetail detail : overviewDetails) {
                    if (null != detail.getTotalTimeInMinutes()) {
                        if (typeName.equalsIgnoreCase(DataNames.BREAKS) && detail.isSettingAllow()) {
                            if (detail.getTotalTimeInMinutes() > 0) {
                                if (null != detail.getBreakTimeAdded()) {
                                    totalHours += detail.getBreakTimeAdded();
                                }
                            } else {
                                totalHours += detail.getTotalTimeInMinutes();
                            }
                        } else {
                            totalHours += detail.getTotalTimeInMinutes();
                        }
                    }
                }
                DbTimesheet timesheet = mDatabase.getTimeSheet(mPrefs.getSiteId(), mPrefs.getUserId(), date);
                if (null != timesheet) {
                    /* update list */
                    return mDatabase.updateTimeSheet(mPrefs.getSiteId(), mPrefs.getUserId(), date, totalHours,
                            new Gson().toJson(overviewDetails), new Gson().toJson(notes), viewType, typeName);
                } else {
                    return mDatabase.insertTimeSheet(date, mPrefs.getSiteId(), mPrefs.getUserId(),
                            new Gson().toJson(overviewDetails), totalHours, new Gson().toJson(notes), viewType, typeName);
                }
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                if (aLong != null) {
                    isRefreshing.setValue(false);
//                    successfulLiveData.setValue(type + " added successfully.");
                    if (null != breakAutomaticalyWorktime && breakAutomaticalyWorktime) {
                        successfulLiveData2.postValue(type + " added successfully.");
                    } else {
                        successfulLiveData.postValue(type + " added successfully.");
                    }
                }
            }
        }.execute();
    }

    private void syncTasksToDatabase(String projectId, boolean isTask, String objectList) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                try {
                    DbTimeSheetTask sheetTask = mDatabase.getTSTasks(mPrefs.getSiteId(), projectId);
                    if (null == sheetTask) {
                        return mDatabase.insertTimeSheetTasks(mPrefs.getSiteId(), mPrefs.getUserId(),
                                projectId, isTask ? objectList : "", isTask ? "" : objectList);
                    } else {
                        return mDatabase.updateTimeSheetTask(mPrefs.getSiteId(), projectId,
                                isTask ? objectList : sheetTask.getProjectTasks(),
                                isTask ? sheetTask.getProjectItems() : objectList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isRefreshing.setValue(false);
            }
        }.execute();

    }

    private void getOfflineTimeSheetTasks(String projectID) {
        new AsyncTask<Void, Void, DbTimeSheetTask>() {
            @Override
            protected DbTimeSheetTask doInBackground(Void... voids) {
                return mDatabase.getTSTasks(mPrefs.getSiteId(), projectID);
            }

            @Override
            protected void onPostExecute(DbTimeSheetTask tsTask) {
                super.onPostExecute(tsTask);
                List<TaskListing> taskList = new ArrayList<>();
                if (null != tsTask && !CommonMethods.isEmptyList(tsTask.getProjectTasks())) {
                    taskList.addAll(new Gson().fromJson(tsTask.getProjectTasks(), new TypeToken<List<TaskListing>>() {
                    }.getType()));
                }
                tasksLiveList.setValue(taskList);
                isRefreshing.setValue(false);
            }
        }.execute();

    }

    private void getOfflinePriceWorkList(String projectID) {
        new AsyncTask<Void, Void, DbTimeSheetTask>() {
            @Override
            protected DbTimeSheetTask doInBackground(Void... voids) {
                return mDatabase.getTSTasks(mPrefs.getSiteId(), projectID);
            }

            @Override
            protected void onPostExecute(DbTimeSheetTask dbTimeSheetTask) {
                super.onPostExecute(dbTimeSheetTask);
                List<PojoPriceWorkModel.PriceWorkListing> list = new ArrayList<>();
                if (null != dbTimeSheetTask && !CommonMethods.isEmptyList(dbTimeSheetTask.getProjectItems())) {
                    list.addAll(new Gson().fromJson(dbTimeSheetTask.getProjectItems(),
                            new TypeToken<List<PojoPriceWorkModel.PriceWorkListing>>() {
                            }.getType()));
                }
                priceWorkLiveList.setValue(list);
                isRefreshing.setValue(false);
            }
        }.execute();

    }

    private void syncSetting(String userSetting) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                try {
                    return mDatabase.insertAccessSetting(mPrefs.getSiteId(), mPrefs.getUserId(), userSetting);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                isRefreshing.setValue(false);
            }
        }.execute();

    }

    public void deleteOfflineOverViewActivity(String date, int position, List<ReturnOverviewDetail> overView, String viewType, String viewTypeName) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                DbTimesheet dbTimeSheet = mDatabase.getTimeSheet(mPrefs.getSiteId(), mPrefs.getUserId(), date);
                Integer totalHours = 0;
                for (ReturnOverviewDetail detail : overView) {
                    if (null != detail.getTotalTimeInMinutes()) {
//                        totalHours += detail.getTotalTimeInMinutes();
                        totalHours += detail.getTotalTimeInMinutes();
                        if (null != detail.getBreakTimeAdded()) {
                            totalHours += detail.getBreakTimeAdded();
                        }
                    }
                }

                if (dbTimeSheet != null) {
                    return mDatabase.updateTimeSheet(mPrefs.getSiteId(), mPrefs.getUserId(), date,
                            totalHours, new Gson().toJson(overView), dbTimeSheet.getNotes(), viewType, viewTypeName);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                deleteActivityLD.setValue(aLong == null ? -1 : position);
            }

        }.execute();
    }

    public void deleteOfflineNote(String date, int position, String notes, String viewType, String typeName) {
        new AsyncTask<Void, Void, Long>() {

            @Override
            protected Long doInBackground(Void... voids) {
                DbTimesheet dbTimeSheet = mDatabase.getTimeSheet(mPrefs.getSiteId(),
                        mPrefs.getUserId(), date);
                if (dbTimeSheet != null) {
                    return mDatabase.updateTimeSheet(mPrefs.getSiteId(), mPrefs.getUserId(), date,
                            dbTimeSheet.getTotalTime(), dbTimeSheet.getOverView(), notes, viewType, typeName);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                delNoteLiveData.setValue(aLong == null ? -1 : position);
            }

        }.execute();
    }


}
