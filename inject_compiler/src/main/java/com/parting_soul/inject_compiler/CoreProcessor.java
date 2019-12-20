package com.parting_soul.inject_compiler;

import com.google.auto.service.AutoService;
import com.parting_soul.jnject_annotation.ListenerClass;
import com.parting_soul.jnject_annotation.OnClick;
import com.parting_soul.jnject_annotation.ViewInject;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * @author parting_soul
 * @date 2019-12-19
 */
@AutoService(Processor.class)
public class CoreProcessor extends AbstractProcessor {

    public static final String VIEW_TYPE = "android.view.View";

    public static final String ACTIVITY_TYPE = "android.app.Activity";

    Messager mMessager;

    Filer mFiler;

    Elements mElements;

    Types mTypes;


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnvironment.getMessager();
        mFiler = processingEnvironment.getFiler();
        mElements = processingEnvironment.getElementUtils();
        mTypes = processingEnvironment.getTypeUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> sets = new HashSet<>();
        sets.add(ViewInject.class.getCanonicalName());
        sets.add(OnClick.class.getCanonicalName());
        return sets;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Map<TypeElement, InjectSet> map = new HashMap<>();

        //解析注解
        parseAnnotation(map, roundEnvironment);

        //生成Java文件
        for (Map.Entry<TypeElement, InjectSet> entry : map.entrySet()) {
            InjectSet injectSet = entry.getValue();

            JavaFile javaFile = injectSet.generatorJavaFile();
            try {
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    private void parseAnnotation(Map<TypeElement, InjectSet> map, RoundEnvironment environment) {

        //解析ViewInject注解
        for (Element element : environment.getElementsAnnotatedWith(ViewInject.class)) {
            parseViewInject(map, element);
        }

        //解析OnClick注解
        for (Element element : environment.getElementsAnnotatedWith(OnClick.class)) {
            parseMethod(map, element);
        }

    }

    private void parseMethod(Map<TypeElement, InjectSet> map, Element element) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        ExecutableElement executableElement = (ExecutableElement) element;

        String name = executableElement.getSimpleName().toString();
        OnClick click = element.getAnnotation(OnClick.class);
        int id = click.value();
        ListenerClass listenerClass = OnClick.class.getAnnotation(ListenerClass.class);
        MethodInject methodInject = new MethodInject(name, listenerClass);

        InjectSet injectSet = map.get(typeElement);
        if (injectSet == null) {
            injectSet = new InjectSet();
            injectSet.setTargetClassName(ClassName.get(typeElement));
            map.put(typeElement, injectSet);
        }
        Map<Integer, ViewInjectEntity> injectEntityMap = injectSet.getViewInjectEntityMap();
        ViewInjectEntity entity;
        if (injectEntityMap == null || injectEntityMap.get(id) == null) {
            entity = new ViewInjectEntity();
        } else {
            entity = injectEntityMap.get(id);
        }
        entity.addMethod(methodInject);
        injectSet.addViewInject(id, entity);
    }

    private void parseViewInject(Map<TypeElement, InjectSet> map, Element element) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        TypeMirror typeMirror = element.asType();
        if (typeMirror.getKind() != TypeKind.DECLARED) {
            return;
        }
        if (!isSubTypeOfType(typeMirror, VIEW_TYPE)) {
            //被注解修饰的不是View的子类
            log("error");
            return;
        }

        ViewInject inject = element.getAnnotation(ViewInject.class);
        int id = inject.value();
        String name = element.getSimpleName().toString();
        TypeName typeName = TypeName.get(typeMirror);
        FieldInject fieldInject = new FieldInject(name, id, typeName);

        InjectSet injectSet = map.get(typeElement);
        if (injectSet == null) {
            injectSet = new InjectSet();
            injectSet.setTargetClassName(ClassName.get(typeElement));
            map.put(typeElement, injectSet);
        }
        Map<Integer, ViewInjectEntity> injectEntityMap = injectSet.getViewInjectEntityMap();
        ViewInjectEntity entity;
        if (injectEntityMap == null || injectEntityMap.get(id) == null) {
            entity = new ViewInjectEntity();
        } else {
            entity = injectEntityMap.get(id);
        }
        entity.setFieldInject(fieldInject);
        injectSet.addViewInject(id, entity);
    }


    /**
     * 判断当前类是否为指定类的子类
     *
     * @param typeMirror
     * @param type
     * @return
     */
    private boolean isSubTypeOfType(TypeMirror typeMirror, String type) {
        if (TextUtils.equal(typeMirror.toString(), type)) {
            return true;
        }
        if (typeMirror.getKind() != TypeKind.DECLARED) {
            return false;
        }
        DeclaredType declaredType = (DeclaredType) typeMirror;
        TypeElement typeElement = (TypeElement) declaredType.asElement();
        TypeMirror superType = typeElement.getSuperclass();
        return isSubTypeOfType(superType, type);
    }

    void log(String msg) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

}
