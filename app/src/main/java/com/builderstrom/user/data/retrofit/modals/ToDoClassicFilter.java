package com.builderstrom.user.data.retrofit.modals;

public class ToDoClassicFilter {
    String id;
    String name;
    String category;
    private UserFilter userFilter;
    private CategotyFilter categotyFilter;
    private DateFilter dateFilter;
    private StatusFilter statusFilter;

    public ToDoClassicFilter() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public UserFilter getUserFilter() {
        return userFilter;
    }

    public void setUserFilter(UserFilter userFilter) {
        this.userFilter = userFilter;
    }

    public CategotyFilter getCategotyFilter() {
        return categotyFilter;
    }

    public void setCategotyFilter(CategotyFilter categotyFilter) {
        this.categotyFilter = categotyFilter;
    }

    public DateFilter getDateFilter() {
        return dateFilter;
    }

    public void setDateFilter(DateFilter dateFilter) {
        this.dateFilter = dateFilter;
    }

    public StatusFilter getStatusFilter() {
        return statusFilter;
    }

    public void setStatusFilter(StatusFilter statusFilter) {
        this.statusFilter = statusFilter;
    }

    public class UserFilter {
        String userId;
        String userName;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }

    public class CategotyFilter {
        String categoryId;
        String categoryName;

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
    }

    public class DateFilter {
        String startDate;
        String endDate;

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
    }

    public class StatusFilter {
        String statusId;
        String statusName;

        public String getStatusId() {
            return statusId;
        }

        public void setStatusId(String statusId) {
            this.statusId = statusId;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }
    }
}
