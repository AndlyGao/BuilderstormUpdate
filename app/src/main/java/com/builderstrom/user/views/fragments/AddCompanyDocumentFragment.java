package com.builderstrom.user.views.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.CatListing;
import com.builderstrom.user.data.retrofit.modals.CompanyDocument;
import com.builderstrom.user.data.retrofit.modals.CompanyStatus;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.PermissionUtils;
import com.builderstrom.user.viewmodels.CompanyViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.activities.DashBoardActivity;
import com.builderstrom.user.views.viewInterfaces.UpdateCompanyDocsListing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddCompanyDocumentFragment extends BaseFragment {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.etUploadFile) EditText etUploadFile;
    @BindView(R.id.etStatus) EditText etStatus;
    @BindView(R.id.etCategory) EditText etCategory;
    @BindView(R.id.etTitle) EditText etTitle;
    @BindView(R.id.etRevision) EditText etRevision;
    @BindView(R.id.etDrawingNo) EditText etDrawingNo;
    @BindView(R.id.etPinComment) EditText etPinComment;
    @BindView(R.id.cbRegDocument) CheckBox cbRegDoc;
    @BindView(R.id.cbSigned) CheckBox cbSigned;
    private String categoryId = "", parentId = "0", statusId = "", filename = "";
    private CompanyViewModel viewModel;
    private List<CompanyStatus> statusList = new ArrayList<>();
    private List<CatListing> categoriesList = new ArrayList<>();
    private UpdateCompanyDocsListing mCallBack;
    private boolean updateList = false;
    private CompanyDocument mDocument;

    public static AddCompanyDocumentFragment newInstance(CompanyDocument data) {
        AddCompanyDocumentFragment fragment = new AddCompanyDocumentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_company_document;
    }

    @Override
    protected void bindView(View view) {
        viewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
        observeViewModel();
        if (getArguments() != null) {
            mDocument = getArguments().getParcelable("data");
        }
    }

    @Override
    protected void init() {
        /* setUp Toolbar */
        toolbar.setNavigationOnClickListener(v -> popOutFragment());

        /* setUp Edit view */
        if (mDocument != null) {
            toolbar.setTitle(getString(R.string.edit_company_document));
            categoryId = mDocument.getCategory();
            statusId = mDocument.getDoc_status();
            etStatus.setText(mDocument.getStatus_title());
            etTitle.setText(mDocument.getTitle());
            etCategory.setText(mDocument.getCat_title());
            etUploadFile.setText(mDocument.getFilename());
            filename = "";
            etRevision.setText(mDocument.getRevision());
            etDrawingNo.setText(mDocument.getDocument_no());
            etPinComment.setText(mDocument.getPinnedcomment() != null ? mDocument.getPinnedcomment().getComment() : null);
            cbSigned.setChecked(mDocument.getSigned_doc() != null && mDocument.getSigned_doc().equals("1"));
            cbRegDoc.setChecked(mDocument.getRegional_doc() != null && mDocument.getRegional_doc().equals("1"));
        } else {
            toolbar.setTitle(getString(R.string.add_company_document));
        }

        viewModel.getCompanyStatusList();
        viewModel.getCompanyCategories();

    }

    private void observeViewModel() {

        viewModel.isLoadingLD.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean != null) {
                if (aBoolean) showProgress();
                else dismissProgress();
            }
        });

        viewModel.companyStatusLD.observe(getViewLifecycleOwner(), companyStatuses -> {
            if (companyStatuses != null) {
                statusList.clear();
                statusList.addAll(companyStatuses);
            }
        });

        viewModel.companyCategoryLD.observe(getViewLifecycleOwner(), companyCategories -> {
            if (companyCategories != null) {
                categoriesList.clear();
                categoriesList.addAll(companyCategories);
            }
        });

        viewModel.addedSuccessLD.observe(getViewLifecycleOwner(), addedSuccess -> {
            if (addedSuccess != null && addedSuccess) {
                updateList = true;
                popOutFragment();
            }
        });

        viewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (errorModel != null && getActivity() != null) {
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);
            }
        });
    }

    @OnClick({R.id.etStatus, R.id.etCategory, R.id.etUploadFile, R.id.btnSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etStatus:
                openStatusPopUp();
                break;
            case R.id.etCategory:
                openCategoriesPopUp();
                break;
            case R.id.etUploadFile:
                checkPermission();
                break;
            case R.id.btnSubmit:
                submitDocument();
                break;
        }
    }

    private void submitDocument() {
        if (etTitle.getText().toString().trim().isEmpty()) {
            errorMessage("please enter title", null, false);
        } else if (etStatus.getText().toString().trim().isEmpty()) {
            errorMessage("please select status ", null, false);
        } else if (etCategory.getText().toString().trim().isEmpty()) {
            errorMessage("please select category", null, false);
        } else if (etRevision.getText().toString().trim().isEmpty()) {
            errorMessage("please enter revision", null, false);
        } else if (etUploadFile.getText().toString().trim().isEmpty()) {
            errorMessage("please select a file", null, false);
        } else {
            if (mDocument != null) {
                viewModel.editCompanyDocument(mDocument.getId(), etTitle.getText().toString().trim(),
                        categoryId, etRevision.getText().toString().trim(), parentId,
                        cbSigned.isChecked() ? "1" : "0", etPinComment.getText().toString().trim(),
                        statusId, cbRegDoc.isChecked() ? "1" : "0", filename,
                        etDrawingNo.getText().toString().trim());
            } else {
                viewModel.addCompanyDocument(etTitle.getText().toString().trim(), categoryId,
                        etCategory.getText().toString().trim(), etRevision.getText().toString().trim(),
                        parentId, cbSigned.isChecked() ? "1" : "0", etPinComment.getText().toString().trim(),
                        statusId, cbRegDoc.isChecked() ? "1" : "0", filename,
                        etDrawingNo.getText().toString().trim());

            }
        }
    }

    private void openStatusPopUp() {
        if (null != getActivity()) {
            hideKeyboard();
            if (!statusList.isEmpty()) {
                ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
                listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(),
                        R.layout.row_dropdown, R.id.tvDropDown, statusList));
                listPopupWindow.setAnchorView(etStatus);
                listPopupWindow.setModal(true);
                listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                    etStatus.setText(statusList.get(position).getTitle());
                    statusId = statusList.get(position).getId();
                    listPopupWindow.dismiss();
                });
                listPopupWindow.show();
            } else {
                errorMessage("No status available", null, false);
            }
        }
    }

    private void openCategoriesPopUp() {
        if (null != getActivity()) {
            hideKeyboard();
            if (!categoriesList.isEmpty()) {
                ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
                listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(),
                        R.layout.row_dropdown, R.id.tvDropDown, categoriesList));
                listPopupWindow.setAnchorView(etCategory);
                listPopupWindow.setModal(true);
                listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                    etCategory.setText(categoriesList.get(position).getTitle());
                    categoryId = categoriesList.get(position).getId();
                    /* parentId = categoriesList.get(position).getParent_category();*/
                    listPopupWindow.dismiss();
                });
                listPopupWindow.show();
            } else {
                errorMessage("No categories available", null, false);
            }
        }
    }

    private void checkPermission() {
        if (permissionUtils == null) {
            permissionUtils = new PermissionUtils(this);
        }
        if (permissionUtils.isPermissionGrantedForExtStorage()) {
            openExplorer("*/*", CommonMethods.FILE_REQUEST_CODE);
        } else {
            permissionUtils.requestFragmentPermissionForExtStorage();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            ((DashBoardActivity) getActivity()).lockNavDrawer();
        }
    }

    @Override
    public void onDestroyView() {
        if (getActivity() != null) {
            ((DashBoardActivity) getActivity()).unlockNavDrawer();
        }
        if (updateList && mCallBack != null) {
            mCallBack.onUpdateBack();
        }
        super.onDestroyView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.STORAGE_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openExplorer("*/*", CommonMethods.FILE_REQUEST_CODE);
            } else {
                errorMessage(getString(R.string.storage_permission_needed), null, false);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonMethods.FILE_REQUEST_CODE) {
            if (data != null && data.getData() != null) {
                filename = CommonMethods.getFilePathFromURI(getActivity(), data.getData());
                File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        CommonMethods.getFileName(getActivity(), data.getData()));
                CommonMethods.copyFile(new File(filename), destinationFile);
                etUploadFile.setText(CommonMethods.getFileName(getActivity(), data.getData()));
            } else {
                errorMessage("unable to read file", null, false);
            }
        }
    }

    public void setCallBack(UpdateCompanyDocsListing mCallBack) {
        this.mCallBack = mCallBack;
    }

}
