package com.parting_soul.inject_compiler;

import com.parting_soul.jnject_annotation.ListenerClass;

/**
 * @author parting_soul
 * @date 2019-12-19
 */
public class MethodInject {
    String name;
    boolean hasReturnValue;
    ListenerClass listenerClass;

    public MethodInject(String name, ListenerClass listenerClass) {
        this.name = name;
        this.listenerClass = listenerClass;
    }

}
