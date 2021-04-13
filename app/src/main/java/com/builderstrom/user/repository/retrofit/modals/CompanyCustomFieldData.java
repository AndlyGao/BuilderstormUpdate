package com.builderstrom.user.repository.retrofit.modals;

import android.os.Parcel;
import android.os.Parcelable;

class CompanyCustomFieldData implements Parcelable {

    private Integer id;
    private String value = "";
    private Integer custom_field_id;
    private String check_input = "";
    private Integer section_primary_key;
    private String field_label = "";
    private String field_type = "";
    private String visible = "";
    private String show_in_view = "";
    private Integer table_column;

    public CompanyCustomFieldData() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getCustom_field_id() {
        return custom_field_id;
    }

    public void setCustom_field_id(Integer custom_field_id) {
        this.custom_field_id = custom_field_id;
    }

    public String getCheck_input() {
        return check_input;
    }

    public void setCheck_input(String check_input) {
        this.check_input = check_input;
    }

    public Integer getSection_primary_key() {
        return section_primary_key;
    }

    public void setSection_primary_key(Integer section_primary_key) {
        this.section_primary_key = section_primary_key;
    }

    public String getField_label() {
        return field_label;
    }

    public void setField_label(String field_label) {
        this.field_label = field_label;
    }

    public String getField_type() {
        return field_type;
    }

    public void setField_type(String field_type) {
        this.field_type = field_type;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getShow_in_view() {
        return show_in_view;
    }

    public void setShow_in_view(String show_in_view) {
        this.show_in_view = show_in_view;
    }

    public Integer getTable_column() {
        return table_column;
    }

    public void setTable_column(Integer table_column) {
        this.table_column = table_column;
    }


    protected CompanyCustomFieldData(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        value = in.readString();
        custom_field_id = in.readByte() == 0x00 ? null : in.readInt();
        check_input = in.readString();
        section_primary_key = in.readByte() == 0x00 ? null : in.readInt();
        field_label = in.readString();
        field_type = in.readString();
        visible = in.readString();
        show_in_view = in.readString();
        table_column = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(value);
        if (custom_field_id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(custom_field_id);
        }
        dest.writeString(check_input);
        if (section_primary_key == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(section_primary_key);
        }
        dest.writeString(field_label);
        dest.writeString(field_type);
        dest.writeString(visible);
        dest.writeString(show_in_view);
        if (table_column == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(table_column);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CompanyCustomFieldData> CREATOR = new Parcelable.Creator<CompanyCustomFieldData>() {
        @Override
        public CompanyCustomFieldData createFromParcel(Parcel in) {
            return new CompanyCustomFieldData(in);
        }

        @Override
        public CompanyCustomFieldData[] newArray(int size) {
            return new CompanyCustomFieldData[size];
        }
    };
}