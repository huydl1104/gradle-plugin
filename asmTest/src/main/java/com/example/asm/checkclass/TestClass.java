package com.example.asm.checkclass;

import com.example.asm.lifecycle.ClassPrintVisitor;
import com.example.asm.utils.ClassOutputUtil;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.IOException;

/**
 * @author yudongliang
 * create time 2021-07-22
 * describe : 检查 class 文件
 * ClassWriter:
 *      ClassWriter类并不会校验其调用的方法顺序是否恰当和参数是否有效，有可能会产生一些被Java虚拟机拒绝的无效的类，
 *   为了能欧提前的检测出这些问题，可以使用 CheckClassAdapter 、TraceClassVisitor 扩展了ClassVisitor，其方法的
 *   调用都委托给另一个 ClassVisitor 。
 *      比如：一个 TraceClassVisitor 或者 ClassWriter ，这两个类并不会打印所访问类的文本，而是验证其方法的调用顺序是否合适，
 *   参数是否有效，然后才会委托给另一个访问器，当发生错误是会抛出 IllegalStateException 或 IllegalArgumentException。
 *
 * ClassReader:
 *      用于读取类文件的字节码，通过 accept 方法委托给 ClassVisitor 解析，重要的 options
 *   1、ClassReader.SKIP_DEBUG：表示跳过调试内容，即：SourceFile(跳过源文件)、SourceDebugExtension（源码调试扩展）、
 *      LocalVariableTable(局部变量表)、LocalVariableTypeObject(局部变量类型表)、LineNumberTable(行号表属性)、
 *      同时一下的方法 不会解析也不会被访问：
 *          ClassVisitor.visitResource，MethodVisitor.visitLocalVariable，MethodVisitor.visitLineNumber
 *      使用这个flag后类文件的调试信息会被移除！！！
 *   2、ClassReader.SHIP_CODE：跳过代码（attributes代码属性将不会被转换和访问，方法体代码不会进行解析和访问）。
 *   3、ClassReader.SKIP_FRAMES：跳过 StackMap(栈图)和StackMapTable(栈图表)，即 MethodVisitor.visitFrames方法不会转换和访问
 *      当使用 CLassWriter.SKIP_FRAMES 时，避免了访问帧内容，这些内容会被忽略和重新计算，无需访问。
 *   4、ClassReader.EXPAND_FRAMES：表示扩展栈帧图，默认栈图以它们的原始格式(V1_6以下使用扩展格式。其他使用压缩格式)被访问，
 *      若是设置了该标识，栈图则始终以扩展方式进行访问(此标识在 ClassReader 和 ClassWriter 中增加了解压/压缩步骤，会大幅度降低性能)
 */
public class TestClass {

    public static void main(String[] args) {
        TestClass main = new TestClass();
        main.exe1();
    }

