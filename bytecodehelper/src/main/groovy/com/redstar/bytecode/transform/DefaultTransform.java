package com.redstar.bytecode.transform;


import com.redstar.bytecode.modifier.AbsModifier;
import com.redstar.bytecode.modifier.ActivityModifier;
import com.redstar.bytecode.modifier.RemoveClassModifier;
import com.redstar.bytecode.modifiermanager.ModifierManager;

import org.gradle.api.Project;

import java.io.File;
import java.util.ArrayList;

/**
 * 默认的transform
 */
public class DefaultTransform extends ModifierTransform {

    public DefaultTransform(Project project, File androidJar, ClassLoader contextClassLoader) {
        super(project, androidJar, contextClassLoader);
    }

    @Override
    protected ModifierManager createModifier() {

        ArrayList<AbsModifier> modifiers=new ArrayList();

        modifiers.add(new ActivityModifier());//修改目标activity
        modifiers.add(new RemoveClassModifier());//删除指定类



        return new ModifierManager(modifiers);

    }

}
