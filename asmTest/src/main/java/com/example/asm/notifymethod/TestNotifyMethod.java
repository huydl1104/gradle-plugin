package com.example.asm.notifymethod;

/**
 * @author yudongliang
 * create time 2021-07-22
 * describe : 修改 class 文件中的方法
 * MethodVisitor方法详解
 *      visitInsn(int)：访问一个零参数要求的字节码指令，如 ACONST_NULL
 *      visitIntInsn(int, int)：访问一个需要零操作栈要求但需要有一个int参数的字节码指令，如BIPUSH
 *      visitVarInsn(int, int)：访问一个有关于局部变量的字节码指令，如ALOAD
 *      visitTypeInsn(int, String)：访问一个有关于类型的字节码指令，如CHECKCAST
 *      visitFieldInsn(int, String, String, String)：访问一个有关于字段的字节码，如PUTFIELD
 *      visitMethodInsn(int, String, String, String, boolean)：访问一个有关于方法调用的字节码，如INVOKESPECIAL
 *      visitJumpInsn(int, Label)：访问跳转字节码，如IFEQ
 *      visitInvokeDynamicInsn(String, String, Handle, Object...)：基于 INVOKEDYNAMIC，动态方法调用，会在lambda表达式和方法引用里面说到
 *      visitLdcInsn(Object)：基于LDC、LDC_W和LDC2_W，将一个常量加载到操作栈用（详细见下文）
 *      visitIincInsn(int, int)：基于IINC、IINC_W，自增/减表达式
 *      visitTableSwitchInsn(int, int, Label, Label...)：基于TABLESWITCH，用于进行table-switch操作
 *      visitLookupSwitchInsn(Label, int[], Label[])：基于LOOKUPSWITCH，用于进行lookup-switch操作
 *      visitMultiANewArrayInsn(String, int)：基于MULTIANEWARRAY，用于创建多重维度数组，如int[][]
 */
public class TestNotifyMethod {
    public static void main(String[] args) {
        TestNotifyMethod main = new TestNotifyMethod();
        main.exe1();
    }

    private void exe1() {

    }
}
