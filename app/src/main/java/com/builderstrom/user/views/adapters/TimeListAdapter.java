package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.StandardBreaks;
import com.builderstrom.user.data.retrofit.modals.TimeModel;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimeListAdapter extends RecyclerView.Adapter<TimeListAdapter.TimeHolder> {
    private Context mContext;
    private List<StandardBreaks> timeModelList;
    private LayoutInflater mLayoutInflater;
    private boolean showDelete;

    public TimeListAdapter(Context mContext, List<StandardBreaks> timeModelList,
                           boolean showDelete) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.timeModelList = timeModelList;
        this.showDelete = showDelete;
    }

    @NonNull
    @Override
    public TimeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TimeHolder(mLayoutInflater.inflate(R.layout.row_over_time, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TimeHolder timeHolder, int i) {
        timeHolder.ivDelete.setVisibility(showDelete ? View.VISIBLE : View.INVISIBLE);
        timeHolder.cbRow.setVisibility(showDelete ? View.VISIBLE : View.INVISIBLE);
        StandardBreaks model = timeModelList.get(i);
        timeHolder.tvStart.setText(
                null == model.getStart_time() || model.getStart_time().isEmpty() ? null : model.getStart_time());
        timeHolder.tvEnd.setText(
                null == model.getEnd_time() || model.getEnd_time().isEmpty() ? null : model.getEnd_time());
        timeHolder.cbRow.setChecked(model.isChecked());

    }

    @Override
    public int getItemCount() {
        return timeModelList.size();
    }

    class TimeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvStart) TextView tvStart;
        @BindView(R.id.tvEnd) TextView tvEnd;
        @BindView(R.id.ivDelete) ImageView ivDelete;
        @BindView(R.id.cbRow) CheckBox cbRow;

        public TimeHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cbRow.setOnCheckedChangeListener((buttonView, isChecked) -> {
                timeModelList.get(getAdapterPosition()).setChecked(isChecked);
//                notifyDataSetChanged();
            });
        }

        @OnClick(R.id.ivDelete)
        public void onClick() {
            removeItem(getAdapterPosition());
        }

        private void removeItem(int adapterPosition) {
            if (adapterPosition < timeModelList.size()) {
                timeModelList.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);
                notifyItemRangeChanged(adapterPosition, timeModelList.size());
            }
        }
    }


    public String getOverTimeList() {
        List<TimeModel> list = new ArrayList<>();
        for (StandardBreaks breaks : timeModelList) {
            String startTime = CommonMethods.convertDateUK(CommonMethods.DF_9, breaks.getStart_time(), CommonMethods.DF_9);
            String endTime = CommonMethods.convertDateUK(CommonMethods.DF_9, breaks.getEnd_time(), CommonMethods.DF_9);
            list.add(new TimeModel(startTime, endTime));
//            list.add(new TimeModel(breaks.getStart_time(), breaks.getEnd_time()));
        }
        Log.e("overtimeList",new Gson().toJson(list));
        return new Gson().toJson(list);
    }


    public String getBreakTimeList() {
        List<TimeModel> list = new ArrayList<>();
        for (StandardBreaks breaks : timeModelList) {
            if ((null == breaks.getProject_id() || breaks.getProject_id().isEmpty()) && breaks.isChecked()) {
                String startTime = CommonMethods.convertDateUK(CommonMethods.DF_9, breaks.getStart_time(), CommonMethods.DF_9);
                String endTime = CommonMethods.convertDateUK(CommonMethods.DF_9, breaks.getEnd_time(), CommonMethods.DF_9);
                list.add(new TimeModel(startTime, endTime));
//                list.add(new TimeModel(breaks.getStart_time(), breaks.getEnd_time()));
            }
        }
        Log.e("breakList",new Gson().toJson(list));
        return new Gson().toJson(list);
    }


    public String getSelectedList() {
        List<String> idList = new ArrayList<>();
        for (StandardBreaks breaks : timeModelList) {
            if (null != breaks.getProject_id() && !breaks.getProject_id().isEmpty() && breaks.isChecked()) {
                idList.add(breaks.getId());
            }
        }
        return CommonMethods.getCommaSeparatedString(idList);
    }

}
