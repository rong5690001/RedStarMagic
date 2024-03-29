package com.redstar.magic.pluginlib.tools;

/**
 * Created by chen.huarong on 2019-06-27.
 * 校验类
 */
public class Validate {

    /**
     * 条件校验，不通过就抛异常
     *
     * @param expression
     * @param message
     */
    public static void validate(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 条件校验，不通过就抛异常
     *
     * @param expression
     * @param message
     */
    public static void validate(boolean expression, String message, Object... value) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, value));
        }
    }
}
