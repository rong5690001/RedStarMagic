package com.redstar.magic.pluginlib.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;
import android.view.InflateException;

import com.redstar.magic.pluginlib.tools.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.zip.ZipFile;

import androidx.annotation.Nullable;

/**
 * Created by chen.huarong on 2019-07-28.
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * The number of bytes in a kilobyte.
     */
    public static final long ONE_KB = 1024;

    /**
     * The number of bytes in a megabyte.
     */
    public static final long ONE_MB = ONE_KB * ONE_KB;

    /**
     * The file copy buffer size (30 MB)
     */
    private static final long FILE_COPY_BUFFER_SIZE = ONE_MB * 30;

    /**
     * 将Assets目录下的fileName文件拷贝至app缓存目录
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String copyAssetToFile(Context context, String fileName, File target) {
        try {
            if (!target.exists()) {
                target.mkdirs();
            }
            File outFile = new File(target, fileName);
            copyInputStreamToFile(context.getAssets().open(fileName), outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Copies a file to a new location.
     *
     * @param srcFile  the validated source file, must not be {@code null}
     * @param destFile the validated destination file, must not be {@code null}
     * @throws IOException if an error occurs
     */
    public static void copyFile(File srcFile, File destFile) throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel input = null;
        FileChannel output = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);
            input = fis.getChannel();
            output = fos.getChannel();
            long size = input.size();
            long pos = 0;
            long count;
            while (pos < size) {
                count = size - pos > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : size - pos;
                pos += output.transferFrom(input, pos, count);
            }
        } finally {
            IoUtils.close(output);
            IoUtils.close(fos);
            IoUtils.close(input);
            IoUtils.close(fis);
        }

        if (srcFile.length() != destFile.length()) {
            throw new IOException("Failed to copy full contents from '"
                    + srcFile + "' to '" + destFile + "'");
        }

    }


    /**
     * 复制文件
     *
     * @param source 源文件输入流
     * @param target 目标文件
     * @return 目标文件绝对路径
     */
    public static String copyInputStreamToFile(InputStream source, File target) {
        try {
            FileOutputStream fos = new FileOutputStream(target);
            copyStream(source, fos);
            IoUtils.close(fos);
            IoUtils.close(source);
            return target.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 复制io流
     *
     * @param is  输入流
     * @param fos 输出流
     * @throws IOException
     */
    public static void copyStream(InputStream is, FileOutputStream fos) throws IOException {
        byte[] buffer = new byte[is.available()];
        int byteCount;
        while ((byteCount = is.read(buffer)) != -1) {
            fos.write(buffer, 0, byteCount);
        }
    }

    /**
     * Cleans a directory without deleting it.
     *
     * @param dir directory to clean
     */
    public static void cleanDir(File dir) {
        if (dir.isDirectory()) {
            final File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        cleanDir(file);
                    }
                    if (!file.delete()) {
                        Logger.w(TAG, "delete %s error", file.getName());
                    }
                }
            }
        }
    }

    public static void closeZipFileQuietly(@Nullable ZipFile zipFile) {
        if (zipFile != null) {
            try {
                zipFile.close();
            } catch (Exception ignored) {
                // ignore it
            }
        }
    }

    /**
     * assets中是否存在指定文件
     *
     * @param fileName 指定文件名
     * @return True or False
     */
    public static boolean assetsFileExist(Context context, String fileName) {
        try {
            for (String plugin : context.getAssets().list("plugins")) {
                if (TextUtils.equals(plugin, fileName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static class XmlPullParserUtil {

        public static String getLayoutStartTagName(Resources res, int layoutResID) {
            XmlResourceParser parser;
            String name;
            try {
                int type;
                parser = res.getLayout(layoutResID);
                while ((type = parser.next()) != XmlPullParser.START_TAG &&
                        type != XmlPullParser.END_DOCUMENT) {
                    // Empty
                }

                if (type != XmlPullParser.START_TAG) {
                    throw new InflateException(parser.getPositionDescription()
                            + ": No start tag found!");
                }
                name = parser.getName();
            } catch (XmlPullParserException e) {
                final InflateException ie = new InflateException(e.getMessage(), e);
                ie.setStackTrace(new StackTraceElement[0]);
                throw ie;
            } catch (Exception e) {
                final InflateException ie = new InflateException(e.getMessage(), e);
                ie.setStackTrace(new StackTraceElement[0]);
                throw ie;
            }
            return name;
        }
    }
}
