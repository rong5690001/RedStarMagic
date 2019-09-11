package com.redstar.bytecode.modifier;

import com.redstar.bytecode.ClassTransformer;
import java.util.ArrayList;
import javassist.CtClass;

/**
 * 修改原activity到目标activity
 */
public class ActivityModifier extends AbsModifier {

    private String[] acs=new String[]{"xxx.xx.xxxx.A","com.redstar.bytecode.C"};

    private long replaceCount;

    @Override
    public void modifier(ArrayList<ClassTransformer> classtransformers) {

        for (int i = 0; i < classtransformers.size(); i++) {

            for (int j = 0; j < classtransformers.get(i).getDirs().size(); j++) {

                ClassTransformer.Dir dir=classtransformers.get(i).getDirs().get(j);
                replaceClassName(dir.getCtClass(),acs[0],acs[1]);

            }

        }

    }

    private void replaceClassName(CtClass ctClass, String orginName, String targetName){
        replaceCount++;
        String ctold=ctClass.getName();
        ctClass.replaceClassName(orginName,targetName);
        String ctnew=ctClass.getName();
        System.out.println(ActivityModifier.this.getClass().getName()+" : count :"+replaceCount+"  -- 修改前:  "+ctold+" 修改后: "+ctnew);
    }


}
