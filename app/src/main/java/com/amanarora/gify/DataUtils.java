package com.amanarora.gify;

public class DataUtils {
    private DataUtils(){}

    public static boolean isNotNull(Object... objects) {
        if (objects != null) {
            for (Object object : objects) {
                if (object == null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isNotNullOrEmpty(String str) {
        return !isNullOrEmpty(str);
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
