package com.builderstrom.user.data.retrofit.modals;

import java.util.List;

public class ToDoCommentsModel extends BaseApiModel {
    private List<ToDoComments> data = null;

    public List<ToDoComments> getData() {
        return data;
    }
}
