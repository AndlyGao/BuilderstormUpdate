package com.builderstrom.user.views.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.modals.DiaryData;
import com.builderstrom.user.repository.retrofit.modals.DiaryLiveModel;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.DiaryViewModel;
import com.builderstrom.user.views.activities.AddDiary;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.DiaryOfflineAdapter;
import com.builderstrom.user.views.adapters.DiaryOnlineAdapter;
import com.builderstrom.user.views.viewInterfaces.ConfirmRetryCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

import static com.builderstrom.user.views.fragments.HomeMenuFragment.userPermissions;

public class DiaryFragment extends BaseFragment {

    @BindView(R.id.btnAddDiary) Button btnAddDiary;
    @BindView(R.id.etDate) EditText etDate;
    @BindView(R.id.tvDiarySubmitted) TextView tvDiarySubmitted;
    @BindView(R.id.tvDataSource) TextView tvDataSource;
    @BindView(R.id.ll_header) LinearLayout ll_header;
    @BindView(R.id.tvNoDataFound) TextView tvNoDataFound;
    @BindView(R.id.rvDiary) RecyclerView rvDiary;
    @BindView(R.id.swipe_container) SwipeRefreshLayout mSwipeRefreshLayout;
    private String inputDate = "";
    private DiaryViewModel viewModel;
    /* for pagination */
    private int limit = 15;
    private int offset = 0;
    private boolean loading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount;
    private List<DiaryData> diaryListLive = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_diary;
    }

    @Override
    protected void bindView(View view) {
        registerAllBroadcasts();

        viewModel = new ViewModelProvider(this).get(DiaryViewModel.class);
        observeViewModel();
        setSwipeRefreshView(mSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this::pullToRefreshAPI);
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

        viewModel.isSuccess.observe(getViewLifecycleOwner(), aBoolean -> {
            if (null != aBoolean) {
                pullToRefreshAPI();
            }
        });

        viewModel.offsetLD.observe(getViewLifecycleOwner(), integer -> {
            if (null != integer) {
                offset = integer;
            }
        });
        /* Online Section */
        viewModel.diaryLiveData.observe(getViewLifecycleOwner(), this::updateDiariesAdapter);

    }


    @Override
    protected void init() {
        /* Add Diary Visibility*/
        if (userPermissions != null) {
            CommonMethods.checkVisiblePermission(userPermissions.getDailyDiaries().getCreateDairy(), btnAddDiary);
        }

        getDateTime();
        pullToRefreshAPI();
        setUpPagination();
    }

    private void getDateTime() {
        inputDate = CommonMethods.getCurrentDate(CommonMethods.DF_2);
        etDate.setText(CommonMethods.convertDate(CommonMethods.DF_2, inputDate, CommonMethods.DF_7));
    }

    private void pullToRefreshAPI() {
        tvDiarySubmitted.setText(String.format("Diary entries for  %s", CommonMethods.convertDate(CommonMethods.DF_2, inputDate, CommonMethods.DF_4)));
        if (null != getActivity()) {
            etDate.setVisibility(CommonMethods.isNetworkAvailable(getActivity()) ? View.VISIBLE : View.GONE);
        }
        offset = 0;
        viewModel.getAllDiaries(inputDate, offset, limit);
    }


    private void updateDiariesAdapter(DiaryLiveModel diaryLiveModel) {
        if (null != getActivity()) {
            if (diaryLiveModel.getOffline()) {
                diaryListLive.clear();
                diaryListLive.addAll(diaryLiveModel.getDiaryList());
                mAdapter = new DiaryOfflineAdapter(getActivity(), diaryListLive);
            } else {
                /* for global reference */
                if (offset <= 1) {
                    diaryListLive.clear();
                }
                diaryListLive.addAll(diaryLiveModel.getDiaryList());
                mAdapter = new DiaryOnlineAdapter(getActivity(), diaryListLive, this::dialogDeleteDiary);
                loading = !diaryLiveModel.getDiaryList().isEmpty();
            }
            rvDiary.setAdapter(mAdapter);
            tvNoDataFound.setText(getString(diaryLiveModel.getOffline() ? (isInternetAvailable() ? R.string.no_data_online : R.string.no_data_found)
                    : R.string.no_data_found));
            tvDataSource.setVisibility(!diaryListLive.isEmpty() && getActivity() != null && isInternetAvailable() && diaryLiveModel.getOffline() ? View.VISIBLE : View.GONE);
            tvNoDataFound.setVisibility(diaryListLive.isEmpty() ? View.VISIBLE : View.GONE);
            ll_header.setVisibility(diaryListLive.isEmpty() ? View.GONE : View.VISIBLE);
        }

    }

    private void dialogDeleteDiary(String title, String diaryID) {
        Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.setContentView(R.layout.dialog_gallery_operations);
        dialog.setCanceledOnTouchOutside(false);
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(String.format("Do you want to delete the '%s' diary item?", title));
        dialog.findViewById(R.id.btnConfirm).setOnClickListener(view -> {
            viewModel.deleteDiaryAPI(diaryID);
            dialog.dismiss();
        });
        dialog.findViewById(R.id.btnCancel).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        unregisterAllBroadcasts();
        super.onDestroyView();
    }

    public void showRefreshing() {
        if (mSwipeRefreshLayout != null && !mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    public void hideRefreshing() {
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }


    /* BroadCasts Sections */
    private BroadcastReceiver projectUpdateBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context mContext, final Intent intent) {
            if (null != getActivity()) {
                getActivity().runOnUiThread(() -> {
                    try {
                        if (intent.hasExtra("KEY_FLAG")) {
                            pullToRefreshAPI();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    };

    private void registerAllBroadcasts() {
        if (null != getActivity()) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(projectUpdateBroadcast, new IntentFilter(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE));
        }
    }

    private void unregisterAllBroadcasts() {
        if (null != getActivity()) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(projectUpdateBroadcast);
        }

    }

    @Override
    public void errorMessage(String message, @Nullable Integer errorId, boolean isFailure) {
        if (isFailure) {
            if (getActivity() != null) {
                ((BaseActivity) getActivity()).showAlert(errorId != null ?
                        getResources().getString(errorId) : message, new ConfirmRetryCallback() {
                    @Override
                    public void onConfirmClicked() {
                        pullToRefreshAPI();
                    }

                    @Override
                    public void onCancelClicked() {
                        popOutFragment();
                    }
                });
            }
        } else {
            super.errorMessage(message, errorId, false);

        }

    }

    @OnClick({R.id.btnAddDiary, R.id.etDate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddDiary:
                startActivity(new Intent(getActivity(), AddDiary.class));
                break;
            case R.id.etDate:
                DatePickerDialog pickerDialog = new DatePickerDialog(requireActivity(), R.style.DatePickerTheme, (view1, year, month, dayOfMonth) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    inputDate = CommonMethods.getCalenderDate(CommonMethods.DF_2, calendar.getTime());
                    etDate.setText(CommonMethods.getCalenderDate(CommonMethods.DF_7, calendar.getTime()));
                    pullToRefreshAPI();
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                pickerDialog.show();
                break;
        }
    }

    private void setUpPagination() {
        LinearLayoutManager mLayoutManager = (LinearLayoutManager) rvDiary.getLayoutManager();
        if (mLayoutManager != null) {
            rvDiary.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
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
            if (null != diaryListLive && diaryListLive.size() >= limit)
                viewModel.getAllDiaries(inputDate, offset + limit + 1, limit);
        }
    }

}
