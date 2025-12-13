package com.example.backend.common.Enums.system.user;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * 用户类型
 * system_user.type
 *
 * @since 2025-12-12
 */
@Getter
@AllArgsConstructor
public enum SystemUserTypeEnum {

    SUPER_USER("super_admin", "超级管理员"),
    MEMBER("member", "普通用户"),
    ;

    @EnumValue
    @NotNull
    final String code;

    @NotNull
    final String name;

}
