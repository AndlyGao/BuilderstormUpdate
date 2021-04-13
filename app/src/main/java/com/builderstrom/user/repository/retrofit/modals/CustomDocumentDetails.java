package com.builderstrom.user.repository.retrofit.modals;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomDocumentDetails implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("original_document_name")
    @Expose
    private String originalDocumentName;
    @SerializedName("document_path")
    @Expose
    private String documentPath;
    @SerializedName("template_id")
    @Expose
    private String templateId;
    @SerializedName("revision")
    @Expose
    private String revision;
    @SerializedName("parent_document_id")
    @Expose
    private String parentDocumentId;
    @SerializedName("document_type")
    @Expose
    private String documentType;
    @SerializedName("project_id")
    @Expose
    private String projectId;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("added_by")
    @Expose
    private String addedBy;
    @SerializedName("date_added")
    @Expose
    private String dateAdded;
    @SerializedName("is_accessing")
    @Expose
    private String isAccessing;
    @SerializedName("accessing_by")
    @Expose
    private String accessingBy;
    @SerializedName("accecssed_time")
    @Expose
    private Object accecssedTime;
    @SerializedName("draft_status")
    @Expose
    private String draftStatus;
    @SerializedName("allowed_to")
    @Expose
    private String allowedTo;
    @SerializedName("last_modified_by")
    @Expose
    private String lastModifiedBy;
    @SerializedName("last_modified_on")
    @Expose
    private Object lastModifiedOn;
    @SerializedName("required_filled")
    @Expose
    private String requiredFilled;
    @SerializedName("notify_users")
    @Expose
    private Object notifyUsers;
    @SerializedName("issue_digital_doc_id")
    @Expose
    private Object issueDigitalDocId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalDocumentName() {
        return originalDocumentName;
    }

    public void setOriginalDocumentName(String originalDocumentName) {
        this.originalDocumentName = originalDocumentName;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getParentDocumentId() {
        return parentDocumentId;
    }

    public void setParentDocumentId(String parentDocumentId) {
        this.parentDocumentId = parentDocumentId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getIsAccessing() {
        return isAccessing;
    }

    public void setIsAccessing(String isAccessing) {
        this.isAccessing = isAccessing;
    }

    public String getAccessingBy() {
        return accessingBy;
    }

    public void setAccessingBy(String accessingBy) {
        this.accessingBy = accessingBy;
    }

    public Object getAccecssedTime() {
        return accecssedTime;
    }

    public void setAccecssedTime(Object accecssedTime) {
        this.accecssedTime = accecssedTime;
    }

    public String getDraftStatus() {
        return draftStatus;
    }

    public void setDraftStatus(String draftStatus) {
        this.draftStatus = draftStatus;
    }

    public String getAllowedTo() {
        return allowedTo;
    }

    public void setAllowedTo(String allowedTo) {
        this.allowedTo = allowedTo;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Object getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Object lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public String getRequiredFilled() {
        return requiredFilled;
    }

    public void setRequiredFilled(String requiredFilled) {
        this.requiredFilled = requiredFilled;
    }

    public Object getNotifyUsers() {
        return notifyUsers;
    }

    public void setNotifyUsers(Object notifyUsers) {
        this.notifyUsers = notifyUsers;
    }

    public Object getIssueDigitalDocId() {
        return issueDigitalDocId;
    }

    public void setIssueDigitalDocId(Object issueDigitalDocId) {
        this.issueDigitalDocId = issueDigitalDocId;
    }


    protected CustomDocumentDetails(Parcel in) {
        id = in.readString();
        originalDocumentName = in.readString();
        documentPath = in.readString();
        templateId = in.readString();
        revision = in.readString();
        parentDocumentId = in.readString();
        documentType = in.readString();
        projectId = in.readString();
        comments = in.readString();
        status = in.readString();
        addedBy = in.readString();
        dateAdded = in.readString();
        isAccessing = in.readString();
        accessingBy = in.readString();
        accecssedTime = (Object) in.readValue(Object.class.getClassLoader());
        draftStatus = in.readString();
        allowedTo = in.readString();
        lastModifiedBy = in.readString();
        lastModifiedOn = (Object) in.readValue(Object.class.getClassLoader());
        requiredFilled = in.readString();
        notifyUsers = (Object) in.readValue(Object.class.getClassLoader());
        issueDigitalDocId = (Object) in.readValue(Object.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(originalDocumentName);
        dest.writeString(documentPath);
        dest.writeString(templateId);
        dest.writeString(revision);
        dest.writeString(parentDocumentId);
        dest.writeString(documentType);
        dest.writeString(projectId);
        dest.writeString(comments);
        dest.writeString(status);
        dest.writeString(addedBy);
        dest.writeString(dateAdded);
        dest.writeString(isAccessing);
        dest.writeString(accessingBy);
        dest.writeValue(accecssedTime);
        dest.writeString(draftStatus);
        dest.writeString(allowedTo);
        dest.writeString(lastModifiedBy);
        dest.writeValue(lastModifiedOn);
        dest.writeString(requiredFilled);
        dest.writeValue(notifyUsers);
        dest.writeValue(issueDigitalDocId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CustomDocumentDetails> CREATOR = new Parcelable.Creator<CustomDocumentDetails>() {
        @Override
        public CustomDocumentDetails createFromParcel(Parcel in) {
            return new CustomDocumentDetails(in);
        }

        @Override
        public CustomDocumentDetails[] newArray(int size) {
            return new CustomDocumentDetails[size];
        }
    };
}