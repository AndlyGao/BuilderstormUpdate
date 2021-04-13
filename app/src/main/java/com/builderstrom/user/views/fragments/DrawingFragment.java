package com.builderstrom.user.views.fragments;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.modals.Datum;
import com.builderstrom.user.service.DownloadTask;
import com.builderstrom.user.service.SyncAllDrawingTask;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.DrawingMenuVM;
import com.builderstrom.user.views.activities.AddDrawingActivity;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.DrawingOfflineAdapter;
import com.builderstrom.user.views.adapters.DrawingOnlineAdapter;
import com.builderstrom.user.views.dialogFragments.DrawingActionDialogFragment;
import com.builderstrom.user.views.dialogFragments.DrawingInfoDialogFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.builderstrom.user.views.fragments.HomeMenuFragment.userPermissions;

public class DrawingFragment extends BaseFragment {

    @BindView(R.id.llSync)
    LinearLayout llSync;
    @BindView(R.id.btnAddDrawing)
    Button btnAddDrawing;
    @BindView(R.id.tvDataSource)
    TextView tvDataSource;
    @BindView(R.id.ll_Header)
    LinearLayout ll_Header;
    @BindView(R.id.ll_Header_Offline)
    LinearLayout ll_Header_Offline;
    @BindView(R.id.tvNoDataFound)
    TextView tvNoDataFound;
    @BindView(R.id.rvDrawings)
    RecyclerView rvDrawings;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.ivSyncAll)
    ImageView ivSyncAll;
    private ArrayList<String> rowIdList = new ArrayList<>();
    private List<Datum> drawingList = new ArrayList<>();
    private DrawingMenuVM viewModel;
    private RecyclerView.Adapter mAdapter;
    /* for pagination */
    private int limit = 20;
    private int offset = 0;
    private boolean loading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount;
    private BroadcastReceiver projectUpdateBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context mContext, final Intent intent) {
            requireActivity().runOnUiThread(() -> {
                try {
                    if (intent.hasExtra("KEY_FLAG")) {
                        pullToRefreshAPI();
                        checkSyncingVisibility();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    };
    private BroadcastReceiver fileDownloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isAdded()) {
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                    errorMessage("File downloaded successfully", null, false);
                }
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_drawing;
    }

    @Override
    protected void bindView(View view) {
        registerAllBroadcasts();
        viewModel = new ViewModelProvider(this).get(DrawingMenuVM.class);
        observeViewModel();

        /* view bindings */
        setSwipeRefreshView(mSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this::pullToRefreshAPI);
        setUpPagination();
    }

    private void observeViewModel() {

        viewModel.isLoadingLD.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) showRefreshing();
            else hideRefreshing();
        });

        viewModel.isSyncingLD.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) showProgress();
            else dismissProgress();
        });

        viewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (errorModel != null && getActivity() != null) {
//                errorMessage(errorModel.getMessage(), null, false);
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);
            }
        });

        viewModel.offsetLD.observe(getViewLifecycleOwner(), integer -> {
            if (integer != null)
                offset = integer;
        });

        viewModel.drawingLiveData.observe(getViewLifecycleOwner(), dataList -> {

            if (null != dataList && isAdded()) {
                if (offset <= 1) {
                    drawingList.clear();
                }
                drawingList.addAll(dataList);
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
                else {
                    setDrawingAdapter(dataList, CommonMethods.isNetworkAvailable(getActivity()));
                }
                viewModel.updateAllSyncedStatus(drawingList);
                if (drawingList.size() - limit >= 5) {
                    rvDrawings.smoothScrollToPosition(offset - 1);
                }

                tvNoDataFound.setVisibility(drawingList.isEmpty() ? View.VISIBLE : View.GONE);
                tvNoDataFound.setText(getString(R.string.no_data_found));
                loading = !dataList.isEmpty();
            }
        });

        viewModel.allSyncedStatusLd.observe(getViewLifecycleOwner(), allSyncLd -> {
            if (allSyncLd != null && isAdded()) {
                if (ivSyncAll.getVisibility() == View.VISIBLE) {
                    ivSyncAll.setSelected(allSyncLd);
                }
            }
        });

        viewModel.isOffline.observe(getViewLifecycleOwner(), aBoolean -> {
            if (null != aBoolean) {
                setDrawingAdapter(drawingList, aBoolean);
            }
        });

        viewModel.notfyAdaptorLD.observe(getViewLifecycleOwner(), aBoolean -> {
            if (null != aBoolean && null != mAdapter) {
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void init() {
        if (userPermissions != null) {
            CommonMethods.checkVisiblePermission(userPermissions.getDrawings().getCreateDraw(), btnAddDrawing);
        }
        checkSyncingVisibility();
        pullToRefreshAPI();

    }

    private void pullToRefreshAPI() {
        offset = 0;
        viewModel.getAllDrawings(offset, limit);
        updateRowIds();
    }

    /* row Ids */
    private void updateRowIds() {
        rowIdList.clear();
        rowIdList.addAll(BuilderStormApplication.mLocalDatabase.getSyncedDrawings(BuilderStormApplication.mPrefs.getSiteId(),
                BuilderStormApplication.mPrefs.getUserId(), BuilderStormApplication.mPrefs.getProjectId()));

    }

    private List<Datum> getListToSync() {
        List<Datum> dataList = new ArrayList<>();
        for (Datum data : ((DrawingOnlineAdapter) mAdapter).getDrawingList()) {
            if (!rowIdList.contains(data.getId())) {
                dataList.add(data);
            }
        }
        return dataList;
    }

    private boolean alreadyScheduleJob() {
        JobScheduler scheduler = (JobScheduler) requireActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        for (JobInfo jobInfo : scheduler.getAllPendingJobs()) {
            if (jobInfo.getId() == DataNames.SYNC_SERVICE_ID) {
                return true;
            }
        }
        return false;
    }

    private void setDrawingAdapter(List<Datum> drawingList, Boolean isOffline) {
        ivSyncAll.setVisibility(isOffline ? View.INVISIBLE : View.VISIBLE);
        if (isOffline) {
            mAdapter = new DrawingOfflineAdapter(getActivity(), drawingList, new DrawingOfflineAdapter.DrawingCallback() {
                @Override
                public void callMoreInfo(int position) {
                    if (getFragmentManager() != null) {
                        DrawingInfoDialogFragment.newInstance(drawingList.get(position)).show(getFragmentManager(), "");
                    }
                }

                @Override
                public void syncLocalDataBase(int position, String RowID) {

                }
            });
        } else {
            mAdapter = new DrawingOnlineAdapter(getActivity(), drawingList, rowIdList, new DrawingOnlineAdapter.DrawingCallback() {

                @Override
                public void callMoreInfo(int position) {
                    if (getFragmentManager() != null) {
                        DrawingInfoDialogFragment.newInstance(drawingList.get(position)).show(getFragmentManager(), "");
                    }
                }

                @Override
                public void syncLocalDataBase(int position, String rowID, ImageView ivRefresh) {
                    if (CommonMethods.downloadFile(getActivity()) && !rowIdList.contains(rowID)) {
                        viewModel.syncDrawingToDB(drawingList.get(position));
                    }
                }

                @Override
                public void downloadSingleFile(int position, boolean isPdfDownload, String link, String filename) {
                    if (CommonMethods.downloadFile(getActivity())) {
                        if (isAdded()) {
                            if (isPdfDownload) {
                                drawingList.get(position).setPdfClick(true);
                            } else {
                                drawingList.get(position).setDwgClick(true);
                            }
                            mAdapter.notifyDataSetChanged();
                            singleDownloadService(link, filename);
                        }
                    }
                }

                @Override
                public void callMoreDot(int position) {

                    if (getParentFragmentManager() != null) {
                        DrawingActionDialogFragment.newInstance(drawingList.get(position)).show(getFragmentManager(), "Drawing action");
                    }
                }
            });
        }
        rvDrawings.setAdapter(mAdapter);
        if (null != getActivity()) {
            tvNoDataFound.setText(getString(isOffline ? (isInternetAvailable() ? R.string.no_data_online : R.string.no_data_found) : R.string.no_data_found));
        }
        tvDataSource.setVisibility(!drawingList.isEmpty() && getActivity() != null && isInternetAvailable() && isOffline ? View.VISIBLE : View.GONE);

        tvNoDataFound.setVisibility(drawingList.isEmpty() ? View.VISIBLE : View.GONE);

    }

    private void singleDownloadService(String link, String fileName) {
        JobScheduler scheduler = (JobScheduler) requireActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        PersistableBundle dataBundle = new PersistableBundle();
        dataBundle.putString("link", link);
        dataBundle.putString("name", fileName);
        scheduler.schedule(new JobInfo.Builder(DataNames.SINGLE_DOWNLOAD_SERVICE_ID,
                new ComponentName(requireActivity(), DownloadTask.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setExtras(dataBundle)
                .build());
    }

    private void syncAllRequest() {
        if (isNotRefreshing()) {
            if (mAdapter != null) {
                if (!((DrawingOnlineAdapter) mAdapter).getDrawingList().isEmpty()) {
                    updateRowIds();
                    if (!getListToSync().isEmpty()) {
                        JobScheduler scheduler = (JobScheduler) requireActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
                        if (!alreadyScheduleJob()) {
                            if (CommonMethods.downloadFile(getActivity())) {
                                PersistableBundle dataBundle = new PersistableBundle();
                                dataBundle.putString("data", new Gson().toJson(getListToSync()));
                                scheduler.schedule(new JobInfo.Builder(DataNames.SYNC_SERVICE_ID,
                                        new ComponentName(requireActivity(), SyncAllDrawingTask.class))
                                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                                        .setExtras(dataBundle)
                                        .build());
                                checkSyncingVisibility();
                            }
                        } else {
                            errorMessage("Syncing is already in progress", null, false);
                        }
                    } else {
                        errorMessage("*Nothing to sync", null, false);
                    }
                } else {
                    errorMessage("*Nothing to sync", null, false);
                }
            }
        }

    }

    private void checkSyncingVisibility() {
        llSync.setVisibility(alreadyScheduleJob() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        unregisterAllBroadcasts();
        super.onDestroyView();
    }

    private boolean isNotRefreshing() {
        return mSwipeRefreshLayout != null && !mSwipeRefreshLayout.isRefreshing();

    }

    @OnClick({R.id.btnAddDrawing, R.id.ivSyncAll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddDrawing:
                startActivity(new Intent(getActivity(), AddDrawingActivity.class));
                break;
            case R.id.ivSyncAll:
                syncAllRequest();
                break;
        }
    }

    private void setUpPagination() {
        LinearLayoutManager mLayoutManager = (LinearLayoutManager) rvDrawings.getLayoutManager();
        if (mLayoutManager != null) {
            rvDrawings.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
//                    if (null != userPermissions && CommonMethods.isPermissionGranted(userPermissions.getDrawings().getCreateDraw())) {
//                        if (dy > 0 && btnAddDrawing.isShown()) {
//                            btnAddDrawing.hide();
//                        } else {
//                            btnAddDrawing.show();
//                        }
//                    }

                    if (dy > 0) {
                        visibleItemCount = mLayoutManager.getChildCount();
                        totalItemCount = mLayoutManager.getItemCount();
                        pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                        if (loading && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loading = false;
                            paginate();
                        }
                    }
                }
            });
        }

    }

    private void paginate() {
        if (isInternetAvailable()) {
            if (null != drawingList)
                viewModel.getAllDrawings(offset + limit + 1, limit);
//            if (null != drawingList && drawingList.size() >= limit)
//                viewModel.getAllDrawings(offset + limit + 1, limit);
        }
    }

    public void hideRefreshing() {
        if (null != mSwipeRefreshLayout && mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public void showRefreshing() {
        if (null != mSwipeRefreshLayout && !mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    /* Broadcast section */
    private void registerAllBroadcasts() {
        if (null != getActivity()) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(projectUpdateBroadcast, new IntentFilter(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE));
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(fileDownloadReceiver, new IntentFilter(DataNames.INTENT_SINGLE_FILE_DOWNLOAD));
        }
    }

    private void unregisterAllBroadcasts() {
        if (null != getActivity()) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(projectUpdateBroadcast);
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(fileDownloadReceiver);
        }
    }

}
