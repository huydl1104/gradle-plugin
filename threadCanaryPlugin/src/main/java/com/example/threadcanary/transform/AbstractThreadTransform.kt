package com.example.threadcanary.transform

import com.android.SdkConstants
import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.example.threadcanary.inters.TransformInterface
import com.google.common.io.Files
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * @author yudongliang
 * create time 2021-08-02
 * describe : 抽象基类 处理业务逻辑
 */
abstract class AbstractThreadTransform :Transform(),TransformInterface {

    override fun getName(): String = "ydl-ThreadTransform"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> = TransformManager.CONTENT_CLASS

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> = TransformManager.SCOPE_FULL_PROJECT

    override fun isIncremental(): Boolean = false

    override fun transform(transformInvocation: TransformInvocation) {
        super.transform(transformInvocation)
        if(!isIncremental){
            transformInvocation.outputProvider.deleteAll()
        }
        transformBefore(transformInvocation)

        transformInvocation.inputs.forEach { input ->
            //jar 包
            input.jarInputs.forEach { jarInput ->
                //拿到 jar file
                val srcJarFile = jarInput.file
                val outputJar = transformInvocation.outputProvider.getContentLocation(jarInput.name,jarInput.contentTypes,jarInput.scopes,Format.JAR)
                val jarInputClassMap = hashMapOf<String,ByteArray>()
                //将jar转换为字节流
                ZipInputStream(FileInputStream(srcJarFile)).use { zipInputStream ->
                    var nextEntry = zipInputStream.nextEntry
                    while (nextEntry != null){
                        if (!nextEntry.isDirectory && nextEntry.name.endsWith(SdkConstants.DOT_CLASS)){
                            val notifyClass = onTransform(transformInvocation,zipInputStream.readBytes())
                            jarInputClassMap[nextEntry.name] = notifyClass
                        }
                        nextEntry = zipInputStream.nextEntry
                    }
                }
                //创建parentFile
                Files.createParentDirs(outputJar)

                ZipOutputStream(FileOutputStream(outputJar)).use { zipOutputStream ->
                    jarInputClassMap.forEach { name, classBytes ->
                        //字符串name 的hashCode 为唯一性 ，可以替换之前的 ZipEntry
                        zipOutputStream.putNextEntry(ZipEntry(name))
                        zipOutputStream.write(classBytes)
                    }
                }

            }
            //文件夹
            input.directoryInputs.forEach { directoryInput ->
                //输入输出文件夹
                val inputDir = directoryInput.file
                val outputDir = transformInvocation.outputProvider.getContentLocation(directoryInput.name,directoryInput.contentTypes,directoryInput.scopes,Format.DIRECTORY)
                //从文件夹中得到所有的文件
                FileUtils.getAllFiles(inputDir).filter { it?.extension?.equals(SdkConstants.EXT_CLASS) ?: false }.toHashSet().forEach { inputFile->
                    //获取class文件的字节码
                    val notifyClassBytes = onTransform(transformInvocation,inputFile.inputStream().readBytes())
                    val outputFile = File(outputDir,FileUtils.relativePossiblyNonExistingPath(inputFile,inputDir))
                    Files.createParentDirs(outputFile)
                    ZipOutputStream(FileOutputStream(outputFile)).use {
                        it.write(notifyClassBytes)
                    }
                }
            }
        }


        transformAfter(transformInvocation)
    }
}