package com.builderstrom.user.repository.retrofit.modals;

import java.util.List;

public class PojoCompanyCommentModel extends BaseApiModel {

    private List<CompanyComment> data = null;


    public List<CompanyComment> getAllComments() {
        return data;
    }
}
