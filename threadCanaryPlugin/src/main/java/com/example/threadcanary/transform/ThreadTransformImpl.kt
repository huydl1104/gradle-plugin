package com.example.threadcanary.transform

import com.android.build.api.transform.TransformInvocation

/**
 * @author yudongliang
 * create time 2021-08-03
 * describe : 具体处理 transform 的实现
 */
class ThreadTransformImpl :AbstractThreadTransform() {

    private val transformHandlers = setOf(
            ReplaceClassNameTransform(mapOf(
                            "java.util.concurrent.ForkJoinPool" to "com.example.testthread.shadow.ShadowForkJoinPool",
                            "android.os.HandlerThread" to "com.example.testthread.shadow.ShadowHandlerThread",
                            "java.util.concurrent.ScheduledThreadPoolExecutor" to "com.example.testthread.shadow.ShadowScheduledThreadPoolExecutor",
                            "java.util.concurrent.ThreadPoolExecutor" to "com.example.testthread.shadow.ShadowThreadPoolExecutor",
                            "java.lang.Thread" to "com.example.testthread.shadow.ShadowThread",
                            "java.util.Timer" to "com.example.testthread.shadow.ShadowTimer",
                    )
            ),
            ReplaceStaticMethodTransform(
                    mapOf("java.util.concurrent.Executors" to "com.example.testthread.shadow.ShadowExecutors", )
            )
    )

    override fun getName(): String {
        return "y-ThreadTransformImpl"
    }

    override fun transformBefore(transformInvocation: TransformInvocation) {
        transformHandlers.forEach { it.transformBefore(transformInvocation) }
    }

    override fun onTransform(
        transformInvocation: TransformInvocation,
        bytecode: ByteArray
    ): ByteArray {
        var classBytes = bytecode
        transformHandlers.forEach {
            classBytes = it.onTransform(transformInvocation,bytecode)
        }
        return classBytes
    }

    override fun transformAfter(transformInvocation: TransformInvocation) {
        transformHandlers.forEach { it.transformAfter(transformInvocation) }
    }
}