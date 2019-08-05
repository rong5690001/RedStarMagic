package com.redstar.bytecode.modifier;

import com.redstar.bytecode.ReplaceClassNameUtil;
import com.redstar.bytecode.ClassTransformer;

import java.util.ArrayList;

/**
 * 修改原activity到目标activity
 */
public class ActivityModifier extends AbsModifier {

    private String[] acs=new String[]{"xxx.xx.xxxx.A","com.redstar.bytecode.C"};

    @Override
    public void modifier(ArrayList<ClassTransformer> classtransformers) {

        for (int i = 0; i < classtransformers.size(); i++) {

            for (int j = 0; j < classtransformers.get(i).getDirs().size(); j++) {

                ClassTransformer.Dir dir=classtransformers.get(i).getDirs().get(j);
                ReplaceClassNameUtil.replaceClassName(dir.getCtClass(),acs[0],acs[1]);

            }

        }

    }

}
