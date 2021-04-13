package com.builderstrom.user.repository.retrofit.modals;

import androidx.annotation.Nullable;

import java.util.List;

public class SnagLocksModel {

    private List<SnagTotal> onLineLocks = null;
    private int[] offlineLocks = null;

    public SnagLocksModel(@Nullable List<SnagTotal> onLineLocks, @Nullable int[] offlineLocks) {
        this.onLineLocks = onLineLocks;
        this.offlineLocks = offlineLocks;
    }

    public List<SnagTotal> getOnLineLocks() {
        return onLineLocks;
    }

    public int[] getOfflineLocks() {
        return offlineLocks;
    }
}
