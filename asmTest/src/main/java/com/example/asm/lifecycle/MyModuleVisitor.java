package com.example.asm.lifecycle;

import com.example.asm.utils.LogUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ModuleVisitor;

import java.util.Arrays;

import kotlin.jvm.internal.Intrinsics;

/**
 * @author yudongliang
 * create time 2021-07-22
 * describe :
 */
public class MyModuleVisitor extends ModuleVisitor {
    public MyModuleVisitor(int api) {
        super(api);
    }

    public MyModuleVisitor(int api, ModuleVisitor moduleVisitor) {
        super(api, moduleVisitor);
    }


    public void visitExport(@Nullable String packaze, int access, @NotNull String... modules) {
        Intrinsics.checkParameterIsNotNull(modules, "modules");
        super.visitExport(packaze, access, (String[]) Arrays.copyOf(modules, modules.length));
        LogUtils.INSTANCE.info("visitExport");
    }

    public void visitMainClass(@Nullable String mainClass) {
        super.visitMainClass(mainClass);
        LogUtils.INSTANCE.info("visitMainClass");
    }

    public void visitOpen(@Nullable String packaze, int access, @NotNull String... modules) {
        Intrinsics.checkParameterIsNotNull(modules, "modules");
        super.visitOpen(packaze, access, (String[])Arrays.copyOf(modules, modules.length));
        LogUtils.INSTANCE.info("visitOpen");
    }

    public void visitPackage(@Nullable String packaze) {
        super.visitPackage(packaze);
        LogUtils.INSTANCE.info("visitPackage");
    }

    public void visitProvide(@Nullable String service, @NotNull String... providers) {
        Intrinsics.checkParameterIsNotNull(providers, "providers");
        super.visitProvide(service, (String[])Arrays.copyOf(providers, providers.length));
        LogUtils.INSTANCE.info("visitProvide");
    }

    public void visitRequire(@Nullable String module, int access, @Nullable String version) {
        super.visitRequire(module, access, version);
        LogUtils.INSTANCE.info("visitRequire");
    }

    public void visitUse(@Nullable String service) {
        super.visitUse(service);
        LogUtils.INSTANCE.info("visitUse");
    }

}
