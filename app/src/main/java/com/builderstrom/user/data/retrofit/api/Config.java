//package com.builderstrom.user.repository.retrofit.api;
//
///**
// * Created by anil_singhania on 25/09/16.
// */
//public class Config {
//
//
//    private static final String DEV_SERVER_URL = "https://dev.builderstorm.com/mobileapp";
//    private static final String LIVE_SERVER_URL = "https://dev.builderstorm.com/mobileapp";
//    private static final String DEFAULT_SERVER_URL = DEV_SERVER_URL;
//    public static String IMAGE_URL = "https://dev.builderstorm.com/mobileapp";
//    public static AppMode appMode = AppMode.DEV;
//    static String BASE_URL = "https://dev.builderstorm.com/mobileapp";
//
//    static public String getBaseURL() {
//        init(appMode);
//        return BASE_URL;
//    }
//
//    public static String getDefaultServerUrl() {
//        return DEFAULT_SERVER_URL;
//    }
//
//    public static String getLiveServerUrl() {
//        return LIVE_SERVER_URL;
//    }
//
//    public static String getDevServerUrl() {
//        return DEV_SERVER_URL;
//    }
//
//    /**
//     * Initialize all the variable in this method
//     *
//     * @param appMode
//     */
//    public static void init(AppMode appMode) {
//
//        switch (appMode) {
//            case DEV:
//                BASE_URL = "https://dev.builderstorm.com/mobileapp";
//                break;
//            case TEST:
//                BASE_URL = "https://dev.builderstorm.com/mobileapp";
//                break;
//            case LIVE:
//                BASE_URL = "https://dev.builderstorm.com/mobileapp";
//                break;
//        }
//    }
//
//    public enum AppMode {
//        DEV, TEST, LIVE
//    }
//
//}
