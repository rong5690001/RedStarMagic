package com.redstar.bytecode;

import javassist.CtClass;

public class ReplaceClassNameUtil {

    public static int testCount;

    public static void replaceClassName(CtClass ctClass, String orginName, String targetName){
        testCount++;
        String ctold=ctClass.getName();
        ctClass.replaceClassName(orginName,targetName);
        String ctnew=ctClass.getName();
        System.out.println("ReplaceClassNameUtil : testCount : "+testCount+"  -- 修改前:  "+ctold+" 修改后: "+ctnew);
    }

}
