package com.example.backend;

import com.example.backend.common.utils.NumberUtils;

public class NumberParseUtilsTest {
    public static void main(String[] args) {
        System.out.println("Long.getLong");
        System.out.println(Long.getLong("7"));  // null
        System.out.println(Long.getLong("2"));  // null
        System.out.println(Long.getLong("3L"));  // null
        System.out.println(Long.getLong("-1L")); // null
        System.out.println(Long.getLong("1L"));  // null
        System.out.println(Long.getLong("java.lang.Long"));  // null
        System.out.println(Long.getLong("sun.arch.data.model"));  // 64

        // long
        System.out.println("Long.parseLong");
        System.out.println(Long.parseLong("-1")); // -1
        System.out.println(Long.parseLong("3"));  // 3
        // System.out.println(Long.parseLong("5L"));
        // Exception in thread "main" java.lang.NumberFormatException: For input string: "5L"

        // Long
        System.out.println("Long.valueOf");
        System.out.println(Long.valueOf("-1"));   // -1
        System.out.println(Long.valueOf("3"));    // 3
        // System.out.println(Long.valueOf("5L"));
        // Exception in thread "main" java.lang.NumberFormatException: For input string: "5L"
        System.out.println(Long.valueOf("011")); // 输出11
        // System.out.println(Long.valueOf("#11")); // 出错
        // System.out.println(Long.valueOf("0X11"));// 出错
        // System.out.println(Long.valueOf("0x11"));// 出错
        System.out.println(Long.valueOf("11"));  // 输出11

        System.out.println("Long.decode");
        System.out.println(Long.decode("-1"));  // -1
        System.out.println(Long.decode("3"));   // 3
        // System.out.println(Long.decode("5L"));
        // Exception in thread "main" java.lang.NumberFormatException: For input string: "5L"
        System.out.println(Long.decode("011")); // 8进制，输出9
        System.out.println(Long.decode("#11")); // 16进制，输出17
        System.out.println(Long.decode("0X11"));// 16进制，输出17
        System.out.println(Long.decode("0x11"));// 16进制，输出17
        System.out.println(Long.decode("11"));  // 10进制，输出11

        System.out.println("Long.parseUnsignedLong");
        // System.out.println(Long.parseUnsignedLong("-1"));
        // Exception in thread "main" java.lang.NumberFormatException: Illegal leading minus sign on unsigned string -1.
        System.out.println(Long.parseUnsignedLong("3"));  // 3
        // System.out.println(Long.parseUnsignedLong("5L"));
        // Exception in thread "main" java.lang.NumberFormatException: For input string: "5L"

        System.out.println("NumberParseUtils.parseLong");
        System.out.println(NumberUtils.parseLong("1"));
        System.out.println(NumberUtils.parseLong(""));
        System.out.println(NumberUtils.parseLong("0"));
        System.out.println(NumberUtils.parseLong("3487215432148412423")); // 19位，可以正常解析
        System.out.println(NumberUtils.parseLong("34872154321484124234")); // 20位，返回null
        // 为小数值时，String类型不能转为Long类型
        System.out.println(NumberUtils.parseLong("0.5"));
        System.out.println(NumberUtils.parseLong("-3.2L"));
        System.out.println(NumberUtils.parseLong("abc"));
        System.out.println(NumberUtils.parseLong(null));

        // 整数类型
        Byte.parseByte("1");
        Short.parseShort("1");
        Integer.parseInt("1");
        Long.parseLong("1");
        // 小数类型
        Float.parseFloat("1.0");
        Double.parseDouble("1.0");
    }
}
