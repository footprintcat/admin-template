package com.example.backend.common.utils;

/**
 * 数字计算工具类
 *
 * @author 张博凯
 * @since 2024.07.18
 */
public class NumberCalcUtils {

    /**
     * 判断两 int 值相加是否会溢出
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean mightAddOverflow(int a, int b) {
        if (a > 0 && b > 0 && a > Integer.MAX_VALUE - b) {
            // 两正数相加后溢出
            return true;
        }
        if (a < 0 && b < 0 && a < Integer.MIN_VALUE - b) {
            // 两负数相加后溢出
            return true;
        }
        return false;
    }

    /**
     * 判断两 int 值相加，若溢出则取最大/最小值
     *
     * @param a
     * @param b
     * @return
     */
    public static int addWithoutOverflow(int a, int b) {
        if (a > 0 && b > 0 && a > Integer.MAX_VALUE - b) {
            // 两正数相加后溢出
            return Integer.MAX_VALUE;
        }
        if (a < 0 && b < 0 && a < Integer.MIN_VALUE - b) {
            // 两负数相加后溢出
            return Integer.MIN_VALUE;
        }
        return a + b;
    }
}
