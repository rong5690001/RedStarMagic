package com.redstar.bytecode;

import com.android.bundle.Errors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javassist.CtClass;

public class ClassTransformer {

    public static final int TYPE_CLASS = 1;
    public static final int TYPE_JAR = 2;

    private int type;

    private File outputProviderContentLocationFile;

    private List<Dir> dirs=new ArrayList<>();

    public ClassTransformer(int type, File outputProviderContentLocationFile) {

        this.type = type;
        this.outputProviderContentLocationFile=outputProviderContentLocationFile;

    }

    public int getType() {

        return type;

    }

    public File getOutputProviderContentLocationFile() {
        return outputProviderContentLocationFile;
    }

    public void addDir(Dir dir){
        dirs.add(dir);
    }

    public List<Dir> getDirs() {
        return dirs;
    }

    public static class Dir {

        private CtClass ctClass;

        public Dir(CtClass ctClass) {
            this.ctClass = ctClass;
        }

        public CtClass getCtClass() {
            return ctClass;
        }
    }

    public static class ClassDir extends Dir {

        private File outputFile;

        public ClassDir(CtClass ctClass, File outputFile) {
            super(ctClass);
            this.outputFile = outputFile;
        }

        public File getOutputFile() {
            return outputFile;
        }
    }

    public static class JarDir extends Dir {

        private String entryName;

        public JarDir(CtClass ctClass, String entryName) {
            super(ctClass);
            this.entryName = entryName;
        }

        public String getEntryName() {
            return entryName;
        }
    }

}