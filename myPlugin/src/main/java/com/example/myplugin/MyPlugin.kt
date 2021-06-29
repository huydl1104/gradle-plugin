package com.example.myplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class MyPlugin : Plugin<Project>{
    override fun apply(p0: Project) {
        println("MyPlugin ------- ")
    }

}