package com.example.g1_orderfoodonline.utils;

import android.util.Log;

/**
 * Utility class for logging
 */
public class LogUtils {
    private static final String APP_TAG = "FoodOnline";
    private static final boolean DEBUG = true;

    public static void debug(String tag, String message) {
        if (DEBUG) {
            Log.d(APP_TAG + ":" + tag, message);
        }
    }

    public static void error(String tag, String message) {
        Log.e(APP_TAG + ":" + tag, message);
    }

    public static void error(String tag, String message, Throwable throwable) {
        Log.e(APP_TAG + ":" + tag, message, throwable);
    }

    public static void info(String tag, String message) {
        Log.i(APP_TAG + ":" + tag, message);
    }
}

