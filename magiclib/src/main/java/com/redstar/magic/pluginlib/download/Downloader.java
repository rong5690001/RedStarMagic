package com.redstar.magic.pluginlib.download;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by chen.huarong on 2019-08-22.
 * 下载器
 */
public class Downloader {

    private static final int TIME_OUT = 15 * 1000;
    private static final Vector<String> sDownloading = new Vector<>();
    private static Handler sMainHandler = new Handler(Looper.getMainLooper());

    private Call mCall;

    private Context mContext;

    private String mUrl;

    private File mTargetFile;

    private OnDownloadListener mOnDownloadListener;

    private Downloader(Context context, String url, File targetFile, OnDownloadListener onDownloadListener) {
        mContext = context.getApplicationContext();
        mUrl = url;
        mTargetFile = targetFile;
        mOnDownloadListener = onDownloadListener;
    }

    /**
     * 下载文件
     */
    public synchronized void download() {

        /**
         * 文件正在下载中，直接退出
         */
        if (sDownloading.contains(mUrl)) {
            return;
        }

//        String fileName = getFileName(mUrl);
//
//        if (TextUtils.isEmpty(fileName)) {
//            throw new NullPointerException("fileName is Null");
//        }

        if (mTargetFile.exists()) {
            mTargetFile.delete();
        }

        try {
            mTargetFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(TIME_OUT, TimeUnit.SECONDS).build();

        final Request request = new Request.Builder().url(mUrl).build();

        mCall = client.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (mOnDownloadListener != null) {
                    mOnDownloadListener.onFailure(call, e);
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {

                InputStream inputStream = null;
                FileOutputStream outputStream = null;

                try {

                    int len;
                    long count = 0;

                    final long total = getMaxContentLength(response);
                    inputStream = response.body().byteStream();
                    outputStream = new FileOutputStream(mTargetFile);

                    byte[] buffer = new byte[1024];

                    if (mOnDownloadListener != null) {
                        mOnDownloadListener.onStart(mTargetFile.getAbsolutePath(), total);
                    }

                    while ((len = inputStream.read(buffer)) != -1) {

                        if (len + count < total) {
                            outputStream.write(buffer, 0, len);
                            count += len;
                        } else {
                            len = (int) (total - count);
                            outputStream.write(buffer, 0, len);
                            count += len;
                        }

                        if (mOnDownloadListener != null) {
                            final long finalCount = count;
                            sMainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mOnDownloadListener.onProgress(finalCount, total);
                                }
                            });
                        }

                        if (count >= total) {
                            if (mOnDownloadListener != null) {
                                sMainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        sDownloading.remove(mUrl);
                                        mOnDownloadListener.onFinished();
                                    }
                                });
                            }
                            break;
                        }
                    }

                    outputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    sDownloading.remove(mUrl);
                    if (mOnDownloadListener != null) {
                        mOnDownloadListener.onFailure(call, e);
                    }
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        sDownloading.remove(mUrl);
                    }
                }

            }
        });
        sDownloading.add(mUrl);
    }

    /**
     * 取消下载
     *
     * @return
     */
    public void cancel() {
        if (mCall != null) {
            mCall.cancel();
        }
        sDownloading.remove(mUrl);
        deleteDownloadedFile();
    }

    /**
     * 获取文件大小
     *
     * @param response
     * @return
     */
    private long getMaxContentLength(Response response) {
        long maxLong = 0;
        try {
            String content_range = response.header("Content-Length");
            String max = content_range.substring(content_range.indexOf("/") + 1);
            maxLong = Long.parseLong(max);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxLong;
    }

    /**
     * 删除下载文件
     *
     * @return
     */
    private boolean deleteDownloadedFile() {
        if (mTargetFile.exists()) {
            return mTargetFile.delete();
        }
        return false;
    }

//    /**
//     * 判断文件是否存在
//     *
//     * @param url
//     * @return
//     */
//    public static boolean isFileExist(Context context, String url) {
//        return getTargetFile(context, url).exists();
//    }

//    /**
//     * 获取文件存放本地路径
//     *
//     * @param context
//     * @param url
//     * @return
//     */
//    public static String getDefaultFileSavePath(Context context, String url) {
//        return getTargetFile(context, url).getAbsolutePath();
//    }
//
//    @NonNull
//    private static File getDownloadRootFile(Context context) {
//        File file = context.getApplicationContext().getExternalFilesDir("download");
//        if (!file.exists()) {
//            file.mkdir();
//        }
//        return file;
//    }
//
//    public static File getTargetFile(Context context, String url) {
//        return new File(getRootPath(context), getFileName(url));
//    }
//
//    private static String getRootPath(Context context) {
//        return getDownloadRootFile(context).getAbsolutePath();
//    }

//    /**
//     * md5加密url作为文件名
//     *
//     * @param url
//     * @return
//     */
//    private static String getFileName(String url) {
//        if (TextUtils.isEmpty(url)) {
//            return url;
//        }
//        String fileName = url;
//        try {
//            String suffix = url.substring(url.lastIndexOf("."));
//            fileName = MD5Utils.md5Encode(url) + suffix;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return fileName;
//    }

    public static class Builder {

        Context mContext;

        String mUrl;
        /**
         * 存储文件
         */
        File mTargetFile;

        OnDownloadListener mOnDownloadListener;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder url(String url) {
            mUrl = url;
            return this;
        }

        public Builder callback(OnDownloadListener onDownloadListener) {
            mOnDownloadListener = onDownloadListener;
            return this;
        }

        public Builder targetFile(File targetFile) {
            mTargetFile = targetFile;
            return this;
        }

        public Downloader build() {
            return new Downloader(mContext, mUrl, mTargetFile, mOnDownloadListener);
        }
    }
}
