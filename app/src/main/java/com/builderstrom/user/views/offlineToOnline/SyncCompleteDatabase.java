package com.builderstrom.user.views.offlineToOnline;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.builderstrom.user.repository.database.databaseModels.DbCompanyDocuments;
import com.builderstrom.user.repository.database.databaseModels.DbDigitalDocs;
import com.builderstrom.user.repository.database.databaseModels.DbRFI;
import com.builderstrom.user.repository.database.databaseModels.DbSnag;
import com.builderstrom.user.repository.database.databaseModels.DbTimesheet;
import com.builderstrom.user.repository.database.databaseModels.Diary;
import com.builderstrom.user.repository.database.databaseModels.Drawings;
import com.builderstrom.user.repository.database.databaseModels.Gallery;
import com.builderstrom.user.repository.database.databaseModels.Login;
import com.builderstrom.user.repository.database.databaseModels.ProjectDocs;
import com.builderstrom.user.repository.database.databaseModels.ReportProblem;
import com.builderstrom.user.repository.database.databaseModels.ShareDocs;
import com.builderstrom.user.repository.database.databaseModels.SignedProject;
import com.builderstrom.user.repository.database.databaseModels.ToDoCompleteStatus;
import com.builderstrom.user.repository.retrofit.modals.CompanyDocument;
import com.builderstrom.user.repository.retrofit.modals.Datum;
import com.builderstrom.user.repository.retrofit.modals.LocalMetaValues;
import com.builderstrom.user.repository.retrofit.modals.PDocsDataModel;
import com.builderstrom.user.repository.retrofit.modals.PojoMyItem;
import com.builderstrom.user.repository.retrofit.modals.Rfi;
import com.builderstrom.user.repository.retrofit.modals.SyncToDoModel;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.MyItemModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class SyncCompleteDatabase {
    private Context mContext;
    private OfflineSyncPresenter syncPresenter;

    public SyncCompleteDatabase(Context mContext) {
        this.mContext = mContext;
        attachPresenters();
    }

    private void attachPresenters() {
        syncPresenter = new OfflineSyncPresenter();
    }

    public void executeSyncing() {
        try {
            if (CommonMethods.isNetworkAvailable(mContext)) {
                if (nothingToSync()) {
                    Log.e("to sync", "Nothing to sync");
//                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(
//                            new Intent(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE).putExtra("KEY_FLAG", true));

                } else {
                    callOfflineSignProjects();
                    callOfflineDrawing();
                    callOfflineGallery();
                    callOfflineDiary();
                    callOfflineRFI();
                    callOfflineSnags();
                    callOfflineSavedDocs();
                    callOfflineShareDocs();
                    callOfflineReports();
                    callOfflineTimesheets();
                    syncCompanyDocuments();
                    syncProjectsDocuments();
                    syncOfflineToDos();
                    syncLoginStatus();
                    syncTodoResolveStatus();
                }
            } else {
                Log.e("Sync connection", "Connection not available");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean nothingToSync() {
        return BuilderStormApplication.mLocalDatabase.anySignRecord()
                && BuilderStormApplication.mLocalDatabase.noDrawingsToSync()
                && BuilderStormApplication.mLocalDatabase.noGalleryToSync()
                && BuilderStormApplication.mLocalDatabase.noDiaryToSync()
                && BuilderStormApplication.mLocalDatabase.noRfiToSync()
                && BuilderStormApplication.mLocalDatabase.noSnagToSync()
                && BuilderStormApplication.mLocalDatabase.noDiDocToSync()
                && BuilderStormApplication.mLocalDatabase.noTimeSheetToSync()
                && BuilderStormApplication.mLocalDatabase.noComDocToSync()
                && BuilderStormApplication.mLocalDatabase.noProDocToSync()
                && BuilderStormApplication.mLocalDatabase.noShareDocToSync()
                && BuilderStormApplication.mLocalDatabase.noToDoToSync()
                && BuilderStormApplication.mLocalDatabase.noLoginStatus()
                && BuilderStormApplication.mLocalDatabase.noToDoStatusToSync();
    }

    /* Syncing Digital documents */
    private void callOfflineSavedDocs() {
        try {
            List<DbDigitalDocs> docs = BuilderStormApplication.mLocalDatabase.getToSyncDiDocList();
            if (null != docs) {
                Log.e(" docs to sync", String.valueOf(docs.size()));
                for (DbDigitalDocs doc : docs) {
                    List<LocalMetaValues> metaDataList = new ArrayList<>();
                    if (CommonMethods.isNotEmptyArray(doc.getSavedData())) {
                        metaDataList.addAll(new Gson().fromJson(doc.getSavedData(), new TypeToken<List<LocalMetaValues>>() {
                        }.getType()));
                    }
                    new Handler().postDelayed(() -> {
                        PojoMyItem item = null;
                        if (!doc.getMyItem().isEmpty()) {
                            item = new Gson().fromJson(doc.getMyItem(), PojoMyItem.class);
                        }
                        syncPresenter.submitDigitalForm(item, doc.getId(),
                                doc.getProjectId(), doc.getAssigned_user() != null ? doc.getAssigned_user() : doc.getUserId(), doc.getTemplateId(),
                                doc.getCustomDocumentId(), doc.getProjectDocumentId(), metaDataList);
                    }, 1000);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Syncing Share documents */
    private void callOfflineShareDocs() {
        try {
            List<ShareDocs> docs = BuilderStormApplication.mLocalDatabase.getToSyncShareDocs(BuilderStormApplication.mPrefs.getSiteId(), BuilderStormApplication.mPrefs.getUserId());
            if (null != docs) {
                Log.e(" docs to sync", String.valueOf(docs.size()));
                for (ShareDocs doc : docs) {
                    new Handler().postDelayed(() -> syncPresenter.syncIssueDocument(new Gson().fromJson(doc.getDocumentData(), new TypeToken<MyItemModel>() {
                    }.getType()), doc.getId()), 1000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Syncing drawings
    private void callOfflineDrawing() {
        try {
            List<Drawings> drawings = BuilderStormApplication.mLocalDatabase.getDrawingsOffline(BuilderStormApplication.mPrefs.getSiteId());

            if (null != drawings) {
                Log.e(" drawings to sync", String.valueOf(drawings.size()));
                for (Drawings drawing : drawings) {
                    Datum itemModel = new Gson().fromJson(drawing.getDrawingItem(), new TypeToken<Datum>() {
                    }.getType());

                    new Handler().postDelayed(() -> syncPresenter.callDrawingAPI(itemModel.getName(),
                            drawing.getProjectID(), itemModel.getDrawingNo(),
                            itemModel.getRevision(), itemModel.getPdflocation(),
                            itemModel.getDwglocation(), itemModel.getOftlocation(), itemModel.getMetaData(),
                            drawings.indexOf(drawing) == drawings.size() - 1
                    ), 1000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Syncing Galleries
    private void callOfflineGallery() {
        try {
            List<Gallery> galleries = BuilderStormApplication.mLocalDatabase.getProjectGalleryOffline(BuilderStormApplication.mPrefs.getSiteId());
            if (galleries != null) {
                Log.e(" galleries to sync", String.valueOf(galleries.size()));
                for (Gallery gallery : galleries) {
                    new Handler().postDelayed(() -> {
                        List<String> base64ImageList = new Gson().fromJson(gallery.getImages(), new TypeToken<List<String>>() {
                        }.getType());
                        List<LocalMetaValues> metaValues = new Gson().fromJson(gallery.getMetaData(), new TypeToken<List<LocalMetaValues>>() {
                        }.getType());

                        syncPresenter.addGalleryMultipartAPI(
                                gallery.getProjectID(), gallery.getGalleryTitle(), gallery.getLatitude(),
                                gallery.getLongitude(), base64ImageList, metaValues, String.valueOf(gallery.getId()));
                    }, 1000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Syncing Signed projects
    private void callOfflineSignProjects() {
        try {
            List<SignedProject> projects = BuilderStormApplication.mLocalDatabase.getSignedProjects(
                    BuilderStormApplication.mPrefs.getSiteId(), BuilderStormApplication.mPrefs.getUserId());
            if (null != projects) {
                Log.e(" projects to sync", String.valueOf(projects.size()));
                for (SignedProject project : projects) {
                    new Handler().postDelayed(() -> syncPresenter.projectSignConfirm(mContext,
                            project.getProjectID(), project.getStartLatitude(),
                            project.getStartLongitude(), project.getEndLatitude(),
                            project.getEndLongitude(), project.getUserStartTime(),
                            project.getUserEndTime(), project.getOverTime_array(), project.getSelected_breaks(),
                            project.getBreaks_array(), project.getId()), 1000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* syncing Diary */
    private void callOfflineDiary() {
        try {
            List<Diary> diaries = BuilderStormApplication.mLocalDatabase.getProjectDiaryOffline(BuilderStormApplication.mPrefs.getSiteId());
            if (diaries != null) {
                Log.e("diaries to sync", String.valueOf(diaries.size()));
                for (Diary diary : diaries) {
                    new Handler().postDelayed(() -> {

                        List<String> files = new Gson().fromJson(diary.getDiaryData(), new TypeToken<List<String>>() {
                        }.getType());

                        List<LocalMetaValues> metaData = new Gson().fromJson(diary.getMetaData(), new TypeToken<List<LocalMetaValues>>() {
                        }.getType());

                        syncPresenter.addDiary(diary.getProjectID(), diary.getTitle(), diary.getDescription(),
                                diary.getDate(), diary.getTime(), files, metaData, diary.getId(), diary.getSiteLabor());
                    }, 1000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Syncing Rfi */
    private void callOfflineRFI() {
        try {
            List<DbRFI> rfis = BuilderStormApplication.mLocalDatabase.getRFIToSync(BuilderStormApplication.mPrefs.getSiteId());
            if (rfis != null) {
                Log.e("rfis to sync", String.valueOf(rfis.size()));
                for (DbRFI rfi : rfis) {
                    if (CommonMethods.isOfflineEntry(rfi.getRowId())) {
                        new Handler().postDelayed(() -> {
                            List<String> localListRFI = new Gson().fromJson(rfi.getRfiData(), new TypeToken<List<String>>() {
                            }.getType());
                            List<LocalMetaValues> metaValues = new Gson().fromJson(rfi.getMetaData(), new TypeToken<List<LocalMetaValues>>() {
                            }.getType());

                            syncPresenter.callAddRFI(rfi.getTitle(), rfi.getDate(),
                                    rfi.getDescription(), rfi.getTime(),
                                    rfi.getProjectID(), rfi.getToUsers(),
                                    rfi.getCcUsers(), metaValues, localListRFI, rfi.getId());
                        }, 1000);
                    } else {
                        if (rfi.getAnsweredAdded()) {
                            // Sync Answer to online already synced Item
                            Rfi rfiAnswer = new Gson().fromJson(rfi.getRfiData(), new TypeToken<Rfi>() {
                            }.getType());

                            new Handler().postDelayed(() -> syncPresenter.addAnswerToRfi(rfiAnswer.getAnswer(), rfiAnswer.getId(), rfiAnswer.getAnswerAttachmentFiles()), 1000);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Syncing Snags */
    private void callOfflineSnags() {
        try {
            List<DbSnag> snags = BuilderStormApplication.mLocalDatabase.getSnagToSync(BuilderStormApplication.mPrefs.getSiteId());
            if (snags != null && !snags.isEmpty()) {
                Log.e("snags to sync", String.valueOf(snags.size()));
                for (DbSnag snag : snags) {
                    new Handler().postDelayed(() -> {
                        List<String> filePaths = new Gson().fromJson(snag.getItemData(), new TypeToken<List<String>>() {
                        }.getType());
                        syncPresenter.addSnag(
                                snag.getProjectId(), snag.getDescription(), snag.getLocation(),
                                snag.getPackageNo(), snag.getToUsers(), snag.getCcUsers(),
                                snag.getDueDate(), snag.getTimeStamp(), filePaths, snag.getId());
                    }, 1000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* syncing Reports */
    private void callOfflineReports() {
        try {
            List<ReportProblem> reports = BuilderStormApplication.mLocalDatabase.getIssueReports(BuilderStormApplication.mPrefs.getUserId());
            if (reports != null) {
                Log.e("reports to sync", String.valueOf(reports.size()));
                for (ReportProblem issue : reports) {
                    new Handler().postDelayed(() -> {
                        List<String> files = new Gson().fromJson(issue.getLogged_files(), new TypeToken<List<String>>() {
                        }.getType());
                        syncPresenter.sendReport(issue.getDescription(), files, issue.getId());
                    }, 1000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Syncing Company Documents */
    private void syncCompanyDocuments() {
        /* sync company docs */
        List<DbCompanyDocuments> docsList = BuilderStormApplication.mLocalDatabase
                .getAllCompanyDocumentsToSync(BuilderStormApplication.mPrefs.getSiteId(),
                        BuilderStormApplication.mPrefs.getUserId());

        Log.e(" Company docs to sync", String.valueOf(docsList.size()));

        for (DbCompanyDocuments document : docsList) {
            new Handler().postDelayed(() -> {
                CompanyDocument companyDocument = new Gson().fromJson(document.getCompanyData(), CompanyDocument.class);
                if (null != companyDocument) {
                    syncPresenter.syncCompanyDocument(document.getId(),
                            companyDocument);
                }
            }, 1000);
        }
    }

    /* Syncing Project Documents */
    private void syncProjectsDocuments() {
        /* sync Project docs */
        List<ProjectDocs> docsList = BuilderStormApplication.mLocalDatabase.getAllProjectDocuments(
                BuilderStormApplication.mPrefs.getSiteId(),
                BuilderStormApplication.mPrefs.getUserId());

        Log.e(" Project docs to sync", String.valueOf(docsList.size()));

        for (ProjectDocs document : docsList) {
            new Handler().postDelayed(() -> {
                PDocsDataModel projectDocument = new Gson().fromJson(document.getProjectData(), PDocsDataModel.class);
                if (null != projectDocument) {
                    syncPresenter.syncProjectDocument(document.getId(),
                            document.getProjectID(), projectDocument);
                }
            }, 1000);
        }
    }

    /* Syncing Timesheets */
    private void callOfflineTimesheets() {
        Log.e(" timesheets", "syncing started");

        List<DbTimesheet> syncList = BuilderStormApplication.mLocalDatabase.getTimeSheetsToSync(
                BuilderStormApplication.mPrefs.getSiteId(), BuilderStormApplication.mPrefs.getUserId());

        for (DbTimesheet timeSheet : syncList) {
            new Handler().postDelayed(() -> syncPresenter.syncTimeSheet(timeSheet), 1000);
        }

    }

    private void syncOfflineToDos() {
        List<SyncToDoModel> toSyncList = BuilderStormApplication.mLocalDatabase.getToDosToSync(
                BuilderStormApplication.mPrefs.getSiteId(), BuilderStormApplication.mPrefs.getUserId());
        Log.e(" todos size", String.valueOf(toSyncList.size()));
        for (SyncToDoModel syncModel : toSyncList) {
            new Handler().postDelayed(() -> syncPresenter.syncToDo(syncModel.getColumnId(),
                    syncModel.getToDo()), 1000);
        }
    }

    private void syncLoginStatus() {
        try {
            Login loginModel = BuilderStormApplication.mLocalDatabase.syncLogOutStatus(
                    BuilderStormApplication.mPrefs.getSiteId(), BuilderStormApplication.mPrefs.getUserId());
            if (null != loginModel) {
                new Handler().postDelayed(() -> {
                    Log.e("Data at syncing", new Gson().toJson(loginModel));
                    syncPresenter.syncLoginStatus(loginModel);
                }, 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncTodoResolveStatus() {
        List<ToDoCompleteStatus> toSyncstatus = BuilderStormApplication.mLocalDatabase.getToDoStautsToSync(
                BuilderStormApplication.mPrefs.getSiteId(), BuilderStormApplication.mPrefs.getUserId());
        Log.e(" todos resolved size", String.valueOf(toSyncstatus.size()));
        for (ToDoCompleteStatus syncStatusModel : toSyncstatus) {
            new Handler().postDelayed(() -> syncPresenter.markTodo(syncStatusModel.getTodoId(),
                    syncStatusModel.getStatus(), syncStatusModel.getId(), syncStatusModel.getDate()), 1000);
        }

    }


}
