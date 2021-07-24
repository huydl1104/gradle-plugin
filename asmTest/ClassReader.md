# ClassReader
用于读取类文件的字节码，通过 accept 方法委托给 ClassVisitor 解析，重要的 options
1、ClassReader.SKIP_DEBUG：表示跳过调试内容，即：SourceFile(跳过源文件)、SourceDebugExtension（源码调试扩展）、
LocalVariableTable(局部变量表)、LocalVariableTypeObject(局部变量类型表)、LineNumberTable(行号表属性)、
同时一下的方法 不会解析也不会被访问：ClassVisitor.visitResource，MethodVisitor.visitLocalVariable，MethodVisitor.visitLineNumber
使用这个flag后类文件的调试信息会被移除！！！
2、ClassReader.SHIP_CODE：跳过代码（attributes代码属性将不会被转换和访问，方法体代码不会进行解析和访问）。
3、ClassReader.SKIP_FRAMES：跳过 StackMap(栈图)和StackMapTable(栈图表)，即 MethodVisitor.visitFrames方法不会转换和访问
当使用 CLassWriter.SKIP_FRAMES 时，避免了访问帧内容，这些内容会被忽略和重新计算，无需访问。
4、ClassReader.EXPAND_FRAMES：表示扩展栈帧图，默认栈图以它们的原始格式(V1_6以下使用扩展格式。其他使用压缩格式)被访问，
若是设置了该标识，栈图则始终以扩展方式进行访问(此标识在 ClassReader 和 ClassWriter 中增加了解压/压缩步骤，会大幅度降低性能)