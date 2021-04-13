package com.builderstrom.user.repository.retrofit.modals;

import java.util.List;

public class ToDoListDataModel {
    private List<PojoToDo> toDoList = null;
    private boolean isOffline = false;

    public ToDoListDataModel(List<PojoToDo> toDoList, boolean isOffline) {
        this.toDoList = toDoList;
        this.isOffline = isOffline;
    }

    public List<PojoToDo> getToDoList() {
        return toDoList;
    }

    public void setToDoList(List<PojoToDo> toDoList) {
        this.toDoList = toDoList;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }
}
