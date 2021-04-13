package com.builderstrom.user.views.dialogFragments;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.SnagComment;
import com.builderstrom.user.viewmodels.SnagViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.SnagCommentsListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SnagCommentsDialogFragment extends BaseDialogFragment {
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvNoComment) TextView tvNoData;
    @BindView(R.id.rvComments) RecyclerView rvComments;
    @BindView(R.id.etComment) EditText etComment;
    @BindView(R.id.ivAttachDocument) ImageView ivAttachDocument;

    private boolean canAnswer = true, isFirstTimeLoaded = true;
    private List<SnagComment> commentList = new ArrayList<>();
    private SnagViewModel viewModel;
    private SnagCommentsListAdapter mAdapter;
    private String snagId;


    public static SnagCommentsDialogFragment newInstance(String snagId, boolean canAnswer) {
        SnagCommentsDialogFragment dialogFragment = new SnagCommentsDialogFragment();
        Bundle args = new Bundle();
        args.putString("snagId", snagId);
        args.putBoolean("canAnswer", canAnswer);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    public static SnagCommentsDialogFragment newInstance(String snagId) {
        SnagCommentsDialogFragment dialogFragment = new SnagCommentsDialogFragment();
        Bundle args = new Bundle();
        args.putString("snagId", snagId);
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

        viewModel = new ViewModelProvider(this).get(SnagViewModel.class);
        observeViewModel();

        ivAttachDocument.setVisibility(View.GONE);
        mAdapter = new SnagCommentsListAdapter(commentList, getActivity());
        rvComments.setAdapter(mAdapter);
    }

    private void observeViewModel() {
        viewModel.isLoadingLD.observe(getViewLifecycleOwner(), loader -> {
            if (null != loader) {
                if (loader) showProgress();
                else dismissProgress();
            }
        });

        viewModel.snagCommentsLD.observe(getViewLifecycleOwner(), snagComments -> {
            if (snagComments != null) {
                commentList.clear();
                commentList.addAll(snagComments);
                mAdapter.notifyDataSetChanged();
                tvNoData.setVisibility(commentList.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });


        viewModel.snagCommentAddedLD.observe(getViewLifecycleOwner(), commentAdded -> {
            if (commentAdded != null && commentAdded) {
                refreshComments();
            }
        });

        viewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (null != errorModel && null != getActivity()) {
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);
            }
        });

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

    private void addComment() {
        hideDialogKeyboard();
        etComment.setError(null);
        if (!etComment.getText().toString().trim().isEmpty()) {
            viewModel.addComment(snagId, etComment.getText().toString().trim());
            etComment.setText(null);
        } else {
            etComment.setError(getString(R.string.required_field));
        }
    }

    @Override
    protected void init() {
        tvTitle.setText(getString(R.string.add_comment));
        if (getArguments() != null) {
            snagId = getArguments().getString("snagId");
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
        viewModel.getSnagComments(snagId);
    }


}
