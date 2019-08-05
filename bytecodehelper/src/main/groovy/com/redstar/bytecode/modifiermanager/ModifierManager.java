package com.redstar.bytecode.modifiermanager;

import com.redstar.bytecode.modifier.AbsModifier;
import com.redstar.bytecode.ClassTransformer;

import java.util.ArrayList;

public class ModifierManager extends AbsModifierManager {

    public ModifierManager(ArrayList<AbsModifier> modifiers) {
        super(modifiers);
    }

    @Override
    public void modifierAll(ArrayList<ClassTransformer> classtransformers) {

        for (int i = 0; i < modifiers.size(); i++) {

            modifiers.get(i).modifier(classtransformers);

        }

    }

}
