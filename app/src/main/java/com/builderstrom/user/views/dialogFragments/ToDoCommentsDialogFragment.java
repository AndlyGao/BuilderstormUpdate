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
import com.builderstrom.user.repository.retrofit.modals.ToDoComments;
import com.builderstrom.user.viewmodels.CompanyViewModel;
import com.builderstrom.user.viewmodels.ProjectDocumentVM;
import com.builderstrom.user.viewmodels.ToDoViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.CompanyCommentListAdapter;
import com.builderstrom.user.views.adapters.PDocsCommentsListAdapter;
import com.builderstrom.user.views.adapters.ToDoCommentsListAdapter;
import com.builderstrom.user.views.viewInterfaces.CommentCountCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ToDoCommentsDialogFragment extends BaseDialogFragment {


    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvNoComment) TextView tvNoData;
    @BindView(R.id.ivAttachDocument) View ivAttachment;

    @BindView(R.id.rvComments) RecyclerView rvComments;
    @BindView(R.id.etComment) EditText etComment;

    private String toDoId = "";
    private boolean isFirstTimeLoaded = true;
    private ToDoCommentsListAdapter mAdapter;
    private List<ToDoComments> commentsList = new ArrayList<>();

    private ToDoViewModel viewModel;

    public static ToDoCommentsDialogFragment newInstance(Integer id) {
        ToDoCommentsDialogFragment dialogFragment = new ToDoCommentsDialogFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
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

        viewModel = new ViewModelProvider(this).get(ToDoViewModel.class);
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


        viewModel.commentsLD.observe(getViewLifecycleOwner(), comments -> {
            if (comments != null && isAdded()) {
                commentsList.clear();
                etComment.setText(null);
                commentsList.addAll(comments);
                tvNoData.setVisibility(commentsList.isEmpty() ? View.VISIBLE : View.GONE);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void addComment() {
        hideDialogKeyboard();
        etComment.setError(null);
        if (!etComment.getText().toString().trim().isEmpty()) {
            /* Add Comment here*/
            viewModel.addToDoComment(toDoId, etComment.getText().toString().trim());
        } else {
            etComment.setError(getString(R.string.required_field));
        }
    }

    @Override
    protected void init() {
        tvTitle.setText(getString(R.string.comments));

        if (getArguments() != null) {
            toDoId = String.valueOf(getArguments().getInt("id"));
        }
        mAdapter = new ToDoCommentsListAdapter(getActivity(), commentsList);
        rvComments.setAdapter(mAdapter);

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
        /* getAll Comments*/
        viewModel.getAllComments(toDoId);
    }


    private void callBroadCast() {
        Intent broadCastIntent = new Intent(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE);
        broadCastIntent.putExtra("KEY_FLAG", true);
        LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(broadCastIntent);
    }
}
