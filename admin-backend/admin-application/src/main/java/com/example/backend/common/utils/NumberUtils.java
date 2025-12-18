package com.example.backend.common.utils;

public class NumberUtils {
    /**
     * Long 转 String 工具类
     * <p>
     * 非数字类型字符串getLong之后为null，不会报错
     *
     * @param str
     * @return 转换失败时不报错，返回 null
     */
    public static Long parseLong(String str) {
        return parseLong(str, null);
    }

    /**
     * Long 转 String 工具类
     * <p>
     * 非数字类型字符串getLong之后为null，不会报错
     *
     * @param str
     * @return 转换失败时不报错，返回 defaultValue
     */
    public static Long parseLong(String str, Long defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Double 转 String 工具类
     * <p>
     * 非数字类型字符串getDouble之后为null，不会报错
     *
     * @param str
     * @return 转换失败时不报错，返回 null
     */
    public static Double parseDouble(String str) {
        return parseDouble(str, null);
    }

    /**
     * Double 转 String 工具类
     * <p>
     * 非数字类型字符串getDouble之后为null，不会报错
     *
     * @param str
     * @return 转换失败时不报错，返回 defaultValue
     */
    public static Double parseDouble(String str, Double defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Double parseDouble(Integer integer) {
        return parseDouble(integer, null);
    }

    /**
     * Double 转 Integer 工具类
     */
    public static Double parseDouble(Integer integer, Double defaultValue) {
        if (integer == null) {
            return defaultValue;
        }
        return Double.valueOf(integer);
    }
}
