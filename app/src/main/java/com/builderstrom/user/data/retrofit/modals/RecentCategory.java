package com.builderstrom.user.data.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecentCategory {

    @SerializedName("category_title")
    @Expose
    private String categoryTitle;
    @SerializedName("color_code")
    @Expose
    private String colorCode;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("documents_pending")
    @Expose
    private Integer documentsPending;
    @SerializedName("category_section")
    @Expose
    private String categorySection;

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getDocumentsPending() {
        return documentsPending;
    }

    public void setDocumentsPending(Integer documentsPending) {
        this.documentsPending = documentsPending;
    }

    public String getCategorySection() {
        return categorySection;
    }

    public void setCategorySection(String categorySection) {
        this.categorySection = categorySection;
    }

}
