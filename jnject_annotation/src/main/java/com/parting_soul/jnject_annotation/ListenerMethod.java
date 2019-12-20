package com.parting_soul.jnject_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author parting_soul
 * @date 2019-12-20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ListenerMethod {
    String name();

    String[] parameters() default {};

    String returnType() default "void";

    String defaultReturn() default "null";
}
