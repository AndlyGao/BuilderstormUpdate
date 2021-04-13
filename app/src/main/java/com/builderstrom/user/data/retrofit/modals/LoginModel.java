package com.builderstrom.user.data.retrofit.modals;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Keep
public class LoginModel {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("authToken")
    @Expose
    private String authToken;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public class Data {

        @SerializedName("uid")
        @Expose
        private Integer uid;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("emp_num")
        @Expose
        private String empNum;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("lastname")
        @Expose
        private String lastname;
        @SerializedName("fullname")
        @Expose
        private String fullname;
        @SerializedName("usertypecode")
        @Expose
        private String usertypecode;
        @SerializedName("last_project_edited")
        @Expose
        private Integer lastProjectEdited;
        @SerializedName("last_region_edited")
        @Expose
        private Integer lastRegionEdited;
        @SerializedName("added_on")
        @Expose
        private String addedOn;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("send_email_status")
        @Expose
        private Integer sendEmailStatus;
        @SerializedName("temp_project_id")
        @Expose
        private Integer tempProjectId;
        @SerializedName("is_induction")
        @Expose
        private Integer isInduction;
        @SerializedName("induction_approval_date")
        @Expose
        private Object inductionApprovalDate;
        @SerializedName("employee_type")
        @Expose
        private Integer employeeType;
        @SerializedName("reference_by")
        @Expose
        private Integer referenceBy;
        @SerializedName("secret_key")
        @Expose
        private String secretKey;
        @SerializedName("reference")
        @Expose
        private String reference;
        @SerializedName("induction_certificate")
        @Expose
        private String inductionCertificate;
        @SerializedName("password_reset_on")
        @Expose
        private String passwordResetOn;
        @SerializedName("user_order")
        @Expose
        private Integer userOrder;
        @SerializedName("user_pay_rates")
        @Expose
        private String userPayRates;
        @SerializedName("is_signedin")
        @Expose
        private Integer isSignedin;
        @SerializedName("signin_date")
        @Expose
        private String signinDate;
        @SerializedName("signin_time")
        @Expose
        private String signinTime;
        @SerializedName("signin_project")
        @Expose
        private Integer signinProject;
        @SerializedName("rfid_code")
        @Expose
        private String rfidCode;
        @SerializedName("fingerprint_hash")
        @Expose
        private String fingerprintHash;
        @SerializedName("induction_id")
        @Expose
        private Integer inductionId;
        @SerializedName("is_deleted")
        @Expose
        private String isDeleted;
        @SerializedName("recovery_email")
        @Expose
        private String recoveryEmail;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("setpin")
        @Expose
        private Object setpin;
        @SerializedName("mobilepin")
        @Expose
        private String mobilepin;
        @SerializedName("user_type")
        @Expose
        private String userType;
        @SerializedName("visible")
        @Expose
        private Integer visible;
        @SerializedName("allowEnrolment")
        @Expose
        private Integer allowEnrolment;
        @SerializedName("charge_rate")
        @Expose
        private Integer chargeRate;
        @SerializedName("rate_details")
        @Expose
        private String rateDetails;
        @SerializedName("usertypeid")
        @Expose
        private Integer usertypeid;
        @SerializedName("is_mobile_pin_set")
        @Expose
        private Boolean isMobilePinSet;
        @SerializedName("permissions")
        @Expose
        private Permissions permissions;
        @SerializedName("permission_section")
        @Expose
        private List<PermissionSection> permissionSection = null;
        @SerializedName("projects")
        @Expose
        private List<ProjectDetails> projects = null;
        @SerializedName("ApkModel")
        @Expose
        private ApkModel apk = null;

        public Integer getUid() {
            return uid;
        }

        public void setUid(Integer uid) {
            this.uid = uid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmpNum() {
            return empNum;
        }

        public void setEmpNum(String empNum) {
            this.empNum = empNum;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getUsertypecode() {
            return usertypecode;
        }

        public void setUsertypecode(String usertypecode) {
            this.usertypecode = usertypecode;
        }

        public Integer getLastProjectEdited() {
            return lastProjectEdited;
        }

        public void setLastProjectEdited(Integer lastProjectEdited) {
            this.lastProjectEdited = lastProjectEdited;
        }

        public Integer getLastRegionEdited() {
            return lastRegionEdited;
        }

        public void setLastRegionEdited(Integer lastRegionEdited) {
            this.lastRegionEdited = lastRegionEdited;
        }

        public String getAddedOn() {
            return addedOn;
        }

        public void setAddedOn(String addedOn) {
            this.addedOn = addedOn;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getSendEmailStatus() {
            return sendEmailStatus;
        }

        public void setSendEmailStatus(Integer sendEmailStatus) {
            this.sendEmailStatus = sendEmailStatus;
        }

        public Integer getTempProjectId() {
            return tempProjectId;
        }

        public void setTempProjectId(Integer tempProjectId) {
            this.tempProjectId = tempProjectId;
        }

        public Integer getIsInduction() {
            return isInduction;
        }

        public void setIsInduction(Integer isInduction) {
            this.isInduction = isInduction;
        }

        public Object getInductionApprovalDate() {
            return inductionApprovalDate;
        }

        public void setInductionApprovalDate(Object inductionApprovalDate) {
            this.inductionApprovalDate = inductionApprovalDate;
        }

        public Integer getEmployeeType() {
            return employeeType;
        }

        public void setEmployeeType(Integer employeeType) {
            this.employeeType = employeeType;
        }

        public Integer getReferenceBy() {
            return referenceBy;
        }

        public void setReferenceBy(Integer referenceBy) {
            this.referenceBy = referenceBy;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getInductionCertificate() {
            return inductionCertificate;
        }

        public void setInductionCertificate(String inductionCertificate) {
            this.inductionCertificate = inductionCertificate;
        }

        public String getPasswordResetOn() {
            return passwordResetOn;
        }

        public void setPasswordResetOn(String passwordResetOn) {
            this.passwordResetOn = passwordResetOn;
        }

        public Integer getUserOrder() {
            return userOrder;
        }

        public void setUserOrder(Integer userOrder) {
            this.userOrder = userOrder;
        }

        public String getUserPayRates() {
            return userPayRates;
        }

        public void setUserPayRates(String userPayRates) {
            this.userPayRates = userPayRates;
        }

        public Integer getIsSignedin() {
            return isSignedin;
        }

        public void setIsSignedin(Integer isSignedin) {
            this.isSignedin = isSignedin;
        }

        public String getSigninDate() {
            return signinDate;
        }

        public void setSigninDate(String signinDate) {
            this.signinDate = signinDate;
        }

        public String getSigninTime() {
            return signinTime;
        }

        public void setSigninTime(String signinTime) {
            this.signinTime = signinTime;
        }

        public Integer getSigninProject() {
            return signinProject;
        }

        public void setSigninProject(Integer signinProject) {
            this.signinProject = signinProject;
        }

        public String getRfidCode() {
            return rfidCode;
        }

        public void setRfidCode(String rfidCode) {
            this.rfidCode = rfidCode;
        }

        public String getFingerprintHash() {
            return fingerprintHash;
        }

        public void setFingerprintHash(String fingerprintHash) {
            this.fingerprintHash = fingerprintHash;
        }

        public Integer getInductionId() {
            return inductionId;
        }

        public void setInductionId(Integer inductionId) {
            this.inductionId = inductionId;
        }

        public String getIsDeleted() {
            return isDeleted;
        }

        public void setIsDeleted(String isDeleted) {
            this.isDeleted = isDeleted;
        }

        public String getRecoveryEmail() {
            return recoveryEmail;
        }

        public void setRecoveryEmail(String recoveryEmail) {
            this.recoveryEmail = recoveryEmail;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Object getSetpin() {
            return setpin;
        }

        public void setSetpin(Object setpin) {
            this.setpin = setpin;
        }

        public String getMobilepin() {
            return mobilepin;
        }

        public void setMobilepin(String mobilepin) {
            this.mobilepin = mobilepin;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public Integer getVisible() {
            return visible;
        }

        public void setVisible(Integer visible) {
            this.visible = visible;
        }

        public Integer getAllowEnrolment() {
            return allowEnrolment;
        }

        public void setAllowEnrolment(Integer allowEnrolment) {
            this.allowEnrolment = allowEnrolment;
        }

        public Integer getChargeRate() {
            return chargeRate;
        }

        public void setChargeRate(Integer chargeRate) {
            this.chargeRate = chargeRate;
        }

        public String getRateDetails() {
            return rateDetails;
        }

        public void setRateDetails(String rateDetails) {
            this.rateDetails = rateDetails;
        }

        public Integer getUsertypeid() {
            return usertypeid;
        }

        public void setUsertypeid(Integer usertypeid) {
            this.usertypeid = usertypeid;
        }

        public Boolean getIsMobilePinSet() {
            return isMobilePinSet;
        }

        public void setIsMobilePinSet(Boolean isMobilePinSet) {
            this.isMobilePinSet = isMobilePinSet;
        }

        public Permissions getPermissions() {
            return permissions;
        }

        public void setPermissions(Permissions permissions) {
            this.permissions = permissions;
        }

        public List<PermissionSection> getPermissionSection() {
            return permissionSection;
        }

        public void setPermissionSection(List<PermissionSection> permissionSection) {
            this.permissionSection = permissionSection;
        }

        public List<ProjectDetails> getProjects() {
            return projects;
        }

        public void setProjects(List<ProjectDetails> projects) {
            this.projects = projects;
        }

        public ApkModel getApk() {
            return apk;
        }

        public void setApk(ApkModel apk) {
            this.apk = apk;
        }
    }


    public class Permissions {

        @SerializedName("notification_manager")
        @Expose
        private NotificationManager notificationManager;
        @SerializedName("projects")
        @Expose
        private Projects projects;
        @SerializedName("delivery")
        @Expose
        private Delivery delivery;
        @SerializedName("documents")
        @Expose
        private Documents documents;
        @SerializedName("users")
        @Expose
        private Users users;
        @SerializedName("cloud_storage")
        @Expose
        private CloudStorage cloudStorage;
        @SerializedName("mails")
        @Expose
        private Mails mails;
        @SerializedName("defect_report")
        @Expose
        private DefectReport defectReport;
        @SerializedName("reports")
        @Expose
        private Reports reports;
        @SerializedName("sub_contract_package")
        @Expose
        private SubContractPackage subContractPackage;
        @SerializedName("customer_management")
        @Expose
        private CustomerManagement customerManagement;
        @SerializedName("site_induction")
        @Expose
        private SiteInduction siteInduction;
        @SerializedName("request_for_approval")
        @Expose
        private RequestForApproval requestForApproval;
        @SerializedName("sms_notification_manager")
        @Expose
        private SmsNotificationManager smsNotificationManager;
        @SerializedName("tags")
        @Expose
        private Tags tags;
        @SerializedName("photo_Gallery")
        @Expose
        private PhotoGallery photoGallery;
        @SerializedName("inspection")
        @Expose
        private Inspection inspection;
        @SerializedName("timesheets_new")
        @Expose
        private TimesheetsNew timesheetsNew;
        @SerializedName("interactive_drawing")
        @Expose
        private InteractiveDrawing interactiveDrawing;
        @SerializedName("daily_diaries")
        @Expose
        private DailyDiaries dailyDiaries;
        @SerializedName("training_compliance")
        @Expose
        private TrainingCompliance trainingCompliance;
        @SerializedName("database_importer")
        @Expose
        private DatabaseImporter databaseImporter;
        @SerializedName("drawings")
        @Expose
        private Drawings drawings;
        @SerializedName("signinginout")
        @Expose
        private Signinginout signinginout;
        @SerializedName("asset_manager")
        @Expose
        private AssetManager assetManager;
        @SerializedName("allocations")
        @Expose
        private Allocations allocations;
        @SerializedName("project_scheduling")
        @Expose
        private ProjectScheduling projectScheduling;
        @SerializedName("rfi")
        @Expose
        private Rfi rfi;
        @SerializedName("manage_localisation")
        @Expose
        private ManageLocalisation manageLocalisation;
        @SerializedName("activity_manager")
        @Expose
        private ActivityManager activityManager;
        @SerializedName("training_plateform")
        @Expose
        private TrainingPlateform trainingPlateform;
        @SerializedName("faq")
        @Expose
        private Faq faq;
        @SerializedName("procurements")
        @Expose
        private Procurements procurements;
        @SerializedName("company_enrolment")
        @Expose
        private CompanyEnrolment companyEnrolment;
        @SerializedName("overview")
        @Expose
        private Overview overview;
        @SerializedName("custom_documents")
        @Expose
        private CustomDocuments customDocuments;
        @SerializedName("submittals")
        @Expose
        private Submittals submittals;
        @SerializedName("todos")
        @Expose
        private Todos todos;
        @SerializedName("variations")
        @Expose
        private Variations variations;
        @SerializedName("assets")
        @Expose
        private Assets assets;
        @SerializedName("bimers")
        @Expose
        private Bimers bimers;
        @SerializedName("chat_system")
        @Expose
        private ChatSystem chatSystem;
        @SerializedName("fleets")
        @Expose
        private Fleets fleets;
        @SerializedName("cost_tracking")
        @Expose
        private CostTracking costTracking;
        @SerializedName("invoices")
        @Expose
        private Invoices invoices;
        @SerializedName("quotations")
        @Expose
        private Quotations quotations;
        @SerializedName("holiday")
        @Expose
        private Holiday holiday;
        @SerializedName("valuations")
        @Expose
        private Valuations valuations;

        public NotificationManager getNotificationManager() {
            return notificationManager;
        }

        public void setNotificationManager(NotificationManager notificationManager) {
            this.notificationManager = notificationManager;
        }

        public Projects getProjects() {
            return projects;
        }

        public void setProjects(Projects projects) {
            this.projects = projects;
        }

        public Delivery getDelivery() {
            return delivery;
        }

        public void setDelivery(Delivery delivery) {
            this.delivery = delivery;
        }

        public Documents getDocuments() {
            return documents;
        }

        public void setDocuments(Documents documents) {
            this.documents = documents;
        }

        public Users getUsers() {
            return users;
        }

        public void setUsers(Users users) {
            this.users = users;
        }

        public CloudStorage getCloudStorage() {
            return cloudStorage;
        }

        public void setCloudStorage(CloudStorage cloudStorage) {
            this.cloudStorage = cloudStorage;
        }

        public Mails getMails() {
            return mails;
        }

        public void setMails(Mails mails) {
            this.mails = mails;
        }

        public DefectReport getDefectReport() {
            return defectReport;
        }

        public void setDefectReport(DefectReport defectReport) {
            this.defectReport = defectReport;
        }

        public Reports getReports() {
            return reports;
        }

        public void setReports(Reports reports) {
            this.reports = reports;
        }

        public SubContractPackage getSubContractPackage() {
            return subContractPackage;
        }

        public void setSubContractPackage(SubContractPackage subContractPackage) {
            this.subContractPackage = subContractPackage;
        }

        public CustomerManagement getCustomerManagement() {
            return customerManagement;
        }

        public void setCustomerManagement(CustomerManagement customerManagement) {
            this.customerManagement = customerManagement;
        }

        public SiteInduction getSiteInduction() {
            return siteInduction;
        }

        public void setSiteInduction(SiteInduction siteInduction) {
            this.siteInduction = siteInduction;
        }

        public RequestForApproval getRequestForApproval() {
            return requestForApproval;
        }

        public void setRequestForApproval(RequestForApproval requestForApproval) {
            this.requestForApproval = requestForApproval;
        }

        public SmsNotificationManager getSmsNotificationManager() {
            return smsNotificationManager;
        }

        public void setSmsNotificationManager(SmsNotificationManager smsNotificationManager) {
            this.smsNotificationManager = smsNotificationManager;
        }

        public Tags getTags() {
            return tags;
        }

        public void setTags(Tags tags) {
            this.tags = tags;
        }

        public PhotoGallery getPhotoGallery() {
            return photoGallery;
        }

        public void setPhotoGallery(PhotoGallery photoGallery) {
            this.photoGallery = photoGallery;
        }

        public Inspection getInspection() {
            return inspection;
        }

        public void setInspection(Inspection inspection) {
            this.inspection = inspection;
        }

        public TimesheetsNew getTimesheetsNew() {
            return timesheetsNew;
        }

        public void setTimesheetsNew(TimesheetsNew timesheetsNew) {
            this.timesheetsNew = timesheetsNew;
        }

        public InteractiveDrawing getInteractiveDrawing() {
            return interactiveDrawing;
        }

        public void setInteractiveDrawing(InteractiveDrawing interactiveDrawing) {
            this.interactiveDrawing = interactiveDrawing;
        }

        public DailyDiaries getDailyDiaries() {
            return dailyDiaries;
        }

        public void setDailyDiaries(DailyDiaries dailyDiaries) {
            this.dailyDiaries = dailyDiaries;
        }

        public TrainingCompliance getTrainingCompliance() {
            return trainingCompliance;
        }

        public void setTrainingCompliance(TrainingCompliance trainingCompliance) {
            this.trainingCompliance = trainingCompliance;
        }

        public DatabaseImporter getDatabaseImporter() {
            return databaseImporter;
        }

        public void setDatabaseImporter(DatabaseImporter databaseImporter) {
            this.databaseImporter = databaseImporter;
        }

        public Drawings getDrawings() {
            return drawings;
        }

        public void setDrawings(Drawings drawings) {
            this.drawings = drawings;
        }

        public Signinginout getSigninginout() {
            return signinginout;
        }

        public void setSigninginout(Signinginout signinginout) {
            this.signinginout = signinginout;
        }

        public AssetManager getAssetManager() {
            return assetManager;
        }

        public void setAssetManager(AssetManager assetManager) {
            this.assetManager = assetManager;
        }

        public Allocations getAllocations() {
            return allocations;
        }

        public void setAllocations(Allocations allocations) {
            this.allocations = allocations;
        }

        public ProjectScheduling getProjectScheduling() {
            return projectScheduling;
        }

        public void setProjectScheduling(ProjectScheduling projectScheduling) {
            this.projectScheduling = projectScheduling;
        }

        public Rfi getRfi() {
            return rfi;
        }

        public void setRfi(Rfi rfi) {
            this.rfi = rfi;
        }

        public ManageLocalisation getManageLocalisation() {
            return manageLocalisation;
        }

        public void setManageLocalisation(ManageLocalisation manageLocalisation) {
            this.manageLocalisation = manageLocalisation;
        }

        public ActivityManager getActivityManager() {
            return activityManager;
        }

        public void setActivityManager(ActivityManager activityManager) {
            this.activityManager = activityManager;
        }

        public TrainingPlateform getTrainingPlateform() {
            return trainingPlateform;
        }

        public void setTrainingPlateform(TrainingPlateform trainingPlateform) {
            this.trainingPlateform = trainingPlateform;
        }

        public Faq getFaq() {
            return faq;
        }

        public void setFaq(Faq faq) {
            this.faq = faq;
        }

        public Procurements getProcurements() {
            return procurements;
        }

        public void setProcurements(Procurements procurements) {
            this.procurements = procurements;
        }

        public CompanyEnrolment getCompanyEnrolment() {
            return companyEnrolment;
        }

        public void setCompanyEnrolment(CompanyEnrolment companyEnrolment) {
            this.companyEnrolment = companyEnrolment;
        }

        public Overview getOverview() {
            return overview;
        }

        public void setOverview(Overview overview) {
            this.overview = overview;
        }

        public CustomDocuments getCustomDocuments() {
            return customDocuments;
        }

        public void setCustomDocuments(CustomDocuments customDocuments) {
            this.customDocuments = customDocuments;
        }

        public Submittals getSubmittals() {
            return submittals;
        }

        public void setSubmittals(Submittals submittals) {
            this.submittals = submittals;
        }

        public Todos getTodos() {
            return todos;
        }

        public void setTodos(Todos todos) {
            this.todos = todos;
        }

        public Variations getVariations() {
            return variations;
        }

        public void setVariations(Variations variations) {
            this.variations = variations;
        }

        public Assets getAssets() {
            return assets;
        }

        public void setAssets(Assets assets) {
            this.assets = assets;
        }

        public Bimers getBimers() {
            return bimers;
        }

        public void setBimers(Bimers bimers) {
            this.bimers = bimers;
        }

        public ChatSystem getChatSystem() {
            return chatSystem;
        }

        public void setChatSystem(ChatSystem chatSystem) {
            this.chatSystem = chatSystem;
        }

        public Fleets getFleets() {
            return fleets;
        }

        public void setFleets(Fleets fleets) {
            this.fleets = fleets;
        }

        public CostTracking getCostTracking() {
            return costTracking;
        }

        public void setCostTracking(CostTracking costTracking) {
            this.costTracking = costTracking;
        }

        public Invoices getInvoices() {
            return invoices;
        }

        public void setInvoices(Invoices invoices) {
            this.invoices = invoices;
        }

        public Quotations getQuotations() {
            return quotations;
        }

        public void setQuotations(Quotations quotations) {
            this.quotations = quotations;
        }

        public Holiday getHoliday() {
            return holiday;
        }

        public void setHoliday(Holiday holiday) {
            this.holiday = holiday;
        }

        public Valuations getValuations() {
            return valuations;
        }

        public void setValuations(Valuations valuations) {
            this.valuations = valuations;
        }

    }

    public class Fleets {

        @SerializedName("view_fleets")
        @Expose
        private String viewFleets;
        @SerializedName("assign_users_to_vehicle")
        @Expose
        private String assignUsersToVehicle;
        @SerializedName("clock_in_clock_out")
        @Expose
        private String clockInClockOut;
        @SerializedName("add_damage")
        @Expose
        private String addDamage;
        @SerializedName("add_vehicle_history")
        @Expose
        private String addVehicleHistory;
        @SerializedName("add_comment")
        @Expose
        private String addComment;
        @SerializedName("add_edit_vehicle")
        @Expose
        private String addEditVehicle;
        @SerializedName("due_date_completed")
        @Expose
        private String dueDateCompleted;
        @SerializedName("book_mot")
        @Expose
        private String bookMot;
        @SerializedName("manage_vehicle_type")
        @Expose
        private String manageVehicleType;
        @SerializedName("manage_tests")
        @Expose
        private String manageTests;
        @SerializedName("manage_notifications")
        @Expose
        private String manageNotifications;

        public String getViewFleets() {
            return viewFleets;
        }

        public void setViewFleets(String viewFleets) {
            this.viewFleets = viewFleets;
        }

        public String getAssignUsersToVehicle() {
            return assignUsersToVehicle;
        }

        public void setAssignUsersToVehicle(String assignUsersToVehicle) {
            this.assignUsersToVehicle = assignUsersToVehicle;
        }

        public String getClockInClockOut() {
            return clockInClockOut;
        }

        public void setClockInClockOut(String clockInClockOut) {
            this.clockInClockOut = clockInClockOut;
        }

        public String getAddDamage() {
            return addDamage;
        }

        public void setAddDamage(String addDamage) {
            this.addDamage = addDamage;
        }

        public String getAddVehicleHistory() {
            return addVehicleHistory;
        }

        public void setAddVehicleHistory(String addVehicleHistory) {
            this.addVehicleHistory = addVehicleHistory;
        }

        public String getAddComment() {
            return addComment;
        }

        public void setAddComment(String addComment) {
            this.addComment = addComment;
        }

        public String getAddEditVehicle() {
            return addEditVehicle;
        }

        public void setAddEditVehicle(String addEditVehicle) {
            this.addEditVehicle = addEditVehicle;
        }

        public String getDueDateCompleted() {
            return dueDateCompleted;
        }

        public void setDueDateCompleted(String dueDateCompleted) {
            this.dueDateCompleted = dueDateCompleted;
        }

        public String getBookMot() {
            return bookMot;
        }

        public void setBookMot(String bookMot) {
            this.bookMot = bookMot;
        }

        public String getManageVehicleType() {
            return manageVehicleType;
        }

        public void setManageVehicleType(String manageVehicleType) {
            this.manageVehicleType = manageVehicleType;
        }

        public String getManageTests() {
            return manageTests;
        }

        public void setManageTests(String manageTests) {
            this.manageTests = manageTests;
        }

        public String getManageNotifications() {
            return manageNotifications;
        }

        public void setManageNotifications(String manageNotifications) {
            this.manageNotifications = manageNotifications;
        }

    }

    public class Faq {

        @SerializedName("view_faq")
        @Expose
        private String viewFaq;
        @SerializedName("crude_category")
        @Expose
        private String crudeCategory;
        @SerializedName("can_answer")
        @Expose
        private String canAnswer;

        public String getViewFaq() {
            return viewFaq;
        }

        public void setViewFaq(String viewFaq) {
            this.viewFaq = viewFaq;
        }

        public String getCrudeCategory() {
            return crudeCategory;
        }

        public void setCrudeCategory(String crudeCategory) {
            this.crudeCategory = crudeCategory;
        }

        public String getCanAnswer() {
            return canAnswer;
        }

        public void setCanAnswer(String canAnswer) {
            this.canAnswer = canAnswer;
        }

    }

    public class Drawings {

        @SerializedName("create_draw")
        @Expose
        private String createDraw;
        @SerializedName("update_draw")
        @Expose
        private String updateDraw;
        @SerializedName("manage_cat")
        @Expose
        private String manageCat;
        @SerializedName("manage_stats")
        @Expose
        private String manageStats;
        @SerializedName("quarantine_drawing")
        @Expose
        private String quarantineDrawing;
        @SerializedName("archive_drawing")
        @Expose
        private String archiveDrawing;
        @SerializedName("view_drawing")
        @Expose
        private String viewDrawing;
        @SerializedName("pinned_comment")
        @Expose
        private String pinnedComment;
        @SerializedName("manage_groups")
        @Expose
        private String manageGroups;
        @SerializedName("view_history")
        @Expose
        private String viewHistory;
        @SerializedName("sending_a_drawing_via_email")
        @Expose
        private String sendingADrawingViaEmail;
        @SerializedName("generate_issue_sheet")
        @Expose
        private String generateIssueSheet;
        @SerializedName("manage_custom_field")
        @Expose
        private String manageCustomField;

        public String getCreateDraw() {
            return createDraw;
        }

        public void setCreateDraw(String createDraw) {
            this.createDraw = createDraw;
        }

        public String getUpdateDraw() {
            return updateDraw;
        }

        public void setUpdateDraw(String updateDraw) {
            this.updateDraw = updateDraw;
        }

        public String getManageCat() {
            return manageCat;
        }

        public void setManageCat(String manageCat) {
            this.manageCat = manageCat;
        }

        public String getManageStats() {
            return manageStats;
        }

        public void setManageStats(String manageStats) {
            this.manageStats = manageStats;
        }

        public String getQuarantineDrawing() {
            return quarantineDrawing;
        }

        public void setQuarantineDrawing(String quarantineDrawing) {
            this.quarantineDrawing = quarantineDrawing;
        }

        public String getArchiveDrawing() {
            return archiveDrawing;
        }

        public void setArchiveDrawing(String archiveDrawing) {
            this.archiveDrawing = archiveDrawing;
        }

        public String getViewDrawing() {
            return viewDrawing;
        }

        public void setViewDrawing(String viewDrawing) {
            this.viewDrawing = viewDrawing;
        }

        public String getPinnedComment() {
            return pinnedComment;
        }

        public void setPinnedComment(String pinnedComment) {
            this.pinnedComment = pinnedComment;
        }

        public String getManageGroups() {
            return manageGroups;
        }

        public void setManageGroups(String manageGroups) {
            this.manageGroups = manageGroups;
        }

        public String getViewHistory() {
            return viewHistory;
        }

        public void setViewHistory(String viewHistory) {
            this.viewHistory = viewHistory;
        }

        public String getSendingADrawingViaEmail() {
            return sendingADrawingViaEmail;
        }

        public void setSendingADrawingViaEmail(String sendingADrawingViaEmail) {
            this.sendingADrawingViaEmail = sendingADrawingViaEmail;
        }

        public String getGenerateIssueSheet() {
            return generateIssueSheet;
        }

        public void setGenerateIssueSheet(String generateIssueSheet) {
            this.generateIssueSheet = generateIssueSheet;
        }

        public String getManageCustomField() {
            return manageCustomField;
        }

        public void setManageCustomField(String manageCustomField) {
            this.manageCustomField = manageCustomField;
        }

    }

    public class Documents {

        @SerializedName("create_doc")
        @Expose
        private String createDoc;
        @SerializedName("manage_doc")
        @Expose
        private String manageDoc;
        @SerializedName("delete_doc")
        @Expose
        private String deleteDoc;
        @SerializedName("edit_doc")
        @Expose
        private String editDoc;
        @SerializedName("update_doc")
        @Expose
        private String updateDoc;
        @SerializedName("view_doc")
        @Expose
        private String viewDoc;
        @SerializedName("pinned_comment")
        @Expose
        private String pinnedComment;
        @SerializedName("view_history")
        @Expose
        private String viewHistory;
        @SerializedName("manage_custom_field")
        @Expose
        private String manageCustomField;
        @SerializedName("sending_a_document_via_email")
        @Expose
        private String sendingADocumentViaEmail;

        public String getCreateDoc() {
            return createDoc;
        }

        public void setCreateDoc(String createDoc) {
            this.createDoc = createDoc;
        }

        public String getManageDoc() {
            return manageDoc;
        }

        public void setManageDoc(String manageDoc) {
            this.manageDoc = manageDoc;
        }

        public String getDeleteDoc() {
            return deleteDoc;
        }

        public void setDeleteDoc(String deleteDoc) {
            this.deleteDoc = deleteDoc;
        }

        public String getEditDoc() {
            return editDoc;
        }

        public void setEditDoc(String editDoc) {
            this.editDoc = editDoc;
        }

        public String getUpdateDoc() {
            return updateDoc;
        }

        public void setUpdateDoc(String updateDoc) {
            this.updateDoc = updateDoc;
        }

        public String getViewDoc() {
            return viewDoc;
        }

        public void setViewDoc(String viewDoc) {
            this.viewDoc = viewDoc;
        }

        public String getPinnedComment() {
            return pinnedComment;
        }

        public void setPinnedComment(String pinnedComment) {
            this.pinnedComment = pinnedComment;
        }

        public String getViewHistory() {
            return viewHistory;
        }

        public void setViewHistory(String viewHistory) {
            this.viewHistory = viewHistory;
        }

        public String getManageCustomField() {
            return manageCustomField;
        }

        public void setManageCustomField(String manageCustomField) {
            this.manageCustomField = manageCustomField;
        }

        public String getSendingADocumentViaEmail() {
            return sendingADocumentViaEmail;
        }

        public void setSendingADocumentViaEmail(String sendingADocumentViaEmail) {
            this.sendingADocumentViaEmail = sendingADocumentViaEmail;
        }

    }

    public class Delivery {

        @SerializedName("mng_restrctn")
        @Expose
        private String mngRestrctn;
        @SerializedName("mng_dlivrypoint")
        @Expose
        private String mngDlivrypoint;
        @SerializedName("create_delivry")
        @Expose
        private String createDelivry;
        @SerializedName("accept_decln_delvry")
        @Expose
        private String acceptDeclnDelvry;
        @SerializedName("a_contrator_del")
        @Expose
        private String aContratorDel;
        @SerializedName("delete_booking")
        @Expose
        private String deleteBooking;
        @SerializedName("view_deliv")
        @Expose
        private String viewDeliv;

        public String getMngRestrctn() {
            return mngRestrctn;
        }

        public void setMngRestrctn(String mngRestrctn) {
            this.mngRestrctn = mngRestrctn;
        }

        public String getMngDlivrypoint() {
            return mngDlivrypoint;
        }

        public void setMngDlivrypoint(String mngDlivrypoint) {
            this.mngDlivrypoint = mngDlivrypoint;
        }

        public String getCreateDelivry() {
            return createDelivry;
        }

        public void setCreateDelivry(String createDelivry) {
            this.createDelivry = createDelivry;
        }

        public String getAcceptDeclnDelvry() {
            return acceptDeclnDelvry;
        }

        public void setAcceptDeclnDelvry(String acceptDeclnDelvry) {
            this.acceptDeclnDelvry = acceptDeclnDelvry;
        }

        public String getAContratorDel() {
            return aContratorDel;
        }

        public void setAContratorDel(String aContratorDel) {
            this.aContratorDel = aContratorDel;
        }

        public String getDeleteBooking() {
            return deleteBooking;
        }

        public void setDeleteBooking(String deleteBooking) {
            this.deleteBooking = deleteBooking;
        }

        public String getViewDeliv() {
            return viewDeliv;
        }

        public void setViewDeliv(String viewDeliv) {
            this.viewDeliv = viewDeliv;
        }

    }

    public class DefectReport {

        @SerializedName("view_snag")
        @Expose
        private String viewSnag;
        @SerializedName("create")
        @Expose
        private String create;
        @SerializedName("mark_quarantine")
        @Expose
        private String markQuarantine;

        public String getViewSnag() {
            return viewSnag;
        }

        public void setViewSnag(String viewSnag) {
            this.viewSnag = viewSnag;
        }

        public String getCreate() {
            return create;
        }

        public void setCreate(String create) {
            this.create = create;
        }

        public String getMarkQuarantine() {
            return markQuarantine;
        }

        public void setMarkQuarantine(String markQuarantine) {
            this.markQuarantine = markQuarantine;
        }

    }

    public class DatabaseImporter {

        @SerializedName("view_database_importer")
        @Expose
        private String viewDatabaseImporter;

        public String getViewDatabaseImporter() {
            return viewDatabaseImporter;
        }

        public void setViewDatabaseImporter(String viewDatabaseImporter) {
            this.viewDatabaseImporter = viewDatabaseImporter;
        }

    }

    public class DailyDiaries {

        @SerializedName("create_dairy")
        @Expose
        private String createDairy;
        @SerializedName("upload_file")
        @Expose
        private String uploadFile;
        @SerializedName("view_d_dairy")
        @Expose
        private String viewDDairy;
        @SerializedName("view_Allproject_dairy")
        @Expose
        private String viewAllprojectDairy;
        @SerializedName("create_custom_fields")
        @Expose
        private String createCustomFields;
        @SerializedName("export_daily_diary")
        @Expose
        private String exportDailyDiary;
        @SerializedName("view_diary_revision_history")
        @Expose
        private String viewDiaryRevisionHistory;

        public String getCreateDairy() {
            return createDairy;
        }

        public void setCreateDairy(String createDairy) {
            this.createDairy = createDairy;
        }

        public String getUploadFile() {
            return uploadFile;
        }

        public void setUploadFile(String uploadFile) {
            this.uploadFile = uploadFile;
        }

        public String getViewDDairy() {
            return viewDDairy;
        }

        public void setViewDDairy(String viewDDairy) {
            this.viewDDairy = viewDDairy;
        }

        public String getViewAllprojectDairy() {
            return viewAllprojectDairy;
        }

        public void setViewAllprojectDairy(String viewAllprojectDairy) {
            this.viewAllprojectDairy = viewAllprojectDairy;
        }

        public String getCreateCustomFields() {
            return createCustomFields;
        }

        public void setCreateCustomFields(String createCustomFields) {
            this.createCustomFields = createCustomFields;
        }

        public String getExportDailyDiary() {
            return exportDailyDiary;
        }

        public void setExportDailyDiary(String exportDailyDiary) {
            this.exportDailyDiary = exportDailyDiary;
        }

        public String getViewDiaryRevisionHistory() {
            return viewDiaryRevisionHistory;
        }

        public void setViewDiaryRevisionHistory(String viewDiaryRevisionHistory) {
            this.viewDiaryRevisionHistory = viewDiaryRevisionHistory;
        }

    }

    public class CustomerManagement {

        @SerializedName("view_crm")
        @Expose
        private String viewCrm;
        @SerializedName("assign_project")
        @Expose
        private String assignProject;
        @SerializedName("view_invoices")
        @Expose
        private String viewInvoices;
        @SerializedName("view_tasks")
        @Expose
        private String viewTasks;
        @SerializedName("view_rfis")
        @Expose
        private String viewRfis;
        @SerializedName("view_defects")
        @Expose
        private String viewDefects;
        @SerializedName("create")
        @Expose
        private String create;
        @SerializedName("delete_company")
        @Expose
        private String deleteCompany;
        @SerializedName("manage_cmpny_attr")
        @Expose
        private String manageCmpnyAttr;
        @SerializedName("view_supplier")
        @Expose
        private String viewSupplier;
        @SerializedName("create_supplier")
        @Expose
        private String createSupplier;
        @SerializedName("view_supplier_order")
        @Expose
        private String viewSupplierOrder;
        @SerializedName("view_add_notes")
        @Expose
        private String viewAddNotes;
        @SerializedName("manage_company_status")
        @Expose
        private String manageCompanyStatus;

        public String getViewCrm() {
            return viewCrm;
        }

        public void setViewCrm(String viewCrm) {
            this.viewCrm = viewCrm;
        }

        public String getAssignProject() {
            return assignProject;
        }

        public void setAssignProject(String assignProject) {
            this.assignProject = assignProject;
        }

        public String getViewInvoices() {
            return viewInvoices;
        }

        public void setViewInvoices(String viewInvoices) {
            this.viewInvoices = viewInvoices;
        }

        public String getViewTasks() {
            return viewTasks;
        }

        public void setViewTasks(String viewTasks) {
            this.viewTasks = viewTasks;
        }

        public String getViewRfis() {
            return viewRfis;
        }

        public void setViewRfis(String viewRfis) {
            this.viewRfis = viewRfis;
        }

        public String getViewDefects() {
            return viewDefects;
        }

        public void setViewDefects(String viewDefects) {
            this.viewDefects = viewDefects;
        }

        public String getCreate() {
            return create;
        }

        public void setCreate(String create) {
            this.create = create;
        }

        public String getDeleteCompany() {
            return deleteCompany;
        }

        public void setDeleteCompany(String deleteCompany) {
            this.deleteCompany = deleteCompany;
        }

        public String getManageCmpnyAttr() {
            return manageCmpnyAttr;
        }

        public void setManageCmpnyAttr(String manageCmpnyAttr) {
            this.manageCmpnyAttr = manageCmpnyAttr;
        }

        public String getViewSupplier() {
            return viewSupplier;
        }

        public void setViewSupplier(String viewSupplier) {
            this.viewSupplier = viewSupplier;
        }

        public String getCreateSupplier() {
            return createSupplier;
        }

        public void setCreateSupplier(String createSupplier) {
            this.createSupplier = createSupplier;
        }

        public String getViewSupplierOrder() {
            return viewSupplierOrder;
        }

        public void setViewSupplierOrder(String viewSupplierOrder) {
            this.viewSupplierOrder = viewSupplierOrder;
        }

        public String getViewAddNotes() {
            return viewAddNotes;
        }

        public void setViewAddNotes(String viewAddNotes) {
            this.viewAddNotes = viewAddNotes;
        }

        public String getManageCompanyStatus() {
            return manageCompanyStatus;
        }

        public void setManageCompanyStatus(String manageCompanyStatus) {
            this.manageCompanyStatus = manageCompanyStatus;
        }

    }

    public class CustomDocuments {

        @SerializedName("manage_templates")
        @Expose
        private String manageTemplates;
        @SerializedName("manage_documents")
        @Expose
        private String manageDocuments;
        @SerializedName("issue_digital_documents")
        @Expose
        private String issueDigitalDocuments;
        @SerializedName("create_documents")
        @Expose
        private String createDocuments;
        @SerializedName("digital_category")
        @Expose
        private String digitalCategory;

        public String getManageTemplates() {
            return manageTemplates;
        }

        public void setManageTemplates(String manageTemplates) {
            this.manageTemplates = manageTemplates;
        }

        public String getManageDocuments() {
            return manageDocuments;
        }

        public void setManageDocuments(String manageDocuments) {
            this.manageDocuments = manageDocuments;
        }

        public String getIssueDigitalDocuments() {
            return issueDigitalDocuments;
        }

        public void setIssueDigitalDocuments(String issueDigitalDocuments) {
            this.issueDigitalDocuments = issueDigitalDocuments;
        }

        public String getCreateDocuments() {
            return createDocuments;
        }

        public void setCreateDocuments(String createDocuments) {
            this.createDocuments = createDocuments;
        }

        public String getDigitalCategory() {
            return digitalCategory;
        }

        public void setDigitalCategory(String digitalCategory) {
            this.digitalCategory = digitalCategory;
        }

    }

    public class CostTracking {

        @SerializedName("view_cost_tracking")
        @Expose
        private String viewCostTracking;
        @SerializedName("view_quants")
        @Expose
        private String viewQuants;
        @SerializedName("create_cost_code")
        @Expose
        private String createCostCode;

        public String getViewCostTracking() {
            return viewCostTracking;
        }

        public void setViewCostTracking(String viewCostTracking) {
            this.viewCostTracking = viewCostTracking;
        }

        public String getViewQuants() {
            return viewQuants;
        }

        public void setViewQuants(String viewQuants) {
            this.viewQuants = viewQuants;
        }

        public String getCreateCostCode() {
            return createCostCode;
        }

        public void setCreateCostCode(String createCostCode) {
            this.createCostCode = createCostCode;
        }

    }

    public class CompanyEnrolment {

        @SerializedName("view_company_enrolment")
        @Expose
        private String viewCompanyEnrolment;
        @SerializedName("add_induction_company")
        @Expose
        private String addInductionCompany;
        @SerializedName("manage_subcontractor_inductees")
        @Expose
        private String manageSubcontractorInductees;
        @SerializedName("sign_off_company_induction")
        @Expose
        private String signOffCompanyInduction;
        @SerializedName("create_checklist")
        @Expose
        private String createChecklist;
        @SerializedName("view_checklist")
        @Expose
        private String viewChecklist;
        @SerializedName("manage_pre_start_document")
        @Expose
        private String managePreStartDocument;
        @SerializedName("view_signed_off_company")
        @Expose
        private String viewSignedOffCompany;
        @SerializedName("create_company_enrolment_custom_fields")
        @Expose
        private String createCompanyEnrolmentCustomFields;

        public String getViewCompanyEnrolment() {
            return viewCompanyEnrolment;
        }

        public void setViewCompanyEnrolment(String viewCompanyEnrolment) {
            this.viewCompanyEnrolment = viewCompanyEnrolment;
        }

        public String getAddInductionCompany() {
            return addInductionCompany;
        }

        public void setAddInductionCompany(String addInductionCompany) {
            this.addInductionCompany = addInductionCompany;
        }

        public String getManageSubcontractorInductees() {
            return manageSubcontractorInductees;
        }

        public void setManageSubcontractorInductees(String manageSubcontractorInductees) {
            this.manageSubcontractorInductees = manageSubcontractorInductees;
        }

        public String getSignOffCompanyInduction() {
            return signOffCompanyInduction;
        }

        public void setSignOffCompanyInduction(String signOffCompanyInduction) {
            this.signOffCompanyInduction = signOffCompanyInduction;
        }

        public String getCreateChecklist() {
            return createChecklist;
        }

        public void setCreateChecklist(String createChecklist) {
            this.createChecklist = createChecklist;
        }

        public String getViewChecklist() {
            return viewChecklist;
        }

        public void setViewChecklist(String viewChecklist) {
            this.viewChecklist = viewChecklist;
        }

        public String getManagePreStartDocument() {
            return managePreStartDocument;
        }

        public void setManagePreStartDocument(String managePreStartDocument) {
            this.managePreStartDocument = managePreStartDocument;
        }

        public String getViewSignedOffCompany() {
            return viewSignedOffCompany;
        }

        public void setViewSignedOffCompany(String viewSignedOffCompany) {
            this.viewSignedOffCompany = viewSignedOffCompany;
        }

        public String getCreateCompanyEnrolmentCustomFields() {
            return createCompanyEnrolmentCustomFields;
        }

        public void setCreateCompanyEnrolmentCustomFields(String createCompanyEnrolmentCustomFields) {
            this.createCompanyEnrolmentCustomFields = createCompanyEnrolmentCustomFields;
        }

    }

    public class CloudStorage {

        @SerializedName("view_cloud")
        @Expose
        private String viewCloud;
        @SerializedName("view_history")
        @Expose
        private String viewHistory;
        @SerializedName("create_file")
        @Expose
        private String createFile;
        @SerializedName("manage_category")
        @Expose
        private String manageCategory;
        @SerializedName("delete_file")
        @Expose
        private String deleteFile;
        @SerializedName("edit_file")
        @Expose
        private String editFile;
        @SerializedName("update_file")
        @Expose
        private String updateFile;
        @SerializedName("pinned_comment")
        @Expose
        private String pinnedComment;
        @SerializedName("manage_access_level")
        @Expose
        private String manageAccessLevel;
        @SerializedName("manage_custom_field")
        @Expose
        private String manageCustomField;
        @SerializedName("sending_a_document_via_email")
        @Expose
        private String sendingADocumentViaEmail;

        public String getViewCloud() {
            return viewCloud;
        }

        public void setViewCloud(String viewCloud) {
            this.viewCloud = viewCloud;
        }

        public String getViewHistory() {
            return viewHistory;
        }

        public void setViewHistory(String viewHistory) {
            this.viewHistory = viewHistory;
        }

        public String getCreateFile() {
            return createFile;
        }

        public void setCreateFile(String createFile) {
            this.createFile = createFile;
        }

        public String getManageCategory() {
            return manageCategory;
        }

        public void setManageCategory(String manageCategory) {
            this.manageCategory = manageCategory;
        }

        public String getDeleteFile() {
            return deleteFile;
        }

        public void setDeleteFile(String deleteFile) {
            this.deleteFile = deleteFile;
        }

        public String getEditFile() {
            return editFile;
        }

        public void setEditFile(String editFile) {
            this.editFile = editFile;
        }

        public String getUpdateFile() {
            return updateFile;
        }

        public void setUpdateFile(String updateFile) {
            this.updateFile = updateFile;
        }

        public String getPinnedComment() {
            return pinnedComment;
        }

        public void setPinnedComment(String pinnedComment) {
            this.pinnedComment = pinnedComment;
        }

        public String getManageAccessLevel() {
            return manageAccessLevel;
        }

        public void setManageAccessLevel(String manageAccessLevel) {
            this.manageAccessLevel = manageAccessLevel;
        }

        public String getManageCustomField() {
            return manageCustomField;
        }

        public void setManageCustomField(String manageCustomField) {
            this.manageCustomField = manageCustomField;
        }

        public String getSendingADocumentViaEmail() {
            return sendingADocumentViaEmail;
        }

        public void setSendingADocumentViaEmail(String sendingADocumentViaEmail) {
            this.sendingADocumentViaEmail = sendingADocumentViaEmail;
        }

    }

    public class ChatSystem {

        @SerializedName("view_chat")
        @Expose
        private String viewChat;

        public String getViewChat() {
            return viewChat;
        }

        public void setViewChat(String viewChat) {
            this.viewChat = viewChat;
        }

    }

    public class Bimers {

        @SerializedName("create_draw")
        @Expose
        private String createDraw;
        @SerializedName("update_draw")
        @Expose
        private String updateDraw;
        @SerializedName("manage_cat")
        @Expose
        private String manageCat;
        @SerializedName("manage_stats")
        @Expose
        private String manageStats;
        @SerializedName("quarantine_drawing")
        @Expose
        private String quarantineDrawing;
        @SerializedName("archive_drawing")
        @Expose
        private String archiveDrawing;
        @SerializedName("view_drawing")
        @Expose
        private String viewDrawing;
        @SerializedName("pinned_comment")
        @Expose
        private String pinnedComment;
        @SerializedName("manage_groups")
        @Expose
        private String manageGroups;
        @SerializedName("view_history")
        @Expose
        private String viewHistory;
        @SerializedName("sending_a_drawing_via_email")
        @Expose
        private String sendingADrawingViaEmail;
        @SerializedName("generate_issue_sheet")
        @Expose
        private String generateIssueSheet;
        @SerializedName("manage_custom_field")
        @Expose
        private String manageCustomField;

        public String getCreateDraw() {
            return createDraw;
        }

        public void setCreateDraw(String createDraw) {
            this.createDraw = createDraw;
        }

        public String getUpdateDraw() {
            return updateDraw;
        }

        public void setUpdateDraw(String updateDraw) {
            this.updateDraw = updateDraw;
        }

        public String getManageCat() {
            return manageCat;
        }

        public void setManageCat(String manageCat) {
            this.manageCat = manageCat;
        }

        public String getManageStats() {
            return manageStats;
        }

        public void setManageStats(String manageStats) {
            this.manageStats = manageStats;
        }

        public String getQuarantineDrawing() {
            return quarantineDrawing;
        }

        public void setQuarantineDrawing(String quarantineDrawing) {
            this.quarantineDrawing = quarantineDrawing;
        }

        public String getArchiveDrawing() {
            return archiveDrawing;
        }

        public void setArchiveDrawing(String archiveDrawing) {
            this.archiveDrawing = archiveDrawing;
        }

        public String getViewDrawing() {
            return viewDrawing;
        }

        public void setViewDrawing(String viewDrawing) {
            this.viewDrawing = viewDrawing;
        }

        public String getPinnedComment() {
            return pinnedComment;
        }

        public void setPinnedComment(String pinnedComment) {
            this.pinnedComment = pinnedComment;
        }

        public String getManageGroups() {
            return manageGroups;
        }

        public void setManageGroups(String manageGroups) {
            this.manageGroups = manageGroups;
        }

        public String getViewHistory() {
            return viewHistory;
        }

        public void setViewHistory(String viewHistory) {
            this.viewHistory = viewHistory;
        }

        public String getSendingADrawingViaEmail() {
            return sendingADrawingViaEmail;
        }

        public void setSendingADrawingViaEmail(String sendingADrawingViaEmail) {
            this.sendingADrawingViaEmail = sendingADrawingViaEmail;
        }

        public String getGenerateIssueSheet() {
            return generateIssueSheet;
        }

        public void setGenerateIssueSheet(String generateIssueSheet) {
            this.generateIssueSheet = generateIssueSheet;
        }

        public String getManageCustomField() {
            return manageCustomField;
        }

        public void setManageCustomField(String manageCustomField) {
            this.manageCustomField = manageCustomField;
        }

    }

    public class Assets {

        @SerializedName("create_assets")
        @Expose
        private String createAssets;
        @SerializedName("import_assets")
        @Expose
        private String importAssets;
        @SerializedName("view_assets")
        @Expose
        private String viewAssets;
        @SerializedName("assign_users_to_assets")
        @Expose
        private String assignUsersToAssets;
        @SerializedName("edit_assets")
        @Expose
        private String editAssets;
        @SerializedName("mark_due_dates_complete")
        @Expose
        private String markDueDatesComplete;
        @SerializedName("add_assets_history")
        @Expose
        private String addAssetsHistory;
        @SerializedName("add_assets_event")
        @Expose
        private String addAssetsEvent;
        @SerializedName("view_add_attachments")
        @Expose
        private String viewAddAttachments;
        @SerializedName("view_add_comments")
        @Expose
        private String viewAddComments;
        @SerializedName("archive_assets")
        @Expose
        private String archiveAssets;
        @SerializedName("dalete_assets")
        @Expose
        private String daleteAssets;
        @SerializedName("manage_tests")
        @Expose
        private String manageTests;
        @SerializedName("manage_categories")
        @Expose
        private String manageCategories;
        @SerializedName("manage_notifications")
        @Expose
        private String manageNotifications;

        public String getCreateAssets() {
            return createAssets;
        }

        public void setCreateAssets(String createAssets) {
            this.createAssets = createAssets;
        }

        public String getImportAssets() {
            return importAssets;
        }

        public void setImportAssets(String importAssets) {
            this.importAssets = importAssets;
        }

        public String getViewAssets() {
            return viewAssets;
        }

        public void setViewAssets(String viewAssets) {
            this.viewAssets = viewAssets;
        }

        public String getAssignUsersToAssets() {
            return assignUsersToAssets;
        }

        public void setAssignUsersToAssets(String assignUsersToAssets) {
            this.assignUsersToAssets = assignUsersToAssets;
        }

        public String getEditAssets() {
            return editAssets;
        }

        public void setEditAssets(String editAssets) {
            this.editAssets = editAssets;
        }

        public String getMarkDueDatesComplete() {
            return markDueDatesComplete;
        }

        public void setMarkDueDatesComplete(String markDueDatesComplete) {
            this.markDueDatesComplete = markDueDatesComplete;
        }

        public String getAddAssetsHistory() {
            return addAssetsHistory;
        }

        public void setAddAssetsHistory(String addAssetsHistory) {
            this.addAssetsHistory = addAssetsHistory;
        }

        public String getAddAssetsEvent() {
            return addAssetsEvent;
        }

        public void setAddAssetsEvent(String addAssetsEvent) {
            this.addAssetsEvent = addAssetsEvent;
        }

        public String getViewAddAttachments() {
            return viewAddAttachments;
        }

        public void setViewAddAttachments(String viewAddAttachments) {
            this.viewAddAttachments = viewAddAttachments;
        }

        public String getViewAddComments() {
            return viewAddComments;
        }

        public void setViewAddComments(String viewAddComments) {
            this.viewAddComments = viewAddComments;
        }

        public String getArchiveAssets() {
            return archiveAssets;
        }

        public void setArchiveAssets(String archiveAssets) {
            this.archiveAssets = archiveAssets;
        }

        public String getDaleteAssets() {
            return daleteAssets;
        }

        public void setDaleteAssets(String daleteAssets) {
            this.daleteAssets = daleteAssets;
        }

        public String getManageTests() {
            return manageTests;
        }

        public void setManageTests(String manageTests) {
            this.manageTests = manageTests;
        }

        public String getManageCategories() {
            return manageCategories;
        }

        public void setManageCategories(String manageCategories) {
            this.manageCategories = manageCategories;
        }

        public String getManageNotifications() {
            return manageNotifications;
        }

        public void setManageNotifications(String manageNotifications) {
            this.manageNotifications = manageNotifications;
        }

    }

    public class AssetManager {

        @SerializedName("view_plant_manager")
        @Expose
        private String viewPlantManager;
        @SerializedName("hire_plant_tools")
        @Expose
        private String hirePlantTools;
        @SerializedName("create_plant_tools")
        @Expose
        private String createPlantTools;
        @SerializedName("project_manager_view")
        @Expose
        private String projectManagerView;
        @SerializedName("view_delivery_list")
        @Expose
        private String viewDeliveryList;
        @SerializedName("manage_categories")
        @Expose
        private String manageCategories;
        @SerializedName("manage_driver")
        @Expose
        private String manageDriver;
        @SerializedName("view_requested_plant_tools_listing")
        @Expose
        private String viewRequestedPlantToolsListing;
        @SerializedName("view_add_notification")
        @Expose
        private String viewAddNotification;
        @SerializedName("change_asset_rate")
        @Expose
        private String changeAssetRate;
        @SerializedName("test_asset")
        @Expose
        private String testAsset;
        @SerializedName("view_his_det")
        @Expose
        private String viewHisDet;
        @SerializedName("cloning")
        @Expose
        private String cloning;

        public String getViewPlantManager() {
            return viewPlantManager;
        }

        public void setViewPlantManager(String viewPlantManager) {
            this.viewPlantManager = viewPlantManager;
        }

        public String getHirePlantTools() {
            return hirePlantTools;
        }

        public void setHirePlantTools(String hirePlantTools) {
            this.hirePlantTools = hirePlantTools;
        }

        public String getCreatePlantTools() {
            return createPlantTools;
        }

        public void setCreatePlantTools(String createPlantTools) {
            this.createPlantTools = createPlantTools;
        }

        public String getProjectManagerView() {
            return projectManagerView;
        }

        public void setProjectManagerView(String projectManagerView) {
            this.projectManagerView = projectManagerView;
        }

        public String getViewDeliveryList() {
            return viewDeliveryList;
        }

        public void setViewDeliveryList(String viewDeliveryList) {
            this.viewDeliveryList = viewDeliveryList;
        }

        public String getManageCategories() {
            return manageCategories;
        }

        public void setManageCategories(String manageCategories) {
            this.manageCategories = manageCategories;
        }

        public String getManageDriver() {
            return manageDriver;
        }

        public void setManageDriver(String manageDriver) {
            this.manageDriver = manageDriver;
        }

        public String getViewRequestedPlantToolsListing() {
            return viewRequestedPlantToolsListing;
        }

        public void setViewRequestedPlantToolsListing(String viewRequestedPlantToolsListing) {
            this.viewRequestedPlantToolsListing = viewRequestedPlantToolsListing;
        }

        public String getViewAddNotification() {
            return viewAddNotification;
        }

        public void setViewAddNotification(String viewAddNotification) {
            this.viewAddNotification = viewAddNotification;
        }

        public String getChangeAssetRate() {
            return changeAssetRate;
        }

        public void setChangeAssetRate(String changeAssetRate) {
            this.changeAssetRate = changeAssetRate;
        }

        public String getTestAsset() {
            return testAsset;
        }

        public void setTestAsset(String testAsset) {
            this.testAsset = testAsset;
        }

        public String getViewHisDet() {
            return viewHisDet;
        }

        public void setViewHisDet(String viewHisDet) {
            this.viewHisDet = viewHisDet;
        }

        public String getCloning() {
            return cloning;
        }

        public void setCloning(String cloning) {
            this.cloning = cloning;
        }

    }

    public class Allocations {

        @SerializedName("view_allo")
        @Expose
        private String viewAllo;
        @SerializedName("creat_alloc")
        @Expose
        private String creatAlloc;

        public String getViewAllo() {
            return viewAllo;
        }

        public void setViewAllo(String viewAllo) {
            this.viewAllo = viewAllo;
        }

        public String getCreatAlloc() {
            return creatAlloc;
        }

        public void setCreatAlloc(String creatAlloc) {
            this.creatAlloc = creatAlloc;
        }

    }

    public class ActivityManager {

        @SerializedName("view_user_activity")
        @Expose
        private String viewUserActivity;
        @SerializedName("view_user_status")
        @Expose
        private String viewUserStatus;

        public String getViewUserActivity() {
            return viewUserActivity;
        }

        public void setViewUserActivity(String viewUserActivity) {
            this.viewUserActivity = viewUserActivity;
        }

        public String getViewUserStatus() {
            return viewUserStatus;
        }

        public void setViewUserStatus(String viewUserStatus) {
            this.viewUserStatus = viewUserStatus;
        }

    }

    public class Holiday {

        @SerializedName("create_req_holiday")
        @Expose
        private String createReqHoliday;
        @SerializedName("view_req_holiday")
        @Expose
        private String viewReqHoliday;
        @SerializedName("manage_general_settings")
        @Expose
        private String manageGeneralSettings;
        @SerializedName("manage_all_request")
        @Expose
        private String manageAllRequest;
        @SerializedName("manage_company_overview")
        @Expose
        private String manageCompanyOverview;
        @SerializedName("manage_user_cal")
        @Expose
        private String manageUserCal;
        @SerializedName("manage_monthly_cal")
        @Expose
        private String manageMonthlyCal;
        @SerializedName("create_req_others")
        @Expose
        private String createReqOthers;
        @SerializedName("create_sickness_report")
        @Expose
        private String createSicknessReport;
        @SerializedName("view_yearly_cal")
        @Expose
        private String viewYearlyCal;

        public String getCreateReqHoliday() {
            return createReqHoliday;
        }

        public void setCreateReqHoliday(String createReqHoliday) {
            this.createReqHoliday = createReqHoliday;
        }

        public String getViewReqHoliday() {
            return viewReqHoliday;
        }

        public void setViewReqHoliday(String viewReqHoliday) {
            this.viewReqHoliday = viewReqHoliday;
        }

        public String getManageGeneralSettings() {
            return manageGeneralSettings;
        }

        public void setManageGeneralSettings(String manageGeneralSettings) {
            this.manageGeneralSettings = manageGeneralSettings;
        }

        public String getManageAllRequest() {
            return manageAllRequest;
        }

        public void setManageAllRequest(String manageAllRequest) {
            this.manageAllRequest = manageAllRequest;
        }

        public String getManageCompanyOverview() {
            return manageCompanyOverview;
        }

        public void setManageCompanyOverview(String manageCompanyOverview) {
            this.manageCompanyOverview = manageCompanyOverview;
        }

        public String getManageUserCal() {
            return manageUserCal;
        }

        public void setManageUserCal(String manageUserCal) {
            this.manageUserCal = manageUserCal;
        }

        public String getManageMonthlyCal() {
            return manageMonthlyCal;
        }

        public void setManageMonthlyCal(String manageMonthlyCal) {
            this.manageMonthlyCal = manageMonthlyCal;
        }

        public String getCreateReqOthers() {
            return createReqOthers;
        }

        public void setCreateReqOthers(String createReqOthers) {
            this.createReqOthers = createReqOthers;
        }

        public String getCreateSicknessReport() {
            return createSicknessReport;
        }

        public void setCreateSicknessReport(String createSicknessReport) {
            this.createSicknessReport = createSicknessReport;
        }

        public String getViewYearlyCal() {
            return viewYearlyCal;
        }

        public void setViewYearlyCal(String viewYearlyCal) {
            this.viewYearlyCal = viewYearlyCal;
        }

    }

    public class Inspection {

        @SerializedName("view_inspection")
        @Expose
        private String viewInspection;
        @SerializedName("create_inspection")
        @Expose
        private String createInspection;
        @SerializedName("quarantine_inspection")
        @Expose
        private String quarantineInspection;
        @SerializedName("delete_inspection")
        @Expose
        private String deleteInspection;

        public String getViewInspection() {
            return viewInspection;
        }

        public void setViewInspection(String viewInspection) {
            this.viewInspection = viewInspection;
        }

        public String getCreateInspection() {
            return createInspection;
        }

        public void setCreateInspection(String createInspection) {
            this.createInspection = createInspection;
        }

        public String getQuarantineInspection() {
            return quarantineInspection;
        }

        public void setQuarantineInspection(String quarantineInspection) {
            this.quarantineInspection = quarantineInspection;
        }

        public String getDeleteInspection() {
            return deleteInspection;
        }

        public void setDeleteInspection(String deleteInspection) {
            this.deleteInspection = deleteInspection;
        }

    }

    public class InteractiveDrawing {

        @SerializedName("view_interactive_drawing")
        @Expose
        private String viewInteractiveDrawing;
        @SerializedName("create_interactive_drawing")
        @Expose
        private String createInteractiveDrawing;
        @SerializedName("sending_a_drawing_via_email")
        @Expose
        private String sendingADrawingViaEmail;
        @SerializedName("quarantine_drawing")
        @Expose
        private String quarantineDrawing;
        @SerializedName("view_history")
        @Expose
        private String viewHistory;

        public String getViewInteractiveDrawing() {
            return viewInteractiveDrawing;
        }

        public void setViewInteractiveDrawing(String viewInteractiveDrawing) {
            this.viewInteractiveDrawing = viewInteractiveDrawing;
        }

        public String getCreateInteractiveDrawing() {
            return createInteractiveDrawing;
        }

        public void setCreateInteractiveDrawing(String createInteractiveDrawing) {
            this.createInteractiveDrawing = createInteractiveDrawing;
        }

        public String getSendingADrawingViaEmail() {
            return sendingADrawingViaEmail;
        }

        public void setSendingADrawingViaEmail(String sendingADrawingViaEmail) {
            this.sendingADrawingViaEmail = sendingADrawingViaEmail;
        }

        public String getQuarantineDrawing() {
            return quarantineDrawing;
        }

        public void setQuarantineDrawing(String quarantineDrawing) {
            this.quarantineDrawing = quarantineDrawing;
        }

        public String getViewHistory() {
            return viewHistory;
        }

        public void setViewHistory(String viewHistory) {
            this.viewHistory = viewHistory;
        }

    }

    public class Invoices {

        @SerializedName("view_quote_in")
        @Expose
        private String viewQuoteIn;
        @SerializedName("view_quote_out")
        @Expose
        private String viewQuoteOut;
        @SerializedName("create_quotes_out")
        @Expose
        private String createQuotesOut;
        @SerializedName("create_quotes_in")
        @Expose
        private String createQuotesIn;
        @SerializedName("invite_users_in")
        @Expose
        private String inviteUsersIn;
        @SerializedName("edit_delete_item")
        @Expose
        private String editDeleteItem;
        @SerializedName("delete")
        @Expose
        private String delete;

        public String getViewQuoteIn() {
            return viewQuoteIn;
        }

        public void setViewQuoteIn(String viewQuoteIn) {
            this.viewQuoteIn = viewQuoteIn;
        }

        public String getViewQuoteOut() {
            return viewQuoteOut;
        }

        public void setViewQuoteOut(String viewQuoteOut) {
            this.viewQuoteOut = viewQuoteOut;
        }

        public String getCreateQuotesOut() {
            return createQuotesOut;
        }

        public void setCreateQuotesOut(String createQuotesOut) {
            this.createQuotesOut = createQuotesOut;
        }

        public String getCreateQuotesIn() {
            return createQuotesIn;
        }

        public void setCreateQuotesIn(String createQuotesIn) {
            this.createQuotesIn = createQuotesIn;
        }

        public String getInviteUsersIn() {
            return inviteUsersIn;
        }

        public void setInviteUsersIn(String inviteUsersIn) {
            this.inviteUsersIn = inviteUsersIn;
        }

        public String getEditDeleteItem() {
            return editDeleteItem;
        }

        public void setEditDeleteItem(String editDeleteItem) {
            this.editDeleteItem = editDeleteItem;
        }

        public String getDelete() {
            return delete;
        }

        public void setDelete(String delete) {
            this.delete = delete;
        }

    }

    public class Mails {

        @SerializedName("send_messages")
        @Expose
        private String sendMessages;
        @SerializedName("view_emails")
        @Expose
        private String viewEmails;
        @SerializedName("view_p_msgs")
        @Expose
        private String viewPMsgs;
        @SerializedName("view_email")
        @Expose
        private String viewEmail;

        public String getSendMessages() {
            return sendMessages;
        }

        public void setSendMessages(String sendMessages) {
            this.sendMessages = sendMessages;
        }

        public String getViewEmails() {
            return viewEmails;
        }

        public void setViewEmails(String viewEmails) {
            this.viewEmails = viewEmails;
        }

        public String getViewPMsgs() {
            return viewPMsgs;
        }

        public void setViewPMsgs(String viewPMsgs) {
            this.viewPMsgs = viewPMsgs;
        }

        public String getViewEmail() {
            return viewEmail;
        }

        public void setViewEmail(String viewEmail) {
            this.viewEmail = viewEmail;
        }

    }

    public class ManageLocalisation {

        @SerializedName("view_localisation")
        @Expose
        private String viewLocalisation;
        @SerializedName("edit_localisation")
        @Expose
        private String editLocalisation;
        @SerializedName("manage_languages")
        @Expose
        private String manageLanguages;

        public String getViewLocalisation() {
            return viewLocalisation;
        }

        public void setViewLocalisation(String viewLocalisation) {
            this.viewLocalisation = viewLocalisation;
        }

        public String getEditLocalisation() {
            return editLocalisation;
        }

        public void setEditLocalisation(String editLocalisation) {
            this.editLocalisation = editLocalisation;
        }

        public String getManageLanguages() {
            return manageLanguages;
        }

        public void setManageLanguages(String manageLanguages) {
            this.manageLanguages = manageLanguages;
        }

    }

    public class NotificationManager {

        @SerializedName("view_noti")
        @Expose
        private String viewNoti;
        @SerializedName("request_notifi")
        @Expose
        private String requestNotifi;
        @SerializedName("view_text_noti")
        @Expose
        private String viewTextNoti;
        @SerializedName("view_log")
        @Expose
        private String viewLog;
        @SerializedName("view_cron")
        @Expose
        private String viewCron;
        @SerializedName("assign_users")
        @Expose
        private String assignUsers;
        @SerializedName("manage_requests")
        @Expose
        private String manageRequests;
        @SerializedName("edit_noti_label")
        @Expose
        private String editNotiLabel;
        @SerializedName("delete_noti")
        @Expose
        private String deleteNoti;

        public String getViewNoti() {
            return viewNoti;
        }

        public void setViewNoti(String viewNoti) {
            this.viewNoti = viewNoti;
        }

        public String getRequestNotifi() {
            return requestNotifi;
        }

        public void setRequestNotifi(String requestNotifi) {
            this.requestNotifi = requestNotifi;
        }

        public String getViewTextNoti() {
            return viewTextNoti;
        }

        public void setViewTextNoti(String viewTextNoti) {
            this.viewTextNoti = viewTextNoti;
        }

        public String getViewLog() {
            return viewLog;
        }

        public void setViewLog(String viewLog) {
            this.viewLog = viewLog;
        }

        public String getViewCron() {
            return viewCron;
        }

        public void setViewCron(String viewCron) {
            this.viewCron = viewCron;
        }

        public String getAssignUsers() {
            return assignUsers;
        }

        public void setAssignUsers(String assignUsers) {
            this.assignUsers = assignUsers;
        }

        public String getManageRequests() {
            return manageRequests;
        }

        public void setManageRequests(String manageRequests) {
            this.manageRequests = manageRequests;
        }

        public String getEditNotiLabel() {
            return editNotiLabel;
        }

        public void setEditNotiLabel(String editNotiLabel) {
            this.editNotiLabel = editNotiLabel;
        }

        public String getDeleteNoti() {
            return deleteNoti;
        }

        public void setDeleteNoti(String deleteNoti) {
            this.deleteNoti = deleteNoti;
        }

    }

    public class Overview {

        @SerializedName("view_gen_set")
        @Expose
        private String viewGenSet;
        @SerializedName("view_report")
        @Expose
        private String viewReport;
        @SerializedName("new_user_from_login")
        @Expose
        private String newUserFromLogin;

        public String getViewGenSet() {
            return viewGenSet;
        }

        public void setViewGenSet(String viewGenSet) {
            this.viewGenSet = viewGenSet;
        }

        public String getViewReport() {
            return viewReport;
        }

        public void setViewReport(String viewReport) {
            this.viewReport = viewReport;
        }

        public String getNewUserFromLogin() {
            return newUserFromLogin;
        }

        public void setNewUserFromLogin(String newUserFromLogin) {
            this.newUserFromLogin = newUserFromLogin;
        }

    }

    public class PhotoGallery {

        @SerializedName("view_img_gallery")
        @Expose
        private String viewImgGallery;
        @SerializedName("add_comments")
        @Expose
        private String addComments;
        @SerializedName("create_gallery")
        @Expose
        private String createGallery;
        @SerializedName("upload_photo")
        @Expose
        private String uploadPhoto;
        @SerializedName("share_gallery")
        @Expose
        private String shareGallery;
        @SerializedName("update_gallery")
        @Expose
        private String updateGallery;
        @SerializedName("delete_gallery")
        @Expose
        private String deleteGallery;
        @SerializedName("manage_custom_field")
        @Expose
        private String manageCustomField;

        public String getViewImgGallery() {
            return viewImgGallery;
        }

        public void setViewImgGallery(String viewImgGallery) {
            this.viewImgGallery = viewImgGallery;
        }

        public String getAddComments() {
            return addComments;
        }

        public void setAddComments(String addComments) {
            this.addComments = addComments;
        }

        public String getCreateGallery() {
            return createGallery;
        }

        public void setCreateGallery(String createGallery) {
            this.createGallery = createGallery;
        }

        public String getUploadPhoto() {
            return uploadPhoto;
        }

        public void setUploadPhoto(String uploadPhoto) {
            this.uploadPhoto = uploadPhoto;
        }

        public String getShareGallery() {
            return shareGallery;
        }

        public void setShareGallery(String shareGallery) {
            this.shareGallery = shareGallery;
        }

        public String getUpdateGallery() {
            return updateGallery;
        }

        public void setUpdateGallery(String updateGallery) {
            this.updateGallery = updateGallery;
        }

        public String getDeleteGallery() {
            return deleteGallery;
        }

        public void setDeleteGallery(String deleteGallery) {
            this.deleteGallery = deleteGallery;
        }

        public String getManageCustomField() {
            return manageCustomField;
        }

        public void setManageCustomField(String manageCustomField) {
            this.manageCustomField = manageCustomField;
        }

    }

    public class Procurements {

        @SerializedName("requisition_deliveries_reconcilation_payment_advance")
        @Expose
        private String requisitionDeliveriesReconcilationPaymentAdvance;

        public String getRequisitionDeliveriesReconcilationPaymentAdvance() {
            return requisitionDeliveriesReconcilationPaymentAdvance;
        }

        public void setRequisitionDeliveriesReconcilationPaymentAdvance(String requisitionDeliveriesReconcilationPaymentAdvance) {
            this.requisitionDeliveriesReconcilationPaymentAdvance = requisitionDeliveriesReconcilationPaymentAdvance;
        }

    }

    public class ProjectScheduling {

        @SerializedName("view")
        @Expose
        private String view;
        @SerializedName("create_task")
        @Expose
        private String createTask;
        @SerializedName("edit_task")
        @Expose
        private String editTask;
        @SerializedName("delete_task")
        @Expose
        private String deleteTask;

        public String getView() {
            return view;
        }

        public void setView(String view) {
            this.view = view;
        }

        public String getCreateTask() {
            return createTask;
        }

        public void setCreateTask(String createTask) {
            this.createTask = createTask;
        }

        public String getEditTask() {
            return editTask;
        }

        public void setEditTask(String editTask) {
            this.editTask = editTask;
        }

        public String getDeleteTask() {
            return deleteTask;
        }

        public void setDeleteTask(String deleteTask) {
            this.deleteTask = deleteTask;
        }

    }

    public class Projects {

        @SerializedName("view_project")
        @Expose
        private String viewProject;
        @SerializedName("see_project_address")
        @Expose
        private String seeProjectAddress;
        @SerializedName("see_project_status")
        @Expose
        private String seeProjectStatus;
        @SerializedName("see_project_stage")
        @Expose
        private String seeProjectStage;
        @SerializedName("see_project_locations")
        @Expose
        private String seeProjectLocations;
        @SerializedName("view_add_notes")
        @Expose
        private String viewAddNotes;
        @SerializedName("see_project_manager")
        @Expose
        private String seeProjectManager;
        @SerializedName("assign_users")
        @Expose
        private String assignUsers;
        @SerializedName("create_project")
        @Expose
        private String createProject;
        @SerializedName("delete_project")
        @Expose
        private String deleteProject;
        @SerializedName("update_prject")
        @Expose
        private String updatePrject;
        @SerializedName("archive_project")
        @Expose
        private String archiveProject;
        @SerializedName("unarchive_project")
        @Expose
        private String unarchiveProject;
        @SerializedName("create_custom_fields")
        @Expose
        private String createCustomFields;
        @SerializedName("generate_order_number")
        @Expose
        private String generateOrderNumber;
        @SerializedName("add_client")
        @Expose
        private String addClient;
        @SerializedName("standard_working_hours")
        @Expose
        private String standardWorkingHours;

        public String getViewProject() {
            return viewProject;
        }

        public void setViewProject(String viewProject) {
            this.viewProject = viewProject;
        }

        public String getSeeProjectAddress() {
            return seeProjectAddress;
        }

        public void setSeeProjectAddress(String seeProjectAddress) {
            this.seeProjectAddress = seeProjectAddress;
        }

        public String getSeeProjectStatus() {
            return seeProjectStatus;
        }

        public void setSeeProjectStatus(String seeProjectStatus) {
            this.seeProjectStatus = seeProjectStatus;
        }

        public String getSeeProjectStage() {
            return seeProjectStage;
        }

        public void setSeeProjectStage(String seeProjectStage) {
            this.seeProjectStage = seeProjectStage;
        }

        public String getSeeProjectLocations() {
            return seeProjectLocations;
        }

        public void setSeeProjectLocations(String seeProjectLocations) {
            this.seeProjectLocations = seeProjectLocations;
        }

        public String getViewAddNotes() {
            return viewAddNotes;
        }

        public void setViewAddNotes(String viewAddNotes) {
            this.viewAddNotes = viewAddNotes;
        }

        public String getSeeProjectManager() {
            return seeProjectManager;
        }

        public void setSeeProjectManager(String seeProjectManager) {
            this.seeProjectManager = seeProjectManager;
        }

        public String getAssignUsers() {
            return assignUsers;
        }

        public void setAssignUsers(String assignUsers) {
            this.assignUsers = assignUsers;
        }

        public String getCreateProject() {
            return createProject;
        }

        public void setCreateProject(String createProject) {
            this.createProject = createProject;
        }

        public String getDeleteProject() {
            return deleteProject;
        }

        public void setDeleteProject(String deleteProject) {
            this.deleteProject = deleteProject;
        }

        public String getUpdatePrject() {
            return updatePrject;
        }

        public void setUpdatePrject(String updatePrject) {
            this.updatePrject = updatePrject;
        }

        public String getArchiveProject() {
            return archiveProject;
        }

        public void setArchiveProject(String archiveProject) {
            this.archiveProject = archiveProject;
        }

        public String getUnarchiveProject() {
            return unarchiveProject;
        }

        public void setUnarchiveProject(String unarchiveProject) {
            this.unarchiveProject = unarchiveProject;
        }

        public String getCreateCustomFields() {
            return createCustomFields;
        }

        public void setCreateCustomFields(String createCustomFields) {
            this.createCustomFields = createCustomFields;
        }

        public String getGenerateOrderNumber() {
            return generateOrderNumber;
        }

        public void setGenerateOrderNumber(String generateOrderNumber) {
            this.generateOrderNumber = generateOrderNumber;
        }

        public String getAddClient() {
            return addClient;
        }

        public void setAddClient(String addClient) {
            this.addClient = addClient;
        }

        public String getStandardWorkingHours() {
            return standardWorkingHours;
        }

        public void setStandardWorkingHours(String standardWorkingHours) {
            this.standardWorkingHours = standardWorkingHours;
        }

    }

    public class Quotations {

        @SerializedName("view_quote_in")
        @Expose
        private String viewQuoteIn;
        @SerializedName("create_quotes_in")
        @Expose
        private String createQuotesIn;

        public String getViewQuoteIn() {
            return viewQuoteIn;
        }

        public void setViewQuoteIn(String viewQuoteIn) {
            this.viewQuoteIn = viewQuoteIn;
        }

        public String getCreateQuotesIn() {
            return createQuotesIn;
        }

        public void setCreateQuotesIn(String createQuotesIn) {
            this.createQuotesIn = createQuotesIn;
        }

    }

    public class Reports {

        @SerializedName("view_report")
        @Expose
        private String viewReport;
        @SerializedName("view_report_section")
        @Expose
        private String viewReportSection;
        @SerializedName("view_rfi_overall")
        @Expose
        private String viewRfiOverall;
        @SerializedName("view_rfi_reports")
        @Expose
        private String viewRfiReports;
        @SerializedName("view_rfi_userperreports")
        @Expose
        private String viewRfiUserperreports;
        @SerializedName("view_defect_reports")
        @Expose
        private String viewDefectReports;
        @SerializedName("view_defect_userperreports")
        @Expose
        private String viewDefectUserperreports;
        @SerializedName("view_document_userperreports")
        @Expose
        private String viewDocumentUserperreports;
        @SerializedName("view_drawing_userperreports")
        @Expose
        private String viewDrawingUserperreports;
        @SerializedName("view_cloud_userperreports")
        @Expose
        private String viewCloudUserperreports;
        @SerializedName("view_photos_userperreports")
        @Expose
        private String viewPhotosUserperreports;
        @SerializedName("view_quotations_userperreports")
        @Expose
        private String viewQuotationsUserperreports;
        @SerializedName("view_valuations_userperreports")
        @Expose
        private String viewValuationsUserperreports;
        @SerializedName("view_dairy_userperreports")
        @Expose
        private String viewDairyUserperreports;
        @SerializedName("view_training_userperreports")
        @Expose
        private String viewTrainingUserperreports;

        public String getViewReport() {
            return viewReport;
        }

        public void setViewReport(String viewReport) {
            this.viewReport = viewReport;
        }

        public String getViewReportSection() {
            return viewReportSection;
        }

        public void setViewReportSection(String viewReportSection) {
            this.viewReportSection = viewReportSection;
        }

        public String getViewRfiOverall() {
            return viewRfiOverall;
        }

        public void setViewRfiOverall(String viewRfiOverall) {
            this.viewRfiOverall = viewRfiOverall;
        }

        public String getViewRfiReports() {
            return viewRfiReports;
        }

        public void setViewRfiReports(String viewRfiReports) {
            this.viewRfiReports = viewRfiReports;
        }

        public String getViewRfiUserperreports() {
            return viewRfiUserperreports;
        }

        public void setViewRfiUserperreports(String viewRfiUserperreports) {
            this.viewRfiUserperreports = viewRfiUserperreports;
        }

        public String getViewDefectReports() {
            return viewDefectReports;
        }

        public void setViewDefectReports(String viewDefectReports) {
            this.viewDefectReports = viewDefectReports;
        }

        public String getViewDefectUserperreports() {
            return viewDefectUserperreports;
        }

        public void setViewDefectUserperreports(String viewDefectUserperreports) {
            this.viewDefectUserperreports = viewDefectUserperreports;
        }

        public String getViewDocumentUserperreports() {
            return viewDocumentUserperreports;
        }

        public void setViewDocumentUserperreports(String viewDocumentUserperreports) {
            this.viewDocumentUserperreports = viewDocumentUserperreports;
        }

        public String getViewDrawingUserperreports() {
            return viewDrawingUserperreports;
        }

        public void setViewDrawingUserperreports(String viewDrawingUserperreports) {
            this.viewDrawingUserperreports = viewDrawingUserperreports;
        }

        public String getViewCloudUserperreports() {
            return viewCloudUserperreports;
        }

        public void setViewCloudUserperreports(String viewCloudUserperreports) {
            this.viewCloudUserperreports = viewCloudUserperreports;
        }

        public String getViewPhotosUserperreports() {
            return viewPhotosUserperreports;
        }

        public void setViewPhotosUserperreports(String viewPhotosUserperreports) {
            this.viewPhotosUserperreports = viewPhotosUserperreports;
        }

        public String getViewQuotationsUserperreports() {
            return viewQuotationsUserperreports;
        }

        public void setViewQuotationsUserperreports(String viewQuotationsUserperreports) {
            this.viewQuotationsUserperreports = viewQuotationsUserperreports;
        }

        public String getViewValuationsUserperreports() {
            return viewValuationsUserperreports;
        }

        public void setViewValuationsUserperreports(String viewValuationsUserperreports) {
            this.viewValuationsUserperreports = viewValuationsUserperreports;
        }

        public String getViewDairyUserperreports() {
            return viewDairyUserperreports;
        }

        public void setViewDairyUserperreports(String viewDairyUserperreports) {
            this.viewDairyUserperreports = viewDairyUserperreports;
        }

        public String getViewTrainingUserperreports() {
            return viewTrainingUserperreports;
        }

        public void setViewTrainingUserperreports(String viewTrainingUserperreports) {
            this.viewTrainingUserperreports = viewTrainingUserperreports;
        }

    }

    public class RequestForApproval {

        @SerializedName("view_approval")
        @Expose
        private String viewApproval;
        @SerializedName("create_approval")
        @Expose
        private String createApproval;
        @SerializedName("delete_approval")
        @Expose
        private String deleteApproval;
        @SerializedName("quarantine_approval")
        @Expose
        private String quarantineApproval;

        public String getViewApproval() {
            return viewApproval;
        }

        public void setViewApproval(String viewApproval) {
            this.viewApproval = viewApproval;
        }

        public String getCreateApproval() {
            return createApproval;
        }

        public void setCreateApproval(String createApproval) {
            this.createApproval = createApproval;
        }

        public String getDeleteApproval() {
            return deleteApproval;
        }

        public void setDeleteApproval(String deleteApproval) {
            this.deleteApproval = deleteApproval;
        }

        public String getQuarantineApproval() {
            return quarantineApproval;
        }

        public void setQuarantineApproval(String quarantineApproval) {
            this.quarantineApproval = quarantineApproval;
        }

    }

    public class Rfi {

        @SerializedName("view_rifs")
        @Expose
        private String viewRifs;
        @SerializedName("rfi_create")
        @Expose
        private String rfiCreate;
        @SerializedName("mark_quarantine")
        @Expose
        private String markQuarantine;
        @SerializedName("view_all_rfis")
        @Expose
        private String viewAllRfis;
        @SerializedName("issue_to_group")
        @Expose
        private String issueToGroup;
        @SerializedName("manage_custom_field")
        @Expose
        private String manageCustomField;
        @SerializedName("sending_rfi_via_email")
        @Expose
        private String sendingRfiViaEmail;

        public String getViewRifs() {
            return viewRifs;
        }

        public void setViewRifs(String viewRifs) {
            this.viewRifs = viewRifs;
        }

        public String getRfiCreate() {
            return rfiCreate;
        }

        public void setRfiCreate(String rfiCreate) {
            this.rfiCreate = rfiCreate;
        }

        public String getMarkQuarantine() {
            return markQuarantine;
        }

        public void setMarkQuarantine(String markQuarantine) {
            this.markQuarantine = markQuarantine;
        }

        public String getViewAllRfis() {
            return viewAllRfis;
        }

        public void setViewAllRfis(String viewAllRfis) {
            this.viewAllRfis = viewAllRfis;
        }

        public String getIssueToGroup() {
            return issueToGroup;
        }

        public void setIssueToGroup(String issueToGroup) {
            this.issueToGroup = issueToGroup;
        }

        public String getManageCustomField() {
            return manageCustomField;
        }

        public void setManageCustomField(String manageCustomField) {
            this.manageCustomField = manageCustomField;
        }

        public String getSendingRfiViaEmail() {
            return sendingRfiViaEmail;
        }

        public void setSendingRfiViaEmail(String sendingRfiViaEmail) {
            this.sendingRfiViaEmail = sendingRfiViaEmail;
        }

    }

    public class Signinginout {

        @SerializedName("view_signinginout")
        @Expose
        private String viewSigninginout;
        @SerializedName("signout_signinginout")
        @Expose
        private String signoutSigninginout;

        public String getViewSigninginout() {
            return viewSigninginout;
        }

        public void setViewSigninginout(String viewSigninginout) {
            this.viewSigninginout = viewSigninginout;
        }

        public String getSignoutSigninginout() {
            return signoutSigninginout;
        }

        public void setSignoutSigninginout(String signoutSigninginout) {
            this.signoutSigninginout = signoutSigninginout;
        }

    }

    public class SiteInduction {

        @SerializedName("view_site_induction")
        @Expose
        private String viewSiteInduction;
        @SerializedName("add_induction_user")
        @Expose
        private String addInductionUser;
        @SerializedName("manage_inductees")
        @Expose
        private String manageInductees;
        @SerializedName("sign_off_user_induction")
        @Expose
        private String signOffUserInduction;
        @SerializedName("create_questionnaire_groups")
        @Expose
        private String createQuestionnaireGroups;
        @SerializedName("create_answers")
        @Expose
        private String createAnswers;
        @SerializedName("create_questions")
        @Expose
        private String createQuestions;
        @SerializedName("create_questionnaire")
        @Expose
        private String createQuestionnaire;
        @SerializedName("view_questionnaire_settings")
        @Expose
        private String viewQuestionnaireSettings;
        @SerializedName("view_site_questionnaires")
        @Expose
        private String viewSiteQuestionnaires;
        @SerializedName("manage_induction")
        @Expose
        private String manageInduction;
        @SerializedName("view_signed_off_user")
        @Expose
        private String viewSignedOffUser;
        @SerializedName("create_custom_fields")
        @Expose
        private String createCustomFields;

        public String getViewSiteInduction() {
            return viewSiteInduction;
        }

        public void setViewSiteInduction(String viewSiteInduction) {
            this.viewSiteInduction = viewSiteInduction;
        }

        public String getAddInductionUser() {
            return addInductionUser;
        }

        public void setAddInductionUser(String addInductionUser) {
            this.addInductionUser = addInductionUser;
        }

        public String getManageInductees() {
            return manageInductees;
        }

        public void setManageInductees(String manageInductees) {
            this.manageInductees = manageInductees;
        }

        public String getSignOffUserInduction() {
            return signOffUserInduction;
        }

        public void setSignOffUserInduction(String signOffUserInduction) {
            this.signOffUserInduction = signOffUserInduction;
        }

        public String getCreateQuestionnaireGroups() {
            return createQuestionnaireGroups;
        }

        public void setCreateQuestionnaireGroups(String createQuestionnaireGroups) {
            this.createQuestionnaireGroups = createQuestionnaireGroups;
        }

        public String getCreateAnswers() {
            return createAnswers;
        }

        public void setCreateAnswers(String createAnswers) {
            this.createAnswers = createAnswers;
        }

        public String getCreateQuestions() {
            return createQuestions;
        }

        public void setCreateQuestions(String createQuestions) {
            this.createQuestions = createQuestions;
        }

        public String getCreateQuestionnaire() {
            return createQuestionnaire;
        }

        public void setCreateQuestionnaire(String createQuestionnaire) {
            this.createQuestionnaire = createQuestionnaire;
        }

        public String getViewQuestionnaireSettings() {
            return viewQuestionnaireSettings;
        }

        public void setViewQuestionnaireSettings(String viewQuestionnaireSettings) {
            this.viewQuestionnaireSettings = viewQuestionnaireSettings;
        }

        public String getViewSiteQuestionnaires() {
            return viewSiteQuestionnaires;
        }

        public void setViewSiteQuestionnaires(String viewSiteQuestionnaires) {
            this.viewSiteQuestionnaires = viewSiteQuestionnaires;
        }

        public String getManageInduction() {
            return manageInduction;
        }

        public void setManageInduction(String manageInduction) {
            this.manageInduction = manageInduction;
        }

        public String getViewSignedOffUser() {
            return viewSignedOffUser;
        }

        public void setViewSignedOffUser(String viewSignedOffUser) {
            this.viewSignedOffUser = viewSignedOffUser;
        }

        public String getCreateCustomFields() {
            return createCustomFields;
        }

        public void setCreateCustomFields(String createCustomFields) {
            this.createCustomFields = createCustomFields;
        }

    }

    public class SmsNotificationManager {

        @SerializedName("text_sms_service")
        @Expose
        private String textSmsService;

        public String getTextSmsService() {
            return textSmsService;
        }

        public void setTextSmsService(String textSmsService) {
            this.textSmsService = textSmsService;
        }

    }

    public class SubContractPackage {

        @SerializedName("view_package")
        @Expose
        private String viewPackage;
        @SerializedName("create_package")
        @Expose
        private String createPackage;
        @SerializedName("delete_package")
        @Expose
        private String deletePackage;
        @SerializedName("award_package")
        @Expose
        private String awardPackage;
        @SerializedName("quarantine_package")
        @Expose
        private String quarantinePackage;

        public String getViewPackage() {
            return viewPackage;
        }

        public void setViewPackage(String viewPackage) {
            this.viewPackage = viewPackage;
        }

        public String getCreatePackage() {
            return createPackage;
        }

        public void setCreatePackage(String createPackage) {
            this.createPackage = createPackage;
        }

        public String getDeletePackage() {
            return deletePackage;
        }

        public void setDeletePackage(String deletePackage) {
            this.deletePackage = deletePackage;
        }

        public String getAwardPackage() {
            return awardPackage;
        }

        public void setAwardPackage(String awardPackage) {
            this.awardPackage = awardPackage;
        }

        public String getQuarantinePackage() {
            return quarantinePackage;
        }

        public void setQuarantinePackage(String quarantinePackage) {
            this.quarantinePackage = quarantinePackage;
        }

    }

    public class Submittals {

        @SerializedName("view_submittals")
        @Expose
        private String viewSubmittals;
        @SerializedName("view_submittals_flow")
        @Expose
        private String viewSubmittalsFlow;
        @SerializedName("view_quarantine_submittals")
        @Expose
        private String viewQuarantineSubmittals;
        @SerializedName("view_back_to_originator_submittals")
        @Expose
        private String viewBackToOriginatorSubmittals;
        @SerializedName("view_archived_submittals")
        @Expose
        private String viewArchivedSubmittals;
        @SerializedName("add_edit_submittals")
        @Expose
        private String addEditSubmittals;
        @SerializedName("add_edit_submittals_flow")
        @Expose
        private String addEditSubmittalsFlow;
        @SerializedName("delete_submittals_flow")
        @Expose
        private String deleteSubmittalsFlow;
        @SerializedName("delete_submittals")
        @Expose
        private String deleteSubmittals;
        @SerializedName("restore_quarantine_submittals")
        @Expose
        private String restoreQuarantineSubmittals;
        @SerializedName("restore_archived_submittals")
        @Expose
        private String restoreArchivedSubmittals;
        @SerializedName("sending_a_document_via_email")
        @Expose
        private String sendingADocumentViaEmail;

        public String getViewSubmittals() {
            return viewSubmittals;
        }

        public void setViewSubmittals(String viewSubmittals) {
            this.viewSubmittals = viewSubmittals;
        }

        public String getViewSubmittalsFlow() {
            return viewSubmittalsFlow;
        }

        public void setViewSubmittalsFlow(String viewSubmittalsFlow) {
            this.viewSubmittalsFlow = viewSubmittalsFlow;
        }

        public String getViewQuarantineSubmittals() {
            return viewQuarantineSubmittals;
        }

        public void setViewQuarantineSubmittals(String viewQuarantineSubmittals) {
            this.viewQuarantineSubmittals = viewQuarantineSubmittals;
        }

        public String getViewBackToOriginatorSubmittals() {
            return viewBackToOriginatorSubmittals;
        }

        public void setViewBackToOriginatorSubmittals(String viewBackToOriginatorSubmittals) {
            this.viewBackToOriginatorSubmittals = viewBackToOriginatorSubmittals;
        }

        public String getViewArchivedSubmittals() {
            return viewArchivedSubmittals;
        }

        public void setViewArchivedSubmittals(String viewArchivedSubmittals) {
            this.viewArchivedSubmittals = viewArchivedSubmittals;
        }

        public String getAddEditSubmittals() {
            return addEditSubmittals;
        }

        public void setAddEditSubmittals(String addEditSubmittals) {
            this.addEditSubmittals = addEditSubmittals;
        }

        public String getAddEditSubmittalsFlow() {
            return addEditSubmittalsFlow;
        }

        public void setAddEditSubmittalsFlow(String addEditSubmittalsFlow) {
            this.addEditSubmittalsFlow = addEditSubmittalsFlow;
        }

        public String getDeleteSubmittalsFlow() {
            return deleteSubmittalsFlow;
        }

        public void setDeleteSubmittalsFlow(String deleteSubmittalsFlow) {
            this.deleteSubmittalsFlow = deleteSubmittalsFlow;
        }

        public String getDeleteSubmittals() {
            return deleteSubmittals;
        }

        public void setDeleteSubmittals(String deleteSubmittals) {
            this.deleteSubmittals = deleteSubmittals;
        }

        public String getRestoreQuarantineSubmittals() {
            return restoreQuarantineSubmittals;
        }

        public void setRestoreQuarantineSubmittals(String restoreQuarantineSubmittals) {
            this.restoreQuarantineSubmittals = restoreQuarantineSubmittals;
        }

        public String getRestoreArchivedSubmittals() {
            return restoreArchivedSubmittals;
        }

        public void setRestoreArchivedSubmittals(String restoreArchivedSubmittals) {
            this.restoreArchivedSubmittals = restoreArchivedSubmittals;
        }

        public String getSendingADocumentViaEmail() {
            return sendingADocumentViaEmail;
        }

        public void setSendingADocumentViaEmail(String sendingADocumentViaEmail) {
            this.sendingADocumentViaEmail = sendingADocumentViaEmail;
        }

    }

    public class Tags {

        @SerializedName("view_tags")
        @Expose
        private String viewTags;

        public String getViewTags() {
            return viewTags;
        }

        public void setViewTags(String viewTags) {
            this.viewTags = viewTags;
        }

    }

    public class TimesheetsNew {

        @SerializedName("view_timesheet")
        @Expose
        private String viewTimesheet;
        @SerializedName("view_project_timesheet")
        @Expose
        private String viewProjectTimesheet;
        @SerializedName("view_company_overview")
        @Expose
        private String viewCompanyOverview;
        @SerializedName("view_approved_timesheet")
        @Expose
        private String viewApprovedTimesheet;
        @SerializedName("view_task_tracking")
        @Expose
        private String viewTaskTracking;
        @SerializedName("manage_project_distribution")
        @Expose
        private String manageProjectDistribution;
        @SerializedName("manage_pricework")
        @Expose
        private String managePricework;
        @SerializedName("manage_metadata")
        @Expose
        private String manageMetadata;
        @SerializedName("set_applicable_users")
        @Expose
        private String setApplicableUsers;
        @SerializedName("setup_project_timesheet")
        @Expose
        private String setupProjectTimesheet;
        @SerializedName("timesheet_view_rates_prices")
        @Expose
        private String timesheetViewRatesPrices;

        public String getViewTimesheet() {
            return viewTimesheet;
        }

        public void setViewTimesheet(String viewTimesheet) {
            this.viewTimesheet = viewTimesheet;
        }

        public String getViewProjectTimesheet() {
            return viewProjectTimesheet;
        }

        public void setViewProjectTimesheet(String viewProjectTimesheet) {
            this.viewProjectTimesheet = viewProjectTimesheet;
        }

        public String getViewCompanyOverview() {
            return viewCompanyOverview;
        }

        public void setViewCompanyOverview(String viewCompanyOverview) {
            this.viewCompanyOverview = viewCompanyOverview;
        }

        public String getViewApprovedTimesheet() {
            return viewApprovedTimesheet;
        }

        public void setViewApprovedTimesheet(String viewApprovedTimesheet) {
            this.viewApprovedTimesheet = viewApprovedTimesheet;
        }

        public String getViewTaskTracking() {
            return viewTaskTracking;
        }

        public void setViewTaskTracking(String viewTaskTracking) {
            this.viewTaskTracking = viewTaskTracking;
        }

        public String getManageProjectDistribution() {
            return manageProjectDistribution;
        }

        public void setManageProjectDistribution(String manageProjectDistribution) {
            this.manageProjectDistribution = manageProjectDistribution;
        }

        public String getManagePricework() {
            return managePricework;
        }

        public void setManagePricework(String managePricework) {
            this.managePricework = managePricework;
        }

        public String getManageMetadata() {
            return manageMetadata;
        }

        public void setManageMetadata(String manageMetadata) {
            this.manageMetadata = manageMetadata;
        }

        public String getSetApplicableUsers() {
            return setApplicableUsers;
        }

        public void setSetApplicableUsers(String setApplicableUsers) {
            this.setApplicableUsers = setApplicableUsers;
        }

        public String getSetupProjectTimesheet() {
            return setupProjectTimesheet;
        }

        public void setSetupProjectTimesheet(String setupProjectTimesheet) {
            this.setupProjectTimesheet = setupProjectTimesheet;
        }

        public String getTimesheetViewRatesPrices() {
            return timesheetViewRatesPrices;
        }

        public void setTimesheetViewRatesPrices(String timesheetViewRatesPrices) {
            this.timesheetViewRatesPrices = timesheetViewRatesPrices;
        }

    }

    public class Todos {

        @SerializedName("view_todos")
        @Expose
        private String viewTodos;
        @SerializedName("add_edit_todos")
        @Expose
        private String addEditTodos;
        @SerializedName("due_date_completed")
        @Expose
        private String dueDateCompleted;
        @SerializedName("manage_todo_categories")
        @Expose
        private String manageTodoCategories;
        @SerializedName("delete_todos")
        @Expose
        private String deleteTodos;

        public String getViewTodos() {
            return viewTodos;
        }

        public void setViewTodos(String viewTodos) {
            this.viewTodos = viewTodos;
        }

        public String getAddEditTodos() {
            return addEditTodos;
        }

        public void setAddEditTodos(String addEditTodos) {
            this.addEditTodos = addEditTodos;
        }

        public String getDueDateCompleted() {
            return dueDateCompleted;
        }

        public void setDueDateCompleted(String dueDateCompleted) {
            this.dueDateCompleted = dueDateCompleted;
        }

        public String getManageTodoCategories() {
            return manageTodoCategories;
        }

        public void setManageTodoCategories(String manageTodoCategories) {
            this.manageTodoCategories = manageTodoCategories;
        }

        public String getDeleteTodos() {
            return deleteTodos;
        }

        public void setDeleteTodos(String deleteTodos) {
            this.deleteTodos = deleteTodos;
        }

    }

    public class TrainingCompliance {

        @SerializedName("view_training_compl")
        @Expose
        private String viewTrainingCompl;
        @SerializedName("view_company_compl")
        @Expose
        private String viewCompanyCompl;
        @SerializedName("training_course")
        @Expose
        private String trainingCourse;
        @SerializedName("training_category")
        @Expose
        private String trainingCategory;
        @SerializedName("training_edit_user_course")
        @Expose
        private String trainingEditUserCourse;
        @SerializedName("set_applicable_user")
        @Expose
        private String setApplicableUser;
        @SerializedName("manage_training_centres")
        @Expose
        private String manageTrainingCentres;
        @SerializedName("process_a_users_course")
        @Expose
        private String processAUsersCourse;
        @SerializedName("book_a_users_course")
        @Expose
        private String bookAUsersCourse;

        public String getViewTrainingCompl() {
            return viewTrainingCompl;
        }

        public void setViewTrainingCompl(String viewTrainingCompl) {
            this.viewTrainingCompl = viewTrainingCompl;
        }

        public String getViewCompanyCompl() {
            return viewCompanyCompl;
        }

        public void setViewCompanyCompl(String viewCompanyCompl) {
            this.viewCompanyCompl = viewCompanyCompl;
        }

        public String getTrainingCourse() {
            return trainingCourse;
        }

        public void setTrainingCourse(String trainingCourse) {
            this.trainingCourse = trainingCourse;
        }

        public String getTrainingCategory() {
            return trainingCategory;
        }

        public void setTrainingCategory(String trainingCategory) {
            this.trainingCategory = trainingCategory;
        }

        public String getTrainingEditUserCourse() {
            return trainingEditUserCourse;
        }

        public void setTrainingEditUserCourse(String trainingEditUserCourse) {
            this.trainingEditUserCourse = trainingEditUserCourse;
        }

        public String getSetApplicableUser() {
            return setApplicableUser;
        }

        public void setSetApplicableUser(String setApplicableUser) {
            this.setApplicableUser = setApplicableUser;
        }

        public String getManageTrainingCentres() {
            return manageTrainingCentres;
        }

        public void setManageTrainingCentres(String manageTrainingCentres) {
            this.manageTrainingCentres = manageTrainingCentres;
        }

        public String getProcessAUsersCourse() {
            return processAUsersCourse;
        }

        public void setProcessAUsersCourse(String processAUsersCourse) {
            this.processAUsersCourse = processAUsersCourse;
        }

        public String getBookAUsersCourse() {
            return bookAUsersCourse;
        }

        public void setBookAUsersCourse(String bookAUsersCourse) {
            this.bookAUsersCourse = bookAUsersCourse;
        }

    }

    public class TrainingPlateform {

        @SerializedName("view_training_plateform")
        @Expose
        private String viewTrainingPlateform;
        @SerializedName("feedback_on_tutorials")
        @Expose
        private String feedbackOnTutorials;

        public String getViewTrainingPlateform() {
            return viewTrainingPlateform;
        }

        public void setViewTrainingPlateform(String viewTrainingPlateform) {
            this.viewTrainingPlateform = viewTrainingPlateform;
        }

        public String getFeedbackOnTutorials() {
            return feedbackOnTutorials;
        }

        public void setFeedbackOnTutorials(String feedbackOnTutorials) {
            this.feedbackOnTutorials = feedbackOnTutorials;
        }

    }

    public class Users {

        @SerializedName("view")
        @Expose
        private String view;
        @SerializedName("create")
        @Expose
        private String create;
        @SerializedName("update")
        @Expose
        private String update;
        @SerializedName("Delete")
        @Expose
        private String delete;
        @SerializedName("change_pass")
        @Expose
        private String changePass;
        @SerializedName("block_user")
        @Expose
        private String blockUser;
        @SerializedName("login_as_other_user")
        @Expose
        private String loginAsOtherUser;
        @SerializedName("set_gangs")
        @Expose
        private String setGangs;
        @SerializedName("set_access_level")
        @Expose
        private String setAccessLevel;
        @SerializedName("set_cloud_access_level")
        @Expose
        private String setCloudAccessLevel;
        @SerializedName("set_others_permission")
        @Expose
        private String setOthersPermission;
        @SerializedName("set_permission_groups")
        @Expose
        private String setPermissionGroups;
        @SerializedName("manage_user_attr")
        @Expose
        private String manageUserAttr;
        @SerializedName("send_welcome_email")
        @Expose
        private String sendWelcomeEmail;
        @SerializedName("see_hourly_rates")
        @Expose
        private String seeHourlyRates;
        @SerializedName("set_hourly_rates")
        @Expose
        private String setHourlyRates;
        @SerializedName("approve_pay_rates")
        @Expose
        private String approvePayRates;
        @SerializedName("view_add_notes")
        @Expose
        private String viewAddNotes;
        @SerializedName("distribution_group")
        @Expose
        private String distributionGroup;

        public String getView() {
            return view;
        }

        public void setView(String view) {
            this.view = view;
        }

        public String getCreate() {
            return create;
        }

        public void setCreate(String create) {
            this.create = create;
        }

        public String getUpdate() {
            return update;
        }

        public void setUpdate(String update) {
            this.update = update;
        }

        public String getDelete() {
            return delete;
        }

        public void setDelete(String delete) {
            this.delete = delete;
        }

        public String getChangePass() {
            return changePass;
        }

        public void setChangePass(String changePass) {
            this.changePass = changePass;
        }

        public String getBlockUser() {
            return blockUser;
        }

        public void setBlockUser(String blockUser) {
            this.blockUser = blockUser;
        }

        public String getLoginAsOtherUser() {
            return loginAsOtherUser;
        }

        public void setLoginAsOtherUser(String loginAsOtherUser) {
            this.loginAsOtherUser = loginAsOtherUser;
        }

        public String getSetGangs() {
            return setGangs;
        }

        public void setSetGangs(String setGangs) {
            this.setGangs = setGangs;
        }

        public String getSetAccessLevel() {
            return setAccessLevel;
        }

        public void setSetAccessLevel(String setAccessLevel) {
            this.setAccessLevel = setAccessLevel;
        }

        public String getSetCloudAccessLevel() {
            return setCloudAccessLevel;
        }

        public void setSetCloudAccessLevel(String setCloudAccessLevel) {
            this.setCloudAccessLevel = setCloudAccessLevel;
        }

        public String getSetOthersPermission() {
            return setOthersPermission;
        }

        public void setSetOthersPermission(String setOthersPermission) {
            this.setOthersPermission = setOthersPermission;
        }

        public String getSetPermissionGroups() {
            return setPermissionGroups;
        }

        public void setSetPermissionGroups(String setPermissionGroups) {
            this.setPermissionGroups = setPermissionGroups;
        }

        public String getManageUserAttr() {
            return manageUserAttr;
        }

        public void setManageUserAttr(String manageUserAttr) {
            this.manageUserAttr = manageUserAttr;
        }

        public String getSendWelcomeEmail() {
            return sendWelcomeEmail;
        }

        public void setSendWelcomeEmail(String sendWelcomeEmail) {
            this.sendWelcomeEmail = sendWelcomeEmail;
        }

        public String getSeeHourlyRates() {
            return seeHourlyRates;
        }

        public void setSeeHourlyRates(String seeHourlyRates) {
            this.seeHourlyRates = seeHourlyRates;
        }

        public String getSetHourlyRates() {
            return setHourlyRates;
        }

        public void setSetHourlyRates(String setHourlyRates) {
            this.setHourlyRates = setHourlyRates;
        }

        public String getApprovePayRates() {
            return approvePayRates;
        }

        public void setApprovePayRates(String approvePayRates) {
            this.approvePayRates = approvePayRates;
        }

        public String getViewAddNotes() {
            return viewAddNotes;
        }

        public void setViewAddNotes(String viewAddNotes) {
            this.viewAddNotes = viewAddNotes;
        }

        public String getDistributionGroup() {
            return distributionGroup;
        }

        public void setDistributionGroup(String distributionGroup) {
            this.distributionGroup = distributionGroup;
        }

    }

    public class Valuations {

        @SerializedName("view_quote_in")
        @Expose
        private String viewQuoteIn;
        @SerializedName("view_quote_out")
        @Expose
        private String viewQuoteOut;
        @SerializedName("create_quotes_out")
        @Expose
        private String createQuotesOut;
        @SerializedName("create_quotes_in")
        @Expose
        private String createQuotesIn;
        @SerializedName("invite_users_in")
        @Expose
        private String inviteUsersIn;
        @SerializedName("delete_value")
        @Expose
        private String deleteValue;
        @SerializedName("submit_valuation")
        @Expose
        private String submitValuation;

        public String getViewQuoteIn() {
            return viewQuoteIn;
        }

        public void setViewQuoteIn(String viewQuoteIn) {
            this.viewQuoteIn = viewQuoteIn;
        }

        public String getViewQuoteOut() {
            return viewQuoteOut;
        }

        public void setViewQuoteOut(String viewQuoteOut) {
            this.viewQuoteOut = viewQuoteOut;
        }

        public String getCreateQuotesOut() {
            return createQuotesOut;
        }

        public void setCreateQuotesOut(String createQuotesOut) {
            this.createQuotesOut = createQuotesOut;
        }

        public String getCreateQuotesIn() {
            return createQuotesIn;
        }

        public void setCreateQuotesIn(String createQuotesIn) {
            this.createQuotesIn = createQuotesIn;
        }

        public String getInviteUsersIn() {
            return inviteUsersIn;
        }

        public void setInviteUsersIn(String inviteUsersIn) {
            this.inviteUsersIn = inviteUsersIn;
        }

        public String getDeleteValue() {
            return deleteValue;
        }

        public void setDeleteValue(String deleteValue) {
            this.deleteValue = deleteValue;
        }

        public String getSubmitValuation() {
            return submitValuation;
        }

        public void setSubmitValuation(String submitValuation) {
            this.submitValuation = submitValuation;
        }

    }

    public class Variations {

        @SerializedName("view_variation")
        @Expose
        private String viewVariation;
        @SerializedName("create_variation")
        @Expose
        private String createVariation;
        @SerializedName("submit_valuation")
        @Expose
        private String submitValuation;
        @SerializedName("approve_reject_variation")
        @Expose
        private String approveRejectVariation;

        public String getViewVariation() {
            return viewVariation;
        }

        public void setViewVariation(String viewVariation) {
            this.viewVariation = viewVariation;
        }

        public String getCreateVariation() {
            return createVariation;
        }

        public void setCreateVariation(String createVariation) {
            this.createVariation = createVariation;
        }

        public String getSubmitValuation() {
            return submitValuation;
        }

        public void setSubmitValuation(String submitValuation) {
            this.submitValuation = submitValuation;
        }

        public String getApproveRejectVariation() {
            return approveRejectVariation;
        }

        public void setApproveRejectVariation(String approveRejectVariation) {
            this.approveRejectVariation = approveRejectVariation;
        }

    }

}

