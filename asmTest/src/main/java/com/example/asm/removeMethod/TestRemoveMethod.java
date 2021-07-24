package com.example.asm.removeMethod;

import com.example.asm.utils.ClassOutputUtil;
import com.example.asm.utils.LogUtils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.List;


/**
 * @author yudongliang
 * create time 2021-07-24
 * describe : 测试 ClassNode 去移除 method
 */
public class TestRemoveMethod {
    public static void main(String[] args) {
        TestRemoveMethod main = new TestRemoveMethod();
        main.exe1();
    }

    private void exe1() {
        try {
            ClassNode node = new ClassNode();
            ClassReader reader = new ClassReader("com/example/asm/testfile/TestRemoveMethod");
            reader.accept(node,0);

            removeMethod(node);

            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            node.accept(writer);

            ClassOutputUtil.byte2File("asmTest/build/asm/TestRemoveMethod.class",writer .toByteArray());

            //1、ClassNode 与 ClassReader 的结合转换
            //ClassNode classNode = ClassNode()
            //ClassNode classReader = ClassReader("")
            //classReader.accept(classNode, ClassReader.SKIP_DEBUG)

            //2、ClassNode 与 ClassWriter 的结合
            //ClassNode classNode1 = ClassNode()
            //ClassWriter classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
            //classNode1.accept(classWriter)


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void removeMethod(ClassNode node){
        List<MethodNode> currentNodes = node.methods;
        if (currentNodes != null && currentNodes.size() > 0){
            for (MethodNode currentNode : currentNodes) {
                LogUtils.info("name = "+currentNode.name +" ,desc ="+currentNode.desc);
                if ("test1".equals(currentNode.name) && "()V".equals(currentNode.desc)){
                    node.methods.remove(currentNode);
                }
            }
        }
    }

}
