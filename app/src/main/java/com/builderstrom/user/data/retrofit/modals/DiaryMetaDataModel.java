package com.builderstrom.user.data.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DiaryMetaDataModel {
    @SerializedName("ResponseCode")
    @Expose
    private Boolean responseCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<MetaDataField> fields = null;

    public Boolean getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Boolean responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MetaDataField> getFields() {
        return fields;
    }

    public void setFields(List<MetaDataField> fields) {
        this.fields = fields;
    }
}
