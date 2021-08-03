package com.example.threadcanary.transform

import com.android.build.api.transform.TransformInvocation

/**
 * @author yudongliang
 * create time 2021-08-03
 * describe :
 */
class ThreadTransformImpl :AbstractThreadTransform() {
    override fun transformBefore(transformInvocation: TransformInvocation) {

    }

    override fun onTransform(
        transformInvocation: TransformInvocation,
        bytecode: ByteArray
    ): ByteArray {
        TODO("Not yet implemented")
    }

    override fun transformAfter(transformInvocation: TransformInvocation) {
        TODO("Not yet implemented")
    }
}