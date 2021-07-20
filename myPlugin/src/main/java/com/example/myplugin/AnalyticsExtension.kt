package com.example.myplugin

/**
 * @author yudongliang
 * create time 2021-07-20
 * describe : 扩展 extension
 */
class AnalyticsExtension {
    companion object{
        //是否需要 展示调试的信息
        var isShowDebugGraphicsInfo = false
    }

    //需要排除的插桩的包名，比如support包一些第三方的代码可以进行排除
    var excludePackageList = arrayListOf<String>()
    //需要定向插桩的包
    var needModifyPackageList = arrayListOf<String>()
}