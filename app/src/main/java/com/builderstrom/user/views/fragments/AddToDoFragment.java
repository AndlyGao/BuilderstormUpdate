package com.builderstrom.user.views.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.Attachment;
import com.builderstrom.user.repository.retrofit.modals.CatListing;
import com.builderstrom.user.repository.retrofit.modals.PojoToDo;
import com.builderstrom.user.repository.retrofit.modals.User;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.PermissionUtils;
import com.builderstrom.user.viewmodels.ToDoViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.activities.DashBoardActivity;
import com.builderstrom.user.views.adapters.AttachmentsAdapter;
import com.builderstrom.user.views.customViews.textChips.NachoTextView;
import com.builderstrom.user.views.viewInterfaces.UpdateCompanyDocsListing;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddToDoFragment extends BaseFragment {

    @BindView(R.id.includeToolbar) Toolbar toolbar;
    @BindView(R.id.tvToUsers) NachoTextView tvToUsers;
    @BindView(R.id.tvCcUsers) NachoTextView tvCCUsers;
    @BindView(R.id.etTitle) EditText etTitle;
    @BindView(R.id.etDueDate) EditText etDueDate;
    @BindView(R.id.etDescription) EditText etDescription;
    @BindView(R.id.etCategory) EditText etCategory;
    @BindView(R.id.rvAttachments) RecyclerView rvAttachments;
    private List<Attachment> attachFiles = new ArrayList<>();
    private AttachmentsAdapter attachmentsAdapter;
    private List<CatListing> categories = new ArrayList<>();
    private String categoryId = "";
    private boolean updatePrevious = false;
    private UpdateCompanyDocsListing mCallback;
    private ToDoViewModel viewModel;
    private PojoToDo editToDoModel;
    private Integer todo_ImageId = null;

    public static AddToDoFragment newInstance(PojoToDo toDoData) {
        AddToDoFragment fragment = new AddToDoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", toDoData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_to_do;
    }

    @Override
    protected void bindView(View view) {
        viewModel = new ViewModelProvider(this).get(ToDoViewModel.class);
        observeViewModel();

        if (getArguments() != null) {
            editToDoModel = getArguments().getParcelable("data");
        }
        attachmentsAdapter = new AttachmentsAdapter(getActivity(), attachFiles,
                true, viewModel);
        rvAttachments.setAdapter(attachmentsAdapter);
    }

    private void observeViewModel() {

        viewModel.isLoadingLD.observe(getViewLifecycleOwner(), loading -> {
            if (loading != null && isAdded()) {
                if (loading) showProgress();
                else dismissProgress();
            }
        });

        viewModel.addToDoSuccessLD.observe(getViewLifecycleOwner(), onSuccess -> {
            if (onSuccess != null && isAdded()) {
                updatePrevious = true;
                popOutFragment();
            }
        });


        viewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (null != errorModel && isAdded() && null != getActivity()) {
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);
            }
        });

        viewModel.usersLD.observe(getViewLifecycleOwner(), allUsers -> {
            if (null != getActivity() && null != allUsers) {
                ArrayAdapter<User> adapter = new ArrayAdapter<>(
                        getActivity(), android.R.layout.select_dialog_item, allUsers);
                tvToUsers.setThreshold(1);
                tvToUsers.setAdapter(adapter);
                tvCCUsers.setThreshold(1);
                tvCCUsers.setAdapter(adapter);
            }
        });

        viewModel.categoriesLD.observe(getViewLifecycleOwner(), catListings -> {
            if (catListings != null && isAdded()) {
                categories.clear();
                categories.addAll(catListings);
            }
        });


    }

    @Override
    protected void init() {
        viewModel.getUsers();
        /* setUp Toolbar */
        toolbar.setTitle(getString(editToDoModel != null ? R.string.edit_to_do : R.string.add_new_to_do));
        toolbar.setNavigationOnClickListener(v -> popOutFragment());

        if (editToDoModel != null) {
            tvToUsers.setTextWithChips(viewModel.setChipInfo(editToDoModel.getToUsers()));
            tvCCUsers.setTextWithChips(viewModel.setChipInfo(editToDoModel.getCcUsers()));
            etTitle.setText(editToDoModel.getTitle());
            etDueDate.setText(CommonMethods.convertDate(CommonMethods.DF_2, editToDoModel.getDueDate(), CommonMethods.DF_7));
            etCategory.setText(editToDoModel.getCategoryTitle());
            categoryId = String.valueOf(editToDoModel.getCategory());
            etDescription.setText(editToDoModel.getDescription());
            if (editToDoModel.getAttachments() != null) {
                attachFiles.clear();
                attachFiles.addAll(editToDoModel.getAttachments());
                if (editToDoModel.getAttachments().size() > 0) {
                    todo_ImageId = attachFiles.get(0).getTodo_image_id() != null ? attachFiles.get(0).getTodo_image_id() : 0;
                }
                attachmentsAdapter.notifyDataSetChanged();
            }
        }
        viewModel.getToDoCategories();
    }

    @OnClick({R.id.etDueDate, R.id.etCategory, R.id.btnUpload, R.id.btnSave})
    public void onClick(View view) {
        hideKeyboard();
        switch (view.getId()) {
            case R.id.etDueDate:
                if (null != getActivity()) {
                    Calendar calendar = Calendar.getInstance();
                    DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(),
                            R.style.DatePickerTheme, (pickerView, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        etDueDate.setText(CommonMethods.getDateFormat(CommonMethods.DF_7).format(calendar.getTime()));
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    pickerDialog.show();
                }
                break;
            case R.id.etCategory:
                openCategoriesPopUp();
                break;
            case R.id.btnUpload:
                checkPermission();
                break;
            case R.id.btnSave:
                if (checkValidations()) {
                    PojoToDo toDo = new PojoToDo();
                    toDo.setId(editToDoModel != null ? editToDoModel.getId() : 0);
                    toDo.setStatus(editToDoModel != null ? editToDoModel.getStatus() : 1);
                    toDo.setTemplateTask(editToDoModel != null ? editToDoModel.getTemplateTask() : 0);
                    toDo.setProjectId(Integer.parseInt(viewModel.mPrefs.getProjectId()));
                    toDo.setTitle(etTitle.getText().toString().trim());
                    toDo.setDescription(etDescription.getText().toString().trim());
                    toDo.setCategory(Integer.parseInt(categoryId));
                    toDo.setCategoryTitle(etCategory.getText().toString().trim());
                    toDo.setDueDate(CommonMethods.convertDate(CommonMethods.DF_7,
                            etDueDate.getText().toString().trim(), CommonMethods.DF_2));
                    toDo.setToUsers(tvToUsers.getChipUsers());
                    toDo.setStrToUsers(CommonMethods.getCommaSeparatedString(tvToUsers.getChipValues()));
                    toDo.setCcUsers(tvCCUsers.getChipUsers());
                    toDo.setStrCcUsers(CommonMethods.getCommaSeparatedString(tvCCUsers.getChipValues()));
                    toDo.setAttachments(attachFiles);
                    toDo.setAddedOn(CommonMethods.getCurrentDate(CommonMethods.DF_1));
                    toDo.setImgae_Id(todo_ImageId != null ? String.valueOf(todo_ImageId) : "");

                    if (editToDoModel != null) {
                        viewModel.editToDo(toDo);
                    } else {
                        viewModel.addToDo(toDo);
                    }
                }
                break;
        }

    }

    private boolean checkValidations() {
        clearAllErrors();
        if (etDueDate.getText().toString().trim().isEmpty()) {
            etDueDate.setError(getString(R.string.required_field));
//            errorMessage("", R.string.please_enter_due_date, false);
            return false;
        } else if (etTitle.getText().toString().trim().isEmpty()) {
            etTitle.setError(getString(R.string.required_field));
//            errorMessage("", R.string.please_enter_title, false);
            return false;
        } else if (etDescription.getText().toString().trim().isEmpty()) {
            etDescription.setError(getString(R.string.required_field));
//            errorMessage("", R.string.please_enter_description, false);
            return false;
        } else if (etCategory.getText().toString().trim().isEmpty()) {
            etCategory.setError(getString(R.string.required_field));
//            errorMessage("", R.string.please_enter_category, false);
            return false;
        }

        return true;

    }

    private void clearAllErrors() {
        etCategory.setError(null);
        etDescription.setError(null);
        etTitle.setError(null);
        etDueDate.setError(null);
    }

    private void openCategoriesPopUp() {
        if (null != getActivity()) {
            if (!categories.isEmpty()) {
                ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
                listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(),
                        R.layout.row_dropdown, R.id.tvDropDown, categories));
                listPopupWindow.setAnchorView(etCategory);
                listPopupWindow.setModal(true);
                listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                    etCategory.setText(categories.get(position).getTitle());
                    categoryId = categories.get(position).getId();
                    Log.e("selected category id", categoryId);
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
                String fileLocation = CommonMethods.getFilePathFromURI(getActivity(), data.getData());
                if (fileLocation != null) {
                    String filename = CommonMethods.getFileNameFromPath(fileLocation);
                    File destinationFile = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS), filename);
                    CommonMethods.copyFile(new File(fileLocation), destinationFile);
                    attachFiles.clear();
                    attachFiles.add(new Attachment(fileLocation, true));
                    attachmentsAdapter.notifyDataSetChanged();
                }
            } else {
                errorMessage("unable to read file", null, false);
            }
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
        if (updatePrevious && null != mCallback) {
            // add callback to update
            mCallback.onUpdateBack();

        }
        super.onDestroyView();
    }

    public void setCallback(UpdateCompanyDocsListing mCallback) {
        this.mCallback = mCallback;
    }
}
