package com.example.asm.annotation;


import com.example.asm.utils.ClassOutputUtil;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_ANNOTATION;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.V1_8;


/**
 * @author yudongliang
 * create time 2021-07-30
 * describe : 解析字节码生成 注解类
 */
public class TestGenerateAnnotation {
    public static void main(String[] args) {
        TestGenerateAnnotation main = new TestGenerateAnnotation();
        main.exe1();
    }

    private void exe1() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        cw.visit(V1_8, ACC_PUBLIC | ACC_ANNOTATION | ACC_ABSTRACT | ACC_INTERFACE, "java/lang/Deprecated2"
                , null, "java/lang/Object", new String[] { "java/lang/annotation/Annotation" });

        cw.visitSource("Deprecated.java", null);

        AnnotationVisitor av1 = cw.visitAnnotation("Ljava/lang/annotation/Retention;", true);
        av1.visitEnum("value", "Ljava/lang/annotation/RetentionPolicy;", "RUNTIME");
        av1.visitEnd();

        AnnotationVisitor av2 = cw.visitAnnotation("Ljava/lang/annotation/Target;", true);
        AnnotationVisitor annotationVisitor1 = av2.visitArray("value");
        annotationVisitor1.visitEnum(null, "Ljava/lang/annotation/ElementType;", "CONSTRUCTOR");
        annotationVisitor1.visitEnum(null, "Ljava/lang/annotation/ElementType;", "FIELD");
        annotationVisitor1.visitEnum(null, "Ljava/lang/annotation/ElementType;", "LOCAL_VARIABLE");
        annotationVisitor1.visitEnum(null, "Ljava/lang/annotation/ElementType;", "METHOD");
        annotationVisitor1.visitEnum(null, "Ljava/lang/annotation/ElementType;", "PACKAGE");
        annotationVisitor1.visitEnum(null, "Ljava/lang/annotation/ElementType;", "MODULE");
        annotationVisitor1.visitEnum(null, "Ljava/lang/annotation/ElementType;", "PARAMETER");
        annotationVisitor1.visitEnum(null, "Ljava/lang/annotation/ElementType;", "TYPE");
        annotationVisitor1.visitEnd();
        ClassOutputUtil.byte2File("asmTest/build/asm/GenerateAnnotation.class", cw.toByteArray());
    }
}
