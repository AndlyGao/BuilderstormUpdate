package com.builderstrom.user.views.dialogFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.viewmodels.PMViewModel;
import com.builderstrom.user.data.retrofit.modals.ProjectNote;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.NotesAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NotesDialogFragment extends BaseDialogFragment {


    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvNoComment) TextView tvNoData;
    @BindView(R.id.ivAttachDocument) View ivAttachment;

    @BindView(R.id.rvComments) RecyclerView rvComments;
    @BindView(R.id.etComment) EditText etComment;
    @BindView(R.id.rlBottom) RelativeLayout rlBottom;

    private String projectId = "";
    private boolean isFirstTimeLoaded = true;
    private NotesAdapter mAdapter;

    private List<ProjectNote> noteList = new ArrayList<>();
    private PMViewModel viewModel;

    public static NotesDialogFragment newInstance(String projectId) {
        NotesDialogFragment dialogFragment = new NotesDialogFragment();
        Bundle args = new Bundle();
        args.putString("projectId", projectId);
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
        mAdapter = new NotesAdapter(getActivity(), noteList);
        viewModel = new ViewModelProvider(this).get(PMViewModel.class);
        observeViewModel();
        rlBottom.setVisibility(View.GONE);
    }

    private void observeViewModel() {

        viewModel.isLoadingLD.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                showProgress();
            } else {
                dismissProgress();
            }
        });

        viewModel.notesLD.observe(getViewLifecycleOwner(), notes -> {
            noteList.clear();
            etComment.setText(null);
            if (notes != null) {
                noteList.addAll(notes);
                tvNoData.setText("No notes found");
                tvNoData.setVisibility(noteList.isEmpty() ? View.VISIBLE : View.GONE);
                mAdapter.notifyDataSetChanged();
            }
        });

//        viewModel.isSuccess.observe(getViewLifecycleOwner(), aBoolean -> {
//            if (aBoolean) {
//                errorMessage("Comment added successfully", null, false);
//                refreshComments();
//                callBroadCast();
//            }
//        });

        viewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (errorModel != null && getActivity() != null) {
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);
            }
        });

    }

//    private void addComment() {
//        hideDialogKeyboard();
//        etComment.setError(null);
//        if (!etComment.getText().toString().trim().isEmpty()) {
//            viewModel.addCommentAPI(documentId, etComment.getText().toString().trim());
//        } else {
//            etComment.setError(getString(R.string.required_field));
//        }
//    }

    @Override
    protected void init() {
        tvTitle.setText(getString(R.string.notes));
        if (getArguments() != null) {
            projectId = getArguments().getString("projectId");
        }
        rvComments.setAdapter(mAdapter);
    }

    @OnClick({R.id.ivClose, R.id.ivAddComment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                dismissWithHideKeyboard();
                break;
            case R.id.ivAddComment:
//                addComment();
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
        viewModel.getAllNotes(projectId, null, null);
    }


    private void callBroadCast() {
        Intent broadCastIntent = new Intent(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE);
        broadCastIntent.putExtra("KEY_FLAG", true);
        LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(broadCastIntent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
