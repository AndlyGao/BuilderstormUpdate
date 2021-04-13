package com.builderstrom.user.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.MenuItem;
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
import com.builderstrom.user.data.retrofit.modals.CatListing;
import com.builderstrom.user.data.retrofit.modals.PojoToDo;
import com.builderstrom.user.data.retrofit.modals.ToDoClassicFilter;
import com.builderstrom.user.data.retrofit.modals.ToDoFilterModel;
import com.builderstrom.user.data.retrofit.modals.ToDoType;
import com.builderstrom.user.data.retrofit.modals.User;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.ToDoViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.ToDoFilterAdapter;
import com.builderstrom.user.views.adapters.ToDoListAdapter;
import com.builderstrom.user.views.dialogFragments.DateFilterDialogFragment;
import com.builderstrom.user.views.dialogFragments.ToDoFilterDialogFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

import static com.builderstrom.user.views.fragments.HomeMenuFragment.userPermissions;

public class ToDoListFragment extends BaseRecyclerViewFragment {

    @BindView(R.id.tvOverDue) TextView tvOverdue;
    @BindView(R.id.tvWarning) TextView tvWarning;
    @BindView(R.id.tvNormal) TextView tvNormal;
    @BindView(R.id.rvSearchFilter) RecyclerView rvSearchFilter;
    @BindView(R.id.tvDataSource) TextView tvDataSource;
    @BindView(R.id.btnAddToDo) AppCompatButton btnAddToDo;
    @BindView(R.id.viewSync) ImageView viewSync;
    @BindView(R.id.llSync) LinearLayout llSync;
    @BindView(R.id.fab_filter) FabSpeedDial fabSpeedDial;
    private ToDoListAdapter mAdapter;
    private List<PojoToDo> toDoList = new ArrayList<>();
    private List<CatListing> categories = new ArrayList<>();
    private List<User> userListing = new ArrayList<>();
    private List<ToDoClassicFilter> filterList = new ArrayList<>();
    private ToDoViewModel viewModel;
    private ToDoFilterModel filterModel;
    private ToDoFilterAdapter toDoFilterAdapter;
    /* Broadcast Receivers */
    private BroadcastReceiver projectUpdateBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context mContext, final Intent intent) {
            if (null != getActivity()) {
                getActivity().runOnUiThread(() -> {
                    try {
                        if (intent.hasExtra("KEY_FLAG")) {
                            pullDownToRefresh();
                            if (!viewModel.isAlreadyScheduleToDoJob() && getView() != null
                                    && llSync.getVisibility() == View.VISIBLE) {
                                llSync.setVisibility(View.GONE);
                            }
                        } else {
                            if (isAdded() && viewModel.isAlreadyScheduleToDoJob()
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
        return R.layout.fragment_list_to_do;
    }

    @Override
    protected void bindView(View view) {
        filterModel = new ToDoFilterModel();
        filterList = new ArrayList<>();
        registerBroadcasts();
        viewModel = new ViewModelProvider(this).get(ToDoViewModel.class);
        viewModel.getToDoCategories();
        viewModel.getUsers();
        observerViewModel();
        filtrationOptions();
    }

    private void filtrationOptions() {
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override public boolean onMenuItemSelected(MenuItem menuItem) {
                ToDoFilterDialogFragment dialogFragment = null;
                DateFilterDialogFragment mDateFilterDialogFragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.action_status:
                        dialogFragment = ToDoFilterDialogFragment.newInstance(0, "Status", categories, userListing);
                        dialogFragment.setFilterValues((id, name, type) -> {
                            filterModel.setStatusId(String.valueOf(id));
                            filterModel.setStatusName(name);
                            filterApply();
                        });
                        dialogFragment.show(getParentFragmentManager(), "action_status");
                        break;

                    case R.id.action_category:
                        dialogFragment = ToDoFilterDialogFragment.newInstance(1, "Categories", categories, userListing);
                        dialogFragment.setFilterValues((id, name, type) -> {
                            filterModel.setCategoryId(String.valueOf(id));
                            filterModel.setCategoryName(name);
                            filterApply();
                        });
                        dialogFragment.show(getParentFragmentManager(), "action_category");
                        break;

                    case R.id.action_user:
                        dialogFragment = ToDoFilterDialogFragment.newInstance(2, "User Responsible", categories, userListing);
                        dialogFragment.setFilterValues((id, name, type) -> {
                            filterModel.setUserId(String.valueOf(id));
                            filterModel.setUserName(name);
                            filterApply();
                        });
                        dialogFragment.show(getParentFragmentManager(), "action_user");
                        break;

                    case R.id.action_date:
                        mDateFilterDialogFragment = DateFilterDialogFragment.newInstance("Date Range");
                        mDateFilterDialogFragment.setFilterValues((startDate, endDate) -> {
                            filterModel.setStartDate(startDate);
                            filterModel.setEndDate(endDate);
                            filterApply();
                        });
                        mDateFilterDialogFragment.show(getParentFragmentManager(), "action_date");
                        break;
                }
                return super.onMenuItemSelected(menuItem);
            }
        });

        toDoFilterAdapter = new ToDoFilterAdapter(getActivity(), filterList, new ToDoFilterAdapter.CallbackFilter() {
            @Override public void callRequest(String categoryId) {
                if (categoryId.equalsIgnoreCase("1")) {
                    filterModel.setUserId("");
                    filterModel.setUserName("");
                } else if (categoryId.equalsIgnoreCase("2")) {
                    filterModel.setStatusId("");
                    filterModel.setStatusName("");
                } else if (categoryId.equalsIgnoreCase("3")) {
                    filterModel.setCategoryId("");
                    filterModel.setCategoryName("");
                } else if (categoryId.equalsIgnoreCase("4")) {
                    filterModel.setStartDate("");
                    filterModel.setEndDate("");
                }
                filterApply();
            }
        });
        rvSearchFilter.setAdapter(toDoFilterAdapter);
    }

    private void filterApply() {
        filterList.clear();
        ToDoClassicFilter classicFilter = null;
        if (filterModel != null) {
            if (!filterModel.getUserName().isEmpty()) {
                classicFilter = new ToDoClassicFilter();
                classicFilter.setId(filterModel.getUserId());
                classicFilter.setName(filterModel.getUserName());
                classicFilter.setCategory("1");
                filterList.add(classicFilter);
            }
            if (!filterModel.getStatusName().isEmpty()) {
                classicFilter = new ToDoClassicFilter();
                classicFilter.setId(filterModel.getStatusId());
                classicFilter.setName(filterModel.getStatusName());
                classicFilter.setCategory("2");
                filterList.add(classicFilter);
            }
            if (!filterModel.getCategoryName().isEmpty()) {
                classicFilter = new ToDoClassicFilter();
                classicFilter.setId(filterModel.getCategoryId());
                classicFilter.setName(filterModel.getCategoryName());
                classicFilter.setCategory("3");
                filterList.add(classicFilter);
            }
            if (!filterModel.getStartDate().isEmpty()) {
                classicFilter = new ToDoClassicFilter();
                classicFilter.setId("0");
                classicFilter.setName(filterModel.getStartDate() + "-" + filterModel.getEndDate());
                classicFilter.setCategory("4");
                filterList.add(classicFilter);
            }
        }
        pullDownToRefresh();
    }

    private void observerViewModel() {
        viewModel.isRefreshingLD.observe(getViewLifecycleOwner(), refreshing -> {
            if (refreshing != null) {
                if (refreshing) showRefreshing();
                else hideRefreshing();
            }
        });

        viewModel.isLoadingLD.observe(getViewLifecycleOwner(), refreshing -> {
            if (isAdded() && refreshing != null) {
                if (refreshing) showProgress();
                else dismissProgress();
            }
        });

        viewModel.offsetLD.observe(getViewLifecycleOwner(), offsetLD -> {
            if (offsetLD != null && isAdded()) {
                offset = offsetLD;
            }
        });

        viewModel.updateStatusLD.observe(getViewLifecycleOwner(), update -> {
            if (update != null && isAdded()) {
                pullDownToRefresh();
            }
        });

        viewModel.todoListLD.observe(getViewLifecycleOwner(), listModelLd -> {
            if (listModelLd != null) {
                if (offset <= 0) {
                    toDoList.clear();
                }
                toDoList.addAll(listModelLd.getToDoList());
                mAdapter.setOffline(listModelLd.isOffline());
                mAdapter.notifyDataSetChanged();
                fabSpeedDial.setVisibility(listModelLd.isOffline() ? View.GONE : View.VISIBLE);
                toDoFilterAdapter.notifyDataSetChanged();

                if (toDoList.isEmpty()) {
                    showNoDataTextView(isInternetAvailable() ? (listModelLd.isOffline() ?
                            R.string.no_data_online : R.string.no_data_found) : R.string.no_data_found);
                } else {
                    hideNoDataTextView();
                }
                loading = listModelLd.getToDoList().size() == LIMIT;
            }
        });

        viewModel.isOfflineViewLD.observe(getViewLifecycleOwner(), isOfflineLD -> {
            if (isOfflineLD != null && isAdded()) {
                viewSync.setVisibility(isOfflineLD ? View.INVISIBLE : View.VISIBLE);
            }
        });

        viewModel.toDoTypeLD.observe(getViewLifecycleOwner(), totals -> {
            if (totals != null && isAdded()) {
                for (ToDoType toDoType : totals) {
                    switch (toDoType.getName()) {
                        case DataNames.TODO_NORMAL:
                            tvNormal.setText(String.valueOf(toDoType.getTotal()));
                            break;
                        case DataNames.TODO_WARNING:
                            tvWarning.setText(String.valueOf(toDoType.getTotal()));
                            break;
                        case DataNames.TODO_OVERDUE:
                            tvOverdue.setText(String.valueOf(toDoType.getTotal()));
                            break;
                    }
                }

                if (totals.isEmpty()) {
                    tvNormal.setText("0");
                    tvWarning.setText("0");
                    tvOverdue.setText("0");
                }
            }
        });

        viewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (getActivity() != null && errorModel != null) {
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);
            }
        });

        viewModel.notifyAdapterLD.observe(getViewLifecycleOwner(), syncedLD -> {
            if (null != syncedLD && isAdded() && null != mAdapter) {
                if (syncedLD) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        viewModel.categoriesLD.observe(getViewLifecycleOwner(), catListings -> {
            if (catListings != null && isAdded()) {
                categories.clear();
                categories.addAll(catListings);
            }
        });

        viewModel.usersLD.observe(getViewLifecycleOwner(), allUsers -> {
            if (null != getActivity() && null != allUsers) {
                userListing.clear();
                userListing.addAll(allUsers);
            }
        });
//
//        viewModel.allSyncedStatusLd.observe(getViewLifecycleOwner(), allSyncLd -> {
//            if (allSyncLd != null && isAdded()) {
//                if (viewSync.getVisibility() == View.VISIBLE) {
//                    viewSync.setSelected(allSyncLd);
//                }
//            }
//        });
    }

    @OnClick({R.id.ivOverDue, R.id.ivNormal, R.id.ivWarning, R.id.viewSync, R.id.btnAddToDo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivNormal:
                showTooltip(getString(R.string.normal), (ImageView) view);
                break;
            case R.id.ivWarning:
                showTooltip(getString(R.string.warning), (ImageView) view);
                break;
            case R.id.ivOverDue:
                showTooltip(getString(R.string.overdue), (ImageView) view);
                break;
            case R.id.viewSync:
                if (isNotRefreshing()) {
                    if (CommonMethods.downloadFile(getActivity())) {
                        viewModel.syncAllToDos(toDoList);
                    }
                }
                break;
            case R.id.btnAddToDo:
                AddToDoFragment toDoFragment = new AddToDoFragment();
                toDoFragment.setCallback(this::pullDownToRefresh);
                getParentFragmentManager().beginTransaction().add(R.id.flTestContainer,
                        toDoFragment, "add ToDo").addToBackStack("add ToDo").commit();
                break;
        }
    }

    @Override
    protected void pagination() {
        if (viewModel.isInternetAvailable() && toDoList.size() >= LIMIT)
            viewModel.getToDoList(offset + LIMIT, LIMIT, filterModel);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return null;
    }

    @Override
    protected RecyclerView.Adapter getRecyclerAdapter() {
        if (mAdapter == null) {
            mAdapter = new ToDoListAdapter(getActivity(), toDoList, viewModel);
        }
        mAdapter.setUpdateCallBack(this::pullDownToRefresh);
        return mAdapter;
    }

    @Override
    protected void setData() {
        if (userPermissions != null) {
            CommonMethods.checkVisiblePermission(userPermissions.getTodos().getAddEditTodos(), btnAddToDo);
        }
        pullDownToRefresh();
    }

    @Override
    protected void pullDownToRefresh() {
        offset = 0;
        viewModel.updateSyncedList();
//        viewModel.updateUsers();
        viewModel.getToDoList(offset, LIMIT, filterModel);
    }

    @Override
    public void onDestroyView() {
        unregisterAllBroadcasts();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        llSync.setVisibility(viewModel.isInternetAvailable() && viewModel.isAlreadyScheduleToDoJob() ? View.VISIBLE : View.GONE);
    }

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
