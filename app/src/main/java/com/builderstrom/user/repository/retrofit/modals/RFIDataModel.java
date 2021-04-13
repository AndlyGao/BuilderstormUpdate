package com.builderstrom.user.repository.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RFIDataModel {
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
    private Datalimit datalimit;
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

    public Datalimit getDatalimit() {
        return datalimit;
    }

    public void setDatalimit(Datalimit datalimit) {
        this.datalimit = datalimit;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public class Data {

        @SerializedName("rfis")
        @Expose
        private List<Rfi> rfis = null;
        @SerializedName("rfitotals")
        @Expose
        private List<Rfitotal> rfitotals = null;

        public List<Rfi> getRfis() {
            return rfis;
        }

        public void setRfis(List<Rfi> rfis) {
            this.rfis = rfis;
        }

        public List<Rfitotal> getRfitotals() {
            return rfitotals;
        }

        public void setRfitotals(List<Rfitotal> rfitotals) {
            this.rfitotals = rfitotals;
        }

    }
    public class Datalimit {

        @SerializedName("offset")
        @Expose
        private Integer offset;
        @SerializedName("limit")
        @Expose
        private Integer limit;

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }

    }


}
