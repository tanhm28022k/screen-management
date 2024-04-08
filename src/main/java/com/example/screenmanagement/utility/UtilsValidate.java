package com.example.screenmanagement.utility;

import lombok.extern.log4j.Log4j2;
import org.slf4j.LoggerFactory;

import java.util.Collection;

@Log4j2
public class UtilsValidate {

    private UtilsValidate() {
        throw new IllegalStateException("Utils class");
    }
    /**
     * Kiem tra object co null hay khong
     *
     * @param objects
     * @return
     */
    public static boolean isNullOrEmpty(final Object[] objects) {
        return objects == null || objects.length == 0;
    }

    /**
     * Kiem tra chuoi String null or rong
     *
     * @param toTest
     * @return
     */
    public static boolean isNullOrEmpty(String toTest) {
        return toTest == null || toTest.isEmpty();
    }

    /**
     * Kiem tra object co null hay khong
     *
     * @param obj
     * @return
     */
    public static boolean isNullObject(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return isNullOrEmpty(obj.toString());
        } else if (obj instanceof Long) {
            return Long.valueOf(obj.toString()) <= 0L;
        } else if (obj instanceof Integer) {
            return Integer.valueOf(obj.toString()) <= 0;
        }
        return false;
    }

    /**
     * Kiem tra list null or empty
     *
     * @param collection
     * @return
     */
    public static boolean isNullOrEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotBlank(String str) {
        return !isNullOrEmpty(str);
    }
}
