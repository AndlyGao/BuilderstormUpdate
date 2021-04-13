package com.builderstrom.user.repository.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RowFormModel implements Cloneable {
    @SerializedName("row_id")
    @Expose
    private String rowID = "";
    @SerializedName("column_id")
    @Expose
    private String columnID = "";
    @SerializedName("is_required")
    @Expose
    private String isRequired = "0";
    @SerializedName("type")
    @Expose
    private String type = "";
    @SerializedName("value")
    @Expose
    private String value = "";
    @SerializedName("label")
    @Expose
    private String label = "";
    @SerializedName("heading_type")
    @Expose
    private String heading_type = "";
    @SerializedName("AnswerString")
    @Expose
    private String AnswerString = "";
    @SerializedName("UploadFiles")
    @Expose
    private List<String> metaUploadFiles = null;
    @SerializedName("options")
    @Expose
    private List<MetaOptions> options = null;
    @SerializedName("image_file")
    @Expose
    private File imageFile = null;
    @SerializedName("colors")
    @Expose
    private List<String> color = null;
    @SerializedName("color_value")
    @Expose
    private List<String> colorValue = null;
    private String positionTag = "";
    private String colorCode = "";


    private boolean repeatColumn = false;

    private boolean isDeletable = false;
    private boolean isEditableColumn = true;

    public RowFormModel() {
    }


    public RowFormModel(String rowID, String columnID, String isRequired, String type, String value,
                        String label, String heading_type, List<MetaOptions> options) {
        this.rowID = rowID;
        this.columnID = columnID;
        this.isRequired = isRequired;
        this.type = type;
        this.value = value;
        this.label = label;
        this.heading_type = heading_type;
        this.options = options;
    }


    public RowFormModel(String rowID, String columnID, String isRequired, String type, String value,
                        String label, String heading_type, boolean isDeletable, List<MetaOptions> options) {
        this.rowID = rowID;
        this.columnID = columnID;
        this.isRequired = isRequired;
        this.type = type;
        this.value = value;
        this.label = label;
        this.heading_type = heading_type;
        this.isDeletable = isDeletable;
        this.options = options;
    }

    public RowFormModel clone() throws CloneNotSupportedException {
        RowFormModel formModel = (RowFormModel) super.clone();
        formModel.setRowID(this.rowID);
        formModel.setColumnID(this.columnID);
        formModel.setIsRequired(this.isRequired);
        formModel.setType(this.type);
        formModel.setValue(this.value);
        formModel.setLabel(this.label);
        formModel.setHeading_type(this.heading_type);
        formModel.setAnswerString("");
        formModel.setMetaUploadFiles(null);
        formModel.setImageFile(null);
        formModel.setColor(this.color);
        formModel.setColorValue(this.colorValue);
        if (options != null && !options.isEmpty()) {
            List<MetaOptions> metaOptions = new ArrayList<>();
            for (MetaOptions optionTest : options) {
                metaOptions.add(optionTest.clone());
            }
            formModel.setOptions(metaOptions);
        } else formModel.setOptions(null);
//        if (color != null && !color.isEmpty()) {
//            List<String> colorCode = new ArrayList<>();
//            for (String code : color) {
////                colorCode.add(code.getClass());
//            }
//            formModel.setColor(colorCode);
//        } else formModel.setOptions(null);
        return formModel;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public String getRowID() {
        return rowID;
    }

    public void setRowID(String rowID) {
        this.rowID = rowID;
    }

    public String getColumnID() {
        return columnID;
    }

    public void setColumnID(String columnID) {
        this.columnID = columnID;
    }

    public String getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(String isRequired) {
        this.isRequired = isRequired;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getHeading_type() {
        return heading_type;
    }

    public void setHeading_type(String heading_type) {
        this.heading_type = heading_type;
    }

    public String getAnswerString() {
        return AnswerString;
    }

    public void setAnswerString(String answerString) {
        AnswerString = answerString;
    }

    public List<String> getMetaUploadFiles() {
        return metaUploadFiles;
    }

    public void setMetaUploadFiles(List<String> metaUploadFiles) {
        this.metaUploadFiles = metaUploadFiles;
    }

    public List<MetaOptions> getOptions() {
        return options;
    }

    public void setOptions(List<MetaOptions> options) {
        this.options = options;
    }


    public boolean isRepeatColumn() {
        return repeatColumn;
    }

    public void setRepeatColumn(boolean repeatColumn) {
        this.repeatColumn = repeatColumn;
    }

    public boolean isDeletable() {
        return isDeletable;
    }

    public void setDeletable(boolean deletable) {
        isDeletable = deletable;
    }

    public String getPositionTag() {
        return positionTag;
    }

    public void setPositionTag(String positionTag) {
        this.positionTag = positionTag;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public boolean isEditableColumn() {
        return isEditableColumn;
    }

    public void setEditableColumn(boolean editableColumn) {
        isEditableColumn = editableColumn;
    }

    public List<String> getColor() {
        return color;
    }

    public void setColor(List<String> color) {
        this.color = color;
    }

    public List<String> getColorValue() {
        return colorValue;
    }

    public void setColorValue(List<String> colorValue) {
        this.colorValue = colorValue;
    }
}
