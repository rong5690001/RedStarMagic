package com.redstar.magic.pluginlib;import android.util.Log;/** * Created by chen.huarong on 2019-06-27. */public class Logger {    private static boolean sIsDebug = false;    public static void debug(boolean isDebug) {        sIsDebug = isDebug;    }    public static void d(String tag, String msg) {        if (sIsDebug) {            Log.d(tag, msg);        }    }    public static void d(String tag, String message, Object... value) {        if (sIsDebug) {            Log.d(tag, String.format(message, value));        }    }    public static void e(String tag, String msg) {        if (sIsDebug) {            Log.e(tag, msg);        }    }    public static void e(String tag, String message, Object... value) {        if (sIsDebug) {            Log.e(tag, String.format(message, value));        }    }}