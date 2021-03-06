package com.example.asm.method;

import com.example.asm.lifecycle.ClassPrintVisitor;
import com.example.asm.utils.LogUtils;
import com.example.asm.utils.AccessCodeUtils;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;

import kotlin.jvm.internal.Intrinsics;

import static org.objectweb.asm.Opcodes.ASM9;

/**
 * @author yudongliang
 * create time 2021-07-22
 * describe : 测试 删除 class 文件中的方法
 */
public class TestDeleteMethod {

    static class DeleteDebugAdapter extends ClassVisitor{

        public ClassWriter classWriter;

        public DeleteDebugAdapter(ClassWriter writer) {
            super(Opcodes.ASM9, writer);
            this.classWriter = writer;
        }

        @Nullable
        public MethodVisitor visitMethod(int access, @Nullable String name, @Nullable String descriptor, @Nullable String signature, @Nullable String[] exceptions) {
            LogUtils.info("visitMethod：access=" + AccessCodeUtils.INSTANCE.accCode2String(access) + ',' + "name=" + name + ',' + "descriptor=" + descriptor + ',' + "signature=" + signature);
            if ("setDebugValue".equals(name)) {
                return null;
            } else {
                ClassWriter var10000 = this.classWriter;
                if (var10000 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("classWriter");
                }
                return var10000.visitMethod(access, name, descriptor, signature, exceptions);
            }
        }

        public void visitSource(@Nullable String source, @Nullable String debug) {
            LogUtils.info("visitSource: source = " + source + ", debug = " + debug);
        }

        public void visit(int version, int access, @Nullable String name, @Nullable String signature, @Nullable String superName, @Nullable String[] interfaces) {
            LogUtils.info("visit: version=" + version + ", access=" + AccessCodeUtils.INSTANCE.accCode2String(access) + ",name=" + name + ",signature=" + signature + ",superName=" + superName);
            ClassWriter var10000 = this.classWriter;
            if (var10000 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("classWriter");
            }

            var10000.visit(version, access, name, signature, superName, interfaces);
        }

        @Nullable
        public FieldVisitor visitField(int access, @Nullable String name, @Nullable String descriptor, @Nullable String signature, @Nullable Object value) {
            LogUtils.info("visitField: access=" + AccessCodeUtils.INSTANCE.accCode2String(access) + ",name=" + name + ",descriptor=" + descriptor + ',' + "signature=" + signature + ",value=" + value);
            ClassWriter var10000 = this.classWriter;
            if (var10000 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("classWriter");
            }

            return var10000.visitField(access, name, descriptor, signature, value);
        }

        public void visitEnd() {
            LogUtils.info("visitEnd");
            ClassWriter var10000 = this.classWriter;
            if (var10000 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("classWriter");
            }
            var10000.visitEnd();
        }

    }

     static class RemoveMethodAdapter extends ClassVisitor {

        private final String name;
        private final String desc;

        public RemoveMethodAdapter(ClassVisitor cv, String name, String desc) {
            super(ASM9, cv);
            this.name = name;
            this.desc = desc;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            // 当匹配时，不委托给下一个 ClassVisitor，这样将移除该方法
            if (name.equals(this.name) && descriptor.equals(this.desc)) {
                return null;
            }
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
    }

    public static void main(String[] args) {
        TestDeleteMethod main = new TestDeleteMethod();
        main.exe1();
    }

    private void exe1(){
        try {
            ClassReader reader = new ClassReader("com/example/asm/testfile/ConvertFile");
            ClassWriter writer = new ClassWriter(reader,ClassWriter.COMPUTE_MAXS);
            RemoveMethodAdapter adapter = new RemoveMethodAdapter(writer, "getDebugMode", "()Z");
            reader.accept(adapter,0);


            String var3 = "==== 删除结果 ======";
            System.out.println(var3);
            ClassPrintVisitor printClassVisitor = new ClassPrintVisitor(Opcodes.ASM9);
            ClassReader printReader = new ClassReader(writer.toByteArray());
            printReader.accept(printClassVisitor, 0);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
