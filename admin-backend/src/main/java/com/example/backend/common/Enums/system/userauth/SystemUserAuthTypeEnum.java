package com.example.backend.common.Enums.system.userauth;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户认证类型枚举
 * system_user.status
 *
 * @since 2025-12-12
 */
@Getter
@AllArgsConstructor
public enum SystemUserAuthTypeEnum {

    PASSWORD("PASSWORD", "账号密码登录"),
    OAUTH2("OAUTH2", "OAuth 2.0 三方登录"),
    ;

    @EnumValue
    final String code;
    final String name;

}
