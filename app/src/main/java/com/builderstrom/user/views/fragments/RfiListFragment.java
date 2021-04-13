package com.builderstrom.user.views.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.modals.PojoAttachment;
import com.builderstrom.user.data.retrofit.modals.Rfi;
import com.builderstrom.user.data.retrofit.modals.Rfitotal;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.PermissionUtils;
import com.builderstrom.user.viewmodels.RFIViewModel;
import com.builderstrom.user.views.activities.AddRFIActivity;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.RfiOfflineAdapter;
import com.builderstrom.user.views.adapters.RfiOnlineAdapter;
import com.builderstrom.user.views.viewInterfaces.OnCameraAndStorageGrantedListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.builderstrom.user.views.fragments.HomeMenuFragment.userPermissions;

public class RfiListFragment extends BaseRecyclerViewFragment {

    @BindView(R.id.llSync) LinearLayout llSync;
    @BindView(R.id.ivSyncAll) ImageView ivSyncAll;
    @BindView(R.id.tvDataSource) TextView tvDataSource;
    @BindView(R.id.rlLocks) RelativeLayout rlLocks;
    @BindView(R.id.btnAddRfi) AppCompatButton btnAddRfi;

    /* Locks Views*/
    @BindView(R.id.tvRFICancel) TextView tvRFICancel;
    @BindView(R.id.tvTimelyClosed) TextView tvTimelyClosed;
    @BindView(R.id.tvCloseOver) TextView tvCloseOver;
    @BindView(R.id.tvRFIOpen) TextView tvRFIOpen;
    @BindView(R.id.tvRFIClouser) TextView tvRFIClouser;
    @BindView(R.id.tvRFIOpenOver) TextView tvRFIOpenOver;

    @BindView(R.id.ivRfiCancel) ImageView ivRFICancel;
    @BindView(R.id.ivTimelyClosed) ImageView ivTimelyClosed;
    @BindView(R.id.ivCloseOver) ImageView ivCloseOver;
    @BindView(R.id.ivRFIOpen) ImageView ivRFIOpen;
    @BindView(R.id.ivRFIClouser) ImageView ivRFIClouser;
    @BindView(R.id.ivRFIOpenOver) ImageView ivRFIOpenOver;

