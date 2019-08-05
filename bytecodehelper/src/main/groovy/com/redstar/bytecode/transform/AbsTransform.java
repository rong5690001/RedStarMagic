package com.redstar.bytecode.transform;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.gradle.internal.pipeline.TransformManager;
import org.gradle.api.Project;
import java.io.IOException;
import java.util.Set;

import javassist.CannotCompileException;
import javassist.NotFoundException;

public abstract class AbsTransform extends Transform {

    protected Project project;

    public AbsTransform(Project project) {

        this.project = project;

    }

    @Override
    public String getName() {
        return "changeBtyeCodeTask";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);

        transformStart(transformInvocation);
        transformOnInput(transformInvocation);
        transformDo();
        try {

                transformOnOutput(transformInvocation);

        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }catch (CannotCompileException e){
            e.printStackTrace();
            throw  new IOException(e.getMessage());
        }
        transformEnd(transformInvocation);

    }

    protected abstract void transformStart(TransformInvocation transformInvocation)throws IOException;

    protected abstract void transformOnInput(TransformInvocation transformInvocation) throws IOException;

    protected abstract void transformDo();

    protected abstract void transformOnOutput(TransformInvocation transformInvocation) throws IOException, NotFoundException, CannotCompileException;

    protected abstract void transformEnd(TransformInvocation transformInvocation);

}
