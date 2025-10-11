package com.example.backend.common.ManageList.enums;

/**
 * 查询类型枚举
 * 用于定义表格查询条件的类型
 */
public enum QueryType {
    // 字符串类型
    STRING,
    // 字符串多选
    STRING_MULTIPLE,
    // 布尔类型
    BOOLEAN,
    // 下拉框单选
    DROPDOWN,
    // 下拉框多选
    DROPDOWN_MULTIPLE,
    // 精确日期时间
    DATETIME_EXACT,
    // 日期时间范围
    DATETIME_RANGE,
    // 精确数字
    NUMBER_EXACT,
    // 数字范围
    NUMBER_RANGE,
    // 2D地图位置
    MAP_LOCATION_2D,
    // 3D地图位置
    MAP_LOCATION_3D
}