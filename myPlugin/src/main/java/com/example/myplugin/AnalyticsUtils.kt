package com.example.myplugin

/**
 * @author yudongliang
 * create time 2021-07-20
 * describe : 打印日志的工具类
 */
object AnalyticsUtils {
    fun log(tips:String){
        if (AnalyticsExtension.isShowDebugGraphicsInfo){
            println(tips)
        }
    }
}