package com.example.asm.annotation;


import com.example.asm.utils.ClassOutputUtil;
import com.example.asm.utils.LogUtils;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;


/**
 * @author yudongliang
 * create time 2021-07-24
 * describe : 测试添加注解
 */
public class TestAddAnnotation {

    static class AddAnnotationAdapterVisitor extends ClassVisitor implements Opcodes {
        private boolean isAddAnnotation;
        public AddAnnotationAdapterVisitor(ClassVisitor classVisitor) {
            super(ASM8, classVisitor);
        }

        @Override
        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
            LogUtils.info("visitField name ="+name+" , descriptor ="+descriptor);
            addAnnotation();
            return super.visitField(access, name, descriptor, signature, value);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            LogUtils.info("visitMethod name ="+name+" , descriptor ="+descriptor);
            addAnnotation();
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            LogUtils.info("visitMethod visible ="+visible+" , descriptor ="+descriptor);
            if (visible && (descriptor.equals("Lcom/example/asm/annotation/MyNewAnnotation;"))){
                isAddAnnotation = true;
            }
            return super.visitAnnotation(descriptor, visible);
        }

        private void addAnnotation(){
            if (!isAddAnnotation){
                AnnotationVisitor annotationVisitor = cv.visitAnnotation("Lcom/example/asm/annotation/MyNewAnnotation;", true);
                annotationVisitor.visitEnum("name","Ljava/lang/String;","aaa");
                annotationVisitor.visitEnd();
                isAddAnnotation = true;
            }
        }
    }

    public static void main(String[] args) {
        TestAddAnnotation main = new TestAddAnnotation();
        main.exe1();
    }

    private void exe1() {
        try {
            ClassReader reader = new ClassReader("com/example/asm/testfile/TestAddAnnotation");
            ClassWriter writer = new ClassWriter(reader,ClassWriter.COMPUTE_MAXS);
            AddAnnotationAdapterVisitor visitor = new AddAnnotationAdapterVisitor(writer);
            reader.accept(visitor,0);


            ClassOutputUtil.byte2File("asmTest/build/asm/AddAnnotation.class", writer.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
