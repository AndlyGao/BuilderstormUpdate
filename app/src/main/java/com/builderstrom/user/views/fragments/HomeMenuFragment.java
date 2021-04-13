package com.builderstrom.user.views.fragments;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.Uri;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.modals.DashBoardMenuModel;
import com.builderstrom.user.data.retrofit.modals.LoginModel;
import com.builderstrom.user.data.retrofit.modals.OverTimeModel;
import com.builderstrom.user.data.retrofit.modals.ProjectDetails;
import com.builderstrom.user.data.retrofit.modals.RecentCategory;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.ProjectSignViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.DashBoardAdapter;
import com.builderstrom.user.views.dialogFragments.ConfirmSignDialogFragment;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;

public class HomeMenuFragment extends GPSLocationFragment {

    public static LoginModel.Permissions userPermissions;
    @BindView(R.id.rvMenuGrid) RecyclerView rvMenuGrid;
    @BindView(R.id.swipe_container) SwipeRefreshLayout mSwipeRefreshLayout;
    private DashBoardAdapter mAdapter;
    private ProjectSignViewModel signViewModel;
    /* Broadcasts section */
    private BroadcastReceiver signInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pullToRefresh();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void bindView(View view) {
        /* register viewModel */
        signViewModel = new ViewModelProvider(this).get(ProjectSignViewModel.class);
        observeViewModel();
        registerNetworkBroadcast();
        versionUpdate();
        /* view bindings */
        setSwipeRefreshView(mSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this::pullToRefresh);
    }

    @Override
    protected void init() {
        signViewModel.callShortcutCategories();
//        signViewModel.accessAllSections();
        pullToRefresh();
    }

    @Override
    public void onDestroyView() {
        unregisterNetworkChanges();
        getViewModelStore().clear();
        super.onDestroyView();
    }

