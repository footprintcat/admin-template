package com.example.backend.common.ManageList.enums;

/**
 * 日期时间值类型枚举
 * 用于定义日期时间数据的值类型
 */
public enum DateValueType {
    // 毫秒级时间戳 (13位数字), 示例: 1696924800000
    TIMESTAMP_MILLISECOND,
    // 秒级时间戳 (10位数字), 示例: 1696924800
    TIMESTAMP_SECOND,
    // ISO 8601 标准格式, 示例: "2023-10-10T12:00:00Z"
    ISO_STRING,
    // 带时区的ISO格式, 示例: "2023-10-10T12:00:00+08:00"
    ISO_TIMEZONE_STRING,
    // 完整日期时间格式 (yyyy-MM-dd HH:mm:ss), 示例: "2023-10-10 12:00:00"
    DATE_TIME_STRING,
    // 仅日期格式 (yyyy-MM-dd), 示例: "2023-10-10"
    DATE_STRING,
    // 仅时间格式 (HH:mm:ss), 示例: "12:00:00"
    TIME_STRING,
    // 带毫秒的完整日期时间格式 (yyyy-MM-dd HH:mm:ss.SSS), 示例: "2023-10-10 12:00:00.000"
    DATE_TIME_MILLISECOND_STRING,
    // RFC 1123 格式 (常用于HTTP协议), 示例: "Tue, 10 Oct 2023 12:00:00 GMT"
    RFC_1123
}