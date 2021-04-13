package com.builderstrom.user.views.customViews.textChips.chip;

public class ChipInfo {

    private final CharSequence mText;
    private final Object mData;

    public ChipInfo(CharSequence text, Object data) {
        this.mText = text;
        this.mData = data;
    }

    public CharSequence getText() {
        return mText;
    }

    public Object getData() {
        return mData;
    }
}
