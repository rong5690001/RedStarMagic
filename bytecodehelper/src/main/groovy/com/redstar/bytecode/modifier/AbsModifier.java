package com.redstar.bytecode.modifier;

import com.redstar.bytecode.ClassTransformer;

import java.util.ArrayList;

/**
 * 修改器 用于定义实际的修改
 */
public abstract class AbsModifier {

  public abstract void modifier(ArrayList<ClassTransformer> classtransformers);

}
