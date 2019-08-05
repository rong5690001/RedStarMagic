package com.redstar.bytecode

import com.android.build.gradle.AppPlugin
import com.redstar.bytecode.transform.DefaultTransform
import javassist.ClassPool
import javassist.LoaderClassPath
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

public class ByteCode implements Plugin<Project> {

    void apply(Project project) {

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        AppPlugin plugin = project.plugins.getPlugin(AppPlugin.class);
        File  sdkDirectory=plugin.extension.sdkDirectory
        String androidJarPath = "platforms/${plugin.extension.compileSdkVersion}/android.jar"
        File androidJar = new File(sdkDirectory, androidJarPath)

        DefaultTransform transform=new DefaultTransform(project,androidJar,contextClassLoader)
        plugin.extension.registerTransform(transform)

        //settingTask(project)先不考虑加入task控制流程了

    }

    ClassPool createClassPool(AppPlugin plugin){
        System.out.println("创建classpool")
        File sdkDirectory = plugin.extension.sdkDirectory
        String androidJarPath = "platforms/${plugin.extension.compileSdkVersion}/android.jar"
        File androidJar = new File(sdkDirectory, androidJarPath)
        ClassLoader contextClassLoader = Thread.currentThread().contextClassLoader
        ClassPool pool=new ClassPool(false)
        pool.appendClassPath(new LoaderClassPath(contextClassLoader))
        pool.appendClassPath(androidJar.getAbsolutePath())
        return pool
    }

    void settingTask(Project project){

        InitConfigurationsExtensions ex=project.extensions.create("bytecodechange", InitConfigurationsExtensions.class)

        System.out.println("ex输出:"+ex.testString)

        project.afterEvaluate {

            InitConfigurationsExtensions initConfigurationsExtensions = project.extensions.findByName("bytecodechange")

            if(initConfigurationsExtensions.enable){

                project.android.applicationVariants.all { variant ->

                    def variantName = variant.name.capitalize()

                    System.out.println("创建task${variantName}")

                    Task bytecodeTask = project.task("btyecode${variantName}", type:TestTask){

                        deftf.test=initConfigurationsExtensions.testString
                        finalizedBy variant.assembleProvider

                    }

                    bytecodeTask.setGroup("bytecode")

                }


            }

        }
    }



}
