package com.example.backend.modules.system.enums.userauth;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * 用户认证类型枚举
 * system_user.status
 *
 * @since 2025-12-12
 */
@Getter
@AllArgsConstructor
public enum UserAuthTypeEnum {

    PASSWORD("password", "账号密码登录"),
    OAUTH2("oauth2", "OAuth 2.0 三方登录"),
    ;

    @EnumValue
    @NotNull
    final String code;

    @NotNull
    final String name;

}
