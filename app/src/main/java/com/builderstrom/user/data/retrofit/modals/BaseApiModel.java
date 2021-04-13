package com.builderstrom.user.data.retrofit.modals;

import androidx.annotation.Keep;

@Keep
public abstract class BaseApiModel {

    protected boolean success = false;

    protected String message = "";

    protected DataLimit datalimit;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataLimit getDataLimit() {
        return datalimit;
    }
}
