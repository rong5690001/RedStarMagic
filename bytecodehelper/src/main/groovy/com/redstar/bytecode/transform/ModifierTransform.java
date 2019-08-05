package com.redstar.bytecode.transform;

import com.android.build.api.transform.TransformInvocation;
import com.redstar.bytecode.modifiermanager.AbsModifierManager;

import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;

public abstract class ModifierTransform extends InputOutPutTransform {

    private AbsModifierManager modifierTransform;

    public ModifierTransform(Project project, File androidJar, ClassLoader contextClassLoader) {
        super(project, androidJar, contextClassLoader);
    }

    @Override
    protected void transformStart(TransformInvocation transformInvocation) throws IOException {
        super.transformStart(transformInvocation);
        modifierTransform=createModifier();
    }

    protected abstract AbsModifierManager createModifier();

    @Override
    protected void transformDo() {
        super.transformDo();
        modifierTransform.modifierAll(classTransforms);
    }
}
