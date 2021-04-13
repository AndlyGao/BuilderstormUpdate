package com.builderstrom.user.data.retrofit.modals;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PDocsDataModel {
    @SerializedName("uid")
    @Expose
    private Integer uid;
    @SerializedName("project_uid")
    @Expose
    private String projectUid;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("category_uid")
    @Expose
    private String categoryUid;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("urlfile")
    @Expose
    private String urlFile;
    @SerializedName("original_name")
    @Expose
    private String originalName;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("parent_id")
    @Expose
    private String parentId;
    @SerializedName("status_id")
    @Expose
    private String statusID;
    @SerializedName("revision")
    @Expose
    private String revision;
    @SerializedName("custom_document_id")
    @Expose
    private Integer customDocumentId;
    @SerializedName("custom_template_id")
    @Expose
    private Integer customTemplateId;
    @SerializedName("signed_doc")
    @Expose
    private String signedDoc;
    @SerializedName("access_level")
    @Expose
    private String accessLevel;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("modified_on")
    @Expose
    private Object modifiedOn;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("is_private")
    @Expose
    private String isPrivate;
    @SerializedName("clone_from_cloud")
    @Expose
    private String cloneFromCloud;
    @SerializedName("doc_status")
    @Expose
    private String docStatus;
    @SerializedName("cloud_doc_id")
    @Expose
    private Object cloudDocId;
    @SerializedName("shared_doc_id")
    @Expose
    private String sharedDocId;
    @SerializedName("shared_section")
    @Expose
    private String sharedSection;
    @SerializedName("document_status")
    @Expose
    private String documentStatus;
    @SerializedName("document_category")
    @Expose
    private String documentCategory;
    @SerializedName("is_fav")
    @Expose
    private String isFav;
    @SerializedName("is_track")
    @Expose
    private String isTrack;
    @SerializedName("privateDoc")
    @Expose
    private String privateDoc;
    @SerializedName("isSigned")
    @Expose
    private String isSigned;
    @SerializedName("uploadername")
    @Expose
    private String uploadername;
    @SerializedName("pinComment")
    @Expose
    private String pincomment;
    @SerializedName("pinnedcomment")
    @Expose
    private PinnedComment pinnedcomment;
    @SerializedName("totalcomments")
    @Expose
    private Integer totalcomments;
    @SerializedName("customDocumentDetails")
    @Expose
    private CustomDocumentDetails customDocumentDetails;
    @SerializedName("custom_field_data")
    @Expose
    private List<Object> customFieldData = null;
    @SerializedName("forwordedDetails")
    @Expose
    private List<ForwordedDetail> forwordedDetails = null;
    private boolean isSynced = false;
    private boolean isOffline = false;
    private boolean isEdited = false;
    private Integer tableRowId;

    public PDocsDataModel(String uid, String title) {
        this.uid = Integer.parseInt(uid);
        this.title = title;
    }

    public PDocsDataModel() {
    }

    public PDocsDataModel(Integer uid, String title, String categoryUid, String documentCategory, String statusID, String revision, String isSigned, String note, String pincomment, String file, String filename, String createdOn, String userName, boolean isSynced, Integer tableRowId) {
        this.uid = uid;
        this.title = title;
        this.categoryUid = categoryUid;
        this.documentCategory = documentCategory;
        this.statusID = statusID;
        this.docStatus = statusID;
        this.revision = revision;
        this.signedDoc = isSigned;
        this.note = note;
        this.pincomment = pincomment;
        this.file = file;
        this.urlFile = file;
        this.originalName = filename;
        this.createdOn = createdOn;
        this.uploadername = userName;
        this.isSynced = isSynced;
        this.tableRowId = tableRowId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getProjectUid() {
        return projectUid;
    }

    public void setProjectUid(String projectUid) {
        this.projectUid = projectUid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryUid() {
        return categoryUid;
    }

    public void setCategoryUid(String categoryUid) {
        this.categoryUid = categoryUid;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getStatusID() {
        return statusID;
    }

    public void setStatusID(String statusID) {
        this.statusID = statusID;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public Integer getCustomDocumentId() {
        return customDocumentId;
    }

    public void setCustomDocumentId(Integer customDocumentId) {
        this.customDocumentId = customDocumentId;
    }

    public String getSignedDoc() {
        return signedDoc;
    }

    public void setSignedDoc(String signedDoc) {
        this.signedDoc = signedDoc;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public Object getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Object modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getCloneFromCloud() {
        return cloneFromCloud;
    }

    public void setCloneFromCloud(String cloneFromCloud) {
        this.cloneFromCloud = cloneFromCloud;
    }

    public String getDocStatus() {
        return docStatus;
    }

    public void setDocStatus(String docStatus) {
        this.docStatus = docStatus;
    }

    public Object getCloudDocId() {
        return cloudDocId;
    }

    public void setCloudDocId(Object cloudDocId) {
        this.cloudDocId = cloudDocId;
    }

    public String getSharedDocId() {
        return sharedDocId;
    }

    public void setSharedDocId(String sharedDocId) {
        this.sharedDocId = sharedDocId;
    }

    public String getSharedSection() {
        return sharedSection;
    }

    public void setSharedSection(String sharedSection) {
        this.sharedSection = sharedSection;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getDocumentCategory() {
        return documentCategory;
    }

    public void setDocumentCategory(String documentCategory) {
        this.documentCategory = documentCategory;
    }

    public String getIsFav() {
        return isFav;
    }

    public void setIsFav(String isFav) {
        this.isFav = isFav;
    }

    public String getIsTrack() {
        return isTrack;
    }

    public void setIsTrack(String isTrack) {
        this.isTrack = isTrack;
    }

    public String getPrivateDoc() {
        return privateDoc;
    }

    public void setPrivateDoc(String privateDoc) {
        this.privateDoc = privateDoc;
    }

    public String getIsSigned() {
        return isSigned;
    }

    public void setIsSigned(String isSigned) {
        this.isSigned = isSigned;
    }

    public String getUploadername() {
        return uploadername;
    }

    public void setUploadername(String uploadername) {
        this.uploadername = uploadername;
    }

    public String getPincomment() {
        return pincomment;
    }

    public void setPincomment(String pincomment) {
        this.pincomment = pincomment;
    }

    public PinnedComment getPinnedcomment() {
        return pinnedcomment;
    }

    public void setPinnedcomment(PinnedComment pinnedcomment) {
        this.pinnedcomment = pinnedcomment;
    }

    public Integer getTotalcomments() {
        return totalcomments;
    }

    public void setTotalcomments(Integer totalcomments) {
        this.totalcomments = totalcomments;
    }

    public CustomDocumentDetails getCustomDocumentDetails() {
        return customDocumentDetails;
    }

    public void setCustomDocumentDetails(CustomDocumentDetails customDocumentDetails) {
        this.customDocumentDetails = customDocumentDetails;
    }

    public List<Object> getCustomFieldData() {
        return customFieldData;
    }

    public void setCustomFieldData(List<Object> customFieldData) {
        this.customFieldData = customFieldData;
    }

    public List<ForwordedDetail> getForwordedDetails() {
        return forwordedDetails;
    }

    public void setForwordedDetails(List<ForwordedDetail> forwordedDetails) {
        this.forwordedDetails = forwordedDetails;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }

    public Integer getTableRowId() {
        return tableRowId;
    }

    public void setTableRowId(Integer tableRowId) {
        this.tableRowId = tableRowId;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    public Integer getCustomTemplateId() {
        return customTemplateId;
    }

    public void setCustomTemplateId(Integer customTemplateId) {
        this.customTemplateId = customTemplateId;
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }
}