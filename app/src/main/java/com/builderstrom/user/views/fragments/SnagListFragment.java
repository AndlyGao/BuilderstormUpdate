package com.builderstrom.user.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.modals.Snag;
import com.builderstrom.user.data.retrofit.modals.SnagTotal;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.SnagViewModel;
import com.builderstrom.user.views.activities.AddSnagActivity;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.SnagOfflineAdapter;
import com.builderstrom.user.views.adapters.SnagOnLineAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.builderstrom.user.views.fragments.HomeMenuFragment.userPermissions;

public class SnagListFragment extends BaseRecyclerViewFragment {

    @BindView(R.id.tvDataSource) TextView tvDataSource;
    @BindView(R.id.btnAddSnagList) AppCompatButton btnAddSnag;
    @BindView(R.id.viewSync) ImageView viewSync;
    @BindView(R.id.llLocks) LinearLayout llLocks;
    @BindView(R.id.llSync) LinearLayout llSync;

    /* Locks */
    @BindView(R.id.tvLockCancel) TextView tvLockCancel;
    @BindView(R.id.tvTimelyClosed) TextView tvTimelyClosed;
    @BindView(R.id.tvCloseOver) TextView tvCloseOver;
    @BindView(R.id.tvLockOpen) TextView tvLockOpen;
    @BindView(R.id.tvLockClouser) TextView tvLockClouser;
    @BindView(R.id.tvOpenOver) TextView tvLockOpenOver;

    @BindView(R.id.ivLockCancel) ImageView ivLockCancel;
    @BindView(R.id.ivTimelyClosed) ImageView ivTimelyClosed;
    @BindView(R.id.ivCloseOver) ImageView ivCloseOver;
    @BindView(R.id.ivLockOpen) ImageView ivLockOpen;
    @BindView(R.id.ivLockClouser) ImageView ivLockClouser;
    @BindView(R.id.ivOpenOver) ImageView ivLockOpenOver;

    private RecyclerView.Adapter mAdapter;

