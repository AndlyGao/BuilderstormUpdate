package com.builderstrom.user.data.retrofit.modals;

public class PojoCostCode {
    private String id = "";
    private String cost_code = "";
    private String is_category = "";

    public String getId() {
        return id;
    }

    public String getCost_code() {
        return cost_code;
    }

    public String getIs_category() {
        return is_category;
    }

    @Override public String toString() {
        return cost_code;
    }
}
