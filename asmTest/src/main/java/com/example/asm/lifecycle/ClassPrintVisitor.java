package com.example.asm.lifecycle;

import com.example.asm.utils.LogUtils;
import com.example.asm.utils.AccessCodeUtils;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ModuleVisitor;
import org.objectweb.asm.TypePath;

/**
 * @author yudongliang
 * create time 2021-07-22
 * describe :
 */
public class ClassPrintVisitor extends ClassVisitor {

    private int apiVersion ;

    public ClassPrintVisitor(int api) {
        this(api,null);
    }

    public ClassPrintVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
        apiVersion = api;
    }


    @Nullable
    public MethodVisitor visitMethod(int access, @Nullable String name, @Nullable String descriptor, @Nullable String signature, @Nullable String[] exceptions) {
        LogUtils.info("visitMethod：access=" + AccessCodeUtils.INSTANCE.accCode2String(access) + ',' + "name=" + name + ',' + "descriptor=" + descriptor + ',' + "signature=" + signature);
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        return new MethodPrint(mv);
    }

    @Nullable
    public ModuleVisitor visitModule(@Nullable String name, int access, @Nullable String version) {
        LogUtils.info("visitModule:name=" + name + ",access=" + AccessCodeUtils.INSTANCE.accCode2String(access) + ",version=" + version);
        return (ModuleVisitor)(new MyModuleVisitor(this.apiVersion));
    }

    public void visitNestHost(@Nullable String nestHost) {
        LogUtils.info("visitNestHost:nestHost=" + nestHost);
        super.visitNestHost(nestHost);
    }

    public void visitInnerClass(@Nullable String name, @Nullable String outerName, @Nullable String innerName, int access) {
        LogUtils.info("visitInnerClass:name=" + name + ",outerName=" + outerName + ",innerName:" + innerName + ",access=" + AccessCodeUtils.INSTANCE.accCode2String(access));
        super.visitInnerClass(name, outerName, innerName, access);
    }

    public void visitSource(@Nullable String source, @Nullable String debug) {
        LogUtils.info("visitSource:source=" + source + ",debug=" + debug);
        super.visitSource(source, debug);
    }

    public void visitOuterClass(@Nullable String owner, @Nullable String name, @Nullable String descriptor) {
        LogUtils.info("visitOuterClass:owner:" + owner + ",name=" + name + ",descriptor=" + descriptor);
        super.visitOuterClass(owner, name, descriptor);
    }

    public void visit(int version, int access, @Nullable String name, @Nullable String signature, @Nullable String superName, @Nullable String[] interfaces) {
        LogUtils.info("visit:version=" + version + ", access=" + AccessCodeUtils.INSTANCE.accCode2String(access) + ",name=" + name + ",signature=" + signature + ",superName=" + superName);
        super.visit(version, access, name, signature, superName, interfaces);
    }

    public void visitNestMember(@Nullable String nestMember) {
        LogUtils.info("visitNestMember:nestMember=" + nestMember);
        super.visitNestMember(nestMember);
    }

    @Nullable
    public FieldVisitor visitField(int access, @Nullable String name, @Nullable String descriptor, @Nullable String signature, @Nullable Object value) {
        LogUtils.info("visitField:access=" + AccessCodeUtils.INSTANCE.accCode2String(access) + ",name=" + name + ",descriptor=" + descriptor + ',' + "signature=" + signature + ",value=" + value);
        return super.visitField(access, name, descriptor, signature, value);
    }

    public void visitEnd() {
        LogUtils.info("visitEnd");
        super.visitEnd();
    }

    @Nullable
    public AnnotationVisitor visitAnnotation(@Nullable String descriptor, boolean visible) {
        LogUtils.info("visitAnnotation:descriptor=" + descriptor + ",visible=" + visible);
        return super.visitAnnotation(descriptor, visible);
    }

    @Nullable
    public AnnotationVisitor visitTypeAnnotation(int typeRef, @Nullable TypePath typePath, @Nullable String descriptor, boolean visible) {
        LogUtils.info("visitTypeAnnotation:typeRef=" + typeRef + ",typePath=" + typePath + ",descriptor=" + descriptor + ",visible=" + visible);
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    public void visitAttribute(@Nullable Attribute attribute) {
        LogUtils.info("visitAttribute");
        super.visitAttribute(attribute);
    }


}
