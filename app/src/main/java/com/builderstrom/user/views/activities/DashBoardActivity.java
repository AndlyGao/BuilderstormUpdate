package com.builderstrom.user.views.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.modals.ProjectDetails;
import com.builderstrom.user.utils.ClientLogger;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.GlideApp;
import com.builderstrom.user.viewmodels.DashBoardViewModel;
import com.builderstrom.user.views.dialogFragments.SupportDialogFragment;
import com.builderstrom.user.views.fragments.CompanyDocListFragment;
import com.builderstrom.user.views.fragments.DigitalDocumentListFragment;
import com.builderstrom.user.views.fragments.HomeMenuFragment;
import com.builderstrom.user.views.fragments.NotificationFragment;
import com.builderstrom.user.views.fragments.ProjectDocumentFragment;
import com.builderstrom.user.views.fragments.RfiListFragment;
import com.builderstrom.user.views.fragments.SnagListFragment;
import com.builderstrom.user.views.fragments.ToDoListFragment;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DashBoardActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    public TextView tvNotification, tvNetworkType, tvStatus;
    public BroadcastReceiver networkStatusBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            runOnUiThread(() -> {
                try {
                    if (intent.hasExtra("KEY_FLAG")) {
                        networkStatus();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    };
    @BindView(R.id.ivHome) ImageView ivHome;
    @BindView(R.id.ivNotification) ImageView ivNotification;
    @BindView(R.id.etProjects) EditText etProjects;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    private String notification_projectId = null;
    private List<ProjectDetails> projectList = new ArrayList<>();
    private DashBoardViewModel mDashBoardViewModel;
    private boolean isFirstTimeLoading = true;

    private BroadcastReceiver updateProjectsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            runOnUiThread(() -> {
                try {
                    if (intent.hasExtra("KEY_FLAG")) {
                        mDashBoardViewModel.accessAllProjects(true, notification_projectId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    };

    private BroadcastReceiver notificationBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            runOnUiThread(() -> {
                try {
                    if (intent.hasExtra("KEY_FLAG")) {
                        if (!mDashBoardViewModel.mPrefs.getAppVersion().isEmpty()) {
                            if (null != ivNotification) {
                                ivNotification.setVisibility(View.GONE);
                                tvNotification.setText("");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    };

    @Override
    protected int getLayoutID() {
        return R.layout.activty_dashboard;
    }

    @Override
    protected void init() {
        mDashBoardViewModel = new ViewModelProvider(this).get(DashBoardViewModel.class);
        observeViewModel();
        isFirstTimeLoading = true;
//        notification_projectId = getIntent().getStringExtra("project_id");
        if (mDashBoardViewModel.mPrefs.getSiteId() != null && !mDashBoardViewModel.mPrefs.getSiteId().isEmpty()) {
            registerNetworkBroadcast();
            initView();
        } else {
            Intent mIntent = new Intent(DashBoardActivity.this, WorkSpaceActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }

        if (getIntent().hasExtra("nav_type")) {
            Log.e("nav_type", getIntent().getStringExtra("nav_type"));
            onNewIntent(getIntent());
        }

    }

    private void replaceNavigation(String navType) {
        Fragment fragment = null;
        switch (navType) {
            case "documents":
                fragment = new ProjectDocumentFragment();
                break;
            case "digital_documents":
                fragment = new DigitalDocumentListFragment();
                break;
            case "cloud_storage":
                fragment = new CompanyDocListFragment();
                break;
            case "rfi":
                fragment = new RfiListFragment();
                break;
            case "defects":
                fragment = new SnagListFragment();
                break;
            case "todos":
                fragment = new ToDoListFragment();
                break;
        }

        if (null != fragment)
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment)
                    /*.addToBackStack(null)*/.commit();
    }

    private void observeViewModel() {
        mDashBoardViewModel.projectsLD.observe(this, projects -> {
            if (projects != null) {
                projectList.clear();
                projectList.addAll(projects);
                if (DashBoardActivity.this.getSupportFragmentManager().findFragmentById(R.id.frame) == null) {
                    DashBoardActivity.this.addFragmentOnTop(new HomeMenuFragment());
                }
            }
        });

        mDashBoardViewModel.dropDownText.observe(this, dropText -> {
            if (dropText != null) {
                if (null != etProjects) {
                    etProjects.setText(dropText);
                }
                Log.e("project Id", mDashBoardViewModel.mPrefs.getProjectId());
            }
        });

        mDashBoardViewModel.isLogOut.observe(this, aBoolean -> {
            if (null != aBoolean) {
                logout();
            }
        });

        mDashBoardViewModel.isLoadingLD.observe(this, aBoolean -> {
            if (aBoolean != null) {
                showProgress();
            } else {
                dismissProgress();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDashBoardViewModel.accessAllProjects(!isFirstTimeLoading, notification_projectId);
        isFirstTimeLoading = false;
    }

    private void initSpinnerProjects() {
        if (!projectList.isEmpty()) {
            ListPopupWindow listPopupWindow = new ListPopupWindow(this);
            listPopupWindow.setAdapter(new ArrayAdapter<>(this, R.layout.row_dropdown, R.id.tvDropDown, projectList));
            listPopupWindow.setAnchorView(etProjects);
            listPopupWindow.setModal(true);
            listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
            listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
            listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                ProjectDetails selectedProject = projectList.get(position);
                etProjects.setText(selectedProject.getTitle());
                mDashBoardViewModel.mPrefs.saveSelectedProject(selectedProject);
                mDashBoardViewModel.mPrefs.setProjectId(selectedProject.getUid());
                mDashBoardViewModel.mPrefs.setProjectName(selectedProject.getTitle());
                callBroadcast();
                listPopupWindow.dismiss();
            });
            listPopupWindow.show();
        }
    }

    @OnClick({R.id.ivHome, R.id.ivMenu, R.id.etProjects})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivHome:
                addFragmentOnTop(new HomeMenuFragment());
                break;
            case R.id.ivMenu:
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
                break;
            case R.id.etProjects:
                initSpinnerProjects();
                break;
        }
    }

    private void initView() {
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView tvHeader = headerView.findViewById(R.id.tvHeaderUser);
        if (tvHeader != null) {
            tvHeader.setText(mDashBoardViewModel.mPrefs.getUserName());
        }
        GlideApp.with(getApplicationContext()).load(mDashBoardViewModel.mPrefs.getProfileImage())
                .apply(new RequestOptions().placeholder(R.drawable.none).error(R.drawable.none)
                        .circleCrop()).into((ImageView) headerView.findViewById(R.id.ivProfile));
        navigationView.setNavigationItemSelectedListener(this);
        tvNotification = (TextView) navigationView.getMenu().findItem(R.id.nav_notification).getActionView();
        if (isInternetAvailable()) {
            GlideApp.with(getApplicationContext()).load(mDashBoardViewModel.mPrefs.getWorkSpaceLogo())
                    .apply(new RequestOptions().error(R.drawable.ic_app_logo)).into(ivHome);
        } else {
            ivHome.setImageResource(R.drawable.ic_app_logo);
        }
        tvNetworkType = headerView.findViewById(R.id.tvNetworkType);
        tvStatus = headerView.findViewById(R.id.tvStatus);
        networkStatus();
        initializeCountDrawer();
        /* update all projects */
        mDashBoardViewModel.syncAllProjects();


    }

    private void networkStatus() {
        if (!isDestroyed()) {
            if (null != tvNetworkType && null != tvStatus) {
                tvNetworkType.setText(String.format("Network Type : %s", CommonMethods.networkType(DashBoardActivity.this)));
                tvStatus.setText(String.format("Network Status : %s", CommonMethods.isNetworkConnected(DashBoardActivity.this) ? "Connected" : "Disconnected"));
            }

            ClientLogger.d(DashBoardActivity.class.getName(), String.format("Network Type : %s", CommonMethods.networkType(DashBoardActivity.this)));
            ClientLogger.d(DashBoardActivity.class.getName(), String.format("Network Status : %s", CommonMethods.isNetworkConnected(DashBoardActivity.this) ? "Connected" : "Disconnected"));
        }
    }

    private void initializeCountDrawer() {
        if (tvNotification != null) {
            tvNotification.setGravity(Gravity.CENTER_VERTICAL);
            tvNotification.setTypeface(null, Typeface.BOLD);
            tvNotification.setTextColor(ContextCompat.getColor(getApplicationContext(),
                    android.R.color.holo_red_dark));
        }
    }

    public void addFragmentOnTop(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
    }

    @Override
    protected void onDestroy() {
        unregisterBroadcasts();
        getIntent().putExtra("nav_type", "1212");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawers();
        } else {
//            Log.e("fragment", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStackImmediate();
            } else {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame);
                if (fragment != null) {
                    if (fragment instanceof HomeMenuFragment) {
                        super.onBackPressed();
//                        finish();
                    } else {
                        addFragmentOnTop(new HomeMenuFragment());
                    }
                } else
                    super.onBackPressed();
            }
        }
    }

    public void lockNavDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void unlockNavDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                addFragmentOnTop(new HomeMenuFragment());
//                drawer.closeDrawers();
                break;
            case R.id.nav_password:
                startActivity(new Intent(DashBoardActivity.this, ChangePasswordActivity.class));
//                drawer.closeDrawers();
                break;
            case R.id.nav_pin:
                startActivity(new Intent(DashBoardActivity.this, ChangePinActivity.class));
//                drawer.closeDrawers();
                break;
            case R.id.nav_notification:
                addFragmentOnTop(new NotificationFragment());
//                drawer.closeDrawers();
//                errorMessage(String.format(getString(R.string.no_version_updated),
//                        CommonMethods.getAppVersion(DashBoardActivity.this)),
//                        null, false);
//                if (!mDashBoardViewModel.mPrefs.getAppVersion().isEmpty()) {
//                    if (!mDashBoardViewModel.mPrefs.getAppVersion().equalsIgnoreCase(
//                            CommonMethods.getAppVersion(getApplicationContext()))) {
//                        new NotificationDialogFragment().show(getSupportFragmentManager(),
//                                "Update application");
//                    } else {
//                        errorMessage(String.format(getString(R.string.no_version_updated),
//                                CommonMethods.getAppVersion(DashBoardActivity.this)),
//                                null, false);
//                    }
//                }
                break;
//            case R.id.nav_digital_docs:
//                addFragmentOnTop(new ShortDocsFragment());
//                drawer.closeDrawers();
//                errorMessage(String.format(getString(R.string.no_version_updated),
//                        CommonMethods.getAppVersion(DashBoardActivity.this)),
//                        null, false);
//                if (!mDashBoardViewModel.mPrefs.getAppVersion().isEmpty()) {
//                    if (!mDashBoardViewModel.mPrefs.getAppVersion().equalsIgnoreCase(
//                            CommonMethods.getAppVersion(getApplicationContext()))) {
//                        new NotificationDialogFragment().show(getSupportFragmentManager(),
//                                "Update application");
//                    } else {
//                        errorMessage(String.format(getString(R.string.no_version_updated),
//                                CommonMethods.getAppVersion(DashBoardActivity.this)),
//                                null, false);
//                    }
//                }
//                break;
            case R.id.nav_setting:
                errorMessage("Work in Progress", null, false);
//                drawer.closeDrawers();
                break;
            case R.id.nav_support:
                SupportDialogFragment.newInstance().show(getSupportFragmentManager(), "support section");
//                drawer.closeDrawers();
                break;
            case R.id.nav_logout:
//                logout();
                mDashBoardViewModel.logOutUser();
                break;

        }
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawers();

        }
        return true;
    }

    private void logout() {
        mDashBoardViewModel.mPrefs.logout();
        drawer.closeDrawers();
        startActivity(new Intent(DashBoardActivity.this, PinLoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    public void callBroadcast() {
        Intent intent = new Intent(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE);
        intent.putExtra("KEY_FLAG", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void unregisterBroadcasts() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(notificationBroadcast);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(networkStatusBroadcast);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(updateProjectsReceiver);
    }

    private void registerNetworkBroadcast() {
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(notificationBroadcast, new IntentFilter(DataNames.INTENT_ACTION_UPDATE_NOTIFICATION));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(networkStatusBroadcast, new IntentFilter(DataNames.INTENT_ACTION_UPDATE_NETWORK));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(updateProjectsReceiver, new IntentFilter(DataNames.INTENT_UPDATE_PROJECTS_LIST));
    }

    @Override
    protected void onPause() {
        super.onPause();
        getIntent().putExtra("nav_type", "default");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra("nav_type")) {
            String menuFragment = intent.getStringExtra("nav_type");
            notification_projectId = intent.getStringExtra("project_id");
            new Handler().postDelayed(() -> replaceNavigation(menuFragment), 100);
        }
    }

}




