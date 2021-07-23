package com.example.asm.addfield;

import com.example.asm.lifecycle.ClassPrintVisitor;
import com.example.asm.utils.LogUtils;
import com.example.asm.utils.ClassOutputUtil;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;

/**
 * @author yudongliang
 * create time 2021-07-22
 * describe : 测试添加字段类
 */
public class TestAddField {
    public static void main(String[] args) {
        TestAddField main = new TestAddField();
        main.exe1();
    }

    private void exe1(){
        try {
            ClassReader reader = new ClassReader("com/example/asm/testfile/Testfile");
            ClassWriter writer = new ClassWriter(reader,ClassWriter.COMPUTE_MAXS);
            reader.accept(new ClassVisitor(Opcodes.ASM9,writer) {//writer
                boolean isExists = false;
                @Override
                public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
//                    return new FiledVisitorPrinter(super.visitField(access, name, descriptor, signature, value));
                    LogUtils.INSTANCE.info("visitField name ->"+name +", descriptor ->"+descriptor+", value ->"+value);
                    if (name.equals("TAG")){
                        isExists = true;
                    }
                    return super.visitField(access, name, descriptor, signature, value);
                }

                @Override
                public void visitEnd() {
                    LogUtils.INSTANCE.info("visitField isExists ->"+isExists +", cv ->"+(cv == null));
                    if (!isExists){
                        cv.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC+Opcodes.ACC_FINAL,"TAG","Ljava/lang/String;",null,"TestFile").visitEnd();
                    }
                    super.visitEnd();
                }

            },0);


            System.out.println("===== 处理后的信息  ======");
            ClassPrintVisitor printVisitor = new ClassPrintVisitor(Opcodes.ASM8);
            ClassReader printReader = new ClassReader(writer.toByteArray());
            printReader.accept(printVisitor, 0);

            ClassOutputUtil.byte2File("asmTest/build/asm/AddFiled.class", writer.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
