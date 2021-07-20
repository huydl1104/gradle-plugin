package com.example.myplugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.*

class AnalyticsPlugin : Plugin<Project>{
    override fun apply(target: Project) {
        AnalyticsUtils.log("MyPlugin ------- ")
        val extension = target.extensions.create(
            "myTestPluginParam",
            AnalyticsExtension::class.java
        )

        //解析根目录的文件  grade.properties
        var properties = Properties()
        var usePlugin =false
        val file = target.rootProject.file("grade.properties")
        if (file.exists()){
            properties.load(file.inputStream())
            usePlugin = properties.getProperty("usePlugin").toBoolean()
        }
        AnalyticsUtils.log("AnalyticsPlugin usePlugin $usePlugin")
        if(usePlugin){
            AnalyticsUtils.log("------------您开启了全埋点插桩插件--------------")
            val appExtension = target.extensions.findByType(AppExtension::class.java)
            //注册 transform 类
            appExtension?.registerTransform(AnalyticsTransform(target,extension))
        }else{
            AnalyticsUtils.log("------------您关闭了全埋点插桩插件--------------")
        }

    }

}