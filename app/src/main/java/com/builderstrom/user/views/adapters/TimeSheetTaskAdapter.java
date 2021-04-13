package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.TaskListing;
import java.util.ArrayList;
import java.util.List;

public class TimeSheetTaskAdapter extends RecyclerView.Adapter<TimeSheetTaskAdapter.MyViewHolder> {

    List<TaskListing> taskList;
    private Context context;

    public TimeSheetTaskAdapter(Context context, List<TaskListing> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @Override
    public TimeSheetTaskAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_check_box, parent, false);
        return new TimeSheetTaskAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TimeSheetTaskAdapter.MyViewHolder holder, int position) {
        TaskListing model = taskList.get(position);
        holder.checkBox.setText(model.getTaskTitle());
        holder.checkBox.setChecked(taskList.get(position).isSelected());
        holder.checkBox.setOnCheckedChangeListener(
                (buttonView, isChecked) -> taskList.get(position).setSelected(isChecked));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.checkBox);
        }
    }

    public List<String> selectedTaskID() {
        List<String> taskID = new ArrayList<>();
        for (TaskListing task : taskList) {
            if (task.isSelected()) {
                taskID.add(task.getId());
            }

        }
        return taskID;
    }

    public List<String> UnselectAllID() {
        List<String> taskID = new ArrayList<>();
        for (TaskListing task : taskList) {
            if (task.isSelected()) {
                task.setSelected(false);
                notifyDataSetChanged();
            }

        }
        return taskID;
    }
}
