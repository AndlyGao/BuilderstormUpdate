package com.builderstrom.user.data.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.List;

public class MetaDataField {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("section_name")
    @Expose
    private String sectionName;
    @SerializedName("field_label")
    @Expose
    private String fieldLabel;
    @SerializedName("field_type")
    @Expose
    private String fieldType;
    @SerializedName("visible")
    @Expose
    private String visible;
    @SerializedName("show_in_view")
    @Expose
    private String showInView;
    @SerializedName("show_order")
    @Expose
    private String showOrder;
    @SerializedName("is_required")
    @Expose
    private String isRequired;
    @SerializedName("is_deletable")
    @Expose
    private String isDeletable;
    @SerializedName("table_column")
    @Expose
    private String tableColumn;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("options")
    @Expose
    private List<MetaOptions> options = null;

    private String answerString = "";

    private File signFile;

    private List<String> metaUploadFiles;

    private List<String> metaServerFiles;

    public String getId() {
        return id;
    }

    public void setId(int id) {
        this.id = String.valueOf(id);
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getShowInView() {
        return showInView;
    }

    public void setShowInView(String showInView) {
        this.showInView = showInView;
    }

    public String getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(String showOrder) {
        this.showOrder = showOrder;
    }

    public String getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(String isRequired) {
        this.isRequired = isRequired;
    }

    public String getIsDeletable() {
        return isDeletable;
    }

    public void setIsDeletable(String isDeletable) {
        this.isDeletable = isDeletable;
    }

    public String getTableColumn() {
        return tableColumn;
    }

    public void setTableColumn(String tableColumn) {
        this.tableColumn = tableColumn;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public List<MetaOptions> getOptions() {
        return options;
    }

    public void setOptions(List<MetaOptions> options) {
        this.options = options;
    }

    public String getAnswerString() {
        return answerString;
    }

    public void setAnswerString(String answerString) {
        this.answerString = answerString;
    }

    public File getSignFile() {
        return signFile;
    }

    public void setSignFile(File signFile) {
        this.signFile = signFile;
    }

    public List<String> getMetaUploadFiles() {
        return metaUploadFiles;
    }

    public void setMetaUploadFiles(List<String> metaUploadFiles) {
        this.metaUploadFiles = metaUploadFiles;
    }

    @Override
    public String toString() {
        return id;
    }

    public List<String> getMetaServerFiles() {
        return metaServerFiles;
    }

    public void setMetaServerFiles(List<String> metaServerFiles) {
        this.metaServerFiles = metaServerFiles;
    }
}
