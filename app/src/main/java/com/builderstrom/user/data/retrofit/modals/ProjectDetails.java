package com.builderstrom.user.data.retrofit.modals;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.List;

@Keep
public class ProjectDetails {
    private String uid;
    private String collection_uid;
    private String title;
    private String added_on;
    private String project_primaryEmail;
    private String username;
    private String password;
    private String server_address;
    private String type;
    private String contact;
    private String address;
    private String zip;
    private String lat;
    private String lang;
    private String start_date;
    private String end_date;
    private String note;
    private String image;
    private String project_id;
    private String group_id_system;
    private String archive;
    private String delete_status;
    private String project_order;
    private String super_admin_project;
    private String status;
    private String stage;
    private String c_manager;
    private String color_code;
    private String client;
    private String description;
    private String project_cost;
    private String region;
    private String db_importor_status;
    private String project_order_no;
    private String hidden_status;
    private String submittal_revision_sequence_button;
    private String submittal_revision_sequence;
    private String submittal_days_warning_button;
    private String submittal_days_warning;
    private String submittal_on_off;
    private String submittal_status_graph;
    private String drawing_revision_sequence_button;
    private String drawing_revision_sequence;
    private String document_controllers;
    private Object logistics_manager;
    private String plant_manager;
    private String proj_doc_flow_manager;
    private String submittals_upload_project_drawing;
    private String submittals_upload_project_document;
    private String showfor_mobile_siteaccess;
    private List<StandardBreaks> standard_breaks;
    private Integer allow_overtime;
    private Integer allow_standard_breaks;
    private float Distance;
    private String projectStartTime = "";
    private String projectEndTime = "";
    private Boolean cost_track_labour = false;
    private boolean isOpened = false;
    private List<List<ProjectCustomData>> custom_field_data = null;
    private ProjectHoursModel projectHours;
    private List<PojoCostCode> costCodes = null;

