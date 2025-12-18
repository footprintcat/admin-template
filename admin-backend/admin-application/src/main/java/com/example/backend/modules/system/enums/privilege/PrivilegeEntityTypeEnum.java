package com.example.backend.modules.system.enums.privilege;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * 对象类型
 *
 * @since 2025-12-14
 */
@Getter
@AllArgsConstructor
public enum PrivilegeEntityTypeEnum {

    IDENTITY("identity", "身份"),
    ROLE("role", "角色"),
    ;

    @EnumValue
    @NotNull
    final String code;

    @NotNull
    final String name;

}
