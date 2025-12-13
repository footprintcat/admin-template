package com.example.backend.common.Enums.system.user;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态枚举
 * system_user.status
 *
 * @since 2025-12-12
 */
@Getter
@AllArgsConstructor
public enum SystemUserStatusEnum {

    NORMAL("normal", "正常（可用）"),
    LOCKED("locked", "锁定（禁用）"),
    DISABLED("disabled", "停用"),
    EXPIRED("expired", "过期"),
    ;

    @EnumValue
    final String code;
    final String name;

}
