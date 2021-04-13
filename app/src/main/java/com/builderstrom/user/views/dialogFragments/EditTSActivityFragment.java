package com.builderstrom.user.views.dialogFragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.ListPopupWindow;
import androidx.lifecycle.ViewModelProviders;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.modals.PojoPriceWorkModel;
import com.builderstrom.user.repository.retrofit.modals.ProjectDetails;
import com.builderstrom.user.repository.retrofit.modals.ReturnOverviewDetail;
import com.builderstrom.user.repository.retrofit.modals.TSNote;
import com.builderstrom.user.repository.retrofit.modals.TaskListing;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.TimeSheetViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.viewInterfaces.OfflineEditTSCallback;
import com.builderstrom.user.views.viewInterfaces.TaskReturnInterface;
import com.builderstrom.user.views.viewInterfaces.EditSuccessCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EditTSActivityFragment extends BaseDialogFragment {

    @BindView(R.id.tvWorkTask)
    TextView tvWorkTask;
    @BindView(R.id.tvPriceItem)
    TextView tvPriceItem;
    @BindView(R.id.tvPriceWorkItem)
    TextView tvPriceWorkItem;

    @BindView(R.id.llOverTime)
    View viewWorkTime;
    @BindView(R.id.llTravel)
    View viewTravel;
    @BindView(R.id.llBreaks)
    View viewBreaks;
    @BindView(R.id.llExpenses)
    View viewExpense;
    @BindView(R.id.llPriceWork)
    View viewPriceWork;
    @BindView(R.id.llHoliday)
    View viewHoliday;
    @BindView(R.id.llSickness)
    View viewSick;
    @BindView(R.id.llNote)
    View viewNote;

    @BindView(R.id.etWorkStartTime)
    EditText etWorkStartTime;
    @BindView(R.id.etWorkEndTime)
    EditText etWorkEndTime;
    @BindView(R.id.etWorkHours)
    EditText etWorkHours;
    @BindView(R.id.etBreakStartTime)
    EditText etBreakStartTime;
    @BindView(R.id.etBreakEndTime)
    EditText etBreakEndTime;
    @BindView(R.id.etBreakTime)
    EditText etBreakHours;
    @BindView(R.id.etTravelStartTime)
    EditText etTravelStartTime;
    @BindView(R.id.etTravelEndTime)
    EditText etTravelEndTime;
    @BindView(R.id.etTravelFrom)
    EditText etTravelFrom;
    @BindView(R.id.etTravelTo)
    EditText etTravelTo;
    @BindView(R.id.etExpenseItem)
    EditText etExpenseItem;
    @BindView(R.id.etExpenseCost)
    EditText etExpenseCost;
    @BindView(R.id.etPriceWorkLocation)
    EditText etPriceWorkLocation;
    @BindView(R.id.etPriceWorkQuantity)
    EditText etPriceWorkQuantity;
    @BindView(R.id.etHolidayStartTime)
    EditText etHolidayStartTime;
    @BindView(R.id.etHolidayEndTime)
    EditText etHolidayEndTime;
    @BindView(R.id.etHolidayHours)
    EditText etHolidayHours;
    @BindView(R.id.etSickStartTime)
    EditText etSickStartTime;
    @BindView(R.id.etSickEndTime)
    EditText etSickEndTime;
    @BindView(R.id.etSickHours)
    EditText etSickHours;
    @BindView(R.id.etNote)
    EditText etNote;

    private TimeSheetViewModel viewModel;

    private TSNote noteDetails;
    private ReturnOverviewDetail activityDetails;

    private List<ProjectDetails> projectsList = new ArrayList<>();
    private List<PojoPriceWorkModel.PriceWorkListing> priceWorkList = new ArrayList<>();
    private List<TaskListing> taskList = new ArrayList<>();
    private List<String> taskIdList = new ArrayList<>();

    private String globalDate = "", travelFromProject = "", travelToProject = "",
            selectedPriceId = "", selectedItemPrice = "";
    private Integer UnfinishedActId;
    private Boolean isOverTimeBreak;

    private EditSuccessCallback mEditSuccessCallback;
    private OfflineEditTSCallback offlineCallback;

    public static EditTSActivityFragment newInstance(int type, Parcelable details,
                                                     String globalDate, Integer unfinishedActId, Boolean isOverTimeBreak) {
        EditTSActivityFragment dialogFragment = new EditTSActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putParcelable("data", details);
        bundle.putString("globalDate", globalDate);
        bundle.putInt("UnfinishedActId", unfinishedActId);
        bundle.putBoolean("OverTimeBreak", isOverTimeBreak);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    public static EditTSActivityFragment newInstance(int type, String globalDate, Parcelable details) {
        EditTSActivityFragment dialogFragment = new EditTSActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("globalDate", globalDate);
        bundle.putParcelable("data", details);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    protected boolean isFullScreenDialog() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_edit_ts_activity;
    }

    @Override
    protected void bindViews(View view) {
        viewModel = ViewModelProviders.of(this).get(TimeSheetViewModel.class);
        observeViewModel();
        /* get all arguments*/
        if (null != getArguments()) {
            globalDate = getArguments().getString("globalDate");
            if (getArguments().getInt("type") == 1) {
                activityDetails = getArguments().getParcelable("data");
                UnfinishedActId = getArguments().getInt("UnfinishedActId");
                isOverTimeBreak = getArguments().getBoolean("OverTimeBreak");
            } else {
                noteDetails = getArguments().getParcelable("data");
            }
        }
    }

    @OnClick({R.id.ivClose, R.id.tvPriceWorkItem, R.id.tvWorkTask, R.id.etWorkStartTime, R.id.etWorkEndTime,
            R.id.etBreakStartTime, R.id.etBreakEndTime, R.id.etTravelStartTime, R.id.etTravelEndTime,
            R.id.etTravelFrom, R.id.etTravelTo, R.id.btnOvertimeSave, R.id.btnBreakSave, R.id.btnPriceSave,
            R.id.btnTravelSave, R.id.btnExpenseSave, R.id.btnNoteSave})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                dismissWithHideKeyboard();
                break;
            case R.id.tvPriceWorkItem:
                showPriceWorkDropdown();
                break;
            case R.id.tvWorkTask:
                if (taskList.size() > 0) {
                    AddTaskDialogFragment mAddTaskDialogFragment = AddTaskDialogFragment.newInstance(
                            taskList);
                    mAddTaskDialogFragment.setTaskReturnInterface(new TaskReturnInterface() {
                        @Override
                        public void updateTaskList(List<String> taskNewList) {
                            if (null != taskNewList) {
                                taskIdList.clear();
                                taskIdList.addAll(taskNewList);
                                setTaskText();
                            }
                        }

                        @Override
                        public void cancelTaskList(List<String> strings) {
                            if (null != strings) {
                                taskIdList.clear();
                                setTaskText();
                            }
                        }
                    });
                    mAddTaskDialogFragment.show(requireFragmentManager(), "Add Task");
                } else {
                    errorMessage("No task available", null, false);
                }
                break;
            case R.id.etWorkStartTime:
                openTimePicker(etWorkStartTime);
                break;
            case R.id.etWorkEndTime:
                openTimePicker(etWorkEndTime);
                break;
            case R.id.etBreakStartTime:
                openTimePicker(etBreakStartTime);
                break;
            case R.id.etBreakEndTime:
                openTimePicker(etBreakEndTime);
                break;
            case R.id.etTravelStartTime:
                openTimePicker(etTravelStartTime);
                break;
            case R.id.etTravelEndTime:
                openTimePicker(etTravelEndTime);
                break;
            case R.id.etTravelFrom:
                showLocationDropdown(false);
                break;
            case R.id.etTravelTo:
                showLocationDropdown(true);
                break;
            case R.id.btnOvertimeSave:
                if (checkForSetting(activityDetails.getSettingTrue())) {
                    etWorkHours.setError(null);
                    if (!etWorkHours.getText().toString().trim().isEmpty()) {

                        if (isInternetAvailable()) {
                            viewModel.apiAddWorkTime(activityDetails.getProjectId(), globalDate, "0", null,
                                    null, etWorkHours.getText().toString().trim(),
                                    taskIdList.isEmpty() ? "" : CommonMethods.getCommaSeparatedString(
                                            taskIdList), String.valueOf(UnfinishedActId),
                                    activityDetails.getActivityid(), isOverTimeBreak);
                        } else {
                            activityDetails.setTimeInMinutes(etWorkHours.getText().toString().trim());
                            activityDetails.setTimestring(activityDetails.getTimeInMinutes() + "Hours");
                            activityDetails.setSelectedTaskids(CommonMethods.getCommaSeparatedString(taskIdList));
                            activityDetails.setTotalTimeInMinutes((Integer.parseInt(activityDetails.getTimeInMinutes()) * 60));
                            if (offlineCallback != null) {
                                offlineCallback.onEditSuccess(activityDetails);
                            }
                            dismissWithHideKeyboard();
                        }

                    } else {
                        etWorkHours.setError("required field");
                    }
                } else {
                    if (etWorkStartTime.getText().toString().trim().isEmpty()) {
                        etWorkStartTime.setError("required field");
                    } else if (etWorkEndTime.getText().toString().trim().isEmpty()) {
                        etWorkEndTime.setError("required field");
                    } else if (!CommonMethods.isStartBeforeEndTime(
                            etWorkStartTime.getText().toString().trim(),
                            etWorkEndTime.getText().toString().trim())
                    ) {
                        errorMessage(getString(R.string.earlier_start_time), null, false);
                    } else {
                        if (isInternetAvailable()) {
                            viewModel.apiAddWorkTime(activityDetails.getProjectId(), globalDate, "1",
                                    etWorkStartTime.getText().toString().trim(),
                                    etWorkEndTime.getText().toString().trim(), null,
                                    taskIdList.isEmpty() ? "" : CommonMethods.getCommaSeparatedString(taskIdList),
                                    String.valueOf(UnfinishedActId), activityDetails.getActivityid(), isOverTimeBreak);
                        } else {
                            activityDetails.setStartTime(etWorkStartTime.getText().toString().trim());
                            activityDetails.setEndTime(etWorkEndTime.getText().toString().trim());
                            activityDetails.setTimestring((activityDetails.getStartTime() + "  to  " + activityDetails.getEndTime()));
                            activityDetails.setSelectedTaskids(CommonMethods.getCommaSeparatedString(taskIdList));
                            activityDetails.setTotalTimeInMinutes(CommonMethods.getTimeInMinutes(activityDetails.getStartTime(), activityDetails.getEndTime()));

                            if (offlineCallback != null) {
                                offlineCallback.onEditSuccess(activityDetails);
                            }
                            dismissWithHideKeyboard();
                        }
                    }
                }
                break;
            case R.id.btnBreakSave:
                if (checkForSetting(
                        activityDetails.getSettingTrue()) && etBreakStartTime.getText().toString().isEmpty()) {
                    errorMessage("please enter start time", null, false);
                } else if (checkForSetting(
                        activityDetails.getSettingTrue()) && etBreakEndTime.getText().toString().trim().isEmpty()) {
                    errorMessage("please enter end time", null, false);
                } else if (!checkForSetting(
                        activityDetails.getSettingTrue()) && etBreakHours.getText().toString().trim().isEmpty()) {
                    errorMessage("please enter break minutes", null, false);

                } else if (checkForSetting(
                        activityDetails.getSettingTrue()) && !CommonMethods.isStartBeforeEndTime(
                        etBreakStartTime.getText().toString().trim(),
                        etBreakEndTime.getText().toString().trim())) {
                    errorMessage(getString(R.string.earlier_start_time), null, false);
                } else {
                    if (isInternetAvailable()) {
                        viewModel.addBreakTime(activityDetails.getActivityid(),
                                activityDetails.getProjectId(), activityDetails.getProjectTitle(),
                                globalDate, DataNames.ADD_ACTION_BREAKS,
                                checkForSetting(activityDetails.getSettingTrue()) ? 1 : 0,
                                checkForSetting(
                                        activityDetails.getSettingTrue()) ? null : etBreakHours.getText().toString().trim(),
                                checkForSetting(
                                        activityDetails.getSettingTrue()) ? etBreakStartTime.getText().toString().trim() : null,
                                checkForSetting(
                                        activityDetails.getSettingTrue()) ? etBreakEndTime.getText().toString().trim() : null,
                                null, null);
                    } else {
                        /* handle for both break and start end time */
                        activityDetails.setStartTime(etBreakStartTime.getText().toString().trim());
                        activityDetails.setEndTime(etBreakEndTime.getText().toString().trim());
                        activityDetails.setTimeInMinutes(etBreakHours.getText().toString().trim());
                        activityDetails.setTimestring(!checkForSetting(activityDetails.getSettingTrue()) ? activityDetails.getTimeInMinutes() + "Minutes" :
                                (activityDetails.getStartTime() + "  to  " + activityDetails.getEndTime()));
                        activityDetails.setTotalTimeInMinutes(-1 * (!checkForSetting(activityDetails.getSettingTrue()) ? Integer.parseInt(activityDetails.getTimeInMinutes()) :
                                CommonMethods.getTimeInMinutes(activityDetails.getStartTime(), activityDetails.getEndTime())));
                        /* Others activityDetailss */
                        if (offlineCallback != null) {
                            offlineCallback.onEditSuccess(activityDetails);
                        }
                        dismissWithHideKeyboard();
                    }
                }
                break;
            case R.id.btnPriceSave:
                if (etPriceWorkLocation.getText().toString().trim().isEmpty()) {
                    errorMessage("please enter location", null, false);
                } else if (etPriceWorkQuantity.getText().toString().trim().isEmpty()) {
                    errorMessage("please enter quantity", null, false);
                } else {
                    if (isInternetAvailable()) {
                        viewModel.addPriceWork(activityDetails.getActivityid(),
                                activityDetails.getProjectId(),
                                globalDate, selectedPriceId, selectedItemPrice,
                                etPriceWorkLocation.getText().toString().trim(),
                                etPriceWorkQuantity.getText().toString().trim());
                    } else {

                        activityDetails.setTotalhrswithrate("");
                        /* Other params */
                        activityDetails.setItem(selectedPriceId);
                        activityDetails.pricework_item_cost = selectedItemPrice;
                        activityDetails.setQuantity(etPriceWorkQuantity.getText().toString().trim());
                        activityDetails.setLocation(etPriceWorkLocation.getText().toString().trim());
                        float price = Float.parseFloat(activityDetails.getQuantity()) * Float.parseFloat(selectedItemPrice);
                        activityDetails.setTimestring(activityDetails.getQuantity() + " no. " + tvPriceWorkItem.getText().toString().trim()
                                + "  @" + selectedItemPrice + " = £" + price);

                        if (offlineCallback != null) {
                            offlineCallback.onEditSuccess(activityDetails);
                        }
                        dismissWithHideKeyboard();
                    }
                }
                break;
            case R.id.btnTravelSave:
                if (etTravelStartTime.getText().toString().trim().isEmpty()) {
                    errorMessage("please enter start time", null, false);
                } else if (etTravelEndTime.getText().toString().trim().isEmpty()) {
                    errorMessage("please enter end time", null, false);
                } else if (!CommonMethods.isStartBeforeEndTime(
                        etTravelStartTime.getText().toString().trim(),
                        etTravelEndTime.getText().toString().trim())
                ) {
                    errorMessage(getString(R.string.earlier_start_time), null, false);
                } else {
                    if (isInternetAvailable()) {
                        viewModel.addTravelTime(activityDetails.getActivityid(), activityDetails.getProjectId(),
                                activityDetails.getProjectTitle(), globalDate, travelFromProject, travelToProject,
                                etTravelStartTime.getText().toString().trim(),
                                etTravelEndTime.getText().toString().trim());
                    } else {
                        activityDetails.setStartTime(etTravelStartTime.getText().toString().trim());
                        activityDetails.setEndTime(etTravelEndTime.getText().toString().trim());
                        activityDetails.setFromLocation(etTravelFrom.getText().toString().trim());
                        activityDetails.setToLocation(etTravelTo.getText().toString().trim());
                        activityDetails.setToLocationId(travelToProject);
                        activityDetails.setFromLocationId(travelFromProject);
                        activityDetails.setTimestring(activityDetails.getStartTime() + " from " + activityDetails.getFromLocation() + " to " + activityDetails.getEndTime() + "  " + activityDetails.getToLocation());
                        activityDetails.setTotalTimeInMinutes(CommonMethods.getTimeInMinutes(activityDetails.getStartTime(), activityDetails.getEndTime()));

                        if (offlineCallback != null) {
                            offlineCallback.onEditSuccess(activityDetails);
                        }
                        dismissWithHideKeyboard();
                    }
                }
                break;
            case R.id.btnExpenseSave:
                if (etExpenseItem.getText().toString().trim().isEmpty()) {
                    errorMessage("item must not be empty", null, false);
                } else if (etExpenseCost.getText().toString().trim().isEmpty()) {
                    errorMessage("cost must not be empty", null, false);
                } else {
                    if (isInternetAvailable()) {
                        viewModel.addExpense(activityDetails.getActivityid(),
                                activityDetails.getProjectId(),
                                globalDate, etExpenseItem.getText().toString().trim(),
                                etExpenseCost.getText().toString().trim());
                    } else {
                        activityDetails.setExpensePrice(etExpenseCost.getText().toString().trim());
                        activityDetails.setTotalhrswithrate("£" + activityDetails.getExpensePrice());
                        activityDetails.setTotalTimeInMinutes(0);
                        activityDetails.date = globalDate;
                        activityDetails.setItem(etExpenseItem.getText().toString().trim());
                        activityDetails.setTimestring(etExpenseItem.getText().toString().trim());
                        if (offlineCallback != null) {
                            offlineCallback.onEditSuccess(activityDetails);
                        }
                        dismissWithHideKeyboard();
                    }
                }
                break;
            case R.id.btnNoteSave:
                if (etNote.getText().toString().trim().isEmpty()) {
                    errorMessage("Please enter note description", null, false);
                } else {
                    if (isInternetAvailable()) {
                        viewModel.addNotes(null, noteDetails.getId(), globalDate,
                                etNote.getText().toString().trim());
                    } else {
                        if (mEditSuccessCallback != null) {
                            mEditSuccessCallback.successCallback(etNote.getText().toString().trim());
                            dismissWithHideKeyboard();
                        }
                    }
                }
                break;

        }
    }

    private void observeViewModel() {
        viewModel.isRefreshing.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean != null) {
                if (aBoolean) showProgress();
                else dismissProgress();
            }
        });

        viewModel.tasksLiveList.observe(getViewLifecycleOwner(), returnTaskList -> {
            if (returnTaskList != null) {
                taskList.clear();
                taskList.addAll(returnTaskList);
                setUpTaskId();
            }
        });

        viewModel.successfulLiveData.observe(getViewLifecycleOwner(), s -> {
            if (s != null) {
                errorMessage(s, null, false);
                mEditSuccessCallback.successCallback(s);
                dismissWithHideKeyboard();
            }
        });

        viewModel.successfulLiveData2.observe(getViewLifecycleOwner(), message -> {
            if (null != message) {
                errorMessage(message, null, false);
                mEditSuccessCallback.successCallback(message);
                dismissWithHideKeyboard();
            }
        });


        viewModel.projectsList.observe(getViewLifecycleOwner(), projects -> {
            if (projects != null) {
                projectsList.clear();
                projectsList.addAll(projects);

                if (null != activityDetails) {
                    travelFromProject = getTravelId(activityDetails.getFromLocation());
                    travelToProject = getTravelId(activityDetails.getToLocation());
                }
            }
        });

        viewModel.priceWorkLiveList.observe(getViewLifecycleOwner(), priceWorkListings -> {
            if (priceWorkListings != null) {
                priceWorkList.clear();
                priceWorkList.addAll(priceWorkListings);
                setUpPriceItem();
            }
        });

        viewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (errorModel != null && getActivity() != null) {
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);
            }
        });
    }

    private void setUpTaskId() {
        if (null != activityDetails) {
            taskIdList.clear();
            if (activityDetails.getSelectedTaskids().contains(",")) {
                taskIdList.addAll(Arrays.asList(activityDetails.getSelectedTaskids().split(",")));
            } else {
                taskIdList.add(activityDetails.getSelectedTaskids());
            }

            /* set Up All Tasks */
            setTaskText();
        }
    }

    private void setTaskText() {
        tvWorkTask.setText(null);
        if (!taskIdList.isEmpty()) {
            for (String taskId : taskIdList) {
                for (TaskListing task : taskList) {
                    if (taskId.equalsIgnoreCase(task.getId())) {
                        task.setSelected(true);
                        if (taskIdList.size() == 1) {
                            tvWorkTask.setText(task.getTaskTitle());
                        } else {
                            tvWorkTask.append(
                                    tvWorkTask.getText().toString().trim().isEmpty() ? task.getTaskTitle() : "  , " + task.getTaskTitle());
                        }
                        break;
                    }
                }
            }
        }
    }

    private void setUpPriceItem() {
        if (null != activityDetails) {
            for (PojoPriceWorkModel.PriceWorkListing item : priceWorkList) {
                if (item.getId().equalsIgnoreCase(activityDetails.getItem())) {
                    tvPriceWorkItem.setText(item.getPriceWorkTitle());
                    tvPriceItem.setText(String.format("%s @ £%s", item.getPriceWorkTitle(),
                            item.getPrice()));
                    selectedItemPrice = item.getPrice();
                }
            }
        }

    }

    private String getTravelId(String fromLocation) {
        if (projectsList != null && !projectsList.isEmpty()) {
            List<ProjectDetails> projects = new ArrayList<>();
            projects.addAll(projectsList);
            projects.add(0, new ProjectDetails("-1", "Home"));
            for (ProjectDetails project : projects) {
                if (fromLocation.equalsIgnoreCase(project.getTitle())) {
                    return project.getUid();
                }
            }
        }
        return "";
    }

    @Override
    protected void init() {
        setupView();
    }

    private void setupView() {
        if (null != activityDetails) {
            switch (activityDetails.getDatatype()) {
                case "Worktime":
                    setUpVisibilities(viewWorkTime, viewBreaks, viewTravel, viewExpense,
                            viewPriceWork, viewHoliday, viewSick, viewNote);
                    break;
                case "Breaks":
                    setUpVisibilities(viewBreaks, viewWorkTime, viewTravel, viewExpense,
                            viewPriceWork, viewHoliday, viewSick, viewNote);
                    break;
                case "Travel":
                    setUpVisibilities(viewTravel, viewWorkTime, viewBreaks, viewExpense,
                            viewPriceWork, viewHoliday, viewSick, viewNote);
                    break;
                case "Expenses":
                    setUpVisibilities(viewExpense, viewWorkTime, viewBreaks, viewTravel,
                            viewPriceWork, viewHoliday, viewSick, viewNote);
                    break;
                case "Pricework":
                    setUpVisibilities(viewPriceWork, viewWorkTime, viewBreaks, viewTravel,
                            viewExpense, viewHoliday, viewSick, viewNote);
                    break;
                case "Holiday":
                    setUpVisibilities(viewHoliday, viewWorkTime, viewBreaks, viewTravel,
                            viewExpense, viewPriceWork, viewSick, viewNote);
                    break;
                case "Sick":
                    setUpVisibilities(viewSick, viewWorkTime, viewBreaks, viewTravel, viewExpense,
                            viewPriceWork, viewHoliday, viewNote);
                    break;
            }
        } else if (null != noteDetails) {
            setUpVisibilities(viewNote, viewSick, viewWorkTime, viewBreaks, viewTravel, viewExpense,
                    viewPriceWork, viewHoliday);
            etNote.setText(noteDetails.getNoteDescription());
        }
    }

    private void setUpVisibilities(View shown, View hidden1, View hidden2, View hidden3,
                                   View hidden4, View hidden5, View hidden6, View hidden7) {
        shown.setVisibility(View.VISIBLE);
        hidden1.setVisibility(View.GONE);
        hidden2.setVisibility(View.GONE);
        hidden3.setVisibility(View.GONE);
        hidden4.setVisibility(View.GONE);
        hidden5.setVisibility(View.GONE);
        hidden6.setVisibility(View.GONE);
        hidden7.setVisibility(View.GONE);

        /* hours View*/
        switch (shown.getId()) {
            case R.id.llOverTime:
                etWorkStartTime.setVisibility(checkForSetting(
                        activityDetails.getSettingTrue()) ? View.GONE : View.VISIBLE);
                etWorkEndTime.setVisibility(checkForSetting(
                        activityDetails.getSettingTrue()) ? View.GONE : View.VISIBLE);
                etWorkHours.setVisibility(checkForSetting(
                        activityDetails.getSettingTrue()) ? View.VISIBLE : View.GONE);

                if (!checkForSetting(activityDetails.getSettingTrue())) {
                    String[] splitWorkTime = activityDetails.getTimestring().split("to");
                    etWorkStartTime.setText(splitWorkTime[0].trim());
                    etWorkEndTime.setText(splitWorkTime[1].trim());
                } else {
                    if (activityDetails.getTimestring().contains("Hours")) {
                        etWorkHours.setText(activityDetails.getTimestring().replace("Hours", "").trim());
                    } else if (activityDetails.getTimestring().contains("Hour")) {
                        etWorkHours.setText(activityDetails.getTimestring().replace("Hour", "").trim());
                    }


                }
                viewModel.getTimeSheetTasks(activityDetails.getProjectId());

                break;
            case R.id.llBreaks:
                etBreakStartTime.setVisibility(checkForSetting(
                        activityDetails.getSettingTrue()) ? View.VISIBLE : View.GONE);
                etBreakEndTime.setVisibility(checkForSetting(
                        activityDetails.getSettingTrue()) ? View.VISIBLE : View.GONE);
                etBreakHours.setVisibility(checkForSetting(
                        activityDetails.getSettingTrue()) ? View.GONE : View.VISIBLE);

                if (checkForSetting(activityDetails.getSettingTrue())) {
                    String[] splitWorkTime = activityDetails.getTimestring().split("to");
                    etBreakStartTime.setText(splitWorkTime[0].trim());
                    etBreakEndTime.setText(splitWorkTime[1].trim());
                } else {
                    etBreakHours.setText(
                            activityDetails.getTimestring().toLowerCase().replace("minutes", "").trim());
                }

                break;
            case R.id.llTravel:
                viewModel.getUserProjects();
                etTravelStartTime.setText(activityDetails.getStartTime());
                etTravelEndTime.setText(activityDetails.getEndTime());
                etTravelTo.setText(activityDetails.getToLocation());
                etTravelFrom.setText(activityDetails.getFromLocation());
                break;
            case R.id.llExpenses:
                etExpenseItem.setText(activityDetails.getTimestring());
                String cost = activityDetails.getTotalhrswithrate().replace("£", "");
                etExpenseCost.setText(cost);
                break;
            case R.id.llPriceWork:
                viewModel.getPriceWorkList(activityDetails.getProjectId());
                selectedPriceId = activityDetails.getItem();

                tvPriceWorkItem.setText(activityDetails.getItem()); /* yet to be find from list*/
                etPriceWorkLocation.setText(activityDetails.getLocation());
                etPriceWorkQuantity.setText(activityDetails.getQuantity());

                break;
            case R.id.llHoliday:
                etHolidayStartTime.setVisibility(checkForSetting(
                        activityDetails.getSettingTrue()) ? View.VISIBLE : View.GONE);
                etHolidayEndTime.setVisibility(checkForSetting(
                        activityDetails.getSettingTrue()) ? View.VISIBLE : View.GONE);
                etHolidayHours.setVisibility(checkForSetting(
                        activityDetails.getSettingTrue()) ? View.GONE : View.VISIBLE);
                break;
            case R.id.llSickness:
                etSickStartTime.setVisibility(checkForSetting(
                        activityDetails.getSettingTrue()) ? View.VISIBLE : View.GONE);
                etSickEndTime.setVisibility(checkForSetting(
                        activityDetails.getSettingTrue()) ? View.VISIBLE : View.GONE);
                etSickHours.setVisibility(checkForSetting(
                        activityDetails.getSettingTrue()) ? View.GONE : View.VISIBLE);
                break;
        }
    }

    private boolean checkForSetting(String settingTrue) {
        return !settingTrue.isEmpty() /*&& settingTrue.equalsIgnoreCase("true")*/;
    }

    private void showLocationDropdown(boolean toLocation) {
        if (!projectsList.isEmpty()) {
            if (null != getActivity()) {
                List<ProjectDetails> projects = new ArrayList<>();
                projects.addAll(projectsList);
                projects.add(0, new ProjectDetails("-1", "Home"));
                ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
                listPopupWindow.setAdapter(
                        new ArrayAdapter<>(getActivity(), R.layout.row_dropdown, R.id.tvDropDown,
                                projects));
                listPopupWindow.setAnchorView(toLocation ? etTravelTo : etTravelFrom);
                listPopupWindow.setModal(true);
                listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                    if (toLocation) {
                        travelToProject = projects.get(position).getUid();
                    } else {
                        travelFromProject = projects.get(position).getUid();
                    }
                    (toLocation ? etTravelTo : etTravelFrom).setText(
                            projects.get(position).getTitle());
                    listPopupWindow.dismiss();
                });
                listPopupWindow.show();
            }
        } else {
            errorMessage("No Projects Found", null, false);
        }
    }

    private void showPriceWorkDropdown() {
        if (!priceWorkList.isEmpty()) {
            if (null != getActivity()) {
                ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
                listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.row_dropdown,
                        R.id.tvDropDown, priceWorkList));
                listPopupWindow.setAnchorView(tvPriceWorkItem);
                listPopupWindow.setModal(true);
                listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                    tvPriceWorkItem.setText(priceWorkList.get(position).getPriceWorkTitle());
                    tvPriceItem.setText(String.format("%s @ £%s",
                            priceWorkList.get(position).getPriceWorkTitle(),
                            priceWorkList.get(position).getPrice()));
                    selectedPriceId = priceWorkList.get(position).getId();
                    selectedItemPrice = priceWorkList.get(position).getPrice();
                    tvPriceItem.setVisibility(View.VISIBLE);
                    listPopupWindow.dismiss();
                });
                listPopupWindow.show();
            }
        } else {
            errorMessage("No Item Found", null, false);
        }
    }

    public void setEditSuccessCallback(EditSuccessCallback mEditSuccessCallback) {
        this.mEditSuccessCallback = mEditSuccessCallback;
    }

    public void setOfflineCallback(OfflineEditTSCallback offlineCallback) {
        this.offlineCallback = offlineCallback;
    }
}
