package com.example.asm;

import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


public class ASMManager {

  public static void debugWrite(ClassWriter cw, String className) {
    debugWrite(cw.toByteArray(), className);
  }

  public static void debugWrite(byte[] bytecode, String className) {
    final File dir = new File("build/asm");
    if (!dir.exists()) {
      if (!dir.mkdirs()) {
        System.err.println("Create " + dir.getPath() + " failed!");
      }
    }
    try (FileOutputStream fos = new FileOutputStream(new File(dir, className + ".class"))) {
      fos.write(bytecode);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Object loadInstance(ClassWriter cw, Class<?> clazz) {
    return loadInstance(cw, clazz.getName());
  }

  public static Object loadInstance(ClassWriter cw, String className) {
    try {
      Class<?> clazz = loadClass(cw, className);
      return clazz.getDeclaredConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Class<?> loadClass(ClassWriter cw, String className) {
    return loadClass(cw.toByteArray(), className);
  }

  public static Class<?> loadClass(byte[] bytecode, String className) {
    AsmClassLoader classLoader = new AsmClassLoader();
    return classLoader.defineClass(className, bytecode);
  }

  private static class AsmClassLoader extends ClassLoader {
    public Class<?> defineClass(String name, byte[] b) {
      return defineClass(name, b, 0, b.length);
    }
  }
}
