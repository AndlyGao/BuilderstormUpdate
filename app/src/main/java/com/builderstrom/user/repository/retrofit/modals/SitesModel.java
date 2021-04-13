package com.builderstrom.user.repository.retrofit.modals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SitesModel {

    @SerializedName("ResponseCode")
    @Expose
    private Boolean responseCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("sites")
    @Expose
    private List<Site> sites = null;

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

    public List<Site> getSites() {
        return sites;
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
    }

    public class Site {

        @SerializedName("subdomain")
        @Expose
        private String subdomain;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("clientsubdomainname")
        @Expose
        private String clientsubdomainname;
        @SerializedName("clientname")
        @Expose
        private String clientname;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("postcode")
        @Expose
        private String postcode;
        @SerializedName("contactnumber")
        @Expose
        private String contactnumber;

        public String getSubdomain() {
            return subdomain;
        }

        public void setSubdomain(String subdomain) {
            this.subdomain = subdomain;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getClientsubdomainname() {
            return clientsubdomainname;
        }

        public void setClientsubdomainname(String clientsubdomainname) {
            this.clientsubdomainname = clientsubdomainname;
        }

        public String getClientname() {
            return clientname;
        }

        public void setClientname(String clientname) {
            this.clientname = clientname;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getContactnumber() {
            return contactnumber;
        }

        public void setContactnumber(String contactnumber) {
            this.contactnumber = contactnumber;
        }

    }

}
