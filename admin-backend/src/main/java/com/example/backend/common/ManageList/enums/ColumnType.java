package com.example.backend.common.ManageList.enums;

/**
 * 列类型枚举
 * 用于定义表格列的类型
 */
public enum ColumnType {
    // 文本类型
    TEXT("text"),
    // 日期时间类型
    DATETIME("datetime"),
    // 图片类型
    IMAGE("image"),

    ;

    final String code;

    ColumnType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}