package com.redstar.magic.pluginlib.utils;
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

public class PluginVersionHelper {
    private Callback callback;
    private Handler handler = new Handler(Looper.getMainLooper());

    public PluginVersionHelper() {

    }

    public void checkVersion(final File file,final String targetApkName,final Callback ck) {
        this.callback = ck;
        new Thread(new Runnable() {
            @Override
            public void run() {

                String apkVersion = null;
                String plugVersion = null;

                try {
                    ZipInputStream zis = new ZipInputStream(new FileInputStream(file));

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
                            }else {
                                callback.onError();
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
        void onError();
    }
}