    public ProjectDetails(String uid, String title) {
        this.uid = uid;
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCollection_uid() {
        return collection_uid;
    }

    public void setCollection_uid(String collection_uid) {
        this.collection_uid = collection_uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdded_on() {
        return added_on;
    }

    public void setAdded_on(String added_on) {
        this.added_on = added_on;
    }

    public String getProject_primaryEmail() {
        return project_primaryEmail;
    }

    public void setProject_primaryEmail(String project_primaryEmail) {
        this.project_primaryEmail = project_primaryEmail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServer_address() {
        return server_address;
    }

    public void setServer_address(String server_address) {
        this.server_address = server_address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getEndDate() {
        return end_date;
    }

    public void setEndDate(String end_date) {
        this.end_date = end_date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getGroup_id_system() {
        return group_id_system;
    }

    public void setGroup_id_system(String group_id_system) {
        this.group_id_system = group_id_system;
    }

    public String getArchive() {
        return archive;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public String getDelete_status() {
        return delete_status;
    }

    public void setDelete_status(String delete_status) {
        this.delete_status = delete_status;
    }

    public String getProject_order() {
        return project_order;
    }

    public void setProject_order(String project_order) {
        this.project_order = project_order;
    }

    public String getSuper_admin_project() {
        return super_admin_project;
    }

    public void setSuper_admin_project(String super_admin_project) {
        this.super_admin_project = super_admin_project;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getC_manager() {
        return c_manager;
    }

    public void setC_manager(String c_manager) {
        this.c_manager = c_manager;
    }

    public String getColor_code() {
        return color_code;
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProject_cost() {
        return project_cost;
    }

    public void setProject_cost(String project_cost) {
        this.project_cost = project_cost;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDb_importor_status() {
        return db_importor_status;
    }

    public void setDb_importor_status(String db_importor_status) {
        this.db_importor_status = db_importor_status;
    }

    public String getProject_order_no() {
        return project_order_no;
    }

    public void setProject_order_no(String project_order_no) {
        this.project_order_no = project_order_no;
    }

    public String getHidden_status() {
        return hidden_status;
    }

    public void setHidden_status(String hidden_status) {
        this.hidden_status = hidden_status;
    }

    public String getSubmittal_revision_sequence_button() {
        return submittal_revision_sequence_button;
    }

    public void setSubmittal_revision_sequence_button(String submittal_revision_sequence_button) {
        this.submittal_revision_sequence_button = submittal_revision_sequence_button;
    }

    public String getSubmittal_revision_sequence() {
        return submittal_revision_sequence;
    }

    public void setSubmittal_revision_sequence(String submittal_revision_sequence) {
        this.submittal_revision_sequence = submittal_revision_sequence;
    }

    public String getSubmittal_days_warning_button() {
        return submittal_days_warning_button;
    }

    public void setSubmittal_days_warning_button(String submittal_days_warning_button) {
        this.submittal_days_warning_button = submittal_days_warning_button;
    }

    public String getSubmittal_days_warning() {
        return submittal_days_warning;
    }

    public void setSubmittal_days_warning(String submittal_days_warning) {
        this.submittal_days_warning = submittal_days_warning;
    }

    public String getSubmittal_on_off() {
        return submittal_on_off;
    }

    public void setSubmittal_on_off(String submittal_on_off) {
        this.submittal_on_off = submittal_on_off;
    }

    public String getSubmittal_status_graph() {
        return submittal_status_graph;
    }

    public void setSubmittal_status_graph(String submittal_status_graph) {
        this.submittal_status_graph = submittal_status_graph;
    }

    public String getDrawing_revision_sequence_button() {
        return drawing_revision_sequence_button;
    }

    public void setDrawing_revision_sequence_button(String drawing_revision_sequence_button) {
        this.drawing_revision_sequence_button = drawing_revision_sequence_button;
    }

    public String getDrawing_revision_sequence() {
        return drawing_revision_sequence;
    }

    public void setDrawing_revision_sequence(String drawing_revision_sequence) {
        this.drawing_revision_sequence = drawing_revision_sequence;
    }

    public String getDocument_controllers() {
        return document_controllers;
    }

    public void setDocument_controllers(String document_controllers) {
        this.document_controllers = document_controllers;
    }

    public Object getLogistics_manager() {
        return logistics_manager;
    }

    public void setLogistics_manager(Object logistics_manager) {
        this.logistics_manager = logistics_manager;
    }

    public String getPlant_manager() {
        return plant_manager;
    }

    public void setPlant_manager(String plant_manager) {
        this.plant_manager = plant_manager;
    }

    public String getProj_doc_flow_manager() {
        return proj_doc_flow_manager;
    }

    public void setProj_doc_flow_manager(String proj_doc_flow_manager) {
        this.proj_doc_flow_manager = proj_doc_flow_manager;
    }

    public String getSubmittals_upload_project_drawing() {
        return submittals_upload_project_drawing;
    }

    public void setSubmittals_upload_project_drawing(String submittals_upload_project_drawing) {
        this.submittals_upload_project_drawing = submittals_upload_project_drawing;
    }

    public String getSubmittals_upload_project_document() {
        return submittals_upload_project_document;
    }

    public void setSubmittals_upload_project_document(String submittals_upload_project_document) {
        this.submittals_upload_project_document = submittals_upload_project_document;
    }

    public String getShowfor_mobile_siteaccess() {
        return showfor_mobile_siteaccess;
    }

    public void setShowfor_mobile_siteaccess(String showfor_mobile_siteaccess) {
        this.showfor_mobile_siteaccess = showfor_mobile_siteaccess;
    }

    public List<StandardBreaks> getStandard_breaks() {
        return standard_breaks;
    }

    public void setStandard_breaks(List<StandardBreaks> standard_breaks) {
        this.standard_breaks = standard_breaks;
    }

    public Integer getAllow_overtime() {
        return allow_overtime;
    }

    public void setAllow_overtime(Integer allow_overtime) {
        this.allow_overtime = allow_overtime;
    }

    public Integer getAllow_standard_breaks() {
        return allow_standard_breaks;
    }

    public void setAllow_standard_breaks(Integer allow_standard_breaks) {
        this.allow_standard_breaks = allow_standard_breaks;
    }

    public float getDistance() {
        return Distance;
    }

    public void setDistance(float distance) {
        Distance = distance;
    }

    public String getProjectStartTime() {
        return projectStartTime;
    }

    public void setProjectStartTime(String projectStartTime) {
        this.projectStartTime = projectStartTime;
    }

    public String getProjectEndTime() {
        return projectEndTime;
    }

    public void setProjectEndTime(String projectEndTime) {
        this.projectEndTime = projectEndTime;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public String getStartDate() {
        return start_date;
    }

    public void setStartDate(String start_date) {
        this.start_date = start_date;
    }

    public List<List<ProjectCustomData>> getCustomFieldData() {
        return custom_field_data;
    }

    public void setCustomFieldData(List<List<ProjectCustomData>> custom_field_data) {
        this.custom_field_data = custom_field_data;
    }

    public ProjectHoursModel getProjectHours() {
        return projectHours;
    }


    public List<PojoCostCode> getCostCodes() {
        return costCodes;
    }

    public Boolean getCostTrackLabour() {
        return cost_track_labour;
    }

    public void setCostTrackLabour(Boolean cost_track_labour) {
        this.cost_track_labour = cost_track_labour;
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }

}
