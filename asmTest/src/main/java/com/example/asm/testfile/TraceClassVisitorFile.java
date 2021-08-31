package com.example.asm.testfile;

/**
 * @author yudongliang
 * create time 2021-07-22
 * describe : 测试 TraceClassVisitor
 */
public class TraceClassVisitorFile {
    public String className = "kotlin";
    public int classVersion = 2;
    public String getTraceInfo(){
        return  className + "----" +classVersion;
    }
    public int testMethod(String value){
        return value.length();
    }
}
