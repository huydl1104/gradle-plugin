package com.example.myplugin

import java.io.File

/**
 * @author yudongliang
 * create time 2021-07-20
 * describe : 对class文件进行修改
 */
object AnalyticsClassModifier {

    /**
     * 是否需要修改（先简单过滤一些不需要操作的文件，提高编译速度，可以根据实际情况添加更多过滤）
     */
    fun isShouldNotify(className: String, extension: AnalyticsExtension):Boolean {
        AnalyticsUtils.log("AnalyticsClassModifier className ->$className ")
        if (className.contains("R$") ||
                className.contains("R2$") ||//R2.class及其子类（butterknife）
                className.contains("R.class") ||
                className.contains("R2.class") ||
                className.contains("BuildConfig.class")) {
            AnalyticsUtils.log("全埋点R/Build过滤 >>>  $className")
            return false
        }
        //需要插桩的class
        if (extension.needModifyPackageList.size > 0){
            val iterator = extension.needModifyPackageList.iterator()
            while(iterator.hasNext()){
                val next = iterator.next()
                if (className.startsWith(next)){
                    AnalyticsUtils.log("需要埋点的包名>>>packageName:$next")
                    AnalyticsUtils.log("需要插桩的类>>>$className")
                    return true
                }
            }
        }
        //拦截不需要插桩的class
        if (extension.excludePackageList.size > 0){
            val iterator = extension.excludePackageList.iterator()
            while(iterator.hasNext()){
                val next = iterator.next()
                if (className.startsWith(next)){
                    AnalyticsUtils.log("需要埋点的包名>>>packageName:$next")
                    AnalyticsUtils.log("需要插桩的类>>>$className")
                    return false
                }
            }
        }
        AnalyticsUtils.log("need Modify：>>>" + className+" 测试"+ File.separator)
        return true
    }
    fun notifyClassFile(inputDir: File, inputFile: File, temporaryDir: File):File? {
        return null
    }

}