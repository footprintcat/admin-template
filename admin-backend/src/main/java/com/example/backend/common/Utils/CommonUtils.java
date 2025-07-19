package com.example.backend.common.Utils;

import java.lang.reflect.Field;

public class CommonUtils {

    public static Double getValueByFeildName(Object obj, String fieldName) {
        try {
            Class aClass = obj.getClass();
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
}
