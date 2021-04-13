package com.builderstrom.user.utils;

import android.content.Context;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.config.PropertyConfigurator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by anilsinghania at ideaFoundation on 18/4/2019.
 */
public class ClientLogger {
    private static final DateFormat formatter = new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss:SSS");

    private static String LOGGER_NAME               = "LOGS";
    private static String LOGGER_NAME_WITH_SPACE    = "LOGS ";


    public static int ANDROID_NATIVE_LOGGER        = 1;
    public static int MICROLOG_4_ANDROID_LOGGER    = 2;

    private static int type;

    /**
     * This initializes the logger depending on the loggerType passed to it. If it is microlog
     * logger, it configures the property configurator also.
     * This method should be called before any logging.
     * @param context
     * @param loggerType
     */
    public static void init(Context context, int loggerType){
        type = loggerType;
        if (type == MICROLOG_4_ANDROID_LOGGER){
            PropertyConfigurator.getConfigurator(context).configure();
        }
    }

    public static void mi(String tag, String msg){
        if (type == MICROLOG_4_ANDROID_LOGGER){
            Logger logger = LoggerFactory.getLogger(LOGGER_NAME_WITH_SPACE);
            logger.info(formatter.format(System.currentTimeMillis())
                    + " >>> " + tag
                    + "::" + msg + " (" + Thread.currentThread().getName() + ")");
        }
        else {
            //Log.i(tag, msg);
        }
    }

    public static void mo(String tag, String msg){
        if (type == MICROLOG_4_ANDROID_LOGGER){
            Logger logger = LoggerFactory.getLogger(LOGGER_NAME_WITH_SPACE);
            logger.info(formatter.format(System.currentTimeMillis())
                    + " <<< " + tag
                    + "::" + msg + " (" + Thread.currentThread().getName() + ")");
        }
        else {
            // Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg){
        if (type == MICROLOG_4_ANDROID_LOGGER){
            Logger logger = LoggerFactory.getLogger(LOGGER_NAME);
            logger.debug(formatter.format(System.currentTimeMillis())
                    + " " + tag
                    + "::" + msg + " (" + Thread.currentThread().getName() + ")");
        }
        else {
            // Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg){
        if (type == MICROLOG_4_ANDROID_LOGGER){
            Logger logger = LoggerFactory.getLogger(LOGGER_NAME_WITH_SPACE);
            logger.info(formatter.format(System.currentTimeMillis())
                    + " " + tag
                    + "::" + msg + " (" + Thread.currentThread().getName() + ")");
        }
        else {
            // Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg){
        if (type == MICROLOG_4_ANDROID_LOGGER){
            Logger logger = LoggerFactory.getLogger(LOGGER_NAME);
            logger.error(formatter.format(System.currentTimeMillis())
                    + " " + tag
                    + "::" + msg + " (" + Thread.currentThread().getName() + ")");
        }
        else {
            // Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable t){
        if (type == MICROLOG_4_ANDROID_LOGGER){
            Logger logger = LoggerFactory.getLogger(LOGGER_NAME);
            logger.error(formatter.format(System.currentTimeMillis())
                    + " " + tag
                    + "::" + msg + " (" + Thread.currentThread().getName() + ")", t);
        }
        else {
            // Log.e(tag, msg, t);
        }
    }

    public static void w(String tag, String msg){
        if (type == MICROLOG_4_ANDROID_LOGGER){
            Logger logger = LoggerFactory.getLogger(LOGGER_NAME_WITH_SPACE);
            logger.warn(formatter.format(System.currentTimeMillis())
                    + " " + tag
                    + "::" + msg + " (" + Thread.currentThread().getName() + ")");
        }
        else {
            // Log.w(tag, msg);
        }
    }
}
