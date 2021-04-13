package com.builderstrom.user.views.viewInterfaces;

import androidx.annotation.Nullable;

public interface ConfirmSignCallback {
    void onConfirmClicked(@Nullable String overTimeList, @Nullable String idList, @Nullable String breaksList);
}
