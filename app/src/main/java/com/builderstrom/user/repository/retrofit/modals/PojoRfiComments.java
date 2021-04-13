package com.builderstrom.user.repository.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PojoRfiComments {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<RfiComment> data = null;
    @SerializedName("datalimit")
    @Expose
    private Object datalimit;
    @SerializedName("authToken")
    @Expose
    private String authToken;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<RfiComment> getData() {
        return data;
    }

    public void setData(List<RfiComment> data) {
        this.data = data;
    }

    public Object getDatalimit() {
        return datalimit;
    }

    public void setDatalimit(Object datalimit) {
        this.datalimit = datalimit;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

}
