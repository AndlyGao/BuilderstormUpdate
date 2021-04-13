package com.builderstrom.user.repository.retrofit.modals;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PojoPriceWorkModel extends BaseApiModel {


    private PriceWorkData data;

    public PriceWorkData getData() {
        return data;
    }

    public class PriceWorkData {

        @SerializedName("PriceworkListing")
        @Expose
        private List<PriceWorkListing> priceworkListing = null;

        public List<PriceWorkListing> getPriceWorkListing() {
            return priceworkListing;
        }

        public void setPriceworkListing(List<PriceWorkListing> priceworkListing) {
            this.priceworkListing = priceworkListing;
        }

    }

    public class PriceWorkListing {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("pricework_title")
        @Expose
        private String priceworkTitle;
        @SerializedName("project_id")
        @Expose
        private String projectId;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("added_by")
        @Expose
        private String addedBy;
        @SerializedName("date_added")
        @Expose
        private String dateAdded;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPriceWorkTitle() {
            return priceworkTitle;
        }

        public void setPriceworkTitle(String priceworkTitle) {
            this.priceworkTitle = priceworkTitle;
        }

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getAddedBy() {
            return addedBy;
        }

        public void setAddedBy(String addedBy) {
            this.addedBy = addedBy;
        }

        public String getDateAdded() {
            return dateAdded;
        }

        public void setDateAdded(String dateAdded) {
            this.dateAdded = dateAdded;
        }

        @Override
        public String toString() {
            return priceworkTitle;
        }
    }
}
