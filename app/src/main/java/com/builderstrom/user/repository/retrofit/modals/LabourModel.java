package com.builderstrom.user.repository.retrofit.modals;

public class LabourModel {
    String fieldType = "";
    String fieldValue = "";

    public LabourModel(String fieldType, String fieldValue) {
        this.fieldType = fieldType;
        this.fieldValue = fieldValue;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
