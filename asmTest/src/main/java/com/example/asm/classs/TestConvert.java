package com.example.asm.classs;

import com.example.asm.utils.LogUtils;
import com.example.asm.utils.AccessCodeUtils;
import com.example.asm.utils.ClassOutputUtil;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;

import kotlin.jvm.internal.Intrinsics;

/**
 * @author yudongliang
 * create time 2021-07-22
 * describe : 转换测试
 */
public class TestConvert {

    static class ChangeVersionAdapter extends ClassVisitor {

        public ClassWriter classWriter;
        public long startTime;
        public ChangeVersionAdapter(ClassWriter writer) {
            super(Opcodes.ASM9, writer);
            this.classWriter = writer;
        }

        @Nullable
        public MethodVisitor visitMethod(int access, @Nullable String name, @Nullable String descriptor, @Nullable String signature, @Nullable String[] exceptions) {
            LogUtils.info("[visitMethod],access = " + AccessCodeUtils.INSTANCE.accCode2String(access) + ',' + "name = " + name + ",descriptor=" + descriptor + ",signature=" + signature);
            ClassWriter var10000 = this.classWriter;
            if (var10000 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("classWriter");
            }

            return var10000.visitMethod(access, name, descriptor, signature, exceptions);
        }

        public void visit(int version, int access, @Nullable String name, @Nullable String signature, @Nullable String superName, @Nullable String[] interfaces) {
            this.startTime = System.nanoTime();
            LogUtils.info("[visit],version = " + version + ",access=" + AccessCodeUtils.INSTANCE.accCode2String(access) + ",name=" + name);
            ClassWriter var10000 = this.classWriter;
            if (var10000 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("classWriter");
            }

            var10000.visit(52, access, name, signature, superName, interfaces);
        }

        public void visitEnd() {
            super.visitEnd();
            LogUtils.info("[visitEnd], time = " + (System.nanoTime() - this.startTime));
        }
    }

    public static void main(String[] args) {
        TestConvert main = new TestConvert();
        main.exe1();
    }

    private void exe1(){
        try {
            ClassReader reader = new ClassReader("com/example/asm/testfile/ConvertFile");
            ClassWriter writer = new ClassWriter(reader,ClassWriter.COMPUTE_MAXS);
            ChangeVersionAdapter adapter = new ChangeVersionAdapter(writer);
            reader.accept(adapter,ClassReader.SKIP_CODE);

            ClassOutputUtil.byte2File("asmTest/build/asm/ConvertFile.class", writer.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


