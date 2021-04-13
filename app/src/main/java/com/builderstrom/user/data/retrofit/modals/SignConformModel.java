//package com.builderstrom.user.repository.retrofit.modals;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
//import java.util.List;
//
//public class SignConformModel {
//
//    @SerializedName("ResponseCode")
//    @Expose
//    private Boolean responseCode;
//    @SerializedName("Message")
//    @Expose
//    private String message;
//    @SerializedName("signinid")
//    @Expose
//    private String signinid;
//    @SerializedName("latest_mobile_apk_version")
//    @Expose
//    private String apkVersion;
//    @SerializedName("mobile_apk_description")
//    @Expose
//    private String apkDescription;
//    @SerializedName("mobile_apk_filepath")
//    @Expose
//    private String apkPath;
//    @SerializedName("details")
//    @Expose
//    private Details details;
//    @SerializedName("all_assigned_projects")
//    @Expose
//    private List<ProjectDetails> allProjects = null;
//
//    public Boolean getResponseCode() {
//        return responseCode;
//    }
//
//    public void setResponseCode(Boolean responseCode) {
//        this.responseCode = responseCode;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public String getSigninid() {
//        return signinid;
//    }
//
//    public void setSigninid(String signinid) {
//        this.signinid = signinid;
//    }
//
//    public String getApkVersion() {
//        return apkVersion;
//    }
//
//    public void setApkVersion(String apkVersion) {
//        this.apkVersion = apkVersion;
//    }
//
//    public String getApkPath() {
//        return apkPath;
//    }
//
//    public void setApkPath(String apkPath) {
//        this.apkPath = apkPath;
//    }
//
//    public Details getDetails() {
//        return details;
//    }
//
//    public void setDetails(Details details) {
//        this.details = details;
//    }
//
//    public String getApkDescription() {
//        return apkDescription;
//    }
//
//    public void setApkDescription(String apkDescription) {
//        this.apkDescription = apkDescription;
//    }
//
//    public List<ProjectDetails> getAllProjects() {
//        return allProjects;
//    }
//
//    public void setAllProjects(List<ProjectDetails> allProjects) {
//        this.allProjects = allProjects;
//    }
//
////    public class Projectdetails {
////
////        @SerializedName("uid")
////        @Expose
////        private String uid;
////        @SerializedName("collection_uid")
////        @Expose
////        private String collectionUid;
////        @SerializedName("title")
////        @Expose
////        private String title;
////        @SerializedName("added_on")
////        @Expose
////        private String addedOn;
////        @SerializedName("project_primaryEmail")
////        @Expose
////        private String projectPrimaryEmail;
////        @SerializedName("username")
////        @Expose
////        private String username;
////        @SerializedName("password")
////        @Expose
////        private String password;
////        @SerializedName("server_address")
////        @Expose
////        private String serverAddress;
////        @SerializedName("type")
////        @Expose
////        private String type;
////        @SerializedName("contact")
////        @Expose
////        private String contact;
////        @SerializedName("address")
////        @Expose
////        private String address;
////        @SerializedName("zip")
////        @Expose
////        private String zip;
////        @SerializedName("lat")
////        @Expose
////        private String lat;
////        @SerializedName("lang")
////        @Expose
////        private String lang;
////        @SerializedName("end_date")
////        @Expose
////        private String endDate;
////        @SerializedName("note")
////        @Expose
////        private String note;
////        @SerializedName("image")
////        @Expose
////        private String image;
////        @SerializedName("project_id")
////        @Expose
////        private String projectId;
////        @SerializedName("group_id_system")
////        @Expose
////        private String groupIdSystem;
////        @SerializedName("archive")
////        @Expose
////        private String archive;
////        @SerializedName("delete_status")
////        @Expose
////        private String deleteStatus;
////        @SerializedName("project_order")
////        @Expose
////        private String projectOrder;
////        @SerializedName("super_admin_project")
////        @Expose
////        private String superAdminProject;
////        @SerializedName("status")
////        @Expose
////        private String status;
////        @SerializedName("stage")
////        @Expose
////        private String stage;
////        @SerializedName("c_manager")
////        @Expose
////        private String cManager;
////        @SerializedName("color_code")
////        @Expose
////        private String colorCode;
////        @SerializedName("client")
////        @Expose
////        private String client;
////        @SerializedName("description")
////        @Expose
////        private String description;
////        @SerializedName("project_cost")
////        @Expose
////        private String projectCost;
////        @SerializedName("region")
////        @Expose
////        private String region;
////        @SerializedName("db_importor_status")
////        @Expose
////        private String dbImportorStatus;
////        @SerializedName("project_order_no")
////        @Expose
////        private String projectOrderNo;
////        @SerializedName("hidden_status")
////        @Expose
////        private String hiddenStatus;
////        @SerializedName("submittal_revision_sequence_button")
////        @Expose
////        private String submittalRevisionSequenceButton;
////        @SerializedName("submittal_revision_sequence")
////        @Expose
////        private String submittalRevisionSequence;
////        @SerializedName("submittal_days_warning_button")
////        @Expose
////        private String submittalDaysWarningButton;
////        @SerializedName("submittal_days_warning")
////        @Expose
////        private String submittalDaysWarning;
////        @SerializedName("submittal_on_off")
////        @Expose
////        private String submittalOnOff;
////        @SerializedName("submittal_status_graph")
////        @Expose
////        private String submittalStatusGraph;
////        @SerializedName("drawing_revision_sequence_button")
////        @Expose
////        private String drawingRevisionSequenceButton;
////        @SerializedName("drawing_revision_sequence")
////        @Expose
////        private String drawingRevisionSequence;
////        @SerializedName("document_controllers")
////        @Expose
////        private String documentControllers;
////        @SerializedName("plant_manager")
////        @Expose
////        private String plantManager;
////        @SerializedName("proj_doc_flow_manager")
////        @Expose
////        private String projDocFlowManager;
////        @SerializedName("submittals_upload_project_drawing")
////        @Expose
////        private String submittalsUploadProjectDrawing;
////        @SerializedName("submittals_upload_project_document")
////        @Expose
////        private String submittalsUploadProjectDocument;
////        @SerializedName("showfor_mobile_siteaccess")
////        @Expose
////        private String showforMobileSiteaccess;
////
////        public String getUid() {
////            return uid;
////        }
////
////        public void setUid(String uid) {
////            this.uid = uid;
////        }
////
////        public String getCollectionUid() {
////            return collectionUid;
////        }
////
////        public void setCollectionUid(String collectionUid) {
////            this.collectionUid = collectionUid;
////        }
////
////        public String getTitle() {
////            return title;
////        }
////
////        public void setTitle(String title) {
////            this.title = title;
////        }
////
////        public String getAddedOn() {
////            return addedOn;
////        }
////
////        public void setAddedOn(String addedOn) {
////            this.addedOn = addedOn;
////        }
////
////        public String getProjectPrimaryEmail() {
////            return projectPrimaryEmail;
////        }
////
////        public void setProjectPrimaryEmail(String projectPrimaryEmail) {
////            this.projectPrimaryEmail = projectPrimaryEmail;
////        }
////
////        public String getUsername() {
////            return username;
////        }
////
////        public void setUsername(String username) {
////            this.username = username;
////        }
////
////        public String getPassword() {
////            return password;
////        }
////
////        public void setPassword(String password) {
////            this.password = password;
////        }
////
////        public String getServerAddress() {
////            return serverAddress;
////        }
////
////        public void setServerAddress(String serverAddress) {
////            this.serverAddress = serverAddress;
////        }
////
////        public String getType() {
////            return type;
////        }
////
////        public void setType(String type) {
////            this.type = type;
////        }
////
////        public String getContact() {
////            return contact;
////        }
////
////        public void setContact(String contact) {
////            this.contact = contact;
////        }
////
////        public String getAddress() {
////            return address;
////        }
////
////        public void setAddress(String address) {
////            this.address = address;
////        }
////
////        public String getZip() {
////            return zip;
////        }
////
////        public void setZip(String zip) {
////            this.zip = zip;
////        }
////
////        public String getLat() {
////            return lat;
////        }
////
////        public void setLat(String lat) {
////            this.lat = lat;
////        }
////
////        public String getLang() {
////            return lang;
////        }
////
////        public void setLang(String lang) {
////            this.lang = lang;
////        }
////
////        public String getEndDate() {
////            return endDate;
////        }
////
////        public void setEndDate(String endDate) {
////            this.endDate = endDate;
////        }
////
////        public String getNote() {
////            return note;
////        }
////
////        public void setNote(String note) {
////            this.note = note;
////        }
////
////        public String getImage() {
////            return image;
////        }
////
////        public void setImage(String image) {
////            this.image = image;
////        }
////
////        public String getProjectId() {
////            return projectId;
////        }
////
////        public void setProjectId(String projectId) {
////            this.projectId = projectId;
////        }
////
////        public String getGroupIdSystem() {
////            return groupIdSystem;
////        }
////
////        public void setGroupIdSystem(String groupIdSystem) {
////            this.groupIdSystem = groupIdSystem;
////        }
////
////        public String getArchive() {
////            return archive;
////        }
////
////        public void setArchive(String archive) {
////            this.archive = archive;
////        }
////
////        public String getDeleteStatus() {
////            return deleteStatus;
////        }
////
////        public void setDeleteStatus(String deleteStatus) {
////            this.deleteStatus = deleteStatus;
////        }
////
////        public String getProjectOrder() {
////            return projectOrder;
////        }
////
////        public void setProjectOrder(String projectOrder) {
////            this.projectOrder = projectOrder;
////        }
////
////        public String getSuperAdminProject() {
////            return superAdminProject;
////        }
////
////        public void setSuperAdminProject(String superAdminProject) {
////            this.superAdminProject = superAdminProject;
////        }
////
////        public String getStatus() {
////            return status;
////        }
////
////        public void setStatus(String status) {
////            this.status = status;
////        }
////
////        public String getStage() {
////            return stage;
////        }
////
////        public void setStage(String stage) {
////            this.stage = stage;
////        }
////
////        public String getCManager() {
////            return cManager;
////        }
////
////        public void setCManager(String cManager) {
////            this.cManager = cManager;
////        }
////
////        public String getColorCode() {
////            return colorCode;
////        }
////
////        public void setColorCode(String colorCode) {
////            this.colorCode = colorCode;
////        }
////
////        public String getClient() {
////            return client;
////        }
////
////        public void setClient(String client) {
////            this.client = client;
////        }
////
////        public String getDescription() {
////            return description;
////        }
////
////        public void setDescription(String description) {
////            this.description = description;
////        }
////
////        public String getProjectCost() {
////            return projectCost;
////        }
////
////        public void setProjectCost(String projectCost) {
////            this.projectCost = projectCost;
////        }
////
////        public String getRegion() {
////            return region;
////        }
////
////        public void setRegion(String region) {
////            this.region = region;
////        }
////
////        public String getDbImportorStatus() {
////            return dbImportorStatus;
////        }
////
////        public void setDbImportorStatus(String dbImportorStatus) {
////            this.dbImportorStatus = dbImportorStatus;
////        }
////
////        public String getProjectOrderNo() {
////            return projectOrderNo;
////        }
////
////        public void setProjectOrderNo(String projectOrderNo) {
////            this.projectOrderNo = projectOrderNo;
////        }
////
////        public String getHiddenStatus() {
////            return hiddenStatus;
////        }
////
////        public void setHiddenStatus(String hiddenStatus) {
////            this.hiddenStatus = hiddenStatus;
////        }
////
////        public String getSubmittalRevisionSequenceButton() {
////            return submittalRevisionSequenceButton;
////        }
////
////        public void setSubmittalRevisionSequenceButton(String submittalRevisionSequenceButton) {
////            this.submittalRevisionSequenceButton = submittalRevisionSequenceButton;
////        }
////
////        public String getSubmittalRevisionSequence() {
////            return submittalRevisionSequence;
////        }
////
////        public void setSubmittalRevisionSequence(String submittalRevisionSequence) {
////            this.submittalRevisionSequence = submittalRevisionSequence;
////        }
////
////        public String getSubmittalDaysWarningButton() {
////            return submittalDaysWarningButton;
////        }
////
////        public void setSubmittalDaysWarningButton(String submittalDaysWarningButton) {
////            this.submittalDaysWarningButton = submittalDaysWarningButton;
////        }
////
////        public String getSubmittalDaysWarning() {
////            return submittalDaysWarning;
////        }
////
////        public void setSubmittalDaysWarning(String submittalDaysWarning) {
////            this.submittalDaysWarning = submittalDaysWarning;
////        }
////
////        public String getSubmittalOnOff() {
////            return submittalOnOff;
////        }
////
////        public void setSubmittalOnOff(String submittalOnOff) {
////            this.submittalOnOff = submittalOnOff;
////        }
////
////        public String getSubmittalStatusGraph() {
////            return submittalStatusGraph;
////        }
////
////        public void setSubmittalStatusGraph(String submittalStatusGraph) {
////            this.submittalStatusGraph = submittalStatusGraph;
////        }
////
////        public String getDrawingRevisionSequenceButton() {
////            return drawingRevisionSequenceButton;
////        }
////
////        public void setDrawingRevisionSequenceButton(String drawingRevisionSequenceButton) {
////            this.drawingRevisionSequenceButton = drawingRevisionSequenceButton;
////        }
////
////        public String getDrawingRevisionSequence() {
////            return drawingRevisionSequence;
////        }
////
////        public void setDrawingRevisionSequence(String drawingRevisionSequence) {
////            this.drawingRevisionSequence = drawingRevisionSequence;
////        }
////
////        public String getDocumentControllers() {
////            return documentControllers;
////        }
////
////        public void setDocumentControllers(String documentControllers) {
////            this.documentControllers = documentControllers;
////        }
////
////        public String getPlantManager() {
////            return plantManager;
////        }
////
////        public void setPlantManager(String plantManager) {
////            this.plantManager = plantManager;
////        }
////
////        public String getProjDocFlowManager() {
////            return projDocFlowManager;
////        }
////
////        public void setProjDocFlowManager(String projDocFlowManager) {
////            this.projDocFlowManager = projDocFlowManager;
////        }
////
////        public String getSubmittalsUploadProjectDrawing() {
////            return submittalsUploadProjectDrawing;
////        }
////
////        public void setSubmittalsUploadProjectDrawing(String submittalsUploadProjectDrawing) {
////            this.submittalsUploadProjectDrawing = submittalsUploadProjectDrawing;
////        }
////
////        public String getSubmittalsUploadProjectDocument() {
////            return submittalsUploadProjectDocument;
////        }
////
////        public void setSubmittalsUploadProjectDocument(String submittalsUploadProjectDocument) {
////            this.submittalsUploadProjectDocument = submittalsUploadProjectDocument;
////        }
////
////        public String getShowforMobileSiteaccess() {
////            return showforMobileSiteaccess;
////        }
////
////        public void setShowforMobileSiteaccess(String showforMobileSiteaccess) {
////            this.showforMobileSiteaccess = showforMobileSiteaccess;
////        }
////
////    }
//
//
//
//    public class Details {
//
//        @SerializedName("projectHours")
//        @Expose
//        private ProjectHours projectHours;
//        @SerializedName("projectdetails")
//        @Expose
//        private ProjectDetails projectdetails;
//        @SerializedName("project_title")
//        @Expose
//        private String projectTitle;
//
//        public ProjectHours getProjectHours() {
//            return projectHours;
//        }
//
//        public void setProjectHours(ProjectHours projectHours) {
//            this.projectHours = projectHours;
//        }
//
//        public ProjectDetails getProjectdetails() {
//            return projectdetails;
//        }
//
//        public void setProjectdetails(ProjectDetails projectdetails) {
//            this.projectdetails = projectdetails;
//        }
//
//        public String getProjectTitle() {
//            return projectTitle;
//        }
//
//        public void setProjectTitle(String projectTitle) {
//            this.projectTitle = projectTitle;
//        }
//
//    }
//}