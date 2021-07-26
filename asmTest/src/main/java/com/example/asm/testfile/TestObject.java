package com.example.asm.testfile;

/**
 * @author yudongliang
 * create time 2021-07-26
 * describe :
 */
public class TestObject {

    public String name;
    public static final Thread F1 = new Thread();

    public  Thread f2 = new Thread();

    public static Thread getF3() {
        return new Thread();
    }

    public Thread getF4() {
        return new Thread();
    }


}
