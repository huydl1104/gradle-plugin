package com.example.threadcanary.transform

import com.android.build.api.transform.TransformInvocation

/**
 * @author yudongliang
 * create time 2021-08-03
 * describe : 具体处理 transform 的实现
 */
class ThreadTransformImpl :AbstractThreadTransform() {

    private val transformHandlers = setOf(
            ReplaceClassNameTransform(
                    mapOf(
                            "java.util.concurrent.ForkJoinPool" to "com.airsaid.threadcanary.shadow.ShadowForkJoinPool",
                            "android.os.HandlerThread" to "com.airsaid.threadcanary.shadow.ShadowHandlerThread",
                            "java.util.concurrent.ScheduledThreadPoolExecutor" to "com.airsaid.threadcanary.shadow.ShadowScheduledThreadPoolExecutor",
                            "java.util.concurrent.ThreadPoolExecutor" to "com.airsaid.threadcanary.shadow.ShadowThreadPoolExecutor",
                            "java.lang.Thread" to "com.airsaid.threadcanary.shadow.ShadowThread",
                            "java.util.Timer" to "com.airsaid.threadcanary.shadow.ShadowTimer",
                    )
            ),
            ReplaceStaticMethodTransform(
                    mapOf(
                            "java.util.concurrent.Executors" to "com.airsaid.threadcanary.shadow.ShadowExecutors",
                    )
            )
    )

    override fun transformBefore(transformInvocation: TransformInvocation) {

    }

    override fun onTransform(
        transformInvocation: TransformInvocation,
        bytecode: ByteArray
    ): ByteArray {

    }

    override fun transformAfter(transformInvocation: TransformInvocation) {

    }
}