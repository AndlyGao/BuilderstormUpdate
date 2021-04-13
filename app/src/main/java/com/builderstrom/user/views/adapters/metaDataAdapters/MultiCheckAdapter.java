package com.builderstrom.user.views.adapters.metaDataAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.MetaOptions;

import java.util.List;

public class MultiCheckAdapter extends RecyclerView.Adapter<MultiCheckAdapter.CheckHolder> {

    private Context mContext;
    private List<MetaOptions> optionsList;
    private LayoutInflater mLayoutInflater;
    private UpdateCallBack callBack;
    private boolean isEditable = true;

    public MultiCheckAdapter(Context mContext, List<MetaOptions> optionsList, UpdateCallBack callBack) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.optionsList = optionsList;
        this.callBack = callBack;
    }

    public MultiCheckAdapter(Context mContext, List<MetaOptions> optionsList, UpdateCallBack callBack, Boolean isEditable) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.optionsList = optionsList;
        this.callBack = callBack;
        this.isEditable = isEditable;
    }


//    public MultiCheckAdapter(Context mContext, List<MetaOptions> optionsList) {
//        this.mContext = mContext;
//        mLayoutInflater = LayoutInflater.from(mContext);
//        this.optionsList = optionsList;
//    }

    @NonNull
    @Override
    public CheckHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CheckHolder(mLayoutInflater.inflate(R.layout.row_check_box, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CheckHolder checkHolder, int i) {
        checkHolder.checkBox.setChecked(optionsList.get(i).isSelected());
        checkHolder.checkBox.setText(optionsList.get(i).getOptionName());
        checkHolder.checkBox.setClickable(isEditable);
        if (isEditable) {
            checkHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                optionsList.get(i).setSelected(isChecked);
                if (callBack != null) {
                    callBack.updateOptions(optionsList);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    public void setCallBack(UpdateCallBack callBack) {
        this.callBack = callBack;
    }

    public List<MetaOptions> getOptionsUpdated() {
        return optionsList;
    }

    public interface UpdateCallBack {
        void updateOptions(List<MetaOptions> options);
    }

    class CheckHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;

        public CheckHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

}
