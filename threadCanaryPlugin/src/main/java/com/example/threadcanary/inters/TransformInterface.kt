package com.example.threadcanary.inters

import com.android.build.api.transform.TransformInvocation

/**
 * @author yudongliang
 * create time 2021-08-02
 * describe : 转换接口的处理类
 */
interface TransformInterface {
    fun transformBefore(transformInvocation: TransformInvocation)
    fun onTransform(transformInvocation: TransformInvocation, bytecode: ByteArray): ByteArray
    fun transformAfter(transformInvocation: TransformInvocation)
}