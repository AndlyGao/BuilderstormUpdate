package com.builderstrom.user.data.retrofit.modals;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChildDrawings implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("project_id")
    @Expose
    private String projectId;
    @SerializedName("company_id")
    @Expose
    private String companyId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("file_name")
    @Expose
    private String fileName;
    @SerializedName("original_name")
    @Expose
    private String originalName;
    @SerializedName("drawing_no")
    @Expose
    private String drawingNo;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("issue")
    @Expose
    private String issue;
    @SerializedName("revision")
    @Expose
    private String revision;
    @SerializedName("pdf_dwg")
    @Expose
    private String pdfDwg;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("modified_on")
    @Expose
    private String modifiedOn;
    @SerializedName("parent_id")
    @Expose
    private String parentId;
    @SerializedName("revision_status")
    @Expose
    private String revisionStatus;
    @SerializedName("access_level")
    @Expose
    private String accessLevel;
    @SerializedName("is_interactive")
    @Expose
    private String isInteractive;
    @SerializedName("quarantine")
    @Expose
    private String quarantine;
    @SerializedName("drawing_recipients")
    @Expose
    private String drawingRecipients;
    @SerializedName("is_private")
    @Expose
    private String isPrivate;
    @SerializedName("archive_status")
    @Expose
    private String archiveStatus;
    @SerializedName("row_position")
    @Expose
    private String rowPosition;
    @SerializedName("is_bimer_drawing")
    @Expose
    private String isBimerDrawing;
    @SerializedName("is_new_type")
    @Expose
    private String isNewType;
    @SerializedName("cloud_doc_id")
    @Expose
    private String cloudDocId;
    @SerializedName("archive_reason")
    @Expose
    private String archiveReason;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("status_title")
    @Expose
    private String statusTitle;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("is_fav")
    @Expose
    private String isFav;
    @SerializedName("is_track")
    @Expose
    private String isTrack;
    @SerializedName("pdflocation")
    @Expose
    private String pdflocation;
    @SerializedName("pdfname")
    @Expose
    private String pdfname;
    @SerializedName("dwglocation")
    @Expose
    private String dwglocation;
    @SerializedName("dwgname")
    @Expose
    private String dwgname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getPdfDwg() {
        return pdfDwg;
    }

    public void setPdfDwg(String pdfDwg) {
        this.pdfDwg = pdfDwg;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRevisionStatus() {
        return revisionStatus;
    }

    public void setRevisionStatus(String revisionStatus) {
        this.revisionStatus = revisionStatus;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getIsInteractive() {
        return isInteractive;
    }

    public void setIsInteractive(String isInteractive) {
        this.isInteractive = isInteractive;
    }

    public String getQuarantine() {
        return quarantine;
    }

    public void setQuarantine(String quarantine) {
        this.quarantine = quarantine;
    }

    public String getDrawingRecipients() {
        return drawingRecipients;
    }

    public void setDrawingRecipients(String drawingRecipients) {
        this.drawingRecipients = drawingRecipients;
    }

    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getArchiveStatus() {
        return archiveStatus;
    }

    public void setArchiveStatus(String archiveStatus) {
        this.archiveStatus = archiveStatus;
    }

    public String getRowPosition() {
        return rowPosition;
    }

    public void setRowPosition(String rowPosition) {
        this.rowPosition = rowPosition;
    }

    public String getIsBimerDrawing() {
        return isBimerDrawing;
    }

    public void setIsBimerDrawing(String isBimerDrawing) {
        this.isBimerDrawing = isBimerDrawing;
    }

    public String getIsNewType() {
        return isNewType;
    }

    public void setIsNewType(String isNewType) {
        this.isNewType = isNewType;
    }

    public String getCloudDocId() {
        return cloudDocId;
    }

    public void setCloudDocId(String cloudDocId) {
        this.cloudDocId = cloudDocId;
    }

    public String getArchiveReason() {
        return archiveReason;
    }

    public void setArchiveReason(String archiveReason) {
        this.archiveReason = archiveReason;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStatusTitle() {
        return statusTitle;
    }

    public void setStatusTitle(String statusTitle) {
        this.statusTitle = statusTitle;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public String getPdflocation() {
        return pdflocation;
    }

    public void setPdflocation(String pdflocation) {
        this.pdflocation = pdflocation;
    }

    public String getPdfname() {
        return pdfname;
    }

    public void setPdfname(String pdfname) {
        this.pdfname = pdfname;
    }

    public String getDwglocation() {
        return dwglocation;
    }

    public void setDwglocation(String dwglocation) {
        this.dwglocation = dwglocation;
    }

    public String getDwgname() {
        return dwgname;
    }

    public void setDwgname(String dwgname) {
        this.dwgname = dwgname;
    }


    protected ChildDrawings(Parcel in) {
        id = in.readString();
        projectId = in.readString();
        companyId = in.readString();
        userId = in.readString();
        categoryId = in.readString();
        name = in.readString();
        fileName = in.readString();
        originalName = in.readString();
        drawingNo = in.readString();
        tags = in.readString();
        status = in.readString();
        issue = in.readString();
        revision = in.readString();
        pdfDwg = in.readString();
        createdOn = in.readString();
        modifiedOn = in.readString();
        parentId = in.readString();
        revisionStatus = in.readString();
        accessLevel = in.readString();
        isInteractive = in.readString();
        quarantine = in.readString();
        drawingRecipients = in.readString();
        isPrivate = in.readString();
        archiveStatus = in.readString();
        rowPosition = in.readString();
        isBimerDrawing = in.readString();
        isNewType = in.readString();
        cloudDocId = in.readString();
        archiveReason = in.readString();
        companyName = in.readString();
        statusTitle = in.readString();
        categoryName = in.readString();
        isFav = in.readString();
        isTrack = in.readString();
        pdflocation = in.readString();
        pdfname = in.readString();
        dwglocation = in.readString();
        dwgname = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(projectId);
        dest.writeString(companyId);
        dest.writeString(userId);
        dest.writeString(categoryId);
        dest.writeString(name);
        dest.writeString(fileName);
        dest.writeString(originalName);
        dest.writeString(drawingNo);
        dest.writeString(tags);
        dest.writeString(status);
        dest.writeString(issue);
        dest.writeString(revision);
        dest.writeString(pdfDwg);
        dest.writeString(createdOn);
        dest.writeString(modifiedOn);
        dest.writeString(parentId);
        dest.writeString(revisionStatus);
        dest.writeString(accessLevel);
        dest.writeString(isInteractive);
        dest.writeString(quarantine);
        dest.writeString(drawingRecipients);
        dest.writeString(isPrivate);
        dest.writeString(archiveStatus);
        dest.writeString(rowPosition);
        dest.writeString(isBimerDrawing);
        dest.writeString(isNewType);
        dest.writeString(cloudDocId);
        dest.writeString(archiveReason);
        dest.writeString(companyName);
        dest.writeString(statusTitle);
        dest.writeString(categoryName);
        dest.writeString(isFav);
        dest.writeString(isTrack);
        dest.writeString(pdflocation);
        dest.writeString(pdfname);
        dest.writeString(dwglocation);
        dest.writeString(dwgname);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ChildDrawings> CREATOR = new Parcelable.Creator<ChildDrawings>() {
        @Override
        public ChildDrawings createFromParcel(Parcel in) {
            return new ChildDrawings(in);
        }

        @Override
        public ChildDrawings[] newArray(int size) {
            return new ChildDrawings[size];
        }
    };
}