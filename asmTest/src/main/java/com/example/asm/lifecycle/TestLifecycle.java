package com.example.asm.lifecycle;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;


/**
 * @author yudongliang
 * create time 2021-07-22
 * describe : classVisitor 的声明周期
 */
public class TestLifecycle {
    public static void main(String[] args) {
        System.out.println("TestLifecycle  --- ");
        TestLifecycle main = new TestLifecycle();
        main.exe();

    }

    private void exe(){
        try {
            //classReader 中String 参数 推荐写为 /
            ClassReader reader = new ClassReader("com/example/asm/testfile/Testfile");
            ClassVisitor visitor = new ClassPrintVisitor(Opcodes.ASM9);
            reader.accept(visitor, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 打印生命周期结果如下，先访问class -> innerClass -> field -> method
     * [Andoter]:visit:version=52, access=ACC_PUBLIC ACC_TRANSITIVE ,name=com/example/asm/testfile/TestFile,signature=null,superName=java/lang/Object
     * [Andoter]:visitSource:source=TestFile.java,debug=null
     * [Andoter]:visitInnerClass:name=com/example/asm/testfile/TestFile$InnerFile,outerName=com/example/asm/testfile/TestFile,innerName:InnerFile,access=ACC_PRIVATE
     * [Andoter]:visitField:access=ACC_PRIVATE ,name=address,descriptor=Ljava/lang/String;,signature=null,value=null
     * [Andoter]:visitField:access=ACC_PRIVATE ,name=age,descriptor=I,signature=null,value=null
     * [Andoter]:visitField:access=ACC_PRIVATE ,name=money,descriptor=D,signature=null,value=null
     * [Andoter]:visitField:access=ACC_PUBLIC ACC_STATIC ,name=happy,descriptor=Z,signature=null,value=null
     * [Andoter]:visitMethod：access=ACC_PUBLIC ,name=<init>,descriptor=()V,signature=null
     * [Andoter]:visitMethod：access=ACC_PRIVATE ,name=getName,descriptor=()Ljava/lang/String;,signature=null
     * [Andoter]:visitMethod：access=ACC_STATIC ,name=<clinit>,descriptor=()V,signature=null
     * [Andoter]:visitEnd
     */

}
