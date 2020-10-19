package com.redstar.magic.pluginlib.tools;

import android.util.Log;

/**
 * Created by chen.huarong on 2019-06-27.
 */
public class Logger {

    private static boolean sIsDebug = false;

    public static void debug(boolean isDebug) {
        sIsDebug = isDebug;
    }

    public static void v(String msg) {
        if (sIsDebug) {
            Log.v("", msg);
        }
    }

    public static void v(String tag, String message, Object... value) {
        if (sIsDebug) {
            Log.v(tag, String.format(message, value));
        }
    }

    public static void d(String tag, String message, Object... value) {
        if (sIsDebug) {
            Log.d(tag, String.format(message, value));
        }
    }

    public static void w(String tag, String message, Object... value) {
        if (sIsDebug) {
            Log.w(tag, String.format(message, value));
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (sIsDebug) {
            Log.w(tag, msg, tr);
        }
    }

    public static void e(String tag, String message, Object... value) {
        if (sIsDebug) {
            Log.e(tag, String.format(message, value));
        }
    }
}
