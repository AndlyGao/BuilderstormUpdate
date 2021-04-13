package com.builderstrom.user.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.appcompat.widget.ListPopupWindow;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.modals.ProjectDetails;
import com.builderstrom.user.viewmodels.PMViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.PMAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ProjManageFragment extends BaseRecyclerViewFragment {

    @BindView(R.id.etStatusSearch)
    EditText etStatusSearch;
    @BindView(R.id.etStageSearch)
    EditText etStageSearch;
    private RecyclerView.Adapter adapter;
    private PMViewModel viewModel;
    private List<ProjectDetails> projectList = new ArrayList<>();
    private List<String> statusList = new ArrayList<>();
    private List<String> stagesList = new ArrayList<>();
    private String searchStatus = "", searchStage = "", projectId = "";
    /* Broadcasts Section */
    private BroadcastReceiver projectUpdateBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context mContext, final Intent intent) {
            if (null != getActivity()) {
                getActivity().runOnUiThread(() -> {
                    try {
                        if (intent.hasExtra("KEY_FLAG")) {
                            projectId = viewModel.mPrefs.getProjectId();
                            pullDownToRefresh();
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
        return R.layout.fragment_pro_manage;
    }

    @Override
    protected void bindView(View view) {
        registerAllBroadcasts();
        viewModel = new ViewModelProvider(this).get(PMViewModel.class);
       /* adapter = new PMAdapter(getActivity(), projectList);
        if (recyclerView != null) {
            recyclerView.setAdapter(adapter);
        }*/
        observeViewModel();

    }

    @Override
    public void onDestroyView() {
        unregisterBroadcasts();
        super.onDestroyView();
    }

    @Override
    protected void pagination() {
        if (isInternetAvailable() && null != projectList && projectList.size() >= LIMIT) {
            viewModel.getAllProject(searchStage, searchStatus, projectId, offset + LIMIT, LIMIT);
        }
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return null;
    }

    @Override
    protected RecyclerView.Adapter getRecyclerAdapter() {
        if (adapter == null) {
            adapter = new PMAdapter(getActivity(), projectList, viewModel);
        }
        return adapter;
    }

    @Override
    protected void setData() {
        offset = 0;
        pullDownToRefresh();
    }

    @Override
    protected void pullDownToRefresh() {
        offset = 0;
        viewModel.getAllProject(searchStage, searchStatus, projectId, offset, LIMIT);
    }

    @OnClick({R.id.etStatusSearch, R.id.etStageSearch})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etStatusSearch:
                openAllStatusPopUp();
                break;
            case R.id.etStageSearch:
                openAllStagesPopUp();
                break;
        }
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

        viewModel.isInitial.observe(getViewLifecycleOwner(), isCleared -> {
            if (isCleared) {
                projectList.clear();
                adapter.notifyDataSetChanged();
            }
        });

        viewModel.statusLD.observe(getViewLifecycleOwner(), status -> {
            statusList.clear();
            if (status != null && !status.isEmpty()) {
                statusList.add(0, "Select Status");
                statusList.addAll(status);
            }

        });
        viewModel.stagesLD.observe(getViewLifecycleOwner(), stages -> {
            stagesList.clear();
            if (stages != null && !stages.isEmpty()) {
                stagesList.add(0, "Select Stage");
                stagesList.addAll(stages);
            }
        });

        viewModel.projectsLD.observe(getViewLifecycleOwner(), projectDetails -> {
            if (null != projectDetails && isAdded()) {
                projectList.addAll(projectDetails);
                loading = projectDetails.size() == LIMIT;
                adapter.notifyDataSetChanged();
            }

            if (projectList.isEmpty()) {
                showNoDataTextView(projectList.isEmpty() ? (isInternetAvailable() ?
                        R.string.no_data_found : R.string.no_data_found)
                        : R.string.no_data_found);
            } else {
                hideNoDataTextView();
            }
        });
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

    private void openAllStatusPopUp() {
        if (null != getActivity()) {
            if (!statusList.isEmpty()) {
                // show popup
                ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
                listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(),
                        R.layout.row_dropdown, R.id.tvDropDown, statusList));
                listPopupWindow.setAnchorView(etStatusSearch);
                listPopupWindow.setModal(true);
                listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                    if (position > 0) {
                        searchStatus = statusList.get(position).trim();
                        etStatusSearch.setText(searchStatus);

                    } else {
                        searchStatus = "";
                        etStatusSearch.setText(statusList.get(0));
                    }
                    offset = 0;
                    viewModel.getAllProject(searchStage, searchStatus, projectId, offset, LIMIT);
                    listPopupWindow.dismiss();
                });
                listPopupWindow.show();
            } else {
                errorMessage("No Status Found", null, false);
            }
        }
    }

    private void openAllStagesPopUp() {
        if (null != getActivity()) {
            if (!stagesList.isEmpty()) {
                // show popup
                ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
                listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(),
                        R.layout.row_dropdown, R.id.tvDropDown, stagesList));
                listPopupWindow.setAnchorView(etStageSearch);
                listPopupWindow.setModal(true);
                listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                    if (position > 0) {
                        searchStage = stagesList.get(position).trim();
                        etStageSearch.setText(searchStage);
                    } else {
                        searchStage = "";
                        etStageSearch.setText(stagesList.get(0));
                    }
                    offset = 0;
                    viewModel.getAllProject(searchStage, searchStatus, projectId, offset, LIMIT);
                    listPopupWindow.dismiss();
                });
                listPopupWindow.show();
            } else {
                errorMessage("No Stage Found", null, false);
            }
        }
    }

}