    private void observeViewModel() {

        signViewModel.isLoadingLD.observe(getViewLifecycleOwner(), loader -> {
            if (loader != null) {
                if (loader) showProgress();
                else dismissProgress();
            }
        });

        signViewModel.isRefreshingLD.observe(getViewLifecycleOwner(), refreshing -> {
            if (refreshing != null) {
                if (refreshing) showRefreshing();
                else hideRefreshing();
            }
        });

        signViewModel.permissionListLD.observe(getViewLifecycleOwner(), permissions -> {
            if (permissions != null) {
                userPermissions = permissions;
            }
        });

        signViewModel.recentCategoryList.observe(getViewLifecycleOwner(), new Observer<List<RecentCategory>>() {
            @Override
            public void onChanged(List<RecentCategory> recentCategories) {
                signViewModel.generateCategoryList(recentCategories);
            }
        });

        signViewModel.homeProjectsListLD.observe(getViewLifecycleOwner(), (List<DashBoardMenuModel> dashBoardMenuModels) -> {
            if (dashBoardMenuModels != null) {
                setMenuAdapter(dashBoardMenuModels);
            }
        });

        signViewModel.notifyGridAdapterLD.observe(getViewLifecycleOwner(), updateAdapter -> {
            if (updateAdapter != null && updateAdapter) {
                if (null != mAdapter)
                    mAdapter.notifyDataSetChanged();
            }
        });

        signViewModel.onlineConfirmDialogLD.observe(getViewLifecycleOwner(), model -> {
            if (model != null) {
                if (model.getDetails() != null && model.getDetails().getProjectDetails() != null
                        && model.getDetails().getProjectDetails().getUid() != null) {
                    OverTimeModel timeModel = new OverTimeModel();
                    timeModel.setAllowOvertime(model.getDetails().getAllowOvertime() == null ? 0 : model.getDetails().getAllowOvertime());
                    timeModel.setAllowStandardBreaks(model.getDetails().getAllowStandardBreaks() == null ? 0 : model.getDetails().getAllowStandardBreaks());
                    timeModel.setStandardBreaks(model.getDetails().getStandardBreaks());
                    ConfirmSignDialogFragment dialogFragment = ConfirmSignDialogFragment.newInstance(
                            String.format("This will Sign '%s' %s '%s' at %s on %s", signViewModel.mPrefs.getUserName(),
                                    signViewModel.mPrefs.isProjectSignIn() ? "Out" : "IN",
                                    model.getDetails().getProjectTitle(), model.getDetails().getTime(),
                                    CommonMethods.convertDate(CommonMethods.DF_2, model.getDetails().getDate(),
                                            CommonMethods.DF_8)), timeModel);
                    dialogFragment.setCallback((overTimeList, idList, breaksList) ->
                            signViewModel.projectSignConfirm(model.getDetails().getProjectDetails()
                                    .getUid(), overTimeList, idList, breaksList));
                    dialogFragment.show(getParentFragmentManager(), "Sign In");
                } else {
                    errorMessage("No project found in current region", null, false);
                }
            }
        });

        signViewModel.offlineSignInDialogLD.observe(getViewLifecycleOwner(), model -> {
            if (model != null) {
                ConfirmSignDialogFragment dialogFragment = ConfirmSignDialogFragment.newInstance(
                        String.format("This will Sign '%s' IN '%s' at %s on %s", signViewModel.mPrefs.getUserName(),
                                model.getTitle(), CommonMethods.getCurrentDate(CommonMethods.DF_9),
                                CommonMethods.getCurrentDate(CommonMethods.DF_8)));
                dialogFragment.setCallback((overTimeList, idList, breaksList) -> signViewModel.offlineSignIn(model));
                dialogFragment.show(HomeMenuFragment.this.getParentFragmentManager(), "confirm in offline");
            }
        });

        signViewModel.offlineSignOutDialogLD.observe(getViewLifecycleOwner(), signedProject -> {
            if (signedProject != null) {
                ProjectDetails projectDetails = new Gson().fromJson(signedProject.getProjectData(), ProjectDetails.class);
                if (null != projectDetails) {

                    OverTimeModel timeModel = new OverTimeModel();
                    timeModel.setAllowOvertime(projectDetails.getAllow_overtime() == null ? 0 : projectDetails.getAllow_overtime());
                    timeModel.setAllowStandardBreaks(projectDetails.getAllow_standard_breaks() == null ? 0 : projectDetails.getAllow_standard_breaks());
                    timeModel.setStandardBreaks(projectDetails.getStandard_breaks());
                    ConfirmSignDialogFragment dialogFragment = ConfirmSignDialogFragment.newInstance(
                            String.format("This will Sign ' %s ' Out ' %s ' at %s on %s", signViewModel.mPrefs.getUserName(),
                                    projectDetails.getTitle(), CommonMethods.getCurrentDate(CommonMethods.DF_9),
                                    CommonMethods.getCurrentDate(CommonMethods.DF_8)), timeModel);
                    dialogFragment.setCallback((overTimeList, idList, breaksList) ->
                            signViewModel.offlineSignOut(idList, breaksList, overTimeList));
                    dialogFragment.show(HomeMenuFragment.this.getParentFragmentManager(), "confirm out offline");
                }
            }
        });

        signViewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (getActivity() != null && errorModel != null) {
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);
            }
        });

        signViewModel.updateAppLD.observe(getViewLifecycleOwner(), update -> {
            if (null != getActivity() && null != update && update && signViewModel.mPrefs.isDialogVisible()) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("BuilderStorm - Construction Project Management")
                        .setMessage("A new update available on play store. Do you want to update?")
                        .setPositiveButton("Update", (dialog, which) -> {
                            signViewModel.mPrefs.setIsDialogVisible(false);
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "pakagename")));
                            } catch (ActivityNotFoundException e) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName() + "&hl=en")));
                            }
                        })
                        .setNegativeButton("Later", (dialog, which) -> {
                            // do nothing
                            signViewModel.mPrefs.setIsDialogVisible(false);
                            dialog.dismiss();
                        })
                        .setCancelable(false)
                        .setIcon(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_launcher))
                        .show();
            }
        });
    }

    private void setMenuAdapter(List<DashBoardMenuModel> dashBoardMenuModels) {
        mAdapter = new DashBoardAdapter(getActivity(), dashBoardMenuModels, (position, catSection, categoryId) -> {
            switch ((position)) {
                case 0:
                    if (userPermissions != null && userPermissions.getSigninginout().getViewSigninginout() != null) {
                        if (userPermissions.getSigninginout().getViewSigninginout().equalsIgnoreCase("on")) {
                            if (HomeMenuFragment.this.isAirplaneModeOff()) {
                                HomeMenuFragment.this.getLocation();
                            } else {
                                HomeMenuFragment.this.showAirplaneDialog();
                            }
                        }
                    }
                    break;
                case 1:
                    HomeMenuFragment.this.replaceFragment(HomeMenuFragment.this.getString(R.string.drawing), new DrawingFragment());
                    break;
                case 2:
                    HomeMenuFragment.this.replaceFragment(HomeMenuFragment.this.getString(R.string.gallery), new ProjectPhotosListFragment());
                    break;
                case 3:
                    HomeMenuFragment.this.replaceFragment(HomeMenuFragment.this.getString(R.string.daily_diary), new DiaryFragment());
                    break;
                case 4:
                    HomeMenuFragment.this.replaceFragment(HomeMenuFragment.this.getString(R.string.rfis), new RfiListFragment());
                    break;
                case 5:
                    HomeMenuFragment.this.replaceFragment(HomeMenuFragment.this.getString(R.string.snag_list), new SnagListFragment());
                    break;
                case 6:
                    HomeMenuFragment.this.replaceFragment(HomeMenuFragment.this.getString(R.string.digital_document), new DigitalDocumentListFragment());
                    break;
                case 7:
                    HomeMenuFragment.this.replaceFragment(HomeMenuFragment.this.getString(R.string.timesheets), new TimesheetsFragment());
                    break;
                case 8:
                    HomeMenuFragment.this.replaceFragment(HomeMenuFragment.this.getString(R.string.company_document), new CompanyDocListFragment());
                    break;
                case 9:
                    HomeMenuFragment.this.replaceFragment(HomeMenuFragment.this.getString(R.string.project_document), new ProjectDocumentFragment());
                    break;
                case 10:
                    HomeMenuFragment.this.replaceFragment(HomeMenuFragment.this.getString(R.string.todo), new ToDoListFragment());
                    break;
                case 11:
                    HomeMenuFragment.this.replaceFragment(HomeMenuFragment.this.getString(R.string.project_management), new ProjManageFragment());
                    break;
                default:
                    ShortDocsFragment shortFragment = ShortDocsFragment.newInstance(categoryId, catSection);
                    HomeMenuFragment.this.getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame, shortFragment, HomeMenuFragment.this.getString(R.string.cat_document))
                            .commit();
                    break;
            }
        });
        rvMenuGrid.setAdapter(mAdapter);
    }

    private void pullToRefresh() {
        signViewModel.projectStatusRefresh(CommonMethods.getCurrentDate(CommonMethods.DF_2));
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

    private void replaceFragment(String fragmentTag, Fragment fragment) {
        if (null != getActivity()) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            boolean fragmentPopped = fragmentManager.popBackStackImmediate(fragmentTag, 0);
//            if (!fragmentPopped && fragmentManager.findFragmentByTag(fragmentTag) == null) {
            FragmentTransaction ftx = fragmentManager.beginTransaction();
//                ftx.addToBackStack(fragmentTag);
            ftx.replace(R.id.frame, fragment, fragmentTag);
            ftx.commit();
//            }
        }
    }

    @Override
    public void onLocationReceived(Location location) {
        signViewModel.projectSign(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
    }

    @Override
    public boolean isAirplaneModeOff() {
        return super.isAirplaneModeOff();
    }

    private void registerNetworkBroadcast() {
        if (null != getActivity()) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(signInfoReceiver, new IntentFilter(DataNames.INTENT_ACTION_SAVE_SIGN_IN_OUT));
        }
    }

    private void unregisterNetworkChanges() {
        if (null != getActivity()) {
            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(signInfoReceiver);
        }
    }

    private void versionUpdate() {
        signViewModel.checkForVersionUpdate();
    }

}
