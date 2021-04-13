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
import com.builderstrom.user.repository.retrofit.modals.DrawingCommentModel;
import com.builderstrom.user.viewmodels.DrawingMenuVM;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.CommentAdapter;
import com.builderstrom.user.views.viewInterfaces.CommentCountCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DrawingCommentsDialogFragment extends BaseDialogFragment {


    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvNoComment) TextView tvNoData;
    @BindView(R.id.ivAttachDocument) View ivAttachment;

    @BindView(R.id.rvComments) RecyclerView rvComments;
    @BindView(R.id.etComment) EditText etComment;

    private boolean isCompanyDocument = false;
    private String documentId = "", documentTitle = "";
    private boolean isFirstTimeLoaded = true;
    private CommentAdapter commentAdapter;

    private List<DrawingCommentModel.Comment> commentList = new ArrayList<>();
    private DrawingMenuVM viewModel;
    private CommentCountCallback mCallback;

    public static DrawingCommentsDialogFragment newInstance(String docId) {
        DrawingCommentsDialogFragment dialogFragment = new DrawingCommentsDialogFragment();
        Bundle args = new Bundle();
        args.putString("doc_id", docId);
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
        commentAdapter = new CommentAdapter(getActivity(), commentList);
        viewModel = new ViewModelProvider(this).get(DrawingMenuVM.class);
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

        viewModel.commentLiveData.observe(getViewLifecycleOwner(), comments -> {
            commentList.clear();
            etComment.setText(null);
            if (comments != null) {
                commentList.addAll(comments);
                tvNoData.setVisibility(commentList.isEmpty() ? View.VISIBLE : View.GONE);
                commentAdapter.notifyDataSetChanged();
            }
        });

        viewModel.isSuccess.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                errorMessage("Comment added successfully", null, false);
                refreshComments();
                callBroadCast();
            }
        });

        viewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (errorModel != null && getActivity() != null) {
//                errorMessage(errorModel.getMessage(), null, false);
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);
            }
        });


    }

    private void addComment() {
        hideDialogKeyboard();
        etComment.setError(null);
        if (!etComment.getText().toString().trim().isEmpty()) {
            viewModel.addCommentAPI(documentId, etComment.getText().toString().trim());
        } else {
            etComment.setError(getString(R.string.required_field));
        }
    }

    @Override
    protected void init() {
        tvTitle.setText(getString(R.string.add_comment));
        if (getArguments() != null) {
            documentId = getArguments().getString("doc_id");
        }
        rvComments.setAdapter(commentAdapter);
    }

    @OnClick({R.id.ivClose, R.id.ivAddComment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
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
        viewModel.getDrawingCommentsAPI(documentId);
    }


    private void callBroadCast() {
        Intent broadCastIntent = new Intent(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE);
        broadCastIntent.putExtra("KEY_FLAG", true);
        LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(broadCastIntent);
    }

    public void setCallback(CommentCountCallback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void onDestroyView() {
        if (mCallback != null && commentList != null) {
//            mCallback.updateCount(companyCommentList.size());
        }
        super.onDestroyView();
    }
}
