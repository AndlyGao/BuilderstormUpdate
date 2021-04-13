package com.builderstrom.user.repository.retrofit.modals;

import java.util.List;

public class PojoSnagList {

    private Boolean ResponseCode;
    private String Message;
    private List<SnagTotal> snagTotals = null;
    private List<Snag> snags = null;

    public Boolean getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(Boolean ResponseCode) {
        this.ResponseCode = ResponseCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public List<SnagTotal> getSnagTotals() {
        return snagTotals;
    }

    public void setSnagTotals(List<SnagTotal> snagTotals) {
        this.snagTotals = snagTotals;
    }

    public List<Snag> getSnags() {
        return snags;
    }

    public void setSnags(List<Snag> snags) {
        this.snags = snags;
    }

}
