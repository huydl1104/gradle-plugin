package com.example.threadcanary.transform

import com.android.build.api.transform.TransformInvocation
import com.example.threadcanary.inters.TransformInterface

/**
 * @author yudongliang
 * create time 2021-08-04
 * describe :
 */
class ReplaceClassNameTransform(mapOf: Map<String, String>) :TransformInterface {

    override fun transformBefore(transformInvocation: TransformInvocation) {

    }

    override fun onTransform(transformInvocation: TransformInvocation, bytecode: ByteArray): ByteArray {

    }

    override fun transformAfter(transformInvocation: TransformInvocation) {

    }
}