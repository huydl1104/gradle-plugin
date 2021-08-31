package com.example.asm.commons;


import com.example.asm.ASMManager;
import com.example.asm.utils.ClassOutputUtil;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;

import java.io.IOException;

import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ASM9;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LADD;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.LSTORE;
import static org.objectweb.asm.Opcodes.LSUB;
import static org.objectweb.asm.Opcodes.PUTSTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

/**
 * 演示将指定类的所有非构造方法前后添加如下代码示例的耗时统计代码：
 */
public class LocalVariablesSorterMain {

  public static void main(String[] args) throws IOException {
    ClassReader cr = new ClassReader(Type.getInternalName(LocalVariablesSorterMain.class));
    ClassWriter cw = new ClassWriter(0);
    AddTimerAdapter addTimerAdapter = new AddTimerAdapter(cw);
    cr.accept(addTimerAdapter, 0);
    ClassOutputUtil.byte2File("asmTest/build/asm/LocalVariablesSorterMain.class", cw.toByteArray());
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
      // 不处理接口和构造方法
      if (mv != null && !isInterface && !name.equals("<init>")) {
        mv = new AddTimerMethodAdapter(owner, access, descriptor, mv);
      }
      return mv;
    }

    @Override
    public void visitEnd() {
      // 添加字段
      if (!isInterface) {
        cv.visitField(ACC_PUBLIC + ACC_STATIC, "s_timer", "J", null, null).visitEnd();
      }
      super.visitEnd();
    }
  }

  static class AddTimerMethodAdapter extends LocalVariablesSorter {
    private final String owner;
    private int time;

    public AddTimerMethodAdapter(String owner, int access, String desc, MethodVisitor mv) {
      super(ASM9, access, desc, mv);
      this.owner = owner;
    }

    @Override
    public void visitCode() {
      super.visitCode();
      mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
      time = newLocal(Type.LONG_TYPE);
      mv.visitVarInsn(LSTORE, time);
    }

    @Override
    public void visitInsn(int opcode) {
      if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        mv.visitVarInsn(LLOAD, time);
        mv.visitInsn(LSUB);
        mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
        mv.visitInsn(LADD);
        mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
      }
      super.visitInsn(opcode);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
      super.visitMaxs(maxStack + 4, maxLocals);
    }
  }

}
