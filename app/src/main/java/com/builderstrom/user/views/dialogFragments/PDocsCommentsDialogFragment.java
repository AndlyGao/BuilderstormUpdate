package com.builderstrom.user.views.dialogFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.modals.CompanyComment;
import com.builderstrom.user.repository.retrofit.modals.PDCommentModel;
import com.builderstrom.user.viewmodels.CompanyViewModel;
import com.builderstrom.user.viewmodels.ProjectDocumentVM;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.CompanyCommentListAdapter;
import com.builderstrom.user.views.adapters.PDocsCommentsListAdapter;
import com.builderstrom.user.views.viewInterfaces.CommentCountCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PDocsCommentsDialogFragment extends BaseDialogFragment {


    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvNoComment) TextView tvNoData;
    @BindView(R.id.ivAttachDocument) View ivAttachment;

    @BindView(R.id.rvComments) RecyclerView rvComments;
    @BindView(R.id.etComment) EditText etComment;

    private boolean isCompanyDocument = false;
    private String documentId = "", documentTitle = "";
    private boolean isFirstTimeLoaded = true;
    private PDocsCommentsListAdapter mProjectAdapter;
    private CompanyCommentListAdapter mCompanyAdapter;

    private List<PDCommentModel.Comment> commentList = new ArrayList<>();
    private List<CompanyComment> companyCommentList = new ArrayList<>();
    private ProjectDocumentVM viewModel;
    private CompanyViewModel companyViewModel;
    private CommentCountCallback mCallback;

    public static PDocsCommentsDialogFragment newInstance(Integer type, String docId, String title) {
        PDocsCommentsDialogFragment dialogFragment = new PDocsCommentsDialogFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putString("doc_id", docId);
        args.putString("doc_title", title);
        dialogFragment.setArguments(args);
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
        ivAttachment.setVisibility(View.GONE);

        mProjectAdapter = new PDocsCommentsListAdapter(getActivity(), commentList);
        mCompanyAdapter = new CompanyCommentListAdapter(getActivity(), companyCommentList);

        viewModel = new ViewModelProvider(this).get(ProjectDocumentVM.class);
        companyViewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
        observeViewModel();


    }

    private void observeViewModel() {

        viewModel.isLoadingLD.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                showProgress();
            } else {
                dismissProgress();
            }
        });


        viewModel.DocCommentLiveList.observe(getViewLifecycleOwner(), comments -> {
            commentList.clear();
            etComment.setText(null);
            if (comments != null) {
                commentList.addAll(comments);
                tvNoData.setVisibility(commentList.isEmpty() ? View.VISIBLE : View.GONE);
                mProjectAdapter.notifyDataSetChanged();
            }
        });

        viewModel.isSuccess.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                refreshComments();
            }
        });


        companyViewModel.isLoadingLD.observe(getViewLifecycleOwner(), loader -> {
            if (loader != null) {
                if (loader) showProgress();
                else dismissProgress();
            }
        });

        companyViewModel.commentsLD.observe(getViewLifecycleOwner(), comments -> {
            if (comments != null) {
                companyCommentList.clear();
                companyCommentList.addAll(comments);
                mCompanyAdapter.notifyDataSetChanged();
                tvNoData.setText(getString(R.string.no_comments_found));
                tvNoData.setVisibility(companyCommentList.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
        companyViewModel.successAddCommentLD.observe(getViewLifecycleOwner(), successMessage -> {
            if (successMessage != null) {
                errorMessage(successMessage, null, false);
                etComment.setText(null);
            }
        });

        companyViewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (errorModel != null && getActivity() != null) {
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);
            }
        });
    }

    private void addComment() {
        hideDialogKeyboard();
        etComment.setError(null);
        if (!etComment.getText().toString().trim().isEmpty()) {
            if (isCompanyDocument) {
                companyViewModel.addComment(documentId, documentTitle, etComment.getText().toString().trim());
            } else {
                viewModel.addComment(documentId, etComment.getText().toString().trim());
            }
        } else {
            etComment.setError(getString(R.string.required_field));
        }
    }

    @Override
    protected void init() {
        tvTitle.setText(getString(R.string.add_comment));

        if (getArguments() != null) {
            isCompanyDocument = getArguments().getInt("type") == DataNames.SHARE_COMPANY_DOCS;
            documentId = getArguments().getString("doc_id");
            documentTitle = getArguments().getString("doc_title");
        }
        rvComments.setAdapter(isCompanyDocument ? mCompanyAdapter : mProjectAdapter);

    }

    @OnClick({R.id.ivClose, R.id.ivAddComment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                callBroadCast();
                dismissWithHideKeyboard();
                break;
            case R.id.ivAddComment:
                addComment();
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isFirstTimeLoaded) {
            isFirstTimeLoaded = false;
            refreshComments();
        }
    }

    private void refreshComments() {
        if (isCompanyDocument) {
            companyViewModel.getAllComments(documentId);
        } else {
            viewModel.projectDocsComments(documentId);
        }
    }

    public void setCallback(CommentCountCallback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void onDestroyView() {
        if (mCallback != null && commentList != null) {
            mCallback.updateCount(companyCommentList.size());
        }
        super.onDestroyView();
    }

    private void callBroadCast() {
        Intent broadCastIntent = new Intent(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE);
        broadCastIntent.putExtra("KEY_FLAG", true);
        LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(broadCastIntent);
    }
}
