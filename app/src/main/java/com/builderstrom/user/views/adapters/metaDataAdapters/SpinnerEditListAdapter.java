package com.builderstrom.user.views.adapters.metaDataAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.MetaOptions;

import java.util.List;

public class SpinnerEditListAdapter extends RecyclerView.Adapter<SpinnerEditListAdapter.SpinnerEditHolder> {

    private Context mContext;
    private List<MetaOptions> optionsList;
    private LayoutInflater mLayoutInflater;


    public SpinnerEditListAdapter(Context mContext, List<MetaOptions> optionsList) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.optionsList = optionsList;
    }

    @NonNull
    @Override
    public SpinnerEditHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SpinnerEditHolder(mLayoutInflater.inflate(R.layout.row_inner_spinner_edit, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SpinnerEditHolder spinnerEditHolder, int i) {
        spinnerEditHolder.checkBox.setText(optionsList.get(i).getOptionName());

    }


    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    class SpinnerEditHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;

        public SpinnerEditHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
