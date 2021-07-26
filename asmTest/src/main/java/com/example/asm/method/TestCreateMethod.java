package com.example.asm.method;

import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

/**
 * @author yudongliang
 * create time 2021-07-24
 * describe :
 *
 */

//class MakeMethod {
//    private var f : Int = 0
//    fun checkAndSetF(f: Int) {
//        if (f >= 0) {
//            this.f = f
//        } else {
//            throw IllegalArgumentException()
//        }
//    }
//}

public class TestCreateMethod {



    public static void main(String[] args) {
        TestCreateMethod main = new TestCreateMethod();
        main.exe1();
    }


    private void exe1() {
        MethodNode methodNode = new MethodNode();
        InsnList list = methodNode.instructions;


    }
}
