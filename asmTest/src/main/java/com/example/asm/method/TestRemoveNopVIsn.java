package com.example.asm.method;

import com.example.asm.utils.ClassOutputUtil;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.ArrayList;


public class TestRemoveNopVIsn {


    public static void main(String[] args) throws Exception {
        TestRemoveNopVIsn main = new TestRemoveNopVIsn();
        main.exe1();
    }

    private void exe1() throws Exception {
        ClassReader reader = new ClassReader(Type.getInternalName(ArrayList.class));
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor visitor = new ClassVisitor(Opcodes.ASM8,writer) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
                if (methodVisitor != null){
                    methodVisitor = new RemoveNopAdapter(methodVisitor);
                }
                return methodVisitor ;
            }
        };
        reader.accept(visitor,0);

        ClassOutputUtil.byte2File("asmTest/build/asm/ArrayListDemo.class", writer.toByteArray());


    }
   static class RemoveNopAdapter extends MethodVisitor implements Opcodes{

        public RemoveNopAdapter( MethodVisitor methodVisitor) {
            super(ASM8, methodVisitor);
        }

       @Override
       public void visitInsn(int opcode) {
            if (opcode != NOP){
                super.visitInsn(opcode);
            }
       }
   }
}
