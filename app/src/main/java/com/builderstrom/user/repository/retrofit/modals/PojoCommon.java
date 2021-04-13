package com.builderstrom.user.repository.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PojoCommon {


    @SerializedName("ResponseCode")
    @Expose
    private Boolean responseCode;
    @SerializedName("Message")
    @Expose
    private String message;

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
}
