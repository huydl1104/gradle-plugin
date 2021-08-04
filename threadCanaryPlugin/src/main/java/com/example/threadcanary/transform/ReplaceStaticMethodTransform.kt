package com.example.threadcanary.transform

import com.android.build.api.transform.TransformInvocation
import com.example.threadcanary.inters.TransformInterface
import org.objectweb.asm.*

/**
 * @author yudongliang
 * create time 2021-08-04
 * describe :
 */
class ReplaceStaticMethodTransform(private val replaceNames: Map<String, String>) :TransformInterface {
    override fun transformBefore(transformInvocation: TransformInvocation) {

    }

    override fun onTransform(transformInvocation: TransformInvocation, bytecode: ByteArray): ByteArray {
        val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
        val cr = ClassReader(bytecode)
        val cv = ReplaceStaticMethodAdapter(replaceNames, cw)
        cr.accept(cv, 0)
        return cw.toByteArray()
    }

    override fun transformAfter(transformInvocation: TransformInvocation) {

    }

    class ReplaceStaticMethodAdapter(private val replaceNames: Map<String, String>, cv: ClassVisitor) : ClassVisitor(Opcodes.ASM9, cv) {
        private var isReplaceClass = false

        override fun visit(version: Int, access: Int, name: String?, signature: String?, superName: String?, interfaces: Array<out String>?) {
            isReplaceClass = replaceNames.containsValue(name)
            super.visit(version, access, name, signature, superName, interfaces)
        }

        override fun visitMethod(access: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor {
            val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
            if (mv != null && !isReplaceClass) {
                return ReplaceStaticMethodMethodAdapter(replaceNames, mv)
            }
            return mv
        }

        class ReplaceStaticMethodMethodAdapter(private val classesName: Map<String, String>, mv: MethodVisitor) : MethodVisitor(Opcodes.ASM9, mv) {
            override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, descriptor: String?, isInterface: Boolean) {
                if (opcode == Opcodes.INVOKESTATIC && classesName.containsKey(owner)) {
                    super.visitMethodInsn(opcode, classesName[owner], name, descriptor, isInterface)
                } else {
                    super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
                }
            }
        }
    }
}