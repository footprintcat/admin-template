package com.example.backend.common.ManageList.enums;

/**
 * 日期时间显示类型枚举
 * 用于定义日期时间在前端的显示格式
 */
public enum DateDisplayType {
    // 原始数据格式（不推荐）
    RAW,
    // 展示毫秒级时间戳 (13位数字), 示例: 1696924800000
    TIMESTAMP_MILLISECOND,
    // 展示秒级时间戳 (10位数字), 示例: 1696924800
    TIMESTAMP_SECOND,
    // 展示为 2023-10-10 12:00:00 格式
    DATE_TIME,
    // 展示为 2023-10-10 格式
    DATE,
    // 展示为 12:00:00 格式
    TIME,
    // 展示为 2023-10-10 12:00:00.000 格式
    DATE_TIME_MILLISECOND
}