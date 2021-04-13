package com.builderstrom.user.views.dialogFragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.RfiComment;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.PermissionUtils;
import com.builderstrom.user.viewmodels.RFIViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.ChildAttachOfflineAdapter;
import com.builderstrom.user.views.adapters.RfiCommentsAdapter;
import com.builderstrom.user.views.viewInterfaces.OnCameraAndStorageGrantedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class AddCommentDialogFragment extends BaseDialogFragment implements OnCameraAndStorageGrantedListener {


    @BindView(R.id.rlBottom) RelativeLayout rlBottom;
    @BindView(R.id.etComment) EditText etComment;
    @BindView(R.id.flAttachment) FrameLayout flAttachments;
    @BindView(R.id.tvNoComment) TextView tvNoData;
    @BindView(R.id.rvComments) RecyclerView rvComments;
    @BindView(R.id.rvAttachment) RecyclerView rvAttachments;


    private List<String> filesList = new ArrayList<>();
    private List<RfiComment> allCommentsList = new ArrayList<>();
    private RfiCommentsAdapter mAdapter;
    private ChildAttachOfflineAdapter mAttachmentAdapter;
    private RFIViewModel viewModel;

    private String rfiId;
    private boolean canAnswer = true, isFirstTimeLoaded = true;

    public static AddCommentDialogFragment newInstance(String rfiId, Boolean canAnswer) {
        AddCommentDialogFragment dialogFragment = new AddCommentDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("rfiId", rfiId);
        bundle.putBoolean("canAnswer", canAnswer);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    protected boolean isFullScreenDialog() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_comment_rfi;
    }

    @Override
    protected void bindViews(View view) {
        viewModel = new ViewModelProvider(this).get(RFIViewModel.class);
        observeViewModel();
        mAdapter = new RfiCommentsAdapter(getContext(), allCommentsList);
        rvComments.setAdapter(mAdapter);
        mAttachmentAdapter = new ChildAttachOfflineAdapter(getContext(), filesList);
        mAttachmentAdapter.setUpdateLastDelete(this :: setFilesView);
        rvAttachments.setAdapter(mAttachmentAdapter);

    }

    private void observeViewModel() {
        viewModel.isLoadingLD.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) showProgress();
            else dismissProgress();
        });

        viewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (errorModel != null && getActivity() != null) {
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);
            }
        });

        viewModel.commentLiveData.observe(getViewLifecycleOwner(), new Observer<List<RfiComment>>() {
            @Override
            public void onChanged(List<RfiComment> rfiComments) {
                allCommentsList.clear();
                if (null != rfiComments) {
                    allCommentsList.addAll(rfiComments);
                    mAdapter.notifyDataSetChanged();
                    tvNoData.setVisibility(allCommentsList.isEmpty() ? View.VISIBLE : View.GONE);
                    if (allCommentsList.size() > 5) {
                        rvComments.smoothScrollToPosition(allCommentsList.size());
                    }
                }
            }
        });

        viewModel.isSuccess.observe(getViewLifecycleOwner(), aBoolean -> {
            if (null != aBoolean) {
                filesList.clear();
                setFilesView();
                etComment.clearComposingText();
                etComment.setText(null);
                refreshAll();
            }
        });
    }

    @OnClick({R.id.ivClose, R.id.ivAddComment, R.id.ivAttachDocument})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                dismissWithHideKeyboard();
                break;
            case R.id.ivAddComment:
                if (etComment.getText().toString().trim().isEmpty()) {
                    errorMessage(getString(R.string.no_empty_comment), null, false);
                } else {
                    addComment(etComment.getText().toString().trim());
                }
                break;
            case R.id.ivAttachDocument:
                attachDocument();
                break;
        }
    }


    private void attachDocument() {
        checkPermission();
    }

    private void checkPermission() {
        permissionUtils = new PermissionUtils(getActivity());
        permissionUtils.setListener(this);
        permissionUtils.checkPermissions();
    }

    private void addComment(String comment) {
        hideDialogKeyboard();
        viewModel.addRfiComment(rfiId, comment, filesList);
    }

    @Override
    protected void init() {
        if (null != getArguments()) {
            rfiId = getArguments().getString("rfiId");
            canAnswer = getArguments().getBoolean("canAnswer");
        }
        setCommentView();
        setFilesView();
    }

    private void setCommentView() {
        rlBottom.setVisibility(canAnswer ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstTimeLoaded) {
            isFirstTimeLoaded = false;
            refreshAll();
        }
    }

    private void refreshAll() {
        viewModel.getAllComments(rfiId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtils.CAMERA_REQUEST:
            case PermissionUtils.STORAGE_REQUEST:
                permissionUtils.verifyResults(requestCode, grantResults);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonMethods.PHOTO_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uriFile = data.getData();
                try {
                    if (uriFile != null) {
                        String fileLocation = CommonMethods.getFilePathFromURI(getActivity(), uriFile);
                        String fileName = CommonMethods.getFileName(getActivity(), uriFile);
                        if (null != fileName.substring(fileName.lastIndexOf("."))) {
                            filesList.add(fileLocation);
                        }
                    }
                } catch (Exception e) {
                    errorMessage("File not supported", null, false);
                } finally {
                    setFilesView();
                }
            }
        }
    }

    private void setFilesView() {
        flAttachments.setVisibility(filesList.isEmpty() ? View.GONE : View.VISIBLE);
        if (!filesList.isEmpty()) {
            mAttachmentAdapter.notifyDataSetChanged();
            rvAttachments.smoothScrollToPosition(filesList.size() - 1);
        }
    }

    @Override
    public void onPermissionsGranted() {
        openExplorer("*/*", CommonMethods.PHOTO_REQUEST_CODE);
    }

    @Override
    public void onPermissionRefused(String whichOne) {
        errorMessage(whichOne, null, false);
    }
}
