package com.builderstrom.user.repository.retrofit.modals;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class CompanyDocument implements Parcelable {
    private String id;
    private String title;
    private String category;
    private String user;
    private String parent_id;
    private String tags;
    private String is_private;
    private String revision;
    private Integer custom_document_id;
    private String created_on;
    private String signed_doc;
    private String doc_status;
    private String regional_doc;
    private String document_no;
    private String region_id;
    private String cat_title;
    private String status_title;
    private String is_fav;
    private String is_track;
    private String filelocation;
    private String filename;
    private String uploadername;
    private String custom_template_id;
    private PinnedComment pinnedcomment;
    private Integer totalcomments;
    private List<User> tousers = null;
    private List<CompanyDocument> childdocs = null;
    private List<List<CompanyCustomFieldData>> custom_field_data = null;
    private boolean isDocSynced = false;
    private String pinComment = "";


    public CompanyDocument() {


    }

    public CompanyDocument(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getIs_private() {
        return is_private;
    }

    public void setIs_private(String is_private) {
        this.is_private = is_private;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public Integer getCustomDocumentId() {
        return custom_document_id;
    }

    public void setCustom_document_id(Integer custom_document_id) {
        this.custom_document_id = custom_document_id;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getSigned_doc() {
        return signed_doc;
    }

    public void setSigned_doc(String signed_doc) {
        this.signed_doc = signed_doc;
    }

    public String getDoc_status() {
        return doc_status;
    }

    public void setDoc_status(String doc_status) {
        this.doc_status = doc_status;
    }

    public String getRegional_doc() {
        return regional_doc;
    }

    public void setRegional_doc(String regional_doc) {
        this.regional_doc = regional_doc;
    }

    public String getDocument_no() {
        return document_no;
    }

    public void setDocument_no(String document_no) {
        this.document_no = document_no;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getCat_title() {
        return cat_title;
    }

    public void setCat_title(String cat_title) {
        this.cat_title = cat_title;
    }

    public String getStatus_title() {
        return status_title;
    }

    public void setStatus_title(String status_title) {
        this.status_title = status_title;
    }

    public String getIs_fav() {
        return is_fav;
    }

    public void setIs_fav(String is_fav) {
        this.is_fav = is_fav;
    }

    public String getIs_track() {
        return is_track;
    }

    public void setIs_track(String is_track) {
        this.is_track = is_track;
    }

    public String getFilelocation() {
        return filelocation;
    }

    public void setFilelocation(String filelocation) {
        this.filelocation = filelocation;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUploaderName() {
        return uploadername;
    }

    public void setUploadername(String uploadername) {
        this.uploadername = uploadername;
    }

    public Integer getTotalcomments() {
        return totalcomments;
    }

    public void setTotalcomments(Integer totalcomments) {
        this.totalcomments = totalcomments;
    }

    public List<CompanyDocument> getChilddocs() {
        return childdocs;
    }

    public void setChilddocs(List<CompanyDocument> childdocs) {
        this.childdocs = childdocs;
    }

    public PinnedComment getPinnedcomment() {
        return pinnedcomment;
    }

    public void setPinnedcomment(PinnedComment pinnedcomment) {
        this.pinnedcomment = pinnedcomment;
    }

    public String getPinComment() {
        return pinComment;
    }

    public void setPinComment(String pinComment) {
        this.pinComment = pinComment;
    }

    public List<List<CompanyCustomFieldData>> getCustom_field_data() {
        return custom_field_data;
    }

    public void setCustom_field_data(List<List<CompanyCustomFieldData>> custom_field_data) {
        this.custom_field_data = custom_field_data;
    }

    @Override
    public String toString() {
        return title;
    }


    public List<User> getToUsers() {
        return tousers;
    }

    public void setToUsers(List<User> tousers) {
        this.tousers = tousers;
    }

    public boolean isDocSynced() {
        return isDocSynced;
    }

    public void setDocSynced(boolean docSynced) {
        isDocSynced = docSynced;
    }

    public String getCustomTemplateId() {
        return custom_template_id;
    }

    public void setCustomTemplateId(String custom_template_id) {
        this.custom_template_id = custom_template_id;
    }


    protected CompanyDocument(Parcel in) {
        id = in.readString();
        title = in.readString();
        category = in.readString();
        user = in.readString();
        parent_id = in.readString();
        tags = in.readString();
        is_private = in.readString();
        revision = in.readString();
        custom_document_id = in.readByte() == 0x00 ? null : in.readInt();
        created_on = in.readString();
        signed_doc = in.readString();
        doc_status = in.readString();
        regional_doc = in.readString();
        document_no = in.readString();
        region_id = in.readString();
        cat_title = in.readString();
        status_title = in.readString();
        is_fav = in.readString();
        is_track = in.readString();
        filelocation = in.readString();
        filename = in.readString();
        uploadername = in.readString();
        custom_template_id = in.readString();
        pinnedcomment = (PinnedComment) in.readValue(PinnedComment.class.getClassLoader());
        totalcomments = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            tousers = new ArrayList<User>();
            in.readList(tousers, User.class.getClassLoader());
        } else {
            tousers = null;
        }
        if (in.readByte() == 0x01) {
            childdocs = new ArrayList<CompanyDocument>();
            in.readList(childdocs, CompanyDocument.class.getClassLoader());
        } else {
            childdocs = null;
        }
        isDocSynced = in.readByte() != 0x00;
        pinComment = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(category);
        dest.writeString(user);
        dest.writeString(parent_id);
        dest.writeString(tags);
        dest.writeString(is_private);
        dest.writeString(revision);
        if (custom_document_id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(custom_document_id);
        }
        dest.writeString(created_on);
        dest.writeString(signed_doc);
        dest.writeString(doc_status);
        dest.writeString(regional_doc);
        dest.writeString(document_no);
        dest.writeString(region_id);
        dest.writeString(cat_title);
        dest.writeString(status_title);
        dest.writeString(is_fav);
        dest.writeString(is_track);
        dest.writeString(filelocation);
        dest.writeString(filename);
        dest.writeString(uploadername);
        dest.writeString(custom_template_id);
        dest.writeValue(pinnedcomment);
        if (totalcomments == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(totalcomments);
        }
        if (tousers == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(tousers);
        }
        if (childdocs == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(childdocs);
        }
        dest.writeByte((byte) (isDocSynced ? 0x01 : 0x00));
        dest.writeString(pinComment);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CompanyDocument> CREATOR = new Parcelable.Creator<CompanyDocument>() {
        @Override
        public CompanyDocument createFromParcel(Parcel in) {
            return new CompanyDocument(in);
        }

        @Override
        public CompanyDocument[] newArray(int size) {
            return new CompanyDocument[size];
        }
    };
}