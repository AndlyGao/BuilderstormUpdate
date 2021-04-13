package com.builderstrom.user.views.adapters.metaDataAdapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.MetaOptions;

import java.util.List;

public class CheckEditListAdapter extends RecyclerView.Adapter<CheckEditListAdapter.CheckEditHolder> {

    private Context mContext;
    private List<MetaOptions> optionsList;
    private LayoutInflater mLayoutInflater;
    private UpdateOptions updateOptions;


    public CheckEditListAdapter(Context mContext, List<MetaOptions> optionsList) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.optionsList = optionsList;
    }

    public CheckEditListAdapter(Context mContext, List<MetaOptions> optionsList, UpdateOptions updateOptions) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.optionsList = optionsList;
        this.updateOptions = updateOptions;
    }

    @NonNull
    @Override
    public CheckEditHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CheckEditHolder(mLayoutInflater.inflate(R.layout.row_inner_check_edit, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CheckEditHolder checkEditHolder, int i) {
        MetaOptions option = optionsList.get(i);
        checkEditHolder.checkBox.setChecked(option.isSelected());
        checkEditHolder.checkBox.setText(option.getOptionName());
        checkEditHolder.editText.setText(option.getAnswerString() == null || option.getAnswerString().isEmpty() ? null : option.getAnswerString());
    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    class CheckEditHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private EditText editText;

        public CheckEditHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            editText = itemView.findViewById(R.id.editText);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    optionsList.get(getAdapterPosition()).setAnswerString(s.toString());
                    if (null != updateOptions) {
                        updateOptions.updateOptions(optionsList);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                optionsList.get(getAdapterPosition()).setSelected(isChecked);
//                notifyDataSetChanged();
                if (null != updateOptions) {
                    updateOptions.updateOptions(optionsList);
                }
            });
        }
    }

    public interface UpdateOptions {
        void updateOptions(List<MetaOptions> optionsList);
    }

}
