package com.example.backend.common.Enums.system.log;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * 系统日志来源枚举
 * system_log.source
 *
 * @since 2025-12-12
 */
@Getter
@AllArgsConstructor
public enum SystemLogSourceEnum {

    BACKEND("backend", "后端日志"),
    MANAGE("manage", "管理端前端上报日志"),
    APP("app", "移动端上报日志"),
    ;

    @EnumValue
    @NotNull
    final String code;

    @NotNull
    final String name;

}
