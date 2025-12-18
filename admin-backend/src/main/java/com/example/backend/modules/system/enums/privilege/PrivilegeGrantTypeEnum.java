package com.example.backend.modules.system.enums.privilege;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * 权限授予类型
 *
 * @since 2025-12-18
 */
@Getter
@AllArgsConstructor
public enum PrivilegeGrantTypeEnum {

    GRANTED("granted", "有权"),
    DENIED("denied", "无权"),
    INHERITABLE("inheritable", "有权继承"),
    ;

    @EnumValue
    @NotNull
    final String code;

    @NotNull
    final String name;

}
