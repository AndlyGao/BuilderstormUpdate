package com.builderstrom.user.data.retrofit.modals;


public class NotificationIconModel {
    private String title;
    private int image_id;

    public NotificationIconModel(int icon, String title) {
        this.image_id = icon;
        this.title = title;
    }


    public NotificationIconModel() {
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
}
