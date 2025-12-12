package com.example.backend.common.Enums.system;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统日志来源枚举
 * system_log.source
 *
 * @since 2025-12-12
 */
@Getter
@AllArgsConstructor
public enum SystemLogSourceEnum {

    BACKEND("BACKEND", "后端日志"),
    MANAGE("MANAGE", "管理端前端上报日志"),
    APP("APP", "移动端上报日志"),
    ;

    @EnumValue
    final String code;
    final String name;

}
