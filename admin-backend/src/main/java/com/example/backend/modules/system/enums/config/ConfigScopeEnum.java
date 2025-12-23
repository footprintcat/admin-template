package com.example.backend.modules.system.enums.config;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * 系统类型枚举
 * system_config.scope
 *
 * @since 2025-12-24
 */
@Getter
@AllArgsConstructor
@Schema(description = "系统类型枚举（backend：后端业务系统；admin：后台管理系统）")
public enum ConfigScopeEnum {

    @Schema(description = "后端")
    BACKEND("backend", "后端"),

    @Schema(description = "后台管理")
    MANAGEMENT("management", "后台管理"),

    @Schema(description = "App")
    APP("app", "App"),

    ;

    @EnumValue
    @NotNull
    final String code;

    @NotNull
    final String name;

}
