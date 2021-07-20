package com.example.myplugin

import android.databinding.tool.ext.fieldSpec
import com.android.SdkConstants
import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.gradle.api.Project
import java.io.File
import java.util.HashMap

/**
 * @author yudongliang
 * create time 2021-07-20
 * describe : Google 在gradle 1.5.0后提供了 Transform API，
 *  允许三方的 plugin 在打包 dex 之前，生成class 文件之后进行操作，
 *  注解是在 java 到 class 文件之间进行操作的
 */
class AnalyticsTransform(private val target: Project, private val extension: AnalyticsExtension) :Transform() {

    /**
     * 该 Transform 对应的task 名称，编译后会出现在 build/intermediates/transform 下生成对应的文件夹
     */
    override fun getName(): String = "Analytics_plugin"

    /**
     * 需要处理的数据类型
     */
    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> = TransformManager.CONTENT_CLASS

    /**
     * 指 Transform 要操作内容的范围，官方文档 Scope 有 7 种类型：
     * 1. EXTERNAL_LIBRARIES：只有外部库
     * 2. PROJECT：只有项目内容
     * 3. PROJECT_LOCAL_DEPS：只有项目的本地依赖(本地jar)
     * 4. PROVIDED_ONLY：只提供本地或远程依赖项
     * 5. SUB_PROJECTS：只有子项目。
     * 6. SUB_PROJECTS_LOCAL_DEPS：只有子项目的本地依赖项(本地jar)。
     * 7. TESTED_CODE：由当前变量(包括依赖项)测试的代码
     */
    override fun getScopes(): MutableSet<in QualifiedContent.Scope> = TransformManager.SCOPE_FULL_PROJECT

    /**
     * 是否支持增量更新
     */
    override fun isIncremental(): Boolean = false

    /**
     * 执行具体的操作
     */
    override fun transform(transformInvocation: TransformInvocation) {
        super.transform(transformInvocation)
        printMsg()
        val outputProvider = transformInvocation.outputProvider
        //先晴空输出文件
        if(!isIncremental){
            outputProvider.deleteAll()
        }
        transformInvocation.inputs.forEach {

            it.directoryInputs.forEach{ directory ->
                /**
                 * 输出的jar包File。  getContentLocation参数：
                 * name：内容File的名称必须是唯一的
                 * types：与内容相关联的类型
                 * scopes：内容的作用范围
                 * format：内容的格式
                 */
                val dest = outputProvider.getContentLocation(directory.name,directory.contentTypes,directory.scopes,Format.DIRECTORY)
                val inputDir = directory.file
                if (inputDir.isDirectory){
                    val notifyMap = HashMap<String, File>()
                    FileUtils.getAllFiles(inputDir).filter { file ->
                        println("dirName -->  ${file?.extension}  ")
                        file?.extension.equals(SdkConstants.EXT_CLASS)
                    }.toHashSet().forEach { classFile ->
                        if (AnalyticsClassModifier.isShouldNotify(classFile.name,extension)){
                            val notifyFile =  AnalyticsClassModifier.notifyClassFile(inputDir,classFile,transformInvocation.context.temporaryDir)
                            val key = classFile.absolutePath.replace(inputDir.absolutePath, "")
                            AnalyticsUtils.log("classFile ${classFile.absolutePath}  , inputDir ->${inputDir.absolutePath}  ,key ->$key")
                            notifyMap[key] = notifyFile
                        }
                    }
                    FileUtils.copyDirectory(directory.file,dest)
                    //将 notifyMap
                    notifyMap.entries.forEach { entry ->
                        val file = File(dest.absolutePath+entry.key)
                        if (file.exists()){
                            file.delete()
                        }
                        FileUtils.copyFile(entry.value, file)
                        entry.value.delete()
                    }
                }

            }
            it.jarInputs.forEach{ jar ->


            }
        }



    }










    private fun printMsg(){
        println()
        println("####################################################################")
        println("########                                                    ########")
        println("########                                                    ########")
        println("########                 transform 编译插件                  ########")
        println("########                   by ydl                           ########")
        println("########                                                    ########")
        println("########                                                    ########")
        println("####################################################################")
        println()
    }

}


