package com.builderstrom.user.views.dialogFragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.ListPopupWindow;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.modals.DashBoardMenuModel;
import com.builderstrom.user.data.retrofit.modals.PojoPriceWorkModel;
import com.builderstrom.user.data.retrofit.modals.ProjectDetails;
import com.builderstrom.user.data.retrofit.modals.ReturnOverviewDetail;
import com.builderstrom.user.data.retrofit.modals.ReturnOverviewhtmldetail;
import com.builderstrom.user.data.retrofit.modals.TSNote;
import com.builderstrom.user.data.retrofit.modals.TaskListing;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.TimeSheetViewModel;
import com.builderstrom.user.views.adapters.TimeSheetMenuAdapter;
import com.builderstrom.user.views.adapters.TimeSheetNotesListAdapter;
import com.builderstrom.user.views.adapters.TimeSheetOverViewListAdapter;
import com.builderstrom.user.views.viewInterfaces.TaskReturnInterface;
import com.builderstrom.user.views.viewInterfaces.UpdateTimeSheetCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddTSOfflineDialogFragment extends BaseDialogFragment implements TimeSheetMenuAdapter.Callback {

    @BindView(R.id.btnShowNote)
    View btnShowNote;
    @BindView(R.id.ivForward)
    View ivForward;
    @BindView(R.id.ivBackward)
    View ivBackward;
    @BindView(R.id.rvGrid)
    RecyclerView rvGridMenu;
    @BindView(R.id.rvOverView)
    RecyclerView rvOverView;
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
    @BindView(R.id.llBreaks)
    LinearLayout llBreaks;
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
    @BindView(R.id.rvStandardBreaks)
    RecyclerView rvStandardBreaks;
    @BindView(R.id.llBreaksSplit)
    LinearLayout llBreakSplit;
    private String globalDate = "", selectedProjectId = "", travelToProjectId = "",
            travelFromProjectId = "", selectedPriceId = "", selectedItemPrice = "",
            viewType = "";
    int workHours;
    private boolean isWorkHoursSelected = false;
    private boolean updateTimeSheet = false;
    private UpdateTimeSheetCallback mCallback;
    /* lists */
    private List<ReturnOverviewDetail> overviewDetailsList = new ArrayList<>();
    private List<ProjectDetails> globalProjects = new ArrayList<>();
    private List<DashBoardMenuModel> gridMenuList = new ArrayList<>();
    private List<TaskListing> taskList = new ArrayList<>();
    private List<PojoPriceWorkModel.PriceWorkListing> priceWorkList = new ArrayList<>();
    private List<TSNote> notesList = new ArrayList<>();
    private TimeSheetViewModel viewModel;
    /* adapters */
    private TimeSheetMenuAdapter gridMenuAdapter;
    private TimeSheetOverViewListAdapter mOverViewListAdapter;
    private TimeSheetNotesListAdapter mNotesListAdapter;

    private List<String> taskIdList = new ArrayList<>();
    private ReturnOverviewhtmldetail menuSetting;
    private Integer selectedPosition;

    public static AddTSOfflineDialogFragment newInstance(String date, String viewType, int adapterPosition) {
        AddTSOfflineDialogFragment dialogFragment = new AddTSOfflineDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("date", date);
        bundle.putString("VIEW_TYPE", viewType);
        bundle.putInt("POSITION", adapterPosition);
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

        /* view adapters*/
        gridMenuAdapter = new TimeSheetMenuAdapter(getActivity(), gridMenuList, this);
        rvGridMenu.setAdapter(gridMenuAdapter);
        mOverViewListAdapter = new TimeSheetOverViewListAdapter(this, overviewDetailsList, viewModel, false);
        rvOverView.setAdapter(mOverViewListAdapter);

        mNotesListAdapter = new TimeSheetNotesListAdapter(this, notesList, viewModel);
        rvNotes.setAdapter(mNotesListAdapter);


        /*  view visibilities */
        llOther.setVisibility(View.GONE);
        ivForward.setVisibility(View.GONE);
        ivBackward.setVisibility(View.GONE);
        rlStandardBreaks.setVisibility(View.GONE);

    }

    @Override
    protected void init() {
        /* get arguments */
        if (getArguments() != null) {
            globalDate = getArguments().getString("date");
            viewType = getArguments().getString("VIEW_TYPE");
            selectedPosition = getArguments().getInt("POSITION");
        }
        tvTitle.setText(String.format("%s is paid Hourly.", BuilderStormApplication.mPrefs.getUserName()));
        tvDayDate.setText(CommonMethods.convertDate(CommonMethods.DF_2, globalDate, CommonMethods.DF_13));
        viewModel.getUserProjects();
        updateMenuSetting();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (updateTimeSheet && null != mCallback) {
            mCallback.onUpdate(globalDate);
        }
    }

    @OnClick({R.id.ivClose, R.id.etWorkStartTime, R.id.etWorkEndTime, R.id.etBreakStartTime, R.id.etBreakEndTime,
            R.id.etTravelStartTime, R.id.etTravelEndTime, R.id.etTravelFrom, R.id.etTravelTo,
            R.id.etHolidayStartTime, R.id.etHolidayEndTime, R.id.etSickStartTime, R.id.etSickEndTime,
            R.id.tvPriceWorkItem, R.id.tvProjects, R.id.tvWorkTask, R.id.btnShowNote, R.id.btnOvertimeSave,
            R.id.btnTravelSave, R.id.btnBreakSave, R.id.btnExpenseSave, R.id.btnPriceSave,
            R.id.btnHolidaySave, R.id.btnSickSave, R.id.btnNoteSave})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                dismissWithHideKeyboard();
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
            case R.id.etTravelFrom:
                showLocationDropdown(false);
                break;
            case R.id.etTravelTo:
                showLocationDropdown(true);
                break;
            case R.id.tvPriceWorkItem:
                showPriceWorkDropdown();
                break;
            case R.id.tvProjects:
                showProjectsDropdown();
                break;
            case R.id.tvWorkTask:
                if (!taskList.isEmpty()) {
                    AddTaskDialogFragment mAddTaskDialogFragment = AddTaskDialogFragment.newInstance(taskList);
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
            case R.id.btnShowNote:
                btnShowNote.setVisibility(View.GONE);
                llEditNote.setVisibility(View.VISIBLE);
                break;
            case R.id.btnOvertimeSave:
                if (!isWorkHoursSelected && etWorkStartTime.getText().toString().trim().isEmpty()) {
                    etWorkStartTime.setError("required field");
                } else if (!isWorkHoursSelected && etWorkEndTime.getText().toString().trim().isEmpty()) {
                    etWorkEndTime.setError("required field");
                } else if (!isWorkHoursSelected && !CommonMethods.isStartBeforeEndTime(etWorkStartTime.getText().toString().trim(),
                        etWorkEndTime.getText().toString().trim())) {
                    errorMessage(getString(R.string.earlier_start_time), null, false);
                } else if (isWorkHoursSelected && etWorkHours.getText().toString().trim().isEmpty()) {
                    etWorkHours.setError("required field");
                } else {
                    ReturnOverviewDetail detail = new ReturnOverviewDetail();
                    detail.setDatatype("Worktime");
                    detail.setData_type(DataNames.ADD_ACTION_WORK_TIME);
                    detail.setActivityid("0");
                    detail.setStartTime(etWorkStartTime.getText().toString().trim());
                    detail.setEndTime(etWorkEndTime.getText().toString().trim());
                    detail.setTimeInMinutes(etWorkHours.getText().toString().trim());
                    detail.setSettingTrue(!isWorkHoursSelected ? "" : "break_require_time");
                    detail.setProjectId(selectedProjectId);
                    detail.setProjectTitle(tvProjects.getText().toString().trim());
                    detail.setSelectedTaskids(CommonMethods.getCommaSeparatedString(taskIdList));
                    detail.setTotalTimeInMinutes(isWorkHoursSelected ? (Integer.parseInt(detail.getTimeInMinutes()) * 60) : CommonMethods.getTimeInMinutes(detail.getStartTime(), detail.getEndTime()));
                    detail.date = globalDate;
                    detail.is_work_start_end_time = !isWorkHoursSelected ? "1" : "0";
                    detail.deleteunfinishedactid = "0";
                    detail.setTimestring(isWorkHoursSelected ? detail.getTimeInMinutes() + " Hours" : (detail.getStartTime() + "  to  " + detail.getEndTime()));
                    overviewDetailsList.add(detail);
                    viewModel.addOfflineData(DataNames.WORK, "Work time", globalDate, overviewDetailsList, notesList, "4", menuSetting.getBreakAutomaticalyWorktime());
//                    viewModel.addOfflineData("Work time", globalDate, overviewDetailsList, notesList, "4", menuSetting.getBreakAutomaticalyWorktime());
                }
                break;

            case R.id.btnTravelSave:
                if (etTravelStartTime.getText().toString().trim().isEmpty()) {
                    errorMessage("Please enter travel start time", null, false);
                } else if (etTravelFrom.getText().toString().trim().isEmpty()) {
                    errorMessage("Please select start location", null, false);
                } else if (etTravelEndTime.getText().toString().trim().isEmpty()) {
                    errorMessage("Please enter travel end time", null, false);
                } else if (etTravelTo.getText().toString().trim().isEmpty()) {
                    errorMessage("Please select end location", null, false);
                } else if (!CommonMethods.isStartBeforeEndTime(etTravelStartTime.getText().toString().trim(),
                        etTravelEndTime.getText().toString().trim())) {
                    errorMessage(getString(R.string.earlier_start_time), null, false);
                } else {
                    ReturnOverviewDetail detail = new ReturnOverviewDetail();
                    detail.setDatatype("Travel");
                    detail.setData_type(DataNames.ADD_ACTION_TRAVEL);
                    detail.setStartTime(etTravelStartTime.getText().toString().trim());
                    detail.setEndTime(etTravelEndTime.getText().toString().trim());
                    detail.setFromLocation(etTravelFrom.getText().toString().trim());
                    detail.setToLocation(etTravelTo.getText().toString().trim());
                    detail.setToLocationId(travelToProjectId);
                    detail.setFromLocationId(travelFromProjectId);
                    detail.setProjectId(selectedProjectId);
                    detail.setProjectTitle(tvProjects.getText().toString().trim());
                    detail.setTotalTimeInMinutes(CommonMethods.getTimeInMinutes(detail.getStartTime(), detail.getEndTime()));
                    detail.date = globalDate;
                    detail.setTimestring(detail.getStartTime() + " from " + detail.getFromLocation() + " to " + detail.getEndTime() + "  " + detail.getToLocation());
                    overviewDetailsList.add(detail);
                    viewModel.addOfflineData(DataNames.TRAVEL, "Travel time", globalDate, overviewDetailsList, notesList, "4", false);
                }
                break;
            case R.id.btnBreakSave:
                if (isWorkHoursSelected && etBreakTime.getText().toString().trim().isEmpty()) {
                    errorMessage("Please enter break time", null, false);
                } else if (!isWorkHoursSelected && etBreakStartTime.getText().toString().trim().isEmpty()) {
                    errorMessage("Please enter break start time", null, false);
                } else if (!isWorkHoursSelected && etBreakEndTime.getText().toString().trim().isEmpty()) {
                    errorMessage("Please enter break end time", null, false);
                } else if (!isWorkHoursSelected && !CommonMethods.isStartBeforeEndTime(etBreakStartTime.getText().toString().trim(),
                        etBreakEndTime.getText().toString().trim())) {
                    errorMessage(getString(R.string.earlier_start_time), null, false);
                } else {
                    ReturnOverviewDetail detail = new ReturnOverviewDetail();
                    detail.setDatatype("Breaks");
                    detail.setData_type(DataNames.ADD_ACTION_BREAKS);
                    detail.setStartTime(etBreakStartTime.getText().toString().trim());
                    detail.setEndTime(etBreakEndTime.getText().toString().trim());
                    detail.setTimeInMinutes(etBreakTime.getText().toString().trim());
                    detail.setSettingTrue(isWorkHoursSelected ? "" : "break_require_time");
                    detail.setProjectId(selectedProjectId);
                    detail.setProjectTitle(tvProjects.getText().toString().trim());
                    // Calculate the break time
                    int breaktime = (isWorkHoursSelected ? Integer.parseInt(detail.getTimeInMinutes()) : CommonMethods.getTimeInMinutes(detail.getStartTime(), detail.getEndTime()));
                    // Check that breaks are paid or not.
                    if (menuSetting.getAllowBreaksPaid() != null && menuSetting.getAllowBreaksPaid()) {
                        // Breaks are paid and now check that work time is required or not.
                        if (menuSetting.getPaidBreakDetails() != null && menuSetting.getPaidBreakDetails().getBreakMustWork() != null && menuSetting.getPaidBreakDetails().getBreakMustWork()) {
                            // The work hours are required and now check that calculated work time is greater than the required.
                            if (menuSetting.getPaidBreakDetails().getBreakHoursToBeWorked() != null && workHours >= CommonMethods.getHoursToMin(menuSetting.getPaidBreakDetails().getBreakHoursToBeWorked())) {
                                // The work hours are greater than the required and now check that how much break time are paid required.
                                if (menuSetting.getPaidBreakDetails().getBreakHoursPaid() != null && menuSetting.getPaidBreakDetails().getBreakHoursPaid()) {
                                    if (menuSetting.getPaidBreakDetails().getBreakMinutesPaid() != null) {
                                        // Now check that paid break time greater than breaks hours.
                                        if (menuSetting.getPaidBreakDetails().getBreakMinutesPaid() >= breaktime) {
                                            detail.setTotalTimeInMinutes(1 * breaktime);
                                            detail.setSettingAllow(true);
                                        } else {
                                            detail.setTotalTimeInMinutes(1 * breaktime);
                                            breaktime = (breaktime - menuSetting.getPaidBreakDetails().getBreakMinutesPaid());
                                            detail.setBreakTimeAdded(-1 * breaktime);
                                            detail.setSettingAllow(true);
                                        }
                                    } else {
                                        detail.setTotalTimeInMinutes(1 * breaktime);
                                    }
                                } else {
                                    detail.setTotalTimeInMinutes(1 * breaktime);
                                }
                            } else {
                                detail.setTotalTimeInMinutes(-1 * breaktime);
                            }
                        } else {
                            detail.setTotalTimeInMinutes(1 * breaktime);
                        }
                    } else {
                        detail.setTotalTimeInMinutes(-1 * breaktime);
                        detail.setSettingAllow(false);
                    }


//                    if (menuSetting.getBreaKPaidTime() != null && !menuSetting.getBreaKPaidTime().isEmpty()) {
//                        int breaktime = (isWorkHoursSelected ? Integer.parseInt(detail.getTimeInMinutes()) : CommonMethods.getTimeInMinutes(detail.getStartTime(), detail.getEndTime()));
//                        if (menuSetting.getMinimumWork() != null && !menuSetting.getMinimumWork().isEmpty()) {
//                            if (workHours >= Integer.parseInt(menuSetting.getMinimumWork()) && breaktime <= Integer.parseInt(menuSetting.getBreaKPaidTime())) {
//                                detail.setTotalTimeInMinutes(1 * (isWorkHoursSelected ? Integer.parseInt(detail.getTimeInMinutes()) : CommonMethods.getTimeInMinutes(detail.getStartTime(), detail.getEndTime())));
//                            }
//                        }
//                    } else {
//                        detail.setTotalTimeInMinutes(-1 * (isWorkHoursSelected ? Integer.parseInt(detail.getTimeInMinutes()) : CommonMethods.getTimeInMinutes(detail.getStartTime(), detail.getEndTime())));
//                    }
//                    detail.setTotalTimeInMinutes(-1 * (isWorkHoursSelected ? Integer.parseInt(detail.getTimeInMinutes()) : CommonMethods.getTimeInMinutes(detail.getStartTime(), detail.getEndTime())));
                    detail.date = globalDate;
                    detail.break_ids = "";
                    detail.breaks_array = "";
                    detail.is_break_start_end_time = isWorkHoursSelected ? "0" : "1";
                    detail.setTimestring(isWorkHoursSelected ? detail.getTimeInMinutes() + " Minutes" : (detail.getStartTime() + "  to  " + detail.getEndTime()));
                    overviewDetailsList.add(detail);
                    viewModel.addOfflineData(DataNames.BREAKS, "Break", globalDate, overviewDetailsList, notesList, (viewType != null ? (!viewType.isEmpty() ? viewType : "0") : "0"), false);
                }
                break;

            case R.id.btnExpenseSave:
                if (etExpenseItem.getText().toString().trim().isEmpty()) {
                    errorMessage("Please enter item name", null, false);
                } else if (etExpenseCost.getText().toString().trim().isEmpty()) {
                    errorMessage("Please enter item price", null, false);
                } else {
                    ReturnOverviewDetail detail = new ReturnOverviewDetail();
                    detail.setDatatype("Expenses");
                    detail.setData_type(DataNames.ADD_ACTION_EXPENSES);
                    detail.setSettingTrue("");
                    detail.setExpensePrice(etExpenseCost.getText().toString().trim());
                    detail.setProjectId(selectedProjectId);
                    detail.setTotalhrswithrate("£" + detail.getExpensePrice());
                    detail.setProjectTitle(tvProjects.getText().toString().trim());
                    detail.setTotalTimeInMinutes(0);
                    detail.date = globalDate;
                    detail.setItem(etExpenseItem.getText().toString().trim());
                    detail.setTimestring(etExpenseItem.getText().toString().trim());
                    overviewDetailsList.add(detail);
                    viewModel.addOfflineData(DataNames.EXPENSE, detail.getDatatype(), globalDate, overviewDetailsList, notesList, "0", false);
                }
                break;
            case R.id.btnPriceSave:
                if (selectedPriceId.isEmpty()) {
                    errorMessage("Please select price item", null, false);
                } else if (etPriceWorkLocation.getText().toString().trim().isEmpty()) {
                    etPriceWorkLocation.setError(getString(R.string.required_field));
                } else if (etPriceWorkQuantity.getText().toString().trim().isEmpty()) {
                    etPriceWorkQuantity.setError(getString(R.string.required_field));
                } else {
                    ReturnOverviewDetail detail = new ReturnOverviewDetail();
                    detail.setDatatype("Pricework");
                    detail.setData_type(DataNames.ADD_ACTION_PRICE_WORK);
                    detail.setSettingTrue("");
                    detail.setActivityid("0");
                    detail.setQuantity(etPriceWorkQuantity.getText().toString().trim());
                    detail.setTotalhrswithrate("");
                    detail.setProjectId(selectedProjectId);
                    detail.setProjectTitle(tvProjects.getText().toString().trim());
                    detail.date = globalDate;
                    detail.setItem(selectedPriceId);
                    detail.pricework_item_cost = selectedItemPrice;
                    detail.setLocation(etPriceWorkLocation.getText().toString().trim());
                    detail.setTotalTimeInMinutes(0);
                    float price = Float.parseFloat(detail.getQuantity()) * Float.parseFloat(selectedItemPrice);
                    detail.setTimestring(detail.getQuantity() + " no. " + tvPriceWorkItem.getText().toString().trim()
                            + "  @" + selectedItemPrice + " = £" + price);

                    overviewDetailsList.add(detail);
                    viewModel.addOfflineData(DataNames.PRICE, detail.getDatatype(), globalDate, overviewDetailsList, notesList, "4", false);
                }
                break;
            case R.id.btnHolidaySave:
                if (isWorkHoursSelected && etHolidayHours.getText().toString().trim().isEmpty()) {
                    errorMessage("Please enter holiday time", null, false);
                } else if (!isWorkHoursSelected && etHolidayStartTime.getText().toString().trim().isEmpty()) {
                    errorMessage("Please enter holiday start time", null, false);
                } else if (!isWorkHoursSelected && etHolidayEndTime.getText().toString().trim().isEmpty()) {
                    errorMessage("Please enter holiday end time", null, false);
                } else if (!isWorkHoursSelected && !CommonMethods.isStartBeforeEndTime(etHolidayStartTime.getText().toString().trim(),
                        etHolidayEndTime.getText().toString().trim())) {
                    errorMessage(getString(R.string.earlier_start_time), null, false);
                } else {
                    ReturnOverviewDetail detail = new ReturnOverviewDetail();
                    detail.setDatatype("Holiday");
                    detail.setStartTime(etHolidayStartTime.getText().toString().trim());
                    detail.setActivityid("0");
                    detail.setTotalhrswithrate("");
                    detail.setEndTime(etHolidayEndTime.getText().toString().trim());
                    detail.setTimeInMinutes(etHolidayHours.getText().toString().trim());
                    detail.setDuration(/*etHolidayDuration.getText().toString().trim()*/ "1");
                    detail.setTotalTimeInMinutes(isWorkHoursSelected ? (Integer.parseInt(detail.getTimeInMinutes()) * 60) : CommonMethods.getTimeInMinutes(detail.getStartTime(), detail.getEndTime()));
                    detail.work_status = DataNames.ADD_ACTION_HOLIDAY;
                    detail.date = globalDate;
                    detail.setProjectId(selectedProjectId);
                    detail.setTimestring(isWorkHoursSelected ? "Holiday " + detail.getTimeInMinutes() + " Hours" : "Holiday " + detail.getStartTime() + " to " + detail.getEndTime());
                    overviewDetailsList.add(detail);
                    viewModel.addOfflineData(DataNames.HOLIDAY, detail.getDatatype(), globalDate, overviewDetailsList, notesList, "2", false);
                }
                break;
            case R.id.btnSickSave:
                if (isWorkHoursSelected && etSickHours.getText().toString().trim().isEmpty()) {
                    errorMessage("Please enter sick time", null, false);
                } else if (!isWorkHoursSelected && etSickStartTime.getText().toString().trim().isEmpty()) {
                    errorMessage("Please enter sick start time", null, false);
                } else if (!isWorkHoursSelected && etSickEndTime.getText().toString().trim().isEmpty()) {
                    errorMessage("Please enter sick end time", null, false);
                } else if (!isWorkHoursSelected && !CommonMethods.isStartBeforeEndTime(etSickStartTime.getText().toString().trim(),
                        etSickEndTime.getText().toString().trim())) {
                    errorMessage(getString(R.string.earlier_start_time), null, false);
                } else {
                    ReturnOverviewDetail detail = new ReturnOverviewDetail();
                    detail.setDatatype("Sick");
                    detail.setActivityid("0");
                    detail.setStartTime(etSickStartTime.getText().toString().trim());
                    detail.setEndTime(etSickEndTime.getText().toString().trim());
                    detail.setTimeInMinutes(etSickHours.getText().toString().trim());
                    detail.setTotalhrswithrate("");
                    detail.setDuration(/*etSickDuration.getText().toString().trim()*/ "1");
                    detail.setTotalTimeInMinutes(isWorkHoursSelected ? (Integer.parseInt(detail.getTimeInMinutes()) * 60) : CommonMethods.getTimeInMinutes(detail.getStartTime(), detail.getEndTime()));
                    detail.work_status = DataNames.ADD_ACTION_SICKNESS;
                    detail.date = globalDate;
                    detail.setProjectId(selectedProjectId);
                    detail.setTimestring(isWorkHoursSelected ? "Sick " + detail.getTimeInMinutes() + " Hours" : "Sick " + detail.getStartTime() + " to " + detail.getEndTime());

                    overviewDetailsList.add(detail);
                    viewModel.addOfflineData(DataNames.SICK, detail.getDatatype(), globalDate, overviewDetailsList, notesList, "3", false);
                }
                break;
            case R.id.btnNoteSave:
                clearAllErrors();
                if (etNote.getText().toString().trim().isEmpty()) {
                    etNote.setError(getString(R.string.required_field));
                } else {
                    TSNote note = new TSNote();
                    note.setFullName(BuilderStormApplication.mPrefs.getUserName());
                    note.setAddedOn(CommonMethods.getCurrentDate(CommonMethods.DF_1));
                    note.setDateFor(globalDate);
                    note.setNoteDescription(etNote.getText().toString().trim());
                    notesList.add(note);
                    viewModel.addOfflineData(DataNames.NOTES, "Note", globalDate, overviewDetailsList, notesList, (viewType != null ? (!viewType.isEmpty() ? viewType : "0") : "0"), false);
                }
                break;

        }
    }

    private void observeViewModel() {

        viewModel.isRefreshing.observe(getViewLifecycleOwner(), aBoolean -> {
            if (null != aBoolean) {
                if (aBoolean) showProgress();
                else dismissProgress();
            }
        });

        viewModel.menuSetting.observe(getViewLifecycleOwner(), returnOverviewHtmlDetail -> {
            menuSetting = returnOverviewHtmlDetail;
            initializeMenu();
        });

        viewModel.projectsList.observe(getViewLifecycleOwner(), projects -> {
            if (null != projects) {
                globalProjects.clear();
                globalProjects.addAll(projects);
            }
        });

        viewModel.tasksLiveList.observe(getViewLifecycleOwner(), returnTaskList -> {
            if (returnTaskList != null) {
                taskList.clear();
                taskList.addAll(returnTaskList);
            }
        });

        viewModel.priceWorkLiveList.observe(getViewLifecycleOwner(), priceWorkListings -> {
            if (null != priceWorkListings) {
                priceWorkList.clear();
                priceWorkList.addAll(priceWorkListings);
                llPriceWork.setVisibility(priceWorkListings.isEmpty() ? View.GONE : View.VISIBLE);
                if (priceWorkListings.isEmpty()) {
                    tvNotSpecified.setText(getString(R.string.no_item_available));
                }
                tvNotSpecified.setVisibility(priceWorkListings.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });

        viewModel.successfulLiveData.observe(getViewLifecycleOwner(), message -> {
            if (null != message) {
                updateTimeSheet = true;
                errorMessage(message, null, false);
                updateOverView();
            }
        });

        viewModel.successfulLiveData2.observe(getViewLifecycleOwner(), message -> {
            if (null != message) {
                updateTimeSheet = true;
                errorMessage(message, null, false);
                clearAllFields();
                hideDialogKeyboard();
                tvNotSpecified.setVisibility(View.GONE);

                setUpVisibilities(llBreaks, llExpense, rvOverView, llOverTime, llTravel,
                        llPriceWork, llHoliday, llSick, llNote);
                viewModel.updateOfflineWeekData(globalDate);
                setUpBreaksView();
            }
        });

        viewModel.offlineOverViewLiveData.observe(getViewLifecycleOwner(), overviewDetails -> {
            if (null != overviewDetails) {
                overviewDetailsList.clear();
                overviewDetailsList.addAll(overviewDetails);
                mOverViewListAdapter.notifyDataSetChanged();
                tvNotSpecified.setText(getString(R.string.no_overview_details_found));
                tvNotSpecified.setVisibility(overviewDetailsList.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });

        viewModel.offlineNotesLiveData.observe(getViewLifecycleOwner(), tsNotes -> {
            if (null != tsNotes) {
                notesList.clear();
                notesList.addAll(tsNotes);
                mNotesListAdapter.notifyDataSetChanged();
            }
        });

        viewModel.deleteActivityLD.observe(getViewLifecycleOwner(), position -> {
            if (position != null && position != -1) {
                updateTimeSheet = true;
                mOverViewListAdapter.notifyDataSetChanged();
            }

        });

        viewModel.delNoteLiveData.observe(getViewLifecycleOwner(), position -> {
            if (position != null && position != -1) {
                mNotesListAdapter.notifyDataSetChanged();
            }
        });

        viewModel.timeSheetOfflineLiveData.observe(getViewLifecycleOwner(), weekData -> {
            if (null != weekData && !weekData.isEmpty()) {
                viewType = weekData.get(selectedPosition).getTimeButton();
                String viewTypeName = weekData.get(selectedPosition).getViewTypeName();
                if (viewTypeName != null && viewTypeName.equals(DataNames.WORK)) {
                    workHours = (weekData.get(selectedPosition).getWorkHours() != null && !weekData.get(selectedPosition).getWorkHours().isEmpty()) ?
                            Integer.parseInt(weekData.get(selectedPosition).getWorkHours()) : 0;
                }
            }
        });

    }

    private void updateMenuSetting() {
        viewModel.getUserSetting();
    }

    /* pop ups and drop downs */

    private void updateOverView() {
        gridMenuAdapter.setOfflineDefaultView();
        viewModel.getOverview(selectedProjectId, globalDate);
        viewModel.updateOfflineWeekData(globalDate);
    }

    private void setUpBreaksView() {
        if (menuSetting != null) {
            rlStandardBreaks.setVisibility(View.GONE);
            llBreakSplit.setVisibility(View.VISIBLE);
            if (null != menuSetting.getBreakRequireTimes()
                    && menuSetting.getBreakRequireTimes()) {
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

    private void showProjectsDropdown() {
        if (null != getActivity()) {
            if (!globalProjects.isEmpty()) {
                ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
                listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.row_dropdown,
                        R.id.tvDropDown, globalProjects));
                listPopupWindow.setAnchorView(tvProjects);
                listPopupWindow.setModal(true);
                listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                    tvProjects.setText(globalProjects.get(position).getTitle());
                    selectedProjectId = globalProjects.get(position).getUid();
                    initializeMenu();
                    listPopupWindow.dismiss();
                });
                listPopupWindow.show();
            } else {
                errorMessage("No Projects Found", null, false);
            }
        }
    }

    private void showLocationDropdown(boolean toLocation) {
        if (!globalProjects.isEmpty()) {
            if (null != getActivity()) {
                List<ProjectDetails> projectList = new ArrayList<>();
                projectList.addAll(globalProjects);
                projectList.add(0, new ProjectDetails("-1", "Home"));
                ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
                listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(),
                        R.layout.row_dropdown, R.id.tvDropDown, projectList));
                listPopupWindow.setAnchorView(toLocation ? etTravelTo : etTravelFrom);
                listPopupWindow.setModal(true);
                listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                    if (toLocation) {
                        travelToProjectId = projectList.get(position).getUid();
                    } else {
                        travelFromProjectId = projectList.get(position).getUid();
                    }
                    (toLocation ? etTravelTo : etTravelFrom).setText(
                            projectList.get(position).getTitle());
                    listPopupWindow.dismiss();
                });
                listPopupWindow.show();
            }
        } else {
            errorMessage("No Projects Found", null, false);
        }
    }

    private void showPriceWorkDropdown() {
        if (null != getActivity()) {
            if (!priceWorkList.isEmpty()) {
                ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
                listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(),
                        R.layout.row_dropdown, R.id.tvDropDown, priceWorkList));
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

            } else {
                errorMessage("No Item Found", null, false);
            }
        }
    }

    /* visibilities */

    /* Callbacks */
    @Override
    public void setItemListener(int action) {
        clearAllFields();
        clearAllErrors();
        hideDialogKeyboard();
        setUpProjectTitle(false);
        tvNotSpecified.setVisibility(View.GONE);
        switch (action) {
            case DataNames.TIME_SHEET_OVERVIEW:
                setUpVisibilities(rvOverView, llOverTime, llTravel, llBreaks, llExpense,
                        llPriceWork, llHoliday, llSick, llNote);
                break;
            case DataNames.TIME_SHEET_WORK_TIME:
                setUpVisibilities(llOverTime, rvOverView, llTravel, llBreaks, llExpense,
                        llPriceWork, llHoliday, llSick, llNote);
                setUpProjectTitle(true);
                viewModel.getTimeSheetTasks(selectedProjectId);
                break;
            case DataNames.TIME_SHEET_TRAVEL:
                setUpVisibilities(llTravel, rvOverView, llOverTime, llBreaks, llExpense,
                        llPriceWork, llHoliday, llSick, llNote);
                break;
            case DataNames.TIME_SHEET_BREAKS:
                setUpVisibilities(llBreaks, llExpense, rvOverView, llOverTime, llTravel,
                        llPriceWork, llHoliday, llSick, llNote);
                break;
            case DataNames.TIME_SHEET_EXPENSES:
                setUpVisibilities(llExpense, llBreaks, rvOverView, llOverTime, llTravel,
                        llPriceWork, llHoliday, llSick, llNote);
                break;
            case DataNames.TIME_SHEET_PRICE_WORK:
                setUpVisibilities(llPriceWork, llExpense, llBreaks, rvOverView, llOverTime, llTravel,
                        llHoliday, llSick, llNote);
                viewModel.getPriceWorkList(selectedProjectId);
                break;
            case DataNames.TIME_SHEET_HOLIDAY:
                setUpVisibilities(llHoliday, rvOverView, llOverTime, llTravel, llBreaks,
                        llExpense, llPriceWork, llSick, llNote);
                break;
            case DataNames.TIME_SHEET_SICK:
                setUpVisibilities(llSick, llNote, rvOverView, llOverTime, llTravel,
                        llBreaks, llExpense, llPriceWork, llHoliday);
                break;
            case DataNames.TIME_SHEET_NOTES:
                setUpVisibilities(llNote, rvOverView, llOverTime, llTravel, llBreaks, llExpense,
                        llPriceWork, llHoliday, llSick);
                break;

        }
    }

    private void setUpVisibilities(View visibleView, View hiddenView1, View hiddenView2,
                                   View hiddenView3, View hiddenView4, View hiddenView5,
                                   View hiddenView6, View hiddenView7, View hiddenView8) {

        switch (visibleView.getId()) {
            case R.id.llOverTime:
                if (menuSetting != null) {
                    if (menuSetting.getAllowTrackTasks() && !selectedProjectId.isEmpty()) {
                        viewModel.getTimeSheetTasks(selectedProjectId);
                        tvWorkTask.setVisibility(View.VISIBLE);
                    } else {
                        tvWorkTask.setVisibility(View.GONE);
                    }
                    isWorkHoursSelected = null != menuSetting.getHourlyTime() && menuSetting.getHourlyTime();
                    etWorkStartTime.setVisibility(isWorkHoursSelected ? View.GONE : View.VISIBLE);
                    etWorkEndTime.setVisibility(isWorkHoursSelected ? View.GONE : View.VISIBLE);
                    etWorkHours.setVisibility(isWorkHoursSelected ? View.VISIBLE : View.GONE);
                    if (isWorkHoursSelected) {
                        if (menuSetting.getUserPayType()) {
                            etWorkHours.setHint("Total Shifts");
                        } else {
                            etWorkHours.setHint("Total Hours");
                        }
                    }
                }
                visibleView.setVisibility(View.VISIBLE);
                break;
            case R.id.llBreaks:
                if (null != menuSetting) {
                    isWorkHoursSelected = !(null != menuSetting.getBreakRequireTimes() && menuSetting.getBreakRequireTimes());
                    etBreakTime.setVisibility(isWorkHoursSelected ? View.VISIBLE : View.GONE);
                    etBreakStartTime.setVisibility(isWorkHoursSelected ? View.GONE : View.VISIBLE);
                    etBreakEndTime.setVisibility(isWorkHoursSelected ? View.GONE : View.VISIBLE);
                }
                visibleView.setVisibility(View.VISIBLE);
                break;
            case R.id.llHoliday:
                if (null != menuSetting) {
                    isWorkHoursSelected = !(menuSetting.getViewType() != null && menuSetting.getViewType());
                    etHolidayStartTime.setVisibility(isWorkHoursSelected ? View.GONE : View.VISIBLE);
                    etHolidayEndTime.setVisibility(isWorkHoursSelected ? View.GONE : View.VISIBLE);
                    etHolidayHours.setVisibility(isWorkHoursSelected ? View.VISIBLE : View.GONE);
                }
                etHolidayDuration.setVisibility(View.GONE);
                visibleView.setVisibility(View.VISIBLE);
                break;
            case R.id.llSickness:
                if (null != menuSetting) {
                    isWorkHoursSelected = !(menuSetting.getViewType() != null && menuSetting.getViewType());
                    etSickStartTime.setVisibility(isWorkHoursSelected ? View.GONE : View.VISIBLE);
                    etSickEndTime.setVisibility(isWorkHoursSelected ? View.GONE : View.VISIBLE);
                    etSickHours.setVisibility(isWorkHoursSelected ? View.VISIBLE : View.GONE);
                }
                etSickDuration.setVisibility(View.GONE);
                visibleView.setVisibility(View.VISIBLE);
                break;
            case R.id.llNote:
                llEditNote.setVisibility(View.GONE);
                btnShowNote.setVisibility(View.VISIBLE);
                visibleView.setVisibility(View.VISIBLE);
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
    }

    private void clearAllFields() {
        if (isAdded()) {
            isWorkHoursSelected = false;
            etWorkStartTime.setText(null);
            etWorkEndTime.setText(null);
            etWorkHours.setText(null);
            travelFromProjectId = "";
            travelFromProjectId = "";
            etTravelStartTime.setText(null);
            etTravelFrom.setText(null);
            etTravelEndTime.setText(null);
            etTravelTo.setText(null);
            etBreakTime.setText(null);
            etBreakStartTime.setText(null);
            etBreakEndTime.setText(null);
            etExpenseItem.setText(null);
            etExpenseCost.setText(null);
            /* price empty */

            selectedPriceId = "";
            selectedItemPrice = "";
            tvPriceWorkItem.setText(null);
            tvPriceItem.setText(null);
            tvPriceItem.setVisibility(View.GONE);
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
            travelToProjectId = "";
            travelFromProjectId = "";
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
                            tvWorkTask.append(
                                    tvWorkTask.getText().toString().trim().isEmpty() ? task.getTaskTitle() : "  , " + task.getTaskTitle());
                        }
                    }
                }
            }
        }
    }

    private void setUpProjectTitle(boolean isVisible) {
        if (!selectedProjectId.isEmpty() && selectedProjectId.equals("-1") && isVisible) {
            etProjectTitle.setVisibility(View.VISIBLE);
        } else {
            etProjectTitle.setVisibility(View.GONE);
        }
    }

    /* set up  views*/
    private void initializeMenu() {
        gridMenuList.clear();
        if (menuSetting != null) {
            if (!menuSetting.getIsProcessed()) {
                tvProjects.setVisibility(View.VISIBLE);
                for (DashBoardMenuModel model : CommonMethods.getTimeSheetMenu()) {

                    if (model.getAction_index().equals(DataNames.TIME_SHEET_OVERVIEW) ||
                            model.getAction_index().equals(DataNames.TIME_SHEET_HOLIDAY) ||
                            model.getAction_index().equals(DataNames.TIME_SHEET_SICK) ||
                            model.getAction_index().equals(DataNames.TIME_SHEET_NOTES)) {
                        gridMenuList.add(model);
                    }

                    if (!selectedProjectId.isEmpty() && (menuSetting.getCheckifAbsent() == null || menuSetting.getCheckifAbsent().isEmpty())) {

                        if (model.getAction_index().equals(DataNames.TIME_SHEET_EXPENSES) && menuSetting.getAllowExpensesPaid()) {
                            gridMenuList.add(model);
                        }

                        if (model.getAction_index().equals(DataNames.TIME_SHEET_PRICE_WORK) && menuSetting.getAllowExpensesPaid()) {
                            gridMenuList.add(model);
                        }
                        if (model.getAction_index().equals(DataNames.TIME_SHEET_WORK_TIME) ||
                                model.getAction_index().equals(DataNames.TIME_SHEET_TRAVEL) ||
                                model.getAction_index().equals(DataNames.TIME_SHEET_BREAKS)) {
                            gridMenuList.add(model);
                        }
                    }
                }

            } else {
                tvProjects.setVisibility(View.GONE);
                tvNotSpecified.setVisibility(View.VISIBLE);
                tvNotSpecified.setText(getString(R.string.processed_time_sheet));
            }
        } else {
            for (DashBoardMenuModel model : CommonMethods.getTimeSheetMenu()) {
                if (model.getAction_index() != DataNames.TIME_SHEET_OTHER) {
                    if (model.getAction_index() == DataNames.TIME_SHEET_OVERVIEW ||
                            model.getAction_index() == DataNames.TIME_SHEET_HOLIDAY ||
                            model.getAction_index() == DataNames.TIME_SHEET_SICK ||
                            model.getAction_index() == DataNames.TIME_SHEET_NOTES) {
                        gridMenuList.add(model);
                    }
                    if (!selectedProjectId.isEmpty()) {
                        if (model.getAction_index() == DataNames.TIME_SHEET_WORK_TIME ||
                                model.getAction_index() == DataNames.TIME_SHEET_TRAVEL ||
                                model.getAction_index() == DataNames.TIME_SHEET_EXPENSES ||
                                model.getAction_index() == DataNames.TIME_SHEET_BREAKS ||
                                model.getAction_index() == DataNames.TIME_SHEET_PRICE_WORK) {
                            gridMenuList.add(model);
                        }
                    }

                }
            }
        }
        updateOverView();
    }

    public String getGlobalDate() {
        return globalDate;
    }

    public Integer getUnfinishedActivityId() {
        return menuSetting != null ? menuSetting.getDeleteunfinishedactid() : 0;
    }

    public void setCallback(UpdateTimeSheetCallback mCallback) {
        this.mCallback = mCallback;
    }
}
