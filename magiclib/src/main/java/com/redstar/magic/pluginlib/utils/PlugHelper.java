package com.redstar.magic.pluginlib.utils;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class PlugHelper {
    private Callback callback;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String targetApkName="plug.key";//写入到插件apk里的配置文件
    private String apkFile = "/dltestapk/testtt.apk";//目标插件apk的sd卡路径
    private String sdPath;//sd卡路径

    public PlugHelper() {
        sdPath = Environment.getExternalStorageDirectory().getPath();
        apkFile = sdPath + apkFile;//最终的目标插件spk的路径
    }

    public void checkVersion(final Callback ck) {
        this.callback = ck;
        new Thread(new Runnable() {
            @Override
            public void run() {

                String apkVersion = null;
                String plugVersion = null;

                try {
                    ZipInputStream zis = new ZipInputStream(new FileInputStream(new File(apkFile)));

                    ZipEntry entry = zis.getNextEntry();
                    while (entry != null) {
                        if (targetApkName.equals(entry.getName())) {

                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            byte[] buffer = new byte[1024];
                            int len = -1;
                            while ((len = zis.read(buffer, 0, buffer.length)) > -1) {
                                byteArrayOutputStream.write(buffer, 0, len);
                            }

                            String fileNeirong = new String(byteArrayOutputStream.toByteArray(), "UTF-8");

                            JSONObject jsobj=new JSONObject(fileNeirong);
                            plugVersion=jsobj.getString("pluginVersion");
                            apkVersion=jsobj.getString("pluginApkVersion");

                        }
                        entry = zis.getNextEntry();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (JSONException e) {
                    e.printStackTrace();
                }

                final String finalApkVersion = apkVersion;
                final String finalPlugVersion = plugVersion;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            if (!TextUtils.isEmpty(finalApkVersion) && !TextUtils.isEmpty(finalPlugVersion)) {
                                callback.onVersion(finalApkVersion, finalPlugVersion);
                            }
                        }
                    }
                });
            }
        }).start();
    }

    public void close() {
        callback = null;
    }

    public interface Callback {
        void onVersion(String appversion, String plugversion);
    }
}