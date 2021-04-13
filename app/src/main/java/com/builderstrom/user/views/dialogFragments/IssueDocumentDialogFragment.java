package com.builderstrom.user.views.dialogFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.modals.PojoMyItem;
import com.builderstrom.user.repository.retrofit.modals.User;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.MyItemModel;
import com.builderstrom.user.viewmodels.ShareViewModel;
import com.builderstrom.user.views.customViews.textChips.NachoTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class IssueDocumentDialogFragment extends BaseDialogFragment {

    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.ivClose) ImageView ivClose;
    @BindView(R.id.tvAutoUsersRFI) NachoTextView atvToUsers;
    @BindView(R.id.tvAutoCCUsersRFI) NachoTextView atvCcUser;
    @BindView(R.id.btnShareRfi) AppCompatButton btnShare;
    @BindView(R.id.llSharedUsers) LinearLayout llSharedUsers;

    private PojoMyItem item;
    private ShareViewModel viewModel;

    public static IssueDocumentDialogFragment newInstance(PojoMyItem data) {
        IssueDocumentDialogFragment dialogFragment = new IssueDocumentDialogFragment();
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
        return R.layout.dialog_fragment_share;
    }

    @Override
    protected void bindViews(View view) {
        /* set up viewModel */
        viewModel = new ViewModelProvider(this).get(ShareViewModel.class);
        observeViewModel();

        atvToUsers.setHint(getString(R.string.select_users));
        atvCcUser.setVisibility(View.GONE);
        llSharedUsers.setVisibility(View.GONE);
        btnShare.setText(getString(R.string.issue_doc));

        if (getArguments() != null) {
            item = getArguments().getParcelable("data");
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

        viewModel.usersLD.observe(getViewLifecycleOwner(), allUsers -> {
            if (allUsers != null) {
                ArrayAdapter<User> adapter = new ArrayAdapter<>(requireActivity(),
                        android.R.layout.select_dialog_item, allUsers);
                atvToUsers.setThreshold(1);
                atvToUsers.setAdapter(adapter);
                atvCcUser.setThreshold(1);
                atvCcUser.setAdapter(adapter);
            }
        });

        viewModel.isSuccess.observe(getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess) {
                errorMessage("Document issued", null, false);
                callBroadCast();
                dismiss();
            }
        });
    }

    @Override
    protected void init() {
        if (item != null) {
            tvTitle.setText(String.format("Issue Document - %s", item.getTemplateTitle()));
        }
        viewModel.getShareUsers(DataNames.RFI_SECTION_USERS);


    }

    @OnClick({R.id.ivClose, R.id.btnShareRfi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                dismissWithHideKeyboard();
                break;
            case R.id.btnShareRfi:
                hideDialogKeyboard();
                if (item != null) {
                    if (atvToUsers.getText().toString().trim().isEmpty()) {
                        errorMessage("", R.string.please_fill_users, false);
                    } else {
                        MyItemModel model = new MyItemModel(CommonMethods.getCommaSeparatedString(atvToUsers.getChipValues()),
                                item.getTemplateId(), item.getIssue(), item.getRecurrence(),
                                item.getForCompleteDate(), item.getIsCompleted(), viewModel.mPrefs.getProjectId());
                        viewModel.issueDigitalDocument(model);
                    }

                }
                break;
        }

    }


    private void callBroadCast() {
        Intent broadCastIntent = new Intent(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE);
        broadCastIntent.putExtra("KEY_FLAG", true);
        LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(broadCastIntent);
    }

}
