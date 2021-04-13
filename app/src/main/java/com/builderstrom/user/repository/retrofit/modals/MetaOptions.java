package com.builderstrom.user.repository.retrofit.modals;


import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetaOptions implements Cloneable{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("custom_field_id")
    @Expose
    private String customFieldId;
    @SerializedName("option_name")
    @Expose
    private String optionName;
    @SerializedName("visibility")
    @Expose
    private String visibility;
    @SerializedName("view_order")
    @Expose
    private String viewOrder;

    private boolean isSelected = false;

    private String answerString = "";

    public MetaOptions() {
    }


    public MetaOptions clone() throws CloneNotSupportedException{
        MetaOptions metaOptions= (MetaOptions) super.clone();
        metaOptions.isSelected=false;
        metaOptions.answerString="";
        return metaOptions;

    }

    public MetaOptions(String optionName, boolean isSelected) {
        this.optionName = optionName;
        this.isSelected = isSelected;
    }


    public MetaOptions(String id, String optionName, boolean isSelected) {
        this.id = id;
        this.optionName = optionName;
        this.isSelected = isSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomFieldId() {
        return customFieldId;
    }

    public void setCustomFieldId(String customFieldId) {
        this.customFieldId = customFieldId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getViewOrder() {
        return viewOrder;
    }

    public void setViewOrder(String viewOrder) {
        this.viewOrder = viewOrder;
    }

    @NonNull
    @Override
    public String toString() {
        return optionName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getAnswerString() {
        return answerString;
    }

    public void setAnswerString(String answerString) {
        this.answerString = answerString;
    }
}
