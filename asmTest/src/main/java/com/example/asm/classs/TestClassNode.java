package com.example.asm.classs;

import com.example.asm.utils.ClassOutputUtil;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * @author yudongliang
 * create time 2021-07-24
 * describe : 使用树的API生成类的过程，创建一个class对象初始化字段，虽然需要多话费30%时间，但是可以按照任意顺序生成元素。
 */
public class TestClassNode {

    public static void main(String[] args) {
        TestClassNode main = new TestClassNode();
        main.exe1();
    }

    private void exe1() {
        ClassNode node = new ClassNode();
        node.version = Opcodes.V1_8;
        node.access = Opcodes.ACC_PUBLIC + Opcodes.ACC_INTERFACE + Opcodes.ACC_ABSTRACT;
        node.name = "com/example/asm/MyComparable";
        //superName：是指父类名，但这个隐含的父类java.lang.Object必须写出
        node.superName = "java/lang/Object";
        //interfaces：指的是实现/继承的接口名
        node.interfaces.add("com/example/asm/Measurable");
        node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC+Opcodes.ACC_FINAL,"money1","I",null,10));
        node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC+Opcodes.ACC_FINAL,"name","Ljava/lang/String;",null,"xiaolan"));
        node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC+Opcodes.ACC_FINAL,"money2","D",null,10000));
        node.methods.add(new MethodNode(Opcodes.ACC_PUBLIC+Opcodes.ACC_FINAL,"compareTo","(Ljava/lang/Object;)I",null,null));
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);

        ClassOutputUtil.byte2File("asmTest/build/asm/MyComparable.class", writer.toByteArray());
    }
}
