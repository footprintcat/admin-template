package com.example.backend.modules.system.enums.log;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * 系统日志类型枚举
 * system_log.type
 *
 * @since 2025-12-12
 */
@Getter
@AllArgsConstructor
public enum LogTypeEnum {

    // 操作类日志
    OPERATION("operation", "操作日志"),
    LOGIN("login", "登录日志"),
    LOGOUT("logout", "登出日志"),
    CREATE("create", "新增日志"),
    UPDATE("update", "更新日志"),
    DELETE("delete", "删除日志"),
    IMPORT("import", "导入日志"),
    EXPORT("export", "导出日志"),
    DOWNLOAD("download", "下载日志"),
    UPLOAD("upload", "上传日志"),

    // 系统类日志
    SYSTEM("system", "系统日志"),
    STARTUP("startup", "启动日志"),
    SHUTDOWN("shutdown", "关闭日志"),
    CONFIG_CHANGE("configChange", "配置变更日志"),

    // 安全类日志
    SECURITY("security", "安全日志"),
    AUTH("auth", "认证日志"),
    PERMISSION("permission", "权限日志"),
    ACCESS("access", "访问日志"),

    // 业务类日志
    // BUSINESS("business", "业务日志"),
    // ORDER("order", "订单日志"),
    // PAYMENT("payment", "支付日志"),
    // REFUND("refund", "退款日志"),

    // 错误与监控
    ERROR("error", "错误日志"),
    WARN("warn", "警告日志"),
    DEBUG("debug", "调试日志"),
    INFO("info", "信息日志"),
    TRACE("trace", "跟踪日志"),
    PERFORMANCE("performance", "性能日志"),

    // 其他
    AUDIT("audit", "审计日志"),
    BACKUP("backup", "备份日志"),
    SYNC("sync", "同步日志"),
    NOTIFICATION("notification", "通知日志"),

    // 默认
    UNKNOWN("unknown", "未知类型");

    ;

    @EnumValue
    @NotNull
    final String code;

    @NotNull
    final String name;

}
