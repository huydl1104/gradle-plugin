package com.example.greeting

import org.gradle.api.provider.Property

/**
 * @author yudongliang
 * create time 2021-07-05
 * describe :
 */
abstract class GreetingPluginExtension {

    abstract val message: Property<String>

    init {
        this.message.convention("<- init -> ")
    }

}