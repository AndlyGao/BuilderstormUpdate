package com.builderstrom.user.views.dialogFragments;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.GalleryComment;
import com.builderstrom.user.viewmodels.ProjectPhotosVM;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.GalleryCommentsListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class GalleryCommentDialogFragment extends BaseDialogFragment {

    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvNoComment) TextView tvNoData;
    @BindView(R.id.etComment) EditText etComment;
    @BindView(R.id.ivAttachDocument) View ivAttachDocument;
    @BindView(R.id.rvComments) RecyclerView rvComments;

    private GalleryCommentsListAdapter mAdapter;
    private List<GalleryComment> commentList = new ArrayList<>();
    private boolean isFirstTime = true;
    private String imageId = "";
    private ProjectPhotosVM viewModel;

    public static GalleryCommentDialogFragment newInstance(String imageId) {
        GalleryCommentDialogFragment dialogFragment = new GalleryCommentDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("imageId", imageId);
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
        viewModel = new ViewModelProvider(this).get(ProjectPhotosVM.class);
        observeViewModel();

        mAdapter = new GalleryCommentsListAdapter(getActivity(), commentList);
        rvComments.setAdapter(mAdapter);

        if (getArguments() != null) {
            imageId = getArguments().getString("imageId");
        }
    }

    private void observeViewModel() {

        viewModel.isLoadingLD.observe(getViewLifecycleOwner(), loader -> {
            if (loader != null) {
                if (loader) showProgress();
                else dismissProgress();
            }
        });

        viewModel.imageCommentsLD.observe(getViewLifecycleOwner(), galleryComments -> {
            if (galleryComments != null) {
                setCommentsAdapter(galleryComments);
            }
        });

        viewModel.successCommentAddedLD.observe(getViewLifecycleOwner(), commentAdded -> {
            if (commentAdded != null && commentAdded) {
                etComment.setText(null);
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
        if (!etComment.getText().toString().isEmpty()) {
            hideDialogKeyboard();
            viewModel.imageCommentAPI(imageId, etComment.getText().toString().trim());
        } else {
            errorMessage(getString(R.string.no_empty_comment), null, false);
        }
    }

    @Override
    protected void init() {
        tvTitle.setText(getString(R.string.add_comment));
        ivAttachDocument.setVisibility(View.GONE);
    }

    private void refreshComments() {
        viewModel.getAllImageComments(imageId);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstTime) {
            refreshComments();
            isFirstTime = false;
        }

    }

    private void setCommentsAdapter(List<GalleryComment> galleryComments) {
        commentList.clear();
        commentList.addAll(galleryComments);
        mAdapter.notifyDataSetChanged();
        tvNoData.setVisibility(commentList.isEmpty() ? View.VISIBLE : View.GONE);
    }


}
