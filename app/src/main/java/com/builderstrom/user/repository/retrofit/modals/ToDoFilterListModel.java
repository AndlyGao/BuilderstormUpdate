package com.builderstrom.user.repository.retrofit.modals;

import androidx.annotation.NonNull;

public class ToDoFilterListModel {

    //Status=1
    //Category=2
    //UserName=3
    //Date=4

    String id = "";
    String name = "";
    Integer category = null;

    public ToDoFilterListModel(String id, String name, Integer category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    @NonNull @Override public String toString() {
        return name;
    }


}
