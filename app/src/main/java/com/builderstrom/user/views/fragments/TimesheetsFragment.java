package com.builderstrom.user.views.fragments;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.modals.ReturnOverviewhtmldetail;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.TimeSheetViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.TimeSheetListAdapter;
import com.builderstrom.user.views.adapters.TimeSheetOfflineListAdapter;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;


public class TimesheetsFragment extends BaseFragment {

    private TimeSheetViewModel viewModel;

    @BindView(R.id.etSelectedDate)
    EditText etSelectedDate;
    @BindView(R.id.tvNoDataFound)
    TextView tvNoData;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvTotalHours)
    TextView tvTotalHours;
    @BindView(R.id.ivNextWeek)
    ImageView ivNext;
    @BindView(R.id.ivPrevWeek)
    ImageView ivPrevious;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private ReturnOverviewhtmldetail menuSetting;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_timesheets;
    }

    @Override
    protected void bindView(View view) {
        /* view model*/
        viewModel = new ViewModelProvider(this).get(TimeSheetViewModel.class);
        observeViewModel();

        mSwipeRefreshLayout.setOnRefreshListener(this::pullDownToRefresh);
        setSwipeRefreshView(mSwipeRefreshLayout);
    }


    @OnClick({R.id.etSelectedDate, R.id.ivNextWeek, R.id.ivPrevWeek})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.etSelectedDate:
                if (null != getActivity()) {
                    DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), R.style.DatePickerTheme, (view1, year, month, dayOfMonth) -> {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        setUpDate(CommonMethods.getCalenderDate(CommonMethods.DF_8, calendar.getTime()));
                    }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    pickerDialog.show();
                }
                break;
            case R.id.ivNextWeek:
                setUpDate(CommonMethods.weeklySwitch(etSelectedDate.getText().toString().trim(), true));
                break;
            case R.id.ivPrevWeek:
                setUpDate(CommonMethods.weeklySwitch(etSelectedDate.getText().toString().trim(), false));
                break;
        }
    }


    private void observeViewModel() {

        viewModel.isRefreshing.observe(getViewLifecycleOwner(), aBoolean -> {
            if (null != aBoolean) {
                if (aBoolean) {
                    showRefreshing();
                } else {
                    hideRefreshing();
                }
            }
        });

        viewModel.timeSheetLiveData.observe(getViewLifecycleOwner(), timeSheetList -> {
            if (null != timeSheetList) {
                setSelectionView();
                mRecyclerView.setAdapter(new TimeSheetListAdapter(getActivity(), timeSheetList, viewModel));
                tvNoData.setVisibility(timeSheetList.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });

        viewModel.timeSheetOfflineLiveData.observe(getViewLifecycleOwner(), timeSheetOfflineList -> {
            if (null != timeSheetOfflineList) {
                setSelectionView();
                mRecyclerView.setAdapter(new TimeSheetOfflineListAdapter(getActivity(), timeSheetOfflineList, viewModel, menuSetting));
                tvNoData.setVisibility(timeSheetOfflineList.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });

        viewModel.totalHoursLiveData.observe(getViewLifecycleOwner(), totalHours -> tvTotalHours.setText(String.format("Week Hours: %s", totalHours)));

        viewModel.weekDateLiveData.observe(getViewLifecycleOwner(), currentDate -> {
            if (null != currentDate) {
                etSelectedDate.setText(currentDate);
            }
        });

        viewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (errorModel != null && getActivity() != null) {
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);
            }
        });

        viewModel.menuSetting.observe(getViewLifecycleOwner(), returnOverviewHtmlDetail -> {
            menuSetting = returnOverviewHtmlDetail;
        });


    }

    private void setSelectionView() {
        etSelectedDate.setVisibility(isInternetAvailable() ? View.VISIBLE : View.GONE);
        ivNext.setVisibility(isInternetAvailable() ? View.VISIBLE : View.GONE);
        ivPrevious.setVisibility(isInternetAvailable() ? View.VISIBLE : View.GONE);
    }

    private void pullDownToRefresh() {
        viewModel.getUserSetting();
        viewModel.updateTimeSheet(CommonMethods.convertDate(CommonMethods.DF_8, etSelectedDate.getText().toString().trim(), CommonMethods.DF_2), false);
    }

    @Override
    protected void init() {
        tvUserName.setText(BuilderStormApplication.mPrefs.getUserName());
        setUpDate(CommonMethods.getCurrentDate(CommonMethods.DF_8));
    }

    @Override
    public void onPause() {
        unregisterBroadcasts();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerBroadcasts();
    }

    private void setUpDate(String date) {
        etSelectedDate.setText(date);
        pullDownToRefresh();
    }

    public void showRefreshing() {
        if (null != mSwipeRefreshLayout && !mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    public void hideRefreshing() {
        if (null != mSwipeRefreshLayout && mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    /* Broadcast Receivers */
    private BroadcastReceiver projectUpdateBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context mContext, Intent intent) {
            if (null != getActivity()) {
                getActivity().runOnUiThread(() -> {
                    try {
                        if (intent.hasExtra("KEY_FLAG")) {
                            pullDownToRefresh();
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

    private void unregisterBroadcasts() {
        if (getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(projectUpdateBroadcast);
        }
    }

}
