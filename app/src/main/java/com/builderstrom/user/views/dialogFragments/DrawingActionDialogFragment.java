package com.builderstrom.user.views.dialogFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.modals.Datum;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.DrawingMenuVM;
import com.builderstrom.user.views.activities.AddDrawingActivity;
import com.builderstrom.user.views.activities.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static com.builderstrom.user.views.fragments.HomeMenuFragment.userPermissions;

public class DrawingActionDialogFragment extends BaseDialogFragment {

    @BindView(R.id.ivEdit) ImageView ivEdit;
    @BindView(R.id.ivFavourite) ImageView ivFavourite;
    @BindView(R.id.ivTrack) ImageView ivTrack;
    @BindView(R.id.ivComment) ImageView ivComment;
    @BindView(R.id.ivShare) ImageView ivShare;
    @BindView(R.id.ivQuarantine) ImageView ivQuarantine;
    @BindView(R.id.ivArchive) ImageView ivArchive;
    @BindView(R.id.ivDelete) ImageView ivDelete;

    //    private UpdateCompanyDocsListing mCallback;
//    private CompanyActionCallBack mActionCallBack;
    private boolean isDocChanged = false;
    private DrawingMenuVM viewModel;
    private Datum mDocument;

    public static DrawingActionDialogFragment newInstance(Datum data) {
        DrawingActionDialogFragment dialogFragment = new DrawingActionDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    protected boolean isFullScreenDialog() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_company_more_action;
    }

    @Override
    protected void bindViews(View view) {
        viewModel = new ViewModelProvider(this).get(DrawingMenuVM.class);
        observeViewModel();

    }

    private void observeViewModel() {

        viewModel.isLoadingLD.observe(getViewLifecycleOwner(), aBoolean -> {
            if (null != aBoolean) {
                if (aBoolean) showProgress();
                else dismissProgress();
            }
        });

        viewModel.isSuccess.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean != null && aBoolean) {
                callBroadcast();
                dismiss();
            }
        });

        viewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (errorModel != null && getActivity() != null) {
//                errorMessage(errorModel.getMessage(), null, false);
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);
            }
        });
    }

    @Override
    protected void init() {
        if (null != getArguments()) {
            mDocument = getArguments().getParcelable("data");
        }
        setView();
    }

    private void setView() {
        if (userPermissions != null) {
            CommonMethods.checkVisiblePermission(userPermissions.getDrawings().getQuarantineDrawing(), ivQuarantine);
            CommonMethods.checkVisiblePermission(userPermissions.getDrawings().getArchiveDrawing(), ivArchive);
            CommonMethods.checkVisiblePermission(userPermissions.getDrawings().getPinnedComment(), ivComment);
            CommonMethods.checkVisiblePermission(userPermissions.getDrawings().getUpdateDraw(), ivEdit);
        }

        if (mDocument != null) {
            ivEdit.setVisibility(isInternetAvailable() ? View.VISIBLE : View.INVISIBLE);
            ivTrack.setSelected(mDocument.getIsTrack() != null && mDocument.getIsTrack().equals("1"));
            ivFavourite.setSelected(mDocument.getIsFav() != null && mDocument.getIsFav().equals("1"));
            ivQuarantine.setSelected(mDocument.getQuarantine() != null && mDocument.getQuarantine().equals("1"));
            ivArchive.setSelected(mDocument.getArchiveStatus() == 1);
            ivComment.setSelected(mDocument.getTotalcomments() > 0);

            ivShare.setVisibility(isInternetAvailable() ? View.VISIBLE : View.INVISIBLE);
            ivComment.setVisibility(isInternetAvailable() ? View.VISIBLE : View.INVISIBLE);

            ivEdit.setVisibility(View.GONE);
            ivDelete.setVisibility(View.GONE);
            ivShare.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.ivClose, R.id.ivEdit, R.id.ivDelete, R.id.ivFavourite, R.id.ivTrack,
            R.id.ivShare, R.id.ivComment, R.id.ivQuarantine, R.id.ivArchive})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                dismiss();
                break;

            case R.id.ivEdit:
                Intent intent = new Intent(getActivity(), AddDrawingActivity.class);
                intent.putExtra("data", mDocument);
                startActivity(intent);
                dismiss();
                break;

            case R.id.ivDelete:
//                if (getActivity() != null) {
//                    new AlertDialog.Builder(getActivity()).setTitle("Delete document")
//                            .setMessage("Are you sure want to delete this document?")
//                            .setPositiveButton(R.string.ok, (dialog, which) -> {
//                                if (null != mActionCallBack) {
//                                    mActionCallBack.onActionClicked(DataNames.ACTION_DOC_DELETE, mDocument);
//                                }
//                                dialog.dismiss();
//                                dismiss();
//                            })
//                            .setNegativeButton(R.string.cancel, null)
//                            .create().show();
//                }
                break;
            case R.id.ivFavourite:
                viewModel.callMoreMenuAPI(DataNames.ACTION_FAVOURITE, mDocument.getId(), (mDocument.getIsFav().equalsIgnoreCase("0") ? "1" : "0"));
                break;

            case R.id.ivTrack:
                viewModel.callMoreMenuAPI(DataNames.ACTION_TRACK, mDocument.getId(), (mDocument.getIsTrack().equalsIgnoreCase("0") ? "1" : "0"));
                break;

            case R.id.ivQuarantine:
                viewModel.callMoreMenuAPI(DataNames.ACTION_QUARANTINE, mDocument.getId(), (mDocument.getQuarantine().equalsIgnoreCase("0") ? "1" : "0"));
                break;

            case R.id.ivArchive:
                viewModel.callMoreMenuAPI(DataNames.ACTION_ARCHIVE, mDocument.getId(), (mDocument.getArchiveStatus() == 0 ? "1" : "0"));
                break;

            case R.id.ivShare:
//                if (getFragmentManager() != null) {
                ShareDialogFragment.newInstance(mDocument.getId(), mDocument.getName(), "", "",
                        DataNames.SHARE_COMPANY_DOCS).show(getParentFragmentManager(), "share Dialog");
//                }
                break;

            case R.id.ivComment:
//                if (getParentFragmentManager() != null) {
                DrawingCommentsDialogFragment commentDialog = DrawingCommentsDialogFragment
                        .newInstance(mDocument.getId());
                commentDialog.setCallback(count -> {
                    isDocChanged = true;
                    ivComment.setSelected(count > 0);
                    mDocument.setTotalcomments(count);
                });
                commentDialog.show(getParentFragmentManager(), "comment Dialog");
                dismiss();
//                }
                break;
        }
    }


//    public void setCallback(UpdateCompanyDocsListing mCallback) {
//        this.mCallback = mCallback;
//    }

//    public void setActionCallback(CompanyActionCallBack mActionCallback) {
//        this.mActionCallBack = mActionCallback;
//    }

    @Override
    public void onDestroyView() {
//        if (isDocChanged && mActionCallBack != null) {
//            mActionCallBack.onActionClicked(DataNames.ACTION_DOC_DEFAULT, mDocument);
//        }
        super.onDestroyView();
    }

    private void callBroadcast() {
        if (getActivity() != null) {
            Intent intent = new Intent(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE);
            intent.putExtra("KEY_FLAG", true);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        }
    }
}
