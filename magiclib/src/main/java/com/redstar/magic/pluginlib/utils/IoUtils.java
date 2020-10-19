package com.redstar.magic.pluginlib.utils;

import java.io.Closeable;
import java.io.IOException;

import androidx.annotation.Nullable;

/**
 * Created by chen.huarong on 2019-09-28.
 */
public class IoUtils {

    /**
     * 关闭io
     *
     * @param io
     */
    public static void close(@Nullable Closeable io) {
        try {
            io.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
