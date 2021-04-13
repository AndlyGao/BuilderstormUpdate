package com.builderstrom.user.data.retrofit.modals;

import java.util.List;

public class Snag {

    private String id = "";
    private String p_s_id = "";
    private String user_id = "";
    private String due_date = "";
    private String location_id = "";
    private String package_id = "";
    private String snag_status = "";
    private String project_id = "";
    private String defect_details = "";
    private String tags = "";
    private String sub_contrator = "";
    private String created_on = "";
    private String modified_on = "";
    private String requested_from = "";
    private List<User> tousers = null;
    private List<User> ccusers = null;
    private Integer can_comment;
    private List<PojoAttachment> attachments = null;
    private boolean selected = false;
    private boolean synced = false;
    private Integer show_status;
    private Integer can_mark;
    private String offlineFiles = "";
    private List<Integer> mark_as = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectSiteId() {
        return p_s_id;
    }

    public void setProjectSiteId(String p_s_id) {
        this.p_s_id = p_s_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDueDate() {
        return due_date;
    }

    public void setDueDate(String due_date) {
        this.due_date = due_date;
    }

    public String getLocationId() {
        return location_id;
    }

    public void setLocationId(String location_id) {
        this.location_id = location_id;
    }

    public String getPackageId() {
        return package_id;
    }

    public void setPackageId(String package_id) {
        this.package_id = package_id;
    }

    public String getSnag_status() {
        return snag_status;
    }

    public void setSnag_status(String snag_status) {
        this.snag_status = snag_status;
    }

    public String getProjectId() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getDefect_details() {
        return defect_details;
    }

    public void setDefect_details(String defect_details) {
        this.defect_details = defect_details;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSubContrator() {
        return sub_contrator;
    }

    public void setSubContrator(String sub_contrator) {
        this.sub_contrator = sub_contrator;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getModifiedOn() {
        return modified_on;
    }

    public void setModifiedOn(String modified_on) {
        this.modified_on = modified_on;
    }

    public List<User> getToUsers() {
        return tousers;
    }

    public void setToUsers(List<User> tousers) {
        this.tousers = tousers;
    }

    public List<User> getCcUsers() {
        return ccusers;
    }

    public void setCcUsers(List<User> ccusers) {
        this.ccusers = ccusers;
    }

    public Integer getCanComment() {
        return can_comment;
    }

    public void setCanComment(Integer can_comment) {
        this.can_comment = can_comment;
    }

    public List<PojoAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<PojoAttachment> attachments) {
        this.attachments = attachments;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getRequestedFrom() {
        return requested_from;
    }

    public void setRequested_from(String requested_from) {
        this.requested_from = requested_from;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public String getOfflineFiles() {
        return offlineFiles;
    }

    public void setOfflineFiles(String offlineFiles) {
        this.offlineFiles = offlineFiles;
    }

    public Integer getShow_status() {
        return show_status;
    }

    public void setShow_status(Integer show_status) {
        this.show_status = show_status;
    }

    public List<Integer> getMark_as() {
        return mark_as;
    }

    public void setMark_as(List<Integer> mark_as) {
        this.mark_as = mark_as;
    }

    public Integer getCanMark() {
        return can_mark;
    }

    public void setCanMark(Integer can_mark) {
        this.can_mark = can_mark;
    }


}
