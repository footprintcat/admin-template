package com.example.backend.common.Enums.system;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统日志类型枚举
 * system_log.type
 *
 * @since 2025-12-12
 */
@Getter
@AllArgsConstructor
public enum SystemLogTypeEnum {

    // 操作类日志
    OPERATION("OPERATION", "操作日志"),
    LOGIN("LOGIN", "登录日志"),
    LOGOUT("LOGOUT", "登出日志"),
    CREATE("CREATE", "新增日志"),
    UPDATE("UPDATE", "更新日志"),
    DELETE("DELETE", "删除日志"),
    IMPORT("IMPORT", "导入日志"),
    EXPORT("EXPORT", "导出日志"),
    DOWNLOAD("DOWNLOAD", "下载日志"),
    UPLOAD("UPLOAD", "上传日志"),

    // 系统类日志
    SYSTEM("SYSTEM", "系统日志"),
    STARTUP("STARTUP", "启动日志"),
    SHUTDOWN("SHUTDOWN", "关闭日志"),
    CONFIG_CHANGE("CONFIG_CHANGE", "配置变更日志"),

    // 安全类日志
    SECURITY("SECURITY", "安全日志"),
    AUTH("AUTH", "认证日志"),
    PERMISSION("PERMISSION", "权限日志"),
    ACCESS("ACCESS", "访问日志"),

    // 业务类日志
    // BUSINESS("BUSINESS", "业务日志"),
    // ORDER("ORDER", "订单日志"),
    // PAYMENT("PAYMENT", "支付日志"),
    // REFUND("REFUND", "退款日志"),

    // 错误与监控
    ERROR("ERROR", "错误日志"),
    WARN("WARN", "警告日志"),
    DEBUG("DEBUG", "调试日志"),
    INFO("INFO", "信息日志"),
    TRACE("TRACE", "跟踪日志"),
    PERFORMANCE("PERFORMANCE", "性能日志"),

    // 其他
    AUDIT("AUDIT", "审计日志"),
    BACKUP("BACKUP", "备份日志"),
    SYNC("SYNC", "同步日志"),
    NOTIFICATION("NOTIFICATION", "通知日志"),

    // 默认
    UNKNOWN("UNKNOWN", "未知类型");

    ;

    @EnumValue
    final String code;
    final String name;

}
