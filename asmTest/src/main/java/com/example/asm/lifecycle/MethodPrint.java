package com.example.asm.lifecycle;

import com.example.asm.utils.LogUtils;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.Arrays;

import static org.objectweb.asm.Opcodes.ASM9;

public class MethodPrint extends MethodVisitor {
    public MethodPrint(MethodVisitor mv) {
        super(ASM9, mv);
    }

    @Override
    public void visitParameter(String name, int access) {
        super.visitParameter(name, access);
        LogUtils.info("visitParameter name: " + name + ", access: " + access);
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        LogUtils.info("visitAnnotationDefault");
        return super.visitAnnotationDefault();
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        LogUtils.info("visitAnnotation descriptor: " + descriptor + ", visible: " + visible);
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        LogUtils.info("visitParameterAnnotation parameter: " + parameter + ", descriptor: " + descriptor + ", visible: " + visible);
        return super.visitParameterAnnotation(parameter, descriptor, visible);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        super.visitAttribute(attribute);
        LogUtils.info("visitAttribute attribute: " + attribute);
    }

    @Override
    public void visitCode() {
        super.visitCode();
        LogUtils.info("visitCode");
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        super.visitTryCatchBlock(start, end, handler, type);
        LogUtils.info("visitTryCatchBlock start: " + start + ", end: " + end + ", handler: " + handler + ", type: " + type);
    }

    @Override
    public void visitLabel(Label label) {
        super.visitLabel(label);
        LogUtils.info("visitLabel label: " + label);
    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        super.visitFrame(type, numLocal, local, numStack, stack);
        LogUtils.info("visitFrame type: " + type + ", numLocal: " + numLocal + ", local: " + Arrays.toString(local)
                + ", numStack: " + numStack + ", stack: " + Arrays.toString(stack));
    }

    @Override
    public void visitInsn(int opcode) {
        super.visitInsn(opcode);
        LogUtils.info("visitInsn opcode: " + opcode);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        super.visitTypeInsn(opcode, type);
        LogUtils.info("visitTypeInsn opcode: " + opcode + ", type: " + type);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        super.visitFieldInsn(opcode, owner, name, descriptor);
        LogUtils.info("visitFieldInsn opcode: " + opcode + ", owner: " + owner + ", name: " + name + ", descriptor: " + descriptor);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        super.visitIntInsn(opcode, operand);
        LogUtils.info("visitIntInsn opcode: " + opcode + ", operand: " + operand);
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        super.visitIincInsn(var, increment);
        LogUtils.info("visitIincInsn var: " + var + ", increment: " + increment);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        super.visitJumpInsn(opcode, label);
        LogUtils.info("visitJumpInsn opcode: " + opcode + ", label: " + label);
    }

    @Override
    public void visitLdcInsn(Object value) {
        super.visitLdcInsn(value);
        LogUtils.info("visitLdcInsn value: " + value);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        LogUtils.info("visitMethodInsn opcode: " + opcode + ", owner: " + owner + ", name: " + name
                + ", descriptor: " + descriptor + ", isInterface: " + isInterface);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        super.visitVarInsn(opcode, var);
        LogUtils.info("visitVarInsn opcode: " + opcode + ", var: " + var);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
        LogUtils.info("visitInvokeDynamicInsn name: " + name + ", descriptor: " + descriptor
                + ", bootstrapMethodHandle: " + bootstrapMethodHandle
                + ", bootstrapMethodArguments: " + Arrays.toString(bootstrapMethodArguments));
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        super.visitLookupSwitchInsn(dflt, keys, labels);
        LogUtils.info("visitLookupSwitchInsn dflt: " + dflt
                + ", keys: " + Arrays.toString(keys)
                + ", labels: " + Arrays.toString(labels));
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        super.visitTableSwitchInsn(min, max, dflt, labels);
        LogUtils.info("visitTableSwitchInsn min: " + min + ", max: " + max + ", dflt: " + dflt
                + ", labels: " + Arrays.toString(labels));
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        super.visitMultiANewArrayInsn(descriptor, numDimensions);
        LogUtils.info("visitMultiANewArrayInsn descriptor: " + descriptor + ", numDimensions: " + numDimensions);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
        LogUtils.info("visitLocalVariable name: " + name + ", descriptor: " + descriptor
                + ", signature: " + signature + ", start: " + start + ", end: " + end + ", index: " + index);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals);
        LogUtils.info("visitMaxs maxStack: " + maxStack + ", maxLocals: " + maxLocals);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        LogUtils.info("visitEnd");
    }
}
