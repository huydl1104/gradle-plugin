package com.example.asm.notifymethod;

import com.example.asm.utils.LogUtils;
import com.example.asm.utils.ClassOutputUtil;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;

import java.io.IOException;


/**
 * @author yudongliang
 * create time 2021-07-22
 * describe : 修改 class 文件中的方法
 *
 */
public class TestNotifyMethod {

    public static class TimeCountAdapter extends ClassVisitor implements Opcodes {
        private String owner;
        private boolean isInterface;

        public TimeCountAdapter(ClassVisitor classVisitor) {
            super(ASM6, classVisitor);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces);
            owner = name;
            isInterface = (access & ACC_INTERFACE) != 0;
            LogUtils.info("visit owner = "+owner+", isInterface = "+isInterface);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor mv=cv.visitMethod(access, name, descriptor, signature, exceptions);
            LogUtils.info("visitMethod name = "+name+", descriptor = "+descriptor);
            if (!isInterface && mv != null && !name.equals("<init>")) {
                AddTimerMethodAdapter at = new AddTimerMethodAdapter(mv);
                at.aa = new AnalyzerAdapter(owner, access, name, descriptor, at);
                at.lvs = new LocalVariablesSorter(access, descriptor, at.aa);
                return at.lvs;
            }
            return mv;
        }

        @Override
        public void visitEnd() {
            super.visitEnd();
        }

    }

    private static class AddTimerMethodAdapter extends MethodVisitor implements Opcodes {
        private int time;
        public LocalVariablesSorter lvs;
        public AnalyzerAdapter aa;

        public AddTimerMethodAdapter(MethodVisitor methodVisitor) {
            super(ASM8, methodVisitor);
        }

        //    public void addTimer() {
        //        long var1 = System.nanoTime();
        //        System.out.println("time running ... ");
        //        var1 = System.nanoTime() - var1;
        //        System.out.println(var1);
        //    }
        @Override
        public void visitCode() {
            mv.visitCode();
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
            time=lvs.newLocal(Type.LONG_TYPE);
            LogUtils.info("time = "+time+" ,sort ="+Type.LONG_TYPE.getSize());
            //LSTORE: 将一个数值从操作数栈存储到局部变量表的指令
            mv.visitVarInsn(LSTORE, time);
        }

        @Override
        public void visitInsn(int opcode) {
            if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
                //INVOKESTATIC：调用类的方法 static
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
                //LLOAD：将一个局部变量加载到操作数栈的指令
                mv.visitVarInsn(LLOAD, time);
                //LSUB：减法指令
                mv.visitInsn(LSUB);
                mv.visitVarInsn(LSTORE, time);

                //GETSTATIC：访问字段指令
                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                mv.visitVarInsn(LLOAD, time);
                //INVOKEVIRTUAL：调用对象的实例方法
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(J)V", false);
            }
//            mv.visitInsn(opcode);
        }
    }


    public static void main(String[] args) {
        TestNotifyMethod main = new TestNotifyMethod();
        main.exe1();
    }

    private void exe1() {
        try {
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
//            TraceClassVisitor tv = new TraceClassVisitor(cw,new PrintWriter(System.out));
            TimeCountAdapter addFiled = new TimeCountAdapter(cw);
            ClassReader classReader= new ClassReader("com/example/asm/testfile/TestAddTimerAdapter");
            classReader.accept(addFiled,ClassReader.EXPAND_FRAMES);
            ClassOutputUtil.byte2File("asmTest/build/asm/TestAddTimerAdapter.class", cw.toByteArray());


            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
