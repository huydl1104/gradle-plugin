package com.example.asm.testfile;

/**
 * @author yudongliang
 * create time 2021-07-22
 * describe : 测试转换类
 */
public class ConvertFile {

    public int number;
    public int age;
    public boolean debug;

    public ConvertFile(int number,int age){
        this.number = number;
        this.age = age;
    }

    public int addNumber(int scale){
        int max = Math.max(age, number);
        return max * scale;
    }

    public void setDebugValue(boolean debug){
        this.debug = debug;
    }

    public Boolean getDebugMode(){
        return debug;
    }
}
