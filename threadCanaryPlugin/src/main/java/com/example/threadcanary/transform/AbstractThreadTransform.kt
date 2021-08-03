package com.example.threadcanary.transform

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.example.threadcanary.inters.TransformInterface

/**
 * @author yudongliang
 * create time 2021-08-02
 * describe :
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
            input.jarInputs.forEach { jarInput ->
                //拿到 jar file
                val srcJarFile = jarInput.file
                val outputJar = transformInvocation.outputProvider.getContentLocation(jarInput.name,jarInput.contentTypes,jarInput.scopes,Format.JAR)

            }
        }





        transformAfter(transformInvocation)
    }
}