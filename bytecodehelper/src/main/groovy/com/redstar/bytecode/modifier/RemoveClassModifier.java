package com.redstar.bytecode.modifier;

import com.redstar.bytecode.ClassTransformer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RemoveClassModifier extends AbsModifier {

    private String target="target";

    @Override
    public void modifier(ArrayList<ClassTransformer> classtransformers) {
        for (int i = 0; i < classtransformers.size(); i++) {

            List<ClassTransformer.Dir> dirs=classtransformers.get(i).getDirs();

            Iterator<ClassTransformer.Dir > it=dirs.iterator();

            while (it.hasNext()){

                ClassTransformer.Dir dir=it.next();
                System.out.println(RemoveClassModifier.this.getClass().getName()+" : "+dir.getCtClass().getName());
                if(dir.getCtClass().getName().equals(target)){
                    System.out.println(RemoveClassModifier.this.getClass().getName()+" : 移除了 : "+dir.getCtClass().getName());
                    dir.getCtClass().detach();
                    it.remove();
                }

            }

        }
    }
}
