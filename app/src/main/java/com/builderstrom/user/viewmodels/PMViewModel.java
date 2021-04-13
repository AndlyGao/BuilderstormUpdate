package com.builderstrom.user.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.builderstrom.user.repository.retrofit.api.ErrorCodes;
import com.builderstrom.user.repository.retrofit.api.RestClient;
import com.builderstrom.user.repository.retrofit.modals.ErrorModel;
import com.builderstrom.user.repository.retrofit.modals.PMNotesModel;
import com.builderstrom.user.repository.retrofit.modals.ProjectDetails;
import com.builderstrom.user.repository.retrofit.modals.ProjectListModel;
import com.builderstrom.user.repository.retrofit.modals.ProjectNote;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak")
public class PMViewModel extends BaseViewModel {
    public MutableLiveData<List<ProjectDetails>> projectsLD;
    public MutableLiveData<List<ProjectNote>> notesLD;
    public MutableLiveData<List<String>> statusLD;
    public MutableLiveData<List<String>> stagesLD;
    public MutableLiveData<Integer> offsetLD;
    public MutableLiveData<Boolean> isInitial;

    public PMViewModel(@NonNull Application application) {
        super(application);
        projectsLD = new MutableLiveData<>();
        statusLD = new MutableLiveData<>();
        stagesLD = new MutableLiveData<>();
        notesLD = new MutableLiveData<>();
        offsetLD = new MutableLiveData<>();
        isInitial = new MutableLiveData<>();
    }

    public void getAllProject(String stage, String status, String projectId, Integer offset, Integer limit) {
        isLoadingLD.postValue(true);
        RestClient.createService().getProjectsList(stage, status, "", projectId, "project ", offset, limit).enqueue(
                new Callback<ProjectListModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ProjectListModel> call, @NonNull Response<ProjectListModel> response) {
                        setLogs(PMViewModel.class.getName(), "getProjectList", new Gson().toJson(response));
                        try {
                            if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                                if (offset <= 1) {
                                    isInitial.postValue(true);
                                }
                                offsetLD.postValue(response.body().getDataLimit().getOffset());
                                projectsLD.postValue(response.body().getData().getProjects());
                                statusLD.postValue(response.body().getData().getProjectStatuses());
                                stagesLD.postValue(response.body().getData().getProjectStages());
                                projectsLD.postValue(response.body().getData().getProjects());
                            } else {
                                handleErrorBody(response.errorBody());
                            }
                        } catch (Exception e) {
                            errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                        }
                        isLoadingLD.postValue(false);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ProjectListModel> call, @NonNull Throwable t) {
                        setLogs(RFIViewModel.class.getName(), "getRFIList", t.getLocalizedMessage());
                        accessAllProjects();
                    }
                });

    }

    public void getAllNotes(String projectId, Integer offset, Integer limit) {
        isLoadingLD.postValue(true);
        RestClient.createService().getProjectsNotes(projectId, offset, limit).enqueue(
                new Callback<PMNotesModel>() {
                    @Override
                    public void onResponse(@NonNull Call<PMNotesModel> call, @NonNull Response<PMNotesModel> response) {
                        setLogs(PMViewModel.class.getName(), "getPMNotes", new Gson().toJson(response));
                        try {
                            if (ErrorCodes.checkCode(response.code()) && null != response.body()) {
                                notesLD.postValue(response.body().getData().getProjectNotes());
                            } else {
                                handleErrorBody(response.errorBody());
                            }
                        } catch (Exception e) {
                            errorModelLD.postValue(new ErrorModel(e, e.getLocalizedMessage()));
                        }
                        isLoadingLD.postValue(false);
                    }

                    @Override
                    public void onFailure(@NonNull Call<PMNotesModel> call, @NonNull Throwable t) {
                        setLogs(RFIViewModel.class.getName(), "getPMNotes", t.getLocalizedMessage());
                        isLoadingLD.postValue(false);
                    }
                });

    }

    public void accessAllProjects() {
        new AsyncTask<Void, Void, List<ProjectDetails>>() {
            @Override
            protected List<ProjectDetails> doInBackground(Void... voids) {
                return mDatabase.getUserProjectList(mPrefs.getSiteId(), mPrefs.getUserId());
            }

            @Override
            protected void onPostExecute(List<ProjectDetails> projects) {
                super.onPostExecute(projects);
                isLoadingLD.postValue(false);
                isInitial.postValue(true);
                projectsLD.postValue(projects);
            }
        }.execute();
    }
}