    private int currentAdapterPosition = -1;
    private List<Rfi> rfiList = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private PermissionUtils permissionUtils;
    private RFIViewModel viewModel;
    private int OpenRFI = 0, CancelledRFI = 0, for_ClosureRFI = 0, closed_timeRFI = 0, open_OverdueRFI = 0,
            closed_OverdueRFI = 0;
    /* Broadcasts Section */
    private BroadcastReceiver projectUpdateBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context mContext, final Intent intent) {
            if (null != getActivity()) {
                getActivity().runOnUiThread(() -> {
                    try {
                        if (intent.hasExtra("KEY_FLAG")) {
                            pullDownToRefresh();
                            if (!viewModel.isAlreadyScheduleRfiJob() && getView() != null
                                    && llSync.getVisibility() == View.VISIBLE) {
                                llSync.setVisibility(View.GONE);

                            }
                        } else {
                            if (isAdded() && viewModel.isAlreadyScheduleRfiJob()
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

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_rfi;
    }

    @Override
    protected void bindView(View view) {
        registerAllBroadcasts();
        viewModel = new ViewModelProvider(this).get(RFIViewModel.class);
        observeViewModel();
    }

    @Override
    public void onDestroyView() {
        unregisterBroadcasts();
        super.onDestroyView();
    }

    private void observeViewModel() {

        viewModel.isLoadingLD.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) showRefreshing();
            else hideRefreshing();
        });

        viewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (errorModel != null && getActivity() != null) {
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);
            }
        });

        viewModel.isOnlineAdapterLD.observe(getViewLifecycleOwner(), isOnlineLd -> {
            if (isOnlineLd != null && isAdded()) {
                rfiList.clear();
                if (isOnlineLd) {
                    adapter = new RfiOnlineAdapter(getActivity(), rfiList, new RfiOnlineAdapter.CallbackRfi() {
                        @Override
                        public void addAnswer(String answerString, int position) {
                            viewModel.addAnswerToRfi(answerString, rfiList.get(position).getId(),
                                    rfiList.get(position).getAnswerAttachmentFiles());
                        }

                        @Override
                        public void initiateFilePicker(int position, String answerText) {
                            currentAdapterPosition = position;
                            rfiList.get(position).setAnswerText(answerText);
                            checkPermission();
                        }

                    }, viewModel);
                } else {
                    adapter = new RfiOfflineAdapter(getActivity(), rfiList, new RfiOfflineAdapter.CallbackRfi() {
                        @Override
                        public void addAnswer(String answerString, int position) {
                            rfiList.get(position).setIsAnswered(true);
                            rfiList.get(position).setAnswer(answerString);
                            rfiList.get(position).setAnswered_by(viewModel.mPrefs.getUserName());
                            rfiList.get(position).setAnswered_on(CommonMethods.getCurrentDate(CommonMethods.DF_1));
                            List<PojoAttachment> attachmentList = new ArrayList<>();
                            if (rfiList.get(position).getAnswerAttachmentFiles() != null) {
                                for (String imagepath : rfiList.get(position).getAnswerAttachmentFiles()) {
                                    PojoAttachment attachment = new PojoAttachment();
                                    attachment.setOriginal_name(CommonMethods.getFileNameFromPath(imagepath));
                                    attachmentList.add(attachment);
                                }
                                rfiList.get(position).setAnswerfiles(attachmentList);
                            }

                            viewModel.mDatabase.updateRFI(rfiList.get(position).getId(),
                                    new Gson().toJson(rfiList.get(position)),
                                    "1", "1");
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void initiateFilePicker(int position, String answerText) {
                            currentAdapterPosition = position;
                            rfiList.get(position).setAnswerText(answerText);
                            checkPermission();
                        }
                    });
                }
                if (recyclerView != null) {
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        viewModel.isSuccess.observe(getViewLifecycleOwner(), aBoolean -> {
            if (null != aBoolean) {
                pullDownToRefresh();
            }
        });

        viewModel.offsetLD.observe(getViewLifecycleOwner(), integer -> {
            if (null != integer) {
                offset = integer;
            }
        });

        viewModel.rfiModelData.observe(getViewLifecycleOwner(), modelData -> {
            if (null != modelData && isAdded()) {
                ivSyncAll.setVisibility(modelData.getOfflineLocks() == null ? View.VISIBLE : View.INVISIBLE);
                rfiList.addAll(modelData.getRfiList());
                viewModel.updateAllSyncRfiStatus(rfiList);
                adapter.notifyDataSetChanged();
                loading = modelData.getRfiList().size() == LIMIT;
                if (modelData.getLocks() == null) {
                    setOfflineLockTopView(modelData.getOfflineLocks());
                    tvDataSource.setVisibility(!modelData.getRfiList().isEmpty()
                            && getActivity() != null && isInternetAvailable() ? View.VISIBLE : View.GONE);
                } else {
                    setOnLineHeaderLocks(modelData.getLocks());
                    tvDataSource.setVisibility(View.GONE);
                }
                if (rfiList.isEmpty()) {
                    showNoDataTextView(modelData.getLocks() == null ? (isInternetAvailable() ?
                            R.string.no_data_online : R.string.no_data_found)
                            : R.string.no_data_found);
                } else {
                    hideNoDataTextView();
                }

            }
        });

        viewModel.notifyRfiAdapterLd.observe(getViewLifecycleOwner(), isNotifiedLd -> {
            if (isNotifiedLd != null && isAdded() && isNotifiedLd) {
                if (adapter != null) adapter.notifyDataSetChanged();
            }
        });

        viewModel.allSyncedRfiLD.observe(getViewLifecycleOwner(), allSyncedLD -> {
            if (allSyncedLD != null && isAdded()) {
                if (ivSyncAll.getVisibility() == View.VISIBLE) {
                    ivSyncAll.setSelected(allSyncedLD);
                }
            }
        });
    }

    @Override
    protected void pagination() {
        if (isInternetAvailable() && null != rfiList && rfiList.size() >= LIMIT) {
            viewModel.getAllRFI(CommonMethods.getCurrentDate(CommonMethods.DF_2),
                    offset + LIMIT, LIMIT);
        }
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
            CommonMethods.checkVisiblePermission(userPermissions.getRfi().getRfiCreate(), btnAddRfi);
        }
        viewModel.updateUsers();        /* get All  RFI users*/
        pullDownToRefresh();
    }

    @Override
    protected void pullDownToRefresh() {
        offset = 0;
        OpenRFI = 0;
        CancelledRFI = 0;
        for_ClosureRFI = 0;
        closed_timeRFI = 0;
        open_OverdueRFI = 0;
        closed_OverdueRFI = 0;
        viewModel.getAllRFI(CommonMethods.getCurrentDate(CommonMethods.DF_2), offset, LIMIT);
    }

    @OnClick({R.id.ivRfiCancel, R.id.ivTimelyClosed, R.id.ivCloseOver, R.id.ivRFIOpen,
            R.id.ivRFIClouser, R.id.ivRFIOpenOver, R.id.btnAddRfi, R.id.ivSyncAll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivRfiCancel:
                showTooltip(tvRFICancel.getText().toString().trim() + " Cancelled", ivRFICancel);
                break;
            case R.id.ivTimelyClosed:
                showTooltip(tvTimelyClosed.getText().toString().trim() + " Closed On Time", ivTimelyClosed);
                break;
            case R.id.ivCloseOver:
                showTooltip(tvCloseOver.getText().toString().trim() + " Closed Overdue", ivCloseOver);
                break;
            case R.id.ivRFIOpen:
                showTooltip(tvRFIOpen.getText().toString().trim() + " Open", ivRFIOpen);
                break;
            case R.id.ivRFIClouser:
                showTooltip(tvRFIClouser.getText().toString().trim() + " For Closure", ivRFIClouser);
                break;
            case R.id.ivRFIOpenOver:
                showTooltip(tvRFIOpenOver.getText().toString().trim() + " Open Overdue", ivRFIOpenOver);
                break;
            case R.id.btnAddRfi:
                startActivity(new Intent(getActivity(), AddRFIActivity.class));
                break;
            case R.id.ivSyncAll:
                if (isNotRefreshing()) {
                    if (CommonMethods.downloadFile(getActivity())) {
                        viewModel.syncAllRfiList(rfiList);
                    }
                }
                break;
        }
    }

    private void setOfflineLockTopView(int[] locks) {
        tvRFICancel.setText(String.valueOf(locks[0]));
        setTopHeaderLock(tvRFICancel, ivRFICancel, locks[0] == 0 ? View.GONE : View.VISIBLE);
        /* RFI Clouser */
        tvRFIClouser.setText(String.valueOf(locks[1]));
        setTopHeaderLock(tvRFIClouser, ivRFIClouser, locks[1] == 0 ? View.GONE : View.VISIBLE);
        /* RFI Timely Closed */
        tvTimelyClosed.setText(String.valueOf(locks[2]));
        setTopHeaderLock(tvTimelyClosed, ivTimelyClosed, locks[2] == 0 ? View.GONE : View.VISIBLE);
        /* RFI Close over */
        tvCloseOver.setText(String.valueOf(locks[3]));
        setTopHeaderLock(tvCloseOver, ivCloseOver, locks[3] == 0 ? View.GONE : View.VISIBLE);
        /* RFI Open */
        tvRFIOpen.setText(String.valueOf(locks[4]));
        setTopHeaderLock(tvRFIOpen, ivRFIOpen, locks[4] == 0 ? View.GONE : View.VISIBLE);
        /* RFI Timely Closed */
        tvRFIOpenOver.setText(String.valueOf(locks[5]));
        setTopHeaderLock(tvRFIOpenOver, ivRFIOpenOver, locks[5] == 0 ? View.GONE : View.VISIBLE);

        rlLocks.setVisibility(CommonMethods.isArrayEmpty(locks) ? View.GONE : View.VISIBLE);
    }

    private void setOnLineHeaderLocks(List<Rfitotal> rfiTotals) {
        rlLocks.setVisibility(rfiList.isEmpty() ? View.GONE : View.VISIBLE);
        for (Rfitotal rfitotal : rfiTotals) {
            switch (rfitotal.getStatus()) {
                case 0:
                    OpenRFI = OpenRFI + rfitotal.getTotal();
                    tvRFIOpen.setText(String.valueOf(OpenRFI));
                    setTopHeaderLock(tvRFIOpen, ivRFIOpen, OpenRFI == 0 ? View.GONE : View.VISIBLE);
                    break;
                case 1:
                    CancelledRFI = CancelledRFI + rfitotal.getTotal();
                    tvRFICancel.setText(String.valueOf(CancelledRFI));
                    setTopHeaderLock(tvRFICancel, ivRFICancel, CancelledRFI == 0 ? View.GONE : View.VISIBLE);
                    break;
                case 2:
                    for_ClosureRFI = for_ClosureRFI + rfitotal.getTotal();
                    tvRFIClouser.setText(String.valueOf(for_ClosureRFI));
                    setTopHeaderLock(tvRFIClouser, ivRFIClouser, for_ClosureRFI == 0 ? View.GONE : View.VISIBLE);
                    break;
                case 3:
                    closed_timeRFI = closed_timeRFI + rfitotal.getTotal();
                    tvTimelyClosed.setText(String.valueOf(closed_timeRFI));
                    setTopHeaderLock(tvTimelyClosed, ivTimelyClosed, closed_timeRFI == 0 ? View.GONE : View.VISIBLE);
                    break;
                case 4:
                    open_OverdueRFI = open_OverdueRFI + rfitotal.getTotal();
                    tvRFIOpenOver.setText(String.valueOf(open_OverdueRFI));
                    setTopHeaderLock(tvRFIOpenOver, ivRFIOpenOver, open_OverdueRFI == 0 ? View.GONE : View.VISIBLE);
                    break;
                case 5:
                    closed_OverdueRFI = closed_OverdueRFI + rfitotal.getTotal();
                    tvCloseOver.setText(String.valueOf(closed_OverdueRFI));
                    setTopHeaderLock(tvCloseOver, ivCloseOver, closed_OverdueRFI == 0 ? View.GONE : View.VISIBLE);
                    break;
            }
        }
    }

    private void setTopHeaderLock(TextView textView, ImageView imageView, int visibility) {
        textView.setVisibility(visibility);
        imageView.setVisibility(visibility);
    }

    /* Permissions */
    private void checkPermission() {
        permissionUtils = new PermissionUtils(getActivity());
        permissionUtils.setListener(new OnCameraAndStorageGrantedListener() {
            @Override
            public void onPermissionsGranted() {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "ChooseFile"), CommonMethods.PHOTO_REQUEST_CODE);
            }

            @Override
            public void onPermissionRefused(String whichOne) {
                errorMessage(whichOne, null, false);
            }
        });
        permissionUtils.checkPermissions();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonMethods.PHOTO_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uriFile = data.getData();
                if (uriFile != null) {
                    String fileLocation = CommonMethods.getFilePathFromURI(getActivity(), uriFile);
                    if (!rfiList.isEmpty()) {
                        if (null == rfiList.get(currentAdapterPosition).getAnswerAttachmentFiles()) {
                            rfiList.get(currentAdapterPosition).setAnswerAttachmentFiles(new ArrayList<>());
                        }
                        rfiList.get(currentAdapterPosition).getAnswerAttachmentFiles().add(fileLocation);
                        adapter.notifyItemChanged(currentAdapterPosition);
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtils.STORAGE_REQUEST:
            case PermissionUtils.CAMERA_REQUEST:
                permissionUtils.verifyResults(requestCode, grantResults);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        llSync.setVisibility(viewModel.isInternetAvailable() && viewModel.isAlreadyScheduleRfiJob() ? View.VISIBLE : View.GONE);
    }

    private void registerAllBroadcasts() {
        if (null != getActivity()) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(projectUpdateBroadcast, new IntentFilter(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE));
        }
    }

    private void unregisterBroadcasts() {
        if (null != getActivity()) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(projectUpdateBroadcast);
        }
    }

}
