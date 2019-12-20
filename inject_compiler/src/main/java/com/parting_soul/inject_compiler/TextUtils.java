package com.parting_soul.inject_compiler;

/**
 * @author parting_soul
 * @date 2019-12-19
 */
public class TextUtils {
    public static boolean equal(String a, String b) {
        if (a == b) return true;
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }

    public static boolean isEmpty(String text) {
        if (text == null) {
            return true;
        }
        return text.isEmpty();
    }
}
