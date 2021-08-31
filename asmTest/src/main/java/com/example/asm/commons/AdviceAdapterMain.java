package com.example.asm.commons;

import com.example.asm.utils.ClassOutputUtil;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.IOException;

import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ASM9;

/**
 * 类将指定类的所有方法（包括构造方法）前后添加上方法执行耗时的统计代码并打印出来。
 *     fun getTest(){
 *         val start = System.currentTimeMillis()
 *
 *         Thread.sleep(100)
 *
 *         val end = System.currentTimeMillis()
 *         System.out.println("方法执行时间 -> ${end-start}")
 *     }
 */
public class AdviceAdapterMain {

  public static void main(String[] args) throws IOException {
    ClassReader cr = new ClassReader(Type.getInternalName(AdviceAdapterMain.class));
    ClassWriter cw = new ClassWriter(0);
    AddTimerAdapter addTimerAdapter = new AddTimerAdapter(cw);
    cr.accept(addTimerAdapter, 0);
    ClassOutputUtil.byte2File("asmTest/build/asm/AdviceAdapterMain.class", cw.toByteArray());
  }

  static class AddTimerAdapter extends ClassVisitor {
    private boolean isInterface;
    private String owner;

    public AddTimerAdapter(ClassVisitor classVisitor) {
      super(ASM9, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
      super.visit(version, access, name, signature, superName, interfaces);
      isInterface = (access & ACC_INTERFACE) != 0;
      owner = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
      MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
      // 不处理接口
      if (mv != null && !isInterface) {
        mv = new AddTimerMethodAdapter(owner, mv, access, name, descriptor);
      }
      return mv;
    }

    @Override
    public void visitEnd() {
      // 添加字段
/*      if (!isInterface) {
        cv.visitField(ACC_PUBLIC + ACC_STATIC, "s_timer", "J", null, null).visitEnd();
      }*/
      super.visitEnd();
    }
  }

  static class AddTimerMethodAdapter extends AdviceAdapter {
    private final String owner;

    public AddTimerMethodAdapter(String owner, MethodVisitor mv, int access, String name, String desc) {
      super(ASM9, mv, access, name, desc);
      this.owner = owner;
    }

    @Override
    protected void onMethodEnter() {
      //    LINENUMBER 11 L0
      //    INVOKESTATIC java/lang/System.currentTimeMillis ()J
      //    LSTORE 1
      mv.visitMethodInsn(INVOKESTATIC,"java/lang/System","currentTimeMillis","()J",false);
      int varLocal = newLocal(Type.LONG_TYPE);
      mv.visitVarInsn(LSTORE, varLocal);
    }


    @Override
    protected void onMethodExit(int opcode) {
      //    LINENUMBER 15 L2
      //    INVOKESTATIC java/lang/System.currentTimeMillis ()J
      //    LSTORE 3
      mv.visitMethodInsn(INVOKESTATIC,"java/lang/System","currentTimeMillis","()J",false);
      int varLocal = newLocal(Type.LONG_TYPE);
      mv.visitVarInsn(LSTORE, varLocal);
      //打印出来时间间隔
      //    LINENUMBER 16 L3
      //    GETSTATIC java/lang/System.out : Ljava/io/PrintStream;
      //    NEW java/lang/StringBuilder
      //    DUP
      //    INVOKESPECIAL java/lang/StringBuilder.<init> ()V
      //    LDC "\u65b9\u6cd5\u6267\u884c\u65f6\u95f4 -> "
      //    INVOKEVIRTUAL java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //    LLOAD 3
      //    LLOAD 1
      //    LSUB
      //    INVOKEVIRTUAL java/lang/StringBuilder.append (J)Ljava/lang/StringBuilder;
      //    INVOKEVIRTUAL java/lang/StringBuilder.toString ()Ljava/lang/String;
      //    INVOKEVIRTUAL java/io/PrintStream.println (Ljava/lang/String;)V
      mv.visitFieldInsn(GETSTATIC,"java/lang/System","out","Ljava/io/PrintStream");
      mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
      mv.visitInsn(DUP);
      mv.visitMethodInsn(INVOKESPECIAL,"java/lang/StringBuilder","<init>","()V",false);
      mv.visitLdcInsn("\\u65b9\\u6cd5\\u6267\\u884c\\u65f6\\u95f4 -> ");
      mv.visitMethodInsn(INVOKEVIRTUAL,"java/lang/StringBuilder","append","(Ljava/lang/String;)Ljava/lang/StringBuilder;",false);
      mv.visitVarInsn(LLOAD, 3);
      mv.visitVarInsn(LLOAD, 1);
      mv.visitInsn(LSUB);
      mv.visitMethodInsn(INVOKEVIRTUAL,"java/lang/StringBuilder","append","(J)Ljava/lang/StringBuilder;",false);
      mv.visitMethodInsn(INVOKEVIRTUAL,"java/lang/StringBuilder","toString","()Ljava/lang/String;",false);
      mv.visitMethodInsn(INVOKEVIRTUAL,"java/io/PrintStream","println","(Ljava/lang/String;)V",false);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
      super.visitMaxs(maxStack + 4, maxLocals);
    }
  }

}
