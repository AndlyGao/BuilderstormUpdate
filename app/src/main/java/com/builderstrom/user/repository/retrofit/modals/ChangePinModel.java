package com.builderstrom.user.repository.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePinModel {

    @SerializedName("ResponseCode")
    @Expose
    private Boolean responseCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public Boolean getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Boolean responseCode) {
        this.responseCode = responseCode;
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

    public class Data {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("mobilepin")
        @Expose
        private String mobilepin;
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
        @SerializedName("user_type")
        @Expose
        private String userType;
        @SerializedName("lngid")
        @Expose
        private String lngid;
        @SerializedName("block_status")
        @Expose
        private String blockStatus;
        @SerializedName("last_project_edited")
        @Expose
        private String lastProjectEdited;
        @SerializedName("added_on")
        @Expose
        private String addedOn;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("modified_on")
        @Expose
        private String modifiedOn;
        @SerializedName("link_validity")
        @Expose
        private String linkValidity;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("send_email_status")
        @Expose
        private String sendEmailStatus;
        @SerializedName("temp_password")
        @Expose
        private String tempPassword;
        @SerializedName("temp_project_id")
        @Expose
        private String tempProjectId;
        @SerializedName("is_induction")
        @Expose
        private String isInduction;
        @SerializedName("employee_type")
        @Expose
        private String employeeType;
        @SerializedName("reference_by")
        @Expose
        private String referenceBy;
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
        private String userOrder;
        @SerializedName("user_pay_rates")
        @Expose
        private String userPayRates;
        @SerializedName("is_signedin")
        @Expose
        private String isSignedin;
        @SerializedName("signin_date")
        @Expose
        private String signinDate;
        @SerializedName("signin_time")
        @Expose
        private String signinTime;
        @SerializedName("signin_project")
        @Expose
        private String signinProject;
        @SerializedName("rfid_code")
        @Expose
        private String rfidCode;
        @SerializedName("fingerprint_hash")
        @Expose
        private String fingerprintHash;
        @SerializedName("induction_id")
        @Expose
        private String inductionId;
        @SerializedName("is_deleted")
        @Expose
        private String isDeleted;
        @SerializedName("recovery_email")
        @Expose
        private String recoveryEmail;
        @SerializedName("fullname")
        @Expose
        private String fullname;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getMobilepin() {
            return mobilepin;
        }

        public void setMobilepin(String mobilepin) {
            this.mobilepin = mobilepin;
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

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getLngid() {
            return lngid;
        }

        public void setLngid(String lngid) {
            this.lngid = lngid;
        }

        public String getBlockStatus() {
            return blockStatus;
        }

        public void setBlockStatus(String blockStatus) {
            this.blockStatus = blockStatus;
        }

        public String getLastProjectEdited() {
            return lastProjectEdited;
        }

        public void setLastProjectEdited(String lastProjectEdited) {
            this.lastProjectEdited = lastProjectEdited;
        }

        public String getAddedOn() {
            return addedOn;
        }

        public void setAddedOn(String addedOn) {
            this.addedOn = addedOn;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getModifiedOn() {
            return modifiedOn;
        }

        public void setModifiedOn(String modifiedOn) {
            this.modifiedOn = modifiedOn;
        }

        public String getLinkValidity() {
            return linkValidity;
        }

        public void setLinkValidity(String linkValidity) {
            this.linkValidity = linkValidity;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSendEmailStatus() {
            return sendEmailStatus;
        }

        public void setSendEmailStatus(String sendEmailStatus) {
            this.sendEmailStatus = sendEmailStatus;
        }

        public String getTempPassword() {
            return tempPassword;
        }

        public void setTempPassword(String tempPassword) {
            this.tempPassword = tempPassword;
        }

        public String getTempProjectId() {
            return tempProjectId;
        }

        public void setTempProjectId(String tempProjectId) {
            this.tempProjectId = tempProjectId;
        }

        public String getIsInduction() {
            return isInduction;
        }

        public void setIsInduction(String isInduction) {
            this.isInduction = isInduction;
        }

        public String getEmployeeType() {
            return employeeType;
        }

        public void setEmployeeType(String employeeType) {
            this.employeeType = employeeType;
        }

        public String getReferenceBy() {
            return referenceBy;
        }

        public void setReferenceBy(String referenceBy) {
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

        public String getUserOrder() {
            return userOrder;
        }

        public void setUserOrder(String userOrder) {
            this.userOrder = userOrder;
        }

        public String getUserPayRates() {
            return userPayRates;
        }

        public void setUserPayRates(String userPayRates) {
            this.userPayRates = userPayRates;
        }

        public String getIsSignedin() {
            return isSignedin;
        }

        public void setIsSignedin(String isSignedin) {
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

        public String getSigninProject() {
            return signinProject;
        }

        public void setSigninProject(String signinProject) {
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

        public String getInductionId() {
            return inductionId;
        }

        public void setInductionId(String inductionId) {
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

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

    }

}
