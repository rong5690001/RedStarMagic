package com.redstar.magic.pluginlib.exception;

import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * 占位activity池异常
 *
 * @author chen.huarong on 2019-11-12
 */
public class ActivityPoolException extends Exception {

    //找不到对应启动模式的占位activity
    public static final String MESSAGE_NOT_FIND_ACTIVITY = "Unable to find a placeholder activity" +
            " corresponding to the launchMode";
    //对应启动模式的占位activity被用完了
    public static final String MESSAGE_ACTIVITY_USED_UP = "The placeholder activity corresponding" +
            " to the launchMode is used up";

    public ActivityPoolException() {
    }

    public ActivityPoolException(String message) {
        super(message);
    }

    public ActivityPoolException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActivityPoolException(Throwable cause) {
        super(cause);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ActivityPoolException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
