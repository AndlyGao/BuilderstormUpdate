package com.builderstrom.user.repository.retrofit.modals;


import androidx.annotation.NonNull;

public class DashBoardMenuModel {
    private String title;
    private int image_id;
    private Integer action_index;
    private String value = "";
    private String color = "";
    private String categoryId = "";
    private String catSection = "";
    private String docsPending = "";

    public DashBoardMenuModel(Integer action, int icon, String title, String value) {
        this.action_index = action;
        this.image_id = icon;
        this.title = title;
        this.value = value;
    }

    public DashBoardMenuModel(Integer action, int icon, String title, String value, String color) {
        this.action_index = action;
        this.image_id = icon;
        this.title = title;
        this.value = value;
        this.color = color;
    }

    public DashBoardMenuModel(Integer action, int icon, String title, String value, String color, String categoryId, String catSection, String docsPending) {
        this.action_index = action;
        this.image_id = icon;
        this.title = title;
        this.value = value;
        this.color = color;
        this.categoryId = categoryId;
        this.catSection = catSection;
        this.docsPending = docsPending;
    }


    public DashBoardMenuModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public Integer getAction_index() {
        return action_index;
    }

    public void setAction_index(Integer action_index) {
        this.action_index = action_index;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCatSection() {
        return catSection;
    }

    public void setCatSection(String catSection) {
        this.catSection = catSection;
    }

    public String getDocsPending() {
        return docsPending;
    }

    public void setDocsPending(String docsPending) {
        this.docsPending = docsPending;
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }

    public boolean equals(String sectionName) {
        return value.equalsIgnoreCase(sectionName);
    }
}
