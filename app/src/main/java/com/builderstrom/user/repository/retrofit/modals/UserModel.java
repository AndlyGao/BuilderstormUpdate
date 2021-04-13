package com.builderstrom.user.repository.retrofit.modals;


import java.util.List;

public class UserModel extends BaseApiModel {

    private List<User> data = null;

    public List<User> getAllUsers() {
        return data;
    }


}
