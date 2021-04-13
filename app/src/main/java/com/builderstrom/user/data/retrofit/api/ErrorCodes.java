package com.builderstrom.user.data.retrofit.api;

public class ErrorCodes {

    public static boolean checkCode(int errorCode) {

        if (errorCode == ApiResponseFlags.OK.getOrdinal()) {
            return true;
        } else if (errorCode == ApiResponseFlags.BAD_REQUEST.getOrdinal()) {
            return false;
        } else if (errorCode == ApiResponseFlags.UNAUTHORIZED.getOrdinal()) {
            return false;
        } else if (errorCode == ApiResponseFlags.USER_NOT_FOUND.getOrdinal()) {
            return false;
        } else if (errorCode == ApiResponseFlags.USER_ALREADY_CHECKED_IN.getOrdinal()) {
            return false;
        } else if (errorCode == ApiResponseFlags.INTERNAL_SERVER_ERROR.getOrdinal()) {
            return false;
        } else if (errorCode == ApiResponseFlags.No_services_on_this_location_found.getOrdinal()) {
            return false;
        } else if (errorCode == ApiResponseFlags.Created.getOrdinal()) {
            return true;
        } else if (errorCode == ApiResponseFlags.Customer_Blocked.getOrdinal()) {
            return false;
        } else {
            return false;
        }
    }

    public enum ApiResponseFlags {
        OK(200),
        Created(201),
        NON_AUTHORITATIVE_INFORMATION(203),
        NO_CONTENT(203),
        RESET_CONTENT(203),
        PARTIAL_CONTENT(203),
        MULTI_STATUS(203),
        ALREADY_REPORTED(203),
        MULTIPLE_CHOICES(203),
        MOVE_PERMANENTLY(203),
        FOUND(203),
        SEE_OTHER(203),
        SEE_(203),
        Customer_Blocked(403),

        No_services_on_this_location_found(204),

        BAD_REQUEST(400),
        UNAUTHORIZED(401),
        USER_NOT_FOUND(406),
        USER_ALREADY_CHECKED_IN(409),
        INTERNAL_SERVER_ERROR(500);
        private int ordinal;

        ApiResponseFlags(int ordinal) {
            this.ordinal = ordinal;
        }

        public int getOrdinal() {
            return ordinal;
        }
    }

}
