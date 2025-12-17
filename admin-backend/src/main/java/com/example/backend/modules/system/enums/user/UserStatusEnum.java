package com.example.backend.modules.system.enums.user;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 用户状态枚举
 * system_user.status
 *
 * @since 2025-12-12
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {

    NORMAL("normal", "正常（可用）", null),
    LOCKED("locked", "锁定（禁用）","当前账号已被锁定，无法登录"),
    DISABLED("disabled", "停用","当前账号已被停用，无法登录"),
    EXPIRED("expired", "过期","账号已过期，无法登录"),
    ;

    @EnumValue
    @NotNull
    final String code;

    @NotNull
    final String name;

    @Nullable
    final String failedMessage;

}
