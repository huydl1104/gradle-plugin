package com.example.threadcanary.transform

import com.android.build.api.transform.TransformInvocation
import com.example.threadcanary.inters.TransformInterface
import org.objectweb.asm.*

/**
 * @author yudongliang
 * create time 2021-08-04
 * describe : 替换class 创建
 */
class ReplaceClassNameTransform(val replaceNames: Map<String, String>) :TransformInterface {

    override fun transformBefore(transformInvocation: TransformInvocation) {

    }

    override fun onTransform(transformInvocation: TransformInvocation, bytecode: ByteArray): ByteArray {
        val reader = ClassReader(bytecode)
        val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
        val classAdapter = ReplaceClassAdapter(replaceNames,writer)
        reader.accept(classAdapter,0)
        return writer.toByteArray()
    }

    override fun transformAfter(transformInvocation: TransformInvocation) {

    }

    class ReplaceClassAdapter(private val replaceNames: Map<String, String>, writer: ClassWriter) : ClassVisitor(Opcodes.ASM8,writer){

        var isReplace = false

        /**
         * version：类的版本，次要的版本存储在16位最高的有效位中，主版本在16个最低有效位中。
         * access：类的访问标志，该类收否弃用，或者 记录操作码
         * name：类的内部名称，类似于别名
         * signature：class 的签名，若是为空，就意味者 该类并没有实现 class 或者接口
         * superName：对于父类的内部的名字，只是针对 Object ，若是 interface 的可能为 null
         * interfaces：类接口的内部名称可能为空。
         */
        override fun visit(version: Int, access: Int, name: String?, signature: String?, superName: String?, interfaces: Array<out String>?) {
            // key 为需要修改的 class ，value 替换之前的class
            isReplace = replaceNames.containsValue(name) //name 和 value 相等不需要替换
            println("class  visit $name  , $superName")
            if (replaceNames.containsKey(name) && !isReplace){
                super.visit(version, access, name, signature, replaceNames[name], interfaces)
            }else{
                super.visit(version, access, name, signature, superName, interfaces)
            }
        }

        override fun visitMethod(access: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor {
            var method = super.visitMethod(access, name, descriptor, signature, exceptions)
            if (method != null){
                method = ReplaceMethodAdapter(replaceNames,method)
            }
            return method
        }

    }

    class ReplaceMethodAdapter(private val replaceNames: Map<String, String>, method: MethodVisitor) : MethodVisitor(Opcodes.ASM8){

        /**
         * 访问类型说明。类型指令是采用内部名称的指令
         * opcode：类型指令的操作码，可以是 new，ANEWARRAY, CHECKCAST ， INSTANCEOF.
         * type：访问类型的操作数，这个操作数必须是 object or array class
         */
        override fun visitTypeInsn(opcode: Int, type: String?) {
            println("method  visitTypeInsn $opcode  , $type")
            // replace object create
            if (opcode == Opcodes.NEW && replaceNames.containsKey(type)) {
                super.visitTypeInsn(opcode, replaceNames[type])
            } else {
                super.visitTypeInsn(opcode, type)
            }

        }

        override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, descriptor: String?, isInterface: Boolean) {
            println("visitMethodInsn  $opcode  , $owner  , $name  ,$isInterface  ,$descriptor")
            // replace super call
            if ("<init>" == name && opcode == Opcodes.INVOKESPECIAL && replaceNames.containsKey(owner)) {
                super.visitMethodInsn(opcode, replaceNames[owner], name, descriptor, isInterface)
            } else {
                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
            }
        }

    }
}