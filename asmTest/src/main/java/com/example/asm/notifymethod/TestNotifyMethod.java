package com.example.asm.notifymethod;

import com.example.asm.utils.LogUtils;
import com.example.asm.utils.ClassOutputUtil;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;

import java.io.IOException;


/**
 * @author yudongliang
 * create time 2021-07-22
 * describe : 修改 class 文件中的方法
 * MethodVisitor方法详解
 *      visitVarInsn(int, int)：访问一个有关于局部变量的字节码指令，如ALOAD
 *      visitInvokeDynamicInsn(String, String, Handle, Object...)：基于 INVOKEDYNAMIC，动态方法调用，会在lambda表达式和方法引用里面说到
 *      visitTableSwitchInsn(int, int, Label, Label...)：基于TABLESWITCH，用于进行table-switch操作
 *      visitLookupSwitchInsn(Label, int[], Label[])：基于LOOKUPSWITCH，用于进行lookup-switch操作
 *      visitMultiANewArrayInsn(String, int)：基于MULTIANEWARRAY，用于创建多重维度数组，如int[][]
 *
 * 方法调用顺序 ()*表示可以调用任意次、[]表示里面的方法最多调用一次
 *      ( visitParameter )*
 *      [ visitAnnotationDefault ]
 *      ( visitAnnotation |visitAnnotableParameterCount | visitParameterAnnotation
 *          visitTypeAnnotation | visitAttribute )*
 *      [ visitCode ( visitFrame | visitInsn | visitLabel | visitInsnAnnotation | visitTryCatchBlock | visitTryCatchAnnotation | visitLocalVariable |
 *      visitLocalVariableAnnotation | visitLineNumber )* visitMaxs ]
 *      visitEnd
 *
 * visitParameter:访问方法一个参数
 * visitAnnotationDefualt:访问注解接口方法的默认值；
 * visitAnnotaion:访问方法的一个注解；
 * visitTypeAnnotation:访问方法签名上的一个类型的注解;
 * visitAnnotableParameterCount:访问注解参数数量，就是访问方法参数有注解参数个数;
 * visitParameterAnnotation:访问参数的注解，返回一个AnnotationVisitor可以访问该注解值;
 * visitAttribute:访问方法属性；
 * visitCode:开始访问方法代码，此处可以添加方法运行前拦截器;
 * visitFrame:访问方法局部变量的当前状态以及操作栈成员信息，方法栈必须是expanded 格式或者compressed格式,该方法必须在visitInsn方法前调用;
 * visitDynamicInsn:访问动态类型指令；
 *
 * visitInsn(int)：访问一个零参数要求的字节码指令，如 ACONST_NULL
 * visitIntInsn(int, int)：访问一个需要零操作栈要求，但需要有一个int参数的字节码指令，如BIPUSH
    public void visitIntInsn(final int opcode, final int operand) {
        if (mv != null) {
          mv.visitIntInsn(opcode, operand);
        }
    }
    opcode表示操作码指令,在这里opcode可以是Opcodes.BIPUSH,Opcodes.SIPUSH,Opcodes.NEWARRAY中一个；
    operand表示操作数，
    如果opcode 为BIPUSH,那么operand value必须在Byte. minValue和Byte.maxValue之间；
    如果opcode 为SIPUSH,那么operand value必须在Short.minValue和Short.minValue之间；
    如果opcode 为NEWARRAY,那么operand value 可以取下面中一个：
    Opcodes.T_BOOLEN,OPcodes.T_BYTE,OPCODES.T_CHAR,OPcodes.T_SHORT,OPcodes.T_INT，OPcodes.T_FLOAT,Opcodes.T_DOUBLE,Opcodes.T_LONG；
 * visitVarInsn:访问本地变量类型指令
 *    public void visitVarInsn(final int opcode, final int var) {
 *      if (mv != null) {
 *         mv.visitVarInsn(opcode, var);
 *      }
 *    }
 *    var表示需要访问的变量；
 *    opcode:操作码可以是LOAD,STORE，RET中一种；
 * visitTypeInsn(int, String)：访问一个有关于类型的字节码指令，类型指令会把类的内部名称当成参数Type 。如CHECKCAST
 *     public void visitTypeInsn(final int opcode, final String type) {
 *       if (mv != null) {
 *         mv.visitTypeInsn(opcode, type);
 *       }
 *     }
 *     opcode:操作码为NEW ,ANEWARRAY,CHECKCAST,INSTANCEOF;
 *     type:对象或者数组的内部名称，可以通过Type.getInternalName()获取；
 *
 * visitFieldInsn(int, String, String, String)：用来加载或者存储对象的Field，访问一个有关于字段的字节码，如 PUTFIELD
 *   public void visitFieldInsn(
 *       final int opcode, final String owner, final String name, final String descriptor) {
 *     if (mv != null) {
 *       mv.visitFieldInsn(opcode, owner, name, descriptor);
 *     }
 *   }
 * visitMethodInsn(int, String, String, String, boolean)：:访问方法调用的操作指令字节码，如INVOKESPECIAL
 *    public void visitMethodInsn(
 *       final int opcode,
 *       final String owner,
 *       final String name,
 *       final String descriptor,
 *       final boolean isInterface) {
 *     if (api < Opcodes.ASM5) {
 *       if (isInterface != (opcode == Opcodes.INVOKEINTERFACE)) {
 *         throw new IllegalArgumentException("INVOKESPECIAL/STATIC on interfaces requires ASM5");
 *       }
 *       visitMethodInsn(opcode, owner, name, descriptor);
 *       return;
 *     }
 *     if (mv != null) {
 *       mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
 *     }
 *    }
 *    opcode：为INVOKESPECIAL,INVOKESTATIC,INVOKEVIRTUAL,INVOKEINTERFACE;
 *    owner:方法拥有者的名称;
 *    name:方法名称;
 *    descriptor:方法描述，参数和返回值;
 *    isInterface；是否是接口;
 *
 * visitJumpInsn(int, Label)：访问跳转字节码，如IFEQ
 *     public void visitJumpInsn(final int opcode, final Label label) {
 *          if (mv != null) {
 *              mv.visitJumpInsn(opcode, label);
 *          }
 *     }
 *     opcode: IFEQ,  IFNE, IFLT, IFGE, IFGT, IFLE, IF_ICMPEQ, IF_ICMPNE, IF_ICMPLT, IF_ICMPGE, IF_ICMPGT, IF_ICMPLE, IF_ACMPEQ, IF_ACMPNE, GOTO, JSR, IFNULL or IFNONNULL.
 *     label:跳转目的label;
 *
 * visitLdcInsn(Object)：访问ldc指令，也就是访问常量池索引，基于LDC、LDC_W和LDC2_W，将一个常量加载到操作栈用（详细见下文）
 *  public void visitLdcInsn(final Object value) {
 *     if (api < Opcodes.ASM5
 *         && (value instanceof Handle
 *             || (value instanceof Type && ((Type) value).getSort() == Type.METHOD))) {
 *       throw new UnsupportedOperationException(REQUIRES_ASM5);
 *     }
 *     if (api != Opcodes.ASM7 && value instanceof ConstantDynamic) {
 *       throw new UnsupportedOperationException("This feature requires ASM7");
 *     }
 *     if (mv != null) {
 *       mv.visitLdcInsn(value);
 *     }
 *   }
 *  value:必须是非空的Integer,Float,Double,Long,String,或者对象的Type,Array的Type,Method Sort的Type，或者Method Handle常量中的Handle，或者ConstantDynamic;
 * visitIincInsn(int, int)：访问本地变量索引增加指令，基于IINC、IINC_W，自增/减表达式
 *   public void visitIincInsn(final int var, final int increment) {
 *     if (mv != null) {
 *       mv.visitIincInsn(var, increment);
 *     }
 *   }
 *   var:表示本地变量的索引
 * visitLocalVariable:访问本地变量描述。
 *   public void visitLocalVariable(
 *       final String name,
 *       final String descriptor,
 *       final String signature,
 *       final Label start,
 *       final Label end,
 *       final int index) {
 *     if (mv != null) {
 *       mv.visitLocalVariable(name, descriptor, signature, start, end, index);
 *     }
 *   }
 *   name:本地变量名称;
 *   desriptor:本地变量类型描述;
 *   signature:本地变量类型签名；
 *   start:关联该本地变量范围的第一个指令的位置；
 *   end:关联该本地变量范围的最后一个指令的位置：
 *   index:本地变量的索引;
 */
