package com.redstar.bytecode;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class TestTask extends DefaultTask {

    @TaskAction
    public void test(){
        System.out.println("Task执行了");
    }

}
