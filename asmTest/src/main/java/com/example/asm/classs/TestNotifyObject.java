package com.example.asm.classs;

import com.example.asm.ASMManager;
import com.example.asm.testfile.TestObject;
import com.example.asm.utils.ClassOutputUtil;
import com.example.asm.utils.LogUtils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.ASM8;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;

/**
 * @author yudongliang
 * create time 2021-07-26
 * describe : 测试更换对象
 */
public class TestNotifyObject {

    private static final HashMap<String,String> classNames = new HashMap<>();

    static {
        classNames.put("java/lang/Thread", "com/example/asm/temp/ShadowThread");
    }


    static class ModifyObjectCreateAdapter extends ClassVisitor {
        // key: old class name, value: new class name
        private final Map<String, String> classesName;
        private boolean isReplaceClass;

        public ModifyObjectCreateAdapter(ClassVisitor classVisitor, Map<String, String> classesName) {
            super(ASM8, classVisitor);
            this.classesName = classesName;
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces);
            isReplaceClass = classesName.containsValue(name);
            LogUtils.info("visit name ="+name +" ,signature ="+signature +",isReplaceClass="+isReplaceClass);
        }

        @Override
        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
            LogUtils.info("visitField name ="+name +" ,descriptor ="+descriptor);
            return super.visitField(access, name, descriptor, signature, value);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            LogUtils.info("visitMethod name ="+name +" ,descriptor ="+descriptor);
            if (mv != null && !isReplaceClass) {
                mv = new ModifyObjectCreateMethodAdapter(mv, classesName);
            }
            return mv;
        }

        static class ModifyObjectCreateMethodAdapter extends MethodVisitor {
            // key: old class name, value: new class name
            private final Map<String, String> classesName;

            public ModifyObjectCreateMethodAdapter(MethodVisitor methodVisitor, Map<String, String> classesName) {
                super(ASM8, methodVisitor);
                this.classesName = classesName;
            }

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                LogUtils.info("visitMethodInsn opcode ="+opcode +" ,owner ="+owner+" ,name ="+name+" ,descriptor ="+descriptor);
                if (name.equals("<init>") && opcode == INVOKESPECIAL && classesName.containsKey(owner)) {
                    super.visitMethodInsn(opcode, classesName.get(owner), name, descriptor, isInterface);
                } else {
                    super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                }
            }

            @Override
            public void visitTypeInsn(int opcode, String type) {
                LogUtils.info("visitTypeInsn opcode ="+opcode +" ,type ="+type);
                if (opcode == NEW && classesName.containsKey(type)) {
                    super.visitTypeInsn(opcode, classesName.get(type));
                } else {
                    super.visitTypeInsn(opcode, type);
                }
            }

            @Override
            public void visitEnd() {
                super.visitEnd();
                LogUtils.info("visitEnd  ==== ");
            }
        }
    }

    public static void main(String[] args) {
        TestNotifyObject main = new TestNotifyObject();
        main.exe1();
    }

    private void exe1() {
        try {
            ClassReader cr = new ClassReader(Type.getInternalName(TestObject.class));
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClassVisitor cv = new ModifyObjectCreateAdapter(cw, classNames);
            cr.accept(cv, 0);

            ClassOutputUtil.byte2File("asmTest/build/asm/TestObject.class", cw.toByteArray());

/*            Object instance = ASMManager.loadInstance(cw, TestObject.class);
            instance.getClass().getDeclaredMethod("getF3").invoke(instance);
            instance.getClass().getDeclaredMethod("getF4").invoke(instance);*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
