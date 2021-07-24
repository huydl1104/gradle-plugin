package com.example.asm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yudongliang
 * create time 2021-07-24
 * describe : 自定义注解
 */
@Target({ElementType.TYPE,ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyNewAnnotation{
    String name();
    String website() default "hello";
    int revision() default 1;
}