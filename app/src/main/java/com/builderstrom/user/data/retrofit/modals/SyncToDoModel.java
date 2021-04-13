package com.builderstrom.user.data.retrofit.modals;

public class SyncToDoModel {
    private Integer columnId;
    private PojoToDo toDo;

    public SyncToDoModel(Integer columnId, PojoToDo toDo) {
        this.columnId = columnId;
        this.toDo = toDo;
    }

    public Integer getColumnId() {
        return columnId;
    }

    public PojoToDo getToDo() {
        return toDo;
    }
}
