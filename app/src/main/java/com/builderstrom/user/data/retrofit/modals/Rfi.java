package com.builderstrom.user.data.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Rfi {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("u_id")
    @Expose
    private String uId;
    @SerializedName("p_id")
    @Expose
    private String pId;
    @SerializedName("p_r_id")
    @Expose
    private String pRId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("duedate")
    @Expose
    private String duedate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("is_draft")
    @Expose
    private Integer isDraft;
    @SerializedName("completed_on")
    @Expose
    private String completedOn;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("requested_by_id")
    @Expose
    private String requestedById;
    @SerializedName("requestBy")
    @Expose
    private String requestBy;
    @SerializedName("requestByLastname")
    @Expose
    private String requestByLastname;
    @SerializedName("is_answered")
    @Expose
    private Boolean isAnswered;
    @SerializedName("show_status")
    @Expose
    private Integer showStatus;
    @SerializedName("is_synced")
    @Expose
    private Boolean isSynced = false;
    @SerializedName("attachments")
    @Expose
    private List<PojoAttachment> attachments;
    @SerializedName("offline_attachments")
    @Expose
    private List<String> files;
    private String offlineFiles = "";
    @SerializedName("answer")
    @Expose
    private String answer;
    @SerializedName("answered_on")
    @Expose
    private String answered_on;
    @SerializedName("answered_by")
    @Expose
    private String answered_by;
    @SerializedName("answered_by_id")
    @Expose
    private String answered_by_id;
    @SerializedName("ansId")
    @Expose
    private String ansId;
    @SerializedName("answer_id")
    @Expose
    private String answer_id;

    @SerializedName("can_answer")
    @Expose
    private Boolean can_answer;
    @SerializedName("can_comment")
    @Expose
    private Boolean can_comment;

    @SerializedName("custom_field_data")
    @Expose
    private List<List<MetaValues>> custom_field_data = null;

    private List<User> tousers = null;
    private List<User> ccusers = null;

    private List<String> answerAttachmentFiles = null;

    private List<PojoAttachment> answerfiles = null;

    private String answerText = null;


    private boolean isOpened = false;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public String getProjectId() {
        return pId;
    }

    public void setPId(String pId) {
        this.pId = pId;
    }

    public String getPRId() {
        return pRId;
    }

    public void setPRId(String pRId) {
        this.pRId = pRId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(Integer isDraft) {
        this.isDraft = isDraft;
    }

    public String getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(String completedOn) {
        this.completedOn = completedOn;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getRequestedById() {
        return requestedById;
    }

    public void setRequestedById(String requestedById) {
        this.requestedById = requestedById;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(String requestBy) {
        this.requestBy = requestBy;
    }

    public String getRequestByLastname() {
        return requestByLastname;
    }

    public void setRequestByLastname(String requestByLastname) {
        this.requestByLastname = requestByLastname;
    }

    public Boolean getIsAnswered() {
        return isAnswered;
    }

    public void setIsAnswered(Boolean isAnswered) {
        this.isAnswered = isAnswered;
    }

    public Integer getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(Integer showStatus) {
        this.showStatus = showStatus;
    }

    public Boolean getSynced() {
        return isSynced;
    }

    public void setSynced(Boolean synced) {
        isSynced = synced;
    }

    public String getOfflineFiles() {
        return offlineFiles;
    }

    public void setOfflineFiles(String offlineFiles) {
        this.offlineFiles = offlineFiles;
    }

    public List<PojoAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<PojoAttachment> attachments) {
        this.attachments = attachments;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswered_on() {
        return answered_on;
    }

    public void setAnswered_on(String answered_on) {
        this.answered_on = answered_on;
    }

    public String getAnswered_by() {
        return answered_by;
    }

    public void setAnswered_by(String answered_by) {
        this.answered_by = answered_by;
    }

    public String getAnswered_by_id() {
        return answered_by_id;
    }

    public void setAnswered_by_id(String answered_by_id) {
        this.answered_by_id = answered_by_id;
    }

    public String getAnsId() {
        return ansId;
    }

    public void setAnsId(String ansId) {
        this.ansId = ansId;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public Boolean getCan_answer() {
        return can_answer;
    }

    public void setCan_answer(Boolean can_answer) {
        this.can_answer = can_answer;
    }

    public Boolean getCan_comment() {
        return can_comment;
    }

    public void setCan_comment(Boolean can_comment) {
        this.can_comment = can_comment;
    }

    public List<User> getTousers() {
        return tousers;
    }

    public void setTousers(List<User> tousers) {
        this.tousers = tousers;
    }

    public List<User> getCcusers() {
        return ccusers;
    }

    public void setCcusers(List<User> ccusers) {
        this.ccusers = ccusers;
    }


    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public List<String> getAnswerAttachmentFiles() {
        return answerAttachmentFiles;
    }

    public void setAnswerAttachmentFiles(List<String> answerAttachmentFiles) {
        this.answerAttachmentFiles = answerAttachmentFiles;
    }

    public List<PojoAttachment> getAnswerfiles() {
        return answerfiles;
    }

    public void setAnswerfiles(List<PojoAttachment> answerfiles) {
        this.answerfiles = answerfiles;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }


    public List<List<MetaValues>> getCustom_field_data() {
        return custom_field_data;
    }

    public void setCustom_field_data(List<List<MetaValues>> custom_field_data) {
        this.custom_field_data = custom_field_data;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
