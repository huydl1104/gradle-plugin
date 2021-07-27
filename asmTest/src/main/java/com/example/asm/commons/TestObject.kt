package com.example.asm.commons

/**
 * @author yudongliang
 * create time 2021-07-27
 * describe :
 */
class TestObject {

    fun getTest(){
        val start = System.currentTimeMillis()

        Thread.sleep(100)

        val end = System.currentTimeMillis()
        System.out.println("方法执行时间 -> ${end-start}")
    }
    //    LINENUMBER 11 L0
    //    INVOKESTATIC java/lang/System.currentTimeMillis ()J
    //    LSTORE 1

    //    LINENUMBER 15 L2
    //    INVOKESTATIC java/lang/System.currentTimeMillis ()J
    //    LSTORE 3


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
    //
}