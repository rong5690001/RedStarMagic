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

        settingTask(project)

    }

    void settingTask(Project project){

        InitConfigurationsExtensions ex=project.extensions.create("easyplugin", InitConfigurationsExtensions.class)

        project.afterEvaluate {

            InitConfigurationsExtensions initConfigurationsExtensions = project.extensions.findByName("easyplugin")

            project.android.applicationVariants.all { variant ->

                def variantName = variant.name.capitalize()

                System.out.println("创建task${variantName}")

                PackagesTask bytecodeTask = project.task("produce${variantName}", type:PackagesTask){
                    mVariant = variant
                    dependsOn variant.assembleProvider
                }

                bytecodeTask.doFirst {
                    pluginVersion=initConfigurationsExtensions.pluginVersion
                    pluginApkVersion=initConfigurationsExtensions.pluginApkVersion
                }

                bytecodeTask.doLast {

                }

                bytecodeTask.setGroup("easyplugin")

            }

        }
    }



}
