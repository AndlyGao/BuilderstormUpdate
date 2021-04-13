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
import com.builderstrom.user.repository.retrofit.modals.User;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.ShareViewModel;
import com.builderstrom.user.views.customViews.textChips.NachoTextView;

import butterknife.BindView;
import butterknife.OnClick;

/* types
 * 1- RFI
 * 2- Snag
 * 3- Project Docs
 * 4- Company Docs
 * */

public class ShareDialogFragment extends BaseDialogFragment {

    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.ivClose) ImageView ivClose;
    @BindView(R.id.tvAutoUsersRFI) NachoTextView atvToUsers;
    @BindView(R.id.tvAutoCCUsersRFI) NachoTextView atvCcUser;
    @BindView(R.id.btnShareRfi) AppCompatButton btnShare;
    @BindView(R.id.tvToUsersModified) TextView tvToUsersNotified;
    @BindView(R.id.tvToUsers) TextView tvToUsers;
    @BindView(R.id.tvCcUsers) TextView tvCcUsers;
    @BindView(R.id.llMoreUsers) LinearLayout llMoreUsers;

    private ShareViewModel viewModel;
    private String shareId, toUser = "", ccUser = "", title = "";
    private Integer type;

    public static ShareDialogFragment newInstance(String id, String title, String toUsers,
                                                  String ccUsers, int type) {
        ShareDialogFragment dialogFragment = new ShareDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("title", title);
        bundle.putString("toUser", toUsers);
        bundle.putString("ccUser", ccUsers);
        bundle.putInt("type", type);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_share;
    }

    @Override
    protected void bindViews(View view) {
        viewModel = new ViewModelProvider(this).get(ShareViewModel.class);
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
                errorMessage("Shared successfully", null, false);
                callBroadCast();
                dismiss();
            }
        });
    }

    private String getCurrentSection() {
        switch (type) {
            case DataNames.SHARE_SNAG:
                return DataNames.SNAG_SECTION_USERS;
            case DataNames.SHARE_RFI:
            case DataNames.SHARE_DOCS:
            default:
                return DataNames.RFI_SECTION_USERS;
        }

    }

    @Override
    protected boolean isFullScreenDialog() {
        return false;
    }

    @Override
    protected void init() {
        if (getArguments() != null) {
            type = getArguments().getInt("type");
            shareId = getArguments().getString("id");
            toUser = getArguments().getString("toUser");
            ccUser = getArguments().getString("ccUser");
            title = getArguments().getString("title");
            atvCcUser.setVisibility(type == DataNames.SHARE_DOCS ||
                    type == DataNames.SHARE_COMPANY_DOCS ? View.GONE : View.VISIBLE);
        }
        viewModel.getShareUsers(type == DataNames.SHARE_RFI ? DataNames.RFI_SECTION_USERS :
                (type == DataNames.SHARE_SNAG ? DataNames.SNAG_SECTION_USERS :
                        (type == DataNames.SHARE_DOCS ? DataNames.PROJECT_DOCS_SECTION_USERS :
                                (type == DataNames.SHARE_COMPANY_DOCS ? DataNames.COMPANY_DOCS_SECTION_USERS : ""))));

        setToolbar();
        setShareButtonText();
        setNotifiedView();
    }

    private void setShareButtonText() {
        btnShare.setText(getString(type != null ?
                (type == DataNames.SHARE_RFI ? R.string.share_rfi : (type == DataNames.SHARE_SNAG ?
                        R.string.share_snag : (type == DataNames.SHARE_COMPANY_DOCS ? R.string.share_document :
                        R.string.share_content))) : R.string.share_content));
    }

    private void setNotifiedView() {
        if (toUser.trim().isEmpty() && ccUser.trim().isEmpty()) {
            llMoreUsers.setVisibility(View.GONE);
            switch (type) {
                case DataNames.SHARE_RFI:
                case DataNames.SHARE_COMPANY_DOCS:
                case DataNames.SHARE_DOCS:
                    tvToUsersNotified.setText(CommonMethods.spannedText(String.format("User Notified: %s", "No User Notified"), 0, 14));
                    break;
                case DataNames.SHARE_SNAG:
                    tvToUsersNotified.setText(CommonMethods.spannedText(String.format("Distribution: %s", "No User Notified"), 0, 13));
                    break;
            }

        } else {
            switch (type) {
                case DataNames.SHARE_RFI:
                case DataNames.SHARE_DOCS:
                case DataNames.SHARE_COMPANY_DOCS:
                    tvToUsersNotified.setText(CommonMethods.spannedText(String.format("User Notified: %s", ""), 0, 14));
                    break;
                case DataNames.SHARE_SNAG:
                    tvToUsersNotified.setText(CommonMethods.spannedText(String.format("Distribution: %s", ""), 0, 13));
                    break;
            }

            llMoreUsers.setVisibility(View.VISIBLE);
            tvToUsers.setVisibility(toUser.isEmpty() ? View.GONE : View.VISIBLE);
            tvCcUsers.setVisibility(ccUser.isEmpty() ? View.GONE : View.VISIBLE);
            if (!toUser.isEmpty()) {
                tvToUsers.setText(CommonMethods.spannedText(String.format("To User(s) : %s", toUser), 0, 12));
            }

            if (!ccUser.isEmpty() && type != DataNames.SHARE_DOCS) {
                tvCcUsers.setText(CommonMethods.spannedText(String.format("CC User(s) : %s", ccUser), 0, 12));
            }
        }
    }

    private void setToolbar() {
        tvTitle.setText(getString(type != null ?
                (type == DataNames.SHARE_RFI ? R.string.share_rfi_title :
                        (type == DataNames.SHARE_SNAG ? R.string.share_snag :
                                (type == DataNames.SHARE_DOCS ? R.string.share_document :
                                        (type == DataNames.SHARE_COMPANY_DOCS ? R.string.share_company_document : R.string.share_any))))
                : R.string.share_any));
    }


    private void callBroadCast() {
        Intent broadCastIntent = new Intent(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE);
        broadCastIntent.putExtra("KEY_FLAG", true);
        LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(broadCastIntent);
    }

    @OnClick({R.id.ivClose, R.id.btnShareRfi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                dismissWithHideKeyboard();
                break;
            case R.id.btnShareRfi:
                hideDialogKeyboard();
                if (validConditions()) {
                    switch (type) {
                        case DataNames.SHARE_COMPANY_DOCS:
                            viewModel.shareCompanyDoc(shareId, title, CommonMethods.getCommaSeparatedString(atvToUsers.getChipValues()));
                            break;
                        case DataNames.SHARE_DOCS:
                            viewModel.shareDocument(shareId, CommonMethods.getCommaSeparatedString(atvToUsers.getChipValues()));
                            break;
                        case DataNames.SHARE_RFI:
                            viewModel.shareRFI(shareId,
                                    CommonMethods.getCommaSeparatedString(atvToUsers.getChipValues()),
                                    CommonMethods.getCommaSeparatedString(atvCcUser.getChipValues()));
                            break;
                        case DataNames.SHARE_SNAG:
                            viewModel.shareSnag(shareId,
                                    CommonMethods.getCommaSeparatedString(atvToUsers.getChipValues()),
                                    CommonMethods.getCommaSeparatedString(atvCcUser.getChipValues()));
                            break;

                    }
                }
//                if (type == DataNames.SHARE_COMPANY_DOCS) {
//                    if (atvToUsers.getText().toString().trim().isEmpty()) {
//                        errorMessage("", R.string.please_fill_to_users, false);
//                    } else {
//                        viewModel.shareCompanyDoc(shareId, title, CommonMethods.getCommaSeparatedString(atvToUsers.getChipValues()));
//                    }
//                } else
//                if (type == DataNames.SHARE_DOCS) {
//                    if (atvToUsers.getText().toString().trim().isEmpty()) {
//                        errorMessage("", R.string.please_fill_to_users, false);
//                    } else {
//                        viewModel.shareDocument(shareId, CommonMethods.getCommaSeparatedString(atvToUsers.getChipValues()));
//                    }
//                } else
//                if (type == DataNames.SHARE_RFI) {
//                    if (atvCcUser.getText().toString().trim().isEmpty() && atvToUsers.getText().toString().trim().isEmpty()) {
//                        errorMessage("", R.string.please_fill_one_from_to_cc, false);
//                    } else {
//                        viewModel.shareRFI(shareId,
//                                CommonMethods.getCommaSeparatedString(atvToUsers.getChipValues()),
//                                CommonMethods.getCommaSeparatedString(atvCcUser.getChipValues()));
//                    }
//                } else {
//                    if (atvCcUser.getText().toString().trim().isEmpty() && atvToUsers.getText().toString().trim().isEmpty()) {
//                        errorMessage("", R.string.please_fill_one_from_to_cc, false);
//                    } else {
//                        viewModel.shareContent(shareId,
//                                CommonMethods.getCommaSeparatedString(atvToUsers.getChipValues()),
//                                CommonMethods.getCommaSeparatedString(atvCcUser.getChipValues()),
//                                getCurrentSection());
//                    }
//                }
                break;
        }
    }

    private boolean validConditions() {
        switch (type) {
            case DataNames.SHARE_COMPANY_DOCS:
            case DataNames.SHARE_DOCS:
                if (atvToUsers.getText().toString().trim().isEmpty()) {
                    errorMessage("", R.string.please_fill_to_users, false);
                    return false;
                } else return true;

            default:
                /* Include share rfi*/
                if (atvCcUser.getText().toString().trim().isEmpty() && atvToUsers.getText().toString().trim().isEmpty()) {
                    errorMessage("", R.string.please_fill_one_from_to_cc, false);
                    return false;
                } else return true;
        }

    }

}
