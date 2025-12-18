package com.example.backend.common.utils;

import java.lang.reflect.Field;

public class CommonUtils {

    public static Double getValueByFieldName(Object obj, String fieldName) {
        try {
            Class<?> aClass = obj.getClass();
            Field field = aClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object o = field.get(obj);
            if (o == null) {
                return null;
            }
            return Double.parseDouble(String.valueOf(o));
        } catch (NoSuchFieldException e) {
            // 没有对应列
            // throw new RuntimeException(e);
            System.out.println("没有对应列");
            return null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            // 无法转换为 Double 类型
            // throw new RuntimeException(e);
            System.out.println("无法转换为 Double 类型");
            return null;
        }
    }

    /**
     * 转换为指定位数的字符串，前面不够补0
     */
    public static String ToFixedString(Long number, Integer count) {
        String strNumber = Long.toString(number);
        int paddingLength = count - strNumber.length();
        return "0".repeat(Math.max(0, paddingLength)) + strNumber;
    }

}
