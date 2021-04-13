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
import com.builderstrom.user.data.retrofit.modals.TimeSheetListModel;
import com.builderstrom.user.viewmodels.TimeSheetViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.dialogFragments.AddTimeSheetDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimeSheetListAdapter extends RecyclerView.Adapter<TimeSheetListAdapter.TimeSheetHolder> {

    private Context mContext;
    private List<TimeSheetListModel.WeekDataModel> dataList;
    private LayoutInflater mLayoutInflater;
    private TimeSheetViewModel viewModel;


    public TimeSheetListAdapter(Context mContext, List<TimeSheetListModel.WeekDataModel> dataList,
                                TimeSheetViewModel viewModel) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.dataList = dataList;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public TimeSheetHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TimeSheetHolder(mLayoutInflater.inflate(R.layout.row_time_sheet, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSheetHolder timeSheetHolder, int position) {
        TimeSheetListModel.WeekDataModel data = dataList.get(position);
        timeSheetHolder.tvDateTime.setText(data.getWeekdayheading());
        timeSheetHolder.btnAddTime.setText(data.getText());
        timeSheetHolder.btnAddTime.setBackgroundResource(setButtonBackground(data.getTimebutton()));

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class TimeSheetHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.btnAddTime) Button btnAddTime;
        @BindView(R.id.tvDateTime) TextView tvDateTime;
        @BindView(R.id.ivSync) ImageView ivSync;

        TimeSheetHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ivSync.setVisibility(View.GONE);
            /* Add Document Visibility*/
//            if (userPermissions != null) {
//                CommonMethods.checkVisiblePermission(userPermissions.getTimesheetsNew().getSetupProjectTimesheet(), btnAddTime);
//            }
        }

        @OnClick(R.id.btnAddTime)
        public void onClick() {
            AddTimeSheetDialogFragment dialogFragment = AddTimeSheetDialogFragment.newInstance(
                    String.valueOf(dataList.get(getAdapterPosition()).getDate()));
            dialogFragment.setCallback(currentDate -> viewModel.updateTimeSheet(currentDate, true));
            dialogFragment.show(((BaseActivity) mContext).getSupportFragmentManager(), "add time sheet");

        }
    }

    private int setButtonBackground(String timeButton) {
        switch (timeButton) {
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
}