    private SnagViewModel viewModel;
    private List<Snag> snagList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_snaglist;
    }

    @Override
    protected void bindView(View view) {
        registerBroadcasts();
        viewModel = new ViewModelProvider(this).get(SnagViewModel.class);
        observerViewModel();
    }

    private void observerViewModel() {
        viewModel.isRefreshingLD.observe(getViewLifecycleOwner(), refreshing -> {
            if (refreshing != null) {
                if (refreshing) showRefreshing();
                else hideRefreshing();
            }
        });

        viewModel.offsetLD.observe(getViewLifecycleOwner(), intOffset -> {
            if (intOffset != null) {
                offset = intOffset;
            }
        });

        viewModel.isOfflineSnagAdapterLD.observe(getViewLifecycleOwner(), isOfflineSnags -> {
            if (isOfflineSnags != null) {
                updateHeader();
                snagList.clear();
                mAdapter = isOfflineSnags ? new SnagOfflineAdapter(getActivity(), snagList) :
                        new SnagOnLineAdapter(getActivity(), snagList, viewModel);
                if (recyclerView != null) {
                    recyclerView.setAdapter(mAdapter);
                }
            }
        });

        viewModel.updateLocksLD.observe(getViewLifecycleOwner(), snagLocksModel -> {
            if (snagLocksModel != null) {
                if (snagLocksModel.getOfflineLocks() != null) {
                    setOfflineLockTopView(snagLocksModel.getOfflineLocks());
                }

                if (snagLocksModel.getOnLineLocks() != null) {
                    updateOnlineHeaderLocks(snagLocksModel.getOnLineLocks());
                }
            }
        });

        viewModel.dataModelLD.observe(getViewLifecycleOwner(), snagDataModel -> {
            if (snagDataModel != null) {
                snagList.addAll(snagDataModel.getSnagList());
                mAdapter.notifyDataSetChanged();
                viewModel.updateAllSyncedStatus(snagList);

                loading = snagDataModel.getSnagList().size() == LIMIT;
                if (snagList.isEmpty()) {
                    showNoDataTextView(snagDataModel.isOffline() ? (isInternetAvailable() ?
                            R.string.no_data_online : R.string.no_data_found)
                            : R.string.no_data_found);
                } else {
                    hideNoDataTextView();
                }

            }
        });

        viewModel.updateSnagLD.observe(getViewLifecycleOwner(), updateSnag -> {
            if (updateSnag != null && updateSnag) {
                pullDownToRefresh();
            }
        });

        viewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (getActivity() != null && errorModel != null) {
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);
            }
        });

        viewModel.notifySnagAdapterLD.observe(getViewLifecycleOwner(), syncedLD -> {
            if (null != syncedLD && null != getActivity()) {
                mAdapter.notifyDataSetChanged();
            }
        });

        viewModel.allSyncedStatusLd.observe(getViewLifecycleOwner(), allSyncLd -> {
            if (allSyncLd != null && isAdded()) {
                if (viewSync.getVisibility() == View.VISIBLE) {
                    viewSync.setSelected(allSyncLd);
                }
            }
        });
    }

    @OnClick({R.id.ivLockCancel, R.id.ivTimelyClosed, R.id.ivCloseOver, R.id.ivLockOpen,
            R.id.ivLockClouser, R.id.ivOpenOver, R.id.viewSync, R.id.btnAddSnagList})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivLockCancel:
                showTooltip(tvLockCancel.getText().toString().trim() + " Cancelled", ivLockCancel);
                break;
            case R.id.ivTimelyClosed:
                showTooltip(tvTimelyClosed.getText().toString().trim() + " Closed On Time", ivTimelyClosed);
                break;
            case R.id.ivCloseOver:
                showTooltip(tvCloseOver.getText().toString().trim() + " Closed Overdue", ivCloseOver);
                break;
            case R.id.ivLockOpen:
                showTooltip(tvLockOpen.getText().toString().trim() + " Open", ivLockOpen);
                break;
            case R.id.ivLockClouser:
                showTooltip(tvLockClouser.getText().toString().trim() + " For Closure", ivLockClouser);
                break;
            case R.id.ivOpenOver:
                showTooltip(tvLockOpenOver.getText().toString().trim() + " Open Overdue", ivLockOpenOver);
                break;
            case R.id.viewSync:
                if (isNotRefreshing()) {
                    if (CommonMethods.downloadFile(getActivity())) {
                        viewModel.syncAllSnagList(snagList);
                    }
                }
                break;
            case R.id.btnAddSnagList:
                startActivity(new Intent(getActivity(), AddSnagActivity.class));
                break;
        }
    }

    @Override
    protected void pagination() {
        if (null != snagList && snagList.size() >= LIMIT)
            viewModel.getSnagList(offset + LIMIT, LIMIT);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return null;
    }

    @Override
    protected RecyclerView.Adapter getRecyclerAdapter() {
        return null;
    }

    @Override
    protected void setData() {
        if (userPermissions != null) {
            CommonMethods.checkVisiblePermission(userPermissions.getDefectReport().getCreate(), btnAddSnag);
        }
        pullDownToRefresh();
    }

    @Override
    protected void pullDownToRefresh() {
        offset = 0;
        viewModel.updateSyncedList();
        viewModel.updateUsers();
        viewModel.getSnagList(offset, LIMIT);

    }

    @Override
    public void onResume() {
        super.onResume();
        llSync.setVisibility(viewModel.isInternetAvailable() && viewModel.isAlreadyScheduleSnagJob() ? View.VISIBLE : View.GONE);
    }

    private void updateOnlineHeaderLocks(List<SnagTotal> snagTotals) {
        /* top locks */
        llLocks.setVisibility(isAllLocksEmpty(snagTotals) ? View.GONE : View.VISIBLE);
        for (SnagTotal snagTotal : snagTotals) {
            switch (snagTotal.getStatus()) {
                case 0:
                    tvLockOpen.setText(String.valueOf(snagTotal.getTotal()));
                    setTopHeaderLock(tvLockOpen, ivLockOpen, snagTotal.getTotal() == 0 ? View.GONE : View.VISIBLE);
                    break;
                case 1:
                    tvLockClouser.setText(String.valueOf(snagTotal.getTotal()));
                    setTopHeaderLock(tvLockClouser, ivLockClouser, snagTotal.getTotal() == 0 ? View.GONE : View.VISIBLE);
                    break;
                case 2:
                    tvTimelyClosed.setText(String.valueOf(snagTotal.getTotal()));
                    setTopHeaderLock(tvTimelyClosed, ivTimelyClosed, snagTotal.getTotal() == 0 ? View.GONE : View.VISIBLE);
                    break;
                case 3:
                    tvLockCancel.setText(String.valueOf(snagTotal.getTotal()));
                    setTopHeaderLock(tvLockCancel, ivLockCancel, snagTotal.getTotal() == 0 ? View.GONE : View.VISIBLE);
                    break;

                case 4:
                    tvLockOpenOver.setText(String.valueOf(snagTotal.getTotal()));
                    setTopHeaderLock(tvLockOpenOver, ivLockOpenOver, snagTotal.getTotal() == 0 ? View.GONE : View.VISIBLE);
                    break;
                case 5:
                    tvCloseOver.setText(String.valueOf(snagTotal.getTotal()));
                    setTopHeaderLock(tvCloseOver, ivCloseOver, snagTotal.getTotal() == 0 ? View.GONE : View.VISIBLE);
                    break;
            }
        }
    }

    private void setOfflineLockTopView(int[] locks) {
        tvLockCancel.setText(String.valueOf(locks[0]));
        setTopHeaderLock(tvLockCancel, ivLockCancel, locks[0] == 0 ? View.GONE : View.VISIBLE);
        /* Snag Clouser */
        tvLockClouser.setText(String.valueOf(locks[1]));
        setTopHeaderLock(tvLockClouser, ivLockClouser, locks[1] == 0 ? View.GONE : View.VISIBLE);
        /* Snag Timely Closed */
        tvTimelyClosed.setText(String.valueOf(locks[2]));
        setTopHeaderLock(tvTimelyClosed, ivTimelyClosed, locks[2] == 0 ? View.GONE : View.VISIBLE);
        /* Snag Close over */
        tvCloseOver.setText(String.valueOf(locks[3]));
        setTopHeaderLock(tvCloseOver, ivCloseOver, locks[3] == 0 ? View.GONE : View.VISIBLE);
        /* Snag Open */
        tvLockOpen.setText(String.valueOf(locks[4]));
        setTopHeaderLock(tvLockOpen, ivLockOpen, locks[4] == 0 ? View.GONE : View.VISIBLE);
        /* Snag Open Over */
        tvLockOpenOver.setText(String.valueOf(locks[5]));
        setTopHeaderLock(tvLockOpenOver, ivLockOpenOver, locks[5] == 0 ? View.GONE : View.VISIBLE);

        llLocks.setVisibility(CommonMethods.isArrayEmpty(locks) ? View.GONE : View.VISIBLE);

    }

    private boolean isAllLocksEmpty(List<SnagTotal> snagTotals) {
        if (snagTotals != null) {
            for (SnagTotal total : snagTotals) {
                if (total.getTotal() > 0) return false;
            }
        }
        return true;
    }

    private void setTopHeaderLock(TextView tvLock, ImageView ivLock, int lockVisibility) {
        tvLock.setVisibility(lockVisibility);
        ivLock.setVisibility(lockVisibility);
    }

    private void updateHeader() {
        if (isAdded())
            viewSync.setVisibility(isInternetAvailable() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        unregisterAllBroadcasts();
        super.onDestroyView();
    }

    /* Broadcast Receivers */
    private BroadcastReceiver projectUpdateBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context mContext, final Intent intent) {
            if (null != getActivity()) {
                getActivity().runOnUiThread(() -> {
                    try {
                        if (intent.hasExtra("KEY_FLAG")) {
                            pullDownToRefresh();
                            if (isAdded() && !viewModel.isAlreadyScheduleSnagJob()
                                    && llSync.getVisibility() == View.VISIBLE) {
                                llSync.setVisibility(View.GONE);
                            }
                        } else {
                            if (isAdded() && viewModel.isAlreadyScheduleSnagJob()
                                    && llSync.getVisibility() == View.GONE) {
                                llSync.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    };

    private void registerBroadcasts() {
        if (getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(projectUpdateBroadcast, new IntentFilter(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE));
        }
    }

    private void unregisterAllBroadcasts() {
        if (getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(projectUpdateBroadcast);
        }
    }


}
