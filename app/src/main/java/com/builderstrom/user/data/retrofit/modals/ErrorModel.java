package com.builderstrom.user.data.retrofit.modals;

import com.builderstrom.user.data.retrofit.api.DataNames;

public class ErrorModel {

    private Integer error_Type;
    private Exception exception;
    private Throwable throwable;
    private String message;

    public ErrorModel(Integer error_Type, Exception exception, Throwable throwable, String message) {
        this.error_Type = error_Type;
        this.exception = exception;
        this.throwable = throwable;
        this.message = message;

    }

    public ErrorModel(Integer error_Type, String message) {
        this.error_Type = error_Type;
        this.message = message;
    }

    public ErrorModel(Exception exception, String message) {
        this.error_Type = DataNames.TYPE_ERROR_EXCEPTION;
        this.exception = exception;
        this.message = message;
    }

    public ErrorModel(Throwable throwable, String message) {
        this.error_Type = DataNames.TYPE_ERROR_FAILURE;
        this.throwable = throwable;
        this.message = message;
    }

    public Integer getError_Type() {
        return error_Type;
    }

    public void setError_Type(Integer error_Type) {
        this.error_Type = error_Type;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
