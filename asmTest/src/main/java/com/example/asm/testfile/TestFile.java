package com.example.asm.testfile;

/**
 * @author yudongliang
 * create time 2021-07-22
 * describe : 测试文件
 */
public class TestFile {

    private String address = "北京市";
    private int age = 30;

    public static boolean happy = true;

    private String getName(){
        return address;
    }

    private class InnerFile{
        private int[] location;
    }

    private double money = 111110;
}
