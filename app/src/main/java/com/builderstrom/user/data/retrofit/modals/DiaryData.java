package com.builderstrom.user.data.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DiaryData {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("fullname")
    @Expose
    private String username;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("project_id")
    @Expose
    private String projectId;
    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("child_id")
    @Expose
    private String childId;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("submitted_on")
    @Expose
    private String submittedOn;
    @SerializedName("values_from")
    @Expose
    private String valuesFrom;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("project_man_hrs")
    @Expose
    private String projectManHrs;
    @SerializedName("attachments")
    @Expose
    private List<AddAttachModel> attachments = null;
    @SerializedName("diarymanhours")
    @Expose
    private List<DiaryManLabour> diarymanhours = null;
    @SerializedName("dairyplants")
    @Expose
    private List<Object> dairyplants = null;
/*
    @SerializedName("custom_field_data")
    @Expose
    private List<MetaValues> custom_field_data = null;*/

    @SerializedName("custom_field_data")
    @Expose
    private List<List<MetaValues>> custom_field_data = null;
    @SerializedName("custom_field")
    @Expose
    private List<LocalMetaValues> localMetaData = null;


    public List<LocalMetaValues> getLocalMetaData() {
        return localMetaData;
    }

    public void setLocalMetaData(List<LocalMetaValues> localMetaData) {
        this.localMetaData = localMetaData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getSubmittedOn() {
        return submittedOn;
    }

    public void setSubmittedOn(String submittedOn) {
        this.submittedOn = submittedOn;
    }

    public String getValuesFrom() {
        return valuesFrom;
    }

    public void setValuesFrom(String valuesFrom) {
        this.valuesFrom = valuesFrom;
    }

    public String getProjectManHrs() {
        return projectManHrs;
    }

    public void setProjectManHrs(String projectManHrs) {
        this.projectManHrs = projectManHrs;
    }

    public List<AddAttachModel> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AddAttachModel> attachments) {
        this.attachments = attachments;
    }

    public List<DiaryManLabour> getDiaryManHours() {
        return diarymanhours;
    }

    public void setDiarymanhours(List<DiaryManLabour> diaryManHours) {
        this.diarymanhours = diaryManHours;
    }

    public List<Object> getDairyplants() {
        return dairyplants;
    }

    public void setDairyplants(List<Object> dairyplants) {
        this.dairyplants = dairyplants;
    }


    public List<List<MetaValues>> getCustom_field_data() {
        return custom_field_data;
    }

    public void setCustom_field_data(List<List<MetaValues>> custom_field_data) {
        this.custom_field_data = custom_field_data;
    }
}
