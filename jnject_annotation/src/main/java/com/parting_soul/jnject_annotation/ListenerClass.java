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
@Target({ElementType.ANNOTATION_TYPE})
public @interface ListenerClass {

    String targetType();

    String setter();

    ListenerMethod[] method();

    String type();
}
