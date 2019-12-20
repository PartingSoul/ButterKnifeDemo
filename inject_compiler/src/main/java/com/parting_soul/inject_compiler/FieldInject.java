package com.parting_soul.inject_compiler;

import com.squareup.javapoet.TypeName;

/**
 * @author parting_soul
 * @date 2019-12-19
 */
public class FieldInject {
    String name;
    int id;
    TypeName typeName;

    public FieldInject(String name, int id, TypeName typeName) {
        this.name = name;
        this.id = id;
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "FieldInject{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", typeName=" + typeName +
                '}';
    }
}
