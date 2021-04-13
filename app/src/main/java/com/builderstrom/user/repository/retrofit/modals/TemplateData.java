package com.builderstrom.user.repository.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TemplateData  {
    @SerializedName("rowdata")
    @Expose
    private DigitalFormModel.Rowdata rowData;
    @SerializedName("rowcolumns")
    @Expose
    private List<DigitalFormModel.Rowcolumn> rowColumns = null;

    private List<RowFormModel> formModelList = null;

    private boolean isDeletable = false;


    public TemplateData(DigitalFormModel.Rowdata rowData, List<DigitalFormModel.Rowcolumn> rowColumns, boolean isDeletable) {
        this.rowData = rowData;
        this.rowColumns = rowColumns;
        this.isDeletable = isDeletable;
    }

    public TemplateData(DigitalFormModel.Rowdata rowData, List<DigitalFormModel.Rowcolumn> rowColumns, List<RowFormModel> formModelList, boolean isDeletable) {
        this.rowData = rowData;
        this.rowColumns = rowColumns;
        this.formModelList = formModelList;
        this.isDeletable = isDeletable;
    }

    public DigitalFormModel.Rowdata getRowData() {
        return rowData;
    }

    public void setRowdata(DigitalFormModel.Rowdata rowData) {
        this.rowData = rowData;
    }

    public List<DigitalFormModel.Rowcolumn> getRowColumns() {
        return rowColumns;
    }

    public void setRowcolumns(List<DigitalFormModel.Rowcolumn> rowColumns) {
        this.rowColumns = rowColumns;
    }

    public boolean isDeletable() {
        return isDeletable;
    }

    public void setDeletable(boolean deletable) {
        isDeletable = deletable;
    }

    public List<RowFormModel> getFormModelList() {
        return formModelList;
    }

    public void setFormModelList(List<RowFormModel> formModelList) {
        this.formModelList = formModelList;
    }
}
