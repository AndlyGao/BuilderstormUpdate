//package com.builderstrom.user.views.activities;
//
//import android.content.Intent;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.localbroadcastmanager.content.LocalBroadcastManager;
//
//import com.builderstrom.user.R;
//import com.builderstrom.user.presenters.MoreMenuPresenter;
//import com.builderstrom.user.repository.retrofit.api.DataNames;
//import com.builderstrom.user.repository.retrofit.modals.Datum;
//import com.builderstrom.user.repository.retrofit.modals.DrawingCommentModel;
//import com.builderstrom.user.repository.retrofit.modals.MoreMenuModel;
//import com.builderstrom.user.utils.BuilderStormApplication;
//import com.builderstrom.user.utils.CommonMethods;
//import com.builderstrom.user.views.viewInterfaces.MoreMenuContract;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import static com.builderstrom.user.views.fragments.HomeMenuFragment.userPermissions;
//
//public class MoreMenuActivity extends SimpleBaseActivity implements MoreMenuContract.view {
//
//    public MoreMenuPresenter presenter;
//    ImageView ivQuarantine, ivFavourite, ivTrack, ivArchive, ivComment, ivDismiss, ivEditDrawing;
//    int quarantineStatus = 0;
//    int trackStatus = 0;
//    int archiveStatus = 0;
//    int favouriteStatus = 0;
//    int commentsStatus = 0;
//
//    private Datum modelData;
//    private TextView txtTitle;
//
//    @Override
//    protected int getLayoutID() {
//        return R.layout.activity_more_menu;
//    }
//
//    @Override
//    protected void init() {
//        getBundleData();
//        initView();
//        setUIData();
//        clickListener();
//    }
//
//    private void getBundleData() {
//        String projectInfo = getIntent().getStringExtra(DataNames.PROJECT_INFO);
//        modelData = new Gson().fromJson(projectInfo, new TypeToken<Datum>() {
//        }.getType());
//        quarantineStatus = Integer.parseInt(modelData.getQuarantine());
//        trackStatus = Integer.parseInt(modelData.getIsTrack());
//        archiveStatus = Integer.parseInt(modelData.getArchiveStatus());
//        favouriteStatus = Integer.parseInt(modelData.getIsFav());
//        commentsStatus = modelData.getTotalcomments();
//    }
//
//    private void clickListener() {
//        ivEditDrawing.setVisibility(View.GONE);
//        ivEditDrawing.setOnClickListener(v -> {
//            Intent intent = new Intent(MoreMenuActivity.this, AddDrawingActivity.class);
//            intent.putExtra("data", modelData);
//            startActivity(intent);
//            finish();
//        });
//
//        ivDismiss.setOnClickListener(view -> finish());
//
//        ivQuarantine.setOnClickListener(v -> {
//            if (quarantineStatus == 0) {
//                quarantineStatus = 1;
//                ivQuarantine.setImageResource(R.drawable.bugfilledsvg);
//                callPresenter("quarantine", quarantineStatus);
//            } else {
//                quarantineStatus = 0;
//                ivQuarantine.setImageResource(R.drawable.bugdefaultsvg);
//                callPresenter("quarantine", quarantineStatus);
//            }
//        });
//
//        ivFavourite.setOnClickListener(v -> {
//            if (favouriteStatus == 0) {
//                favouriteStatus = 1;
//                ivFavourite.setImageResource(R.drawable.starfilledsvg);
//                callPresenter("favourite", favouriteStatus);
//            } else {
//                favouriteStatus = 0;
//                ivFavourite.setImageResource(R.drawable.stardefaultsvg);
//                callPresenter("favourite", favouriteStatus);
//            }
//        });
//
//        ivTrack.setOnClickListener(v -> {
//            if (trackStatus == 0) {
//                trackStatus = 1;
//                ivTrack.setImageResource(R.drawable.eyefilledsvg);
//                callPresenter("track", trackStatus);
//            } else {
//                trackStatus = 0;
//                ivTrack.setImageResource(R.drawable.eyedefaultsvg);
//                callPresenter("track", trackStatus);
//            }
//        });
//
//        ivArchive.setOnClickListener(v -> {
//            if (archiveStatus == 0) {
//                archiveStatus = 1;
//                ivArchive.setImageResource(R.drawable.ic_archive);
//                callPresenter("archive", archiveStatus);
//            } else {
//                archiveStatus = 0;
//                ivArchive.setImageResource(R.drawable.ic_unarchive);
//                callPresenter("archive", archiveStatus);
//            }
//        });
//
//        ivComment.setOnClickListener(v -> {
//            Intent intent = new Intent(getApplicationContext(), ProjectCommentActivity.class);
//            intent.putExtra(DataNames.PROJECT_INFO, new Gson().toJson(modelData));
//            startActivity(intent);
//            finish();
//        });
//    }
//
//    private void initView() {
//        /* presenter */
//        presenter = new MoreMenuPresenter();
//        presenter.attachView(this);
//
//        /* bind views */
//        txtTitle = findViewById(R.id.txt_title_project_dots_dialog);
//        ivQuarantine = findViewById(R.id.ivQuarantine);
//        ivFavourite = findViewById(R.id.ivFavourite);
//        ivTrack = findViewById(R.id.ivTrack);
//        ivArchive = findViewById(R.id.ivArchive);
//        ivComment = findViewById(R.id.ivComment);
//        ivDismiss = findViewById(R.id.ivClose);
//        ivEditDrawing = findViewById(R.id.ivEditDrawing);
//
//        CommonMethods.checkVisiblePermission(userPermissions.getDrawings().getQuarantineDrawing(), ivQuarantine);
//        CommonMethods.checkVisiblePermission(userPermissions.getDrawings().getArchiveDrawing(), ivArchive);
//        CommonMethods.checkVisiblePermission(userPermissions.getDrawings().getPinnedComment(), ivComment);
//        CommonMethods.checkVisiblePermission(userPermissions.getDrawings().getUpdateDraw(), ivEditDrawing);
//    }
//
//    private void setUIData() {
//        if (modelData.getName() != null) {
//            txtTitle.setText(modelData.getName());
//        }
//
//        if (quarantineStatus == 0) {
//            ivQuarantine.setImageResource(R.drawable.bugdefaultsvg);
//        } else {
//            ivQuarantine.setImageResource(R.drawable.bugfilledsvg);
//        }
//
//        if (favouriteStatus == 0) {
//            ivFavourite.setImageResource(R.drawable.stardefaultsvg);
//        } else {
//            ivFavourite.setImageResource(R.drawable.starfilledsvg);
//        }
//
//        if (trackStatus == 0) {
//            ivTrack.setImageResource(R.drawable.eyedefaultsvg);
//        } else {
//            ivTrack.setImageResource(R.drawable.eyefilledsvg);
//        }
//
//        if (archiveStatus == 0) {
//            ivArchive.setImageResource(R.drawable.ic_unarchive);
//        } else {
//            ivArchive.setImageResource(R.drawable.ic_archive);
//        }
//
//        if (commentsStatus == 0) {
//            ivComment.setImageResource(R.drawable.commentsvg);
//        } else if (commentsStatus == 1) {
//            ivComment.setImageResource(R.drawable.ic_comment_filled);
//        } else {
//            ivComment.setImageResource(R.drawable.ic_multi_comment);
//        }
//    }
//
//    private void callPresenter(String action, int status) {
//        if (CommonMethods.isNetworkAvailable(MoreMenuActivity.this)) {
//            presenter.callMoreMenuAPI(MoreMenuActivity.this, action,
//                    BuilderStormApplication.mPrefs.getSiteId(), BuilderStormApplication.mPrefs.getProjectId(),
//                    BuilderStormApplication.mPrefs.getUserId(), modelData.getId(), String.valueOf(status));
//        } else {
//            CommonMethods.displayToast(MoreMenuActivity.this, "No internet connection");
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (presenter != null)
//            presenter.detachView();
//        super.onDestroy();
//
//    }
//
//    @Override
//    public void successMenu(MoreMenuModel response) {
//        if (response.getResponseCode()) {
//            CommonMethods.displayToast(MoreMenuActivity.this, response.getMessage());
//            callBroadcast();
//        }
//    }
//
//    @Override
//    public void successComment(MoreMenuModel response) {
//
//    }
//
//    @Override
//    public void successFetchComment(DrawingCommentModel response) {
//
//    }
//
//    public void callBroadcast() {
//        Intent intent = new Intent(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE);
//        intent.putExtra("KEY_FLAG", true);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//    }
//
//    @Override
//    public void globalSyncService() {
//
//    }
//}
