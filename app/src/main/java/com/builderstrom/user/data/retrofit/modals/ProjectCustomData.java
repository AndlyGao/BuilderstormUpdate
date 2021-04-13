package com.builderstrom.user.data.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProjectCustomData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("custom_field_id")
    @Expose
    private Integer customFieldId;
    @SerializedName("check_input")
    @Expose
    private String checkInput;
    @SerializedName("section_primary_key")
    @Expose
    private Integer sectionPrimaryKey;
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
    @SerializedName("table_column")
    @Expose
    private Integer tableColumn;
    @SerializedName("file_name")
    @Expose
    private String fileName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getCustomFieldId() {
        return customFieldId;
    }

    public void setCustomFieldId(Integer customFieldId) {
        this.customFieldId = customFieldId;
    }

    public String getCheckInput() {
        return checkInput;
    }

    public void setCheckInput(String checkInput) {
        this.checkInput = checkInput;
    }

    public Integer getSectionPrimaryKey() {
        return sectionPrimaryKey;
    }

    public void setSectionPrimaryKey(Integer sectionPrimaryKey) {
        this.sectionPrimaryKey = sectionPrimaryKey;
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

    public Integer getTableColumn() {
        return tableColumn;
    }

    public void setTableColumn(Integer tableColumn) {
        this.tableColumn = tableColumn;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
