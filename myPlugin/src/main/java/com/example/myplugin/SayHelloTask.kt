package com.example.myplugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * @author yudongliang
 * create time 2021-07-09
 * describe :
 */
//构造函数必须用@javax.inject.Inject注解标识
class SayHelloTask @javax.inject.Inject constructor (var age:Int) :DefaultTask(){
    @TaskAction
    fun sayHello(){
        println (" age is $age")
    }
}