public class TestNotifyMethod {

    public static class TimeCountAdapter extends ClassVisitor implements Opcodes {
        private String owner;
        private boolean isInterface;

        public TimeCountAdapter(ClassVisitor classVisitor) {
            super(ASM6, classVisitor);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces);
            owner = name;
            isInterface = (access & ACC_INTERFACE) != 0;
            LogUtils.info("visit owner = "+owner+", isInterface = "+isInterface);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor mv=cv.visitMethod(access, name, descriptor, signature, exceptions);
            LogUtils.info("visitMethod name = "+name+", descriptor = "+descriptor);
            if (!isInterface && mv != null && !name.equals("<init>")) {
                AddTimerMethodAdapter at = new AddTimerMethodAdapter(mv);
                at.aa = new AnalyzerAdapter(owner, access, name, descriptor, at);
                at.lvs = new LocalVariablesSorter(access, descriptor, at.aa);
                return at.lvs;
            }
            return mv;
        }

        @Override
        public void visitEnd() {
            super.visitEnd();
        }

    }

    private static class AddTimerMethodAdapter extends MethodVisitor implements Opcodes {
        private int time;
        public LocalVariablesSorter lvs;
        public AnalyzerAdapter aa;

        public AddTimerMethodAdapter(MethodVisitor methodVisitor) {
            super(ASM8, methodVisitor);
        }

        //    public void addTimer() {
        //        long var1 = System.nanoTime();
        //        System.out.println("time running ... ");
        //        var1 = System.nanoTime() - var1;
        //        System.out.println(var1);
        //    }
        @Override
        public void visitCode() {
            mv.visitCode();
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
            time=lvs.newLocal(Type.LONG_TYPE);
            LogUtils.info("time = "+time+" ,sort ="+Type.LONG_TYPE.getSize());
            //LSTORE: 将一个数值从操作数栈存储到局部变量表的指令
            mv.visitVarInsn(LSTORE, time);
        }

        @Override
        public void visitInsn(int opcode) {
            if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
                //INVOKESTATIC：调用类的方法 static
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
                //LLOAD：将一个局部变量加载到操作数栈的指令
                mv.visitVarInsn(LLOAD, time);
                //LSUB：减法指令
                mv.visitInsn(LSUB);
                mv.visitVarInsn(LSTORE, time);

                //GETSTATIC：访问字段指令
                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                mv.visitVarInsn(LLOAD, time);
                //INVOKEVIRTUAL：调用对象的实例方法
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(J)V", false);
            }
            mv.visitInsn(opcode);
        }
    }


    public static void main(String[] args) {
        TestNotifyMethod main = new TestNotifyMethod();
        main.exe1();
    }

    private void exe1() {
        try {
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
//            TraceClassVisitor tv = new TraceClassVisitor(cw,new PrintWriter(System.out));
            TimeCountAdapter addFiled = new TimeCountAdapter(cw);
            ClassReader classReader= new ClassReader("com/example/asm/testfile/TestAddTimerAdapter");
            classReader.accept(addFiled,ClassReader.EXPAND_FRAMES);
            ClassOutputUtil.byte2File("asmTest/build/asm/TestAddTimerAdapter.class", cw.toByteArray());


            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
