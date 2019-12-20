package com.parting_soul.inject;

import android.app.Activity;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author parting_soul
 * @date 2019-12-19
 */
public class Inject {
    private static final Map<Class<?>, Constructor<?>> INJECTS = new HashMap<>();

    public static void inject(Activity activity) {
        try {
            Class<?> clazz = Class.forName(activity.getClass().getCanonicalName() + "_Inject");
            Constructor<?> constructor = INJECTS.get(clazz);
            if (constructor == null) {
                constructor = clazz.getConstructor(activity.getClass());
                INJECTS.put(clazz, constructor);
            }
            constructor.newInstance(activity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
