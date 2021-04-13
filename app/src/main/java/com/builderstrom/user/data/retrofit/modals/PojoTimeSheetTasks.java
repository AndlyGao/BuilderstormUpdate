package com.builderstrom.user.data.retrofit.modals;


import java.util.List;

public class PojoTimeSheetTasks extends BaseApiModel {

    private List<TaskListing> data;

    public List<TaskListing> getData() {
        return data;
    }

    public void setData(List<TaskListing> data) {
        this.data = data;
    }

//    public class TSTask {
//
//        @SerializedName("TaskListing")
//        @Expose
//        private List<TaskListing> taskListing = null;
//
//        public List<TaskListing> getTaskListing() {
//            return taskListing;
//        }
//
//        public void setTaskListing(List<TaskListing> taskListing) {
//            this.taskListing = taskListing;
//        }
//
//    }


}
