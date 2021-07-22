package com.example.asm.checkclass;


import com.example.asm.utils.ClassOutputUtil;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;

/**
 * @author yudongliang
 * create time 2021-07-22
 * describe : 可视化 ClassWrite输出的字节数组，ASM中提供了 TraceClassVisitor 用来输出检查
 */
public class TestTreeClassVisitor {
    public static void main(String[] args) {
        TestTreeClassVisitor main = new TestTreeClassVisitor();
        main.exe1();
    }

    private void exe1() {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        //使用 TraceClassVisitor，同时使用 System.out 流将结果输出。
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(writer, new PrintWriter(System.out));

        traceClassVisitor.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC + Opcodes.ACC_INTERFACE + Opcodes.ACC_ABSTRACT,
                "com.andoter.asm_example.part2/TraceClassVisitorDemo", null, "java/lang/Object", null);
//        traceClassVisitor.visitSource("TraceClassVisitorDemo.class", null);
        traceClassVisitor.visitField(Opcodes.ACC_PUBLIC+ Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "className", "Ljava/lang/String;", null, "").visitEnd();
        traceClassVisitor.visitField(Opcodes.ACC_PUBLIC+ Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "classVersion", "I", null, 50).visitEnd();
        traceClassVisitor.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, "getTraceInfo", "()Ljava/lang/String;", null, null).visitEnd();
        traceClassVisitor.visitEnd();

        ClassOutputUtil.byte2File("asmTest/build/asm/TraceClassVisitorDemo.class", writer.toByteArray());
    }

}
