package com.redstar.bytecode.transform;

import com.android.SdkConstants;
import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.tools.r8.com.google.common.io.Files;
import com.android.utils.FileUtils;
import com.redstar.bytecode.ClassTransformer;
import com.google.common.collect.FluentIterable;
import org.gradle.api.Project;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

public class InputOutPutTransform extends AbsTransform {

    protected ArrayList<ClassTransformer> classTransforms = new ArrayList();

    protected ClassPool classPool;

    protected File androidJar;

    protected ClassLoader contextClassLoader;

    public InputOutPutTransform(Project project, File androidJar, ClassLoader contextClassLoader) {
        super(project);
        this.androidJar = androidJar;
        this.contextClassLoader = contextClassLoader;
    }

    @Override
    protected void transformStart(TransformInvocation transformInvocation) throws IOException {

        transformInvocation.getOutputProvider().deleteAll();
        classTransforms.clear();
        createClassPool();

    }

    private void createClassPool() {

        classPool =new ClassPool(false);
        classPool.appendClassPath(new LoaderClassPath(contextClassLoader));
        try {
            classPool.appendClassPath(androidJar.getAbsolutePath());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void transformOnInput(TransformInvocation transformInvocation) throws IOException {

        Iterator<TransformInput> inputs = transformInvocation.getInputs().iterator();

        while (inputs.hasNext()) {

            TransformInput it = inputs.next();

            Iterator<DirectoryInput> dirInputs = it.getDirectoryInputs().iterator();

            Iterator<JarInput> jarInputs = it.getJarInputs().iterator();

            while (dirInputs.hasNext()) {

                DirectoryInput dir = dirInputs.next();

                ClassTransformer ctfer = new ClassTransformer(ClassTransformer.TYPE_CLASS, getOutputProviderContentLocationDirectory(transformInvocation, dir));
                classTransforms.add(ctfer);

                FluentIterable<File> allFiles = FileUtils.getAllFiles(dir.getFile());

                for (int i = 0; i < allFiles.size(); i++) {

                    File file = allFiles.get(i);

                    if (file != null && file.getName() != null && file.getName().endsWith(SdkConstants.DOT_CLASS)) {

                        FileInputStream stream = new FileInputStream(file);

                        CtClass ctClass = classPool.makeClass(stream);

                        ClassTransformer.ClassDir classDir = new ClassTransformer.ClassDir(ctClass, getClassDirOutputFile(ctfer.getOutputProviderContentLocationFile(), dir, file));

                        ctfer.addDir(classDir);


                    }

                }

            }

            while (jarInputs.hasNext()) {

                JarInput jar = jarInputs.next();

                ClassTransformer ctfer = new ClassTransformer(ClassTransformer.TYPE_JAR, getOutputProviderContentLocationJar(transformInvocation, jar));
                classTransforms.add(ctfer);

                ZipInputStream zis = new ZipInputStream(new FileInputStream(jar.getFile()));
                ZipEntry entry = zis.getNextEntry();

                while (entry != null) {

                    if (!entry.isDirectory() && entry.getName().endsWith(SdkConstants.DOT_CLASS)) {

                        CtClass ctClass = classPool.makeClass(zis);

                        ClassTransformer.JarDir jarDir = new ClassTransformer.JarDir(ctClass, entry.getName());

                        ctfer.addDir(jarDir);

                    }

                    entry = zis.getNextEntry();

                }

            }

        }

    }



    @Override
    protected void transformDo() {

    }

    @Override
    protected void transformOnOutput(TransformInvocation transformInvocation) throws IOException, NotFoundException, CannotCompileException {

        for (ClassTransformer sp : classTransforms) {

            int type = sp.getType();

            if (ClassTransformer.TYPE_CLASS == type) {

                for (int i = 0; i < sp.getDirs().size(); i++) {

                    ClassTransformer.ClassDir dir = (ClassTransformer.ClassDir) sp.getDirs().get(i);

                    Files.createParentDirs(dir.getOutputFile());

                    FileOutputStream fos = new FileOutputStream(dir.getOutputFile());

                    dir.getCtClass().toBytecode(new DataOutputStream(fos));

                    fos.close();

                }


            } else if (ClassTransformer.TYPE_JAR == type) {

                ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(sp.getOutputProviderContentLocationFile()));
                Files.createParentDirs(sp.getOutputProviderContentLocationFile());

                for (int i = 0; i < sp.getDirs().size(); i++) {

                    ClassTransformer.JarDir jarDir = (ClassTransformer.JarDir) sp.getDirs().get(i);

                    zos.putNextEntry(new ZipEntry(jarDir.getEntryName()));

                    jarDir.getCtClass().toBytecode(new DataOutputStream(zos));

                }

                zos.closeEntry();
                zos.close();

            }

        }

    }

    @Override
    protected void transformEnd(TransformInvocation transformInvocation) {

    }

    /**
     * 生成dir的输出目录
     *
     * @param dir
     * @param childFile
     * @return
     */
    private File getClassDirOutputFile(File outputProviderContentLocationFile, DirectoryInput dir, File childFile) {

        return new File(outputProviderContentLocationFile, FileUtils.relativePossiblyNonExistingPath(childFile, dir.getFile()));

    }

    private File getOutputProviderContentLocationDirectory(TransformInvocation transformInvocation, QualifiedContent dir) {
        return transformInvocation.getOutputProvider().getContentLocation(dir.getName(), dir.getContentTypes(), dir.getScopes(), Format.DIRECTORY);
    }

    private File getOutputProviderContentLocationJar(TransformInvocation transformInvocation, QualifiedContent dir) {
        String jarName = dir.getName();
       /* String md5Name = DigestUtils.md5Hex(dir.getFile().getAbsolutePath());

        if (jarName.endsWith(".jar")) {
            jarName = jarName.substring(0, jarName.length() - 4) + md5Name;
        }*/

        return transformInvocation.getOutputProvider().getContentLocation(jarName, dir.getContentTypes(), dir.getScopes(), Format.JAR);
    }

}
