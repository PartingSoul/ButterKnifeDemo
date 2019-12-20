package com.parting_soul.jnject_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author parting_soul
 * @date 2019-12-19
 */
@ListenerClass(
        targetType = "android.view.View",
        type = "android.view.View.OnClickListener",
        setter = "setOnClickListener",
        method = {
                @ListenerMethod(
                        name = "onClick",
                        parameters = {"android.view.View"}
                )
        }
)
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface OnClick {
    int value();
}
