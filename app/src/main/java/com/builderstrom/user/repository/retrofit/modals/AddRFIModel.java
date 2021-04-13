package com.builderstrom.user.repository.retrofit.modals;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class AddRFIModel {

    @SerializedName("ResponseCode")
    @Expose
    private Boolean responseCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("rfi_id")
    @Expose
    private Integer rfiId;

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

    public Integer getRfiId() {
        return rfiId;
    }

    public void setRfiId(Integer rfiId) {
        this.rfiId = rfiId;
    }

}
