package com.redstar.bytecode.modifiermanager;

import com.redstar.bytecode.modifier.AbsModifier;
import com.redstar.bytecode.ClassTransformer;

import java.util.ArrayList;

public abstract class AbsModifierManager {

    protected ArrayList<AbsModifier> modifiers=new ArrayList();

    public AbsModifierManager(ArrayList<AbsModifier> modifiers) {
        this.modifiers = modifiers;
    }

    public abstract void modifierAll(ArrayList<ClassTransformer> classInputs);

}
