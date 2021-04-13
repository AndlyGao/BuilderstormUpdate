package com.builderstrom.user.data.retrofit.modals;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;

public class Datum implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("isSync")
    @Expose
    private boolean isSync = false;
    @SerializedName("pdfLocal")
    @Expose
    private MultipartBody.Part pdfLocal;
    @SerializedName("dwgLocal")
    @Expose
    private MultipartBody.Part dwgLocal;
    @SerializedName("isPdfClick")
    @Expose
    private boolean isPdfClick;
    @SerializedName("isDwgClick")
    @Expose
    private boolean isDwgClick;
    @SerializedName("isOftClick")
    @Expose
    private boolean isOftClick;
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
    @SerializedName("is_interactive")
    @Expose
    private String isInteractive;
    @SerializedName("quarantine")
    @Expose
    private String quarantine;
    @SerializedName("is_private")
    @Expose
    private String isPrivate;
    @SerializedName("archive_status")
    @Expose
    private int archiveStatus;
    @SerializedName("row_position")
    @Expose
    private String rowPosition;
    @SerializedName("totalcomments")
    @Expose
    private int totalcomments;
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
    @SerializedName("otherFileLoc")
    @Expose
    private String oftlocation;
    @SerializedName("dwgname")
    @Expose
    private String dwgname;
    @SerializedName("otherFileName")
    @Expose
    private String oftname;
    @SerializedName("uploadername")
    @Expose
    private String uploadername;
    @SerializedName("childdrawings")
    @Expose
    private List<ChildDrawings> childdrawings = null;
    @SerializedName("custom_field_data")
    @Expose
    private List<List<MetaValues>> custom_field_data = null;


    private List<LocalMetaValues> metaData;


    public Datum(String pdfname, String dwgname, String pdflocation, String dwglocation, String drawingNo, String name, String revision, String uploadername, String createdOn) {
        this.pdfname = pdfname;
        this.dwgname = dwgname;
        this.pdflocation = pdflocation;
        this.dwglocation = dwglocation;
        this.drawingNo = drawingNo;
        this.name = name;
        this.revision = revision;
        this.uploadername = uploadername;
        this.createdOn = createdOn;

    }

    public Datum(String pdfname, String dwgname, String oftname, String pdflocation, String dwglocation, String oftlocation, String drawingNo, String name, String revision, String quarantine, int archiveStatus, String uploadername, String createdOn, List<LocalMetaValues> metaData) {
        this.pdfname = pdfname;
        this.dwgname = dwgname;
        this.oftname = oftname;
        this.pdflocation = pdflocation;
        this.dwglocation = dwglocation;
        this.oftlocation = oftlocation;
        this.drawingNo = drawingNo;
        this.name = name;
        this.quarantine = quarantine;
        this.archiveStatus = archiveStatus;
        this.revision = revision;
        this.uploadername = uploadername;
        this.createdOn = createdOn;
        this.metaData = metaData;
    }

    public MultipartBody.Part getPdfLocal() {
        return pdfLocal;
    }

    public void setPdfLocal(MultipartBody.Part pdfLocal) {
        this.pdfLocal = pdfLocal;
    }

    public MultipartBody.Part getDwgLocal() {
        return dwgLocal;
    }

    public void setDwgLocal(MultipartBody.Part dwgLocal) {
        this.dwgLocal = dwgLocal;
    }

    public boolean isDwgClick() {
        return isDwgClick;
    }

    public void setDwgClick(boolean dwgClick) {
        isDwgClick = dwgClick;
    }

    public boolean isOftClick() {
        return isOftClick;
    }

    public void setOftClick(boolean oftClick) {
        isOftClick = oftClick;
    }

    public boolean isPdfClick() {
        return isPdfClick;
    }

    public void setPdfClick(boolean pdfClick) {
        isPdfClick = pdfClick;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

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

    public int getTotalcomments() {
        return totalcomments;
    }

    public void setTotalcomments(int totalcomments) {
        this.totalcomments = totalcomments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    public int getArchiveStatus() {
        return archiveStatus;
    }

    public void setArchiveStatus(int archiveStatus) {
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

    public String getOftname() {
        return oftname;
    }

    public void setOftname(String oftname) {
        this.oftname = oftname;
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

    public String getOftlocation() {
        return oftlocation;
    }

    public void setOftlocation(String oftlocation) {
        this.oftlocation = oftlocation;
    }

    public String getUploadername() {
        return uploadername;
    }

    public void setUploadername(String uploadername) {
        this.uploadername = uploadername;
    }

    public List<ChildDrawings> getChilddrawings() {
        return childdrawings;
    }

    public void setChilddrawings(List<ChildDrawings> childdrawings) {
        this.childdrawings = childdrawings;
    }

    public List<LocalMetaValues> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<LocalMetaValues> metaData) {
        this.metaData = metaData;
    }

    public List<List<MetaValues>> getCustom_field_data() {
        return custom_field_data;
    }

    public void setCustom_field_data(List<List<MetaValues>> custom_field_data) {
        this.custom_field_data = custom_field_data;
    }


    protected Datum(Parcel in) {
        id = in.readString();
        isSync = in.readByte() != 0x00;
        pdfLocal = (MultipartBody.Part) in.readValue(MultipartBody.Part.class.getClassLoader());
        dwgLocal = (MultipartBody.Part) in.readValue(MultipartBody.Part.class.getClassLoader());
        isPdfClick = in.readByte() != 0x00;
        isDwgClick = in.readByte() != 0x00;
        isOftClick = in.readByte() != 0x00;
        projectId = in.readString();
        companyId = in.readString();
        userId = in.readString();
        categoryId = in.readString();
        name = in.readString();
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
        isInteractive = in.readString();
        quarantine = in.readString();
        isPrivate = in.readString();
        archiveStatus = in.readInt();
        rowPosition = in.readString();
        totalcomments = in.readInt();
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
        oftlocation = in.readString();
        dwgname = in.readString();
        oftname = in.readString();
        uploadername = in.readString();
        if (in.readByte() == 0x01) {
            childdrawings = new ArrayList<ChildDrawings>();
            in.readList(childdrawings, ChildDrawings.class.getClassLoader());
        } else {
            childdrawings = null;
        }
        if (in.readByte() == 0x01) {
            metaData = new ArrayList<LocalMetaValues>();
            in.readList(metaData, LocalMetaValues.class.getClassLoader());
        } else {
            metaData = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeByte((byte) (isSync ? 0x01 : 0x00));
        dest.writeValue(pdfLocal);
        dest.writeValue(dwgLocal);
        dest.writeByte((byte) (isPdfClick ? 0x01 : 0x00));
        dest.writeByte((byte) (isDwgClick ? 0x01 : 0x00));
        dest.writeByte((byte) (isOftClick ? 0x01 : 0x00));
        dest.writeString(projectId);
        dest.writeString(companyId);
        dest.writeString(userId);
        dest.writeString(categoryId);
        dest.writeString(name);
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
        dest.writeString(isInteractive);
        dest.writeString(quarantine);
        dest.writeString(isPrivate);
        dest.writeInt(archiveStatus);
        dest.writeString(rowPosition);
        dest.writeInt(totalcomments);
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
        dest.writeString(oftlocation);
        dest.writeString(dwgname);
        dest.writeString(oftname);
        dest.writeString(uploadername);
        if (childdrawings == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(childdrawings);
        }
        if (metaData == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(metaData);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Datum> CREATOR = new Parcelable.Creator<Datum>() {
        @Override
        public Datum createFromParcel(Parcel in) {
            return new Datum(in);
        }

        @Override
        public Datum[] newArray(int size) {
            return new Datum[size];
        }
    };
}