package com.builderstrom.user.views.viewInterfaces;

import com.builderstrom.user.repository.retrofit.modals.CompanyDocument;

public interface CompanyActionCallBack {
    void onActionClicked(int action, CompanyDocument companyDocument, int position);

}
