package com.builderstrom.user.data.retrofit.modals;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class MetaValues implements Parcelable {
    private String id;
    private String value;
    private String custom_field_id;
    private String check_input;
    private String section_primary_key;
    private String field_label;
    private String field_type;
    private String visible;
    private String show_in_view;
    private String table_column;

    public MetaValues(String custom_field_id, String value) {
        this.value = value;
        this.custom_field_id = custom_field_id;
    }

    public MetaValues(String custom_field_id, String value, String check_input) {
        this.value = value;
        this.custom_field_id = custom_field_id;
        this.check_input = check_input;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCustom_field_id() {
        return custom_field_id;
    }

    public void setCustom_field_id(String custom_field_id) {
        this.custom_field_id = custom_field_id;
    }

    public String getCheck_input() {
        return check_input;
    }

    public void setCheck_input(String check_input) {
        this.check_input = check_input;
    }

    public String getSection_primary_key() {
        return section_primary_key;
    }

    public void setSection_primary_key(String section_primary_key) {
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

    public String getTable_column() {
        return table_column;
    }

    public void setTable_column(String table_column) {
        this.table_column = table_column;
    }

    @Override
    public String toString() {
        return TextUtils.concat("\n", id, "-->", "op", value, "-->", "ch", check_input).toString();
    }


    protected MetaValues(Parcel in) {
        id = in.readString();
        value = in.readString();
        custom_field_id = in.readString();
        check_input = in.readString();
        section_primary_key = in.readString();
        field_label = in.readString();
        field_type = in.readString();
        visible = in.readString();
        show_in_view = in.readString();
        table_column = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(value);
        dest.writeString(custom_field_id);
        dest.writeString(check_input);
        dest.writeString(section_primary_key);
        dest.writeString(field_label);
        dest.writeString(field_type);
        dest.writeString(visible);
        dest.writeString(show_in_view);
        dest.writeString(table_column);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MetaValues> CREATOR = new Parcelable.Creator<MetaValues>() {
        @Override
        public MetaValues createFromParcel(Parcel in) {
            return new MetaValues(in);
        }

        @Override
        public MetaValues[] newArray(int size) {
            return new MetaValues[size];
        }
    };
}