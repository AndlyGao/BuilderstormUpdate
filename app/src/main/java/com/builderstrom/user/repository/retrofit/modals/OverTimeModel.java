package com.builderstrom.user.repository.retrofit.modals;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class OverTimeModel implements Parcelable {
    private Integer allowOvertime;
    private Integer allowStandardBreaks;
    private List<StandardBreaks> standardBreaks = null;
    private List<StandardBreaks> overtime = null;

    public OverTimeModel(Integer allowOvertime, Integer allowStandardBreaks, List<StandardBreaks> standardBreaks, List<StandardBreaks> overtime) {
        this.allowOvertime = allowOvertime;
        this.allowStandardBreaks = allowStandardBreaks;
        this.standardBreaks = standardBreaks;
        this.overtime = overtime;
    }

    public OverTimeModel() {
    }

    public Integer getAllowOvertime() {
        return allowOvertime;
    }

    public void setAllowOvertime(Integer allowOvertime) {
        this.allowOvertime = allowOvertime;
    }

    public Integer getAllowStandardBreaks() {
        return allowStandardBreaks;
    }

    public void setAllowStandardBreaks(Integer allowStandardBreaks) {
        this.allowStandardBreaks = allowStandardBreaks;
    }

    public List<StandardBreaks> getStandardBreaks() {
        return standardBreaks;
    }

    public void setStandardBreaks(List<StandardBreaks> standardBreaks) {
        this.standardBreaks = standardBreaks;
    }

    public List<StandardBreaks> getOvertime() {
        return overtime;
    }

    public void setOvertime(List<StandardBreaks> overtime) {
        this.overtime = overtime;
    }


    protected OverTimeModel(Parcel in) {
        allowOvertime = in.readByte() == 0x00 ? null : in.readInt();
        allowStandardBreaks = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            standardBreaks = new ArrayList<StandardBreaks>();
            in.readList(standardBreaks, StandardBreaks.class.getClassLoader());
        } else {
            standardBreaks = null;
        }
        if (in.readByte() == 0x01) {
            overtime = new ArrayList<StandardBreaks>();
            in.readList(overtime, StandardBreaks.class.getClassLoader());
        } else {
            overtime = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (allowOvertime == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(allowOvertime);
        }
        if (allowStandardBreaks == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(allowStandardBreaks);
        }
        if (standardBreaks == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(standardBreaks);
        }
        if (overtime == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(overtime);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OverTimeModel> CREATOR = new Parcelable.Creator<OverTimeModel>() {
        @Override
        public OverTimeModel createFromParcel(Parcel in) {
            return new OverTimeModel(in);
        }

        @Override
        public OverTimeModel[] newArray(int size) {
            return new OverTimeModel[size];
        }
    };
}