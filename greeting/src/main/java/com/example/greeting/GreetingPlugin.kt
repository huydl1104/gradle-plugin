package com.example.greeting

import org.gradle.api.Plugin
import org.gradle.api.Project

class GreetingPlugin :Plugin<Project> {

    override fun apply(target: Project) {
        val extension = target.extensions.create("greeting",GreetingPluginExtension::class.java)  as GreetingPluginExtension
        target.task("helloGreeting"){
            it.doLast {
                println("Hello from GreetingPlugin -> ${extension.message.get()} ")
            }
        }
    }

}