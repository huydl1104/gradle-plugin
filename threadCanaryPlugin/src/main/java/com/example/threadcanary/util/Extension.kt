package com.example.threadcanary.util

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.GradleException
import org.gradle.api.Project

/**
 * @author yudongliang
 * create time 2021-08-02
 * describe : 扩展函数
 */
fun Project.getCustomExtension():BaseExtension{
    //寻找指定的 extension
    val appExtension = this.extensions.findByType(AppExtension::class.java)
    if (appExtension != null){
        return appExtension
    }
    val libraryExtension = this.extensions.findByType(LibraryExtension::class.java)
    if (libraryExtension != null){
        return libraryExtension
    }
    throw GradleException("$displayName is not a android Project ... ")
}