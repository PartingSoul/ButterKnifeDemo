package com.parting_soul.inject_compiler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author parting_soul
 * @date 2019-12-19
 */
public class ViewInjectEntity {
    FieldInject mFieldInject;
    List<MethodInject> mMethodInjects;

    public ViewInjectEntity setFieldInject(FieldInject inject) {
        this.mFieldInject = inject;
        return this;
    }

    public ViewInjectEntity addMethod(MethodInject inject) {
        if (mMethodInjects == null) {
            mMethodInjects = new ArrayList<>();
        }
        mMethodInjects.add(inject);
        return this;
    }

}
