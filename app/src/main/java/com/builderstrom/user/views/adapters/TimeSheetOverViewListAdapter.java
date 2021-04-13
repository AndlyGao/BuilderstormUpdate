package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.modals.ReturnOverviewDetail;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.TimeSheetViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.dialogFragments.AddTSOfflineDialogFragment;
import com.builderstrom.user.views.dialogFragments.AddTimeSheetDialogFragment;
import com.builderstrom.user.views.dialogFragments.EditTSActivityFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimeSheetOverViewListAdapter extends RecyclerView.Adapter<TimeSheetOverViewListAdapter.OverViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<ReturnOverviewDetail> dataList;
    private TimeSheetViewModel viewModel;
    private DialogFragment dialogFragment;
    private Boolean isOverTimeBreak;

    public TimeSheetOverViewListAdapter(DialogFragment fragment, List<ReturnOverviewDetail> dataList,
                                        TimeSheetViewModel viewModel, Boolean isOverTimeBreak) {
        this.dialogFragment = fragment;
        this.mContext = fragment.getActivity();
        mLayoutInflater = LayoutInflater.from(mContext);
        this.dataList = dataList;
        this.viewModel = viewModel;
        this.isOverTimeBreak = isOverTimeBreak;
    }

    @NonNull
    @Override
    public OverViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new OverViewHolder(mLayoutInflater.inflate(R.layout.row_overview_timesheet,
                viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OverViewHolder overViewHolder, int i) {
        ReturnOverviewDetail detail = dataList.get(i);
        overViewHolder.tvPrice.setVisibility(View.VISIBLE);


        switch (detail.getDatatype()) {
            case "Worktime":
                overViewHolder.tvTiming.setText(String.format("%s %s", detail.getTimestring(),
                        detail.getProjectTitle()));
                overViewHolder.tvPrice.setText(detail.getTotalhrswithrate());
                overViewHolder.ivEdit.setVisibility(View.VISIBLE);
                break;
            case "Breaks":
                overViewHolder.tvTiming.setText(detail.getTimestring());
                overViewHolder.tvPrice.setText(detail.getProjectTitle());
                overViewHolder.ivEdit.setVisibility(View.VISIBLE);
                break;
            case "StandardBreaks":
                overViewHolder.tvTiming.setText(detail.getTimestring());
                overViewHolder.tvPrice.setText(detail.getProjectTitle());
                overViewHolder.ivEdit.setVisibility(View.GONE);
                break;
            case "Travel":
                overViewHolder.tvTiming.setText(detail.getTimestring());
                overViewHolder.tvPrice.setText(detail.getTotalhrswithrate());
                overViewHolder.ivEdit.setVisibility(View.VISIBLE);
                break;
            case "Expenses":
                overViewHolder.tvTiming.setText(String.format("%s %s", detail.getTimestring(), detail.getTotalhrswithrate()));
                overViewHolder.tvPrice.setText(detail.getProjectTitle());
                overViewHolder.ivEdit.setVisibility(View.VISIBLE);
                break;
            case "Pricework":
                overViewHolder.tvTiming.setText(detail.getTotalhrswithrate().isEmpty() ? detail.getTimestring() : detail.getTotalhrswithrate());
                overViewHolder.tvPrice.setText(detail.getProjectTitle());
                overViewHolder.ivEdit.setVisibility(View.VISIBLE);
                break;
            case "Holiday":
            case "Sick":
                overViewHolder.tvTiming.setText(detail.getTotalhrswithrate().isEmpty() ? detail.getTimestring() : detail.getTotalhrswithrate());
                overViewHolder.tvPrice.setText(detail.getProjectTitle());
                overViewHolder.tvPrice.setVisibility(View.GONE);
                overViewHolder.ivEdit.setVisibility(View.GONE);
                break;
            case "MetaData":
                overViewHolder.tvTiming.setText(detail.getTotalhrswithrate().isEmpty() ? detail.getTimestring() : detail.getTotalhrswithrate());
                overViewHolder.tvPrice.setText(detail.getProjectTitle());
                overViewHolder.ivEdit.setVisibility(View.GONE);
                break;
            default:
                overViewHolder.tvTiming.setText(detail.getTotalhrswithrate().isEmpty() ? detail.getTimestring() : detail.getTotalhrswithrate());
                overViewHolder.tvPrice.setText(detail.getProjectTitle());
                overViewHolder.ivEdit.setVisibility(View.VISIBLE);
                break;
        }


        overViewHolder.ivType.setImageResource(checkOverViewType(detail.getDatatype()));

    }

    private int checkOverViewType(String dataType) {
        switch (dataType) {
            case "Breaks":
            case "StandardBreaks":
                return R.drawable.ic_breaks;
            case "Travel":
                return R.drawable.ic_travel;
            case "Expenses":
                return R.drawable.ic_expenses;
            case "Pricework":
                return R.drawable.ic_pound;
            case "Worktime":
                return R.drawable.ic_worktime;
            case "MetaData":
                return R.drawable.ic_other;
            case "Holiday":
                return R.drawable.ic_holiday;
            case "Sick":
                return R.drawable.ic_sick;
            default:
                return R.drawable.ic_calendar;
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void removeItem(int position) {
        if (position < dataList.size()) {
            dataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public class OverViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTiming)
        TextView tvTiming;
        @BindView(R.id.tvPrice)
        TextView tvPrice;
        @BindView(R.id.ivType)
        ImageView ivType;
        @BindView(R.id.ivEdit)
        ImageView ivEdit;

        public OverViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.ivEdit, R.id.ivDelete})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivEdit:
                    if (dataList != null && getAdapterPosition() < dataList.size()) {
                        if (dialogFragment instanceof AddTimeSheetDialogFragment) {
                            AddTimeSheetDialogFragment dFragment = (AddTimeSheetDialogFragment) dialogFragment;
                            EditTSActivityFragment editFragment = EditTSActivityFragment.newInstance(1,
                                    dataList.get(getAdapterPosition()), dFragment.getGlobalDate(),
                                    dFragment.getTimeSheetOverview().getOverviewHtml().getDeleteunfinishedactid(), isOverTimeBreak);
                            editFragment.setEditSuccessCallback(dFragment);
                            editFragment.show(((BaseActivity) mContext).getSupportFragmentManager(), "edit view");
                        } else {
                            AddTSOfflineDialogFragment dFragment = (AddTSOfflineDialogFragment) dialogFragment;
                            EditTSActivityFragment editFragment = EditTSActivityFragment.newInstance(1,
                                    dataList.get(getAdapterPosition()), dFragment.getGlobalDate(),
                                    dFragment.getUnfinishedActivityId(), isOverTimeBreak);
                            editFragment.setOfflineCallback(overviewDetail -> {
                                dataList.set(getAdapterPosition(), overviewDetail);
                                viewModel.deleteOfflineOverViewActivity(dFragment.getGlobalDate(),
                                        getAdapterPosition(), dataList, CommonMethods.timesheetViewType(dataList.get(getAdapterPosition()).getDatatype()), dataList.get(getAdapterPosition()).getDatatype());
                            });
                            editFragment.show(((BaseActivity) mContext).getSupportFragmentManager(), "edit view");
                        }
                    }
                    break;
                case R.id.ivDelete:
                    if (dataList != null && getAdapterPosition() < dataList.size()) {
                        if (dialogFragment instanceof AddTSOfflineDialogFragment) {
//                            String viewType = CommonMethods.timesheetViewType(dataList.get(getAdapterPosition()).getDatatype());
                            String viewType = "0";
                            String viewTypeName = "";
                            dataList.remove(getAdapterPosition());
                            if (dataList.size() > 0) {
                                for (ReturnOverviewDetail model : dataList) {
                                    if (model.getDatatype().equals("Worktime")) {
                                        viewType = "4";
                                        viewTypeName = DataNames.WORK;
                                        break;
                                    }
                                }
                            }
                            AddTSOfflineDialogFragment offFragment = (AddTSOfflineDialogFragment) dialogFragment;
                            viewModel.deleteOfflineOverViewActivity(offFragment.getGlobalDate(),
                                    getAdapterPosition(), dataList, viewType, viewTypeName);
//                                    getAdapterPosition(), dataList, viewType.equals("4") ? "0" : viewType);
                        } else {
                            viewModel.deleteOverviewActivity(dataList.get(getAdapterPosition()).getActivityid(),
                                    getAdapterPosition());
                        }
                    }
                    break;
            }
        }
    }
}
