package com.example.asm.type;
import com.example.asm.testfile.TraceClassVisitorFile;
import com.example.asm.utils.ADLog;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * @author yudongliang
 * create time 2021-07-22
 * describe : Type用法
 */
public class TestType {

    public static void main(String[] args) {
        TestType main = new TestType();
        main.exe1();
    }

    private void exe1() {
        // getInternalName 方法返回一个Type的内部名。
        // 例如：Type.getType(String.class). getInternalName()给出 String 类
        String className = Type.getType(String.class).getClassName();
        String internalName = Type.getType(String.class).getInternalName();
        //[Andoter]:className ->java.lang.String, internalName ->java/lang/String
        ADLog.info("className ->"+className+", internalName ->"+internalName);

        String descriptor = Type.getDescriptor(String.class);
        String descriptor1 = Type.getType(String.class).getDescriptor();
        //[Andoter]:descriptor ->Ljava/lang/String;, descriptor1 ->Ljava/lang/String;
        ADLog.info("descriptor ->"+descriptor+", descriptor1 ->"+descriptor1);

        try {
            String methodDescriptor = Type.getMethodDescriptor(TraceClassVisitorFile.class.getDeclaredMethod("testMethod",String.class));
            //[Andoter]:methodDescriptor ->(Ljava/lang/String;)I
            ADLog.info("methodDescriptor ->"+methodDescriptor);

            Type[] testMethods = Type.getArgumentTypes(TraceClassVisitorFile.class.getDeclaredMethod("testMethod", String.class));
            //[Andoter]:getArgumentType  ->Ljava/lang/String;
            ADLog.info("getArgumentType  ->"+testMethods[0]);

            Type testMethod = Type.getReturnType(TraceClassVisitorFile.class.getDeclaredMethod("testMethod", String.class));
            //[Andoter]:returnType  ->I
            ADLog.info("returnType  ->"+testMethod);

            int type = Type.FLOAT_TYPE.getOpcode(Opcodes.IMUL);
            ADLog.info("type  ->"+type);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


    }

}
