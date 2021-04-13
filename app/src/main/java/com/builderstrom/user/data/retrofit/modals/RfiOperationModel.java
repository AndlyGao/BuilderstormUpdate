package com.builderstrom.user.data.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RfiOperationModel {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("ResponseCode")
    @Expose
    private Boolean responseCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Boolean responseCode) {
        this.responseCode = responseCode;
    }
}
