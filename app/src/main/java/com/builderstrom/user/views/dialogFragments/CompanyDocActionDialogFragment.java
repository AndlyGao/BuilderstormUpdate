package com.builderstrom.user.views.dialogFragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.modals.CompanyDocument;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.CompanyViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.fragments.AddCompanyDocumentFragment;
import com.builderstrom.user.views.viewInterfaces.CompanyActionCallBack;
import com.builderstrom.user.views.viewInterfaces.UpdateCompanyDocsListing;

import butterknife.BindView;
import butterknife.OnClick;

import static com.builderstrom.user.views.fragments.HomeMenuFragment.userPermissions;

public class CompanyDocActionDialogFragment extends BaseDialogFragment {


    @BindView(R.id.ivEdit)
    ImageView ivEdit;
    @BindView(R.id.ivFavourite)
    ImageView ivFavourite;
    @BindView(R.id.ivTrack)
    ImageView ivTrack;
    @BindView(R.id.ivComment)
    ImageView ivComment;
    @BindView(R.id.ivShare)
    ImageView ivShare;
    @BindView(R.id.ivDelete)
    ImageView ivDelete;

    private UpdateCompanyDocsListing mCallback;
    private CompanyActionCallBack mActionCallBack;
    private boolean isDocChanged = false;
    private CompanyViewModel viewModel;
    private CompanyDocument mDocument;
    private int position = -1;

    public static CompanyDocActionDialogFragment newInstance(CompanyDocument data, int adapterposition) {
        CompanyDocActionDialogFragment dialogFragment = new CompanyDocActionDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", adapterposition);
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
        viewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
        observeViewModel();
    }

    private void observeViewModel() {

        viewModel.isLoadingLD.observe(getViewLifecycleOwner(), aBoolean -> {
            if (null != aBoolean) {
                if (aBoolean) showProgress();
                else dismissProgress();
            }
        });


        viewModel.trackStatusLD.observe(getViewLifecycleOwner(), trackStatus -> {
            if (trackStatus != null) {
                if (trackStatus) {
                    isDocChanged = true;
                    ivTrack.setSelected(!ivTrack.isSelected());
                    mDocument.setIs_track(ivTrack.isSelected() ? "1" : "0");
                }
            }

        });
        viewModel.favStatusLD.observe(getViewLifecycleOwner(), favStatus -> {
            if (favStatus != null) {
                if (favStatus) {
                    isDocChanged = true;
                    ivFavourite.setSelected(!ivFavourite.isSelected());
                    mDocument.setIs_fav(ivFavourite.isSelected() ? "1" : "0");
                }
            }
        });
        viewModel.successDeleteCompanyDocLD.observe(getViewLifecycleOwner(), successDelete -> {
            if (successDelete != null && successDelete) {
                if (null != mActionCallBack) {
                    mActionCallBack.onActionClicked(DataNames.ACTION_DOC_DELETE, mDocument, position);
                }
                dismiss();
            }
        });


        viewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (errorModel != null && getActivity() != null) {
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);
            }
        });
    }

    @Override
    protected void init() {
        if (null != getArguments()) {
            position = getArguments().getInt("position");
            mDocument = getArguments().getParcelable("data");
        }
        setView();

    }

    private void setView() {
        if (mDocument != null) {
            ivFavourite.setSelected(mDocument.getIs_fav() != null && mDocument.getIs_fav().equals("1"));
            ivTrack.setSelected(mDocument.getIs_track() != null && mDocument.getIs_track().equals("1"));
            ivComment.setSelected(mDocument.getTotalcomments() != null && mDocument.getTotalcomments() > 0);
            ivEdit.setVisibility(isInternetAvailable() ? View.VISIBLE : View.INVISIBLE);
            ivShare.setVisibility(isInternetAvailable() ? View.VISIBLE : View.INVISIBLE);
            ivComment.setVisibility(isInternetAvailable() ? View.VISIBLE : View.INVISIBLE);
        }
        if (userPermissions != null) {
            CommonMethods.checkVisiblePermission(userPermissions.getCloudStorage().getEditFile(), ivEdit);
            CommonMethods.checkVisiblePermission(userPermissions.getCloudStorage().getDeleteFile(), ivDelete);
            CommonMethods.checkVisiblePermission(userPermissions.getCloudStorage().getPinnedComment(), ivComment);
        }
    }

    @OnClick({R.id.ivClose, R.id.ivEdit, R.id.ivDelete, R.id.ivFavourite, R.id.ivTrack,
            R.id.ivShare, R.id.ivComment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                dismiss();
                break;
            case R.id.ivEdit:
                AddCompanyDocumentFragment fragment = AddCompanyDocumentFragment.newInstance(mDocument);
                fragment.setCallBack(mCallback);
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.flTestContainer, fragment, "edit Company")
                            .addToBackStack("edit Company").commit();
                    dismiss();
                }
                break;
            case R.id.ivDelete:
                if (getActivity() != null) {
                    new AlertDialog.Builder(getActivity()).setTitle("Delete document")
                            .setMessage("Are you sure want to delete this document?")
                            .setPositiveButton(R.string.ok, (dialog, which) -> {
                                viewModel.deleteCompanyDocument(mDocument.getId());
                                dialog.dismiss();
                            })
                            .setNegativeButton(R.string.cancel, null)
                            .create().show();
                }
                break;
            case R.id.ivFavourite:
                viewModel.favoriteDocument(mDocument.getId(), mDocument.getTitle());
                break;
            case R.id.ivTrack:
                viewModel.trackDocument(mDocument.getId(), mDocument.getTitle());
                break;
            case R.id.ivShare:
                if (getFragmentManager() != null) {
                    ShareDialogFragment.newInstance(mDocument.getId(), mDocument.getTitle(),
                            mDocument.getToUsers() == null || mDocument.getToUsers().isEmpty() ? ""
                                    : CommonMethods.getCommaSeparatedString(mDocument.getToUsers()),
                            "", DataNames.SHARE_COMPANY_DOCS)
                            .show(getFragmentManager(), "share Dialog");
                    dismiss();
                }
                break;
            case R.id.ivComment:
                if (getFragmentManager() != null) {
                    PDocsCommentsDialogFragment commentDialog = PDocsCommentsDialogFragment
                            .newInstance(DataNames.SHARE_COMPANY_DOCS, mDocument.getId(),
                                    mDocument.getTitle());
                    commentDialog.setCallback(count -> {
                        isDocChanged = true;
                        ivComment.setSelected(count > 0);
                        mDocument.setTotalcomments(count);
                    });
                    commentDialog.show(getFragmentManager(), "comment Dialog");
                }
                break;
        }
    }


    public void setCallback(UpdateCompanyDocsListing mCallback) {
        this.mCallback = mCallback;
    }

    public void setActionCallback(CompanyActionCallBack mActionCallback) {
        this.mActionCallBack = mActionCallback;
    }

    @Override
    public void onDestroyView() {
        if (isDocChanged && mActionCallBack != null) {
            mActionCallBack.onActionClicked(DataNames.ACTION_DOC_DEFAULT, mDocument, position);
        }
        super.onDestroyView();
    }
}
