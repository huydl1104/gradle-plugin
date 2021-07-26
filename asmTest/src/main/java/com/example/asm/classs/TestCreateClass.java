package com.example.asm.classs;

import com.example.asm.lifecycle.ClassPrintVisitor;
import com.example.asm.utils.ClassOutputUtil;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.IOException;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.V1_8;

/**
 * @author yudongliang
 * create time 2021-07-22
 * describe : 检查 class 文件
 */
public class TestCreateClass {

    public static void main(String[] args) {
        TestCreateClass main = new TestCreateClass();
        main.exe2();
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

    private void exe2(){
        ClassOutputUtil.byte2File("asmTest/build/asm/Person.class", getPersonBytecode());
    }

    public static byte[] getPersonBytecode() {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(V1_8, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, "pkg/Person", null, "java/lang/Object", null);
        cw.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL, "TAG", "Ljava/lang/String;", null, "Person").visitEnd();
        cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "getAge", "()I", null, null).visitEnd();
        cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "setName", "(Ljava/lang/String;)V", null, null).visitEnd();
        cw.visitEnd();
        return cw.toByteArray();
    }

    /**
     * 解析 ArrayList
     */
    private void exe3(){
        try {
            ClassReader reader = new ClassReader("java/util/ArrayList");
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
