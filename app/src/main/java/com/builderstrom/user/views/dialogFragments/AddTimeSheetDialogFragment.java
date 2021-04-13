package com.builderstrom.user.views.dialogFragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.modals.DashBoardMenuModel;
import com.builderstrom.user.repository.retrofit.modals.PojoPriceWorkModel;
import com.builderstrom.user.repository.retrofit.modals.ProjectDetails;
import com.builderstrom.user.repository.retrofit.modals.StandardBreaks;
import com.builderstrom.user.repository.retrofit.modals.TaskListing;
import com.builderstrom.user.repository.retrofit.modals.TimesheetMetaData;
import com.builderstrom.user.repository.retrofit.modals.TimesheetOverview;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.TimeSheetViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.TimeListAdapter;
import com.builderstrom.user.views.adapters.TimeSheetMenuAdapter;
import com.builderstrom.user.views.adapters.TimeSheetNotesListAdapter;
import com.builderstrom.user.views.adapters.TimeSheetOverViewListAdapter;
import com.builderstrom.user.views.adapters.TimesheetOtherListAdapter;
import com.builderstrom.user.views.viewInterfaces.AddTimeInterface;
import com.builderstrom.user.views.viewInterfaces.EditSuccessCallback;
import com.builderstrom.user.views.viewInterfaces.TaskReturnInterface;
import com.builderstrom.user.views.viewInterfaces.UpdateTimeSheetCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddTimeSheetDialogFragment extends BaseDialogFragment implements EditSuccessCallback {

    private boolean updateTimeSheet = false;
    private String globalDate = "" /* DF_2 format(yyyy-MM-dd)*/,
            selectedProjectId = "", travelFromProject = "", travelToProject = "",
            selectedPriceId = "", selectedItemPrice = "", selectedTaskId = "";

    /* adapters */
    private TimeSheetMenuAdapter mAdapter;
    private TimesheetOtherListAdapter otherListAdapter;
    private TimeSheetOverViewListAdapter overViewListAdapter;
    private TimeListAdapter mBreaksAdapter;
    private TimeSheetNotesListAdapter notesAdapter;
    private TimesheetOverview.PojoOverViewData mTimeSheetOverview;

    /* lists */
    private List<StandardBreaks> breaksList = new ArrayList<>();
    private List<DashBoardMenuModel> gridList = new ArrayList<>();
    private List<ProjectDetails> projectsList = new ArrayList<>();
    private List<TaskListing> taskList = new ArrayList<>();
    private List<PojoPriceWorkModel.PriceWorkListing> priceWorkList = new ArrayList<>();
    private List<String> taskIdList = new ArrayList<>();
    private List<TimesheetMetaData.MetaSetup> sheetMetaDataList = new ArrayList<>();

    /* view model */
    private TimeSheetViewModel viewModel;

    /* views */
    @BindView(R.id.btnShowNote)
    AppCompatButton btnShowNote;
    @BindView(R.id.rvGrid)
    RecyclerView rvGrid;
    @BindView(R.id.rvOther)
    RecyclerView rvOther;
    @BindView(R.id.rvOverView)
    RecyclerView rvOverView;
    @BindView(R.id.rvStandardBreaks)
    RecyclerView rvStandardBreaks;
    @BindView(R.id.rvNotes)
    RecyclerView rvNotes;


    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDayDate)
    TextView tvDayDate;
    @BindView(R.id.tvProjects)
    TextView tvProjects;
    @BindView(R.id.tvWorkTask)
    TextView tvWorkTask;
    @BindView(R.id.tvPriceWorkItem)
    TextView tvPriceWorkItem;
    @BindView(R.id.tvNotSpecified)
    TextView tvNotSpecified;
    @BindView(R.id.tvPriceItem)
    TextView tvPriceItem;


    @BindView(R.id.etProjectTitle)
    EditText etProjectTitle;
    @BindView(R.id.etWorkStartTime)
    EditText etWorkStartTime;
    @BindView(R.id.etWorkEndTime)
    EditText etWorkEndTime;
    @BindView(R.id.etWorkHours)
    EditText etWorkHours;

    @BindView(R.id.etTravelStartTime)
    EditText etTravelStartTime;
    @BindView(R.id.etTravelFrom)
    EditText etTravelFrom;
    @BindView(R.id.etTravelEndTime)
    EditText etTravelEndTime;
    @BindView(R.id.etTravelTo)
    EditText etTravelTo;


    @BindView(R.id.etBreakStartTime)
    EditText etBreakStartTime;
    @BindView(R.id.etBreakEndTime)
    EditText etBreakEndTime;
    @BindView(R.id.etBreakTime)
    EditText etBreakTime;

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
    @BindView(R.id.etHolidayDuration)
    EditText etHolidayDuration;


    @BindView(R.id.etSickStartTime)
    EditText etSickStartTime;
    @BindView(R.id.etSickEndTime)
    EditText etSickEndTime;
    @BindView(R.id.etSickHours)
    EditText etSickHours;
    @BindView(R.id.etSickDuration)
    EditText etSickDuration;

    @BindView(R.id.etNote)
    EditText etNote;

    @BindView(R.id.llOverTime)
    LinearLayout llOverTime;
    @BindView(R.id.llTravel)
    LinearLayout llTravel;
    @BindView(R.id.llBreaksSplit)
    LinearLayout llBreakSplit;
    @BindView(R.id.llBreaks)
    LinearLayout llBreak;
    @BindView(R.id.llExpenses)
    LinearLayout llExpense;
    @BindView(R.id.llPriceWork)
    LinearLayout llPriceWork;
    @BindView(R.id.llHoliday)
    LinearLayout llHoliday;
    @BindView(R.id.llSickness)
    LinearLayout llSick;
    @BindView(R.id.llEditNote)
    LinearLayout llEditNote;
    @BindView(R.id.llNote)
    LinearLayout llNote;
    @BindView(R.id.llOther)
    LinearLayout llOther;
    @BindView(R.id.rlStandardBreaks)
    RelativeLayout rlStandardBreaks;

    private UpdateTimeSheetCallback mCallback;

    public static AddTimeSheetDialogFragment newInstance(String date) {
        AddTimeSheetDialogFragment dialogFragment = new AddTimeSheetDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("date", date);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    protected boolean isFullScreenDialog() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_add_time_sheet;
    }

    @Override
    protected void bindViews(View view) {
        viewModel = new ViewModelProvider(this).get(TimeSheetViewModel.class);
        observeViewModel();
        /* setAdapters */
        mBreaksAdapter = new TimeListAdapter(getActivity(), breaksList, true);
    }

    @OnClick({R.id.ivClose, R.id.ivForward, R.id.ivBackward, R.id.etTravelStartTime,
            R.id.etTravelEndTime, R.id.etBreakStartTime, R.id.etBreakEndTime,
            R.id.etWorkStartTime, R.id.etWorkEndTime, R.id.etHolidayStartTime, R.id.etHolidayEndTime,
            R.id.etSickStartTime, R.id.etSickEndTime, R.id.tvProjects, R.id.tvWorkTask,
            R.id.tvPriceWorkItem, R.id.etTravelTo, R.id.etTravelFrom, R.id.ivAddBreak,
            R.id.btnShowNote, R.id.btnOvertimeSave, R.id.btnTravelSave, R.id.btnBreakSave,
            R.id.btnBreakSaveMore, R.id.btnExpenseSave, R.id.btnPriceSave, R.id.btnHolidaySave,
            R.id.btnHolidayCancel, R.id.btnSickSave, R.id.btnSickCancel,
            R.id.btnNoteSave, R.id.btnOtherSave})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                dismissWithHideKeyboard();
                break;
            case R.id.ivForward:
                setUpDate(CommonMethods.daySwitch(globalDate, true, CommonMethods.DF_2,
                        CommonMethods.DF_2));
                break;
            case R.id.ivBackward:
                setUpDate(CommonMethods.daySwitch(globalDate, false, CommonMethods.DF_2,
                        CommonMethods.DF_2));
                break;
            case R.id.etTravelStartTime:
                openTimePicker(etTravelStartTime);
                break;
            case R.id.etTravelEndTime:
                openTimePicker(etTravelEndTime);
                break;
            case R.id.etBreakStartTime:
                openTimePicker(etBreakStartTime);
                break;
            case R.id.etBreakEndTime:
                openTimePicker(etBreakEndTime);
                break;
            case R.id.etWorkStartTime:
                openTimePicker(etWorkStartTime);
                break;
            case R.id.etWorkEndTime:
                openTimePicker(etWorkEndTime);
                break;
            case R.id.etHolidayStartTime:
                openTimePicker(etHolidayStartTime);
                break;
            case R.id.etHolidayEndTime:
                openTimePicker(etHolidayEndTime);
                break;
            case R.id.etSickStartTime:
                openTimePicker(etSickStartTime);
                break;
            case R.id.etSickEndTime:
                openTimePicker(etSickEndTime);
                break;
            case R.id.tvProjects:
                showProjectsDropdown();
                break;
            case R.id.tvWorkTask:
                if (!taskList.isEmpty()) {
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
            case R.id.tvPriceWorkItem:
                showPriceWorkDropdown();
                break;
            case R.id.etTravelTo:
                showLocationDropdown(true);
                break;
            case R.id.etTravelFrom:
                showLocationDropdown(false);
                break;
            case R.id.ivAddBreak:
                AddTimeDialogFragment timeDialogFragment = new AddTimeDialogFragment();
                timeDialogFragment.setCallback((startTime, endTime) -> {
                    breaksList.add(new StandardBreaks(startTime, endTime));
                    mBreaksAdapter.notifyDataSetChanged();
                });
                timeDialogFragment.show(getParentFragmentManager(), "time dialog");
                break;
            case R.id.btnShowNote:
                btnShowNote.setVisibility(View.GONE);
                llEditNote.setVisibility(View.VISIBLE);
                break;
            case R.id.btnOvertimeSave:
                if (mTimeSheetOverview.getOverviewHtml().getHourlyTime()) {
                    etWorkHours.setError(null);
                    if (!etWorkHours.getText().toString().trim().isEmpty()) {
                        viewModel.apiAddWorkTime(selectedProjectId, globalDate, "0", null, null,
                                etWorkHours.getText().toString().trim(),
                                taskIdList.isEmpty() ? "" : CommonMethods.getCommaSeparatedString(
                                        taskIdList),
                                mTimeSheetOverview.getOverviewHtml().getDeleteunfinishedactid().toString(), "0"
                                , mTimeSheetOverview.getOverviewHtml().getBreakAutomaticalyWorktime());
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
                        viewModel.apiAddWorkTime(selectedProjectId, globalDate, "1",
                                etWorkStartTime.getText().toString().trim(),
                                etWorkEndTime.getText().toString().trim(), null
                                , taskIdList.isEmpty() ? "" : CommonMethods.getCommaSeparatedString(taskIdList),
                                mTimeSheetOverview.getOverviewHtml().getDeleteunfinishedactid().toString(), "0"
                                , mTimeSheetOverview.getOverviewHtml().getBreakAutomaticalyWorktime());
                    }
                }
                break;
            case R.id.btnTravelSave:
                if (checkTravelCondition()) {
                    viewModel.addTravelTime("0", selectedProjectId,
                            etProjectTitle.getText().toString().trim(), globalDate, travelFromProject,
                            travelToProject, etTravelStartTime.getText().toString().trim(),
                            etTravelEndTime.getText().toString().trim());
                }
                break;
            case R.id.btnBreakSave:
                if (mTimeSheetOverview.getOverviewHtml().getAllowStandardBreaks() != null &&
                        mTimeSheetOverview.getOverviewHtml().getAllowStandardBreaks() &&
                        mTimeSheetOverview.getProjectStandardBreaks() != null &&
                        !mTimeSheetOverview.getProjectStandardBreaks().isEmpty()) {
                    if (mBreaksAdapter.getSelectedList().isEmpty() && CommonMethods.isEmptyList(
                            mBreaksAdapter.getBreakTimeList())) {
                        errorMessage("Please select at least one standard break", null, false);
                    } else {
                        viewModel.addBreakTime("0", selectedProjectId,
                                etProjectTitle.getText().toString().trim(), globalDate,
                                DataNames.ADD_ACTION_STANDARD_BREAKS, null,
                                null, null, null,
                                mBreaksAdapter.getSelectedList(), mBreaksAdapter.getBreakTimeList());
                    }
                } else {
                    if (null != mTimeSheetOverview.getOverviewHtml().getBreakRequireTimes()
                            && mTimeSheetOverview.getOverviewHtml().getBreakRequireTimes()) {
                        if (etBreakStartTime.getText().toString().trim().isEmpty()) {
                            errorMessage("Please enter break start time", null, false);
                        } else if (etBreakEndTime.getText().toString().trim().isEmpty()) {
                            errorMessage("Please enter break end time", null, false);
                        } else if (!CommonMethods.isStartBeforeEndTime(
                                etBreakStartTime.getText().toString().trim(),
                                etBreakEndTime.getText().toString().trim())) {
                            errorMessage(getString(R.string.earlier_start_time), null, false);
                        } else {
                            viewModel.addBreakTime("0", selectedProjectId,
                                    etProjectTitle.getText().toString().trim(),
                                    globalDate, DataNames.ADD_ACTION_BREAKS,
                                    1, null, etBreakStartTime.getText().toString().trim(),
                                    etBreakEndTime.getText().toString().trim(), null, null);
                        }

                    } else {
                        if (etBreakTime.getText().toString().trim().isEmpty()) {
                            errorMessage("Please enter break time", null, false);
                        } else {
                            viewModel.addBreakTime("0", selectedProjectId,
                                    etProjectTitle.getText().toString().trim(), globalDate,
                                    DataNames.ADD_ACTION_BREAKS, 0,
                                    etBreakTime.getText().toString().trim(), null,
                                    null, null, null);
                        }
                    }
                }
                break;
            case R.id.btnBreakSaveMore:
                errorMessage("Save More Add Time", null, false);
                break;
            case R.id.btnExpenseSave:
                if (checkExpenseCondition()) {
                    viewModel.addExpense("0", selectedProjectId, globalDate,
                            etExpenseItem.getText().toString().trim(),
                            etExpenseCost.getText().toString().trim());
                }
                break;
            case R.id.btnPriceSave:
                if (checkPriceWorkCondition()) {
                    viewModel.addPriceWork("0", selectedProjectId, globalDate, selectedPriceId,
                            selectedItemPrice, etPriceWorkLocation.getText().toString().trim(),
                            etPriceWorkQuantity.getText().toString().trim());
                }
                break;
            case R.id.btnHolidaySave:
                clearAllErrors();
                if (mTimeSheetOverview.getOverviewHtml().getViewType()) {
                    if (etHolidayStartTime.getText().toString().trim().isEmpty()) {
                        etHolidayStartTime.setError("required field");
                    } else if (etHolidayEndTime.getText().toString().trim().isEmpty()) {
                        etHolidayEndTime.setError("required field");
                    } else if (etHolidayDuration.getText().toString().trim().isEmpty()) {
                        etHolidayDuration.setError("required field");
                    } else if (!CommonMethods.isStartBeforeEndTime(
                            etHolidayStartTime.getText().toString().trim(),
                            etHolidayEndTime.getText().toString().trim())
                    ) {
                        errorMessage(getString(R.string.earlier_start_time), null, false);
                    } else {
                        viewModel.addHoliday(globalDate, DataNames.ADD_ACTION_HOLIDAY,
                                null, etHolidayStartTime.getText().toString().trim(),
                                etHolidayEndTime.getText().toString().trim(),
                                etHolidayDuration.getText().toString().trim());
                    }
                } else {
                    if (etHolidayHours.getText().toString().trim().isEmpty()) {
                        etHolidayHours.setError("required field");
                    } else if (etHolidayDuration.getText().toString().trim().isEmpty()) {
                        etHolidayDuration.setError("required field");
                    } else {
                        viewModel.addHoliday(globalDate, DataNames.ADD_ACTION_HOLIDAY,
                                etHolidayHours.getText().toString().trim(), null,
                                null, etHolidayDuration.getText().toString().trim());
                    }
                }
                break;
            case R.id.btnHolidayCancel:
            case R.id.btnSickCancel:
                clearAllFields();
                break;
            case R.id.btnSickSave:
                clearAllErrors();
                if (mTimeSheetOverview.getOverviewHtml().getViewType()) {

                    if (etSickStartTime.getText().toString().trim().isEmpty()) {
                        etSickStartTime.setError("required field");
                    } else if (etSickEndTime.getText().toString().trim().isEmpty()) {
                        etSickEndTime.setError("required field");
                    } else if (etSickDuration.getText().toString().trim().isEmpty()) {
                        etSickDuration.setError("required field");
                    } else if (!CommonMethods.isStartBeforeEndTime(
                            etSickStartTime.getText().toString().trim(),
                            etSickEndTime.getText().toString().trim())
                    ) {
                        errorMessage(getString(R.string.earlier_start_time), null, false);
                    } else {
                        viewModel.addHoliday(globalDate, DataNames.ADD_ACTION_SICKNESS,
                                null, etSickStartTime.getText().toString().trim(),
                                etSickEndTime.getText().toString().trim(),
                                etSickDuration.getText().toString().trim());
                    }
                } else {
                    if (etSickHours.getText().toString().trim().isEmpty()) {
                        etSickHours.setError("required field");
                    } else if (etSickDuration.getText().toString().trim().isEmpty()) {
                        etSickDuration.setError("required field");
                    } else {
                        viewModel.addHoliday(globalDate, DataNames.ADD_ACTION_SICKNESS,
                                etSickHours.getText().toString().trim(), null,
                                null, etSickDuration.getText().toString().trim());
                    }
                }
                break;
            case R.id.btnNoteSave:
                clearAllErrors();
                if (etNote.getText().toString().trim().isEmpty()) {
                    etNote.setError("required field");
                } else {
                    viewModel.addNotes(selectedProjectId, null, globalDate, etNote.getText().toString().trim());
                }
                break;
            case R.id.btnOtherSave:
                if (otherListAdapter.getSelectedMetaData().isEmpty()) {
                    errorMessage("Please select at least one checkbox of metadata", null, false);
                } else {
                    viewModel.addMetaData(selectedProjectId, globalDate, CommonMethods.getCommaSeparatedString(
                            otherListAdapter.getSelectedMetaData()));
                }
                break;

        }
    }

    @Override
    protected void init() {
        /* access all projects */
        viewModel.getUserProjects();
        setMenuAdapter();

        if (null != getArguments()) {
            globalDate = getArguments().getString("date");
            tvDayDate.setText(
                    CommonMethods.convertDate(CommonMethods.DF_2, globalDate, CommonMethods.DF_13));
        }

    }

    private void setTaskText() {
        tvWorkTask.setText(null);
        tvWorkTask.clearComposingText();
        if (!taskIdList.isEmpty()) {
            for (String taskId : taskIdList) {
                for (TaskListing task : taskList) {
                    if (taskId.equalsIgnoreCase(task.getId())) {
                        if (taskIdList.size() == 1) {
                            tvWorkTask.setText(task.getTaskTitle());
                        } else {
                            tvWorkTask.append(tvWorkTask.getText().toString().trim().isEmpty() ?
                                    task.getTaskTitle() : "  , " + task.getTaskTitle());
                        }
                    }
                }
            }
        }
    }

    /* set up  views*/
    private void initializeMenu() {
        if (mTimeSheetOverview != null) {
            tvTitle.setText(mTimeSheetOverview.getTopmsg());
            gridList.clear();
            if (!mTimeSheetOverview.getOverviewHtml().getIsProcessed()) {
                tvProjects.setVisibility(View.VISIBLE);
                for (DashBoardMenuModel model : CommonMethods.getTimeSheetMenu()) {
                    if (model.getAction_index().equals(DataNames.TIME_SHEET_OVERVIEW) ||
                            model.getAction_index().equals(DataNames.TIME_SHEET_HOLIDAY) ||
                            model.getAction_index().equals(DataNames.TIME_SHEET_SICK) ||
                            model.getAction_index().equals(DataNames.TIME_SHEET_NOTES)) {
                        gridList.add(model);
                    }

                    if (!selectedProjectId.isEmpty() && (mTimeSheetOverview.getOverviewHtml().getCheckifAbsent() == null
                            || mTimeSheetOverview.getOverviewHtml().getCheckifAbsent().isEmpty())) {

                        if (model.getAction_index().equals(DataNames.TIME_SHEET_EXPENSES)
                                && mTimeSheetOverview.getOverviewHtml().getAllowExpensesPaid()) {
                            gridList.add(model);
                        }

                        if (model.getAction_index().equals(DataNames.TIME_SHEET_PRICE_WORK)
                                && mTimeSheetOverview.getOverviewHtml().getAllowExpensesPaid()) {
                            gridList.add(model);
                        }
                        if (model.getAction_index().equals(DataNames.TIME_SHEET_WORK_TIME) ||
                                model.getAction_index().equals(DataNames.TIME_SHEET_TRAVEL) ||
                                model.getAction_index().equals(DataNames.TIME_SHEET_BREAKS)) {
                            gridList.add(model);
                        }

                        /* Add Other Section */
                        if (model.getAction_index().equals(
                                DataNames.TIME_SHEET_OTHER) && sheetMetaDataList.size() > 0) {
                            gridList.add(model);
                        }

                    }
                }
//                tvNotSpecified.setText(
//                        (mTimeSheetOverview.getMessage() != null ? mTimeSheetOverview.getMessage() : "No Overview found"));

                if (mTimeSheetOverview.getOverviewDetail() == null || mTimeSheetOverview.getOverviewDetail().isEmpty()) {
                    tvNotSpecified.setText(String.format(getString(R.string.no_time_sheet_date_message),
                            CommonMethods.convertDate(CommonMethods.DF_2, globalDate, CommonMethods.DF_4)));
                } else tvNotSpecified.setText(getString(R.string.no_over_view_found));

                tvNotSpecified.setVisibility(View.VISIBLE);

                /* overview list */
                overViewListAdapter = new TimeSheetOverViewListAdapter(this,
                        null != mTimeSheetOverview.getOverviewDetail() ?
                                mTimeSheetOverview.getOverviewDetail() : new ArrayList<>(),
                        viewModel,mTimeSheetOverview.getOverviewHtml().getBreakAutomaticalyWorktime());
                if (null != getActivity()) {
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
                    rvOverView.addItemDecoration(dividerItemDecoration);
                }
                rvOverView.setAdapter(overViewListAdapter);

                /* user notes*/
                notesAdapter = new TimeSheetNotesListAdapter(this,
                        null != mTimeSheetOverview.getUserNotes() ?
                                mTimeSheetOverview.getUserNotes() : new ArrayList<>(), viewModel);
                rvNotes.setAdapter(notesAdapter);

                tvNotSpecified.setVisibility(
                        null != mTimeSheetOverview.getOverviewDetail() && !mTimeSheetOverview.getOverviewDetail().isEmpty() ? View.GONE : View.VISIBLE);
            } else {
                tvProjects.setVisibility(View.GONE);
                tvNotSpecified.setVisibility(View.VISIBLE);
                tvNotSpecified.setText(getString(R.string.processed_time_sheet));
            }

            mAdapter.setDefaultView();
            setUpVisibilities(rvOverView, llOverTime, llTravel, llBreak, llExpense, llPriceWork,
                    llHoliday, llSick, llNote, llOther);
        }

    }

    private void setMenuAdapter() {
        mAdapter = new TimeSheetMenuAdapter(getActivity(), gridList, action -> {
            clearAllFields();
            hideDialogKeyboard();
            tvNotSpecified.setVisibility(View.GONE);
            switch (action) {
                case DataNames.TIME_SHEET_OVERVIEW:
                    setUpVisibilities(rvOverView, llOverTime, llTravel, llBreak, llExpense,
                            llPriceWork, llHoliday, llSick, llNote, llOther);
                    updateOverview();
                    taskIdList.clear();
                    break;
                case DataNames.TIME_SHEET_WORK_TIME:
                    setUpVisibilities(llOverTime, rvOverView, llTravel, llBreak, llExpense,
                            llPriceWork, llHoliday, llSick, llNote, llOther);
                    setUpWorkTime();
                    break;
                case DataNames.TIME_SHEET_TRAVEL:
                    setUpVisibilities(llTravel, rvOverView, llOverTime, llBreak, llExpense,
                            llPriceWork, llHoliday, llSick, llNote, llOther);
                    setUpProjectTitle(false);
                    break;
                case DataNames.TIME_SHEET_BREAKS:
                    setUpVisibilities(llBreak, llExpense, rvOverView, llOverTime, llTravel,
                            llPriceWork, llHoliday, llSick, llNote, llOther);
                    setUpBreaksView();
                    break;
                case DataNames.TIME_SHEET_EXPENSES:
                    setUpVisibilities(llExpense, llBreak, rvOverView, llOverTime, llTravel,
                            llPriceWork, llHoliday, llSick, llNote, llOther);
                    break;
                case DataNames.TIME_SHEET_PRICE_WORK:
                    viewModel.getPriceWorkList(selectedProjectId);
                    break;
                case DataNames.TIME_SHEET_HOLIDAY:
                    setUpVisibilities(llHoliday, rvOverView, llOverTime, llTravel, llBreak,
                            llExpense, llPriceWork, llSick, llNote, llOther);
                    break;
                case DataNames.TIME_SHEET_SICK:
                    setUpVisibilities(llSick, llNote, llOther, rvOverView, llOverTime, llTravel,
                            llBreak, llExpense, llPriceWork, llHoliday);
                    break;
                case DataNames.TIME_SHEET_NOTES:
                    setUpVisibilities(llNote, rvOverView, llOverTime, llTravel, llBreak, llExpense,
                            llPriceWork, llHoliday, llSick, llOther);
                    break;
                case DataNames.TIME_SHEET_OTHER:
                    setUpVisibilities(llOther, rvOverView, llOverTime, llTravel, llBreak, llExpense,
                            llPriceWork, llHoliday, llSick, llNote);
                    for (TimesheetMetaData.MetaSetup setup : sheetMetaDataList) {
                        setup.setChecked(false);
                    }
                    setMetaAdapter();
                    break;
            }
        });
        rvGrid.setAdapter(mAdapter);
    }

    /*
     * Update Timesheet overview list(For Overview menu option )
     */
    private void updateOverview() {
        viewModel.getOverview(selectedProjectId, globalDate);
    }

    private void setUpWorkTime() {
        setUpProjectTitle(true);
        if (mTimeSheetOverview != null && mTimeSheetOverview.getOverviewHtml() != null) {
            if (mTimeSheetOverview.getOverviewHtml().getAllowTrackTasks() && !selectedProjectId.isEmpty()) {
                viewModel.getTimeSheetTasks(selectedProjectId);
                tvWorkTask.setVisibility(View.VISIBLE);
            } else {
                tvWorkTask.setVisibility(View.GONE);
            }

            if (mTimeSheetOverview.getOverviewHtml().getHourlyTime()) {
                etWorkStartTime.setVisibility(View.GONE);
                etWorkEndTime.setVisibility(View.GONE);
                etWorkHours.setVisibility(View.VISIBLE);
                if (mTimeSheetOverview.getOverviewHtml().getUserPayType()) {
                    etWorkHours.setHint("Total Shifts");
                } else {
                    etWorkHours.setHint("Total Hours");
                }
            } else {
                etWorkStartTime.setVisibility(View.VISIBLE);
                etWorkEndTime.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setUpBreaksView() {
        if (mTimeSheetOverview != null) {
            if (mTimeSheetOverview.getOverviewHtml().getAllowStandardBreaks() != null &&
                    mTimeSheetOverview.getOverviewHtml().getAllowStandardBreaks() &&
                    mTimeSheetOverview.getProjectStandardBreaks() != null &&
                    !mTimeSheetOverview.getProjectStandardBreaks().isEmpty()) {
                breaksList.clear();
                breaksList.addAll(mTimeSheetOverview.getProjectStandardBreaks());
                mBreaksAdapter = new TimeListAdapter(getActivity(), breaksList, true);
                rvStandardBreaks.setAdapter(mBreaksAdapter);
                rlStandardBreaks.setVisibility(View.VISIBLE);
                llBreakSplit.setVisibility(View.GONE);
            } else {
                rlStandardBreaks.setVisibility(View.GONE);
                llBreakSplit.setVisibility(View.VISIBLE);
                if (null != mTimeSheetOverview.getOverviewHtml().getBreakRequireTimes()
                        && mTimeSheetOverview.getOverviewHtml().getBreakRequireTimes()) {
                    etBreakTime.setVisibility(View.GONE);
                    etBreakStartTime.setVisibility(View.VISIBLE);
                    etBreakEndTime.setVisibility(View.VISIBLE);
                } else {
                    etBreakTime.setVisibility(View.VISIBLE);
                    etBreakStartTime.setVisibility(View.GONE);
                    etBreakEndTime.setVisibility(View.GONE);
                }
            }
        }
    }

    private void setMetaAdapter() {
        otherListAdapter = new TimesheetOtherListAdapter(getActivity(), sheetMetaDataList);
        rvOther.setAdapter(otherListAdapter);
    }

    private void setUpProjectTitle(boolean isVisible) {
        if (!selectedProjectId.isEmpty() && selectedProjectId.equals("-1") && isVisible) {
            etProjectTitle.setVisibility(View.VISIBLE);
        } else {
            etProjectTitle.setVisibility(View.GONE);
        }
    }

    private void setUpDate(String selectedDate) {
        globalDate = selectedDate;
        tvDayDate.setText(
                CommonMethods.convertDate(CommonMethods.DF_2, globalDate, CommonMethods.DF_13));
        updateOverview();
    }

    /* check for conditions */
    private boolean checkExpenseCondition() {
        if (etExpenseItem.getText().toString().isEmpty()) {
            errorMessage("Please enter expense item", null, false);
            return false;
        } else if (etExpenseCost.getText().toString().trim().isEmpty()) {
            errorMessage("Please enter item cost", null, false);
            return false;
        }
        return true;

    }

    private boolean checkTravelCondition() {
        if (etTravelStartTime.getText().toString().isEmpty()) {
            errorMessage("Please enter travel start time", null, false);
            return false;
        } else if (etTravelEndTime.getText().toString().trim().isEmpty()) {
            errorMessage("Please enter travel end time", null, false);
            return false;

        } else if (travelFromProject.isEmpty()) {
            errorMessage("Please select travel from location", null, false);
            return false;

        } else if (travelToProject.isEmpty()) {
            errorMessage("Please select travel to location", null, false);
            return false;
        } else if (!CommonMethods.isStartBeforeEndTime(
                etTravelStartTime.getText().toString().trim(),
                etTravelEndTime.getText().toString().trim())
        ) {
            errorMessage(getString(R.string.earlier_start_time), null, false);
            return false;
        }
        return true;

    }

    private boolean checkPriceWorkCondition() {
        etPriceWorkLocation.setError(null);
        etPriceWorkQuantity.setError(null);

        if (selectedPriceId.isEmpty()) {
            errorMessage("Please select an item", null, false);
            return false;
        } else if (etPriceWorkLocation.getText().toString().isEmpty()) {
            etPriceWorkLocation.setError("required field");
            return false;
        } else if (etPriceWorkQuantity.getText().toString().trim().isEmpty()) {
            etPriceWorkQuantity.setError("required field");
            return false;
        }
        return true;
    }

    private void observeViewModel() {

        viewModel.isRefreshing.observe(getViewLifecycleOwner(), aBoolean -> {
            if (null != aBoolean) {
                if (aBoolean) {
                    showProgress();
                } else {
                    dismissProgress();
                }
            }
        });

        viewModel.projectsList.observe(getViewLifecycleOwner(), projects -> {
            if (projects != null) {
                projectsList.clear();
                projectsList.addAll(projects);
            }
        });

        viewModel.overviewLiveData.observe(getViewLifecycleOwner(), timeSheetOverview -> {
            if (timeSheetOverview != null) {
                mTimeSheetOverview = timeSheetOverview;
                initializeMenu();
            }
        });

        viewModel.tasksLiveList.observe(getViewLifecycleOwner(), returnTaskList -> {
            if (returnTaskList != null) {
                taskList.clear();
                taskList.addAll(returnTaskList);
            }
        });

        viewModel.priceWorkLiveList.observe(getViewLifecycleOwner(), priceWorkListings -> {
            if (priceWorkListings != null) {
                priceWorkList.clear();
                priceWorkList.addAll(priceWorkListings);
                setUpVisibilities(llPriceWork, rvOverView, llOverTime, llTravel, llBreak, llExpense,
                        llHoliday, llSick, llNote, llOther);
            }
        });

        viewModel.metadataLiveList.observe(getViewLifecycleOwner(), metaDataList -> {
            if (metaDataList != null) {
                sheetMetaDataList.clear();
                sheetMetaDataList.addAll(metaDataList);
                setMetaAdapter();
                initializeMenu();
            }
        });

        viewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (errorModel != null && getActivity() != null) {
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);

            }
        });

        viewModel.successfulLiveData.observe(getViewLifecycleOwner(), message -> {
            if (null != message) {
                updateTimeSheet = true;
                errorMessage(message, null, false);
                updateOverview();
            }
        });

        viewModel.successfulLiveData2.observe(getViewLifecycleOwner(), message -> {
            if (null != message) {
                updateTimeSheet = true;
                errorMessage(message, null, false);
                clearAllFields();
                hideDialogKeyboard();
                tvNotSpecified.setVisibility(View.GONE);
                setUpVisibilities(llBreak, llExpense, rvOverView, llOverTime, llTravel,
                        llPriceWork, llHoliday, llSick, llNote, llOther);
                setUpBreaksView();
            }
        });

        viewModel.deleteActivityLD.observe(getViewLifecycleOwner(), position -> {
            if (position != null) {
                updateTimeSheet = true;
                overViewListAdapter.removeItem(position);
            }
        });

        viewModel.toastMetaData.observe(getViewLifecycleOwner(), message -> {
            if (null != message) {
                errorMessage(message, null, false);
            }
        });

        viewModel.delNoteLiveData.observe(getViewLifecycleOwner(), position -> {
            if (null != position && null != notesAdapter) {
                notesAdapter.deleteNoteAt(position);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        updateOverview();
    }

    private void clearAllFields() {
        if (isAdded()) {
            etWorkStartTime.setText(null);
            etWorkEndTime.setText(null);
            etWorkHours.setText(null);
            etTravelStartTime.setText(null);
            etTravelFrom.setText(null);
            etTravelEndTime.setText(null);
            etTravelTo.setText(null);
            etBreakTime.setText(null);
            etBreakStartTime.setText(null);
            etBreakEndTime.setText(null);
            etExpenseItem.setText(null);
            etExpenseCost.setText(null);
            tvPriceItem.setText(null);
            etPriceWorkLocation.setText(null);
            etPriceWorkQuantity.setText(null);
            etHolidayHours.setText(null);
            etHolidayStartTime.setText(null);
            etHolidayEndTime.setText(null);
            etHolidayDuration.setText(null);
            etSickHours.setText(null);
            etSickStartTime.setText(null);
            etSickEndTime.setText(null);
            etSickDuration.setText(null);
            etNote.setText(null);
            travelToProject = "";
            travelFromProject = "";
            tvWorkTask.setText(null);
        }
    }

    private void clearAllErrors() {
        if (isAdded()) {
            etWorkStartTime.setError(null);
            etWorkEndTime.setError(null);
            etWorkHours.setError(null);
            etTravelStartTime.setError(null);
            etTravelFrom.setError(null);
            etTravelEndTime.setError(null);
            etTravelTo.setError(null);
            etBreakTime.setError(null);
            etBreakStartTime.setError(null);
            etBreakEndTime.setError(null);
            etExpenseItem.setError(null);
            etExpenseCost.setError(null);
            tvPriceItem.setError(null);
            etPriceWorkLocation.setError(null);
            etPriceWorkQuantity.setError(null);
            etHolidayHours.setError(null);
            etHolidayStartTime.setError(null);
            etHolidayEndTime.setError(null);
            etHolidayDuration.setError(null);
            etSickHours.setError(null);
            etSickStartTime.setError(null);
            etSickEndTime.setError(null);
            etSickDuration.setError(null);
            etNote.setError(null);
        }
    }

    private void setUpVisibilities(View visibleView, View hiddenView1, View hiddenView2,
                                   View hiddenView3, View hiddenView4, View hiddenView5,
                                   View hiddenView6, View hiddenView7, View hiddenView8,
                                   View hiddenView9) {

        setUpProjectTitle(false);
        switch (visibleView.getId()) {
            case R.id.llPriceWork:
                visibleView.setVisibility(priceWorkList.isEmpty() ? View.GONE : View.VISIBLE);
                tvNotSpecified.setVisibility(!priceWorkList.isEmpty() ? View.GONE : View.VISIBLE);
                tvNotSpecified.setText(getString(R.string.no_item_available));
                break;
            case R.id.llHoliday:
                visibleView.setVisibility(View.VISIBLE);
                if (mTimeSheetOverview != null && mTimeSheetOverview.getOverviewHtml() != null
                        && mTimeSheetOverview.getOverviewHtml().getViewType() != null) {
                    boolean viewType = mTimeSheetOverview.getOverviewHtml().getViewType();
                    etHolidayHours.setVisibility(viewType ? View.GONE : View.VISIBLE);
                    etHolidayStartTime.setVisibility(viewType ? View.VISIBLE : View.GONE);
                    etHolidayEndTime.setVisibility(viewType ? View.VISIBLE : View.GONE);
                } else {
                    etHolidayHours.setVisibility(View.GONE);
                }
                break;
            case R.id.llSickness:
                visibleView.setVisibility(View.VISIBLE);
                if (mTimeSheetOverview != null && mTimeSheetOverview.getOverviewHtml() != null
                        && mTimeSheetOverview.getOverviewHtml().getViewType() != null) {
                    boolean viewType = mTimeSheetOverview.getOverviewHtml().getViewType();
                    etSickHours.setVisibility(viewType ? View.GONE : View.VISIBLE);
                    etSickStartTime.setVisibility(viewType ? View.VISIBLE : View.GONE);
                    etSickEndTime.setVisibility(viewType ? View.VISIBLE : View.GONE);
                } else {
                    etHolidayHours.setVisibility(View.GONE);
                }
                break;

            case R.id.llNote:
                visibleView.setVisibility(View.VISIBLE);
                llEditNote.setVisibility(View.GONE);
                btnShowNote.setVisibility(View.VISIBLE);
                break;
            default:
                visibleView.setVisibility(View.VISIBLE);
                break;
        }

        hiddenView1.setVisibility(View.GONE);
        hiddenView2.setVisibility(View.GONE);
        hiddenView3.setVisibility(View.GONE);
        hiddenView4.setVisibility(View.GONE);
        hiddenView5.setVisibility(View.GONE);
        hiddenView6.setVisibility(View.GONE);
        hiddenView7.setVisibility(View.GONE);
        hiddenView8.setVisibility(View.GONE);
        hiddenView9.setVisibility(View.GONE);
        tvPriceItem.setVisibility(View.GONE);


    }

    /* pop ups and drop downs */

    private void showProjectsDropdown() {
        if (!projectsList.isEmpty()) {
            if (null != getActivity()) {
                ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
                listPopupWindow.setAdapter(
                        new ArrayAdapter<>(getActivity(), R.layout.row_dropdown, R.id.tvDropDown,
                                projectsList));
                listPopupWindow.setAnchorView(tvProjects);
                listPopupWindow.setModal(true);
                listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                    tvProjects.setText(projectsList.get(position).getTitle());
                    selectedProjectId = projectsList.get(position).getUid();
                    viewModel.getSheetMetaData(selectedProjectId);
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
                listPopupWindow.setAdapter(
                        new ArrayAdapter<>(getActivity(), R.layout.row_dropdown, R.id.tvDropDown,
                                priceWorkList));
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

    private void showLocationDropdown(boolean toLocation) {
        if (null != getActivity()) {
            if (!projectsList.isEmpty()) {

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

            } else {
                errorMessage("No Projects Found", null, false);
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (null != mCallback && updateTimeSheet) {
            // update previous fragment
            mCallback.onUpdate(globalDate);
        }
        super.onDestroyView();
    }

    public void setCallback(UpdateTimeSheetCallback mCallback) {
        this.mCallback = mCallback;
    }

    public String getGlobalDate() {
        return globalDate;
    }

    public TimesheetOverview.PojoOverViewData getTimeSheetOverview() {
        return mTimeSheetOverview;
    }

    @Override
    public void successCallback(String success) {
        updateTimeSheet = true;
        updateOverview();
    }
}
