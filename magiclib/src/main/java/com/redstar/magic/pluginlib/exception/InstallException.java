package com.redstar.magic.pluginlib.exception;

/**
 * Created by chen.huarong on 2019-09-28.
 * 安装异常
 */
public class InstallException extends Exception {

    /**
     * 解析apk异常
     */
    public static final int ERROR_RESOLVE_APK = -1000;

    public int errorCode;

    public InstallException(int errorCode) {
        this.errorCode = errorCode;
    }

    public InstallException(String message) {
        super(message);
    }

    public InstallException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstallException(Throwable cause) {
        super(cause);
    }

}