package com.builderstrom.user.repository.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProjectDocumentModel extends BaseApiModel {

    @SerializedName("data")
    @Expose
    private List<PDocsDataModel> allfile = null;

    public List<PDocsDataModel> getAllfile() {
        return allfile;
    }

    public void setAllfile(List<PDocsDataModel> allfile) {
        this.allfile = allfile;
    }

}

