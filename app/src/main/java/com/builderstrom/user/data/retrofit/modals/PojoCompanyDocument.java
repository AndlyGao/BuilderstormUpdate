package com.builderstrom.user.data.retrofit.modals;

import java.util.List;

public class PojoCompanyDocument extends BaseApiModel {

    private List<CompanyDocument> data = null;

    public List<CompanyDocument> getAllDocuments() {
        return data;
    }

}