    private void exe1(){
        ClassWriter writer = new ClassWriter(Opcodes.ASM9);
        CheckClassAdapter checkClassAdapter = new CheckClassAdapter(writer);
        //ACC_TRANSITIVE 相当于 class
        checkClassAdapter.visit(Opcodes.V1_8,Opcodes.ACC_PUBLIC+Opcodes.ACC_ABSTRACT+Opcodes.ACC_TRANSITIVE,
                "com/example/asm/Comparable",null,"java/lang/Object",
                new String[]{"com/example/asm/testfile/IAction"});
        checkClassAdapter.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC+Opcodes.ACC_FINAL,"field1", "I",null,10).visitEnd();
        checkClassAdapter.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC+Opcodes.ACC_FINAL,"field2", "Ljava/lang/String;",null,"aaa").visitEnd();
        checkClassAdapter.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC+Opcodes.ACC_FINAL,"field3", "I",null,50).visitEnd();
        checkClassAdapter.visitMethod(Opcodes.ACC_PUBLIC+Opcodes.ACC_ABSTRACT,"compareTo","(Ljava/lang/Object;)I",null,new String[]{"java/lang/Exception"}).visitEnd();
        checkClassAdapter.visitEnd();
        byte[] bytes = writer.toByteArray();

        ClassOutputUtil.byte2File("asmTest/build/asm/Comparable.class", bytes);
    }

    /**
     * 解析 ArrayList
     */
    private void exe2(){
        try {
            ClassReader reader = new ClassReader("java/util.ArrayList");
//            ArrayList
            ClassPrintVisitor visitor = new ClassPrintVisitor(Opcodes.ASM9);
            reader.accept(visitor,ClassReader.SKIP_DEBUG);

        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        [Andoter]:visit:version=52, access=ACC_PUBLIC ACC_TRANSITIVE ,name=java/util/ArrayList,signature=<E:Ljava/lang/Object;>Ljava/util/AbstractList<TE;>;Ljava/util/List<TE;>;Ljava/util/RandomAccess;Ljava/lang/Cloneable;Ljava/io/Serializable;,superName=java/util/AbstractList
        [Andoter]:visitInnerClass:name=java/util/ArrayList$ArrayListSpliterator,outerName=java/util/ArrayList,innerName:ArrayListSpliterator,access=ACC_STATIC ACC_FINAL
        [Andoter]:visitInnerClass:name=java/util/ArrayList$SubList,outerName=java/util/ArrayList,innerName:SubList,access=ACC_PRIVATE
        [Andoter]:visitInnerClass:name=java/util/ArrayList$ListItr,outerName=java/util/ArrayList,innerName:ListItr,access=ACC_PRIVATE
        [Andoter]:visitInnerClass:name=java/util/ArrayList$Itr,outerName=java/util/ArrayList,innerName:Itr,access=ACC_PRIVATE
        [Andoter]:visitField:access=ACC_PRIVATE ACC_STATIC ACC_FINAL ,name=serialVersionUID,descriptor=J,signature=null,value=8683452581122892189
        [Andoter]:visitField:access=ACC_PRIVATE ACC_STATIC ACC_FINAL ,name=DEFAULT_CAPACITY,descriptor=I,signature=null,value=10
        [Andoter]:visitField:access=ACC_PRIVATE ACC_STATIC ACC_FINAL ,name=EMPTY_ELEMENTDATA,descriptor=[Ljava/lang/Object;,signature=null,value=null
        [Andoter]:visitField:access=ACC_PRIVATE ACC_STATIC ACC_FINAL ,name=DEFAULTCAPACITY_EMPTY_ELEMENTDATA,descriptor=[Ljava/lang/Object;,signature=null,value=null
        [Andoter]:visitField:access=ACC_TRANSIENT ,name=elementData,descriptor=[Ljava/lang/Object;,signature=null,value=null
        [Andoter]:visitField:access=ACC_PRIVATE ,name=size,descriptor=I,signature=null,value=null
        [Andoter]:visitField:access=ACC_PRIVATE ACC_STATIC ACC_FINAL ,name=MAX_ARRAY_SIZE,descriptor=I,signature=null,value=2147483639
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=<init>,descriptor=(I)V,signature=null
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=<init>,descriptor=()V,signature=null
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=<init>,descriptor=(Ljava/util/Collection;)V,signature=(Ljava/util/Collection<+TE;>;)V
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=trimToSize,descriptor=()V,signature=null
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=ensureCapacity,descriptor=(I)V,signature=null
        [Andoter]:visitMethod：access=ACC_PRIVATE ACC_STATIC ,name=calculateCapacity,descriptor=([Ljava/lang/Object;I)I,signature=null
        [Andoter]:visitMethod：access=ACC_PRIVATE ,name=ensureCapacityInternal,descriptor=(I)V,signature=null
        [Andoter]:visitMethod：access=ACC_PRIVATE ,name=ensureExplicitCapacity,descriptor=(I)V,signature=null
        [Andoter]:visitMethod：access=ACC_PRIVATE ,name=grow,descriptor=(I)V,signature=null
        [Andoter]:visitMethod：access=ACC_PRIVATE ACC_STATIC ,name=hugeCapacity,descriptor=(I)I,signature=null
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=size,descriptor=()I,signature=null
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=isEmpty,descriptor=()Z,signature=null
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=contains,descriptor=(Ljava/lang/Object;)Z,signature=null
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=indexOf,descriptor=(Ljava/lang/Object;)I,signature=null
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=lastIndexOf,descriptor=(Ljava/lang/Object;)I,signature=null
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=clone,descriptor=()Ljava/lang/Object;,signature=null
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=toArray,descriptor=()[Ljava/lang/Object;,signature=null
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=toArray,descriptor=([Ljava/lang/Object;)[Ljava/lang/Object;,signature=<T:Ljava/lang/Object;>([TT;)[TT;
        [Andoter]:visitMethod：access=,name=elementData,descriptor=(I)Ljava/lang/Object;,signature=(I)TE;
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=get,descriptor=(I)Ljava/lang/Object;,signature=(I)TE;
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=set,descriptor=(ILjava/lang/Object;)Ljava/lang/Object;,signature=(ITE;)TE;
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=add,descriptor=(Ljava/lang/Object;)Z,signature=(TE;)Z
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=add,descriptor=(ILjava/lang/Object;)V,signature=(ITE;)V
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=remove,descriptor=(I)Ljava/lang/Object;,signature=(I)TE;
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=remove,descriptor=(Ljava/lang/Object;)Z,signature=null
        [Andoter]:visitMethod：access=ACC_PRIVATE ,name=fastRemove,descriptor=(I)V,signature=null
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=clear,descriptor=()V,signature=null
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=addAll,descriptor=(Ljava/util/Collection;)Z,signature=(Ljava/util/Collection<+TE;>;)Z
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=addAll,descriptor=(ILjava/util/Collection;)Z,signature=(ILjava/util/Collection<+TE;>;)Z
        [Andoter]:visitMethod：access=ACC_PROTECTED ,name=removeRange,descriptor=(II)V,signature=null
        [Andoter]:visitMethod：access=ACC_PRIVATE ,name=rangeCheck,descriptor=(I)V,signature=null
        [Andoter]:visitMethod：access=ACC_PRIVATE ,name=rangeCheckForAdd,descriptor=(I)V,signature=null
        [Andoter]:visitMethod：access=ACC_PRIVATE ,name=outOfBoundsMsg,descriptor=(I)Ljava/lang/String;,signature=null
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=removeAll,descriptor=(Ljava/util/Collection;)Z,signature=(Ljava/util/Collection<*>;)Z
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=retainAll,descriptor=(Ljava/util/Collection;)Z,signature=(Ljava/util/Collection<*>;)Z
        [Andoter]:visitMethod：access=ACC_PRIVATE ,name=batchRemove,descriptor=(Ljava/util/Collection;Z)Z,signature=(Ljava/util/Collection<*>;Z)Z
        [Andoter]:visitMethod：access=ACC_PRIVATE ,name=writeObject,descriptor=(Ljava/io/ObjectOutputStream;)V,signature=null
        [Andoter]:visitMethod：access=ACC_PRIVATE ,name=readObject,descriptor=(Ljava/io/ObjectInputStream;)V,signature=null
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=listIterator,descriptor=(I)Ljava/util/ListIterator;,signature=(I)Ljava/util/ListIterator<TE;>;
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=listIterator,descriptor=()Ljava/util/ListIterator;,signature=()Ljava/util/ListIterator<TE;>;
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=iterator,descriptor=()Ljava/util/Iterator;,signature=()Ljava/util/Iterator<TE;>;
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=subList,descriptor=(II)Ljava/util/List;,signature=(II)Ljava/util/List<TE;>;
        [Andoter]:visitMethod：access=ACC_STATIC ,name=subListRangeCheck,descriptor=(III)V,signature=null
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=forEach,descriptor=(Ljava/util/function/Consumer;)V,signature=(Ljava/util/function/Consumer<-TE;>;)V
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=spliterator,descriptor=()Ljava/util/Spliterator;,signature=()Ljava/util/Spliterator<TE;>;
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=removeIf,descriptor=(Ljava/util/function/Predicate;)Z,signature=(Ljava/util/function/Predicate<-TE;>;)Z
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=replaceAll,descriptor=(Ljava/util/function/UnaryOperator;)V,signature=(Ljava/util/function/UnaryOperator<TE;>;)V
        [Andoter]:visitMethod：access=ACC_PUBLIC ,name=sort,descriptor=(Ljava/util/Comparator;)V,signature=(Ljava/util/Comparator<-TE;>;)V
        [Andoter]:visitMethod：access=ACC_STATIC ACC_SYNTHETIC ,name=access$000,descriptor=(Ljava/util/ArrayList;)I,signature=null
        [Andoter]:visitMethod：access=ACC_STATIC ,name=<clinit>,descriptor=()V,signature=null
        [Andoter]:visitEnd
         */
    }

}
