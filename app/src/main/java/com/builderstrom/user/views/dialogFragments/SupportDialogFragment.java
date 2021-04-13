package com.builderstrom.user.views.dialogFragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.PermissionUtils;
import com.builderstrom.user.viewmodels.SupportViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.ChildAttachOfflineAdapter;
import com.builderstrom.user.views.viewInterfaces.OnCameraAndStorageGrantedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SupportDialogFragment extends BaseDialogFragment implements OnCameraAndStorageGrantedListener {
    @BindView(R.id.etText) EditText etText;
    @BindView(R.id.tvLogFile) TextView tvLogFile;
    @BindView(R.id.rvAttachment) RecyclerView rvAttachments;
    @BindView(R.id.flAttachment) FrameLayout flAttachments;

    private List<String> filesList = new ArrayList<>();
    private ChildAttachOfflineAdapter mAttachmentAdapter;
    private SupportViewModel viewModel;
    private String filepath = "";


    public static SupportDialogFragment newInstance() {
        return new SupportDialogFragment();
    }

    @Override
    protected boolean isFullScreenDialog() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_support;
    }

    @Override
    protected void bindViews(View view) {
        viewModel = new ViewModelProvider(this).get(SupportViewModel.class);
        observeViewModel();
        mAttachmentAdapter = new ChildAttachOfflineAdapter(getContext(), filesList);
        rvAttachments.setAdapter(mAttachmentAdapter);
    }

    private void observeViewModel() {
        viewModel.isLoadingLD.observe(getViewLifecycleOwner(), loader -> {
            if (null != loader) {
                if (loader) showProgress();
                else dismissProgress();
            }
        });

        viewModel.submitReportLD.observe(getViewLifecycleOwner(), message -> {
            if (null != message) {
                dismissWithHideKeyboard();
                filesList.clear();
            }
        });

        viewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (null != getActivity() && null != errorModel) {
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);
            }
        });


    }


    @OnClick({R.id.ivClose, R.id.btnSendReport, R.id.btnUploadFiles})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                dismissWithHideKeyboard();
                break;
            case R.id.btnSendReport:
                if (etText.getText().toString().trim().isEmpty()) {
                    etText.setError("Please enter your description");
                } else {
                    filesList.add(filepath);
                    viewModel.sendReport(etText.getText().toString(), filesList);
                }
                break;
            case R.id.btnUploadFiles:
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

    @Override
    protected void init() {
        setLogFileView();
    }


    private void setLogFileView() {
        try {
            Uri outputFileUri = CommonMethods.getUriFromName(getActivity(), "microlog.txt");
            if (outputFileUri != null) {
                filepath = CommonMethods.getFilePathFromURI(getActivity(), outputFileUri);
                tvLogFile.setVisibility(filepath == null || filepath.isEmpty() ? View.GONE : View.VISIBLE);
                tvLogFile.setOnClickListener(v -> {
                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(outputFileUri, "application/*");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                showInstallNewAppDialog();
                            }
                        }
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showInstallNewAppDialog() {
        if (null != getActivity()) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Micrologs")
                    .setMessage("To preview this file a 3rd party application needs to be installed. Are you happy for this to be installed?")
                    .setNegativeButton("Cancel", null)
                    .create().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
                        String fileLocation = CommonMethods.getFilePathFromURI(getActivity(),
                                uriFile);
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

    @Override
    public void onPermissionsGranted() {
        openExplorer("*/*", CommonMethods.PHOTO_REQUEST_CODE);
    }

    @Override
    public void onPermissionRefused(String whichOne) {
        errorMessage(whichOne, null, false);
    }

    private void setFilesView() {
        flAttachments.setVisibility(filesList.isEmpty() ? View.GONE : View.VISIBLE);
        if (!filesList.isEmpty()) {
            mAttachmentAdapter.notifyDataSetChanged();
            rvAttachments.smoothScrollToPosition(filesList.size() - 1);
        }
    }


}
