package com.builderstrom.user.repository.retrofit.modals;

import androidx.annotation.NonNull;

public class CompanyStatus {
    private String id;
    private String region_id;
    private String title;
    /* Company Status */
    private String status_type;
    private String created_on;

    /*Company Categories */
    private String parent_category;
    private String visibility;
    private String item_order;
    private Boolean child;

    public CompanyStatus(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus_type() {
        return status_type;
    }

    public void setStatus_type(String status_type) {
        this.status_type = status_type;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }


    public String getParent_category() {
        return parent_category;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getItem_order() {
        return item_order;
    }

    public Boolean getChild() {
        return child;
    }



    @NonNull
    @Override
    public String toString() {
        return title;
    }
}
