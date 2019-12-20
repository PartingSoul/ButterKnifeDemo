package com.parting_soul.jnject_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author parting_soul
 * @date 2019-12-19
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface ViewInject {
    int value();
}
