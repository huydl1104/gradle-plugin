package com.example.threadcanary

import com.example.threadcanary.transform.ThreadTransformImpl
import com.example.threadcanary.util.getCustomExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class ThreadCanaryPlugin :Plugin<Project>{
    override fun apply(target: Project) {
        target.getCustomExtension().registerTransform(ThreadTransformImpl())
    }
}