package com.builderstrom.user.repository.retrofit.modals;

import java.util.List;

public class PojoTSHoliday extends BaseApiModel {
    private List<PojoHoliday> data = null;

    public List<PojoHoliday> getData() {
        return data;
    }

    public class PojoHoliday {
        private String requested_date = "";
        private String message = "";
        private Integer holiday_added;


        public String getRequested_date() {
            return requested_date;
        }

        public String getMessage() {
            return message;
        }

        public Integer getHoliday_added() {
            return holiday_added;
        }
    }
}
