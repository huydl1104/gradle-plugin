package com.example.threadcanary.transform

import com.android.build.api.transform.TransformInvocation
import com.example.threadcanary.inters.TransformInterface

/**
 * @author yudongliang
 * create time 2021-08-04
 * describe :
 */
class ReplaceStaticMethodTransform(mapOf: Map<String, String>) :TransformInterface {
    override fun transformBefore(transformInvocation: TransformInvocation) {
        TODO("Not yet implemented")
    }

    override fun onTransform(transformInvocation: TransformInvocation, bytecode: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }

    override fun transformAfter(transformInvocation: TransformInvocation) {
        TODO("Not yet implemented")
    }
}