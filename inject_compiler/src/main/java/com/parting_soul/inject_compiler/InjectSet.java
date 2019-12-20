package com.parting_soul.inject_compiler;

import com.parting_soul.jnject_annotation.ListenerClass;
import com.parting_soul.jnject_annotation.ListenerMethod;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;

/**
 * @author parting_soul
 * @date 2019-12-19
 */
public class InjectSet {
    private static final ClassName CLASS_NAME_ACTIVITY = ClassName.get("android.app", "Activity");
    private static final ClassName CLASS_NAME_VIEW = ClassName.get("android.view", "View");
    private ClassName mTargetClassName;
    private Map<Integer, ViewInjectEntity> mViewInjectEntityMap;

    public JavaFile generatorJavaFile() {
        return JavaFile.builder(mTargetClassName.packageName(), createType())
                .build();
    }

    /**
     * 创建类
     *
     * @return
     */
    private TypeSpec createType() {
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(getInjectClassName());
        typeSpecBuilder.addMethod(createConstructor())
                .addModifiers(Modifier.PUBLIC);
        return typeSpecBuilder.build();
    }

    /**
     * 创建构造方法
     *
     * @return
     */
    private MethodSpec createConstructor() {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder();
        builder.addParameter(mTargetClassName, "target")
                .addModifiers(Modifier.PUBLIC);

        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        codeBlockBuilder.add("$T view;\n\n", CLASS_NAME_VIEW);

        if (mViewInjectEntityMap != null) {
            for (Map.Entry<Integer, ViewInjectEntity> entry : mViewInjectEntityMap.entrySet()) {
                ViewInjectEntity inject = entry.getValue();
                int id = entry.getKey();

                codeBlockBuilder.add("view = target.findViewById($L);", id)
                        .add("\n");

                FieldInject fieldInject = inject.mFieldInject;
                if (fieldInject != null) {
                    codeBlockBuilder.add("target.$L = ($T)view;", fieldInject.name, fieldInject.typeName)
                            .add("\n");
                }

                List<MethodInject> methodInjects = inject.mMethodInjects;
                if (methodInjects != null) {
                    for (MethodInject methodInject : methodInjects) {
                        addMethod(codeBlockBuilder, methodInject);
                    }
                }

                codeBlockBuilder.add("\r\n");
            }
            builder.addCode(codeBlockBuilder.build());
        }
        return builder.build();
    }

    /**
     * 添加方法
     *
     * @param codeBlockBuilder
     * @param methodInject
     */
    private void addMethod(CodeBlock.Builder codeBlockBuilder, MethodInject methodInject) {
        ListenerClass listenerClass = methodInject.listenerClass;
        ListenerMethod[] methods = listenerClass.method();

        TypeSpec.Builder callbackBuilder = TypeSpec.anonymousClassBuilder("")
                .superclass(ClassName.bestGuess(listenerClass.type()));
        for (ListenerMethod method : methods) {
            MethodSpec.Builder spec = MethodSpec.methodBuilder(method.name())
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class);

            //返回值
            if (TextUtils.isEmpty(method.defaultReturn())) {
                spec.returns(ClassName.bestGuess(method.defaultReturn()));
            }

            //添加方法形参
            String[] params = method.parameters();
            for (int i = 0; i < params.length; i++) {
                String param = params[i];
                spec.addParameter(
                        ParameterSpec.builder(ClassName.bestGuess(param), "param" + i).build()
                );
            }

            //添加方法体
            spec.addStatement("target.$L()", methodInject.name);

            callbackBuilder.addMethod(spec.build());
        }

        codeBlockBuilder.add("view.$L($L);", listenerClass.setter(), callbackBuilder.build())
                .add("\n");
    }

    /**
     * 获取生成inject类的名字
     *
     * @return
     */
    private String getInjectClassName() {
        return mTargetClassName.simpleName() + "_Inject";
    }

    public InjectSet setTargetClassName(ClassName className) {
        this.mTargetClassName = className;
        return this;
    }

    public InjectSet addViewInject(Integer id, ViewInjectEntity inject) {
        if (mViewInjectEntityMap == null) {
            mViewInjectEntityMap = new HashMap<>();
        }
        mViewInjectEntityMap.put(id, inject);
        return this;
    }

    public Map<Integer, ViewInjectEntity> getViewInjectEntityMap() {
        return mViewInjectEntityMap;
    }
}
