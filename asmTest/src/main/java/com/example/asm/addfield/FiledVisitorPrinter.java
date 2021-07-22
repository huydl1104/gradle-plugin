package com.example.asm.addfield;

import com.example.asm.utils.ADLog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

import kotlin.jvm.internal.Intrinsics;

/**
 * @author yudongliang
 * create time 2021-07-22
 * describe : FieldVisitor
 */
public class FiledVisitorPrinter extends FieldVisitor {


    public FiledVisitorPrinter(FieldVisitor fieldVisitor) {
        super(Opcodes.ASM8, fieldVisitor);
    }

    public void visitEnd() {
        super.visitEnd();
        ADLog.INSTANCE.info("visitEnd");

    }

    @NotNull
    public AnnotationVisitor visitAnnotation(@Nullable String descriptor, boolean visible) {
        ADLog.INSTANCE.info("visitAnnotation, des = " + descriptor + ", visiable = " + visible);
        AnnotationVisitor var10000 = super.visitAnnotation(descriptor, visible);
        Intrinsics.checkExpressionValueIsNotNull(var10000, "super.visitAnnotation(descriptor, visible)");
        return var10000;
    }

    @NotNull
    public AnnotationVisitor visitTypeAnnotation(int typeRef, @Nullable TypePath typePath, @Nullable String descriptor, boolean visible) {
        AnnotationVisitor var10000 = super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
        Intrinsics.checkExpressionValueIsNotNull(var10000, "super.visitTypeAnnotatio…ath, descriptor, visible)");
        return var10000;
    }

    public void visitAttribute(@Nullable Attribute attribute) {
        super.visitAttribute(attribute);
    }

}
