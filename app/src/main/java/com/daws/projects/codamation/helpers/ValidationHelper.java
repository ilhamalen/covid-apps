package com.daws.projects.codamation.helpers;

import java.util.Collection;

public class ValidationHelper {

    public static boolean isCollectionNotEmpty(Collection c) {
        return (c != null && !c.isEmpty());
    }

    public static boolean checkNotNullOrEmpty(String string) {
        return (string != null && !string.contains("null") && !string.isEmpty());
    }

    public static boolean checkNotNullOrEmpty(Integer integer) {
        return (integer != null);
    }

    public static boolean checkNotNullOrEmpty(Double doubleValue) {
        return (doubleValue != null);
    }

    public static boolean checkNotNullOrEmpty(Long longVariable){
        return longVariable != null;
    }
}
