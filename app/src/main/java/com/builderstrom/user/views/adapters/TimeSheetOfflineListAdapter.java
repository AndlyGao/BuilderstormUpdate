package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.modals.PojoOffTimeSheetModal;
import com.builderstrom.user.data.retrofit.modals.ReturnOverviewhtmldetail;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.TimeSheetViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.dialogFragments.AddTSOfflineDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimeSheetOfflineListAdapter extends RecyclerView.Adapter<TimeSheetOfflineListAdapter.TSHolder> {
    private Context mContext;
    private List<PojoOffTimeSheetModal> dataList;
    private LayoutInflater mLayoutInflater;
    private TimeSheetViewModel viewModel;
    private ReturnOverviewhtmldetail menuSetting;


    public TimeSheetOfflineListAdapter(Context mContext, List<PojoOffTimeSheetModal> dataList,
                                       TimeSheetViewModel viewModel, ReturnOverviewhtmldetail menuSetting) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.dataList = dataList;
        this.viewModel = viewModel;
        this.menuSetting = menuSetting;
    }

    @NonNull
    @Override
    public TSHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TSHolder(mLayoutInflater.inflate(R.layout.row_time_sheet, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull TSHolder tsHolder, int position) {
        PojoOffTimeSheetModal data = dataList.get(position);
        tsHolder.ivSync.setVisibility(((data.getWorkHours() == null || data.getWorkHours().isEmpty() || data.getWorkHours().equals("0")) && !data.getViewTypeName().equals(DataNames.PRICE)) ? View.GONE : View.VISIBLE);
        tsHolder.tvDateTime.setText(data.getDay());
        Integer totalTime = Integer.parseInt(data.getWorkHours());
        if (calculateWorkInhHours(totalTime, menuSetting)) {
            totalTime = totalTime - menuSetting.getDeductBreakDetails().getDeductBreakMinutes();
            tsHolder.btnAddTime.setText(data.isShowPriceWork() ? "P/W" : CommonMethods.convertMinToHours(totalTime));
        } else {
            tsHolder.btnAddTime.setText(data.isShowPriceWork() ? "P/W" : CommonMethods.convertMinToHours(Integer.parseInt(data.getWorkHours())));
        }

//      tsHolder.btnAddTime.setBackgroundResource(setButtonBackground(data.isShowPriceWork() ? DataNames.BUTTON_PRIMARY : DataNames.BUTTON_DANGER));
        tsHolder.btnAddTime.setBackgroundResource(setButtonBackground(data.getTimeButton()));
    }

    private boolean calculateWorkInhHours(Integer totalTime, ReturnOverviewhtmldetail menuSetting) {
        if (menuSetting != null && totalTime != null && totalTime > 0) {
            if (menuSetting.getDeductBreak() != null && menuSetting.getDeductBreak()) {
                if (menuSetting.getDeductBreakDetails() != null) {
                    if (menuSetting.getDeductBreakDetails().getBreakUserMustWork() != null && menuSetting.getDeductBreakDetails().getBreakUserMustWork()) {
                        if (menuSetting.getDeductBreakDetails().getBreakApplyAfterHoursWorked() != null && menuSetting.getDeductBreakDetails().getBreakApplyAfterHoursWorked() > 0) {
                            Integer minimumTimeRequiredInMinutes = CommonMethods.convertHoursToMinutes(menuSetting.getDeductBreakDetails().getBreakApplyAfterHoursWorked());
                            if (totalTime >= minimumTimeRequiredInMinutes) {
                                if (menuSetting.getDeductBreakDetails().getDeductBreakMinutes() != null && menuSetting.getDeductBreakDetails().getDeductBreakMinutes() > 0) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private int setButtonBackground(String workHours) {
        switch (workHours) {
            case DataNames.BUTTON_DANGER:
                return R.drawable.drawable_danger;
            case DataNames.BUTTON_SUCCESS:
                return R.drawable.drawable_success;
            case DataNames.BUTTON_HOLIDAY:
                return R.drawable.drawable_holiday;
            case DataNames.BUTTON_WARNING:
                return R.drawable.drawable_warning;
//            case DataNames.BUTTON_PRIMARY:
//                return R.drawable.drawable_primary;
            default:
                return R.drawable.drawable_primary;
        }
    }

    class TSHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btnAddTime)
        Button btnAddTime;
        @BindView(R.id.tvDateTime)
        TextView tvDateTime;
        @BindView(R.id.ivSync)
        ImageView ivSync;

        TSHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.btnAddTime)
        public void onClick() {
            if (null != dataList && getAdapterPosition() < dataList.size()) {
                AddTSOfflineDialogFragment dialogFragment = AddTSOfflineDialogFragment.newInstance(CommonMethods.convertDate(CommonMethods.DF_11,
                        dataList.get(getAdapterPosition()).getDay(), CommonMethods.DF_2), dataList.get(getAdapterPosition()).getTimeButton(), getAdapterPosition());
                dialogFragment.setCallback(currentDate -> viewModel.updateTimeSheet(currentDate, true));
                dialogFragment.show(((BaseActivity) mContext).getSupportFragmentManager(), "add offline timesheet");
            }
        }
    }

}
