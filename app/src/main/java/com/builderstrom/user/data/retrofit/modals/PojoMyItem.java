package com.builderstrom.user.data.retrofit.modals;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class PojoMyItem implements Parcelable {

    private String template_title = "";
    private String issued_by = "";
    private String issued_id = "";
    private String for_complete_date = "";
    private String template_id = "";
    private String label_date = "";
    private String reference_id = "";
    private String issue = "";
    private String recurrence = "";
    private String recurrence_type = "";
    private String category = "";
    private String doc_id = "";
    private Integer is_completed;
    private boolean isSynced = false;
    private ArrayList<MyItemsStatusModel> status = null;

    @NonNull
    @Override
    public String toString() {
        return template_title + for_complete_date + " " + recurrence_type;
    }

    public PojoMyItem() {
    }

    public String getTemplateTitle() {
        return template_title;
    }

    public String getIssuedBy() {
        return issued_by;
    }

    public String getForCompleteDate() {
        return for_complete_date;
    }

    public String getTemplateId() {
        return template_id;
    }

    public String getLabelDate() {
        return label_date;
    }

    public ArrayList<MyItemsStatusModel> getStatus() {
        return status;
    }

    public String getReference_id() {
        return reference_id;
    }

    public String getIssueId() {
        return issued_id;
    }

    public Integer getIsCompleted() {
        return is_completed;
    }


    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }


    public String getIssue() {
        return issue;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public String getCategory() {
        return category;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getRecurrence_type() {
        return recurrence_type;
    }

    public void setRecurrence_type(String recurrence_type) {
        this.recurrence_type = recurrence_type;
    }

    protected PojoMyItem(Parcel in) {
        template_title = in.readString();
        issued_by = in.readString();
        issued_id = in.readString();
        for_complete_date = in.readString();
        template_id = in.readString();
        label_date = in.readString();
        reference_id = in.readString();
        issue = in.readString();
        recurrence = in.readString();
        recurrence_type = in.readString();
        category = in.readString();
        doc_id = in.readString();
        is_completed = in.readByte() == 0x00 ? null : in.readInt();
        isSynced = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            status = new ArrayList<MyItemsStatusModel>();
            in.readList(status, MyItemsStatusModel.class.getClassLoader());
        } else {
            status = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(template_title);
        dest.writeString(issued_by);
        dest.writeString(issued_id);
        dest.writeString(for_complete_date);
        dest.writeString(template_id);
        dest.writeString(label_date);
        dest.writeString(reference_id);
        dest.writeString(issue);
        dest.writeString(recurrence);
        dest.writeString(recurrence_type);
        dest.writeString(category);
        dest.writeString(doc_id);
        if (is_completed == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(is_completed);
        }
        dest.writeByte((byte) (isSynced ? 0x01 : 0x00));
        if (status == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(status);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PojoMyItem> CREATOR = new Parcelable.Creator<PojoMyItem>() {
        @Override
        public PojoMyItem createFromParcel(Parcel in) {
            return new PojoMyItem(in);
        }

        @Override
        public PojoMyItem[] newArray(int size) {
            return new PojoMyItem[size];
        }
    };
}