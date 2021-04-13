package com.builderstrom.user.repository.retrofit.modals;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class LocalMetaValues implements Parcelable {
    private String customFieldId = "";
    private String customFieldValue = "";
    private String checkInput = "";
    private Boolean isAttachment = false;

    public LocalMetaValues() {
    }

    public LocalMetaValues(String customFieldId, String customFieldValue) {
        this.customFieldId = customFieldId;
        this.customFieldValue = customFieldValue;
    }

    public LocalMetaValues(String customFieldId, String customFieldValue, String checkInput) {
        this.customFieldId = customFieldId;
        this.customFieldValue = customFieldValue;
        this.checkInput = checkInput;
    }

    public String getCustomFieldId() {
        return customFieldId;
    }

    public void setCustomFieldId(String customFieldId) {
        this.customFieldId = customFieldId;
    }

    public String getCustomFieldValue() {
        return customFieldValue;
    }

    public void setCustomFieldValue(String customFieldValue) {
        this.customFieldValue = customFieldValue;
    }

    public String getCheckInput() {
        return checkInput;
    }

    public void setCheckInput(String checkInput) {
        this.checkInput = checkInput;
    }

    @Override
    public String toString() {
        return TextUtils.concat("\n", customFieldId, "-->", "op", customFieldValue, "-->", "ch", checkInput).toString();
    }

    public Boolean getAttachment() {
        return isAttachment;
    }

    public void setAttachment(Boolean attachment) {
        isAttachment = attachment;
    }

    protected LocalMetaValues(Parcel in) {
        customFieldId = in.readString();
        customFieldValue = in.readString();
        checkInput = in.readString();
        byte isAttachmentVal = in.readByte();
        isAttachment = isAttachmentVal == 0x02 ? null : isAttachmentVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customFieldId);
        dest.writeString(customFieldValue);
        dest.writeString(checkInput);
        if (isAttachment == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isAttachment ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LocalMetaValues> CREATOR = new Parcelable.Creator<LocalMetaValues>() {
        @Override
        public LocalMetaValues createFromParcel(Parcel in) {
            return new LocalMetaValues(in);
        }

        @Override
        public LocalMetaValues[] newArray(int size) {
            return new LocalMetaValues[size];
        }
    };
}