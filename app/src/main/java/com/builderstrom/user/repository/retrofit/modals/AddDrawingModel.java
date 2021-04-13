package com.builderstrom.user.repository.retrofit.modals;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class AddDrawingModel {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
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

    public class Data {

        @SerializedName("drawingid")
        @Expose
        private Integer drawingid;

        public Integer getDrawingid() {
            return drawingid;
        }

        public void setDrawingid(Integer drawingid) {
            this.drawingid = drawingid;
        }

    }

}


