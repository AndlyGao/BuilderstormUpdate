package com.builderstrom.user.views.adapters.metaDataAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.MetaOptions;

import java.util.List;

public class RadioListAdapter extends RecyclerView.Adapter<RadioListAdapter.RadioHolder> {
    private Context mContext;
    private List<MetaOptions> optionsList;
    private LayoutInflater mLayoutInflater;
    private UpdateOptions updateOptions;
    private boolean isEditable = true;

    public RadioListAdapter(Context mContext, List<MetaOptions> optionsList, UpdateOptions updateOptions) {
        this.mContext = mContext;
        this.optionsList = optionsList;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.updateOptions = updateOptions;
    }

    public RadioListAdapter(Context mContext, List<MetaOptions> optionsList) {
        this.mContext = mContext;
        this.optionsList = optionsList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public RadioListAdapter(Context mContext, List<MetaOptions> optionsList, Boolean isEditable) {
        this.mContext = mContext;
        this.optionsList = optionsList;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.isEditable = isEditable;
    }

    @NonNull
    @Override
    public RadioHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RadioHolder(mLayoutInflater.inflate(R.layout.row_radio, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RadioHolder radioHolder, int i) {
        MetaOptions data = optionsList.get(i);
        radioHolder.ivRadio.setImageResource(data.isSelected() ? android.R.drawable.radiobutton_on_background : android.R.drawable.radiobutton_off_background);
        radioHolder.tvRadio.setText(data.getOptionName());
        if (isEditable) {
            radioHolder.constraintLayout.setOnClickListener(v -> updateOptionsCallback(i));
        }
    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    private void updateOptionsCallback(int adapterPosition) {
        for (MetaOptions data : optionsList) {
            data.setSelected(adapterPosition == optionsList.indexOf(data));
        }
//        if (updateOptions != null) {
//            updateOptions.updateOptions(optionsList);
//        }
        notifyDataSetChanged();
    }

    public interface UpdateOptions {
        void updateOptions(List<MetaOptions> optionsList);
    }

    class RadioHolder extends RecyclerView.ViewHolder {
        private TextView tvRadio;
        private ImageView ivRadio;
        private ConstraintLayout constraintLayout;

        RadioHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            tvRadio = itemView.findViewById(R.id.tvOption);
            ivRadio = itemView.findViewById(R.id.ivRadio);
        }
    }

}
