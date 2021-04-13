package com.builderstrom.user.repository.retrofit.modals;

public class DigitalDoc {

    private String template_title;
    private String template_id;
    private String category_id = "";
    private String recurrence_type = "";
    private boolean isSynced = false;

    public DigitalDoc(String templateTitle, String templateId, String category,
                      String recurrence_type) {
        this.template_title = templateTitle;
        this.template_id = templateId;
        this.category_id = category;
        this.recurrence_type = recurrence_type;
    }

    public String getTemplateTitle() {
        return template_title;
    }

    public void setTemplateTitle(String templateTitle) {
        this.template_title = templateTitle;
    }

    public String getTemplateId() {
        return template_id;
    }

    public void setTemplateId(String templateId) {
        this.template_id = templateId;
    }

    public boolean getSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public String getCategory() {
        return category_id;
    }

    public void setCategory(String category) {
        this.category_id = category;
    }

    public String getRecurrence_type() {
        return recurrence_type;
    }

    public void setRecurrence_type(String recurrence_type) {
        this.recurrence_type = recurrence_type;
    }
}
