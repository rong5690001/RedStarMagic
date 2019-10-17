package com.redstar.bytecode;

import com.android.build.gradle.api.BaseVariant;
import com.android.build.gradle.api.BaseVariantOutput;
import com.google.common.io.Files;
import com.google.gson.JsonObject;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class PackagesTask extends DefaultTask {

    @Input
    public BaseVariant mVariant;

    public String pluginVersion;

    public String pluginApkVersion;

    @TaskAction
    public void copyPackages() {

        File originFile = getOriginFile();
        File outPutFile = new File(originFile.getParentFile().getAbsoluteFile() + "/testtt.apk");

        ZipInputStream in = null;
        ZipOutputStream out = null;

        try {

            Files.createParentDirs(outPutFile);
            in = new ZipInputStream(new FileInputStream(originFile));
            out = new ZipOutputStream(new FileOutputStream(outPutFile));
            ZipEntry entry = in.getNextEntry();

            while (entry != null) {

                String entryName=entry.getName();

                out.putNextEntry(new ZipEntry(entry.getName()));

                System.out.println("insert_apk: "+entryName);

                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, len);
                }
                entry = in.getNextEntry();

            }

            insertData(out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("insert_apk:  error: " + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("insert_apk:  error: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("insert_apk:  error: " + e.toString());
        } finally {

            try {

                if (out != null) {
                    out.closeEntry();
                    out.close();
                }

                if (in != null) {
                    in.close();
                }

            }catch (Exception e){

                e.printStackTrace();
                System.out.print("打包任务: error: " + e.toString());

            }

        }

    }

    private File getOriginFile() {

        Iterator<BaseVariantOutput> bvots = mVariant.getOutputs().iterator();

        while (bvots.hasNext()) {

            BaseVariantOutput ot = bvots.next();
            return ot.getOutputFile();

        }

        return null;

    }

    private void insertData(ZipOutputStream out) throws IOException {

        JsonObject js=new JsonObject();
        js.addProperty("pluginVersion",pluginVersion);
        js.addProperty("pluginApkVersion",pluginApkVersion);

        out.putNextEntry(new ZipEntry("plug.key"));
        byte[] by=js.toString().getBytes();
        out.write(by,0,by.length);

    }


}