package com.redstar.magic.pluginlib.download;

import okhttp3.Call;

/**
 * Created by chen.huarong on 2019-08-23.
 * 下载监听器
 */
public interface OnDownloadListener {

    void onStart(String absolutePath, long total);

    void onProgress(long count, long total);

    void onFinished();

    void onFailure(Call call, Exception e);
}
