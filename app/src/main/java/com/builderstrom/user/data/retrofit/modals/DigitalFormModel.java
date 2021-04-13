package com.builderstrom.user.data.retrofit.modals;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DigitalFormModel extends BaseApiModel {
    private List<TemplateData> data = null;
    private DigitalDocHeader dataHeader;
    private Integer deleteRowID;
    private Boolean isStagedDocument;

    public DigitalDocHeader getDataHeader() {
        return dataHeader;
    }

    public List<TemplateData> getData() {
        return data;
    }

    public Integer getDeleteRowID() {
        return deleteRowID;
    }

    public void setDeleteRowID(Integer deleteRowID) {
        this.deleteRowID = deleteRowID;
    }

    public Boolean getStaged() {
        return isStagedDocument;
    }

    public void setStaged(Boolean staged) {
        isStagedDocument = staged;
    }

    public class DigitalDocHeader {

        private String template_header = "";
        private String template_footer = "";

        public String getTemplate_header() {
            return template_header;
        }

        public String getTemplate_footer() {
            return template_footer;
        }
    }

    public class ColumnData {
        @SerializedName("label")
        @Expose
        private String label;
        @SerializedName("label_pos")
        @Expose
        private String labelPos;
        @SerializedName("label_width")
        @Expose
        private String labelWidth;
        @SerializedName("control_width")
        @Expose
        private String controlWidth;
        @SerializedName("options")
        @Expose
        private List<Option> options = null;
        @SerializedName("is_required")
        @Expose
        private Integer isRequired;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("value")
        @Expose
        private String value;
        @SerializedName("heading_type")
        @Expose
        private String headingType;
        /////////////////////////////////////////////////////////////////////////


        @SerializedName("label_2")
        @Expose
        private String label2;
        @SerializedName("result_label")
        @Expose
        private String resultLabel;
        @SerializedName("maximum_length_val")
        @Expose
        private String maximumLengthVal;
        @SerializedName("minimum_length_val")
        @Expose
        private String minimumLengthVal;

        @SerializedName("options2")
        @Expose
        private List<Option> options2 = null;
        @SerializedName("color_number")
        @Expose
        private List<String> colorNumber = null;
        @SerializedName("color_codes")
        @Expose
        private List<String> colorCodes = null;
        @SerializedName("value1")
        @Expose
        private String value1;
        @SerializedName("value2")
        @Expose
        private String value2;
        @SerializedName("result")
        @Expose
        private String result;
        @SerializedName("default_answer")
        @Expose
        private String defaultAnswer;

        public String getLabel1() {
            return label;
        }

        public void setLabel1(String label) {
            this.label = label;
        }

        public String getLabel2() {
            return label2;
        }

        public void setLabel2(String label2) {
            this.label2 = label2;
        }

        public String getLabel3() {
            return resultLabel;
        }

        public void setLabel3(String label3) {
            this.resultLabel = label3;
        }

        public String getMaximumLengthVal() {
            return maximumLengthVal;
        }

        public void setMaximumLengthVal(String maximumLengthVal) {
            this.maximumLengthVal = maximumLengthVal;
        }

        public String getMinimumLengthVal() {
            return minimumLengthVal;
        }

        public void setMinimumLengthVal(String minimumLengthVal) {
            this.minimumLengthVal = minimumLengthVal;
        }

        public List<Option> getOptions1() {
            return options;
        }

        public void setOptions1(List<Option> options1) {
            this.options = options1;
        }

        public List<Option> getOptions2() {
            return options2;
        }

        public void setOptions2(List<Option> options2) {
            this.options2 = options2;
        }

        public List<String> getColorNumber() {
            return colorNumber;
        }

        public void setColorNumber(List<String> colorNumber) {
            this.colorNumber = colorNumber;
        }

        public List<String> getColorCodes() {
            return colorCodes;
        }

        public void setColorCodes(List<String> colorCodes) {
            this.colorCodes = colorCodes;
        }

        public String getValue1() {
            return value1;
        }

        public void setValue1(String value1) {
            this.value1 = value1;
        }

        public String getValue2() {
            return value2;
        }

        public void setValue2(String value2) {
            this.value2 = value2;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getDefaultAnswer() {
            return defaultAnswer;
        }

        public void setDefaultAnswer(String defaultAnswer) {
            this.defaultAnswer = defaultAnswer;
        }

        ////////////////////////////////////////////////////////////////////////
        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getLabelPos() {
            return labelPos;
        }

        public void setLabelPos(String labelPos) {
            this.labelPos = labelPos;
        }

        public String getLabelWidth() {
            return labelWidth;
        }

        public void setLabelWidth(String labelWidth) {
            this.labelWidth = labelWidth;
        }

        public String getControlWidth() {
            return controlWidth;
        }

        public void setControlWidth(String controlWidth) {
            this.controlWidth = controlWidth;
        }

        public List<Option> getOptions() {
            return options;
        }

        public void setOptions(List<Option> options) {
            this.options = options;
        }

        public Integer getIsRequired() {
            return isRequired;
        }

        public void setIsRequired(Integer isRequired) {
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

        public String getHeadingType() {
            return headingType;
        }

        public void setHeadingType(String headingType) {
            this.headingType = headingType;
        }
    }

    public class Rowcolumn {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("template_id")
        @Expose
        private String templateId;
        @SerializedName("row_id")
        @Expose
        private String rowId;
        @SerializedName("parent_column")
        @Expose
        private String parentColumn;
        @SerializedName("column_data")
        @Expose
        private ColumnData columnData;
        @SerializedName("old_col_id")
        @Expose
        private String oldColId;
        @SerializedName("old_parent_column_id")
        @Expose
        private String oldParentColumnId;
        @SerializedName("width")
        @Expose
        private Integer width;
        @SerializedName("column_value")
        @Expose
        private String columnValue;
        @SerializedName("isEditableColumn")
        @Expose
        private Boolean isEditableColumn = true;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTemplateId() {
            return templateId;
        }

        public void setTemplateId(String templateId) {
            this.templateId = templateId;
        }

        public String getRowId() {
            return rowId;
        }

        public void setRowId(String rowId) {
            this.rowId = rowId;
        }

        public String getParentColumn() {
            return parentColumn;
        }

        public void setParentColumn(String parentColumn) {
            this.parentColumn = parentColumn;
        }

        public ColumnData getColumnData() {
            return columnData;
        }

        public void setColumnData(ColumnData columnData) {
            this.columnData = columnData;
        }

        public String getOldColId() {
            return oldColId;
        }

        public void setOldColId(String oldColId) {
            this.oldColId = oldColId;
        }

        public String getOldParentColumnId() {
            return oldParentColumnId;
        }

        public void setOldParentColumnId(String oldParentColumnId) {
            this.oldParentColumnId = oldParentColumnId;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public String getColumnValue() {
            return columnValue;
        }

        public void setColumnValue(String columnValue) {
            this.columnValue = columnValue;
        }

        public Boolean getEditableColumn() {
            return isEditableColumn;
        }

        public void setEditableColumn(Boolean editableColumn) {
            isEditableColumn = editableColumn;
        }
    }

    public class Rowdata {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("template_id")
        @Expose
        private String templateId;
        @SerializedName("page")
        @Expose
        private String page;
        @SerializedName("row_order")
        @Expose
        private String rowOrder;
        @SerializedName("repeat_status")
        @Expose
        private String repeatStatus;
        @SerializedName("isEditableRow")
        @Expose
        private Boolean isEditableRow = true;
        @SerializedName("added_by")
        @Expose
        private String addedBy;
        @SerializedName("stage_id")
        @Expose
        private String stageId;
        @SerializedName("old_row_id")
        @Expose
        private String oldRowId;
        @SerializedName("old_stage_id")
        @Expose
        private String oldStageId;
        @SerializedName("added_on")
        @Expose
        private String addedOn;
        @SerializedName("is_repeat_data")
        @Expose
        private Boolean isRepeatData;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTemplateId() {
            return templateId;
        }

        public void setTemplateId(String templateId) {
            this.templateId = templateId;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getRowOrder() {
            return rowOrder;
        }

        public void setRowOrder(String rowOrder) {
            this.rowOrder = rowOrder;
        }

        public String getRepeatStatus() {
            return repeatStatus;
        }

        public void setRepeatStatus(String repeatStatus) {
            this.repeatStatus = repeatStatus;
        }

        public String getAddedBy() {
            return addedBy;
        }

        public void setAddedBy(String addedBy) {
            this.addedBy = addedBy;
        }

        public String getStageId() {
            return stageId;
        }

        public void setStageId(String stageId) {
            this.stageId = stageId;
        }

        public String getOldRowId() {
            return oldRowId;
        }

        public void setOldRowId(String oldRowId) {
            this.oldRowId = oldRowId;
        }

        public String getOldStageId() {
            return oldStageId;
        }

        public void setOldStageId(String oldStageId) {
            this.oldStageId = oldStageId;
        }

        public String getAddedOn() {
            return addedOn;
        }

        public void setAddedOn(String addedOn) {
            this.addedOn = addedOn;
        }

        public Boolean getEditableRow() {
            return isEditableRow;
        }

        public void setEditableRow(Boolean editableRow) {
            isEditableRow = editableRow;
        }

        public Boolean getRepeatData() {
            return isRepeatData;
        }

        public void setRepeatData(Boolean repeatData) {
            isRepeatData = repeatData;
        }
    }

    public class Option {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("isSelected")
        @Expose
        private boolean isSelected = false;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        @NonNull
        @Override
        public String toString() {
            return name;
        }
    }

}