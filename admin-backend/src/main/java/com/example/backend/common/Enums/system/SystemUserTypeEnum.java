package com.example.backend.common.Enums.system;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型
 * system_user.type
 *
 * @since 2025-12-12
 */
@Getter
@AllArgsConstructor
public enum SystemUserTypeEnum {

    SUPER_USER("SUPER_ADMIN", "超级管理员"),
    MEMBER("MEMBER", "普通用户"),
    ;

    @EnumValue
    final String code;
    final String name;

}