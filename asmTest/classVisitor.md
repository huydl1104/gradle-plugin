# classWriter
ClassWriter类并不会校验其调用的方法顺序是否恰当和参数是否有效，有可能会产生一些被Java虚拟机拒绝的无效的类，
为了能欧提前的检测出这些问题，可以使用 CheckClassAdapter 、TraceClassVisitor 扩展了ClassVisitor，其方法的
调用都委托给另一个 ClassVisitor ,用来生成asm和改变字节码
比如：一个 TraceClassVisitor 或者 ClassWriter ，这两个类并不会打印所访问类的文本，而是验证其方法的调用顺序是否合适，
参数是否有效，然后才会委托给另一个访问器，当发生错误是会抛出 IllegalStateException 或 IllegalArgumentException。
## 常用方法的执行顺序
注：visit，visitEnd必须调用一次，[]表示最多调用一次；()*表示()里面的访问可以按照排列顺序调用多次；

```
visit 
[visitSource] [visitModule] [visitNestHost] [visitOuterClass]
(visitAnnotation | visitTypeAnnotation|visitAttribute)*
(visitNestMember|visitInnerClass|visitField| visitMethod)* 
visitEnd;
```
## 常用方法详解
visit:访问类的头部。
```
public void visit(final int version,final int access,  final String name,  final String signature, final String superName, final String[] interfaces)｛
    if (cv != null) {
        cv.visit(version, access, name, signature, superName, interfaces);
    }}；
其中version指的是类的版本；
acess指的是类的修饰符；
name类的名称；
signature类的签名，如果类不是泛型或者没有继承泛型类，那么signature为空；
superName类的父类名称；
```
visitSource: 访问类的源码，就是.java文件,一般情况用不上
```
  public void visitSource(final String source, final String debug) {
    if (cv != null) {
      cv.visitSource(source, debug);
    }
  }
```
visitAnnotation:访问类的注解。
```
  public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
    if (cv != null) {
      return cv.visitAnnotation(descriptor, visible);
    }
    return null;
  }
descriptor:表示类注解类的描述；
visible表示该注解是否运行时可见；
return AnnotationVisitor:表示该注解类的Visitor，可以用来访问注解值；
```
visitTypeAnnotation:访问类的签名类型(某个泛型)的注解.
```
 public AnnotationVisitor visitTypeAnnotation(
      final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
    if (api < Opcodes.ASM5) {
      throw new UnsupportedOperationException("This feature requires ASM5");
    }
    if (cv != null) {
      return cv.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }
    return null;
  }
typeRef:指的是类型引用,在这里只能是TypeReference.(CLASS_TYPE_PARAMETER |CLASS_TYPE_PARAMETER_BOUND|CLASS_EXTENDS )；
typePath:被注解的类型参数，wildcard bound,array element type,包含typeRef的static inner type;
descriptor: 注解类的描述；
visible:该注解类型运行时是否可见;
```
visitAttribute:访问类的非标准属性.
```
  public void visitAttribute(final Attribute attribute) {
    if (cv != null) {
      cv.visitAttribute(attribute);
    }
  }
```
visitInnerClass:访问一个内部类的信息.
visitField:访问一个类的域信息，如果需要修改或者新增一个域，可以通过重写此方法.
```
  public FieldVisitor visitField( final int access, final String name, final String descriptor,final String signature,
      final Object value) {
    if (cv != null) {
      return cv.visitField(access, name, descriptor, signature, value);
    }
    return null;
  }
access：表示该域的访问方式，public，private或者static,final等等；
name：指的是域的名称；
descriptro:域的描述,一般指的是该field的参数类型;
signature:指的是域的签名，一般是泛型域才会有签名;
value:指的该域的初始值
reture FiedVisitor:表示将返回一个可以访问该域注解和属性的访问对象，如果不感兴趣的话，可以设置为空;
```
visitMethod:访问类的方法，如果需要修改类方法信息，则可以重写此方法.
```
  public MethodVisitor visitMethod( final int access,final String name,final String descriptor,final String signature, final String[] exceptions) {
    if (cv != null) {
      return cv.visitMethod(access, name, descriptor, signature, exceptions);
    }
    return null;
  }
accessL:访问修饰符 如：public static final 
name:方法的名称
decsriptor:表示方法的参数类型和返回值类型；
```
visitEnd:访问类的尾部，只有当类访问结束时，才能调用该方法，同时必须调用该方法.