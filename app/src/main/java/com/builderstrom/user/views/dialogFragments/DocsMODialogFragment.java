package com.builderstrom.user.views.dialogFragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.modals.ForwordedDetail;
import com.builderstrom.user.repository.retrofit.modals.PDocsDataModel;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.ProjectDocumentVM;
import com.builderstrom.user.views.viewInterfaces.EditSuccessCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DocsMODialogFragment extends BaseDialogFragment {
    @BindView(R.id.ivFavourite) ImageView ivFavourite;
    @BindView(R.id.ivTrack) ImageView ivTrack;
    @BindView(R.id.ivComment) ImageView ivComment;
    @BindView(R.id.tvTitle) TextView tvTitle;
    private ProjectDocumentVM viewModel;
    private EditSuccessCallback mEditSuccessCallback;
    private PDocsDataModel dataModel;
/*
    public static DocsMODialogFragment newInstance(Parcelable details) {
        DocsMODialogFragment dialogFragment = new DocsMODialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", details);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }  */

    public static DocsMODialogFragment newInstance(String details) {
        DocsMODialogFragment dialogFragment = new DocsMODialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("data", details);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    protected boolean isFullScreenDialog() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_docs_more_menu;
    }

    @Override
    protected void bindViews(View view) {
        viewModel = new ViewModelProvider(this).get(ProjectDocumentVM.class);
        observeViewModel();

        if (null != getArguments()) {
            dataModel=new Gson().fromJson(getArguments().getString("data"), new TypeToken<PDocsDataModel>(){
            }.getType());
//            dataModel = getArguments().getParcelable("data");
        }

    }

    private void observeViewModel() {

        viewModel.isLoadingLD.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                showProgress();
            } else {
                dismissProgress();
            }
        });

        viewModel.isSuccess.observe(getViewLifecycleOwner(), isSucess -> {
            if (isSucess != null) {
                mEditSuccessCallback.successCallback("Document action has been performed successfully.");
                dismiss();
            }
        });

        viewModel.isDeleted.observe(getViewLifecycleOwner(), isSucess -> {
            if (isSucess != null) {
                mEditSuccessCallback.successCallback("Document has been deleted successfully!");
                dismiss();
            }
        });
    }

    @OnClick({R.id.ivClose, R.id.ivTrack, R.id.ivFavourite, R.id.ivComment, R.id.ivShare, R.id.ivDelete,})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                dismiss();
                break;
            case R.id.ivDelete:
                if (getActivity() != null) {
                    new AlertDialog.Builder(getActivity()).setTitle("Delete document")
                            .setMessage("Are you sure want to delete this document?")
                            .setPositiveButton(R.string.ok, (dialog, which) -> {
                                viewModel.deleteProjectDocument(String.valueOf(dataModel.getUid()));
                                dialog.dismiss();
                            })
                            .setNegativeButton(R.string.cancel, null)
                            .create().show();
                }
                break;
            case R.id.ivTrack:
                if (null != dataModel.getUid())
                    viewModel.actionOnDocument(DataNames.ACTION_TRACK, dataModel.getUid());
                break;
            case R.id.ivFavourite:
                if (null != dataModel.getUid())
                    viewModel.actionOnDocument(DataNames.ACTION_FAVOURITE, dataModel.getUid());
                break;
            case R.id.ivComment:
                if (dataModel.getUid() != null && getFragmentManager() != null) {
                    PDocsCommentsDialogFragment commentOption = PDocsCommentsDialogFragment
                            .newInstance(DataNames.SHARE_DOCS, String.valueOf(dataModel.getUid()), dataModel.getTitle());
                    commentOption.show(getFragmentManager(), "comment view");
                    dismiss();
                }
                break;
            case R.id.ivShare:
                if (dataModel.getUid() != null && getFragmentManager() != null) {
                    ShareDialogFragment.newInstance(String.valueOf(dataModel.getUid()), dataModel.getTitle(),
                            getToUser(dataModel.getForwordedDetails()), "",
                            DataNames.SHARE_DOCS).show(getFragmentManager(), "share Docs");
                    dismiss();
                }
                break;
        }
    }

    @Override
    protected void init() {
        setUIData();
    }

    private String getToUser(List<ForwordedDetail> forwardedDetails) {
        if (forwardedDetails != null) {
            ArrayList<String> userNames = new ArrayList<>();
            for (ForwordedDetail detail : forwardedDetails) {
                userNames.add(detail.getFullname());
            }
            return CommonMethods.getCommaSeparatedString(userNames);
        }
        return "";
    }


    private void setUIData() {
        if (null != dataModel) {
//            tvTitle.setText(dataModel.getTitle());

            ivFavourite.setImageResource(dataModel.getIsFav().equals("0") ?
                    R.drawable.stardefaultsvg : R.drawable.starfilledsvg);

            ivTrack.setImageResource(dataModel.getIsTrack().equals("0") ?
                    R.drawable.eyedefaultsvg : R.drawable.eyefilledsvg);

            if (dataModel.getTotalcomments() == 0) {
                ivComment.setImageResource(R.drawable.commentsvg);
            } else if (dataModel.getTotalcomments() == 1) {
                ivComment.setImageResource(R.drawable.ic_comment_filled);
            } else {
                ivComment.setImageResource(R.drawable.ic_multi_comment);
            }
        }
    }

    public void setEditSuccessCallback(EditSuccessCallback mEditSuccessCallback) {
        this.mEditSuccessCallback = mEditSuccessCallback;
    }

}
